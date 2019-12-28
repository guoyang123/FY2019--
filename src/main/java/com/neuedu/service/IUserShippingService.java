package com.neuedu.service;

import com.neuedu.pojo.Shipping;
import com.neuedu.utils.ServerResponse;

public interface IUserShippingService {


    public ServerResponse add(Shipping shipping,Integer userId);

}
