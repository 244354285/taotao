package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.*;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {

    //自动注入itemMapper
    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;

    /**
     * 根据id查询商品信息
     * @param itemId 商品主键
     * @return item的pojo
     */
    @Override
    public TbItem getItemById(Long itemId) {
        return itemMapper.selectByPrimaryKey(itemId);
    }

    /**
     * 获取分页信息
     * @param page
     * @param rows
     * @return
     */
    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        //分页处理
        PageHelper.startPage(page,rows);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        //获取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
        //返回处理结果
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);
        return result;
    }

    /**
     * 插入商品信息
     * @param item
     * @param desc
     * @return
     */
    @Override
    public TaotaoResult createItem(TbItem item, String desc, String itemParam) {
        //生成商品id
        long itemId = IDUtils.getItemId();
        //补全TbItem属性
        item.setId(itemId);
        //商品状态，1-正常，2-下架，3-删除
        item.setStatus((byte) 1);
        //创建时间和更新时间
        Date date = new Date();
        item.setCreated(date);
        item.setUpdated(date);
        //插入商品表
        itemMapper.insert(item);
        //商品描述
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);
        //插入商品描述数据
        itemDescMapper.insert(itemDesc);
        //添加规格参数处理
        TbItemParamItem itemParamItem = new TbItemParamItem();
        itemParamItem.setItemId(itemId);
        itemParamItem.setParamData(itemParam);
        itemParamItem.setCreated(date);
        itemParamItem.setUpdated(date);
        //插入数据
        itemParamItemMapper.insert(itemParamItem);
        return TaotaoResult.ok();
    }

    /**
     * 根据id查询规格参数，展示到用户页面
     * @param itemId
     * @return
     */
    @Override
    @SuppressWarnings("all")
    public String getItemParamHtml(Long itemId) {
        //根据id查询规格参数
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        //执行查询
        List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
        if (list == null || list.isEmpty()){
            return "";
        }
        //去规格参数
        TbItemParamItem itemParamItem = list.get(0);
        //取json数据
        String paramData = itemParamItem.getParamData();
        //转换成java对象
        List<Map> mapList = JsonUtils.jsonToList(paramData,Map.class);
        //遍历list生成的html
        StringBuffer sb = new StringBuffer();
        sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"1\" class=\"Ptable\">\n");
        sb.append(" <tbody>\n");
        for(Map m1:mapList) {
            sb.append("<tr>\n");
            sb.append("<th class=\"tdTitle\" colspan=\"2\">"+m1.get("group")+"</th>\n");
            sb.append("</tr>\n");
            List<Map> list2 = (List<Map>) m1.get("params");
            for(Map m2:list2) {
                sb.append("<tr>\n");
                sb.append("<td class=\"tdTitle\">"+m2.get("k")+"</td>\n");
                sb.append("<td>"+m2.get("v")+"</td>\n");
                sb.append("</tr>\n");
            }
        }
        sb.append("</tbody>\n");
        sb.append("</table>");
        return sb.toString();
    }


    /**
     * 删除商品信息
     * @param itemId 商品id
     * @return
     */
    @Override
    public TaotaoResult deleteItem(long[] itemId) {
        try {
            for (int i=0; i<itemId.length; i++){
                //删除商品基本信息
                itemMapper.deleteByPrimaryKey(itemId[i]);
                //删除商品详情
                itemDescMapper.deleteByPrimaryKey(itemId[i]);
                //删除商品类目
                TbItemParamItemExample example = new TbItemParamItemExample();
                TbItemParamItemExample.Criteria criteria = example.createCriteria();
                criteria.andItemIdEqualTo(itemId[i]);
                //执行删除
                itemParamItemMapper.deleteByExample(example);
            }
            return TaotaoResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }

    /**
     * 更新商品信息
     * @param item
     * @param desc
     * @param itemParams
     * @return
     */
    @Override
    public TaotaoResult updateItem(TbItem item,String desc,String itemParams,Long itemParamId) {
        //更新商品基本信息
        TbItem item1 = new TbItem();
        item1.setId(item.getId());
        item1.setCid(item.getCid());
        item1.setTitle(item.getTitle());
        item1.setSellPoint(item.getSellPoint());
        item1.setPrice(item.getPrice());
        item1.setNum(item.getNum());
        item1.setBarcode(item.getBarcode());
        item1.setImage(item.getImage());
        //默认为 1.正常
        item1.setStatus((byte) 1);
        item1.setUpdated(new Date());
        itemMapper.updateByPrimaryKeySelective(item1);

        //更新商品详情
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemDesc.setUpdated(new Date());
        itemDescMapper.updateByPrimaryKeySelective(itemDesc);

        //更新商品规格参数
        TbItemParamItem itemParamItem = new TbItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setId(itemParamId);
        itemParamItem.setParamData(itemParams);
        itemParamItem.setUpdated(new Date());
        itemParamItemMapper.updateByPrimaryKeyWithBLOBs(itemParamItem);

        return TaotaoResult.ok();
    }

    /**
     * 商品下架
     * @param itemIds 商品id数组
     * @return
     */
    @Override
    public TaotaoResult instockItem(long[] itemIds) {
        List<TbItem> list = new ArrayList<>();
        for (int i=0; i<itemIds.length; i++){
            TbItem item = itemMapper.selectByPrimaryKey(itemIds[i]);
            list.add(item);
        }
        for(TbItem item1 : list){
            //商品状态，1-正常，2-下架，3-删除
            item1.setStatus((byte) 2);
            itemMapper.updateByPrimaryKeySelective(item1);
        }
        return TaotaoResult.ok();
    }

    /**
     * 商品上架
     * @param itemIds 商品id数组
     * @return
     */
    @Override
    public TaotaoResult reshelfItem(long[] itemIds) {
        List<TbItem> list = new ArrayList<>();
        for (int i=0; i<itemIds.length; i++){
            TbItem item = itemMapper.selectByPrimaryKey(itemIds[i]);
            list.add(item);
        }
        for(TbItem item1 : list){
            //商品状态，1-正常，2-下架，3-删除
            item1.setStatus((byte) 1);
            itemMapper.updateByPrimaryKeySelective(item1);
        }
        return TaotaoResult.ok();
    }
}
