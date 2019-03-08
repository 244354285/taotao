package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service.ItemDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品详情controller
 */
@Controller
public class ItemDescController {

    @Autowired
    private ItemDescService itemDescService;

    /**
     * 商品详情回显到编辑页面
     */
    @RequestMapping("/item/query/item/desc/{itemId}")
    @ResponseBody
    public TaotaoResult showItemDesc(@PathVariable Long itemId){
        TaotaoResult result = itemDescService.getItemDescById(itemId);
        return result;
    }
}
