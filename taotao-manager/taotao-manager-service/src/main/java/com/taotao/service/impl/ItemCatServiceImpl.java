package com.taotao.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.*;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品分类管理
 */
@Service
public class ItemCatServiceImpl implements ItemCatService{

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @SuppressWarnings("all")
    public List<EasyUITreeNode> getItemCatList(long parentId) {
        //根据parentId查询分类列表
        TbItemCatExample example = new TbItemCatExample();
        //设置查询条件
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbItemCat> list = itemCatMapper.selectByExample(example);
        //转换成EasyUITreeNode列表
        List<EasyUITreeNode> resultList = new ArrayList<EasyUITreeNode>();
        for (TbItemCat tbItemCat : list) {
            //创建一个节点对象
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getIsParent()?"closed":"open");
            //添加到列表中
            resultList.add(node);
        }
        return resultList;
    }

    /**
     * 创建分类
     * @param parentId 父id
     * @param name 创建的分类名称
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult createItemCat(Long parentId, String name) {
        //查询父类目
        TbItemCat parentNode = itemCatMapper.selectByPrimaryKey(parentId);
        TbItemCat itemCat = new TbItemCat();
        itemCat.setName(name);
        //是否为父类目 是为true，否为false
        if (parentNode.getParentId() == 0){
            itemCat.setIsParent(true);
        }else {
            itemCat.setIsParent(false);
        }
        itemCat.setParentId(parentId);
        //状态。可选值:1(正常),2(删除)
        itemCat.setStatus(1);
        itemCat.setSortOrder(1);
        itemCat.setCreated(new Date());
        itemCat.setUpdated(new Date());

        //执行插入
        itemCatMapper.insert(itemCat);

        //获取返回的主键
        Long id = itemCat.getId();

        //判断父节点的isparent属性
        if (!parentNode.getIsParent()){
            parentNode.setIsParent(true);
            parentNode.setUpdated(new Date());
            //更新父节点
            itemCatMapper.updateByPrimaryKey(parentNode);
        }

        return TaotaoResult.ok();
    }

    /**
     * 更新商品类目名称
     * @param id 商品类目id
     * @param name 商品类目名称
     * @return
     */
    @Override
    public TaotaoResult updateItemCat(Long id, String name) {
        TbItemCat itemCat = new TbItemCat();
        itemCat.setId(id);
        itemCat.setName(name);
        itemCat.setUpdated(new Date());
        //调用mapper执行更新
        itemCatMapper.updateByPrimaryKeySelective(itemCat);
        return TaotaoResult.ok();
    }

    /**
     * 删除商品类目
     * @param id 类目id
     * @return
     */
    @Override
    public TaotaoResult deleteItemCat(Long id) {
        TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(id);
        //判断删除的类目是否为父类目
        if (itemCat.getIsParent() == true){
            //如果是父类目
            TbItemCatExample example = new TbItemCatExample();
            TbItemCatExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(id);
            List<TbItemCat> list = itemCatMapper.selectByExample(example);
            //遍历子类目
            for (TbItemCat itemCat1 : list){
                //删除子类目
                itemCatMapper.deleteByPrimaryKey(itemCat1.getId());
            }
        }
        //删除此类目
        itemCatMapper.deleteByPrimaryKey(id);

        //判断父类目还有没有子类目，如果没有重新设置isParent属性为false
        Long parentId = itemCat.getParentId();
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //查询是否还有子类目
        List<TbItemCat> childNodes = itemCatMapper.selectByExample(example);
        if (childNodes.size() <= 0){
            //查询出父类目
            TbItemCat parentNode = itemCatMapper.selectByPrimaryKey(parentId);
            parentNode.setIsParent(false);
            parentNode.setUpdated(new Date());
            itemCatMapper.updateByPrimaryKey(parentNode);
        }

        return TaotaoResult.ok();
    }
}
