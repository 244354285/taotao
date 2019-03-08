package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.sso.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出登录controller
 */
@Controller
public class LogoutController {

    @Autowired
    private LogoutService logoutService;

    @RequestMapping("/user/logout/{token}")
    @ResponseBody
    public TaotaoResult logout(HttpServletRequest request, HttpServletResponse response,@PathVariable String token){
        try {
            TaotaoResult result = logoutService.logout(request, response, token);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.ok();
        }
    }

}
