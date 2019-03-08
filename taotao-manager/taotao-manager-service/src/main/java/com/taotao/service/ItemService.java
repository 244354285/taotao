package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface ItemService {

    TbItem getItemById(Long itemId);

    /**
     * 分页查询商品列表
     * @param page
     * @param rows
     * @return
     */
    EasyUIDataGridResult getItemList(int page,int rows);

    /**
     * 插入商品信息
     * @param item
     * @param desc
     * @return
     */
    TaotaoResult createItem(TbItem item,String desc,String itemParam);

    /**
     * 获取商品规格模板
     * @param itemId
     * @return
     */
    String getItemParamHtml(Long itemId);


    /**
     * 删除商品信息
     */
    TaotaoResult deleteItem(long[] itemId);

    /**
     * 更新商品
     */
    TaotaoResult updateItem(TbItem item,String desc,String itemParams,Long itemParamId);

    /**
     * 商品下架
     */
    TaotaoResult instockItem(long[] itemIds);

    /**
     * 商品上架
     */
    TaotaoResult reshelfItem(long[] itemIds);
}
