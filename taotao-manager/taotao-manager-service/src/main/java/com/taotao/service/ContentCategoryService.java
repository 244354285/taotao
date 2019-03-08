package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

/**
 * 内容分类service
 */
public interface ContentCategoryService {

    /**
     * 获取内容分类列表
     * @param parentId 父分类id
     * @return
     */
    List<EasyUITreeNode> getContentCatList(Long parentId);

    /**
     * 添加内容分类
     * @param parentId 父分类id
     * @param name 内容分类名称
     * @return
     */
    TaotaoResult insertCategory(Long parentId,String name);

    /**
     * 删除内容分类
     * @param id 父分类id
     * @return
     */
    TaotaoResult deleteCategory(Long id);

    /**
     * 更新内容分类
     * @param id 内容分类id
     * @param name 内容分类名称
     * @return
     */
    TaotaoResult updateCategory(Long id,String name);
}
