package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品类目控制层
 */
@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${REST_ITEMCAT_URL}")
    private String REST_ITEMCAT_URL;
    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("/list")
    @ResponseBody
    public List<EasyUITreeNode> getItemCatList(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        List<EasyUITreeNode> list = itemCatService.getItemCatList(parentId);
        return list;
    }

    /**
     * 插入商品类目
     *
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    public TaotaoResult createItemCat(Long parentId, String name) {
        try {
            //调用service服务创建类目
            itemCatService.createItemCat(parentId, name);

            //调用taotao-rest服务同步缓存
            syncItemCat();

            return TaotaoResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }

    /**
     * 更新商品类目
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public TaotaoResult updateItemCat(Long id, String name) {
        try {
            //调用service服务创建类目
            itemCatService.updateItemCat(id, name);

            //调用taotao-rest服务同步缓存
            syncItemCat();

            return TaotaoResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }

    /**
     * 删除商品类目
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public TaotaoResult deleteItemCat(Long id){
        try {
            //调用service服务创建类目
            itemCatService.deleteItemCat(id);

            //调用taotao-rest服务同步缓存
            syncItemCat();

            return TaotaoResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }



    /**
     * 同步商品类目缓存
     */
    private void syncItemCat() {
        //调用taotao-rest服务同步缓存
        HttpClientUtil.doGet(REST_BASE_URL + REST_ITEMCAT_URL);
    }
}