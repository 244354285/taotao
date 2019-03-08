package com.taotao.common.utils;

import java.util.Random;

/**
 * id生成工具类
 */
public class IDUtils {

    /**
     * 商品id生成
     * @return
     */
     public static long getItemId(){
         //去当前时间的长整型包含毫秒
         long millis = System.currentTimeMillis();
         //生成两位随机数
         Random random = new Random();
         int end2 = random.nextInt(99);
         //如果不足两位前面补0
         String str = millis + String.format("%02d",end2);
         long id = new Long(str);
         return id;
     }
}
