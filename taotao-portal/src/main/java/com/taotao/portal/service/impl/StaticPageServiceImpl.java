package com.taotao.portal.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.pojo.TbItem;
import com.taotao.portal.pojo.PortalItem;
import com.taotao.portal.service.ItemService;
import com.taotao.portal.service.StaticPageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Service
public class StaticPageServiceImpl implements StaticPageService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${STATIC_PAGE_PATH}")
    private String STATIC_PAGE_PATH;

    @Override
    public TaotaoResult genItemHtml(Long itemId){
        Writer out = null;
        try {
            //商品基本信息
            TbItem item = itemService.getItemById(itemId);
            //商品描述
            String itemDesc = itemService.getItemDescById(itemId);
            //规格参数
            String itemParam = itemService.getItemParamById(itemId);
            //生成静态页面
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            //创建数据集
            Map root = new HashMap();
            //向数据集中添加属性
            root.put("item",item);
            root.put("itemDesc",itemDesc);
            root.put("itemParam",itemParam);
            //创建一个Writer对象
            out = new FileWriter(new File(STATIC_PAGE_PATH+itemId+".html"));
            //生成静态文件
            template.process(root,out);
            out.flush();
            return TaotaoResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }  finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
