package com.neuedu.controller;

import com.neuedu.common.JedisApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
public class RedisController {

    @Autowired
    JedisApi jedisApi;

    @RequestMapping("/test")
    public String  test(){


       jedisApi.set("neuedu2","neuedu2 hello redis");



       return jedisApi.get("neuedu2");
    }

}
