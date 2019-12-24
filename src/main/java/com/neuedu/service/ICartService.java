package com.neuedu.service;

import com.neuedu.utils.ServerResponse;

public interface ICartService {


    public ServerResponse add(Integer productId,Integer count,Integer userId);


    public  ServerResponse list(Integer userId);

    public  ServerResponse choice(Integer productId,Integer userId);
}
