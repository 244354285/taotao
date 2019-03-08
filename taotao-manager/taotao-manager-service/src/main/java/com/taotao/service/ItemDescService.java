package com.taotao.service;

import com.taotao.common.pojo.TaotaoResult;

public interface ItemDescService {

    /**
     * 根据商品id获取商品描述
     */
    TaotaoResult getItemDescById(Long itemId);
}
