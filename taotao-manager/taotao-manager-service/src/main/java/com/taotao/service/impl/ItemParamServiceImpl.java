package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.ParamResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.ParamResultMapper;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.*;
import com.taotao.pojo.TbItemParamExample.Criteria;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ItemParamServiceImpl implements ItemParamService {

    @Autowired
    private TbItemParamMapper itemParamMapper;

    @Autowired
    private ParamResultMapper paramResultMapper;

    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;

    /**
     * 获取商品规格
     * @return
     */
    @Override
    public EasyUIDataGridResult getItemParamList(int page, int rows) {
        //分页处理
        PageHelper.startPage(page,rows);
        //执行查询
        List<ParamResult> list = paramResultMapper.getParam();
        //获取分页信息
        PageInfo<ParamResult> pageInfo = new PageInfo<ParamResult>(list);
        //返回处理结果
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);
        return result;
    }

    @Override
    public TaotaoResult insertItemParam(Long cid, String paramData) {
        TbItemParam itemParam = new TbItemParam();
        itemParam.setItemCatId(cid);
        itemParam.setParamData(paramData);
        itemParam.setCreated(new Date());
        itemParam.setUpdated(new Date());
        itemParamMapper.insert(itemParam);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult getItemParamByCid(long cid) {
        TbItemParamExample example = new TbItemParamExample();
        Criteria criteria = example.createCriteria();
        criteria.andItemCatIdEqualTo(cid);
        List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);
        //判断是否查询到结果
        if (list != null && list.size() > 0) {
            return TaotaoResult.ok(list.get(0));
        }
        return TaotaoResult.ok();
    }

    /**
     * 获取商品规格
     * @param itemId
     * @return
     */
    @Override
    public TaotaoResult getItemParamById(Long itemId) {
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        //执行查询
        List<TbItemParamItem> itemParamItems = itemParamItemMapper.selectByExampleWithBLOBs(example);
        //判断查询结果是否有值
        if (itemParamItems != null && itemParamItems.size() > 0){
            //有值返回
            return TaotaoResult.ok(itemParamItems.get(0));
        }else {
            //没有值为空
            return null;
        }
    }

    /**
     * 删除商品规格
     */
    @Override
    public TaotaoResult deleteItemParams(long[] itemParamId) {
		List<TbItemParam> list = new ArrayList<>();
		for(int i=0;i<itemParamId.length;i++){
			TbItemParam itemParam = itemParamMapper.selectByPrimaryKey(itemParamId[i]);
			list.add(itemParam);
		}
		for(TbItemParam itemParam : list){
			itemParamMapper.deleteByPrimaryKey(itemParam.getId());
		}
        return TaotaoResult.ok();
    }



}
