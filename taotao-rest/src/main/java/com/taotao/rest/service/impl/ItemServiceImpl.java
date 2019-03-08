package com.taotao.rest.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.rest.component.JedisClient;
import com.taotao.rest.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品管理service
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    public TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_ITEM_KEY}")
    private String REDIS_ITEM_KEY;
    @Value("${ITEM_BASE_INFO_KEY}")
    private String ITEM_BASE_INFO_KEY;
    @Value("${ITEM_EXPIRE_SECOND}")
    private Integer ITEM_EXPIRE_SECOND;
    @Value("${ITEM_DESC_KEY}")
    private String ITEM_DESC_KEY;
    @Value("${ITEM_PARAM_KEY}")
    private String ITEM_PARAM_KEY;


    public TbItem getItemById(Long itemId) {
        try {
            //查询缓存,如果有缓存直接返回
            String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_BASE_INFO_KEY);
            //判断数据是否存在
            if (StringUtils.isNotBlank(json)){
                //把json数据转换成java对象
                TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
                return item;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //根据商品id查询商品信息
        TbItem item = itemMapper.selectByPrimaryKey(itemId);

        //添加缓存的原则不能影响正常业务逻辑
        try {
            //向redis中添加缓存
            jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_BASE_INFO_KEY
                    , JsonUtils.objectToJson(item));
            //设置key的过期时间
            jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_BASE_INFO_KEY,ITEM_EXPIRE_SECOND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    public TbItemDesc geItemDescById(Long itemId) {
        try {
            //查询缓存,如果有缓存直接返回
            String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_DESC_KEY);
            //判断数据是否存在
            if (StringUtils.isNotBlank(json)){
                //把json数据转换成java对象
                TbItemDesc itemDesc = (TbItemDesc) JsonUtils.jsonToList(json,TbItemDesc.class);
                return itemDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);

        //添加缓存的原则不能影响正常业务逻辑
        try {
            //向redis中添加缓存
            jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_DESC_KEY
                    , JsonUtils.objectToJson(itemDesc));
            //设置key的过期时间
            jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_DESC_KEY ,ITEM_EXPIRE_SECOND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemDesc;
    }

    public TbItemParamItem getItemParam(Long itemId){
        //查询缓存
        try {
            String json = jedisClient.get(REDIS_ITEM_KEY +":"+ itemId +":"+ ITEM_PARAM_KEY);
            //判断数据是否存在
            if (StringUtils.isNotBlank(json)){
                //把json转换成java对象
                TbItemParamItem itemParamItem = JsonUtils.jsonToPojo(json, TbItemParamItem.class);
                return itemParamItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //根据商品id查询商品参数
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        //查询数据
        List<TbItemParamItem> itemParamItems = itemParamItemMapper.selectByExampleWithBLOBs(example);
        if (itemParamItems != null && itemParamItems.size() > 0){
            TbItemParamItem itemParamItem = itemParamItems.get(0);
            //添加缓存
            try {
                jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_PARAM_KEY, JsonUtils.objectToJson(itemParamItem));
                jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_PARAM_KEY,ITEM_EXPIRE_SECOND);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return itemParamItem;
        }
        return null;
    }

    /**
     * 执行缓存同步，删除缓存中的商品信息
     * @param itemId 商品id
     * @return TaotaoResult
     */
    public TaotaoResult syncItem(Long itemId) {
        //删除缓存中的商品信息
        jedisClient.del(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_BASE_INFO_KEY);
        //删除缓存中的商品详情
        jedisClient.del(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_DESC_KEY);
        //删除缓存中的商品类目
        jedisClient.del(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_PARAM_KEY);
        return TaotaoResult.ok();
    }
}
