package com.neuedu.service;

import com.neuedu.utils.ServerResponse;

import java.util.Map;

public interface IPayService {

    public ServerResponse pay(Long orderNo);

    public  String callbackLogic(Map<String,String> stringMap);

}
