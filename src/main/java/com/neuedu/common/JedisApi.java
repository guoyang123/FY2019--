package com.neuedu.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@Component
public class JedisApi {


    @Autowired
    JedisPool jedisPool;

    /**
     * 添加字符串k,v
     * */
    public  String  set(String key,String value){

        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String rsult=jedis.set(key,value);
            jedisPool.returnResource(jedis);
            return rsult;
        }catch (RuntimeException e){

            jedisPool.returnBrokenResource(jedis);
        }


        return null;

    }

    public  String  get(String key){

        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String rsult=jedis.get(key);
            jedisPool.returnResource(jedis);
            return rsult;
        }catch (RuntimeException e){

            jedisPool.returnBrokenResource(jedis);
        }


        return null;

    }

    public  Long  setnx(String key,String value){

        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            Long rsult=jedis.setnx(key,value);
            jedisPool.returnResource(jedis);
            return rsult;
        }catch (RuntimeException e){

            jedisPool.returnBrokenResource(jedis);
        }


        return null;

    }


    public  Long  ttl(String key){

        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            Long rsult=jedis.ttl(key);
            jedisPool.returnResource(jedis);
            return rsult;
        }catch (RuntimeException e){

            jedisPool.returnBrokenResource(jedis);
        }


        return null;

    }


    public  Long  expire(String key,int  expire){

        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            Long rsult=jedis.expire(key,expire);
            jedisPool.returnResource(jedis);
            return rsult;
        }catch (RuntimeException e){

            jedisPool.returnBrokenResource(jedis);
        }


        return null;

    }




    public  String   mset(String... keyvalues){

        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String  rsult=jedis.mset(keyvalues);

            jedisPool.returnResource(jedis);
            return rsult;
        }catch (RuntimeException e){

            jedisPool.returnBrokenResource(jedis);
        }


        return null;

    }


    public Set<String> mpop(String key, int count){

        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            Set<String>  rsult=jedis.spop(key,count);
            jedisPool.returnResource(jedis);
            return rsult;
        }catch (RuntimeException e){

            jedisPool.returnBrokenResource(jedis);
        }


        return null;

    }
}
