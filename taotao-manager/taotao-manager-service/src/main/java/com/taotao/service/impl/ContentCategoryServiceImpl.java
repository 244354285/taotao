package com.taotao.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类管理service
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    public TbContentCategoryMapper contentCategoryMapper;

    @Override
    @SuppressWarnings("all")
    public List<EasyUITreeNode> getContentCatList(Long parentId) {
        //根据parentId查询子节点列表
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        //转换成EasyUITreeNode列表
        List<EasyUITreeNode> resultList = new ArrayList<EasyUITreeNode>();
        for(TbContentCategory tbContentCategory : list){
            //创建一个EasyUITreeNode节点
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            //添加到列表中
            resultList.add(node);
        }
        return resultList;
    }

    /**
     * 插入内容分类
     * @param parentId
     * @param name
     * @return
     */
    public TaotaoResult insertCategory(Long parentId, String name) {
        //查询父节点
        TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);

        //创建一个pojo对象
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setName(name);
        contentCategory.setParentId(parentId);
        //1、正常，2、删除
        contentCategory.setStatus(1);
        //判断父分类是否为一级目录
        if (parentNode.getParentId() == 0){
            //是一级目录，直接设置此分类为目录分类
            contentCategory.setIsParent(true);
        }else{
            contentCategory.setIsParent(false);
        }
        //排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
        contentCategory.setSortOrder(1);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        contentCategoryMapper.insert(contentCategory);
        //取返回的主键
        Long id = contentCategory.getId();

        //判断父节点的isparent属性
        if (!parentNode.getIsParent()){
            parentNode.setIsParent(true);
            parentNode.setUpdated(new Date());
            //更新父节点
            contentCategoryMapper.updateByPrimaryKey(parentNode);
        }
        return TaotaoResult.ok();
    }

    /**
     * 删除内容分类
     * @param id 分类id
     * @return
     */
    @Override
    public TaotaoResult deleteCategory(Long id) {
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        //判断是否是父分类
        if (contentCategory.getIsParent() == true){
            //如果是父分类
            TbContentCategoryExample example = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(id);
            //查询父分类id为此id的分类
            List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
            //遍历子分类
            for(TbContentCategory contentCategory1 : list){
                //删除子分类
                contentCategoryMapper.deleteByPrimaryKey(contentCategory1.getId());
            }
        }
        //删除此分类
        contentCategoryMapper.deleteByPrimaryKey(id);

        //判断父分类还有没有子分类，如果没有重新设置isParent属性为false
        Long parentId = contentCategory.getParentId();
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //查询是否还有子分类
        List<TbContentCategory> childNodes = contentCategoryMapper.selectByExample(example);
        if (childNodes.size() <= 0){
            TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);
            parentNode.setIsParent(false);
            parentNode.setUpdated(new Date());
            contentCategoryMapper.updateByPrimaryKey(parentNode);
        }
        return TaotaoResult.ok();
    }

    /**
     * 更新内容分类
     * @param id 内容分类id
     * @param name 内容分类名称
     * @return
     */
    @Override
    public TaotaoResult updateCategory(Long id, String name) {
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setId(id);
        contentCategory.setName(name);
        contentCategory.setUpdated(new Date());
        //调用mapper执行更新
        contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        return TaotaoResult.ok();
    }

}
