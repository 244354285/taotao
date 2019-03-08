package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台首页展示
 */
@Controller
public class PageController {

    /**
     * 默认进入index页面
     * @return
     */
    @RequestMapping("/")
    public String showIndex(){
        return "index";
    }

    /**
     * 获取页面
     * @param page
     * @return
     */
    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page){
        return page;
    }

    /**
     * 进入商品信息编辑页面
     */
    @RequestMapping("/page/item-edit")
    public String showItemEditPage(){
        return "item-edit";
    }
}
