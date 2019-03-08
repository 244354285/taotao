package com.taotao.portal.controller;

import com.taotao.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户Controller
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户退出登录
     * @return
     */
    @RequestMapping("/user/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        userService.logout(request,response);
        return "redirect:/index.html";
    }
}
