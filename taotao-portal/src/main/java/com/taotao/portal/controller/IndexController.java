package com.taotao.portal.controller;

import com.taotao.portal.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/index")
    public String showIndex(ModelMap modelMap){
        //获取轮播图
        String json = contentService.getAd1List();
        System.out.println(json);
        //传递给页面
        modelMap.addAttribute("adad",json);
        return "index";
    }

    @RequestMapping(value = "/posttest",method = RequestMethod.POST)
    @ResponseBody
    public String postTest(String name,String pass){
        System.out.println(name);
        System.out.println(pass);
        return "OK";
    }


}
