package com.taotao.portal.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.portal.service.StaticPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 生成静态页面controller
 */
@Controller
public class StaticPageController {
    @Autowired
    private StaticPageService staticPageService;

    @RequestMapping("/gen/item/{itemId}")
    @ResponseBody
    public TaotaoResult genItemPage(@PathVariable Long itemId){
        try {
            TaotaoResult result = staticPageService.genItemHtml(itemId);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }
}
