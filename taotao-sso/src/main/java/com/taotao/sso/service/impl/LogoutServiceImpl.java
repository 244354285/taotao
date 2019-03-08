package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.component.JedisClient;
import com.taotao.sso.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出登录service
 */
@Service
public class LogoutServiceImpl implements LogoutService {

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_SESSION_KEY}")
    private String REDIS_SESSION_KEY;

    @Override
    public TaotaoResult logout(HttpServletRequest request, HttpServletResponse response, String token) {
        //清除cookie
        CookieUtils.deleteCookie(request,response,"TT_TOKEN");
        //清除缓存
        jedisClient.del(REDIS_SESSION_KEY+":"+token);
        return TaotaoResult.ok();
    }
}
