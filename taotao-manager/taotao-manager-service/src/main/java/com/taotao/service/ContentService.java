package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

public interface ContentService {

    /**
     * 根据categoryId查询内容
     * @param categoryId 内容分类id
     * @return
     */
    EasyUIDataGridResult getContentById(Long categoryId,Integer page, Integer rows);

    /**
     * 添加内容
     * @param content
     * @return
     */
    TaotaoResult insertContent(TbContent content);

    /**
     * 删除内容
     * @param ids
     * @return
     */
    TaotaoResult deleteContent(long[] ids);

    /**
     * 更新内容
     * @param content
     * @return
     */
    TaotaoResult updateContent(TbContent content);

    /**
     * 根据id获取CategoryId
     */
    List<Long> getCategoryIdById(long[] ids);
}
