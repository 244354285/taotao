package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * 注册service
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TbUserMapper userMapper;

    /**
     * 校验注册数据
     * @param param
     * @param type
     * @return
     */
    @Override
    public TaotaoResult checkData(String param, int type) {
        //根据数据类型检查数据
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //1、2、3分别代表username、phone、email
        if (1 == type){
            criteria.andUsernameEqualTo(param);
        }else if(2 == type){
            criteria.andPhoneEqualTo(param);
        }else if(3 == type){
            criteria.andEmailEqualTo(param);
        }
        //执行查询
        List<TbUser> list = userMapper.selectByExample(example);
        //判断查询结果是否为空
        if (list == null || list.isEmpty()){
            return TaotaoResult.ok(true);
        }
        return TaotaoResult.ok(false);
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @return
     */
    @Override
    public TaotaoResult register(TbUser user) {
        //校验数据
        //校验用户名和密码不能为空
        if (StringUtils.isBlank(user.getUsername())
            || StringUtils.isBlank(user.getPassword())){
            return TaotaoResult.build(400,"用户名或密码不能为空");
        }
        //校验数据是否重复
        //校验用户名
        TaotaoResult result = checkData(user.getUsername(),1);
        if (!(boolean) result.getData()){
            return TaotaoResult.build(400,"用户名已存在");
        }
        //校验手机号
        if (user.getPhone() != null){
            result = checkData(user.getPassword(),2);
            if (!(boolean) result.getData()){
                return TaotaoResult.build(400,"手机号已存在");
            }
        }
        //校验邮箱
        if (user.getEmail() != null){
            result = checkData(user.getEmail(),3);
            if (!(boolean) result.getData()){
                return TaotaoResult.build(400,"邮箱已存在");
            }
        }
        //插入数据
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //密码进行MD5加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        userMapper.insert(user);
        return TaotaoResult.ok();
    }
}
