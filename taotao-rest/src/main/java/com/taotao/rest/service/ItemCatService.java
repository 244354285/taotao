package com.taotao.rest.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.rest.pojo.ItemCatResult;

public interface ItemCatService {

    ItemCatResult getItemCatList();

    void deleteItemCat();
}
