package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容管理Controller
 */
@Controller
@RequestMapping("/content")
public class ContentController {

    @Value("${REST_BASE_URL}")
    private  String REST_BASE_URL;

    @Value("${REST_CONTENT_SYNC_URL}")
    private  String REST_CONTENT_SYNC_URL;

    @Autowired
    private ContentService contentService;

    /**
     * 根据分类id查询内容列表
     * @param categoryId 分类id
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/query/list")
    @ResponseBody
    public EasyUIDataGridResult getContentById(Long categoryId, Integer page, Integer rows){
        EasyUIDataGridResult result = contentService.getContentById(categoryId, page, rows);
        return result;
    }

    /**
     * 添加内容
     * @param content
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public TaotaoResult insertContent(TbContent content){
        TaotaoResult result = contentService.insertContent(content);
        //调用taotao-rest发布的服务同步缓存
        HttpClientUtil.doGet(REST_BASE_URL+REST_CONTENT_SYNC_URL+content.getCategoryId());
        return result;
    }

    /**
     * 删除内容
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public TaotaoResult deleteContent(String[] ids){
        try {
            long[] contentIds = new long[ids.length];
            for (int i=0; i<ids.length; i++){
                contentIds[i] = Long.parseLong(ids[i]);
            }

            //获取内容分类id，用于同步缓存
            List<Long> categoryIds = contentService.getCategoryIdById(contentIds);

            TaotaoResult result = contentService.deleteContent(contentIds);

            for (Long categoryId : categoryIds){
                //调用taotao-rest发布的服务同步缓存
                HttpClientUtil.doGet(REST_BASE_URL+REST_CONTENT_SYNC_URL+categoryId);
            }

            return result;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }

    /**
     * 更新内容
     * @param content
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public TaotaoResult updateContent(TbContent content){
        try {
            TaotaoResult result = contentService.updateContent(content);
            //调用taotao-rest发布的服务同步缓存
            HttpClientUtil.doGet(REST_BASE_URL+REST_CONTENT_SYNC_URL+content.getCategoryId());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }

}
