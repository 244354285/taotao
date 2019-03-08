package com.taotao.portal.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.portal.pojo.PortalItem;
import com.taotao.portal.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 查询商品信息服务
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${REST_ITEM_BASE_URL}")
    private String REST_ITEM_BASE_URL;
    @Value("${REST_ITEM_DESC_URL}")
    private String REST_ITEM_DESC_URL;
    @Value("${REST_ITEM_PARAM_URL}")
    private String REST_ITEM_PARAM_URL;

    @Override
    public TbItem getItemById(Long itemId) {
        try {
            //根据商品id查询商品基本信息
            String json = HttpClientUtil.doGet(REST_BASE_URL + REST_ITEM_BASE_URL + itemId);
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, PortalItem.class);
            PortalItem item = (PortalItem) taotaoResult.getData();
            return item;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getItemDescById(Long itemId) {
        try {
            //根据商品id调用taotao-rest服务获得数据
            String json = HttpClientUtil.doGet(REST_BASE_URL + REST_ITEM_DESC_URL + itemId);
            //转换成java对象
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbItemDesc.class);
            //获取商品描述信息
            TbItemDesc itemDesc = (TbItemDesc) taotaoResult.getData();
            String desc = itemDesc.getItemDesc();
            return desc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("all")
    public String getItemParamById(Long itemId) {
        try {
            //根据商品id获得对应的规格参数
            String json = HttpClientUtil.doGet(REST_BASE_URL + REST_ITEM_PARAM_URL + itemId);
            //转换成java对象
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbItemParamItem.class);
            //获取规格参数
            TbItemParamItem itemParamItem = (TbItemParamItem) taotaoResult.getData();
            String paramJson = itemParamItem.getParamData();
            //把规格参数的json数据转换成java对象
            List<Map> mapList = JsonUtils.jsonToList(paramJson,Map.class);
            //遍历list生成的html
            StringBuffer sb = new StringBuffer();
            sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"1\" class=\"Ptable\">\n");
            sb.append(" <tbody>\n");
            for(Map m1:mapList) {
                sb.append("<tr>\n");
                sb.append("<th class=\"tdTitle\" colspan=\"2\">"+m1.get("group")+"</th>\n");
                sb.append("</tr>\n");
                List<Map> list2 = (List<Map>) m1.get("params");
                for(Map m2:list2) {
                    sb.append("<tr>\n");
                    sb.append("<td class=\"tdTitle\">"+m2.get("k")+"</td>\n");
                    sb.append("<td>"+m2.get("v")+"</td>\n");
                    sb.append("</tr>\n");
                }
            }
            sb.append("</tbody>\n");
            sb.append("</table>");
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
