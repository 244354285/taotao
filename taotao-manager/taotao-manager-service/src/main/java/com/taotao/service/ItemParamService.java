package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;

public interface ItemParamService {

    /**
     * 查询规格
     */
    EasyUIDataGridResult getItemParamList(int page, int rows);

    TaotaoResult insertItemParam(Long cid,String paramData);

    TaotaoResult getItemParamByCid(long cid);

    TaotaoResult getItemParamById(Long itemId);

    TaotaoResult deleteItemParams(long[] itemParamIds);
}
