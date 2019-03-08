package com.taotao.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容管理service
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    /**
     * 根据categoryId查询内容
     * @param categoryId 内容分类id
     * @return
     */
    @Override
    public EasyUIDataGridResult getContentById(Long categoryId,Integer page, Integer rows) {
        //分页处理
        PageHelper.startPage(page,rows);
        //执行查询
        TbContentExample example = new TbContentExample();
        example.createCriteria().andCategoryIdEqualTo(categoryId);
        List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
        //获取分页结果
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        //返回处理结果
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);

        return result;
    }

    /**
     * 添加内容
     * @param content
     * @return
     */
    public TaotaoResult insertContent(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //插入数据
        contentMapper.insert(content);
        return TaotaoResult.ok();
    }

    /**
     * 删除内容
     * @param ids
     * @return
     */
    @Override
    public TaotaoResult deleteContent(long[] ids) {
        //获取ids数组的每一个元素
        for(int i=0; i<ids.length; i++){
            contentMapper.deleteByPrimaryKey(ids[i]);
        }
        return TaotaoResult.ok();
    }

    /**
     * 更新内容
     * @param content
     * @return
     */
    @Override
    public TaotaoResult updateContent(TbContent content) {
        //创建内容对象
        TbContent content1 = new TbContent();
        content1.setId(content.getId());
        content1.setCategoryId(content.getCategoryId());
        content1.setTitle(content.getTitle());
        content1.setSubTitle(content.getSubTitle());
        content1.setTitleDesc(content.getTitleDesc());
        content1.setUrl(content.getUrl());
        content1.setPic(content.getPic());
        content1.setPic2(content.getPic2());
        content1.setContent(content.getContent());
        content1.setUpdated(new Date());

        //执行更新
        contentMapper.updateByPrimaryKeySelective(content1);
        return TaotaoResult.ok();
    }

    /**
     * 根据内容id获取内容分类id
     * @param ids
     * @return
     */
    @Override
    public List<Long> getCategoryIdById(long[] ids) {
        List<Long> list = new ArrayList<>();
        for (int i=0; i<ids.length; i++){
            TbContent content = contentMapper.selectByPrimaryKey(ids[i]);
            list.add(content.getCategoryId());
        }
        return list;
    }

}
