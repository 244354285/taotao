package com.taotao.rest.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;

public interface ItemService {

    TbItem getItemById(Long itemId);

    TbItemDesc geItemDescById(Long itemId);

    TbItemParamItem getItemParam(Long itemId);

    TaotaoResult syncItem(Long itemId);
}
