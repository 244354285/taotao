package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.component.JedisClient;
import com.taotao.sso.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * 登录service
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Value("${REDIS_SESSION_KEY}")
    private String REDIS_SESSION_KEY;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private JedisClient jedisClient;

    @Override
    public TaotaoResult login(String username, String password, HttpServletRequest request,
                              HttpServletResponse response) {
        //校验用户名密码是否正确
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = userMapper.selectByExample(example);
        //获取用户信息
        if (list == null || list.isEmpty()){
            return TaotaoResult.build(400,"用户名不存在");
        }
        TbUser user = list.get(0);
        //校验密码
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
            return TaotaoResult.build(400,"用户名或密码错误");
        }
        //登录成功
        //生成token
        String token = UUID.randomUUID().toString();
        //把用户信息写入redis
        //key:REDIS_SESSION:{TOKEN}
        //value:user转json
        user.setPassword(null);
        try {
            jedisClient.set(REDIS_SESSION_KEY+":"+token, JsonUtils.objectToJson(user));
            //设置session过期时间
            jedisClient.expire(REDIS_SESSION_KEY+":"+token,SESSION_EXPIRE);
        }catch (Exception e){
            return TaotaoResult.build(400, "出错啦");
        }

        //写cookie
        CookieUtils.setCookie(request,response,"TT_TOKEN",token);
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        //根据token取用户信息
        String json = jedisClient.get(REDIS_SESSION_KEY+":"+token);
        //判断是否查询到结果
        if (StringUtils.isBlank(json)){
            return TaotaoResult.build(400,"用户session已过期");
        }
        //把json转换成java对象
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        //更新session的过期时间
        jedisClient.expire(REDIS_SESSION_KEY+":"+token,SESSION_EXPIRE);
        return TaotaoResult.ok(user);
    }
}
