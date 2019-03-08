package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

/**
 * 商品类目service接口
 */
public interface ItemCatService {

    /**
     * 根据parentIdc查询一级类目
     * @param parentId
     * @return
     */
    List<EasyUITreeNode> getItemCatList(long parentId);

    /**
     * 创建分类
     * @param parentId 父id
     * @param name 创建的分类名称
     * @return TaotaoResult
     */
    TaotaoResult createItemCat(Long parentId,String name);

    /**
     * 更新商品类目
     * @param id 商品类目id
     * @param name 商品类目名称
     * @return TaotaoResult
     */
    TaotaoResult updateItemCat(Long id,String name);

    /**
     * 删除商品类目
     * @param id 类目id
     * @return
     */
    TaotaoResult deleteItemCat(Long id);
}
