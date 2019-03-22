package com.taotao.search.service;

import com.taotao.common.pojo.TaotaoResult;

public interface ItemService {

    TaotaoResult importItem() throws Exception;

    void deleteAll() throws Exception;
}
