package com.taotao.portal.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;
import com.taotao.portal.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车service实现
 */
@Service
public class CartServiceImpl implements CartService {

    @Value("${COOKIE_EXPIRE}")
    private Integer COOKIE_EXPIRE;

    @Autowired
    private ItemService itemService;

    /**
     * 添加商品到购物车
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @Override
    public TaotaoResult addCart(long itemId, Integer num, HttpServletRequest request,
                                HttpServletResponse response) {
        //从cookie取商品列表
        List<CartItem> itemList = getCartItemList(request);
        //从商品列表中查询商品是否存在
        boolean haveFlg = false;
        for(CartItem cartItem : itemList){
            //如果商品存在，数量相加
            if (cartItem.getId() == itemId){
                cartItem.setNum(cartItem.getNum()+num);
                haveFlg = true;
                break;
            }
        }
        //如果不存在此商品,调用rest服务，根据商品id获取商品信息
        if (!haveFlg){
            TbItem item = itemService.getItemById(itemId);
            //转换成CartItem
            CartItem cartItem = new CartItem();
            cartItem.setId(itemId);
            cartItem.setNum(num);
            cartItem.setPrice(item.getPrice());
            cartItem.setTitle(item.getTitle());
            if (StringUtils.isNotBlank(item.getImage())){
                String image = item.getImage();
                String[] strings = image.split(",");
                cartItem.setImage(strings[0]);
            }
            //添加到购物车商品列表
            itemList.add(cartItem);
        }
        //把购物车商品写入到cookie
        CookieUtils.setCookie(request,response,"TT_CART",
                JsonUtils.objectToJson(itemList),COOKIE_EXPIRE,true);

        return TaotaoResult.ok();
    }

    /**
     * 获取购物车商品列表
     * @param request
     * @return
     */
    private List<CartItem> getCartItemList(HttpServletRequest request){
        try {
            //从cookie取商品列表
            String json = CookieUtils.getCookieValue(request, "TT_CART", true);
            //把json转换成java对象
            List<CartItem> list = JsonUtils.jsonToList(json, CartItem.class);
            return list == null?new ArrayList<CartItem>():list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<CartItem>();
        }
    }

    /**
     * 获取购物车信息
     * @param request
     * @return
     */
    @Override
    public List<CartItem> getCartItemLists(HttpServletRequest request) {
        List<CartItem> list = getCartItemList(request);
        return list;
    }

    /**
     * 更新购物车商品数量
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @Override
    public TaotaoResult updateCartItem(long itemId, Integer num, HttpServletRequest request,
                                       HttpServletResponse response) {
        //从cookie中取购物车商品列表
        List<CartItem> itemList = getCartItemList(request);
        //根据商品id查询
        for(CartItem cartItem : itemList){
            if(cartItem.getId() == itemId){
                //更新数量
                cartItem.setNum(num);
                break;
            }
        }
        //写入cookie
        CookieUtils.setCookie(request,response,"TT_CART",JsonUtils.objectToJson(itemList),COOKIE_EXPIRE,true);
        return TaotaoResult.ok();
    }

    /**
     * 删除购物车商品
     * @param itemId
     * @param request
     * @param response
     * @return
     */
    @Override
    public TaotaoResult deleteCartItem(long itemId, HttpServletRequest request, HttpServletResponse response) {
         //从cookie中取商品列表
        List<CartItem> itemList = getCartItemList(request);
        //找到对应的id商品
        for(CartItem cartItem : itemList){
            if (cartItem.getId() == itemId){
                //删除购物车商品
                itemList.remove(cartItem);
                break;
            }
        }
        //重新把商品列表写入cookie
        CookieUtils.setCookie(request,response,"TT_CART",JsonUtils.objectToJson(itemList),COOKIE_EXPIRE,true);
        return TaotaoResult.ok();
    }
}
