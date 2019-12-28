package com.neuedu.service;

import com.neuedu.utils.ServerResponse;

import java.util.List;

public interface ICartService {


    public ServerResponse add(Integer productId,Integer count,Integer userId);


    public  ServerResponse list(Integer userId);

    public  ServerResponse choice(Integer productId,Integer userId);

    public ServerResponse findCartByUserIdAndChecked(Integer userId);

    public ServerResponse deleteByIds(List<Integer> ids);
}
