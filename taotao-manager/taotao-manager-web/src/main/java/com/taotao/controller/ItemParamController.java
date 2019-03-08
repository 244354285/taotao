package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/item/param")
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    @RequestMapping("/query/itemcatid/{itemCatId}")
    @ResponseBody
    public TaotaoResult getItemParamByCid(@PathVariable Long itemCatId) {
        TaotaoResult result = itemParamService.getItemParamByCid(itemCatId);
        return result;
    }

    @RequestMapping("/list")
    @ResponseBody
    public EasyUIDataGridResult getItemParamList(Integer page, Integer rows){
        EasyUIDataGridResult result = itemParamService.getItemParamList(page,rows);
        System.out.println("-----------------"+result.getRows().get(1).toString()+"------------");
        return result;
    }

    @RequestMapping("/save/{cid}")
    @ResponseBody
    public TaotaoResult insertItemParam(@PathVariable Long cid,String paramData){
        TaotaoResult result = itemParamService.insertItemParam(cid, paramData);
        return result;
    }

    @RequestMapping("/item/query/{itemId}")
    @ResponseBody
    public TaotaoResult showItemParamForEdit(@PathVariable Long itemId){
        TaotaoResult result = itemParamService.getItemParamById(itemId);
        return result;
    }
}
