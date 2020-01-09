package com.neuedu.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisPoolConfig {


    @Value("${redis.maxTotal}")
    private int maxTotal;
    @Value("${redis.maxIdle}")
    private int maxIdle;
    @Value("${redis.blockWhenExhausted}")
    private boolean blockWhenExhausted;
    @Value("${redis.maxWaitMillis}")
    private int maxWaitMillis;
    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${redis.testOnReturn}")
    private boolean testOnReturn;
    @Value("${redis.jmxEnabled}")
    private  boolean jmxEnabled;

    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.timeout}")
    private int timeout;
    @Value("${redis.password}")
    private String password;

    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig(){

        GenericObjectPoolConfig genericObjectPoolConfig=new GenericObjectPoolConfig();

        genericObjectPoolConfig.setMaxTotal(maxTotal);
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
        genericObjectPoolConfig.setMaxWaitMillis(maxWaitMillis);
        genericObjectPoolConfig.setTestOnBorrow(testOnBorrow);
        genericObjectPoolConfig.setTestOnReturn(testOnReturn);
        genericObjectPoolConfig.setJmxEnabled(jmxEnabled);

        return genericObjectPoolConfig;
    }



    @Bean
    public JedisPool jedisPool(){

       JedisPool jedisPool=new JedisPool(genericObjectPoolConfig(),host,port,timeout,password);

       return jedisPool;
    }


}
