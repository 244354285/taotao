package com.taotao.order.component.impl;

import com.taotao.order.component.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

/**
 * redis客户端集群版实现类
 */
public class JedisClientCluster implements JedisClient {

    @Autowired
    private JedisCluster jedisCluster;

    public String set(String key, String value) {
        return jedisCluster.set(key,value);
    }

    public String get(String key) {
        return jedisCluster.get(key);
    }

    public Long hset(String key, String item, String value) {
        return jedisCluster.hset(key,item,value);
    }

    public String hget(String key, String item) {
        return jedisCluster.hget(key,item);
    }

    public Long incr(String key) {
        return jedisCluster.incr(key);
    }

    public Long decr(String key) {
        return jedisCluster.decr(key);
    }

    public Long expire(String key, int second) {
        return jedisCluster.expire(key,second);
    }

    public Long ttl(String key) {
        return jedisCluster.ttl(key);
    }

    public Long hdel(String key, String item) {
        return jedisCluster.hdel(key,item);
    }
}
