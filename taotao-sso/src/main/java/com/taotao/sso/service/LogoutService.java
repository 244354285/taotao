package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LogoutService {

    TaotaoResult logout(HttpServletRequest request, HttpServletResponse response,String token);
}
