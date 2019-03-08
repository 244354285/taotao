package com.taotao.rest.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.rest.component.JedisClient;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.ItemCatResult;
import com.taotao.rest.service.ItemCatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页分类展示服务
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_ITEMCAT_KEY}")
    private String REDIS_ITEMCAT_KEY;

    public ItemCatResult getItemCatList() {

        try {
            //查询缓存，如果缓存中有，直接返回
            String json = jedisClient.get(REDIS_ITEMCAT_KEY);
            if (StringUtils.isNotBlank(json)){
                ItemCatResult result = JsonUtils.jsonToPojo(json, ItemCatResult.class);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //调用递归方法查询商品分类列表
        List catList = getItemCatList(0L);
        ItemCatResult result = new ItemCatResult();
        result.setData(catList);

        try {
            //向redis中添加缓存
            jedisClient.set(REDIS_ITEMCAT_KEY, JsonUtils.objectToJson(result));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    private List getItemCatList(Long parentId){
        //根据parentId查询列表
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbItemCat> list = itemCatMapper.selectByExample(example);
        List resultList = new ArrayList();
        int index = 0;
        for(TbItemCat itemCat : list){
            //判断元素是否大于14个
            if(index >= 14){
                break;
            }
            //如果时父节点
            if (itemCat.getIsParent()){
                CatNode node = new CatNode();
                node.setUrl("/products/"+itemCat.getId()+"html");
                if (itemCat.getParentId() == 0){
                    node.setName("<a href='/products/"+itemCat.getId()+".html'>"+itemCat.getName()+"</a>");
                    //第一级节点不能超过14个元素，index为计数器
                    index++;
                }else{
                    node.setName(itemCat.getName());
                }
                node.setItems(getItemCatList(itemCat.getId()));
                //把node添加到列表中
                resultList.add(node);
            }else {
                //如果是叶子节点
                String item = "/products/"+itemCat.getId()+".html|"+itemCat.getName()+"";
                resultList.add(item);
            }
        }
        return resultList;
    }

    /**
     * 删除商品类目的redis缓存
     * @return
     */
    public void deleteItemCat() {
        jedisClient.del(REDIS_ITEMCAT_KEY);
    }
}
