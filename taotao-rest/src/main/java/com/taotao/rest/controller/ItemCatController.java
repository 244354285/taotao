package com.taotao.rest.controller;

import com.taotao.common.utils.JsonUtils;
import com.taotao.rest.pojo.ItemCatResult;
import com.taotao.rest.service.ItemCatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品分类查询服务
 */
@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping(value="/list",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody String getItemCatList(String callback){
        ItemCatResult result = itemCatService.getItemCatList();
        if (StringUtils.isBlank(callback)){
            //需要把result转换成字符串
            String json = JsonUtils.objectToJson(result);
            return json;
        }
        //如果字符串不为空，需要支持jsonp调用
        //需要把result转换成字符串
        String json = JsonUtils.objectToJson(result);
        return callback + "("+json+");";
    }

    /**
     * 同步商品类目
     */
    @RequestMapping("/sync")
    public void syncItemCat(){
        itemCatService.deleteItemCat();
    }
}
