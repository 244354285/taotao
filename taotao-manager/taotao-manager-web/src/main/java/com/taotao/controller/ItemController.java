package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * item控制层
 */
@Controller
public class ItemController {

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${REST_ITEM_SYNC_URL}")
    private String REST_ITEM_SYNC_URL;
    @Value("${SEARCH_BASE_URL}")
    private String SEARCH_BASE_URL;
    @Value("${SEARCH_ITEM_SYNC_URL}")
    private String SEARCH_ITEM_SYNC_URL;

    //注入itemService
    @Autowired
    private ItemService itemService;

    /**
     * 根据id查询商品
     * @param itemId
     * @return
     */
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId){
        TbItem item = itemService.getItemById(itemId);
        return item;
    }

    /**
     * 获取所有商品信息
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page,Integer rows){
        EasyUIDataGridResult result = itemService.getItemList(page,rows);
        return result;
    }

    /**
     * 插入商品
     */
    @RequestMapping(value="item/save",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createItem(TbItem item,String desc,String itemParams){
        TaotaoResult result = itemService.createItem(item,desc,itemParams);
        //同步搜索索引库
        syncItemToSolr();
        return result;
    }

    @RequestMapping("/page/item/{itemId}")
    public String showItemParam(@PathVariable Long itemId, Model model){
        String html = itemService.getItemParamHtml(itemId);
        model.addAttribute("html",html);
        return "itemparam";
    }


    /**
     * 删除商品
     */
    @RequestMapping(value = "/item/delete",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteItem(String[] ids){
        long[] itemIds = new long[ids.length];
        for (int i=0; i<ids.length; i++){
            itemIds[i]= Long.parseLong(ids[i]);
        }
        TaotaoResult result = itemService.deleteItem(itemIds);

        //调用taotao-rest发布的服务同步缓存
        for (int i=0;i<itemIds.length; i++){
            HttpClientUtil.doGet(REST_BASE_URL+REST_ITEM_SYNC_URL+itemIds[i]);
        }

        //同步搜索索引库
        syncItemToSolr();
        return result;
    }

    /**
     * 编辑商品
     */
    @RequestMapping(value = "/item/update",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateItem(TbItem item,String desc,String itemParams,Long itemParamId){
        //调用service服务进行更新数据
        TaotaoResult result = itemService.updateItem(item, desc, itemParams,itemParamId);

        //调用taotao-rest发布的服务同步缓存
        HttpClientUtil.doGet(REST_BASE_URL+REST_ITEM_SYNC_URL+item.getId());

        //同步搜索索引库
        syncItemToSolr();
        return result;
    }


    /**
     * 商品下架
     */
    @RequestMapping(value = "/item/instock",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult instockItem(String[] ids){
        long[] itemIds = new long[ids.length];
        for (int i=0; i<ids.length; i++){
            itemIds[i]= Long.parseLong(ids[i]);
        }
        TaotaoResult result = itemService.instockItem(itemIds);

        //调用taotao-rest发布的服务同步缓存
        for (int i=0;i<itemIds.length; i++){
            HttpClientUtil.doGet(REST_BASE_URL+REST_ITEM_SYNC_URL+itemIds[i]);
        }

        //同步搜索索引库
        syncItemToSolr();
        return result;
    }

    /**
     * 商品上架
     */
    @RequestMapping(value = "/item/reshelf",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult reshelfItem(String[] ids){
        long[] itemIds = new long[ids.length];
        for (int i=0; i<ids.length; i++){
            itemIds[i]= Long.parseLong(ids[i]);
        }
        TaotaoResult result = itemService.reshelfItem(itemIds);

        //同步搜索索引库
        syncItemToSolr();
        return result;
    }

    /**
     * 同步商品信息到solr索引库
     */
    public void syncItemToSolr(){
        HttpClientUtil.doGet(SEARCH_BASE_URL+SEARCH_ITEM_SYNC_URL);
    }

}
