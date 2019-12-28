package com.neuedu.service.impl;

import com.neuedu.common.ResponseCode;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IUserShippingService;
import com.neuedu.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserShippingImpl implements IUserShippingService {
    @Autowired
    ShippingMapper shippingMapper;
    @Override
    public ServerResponse add(Shipping shipping, Integer userId) {

        if(shipping==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMTER_NOT_EMPTY.getCode(),ResponseCode.PARAMTER_NOT_EMPTY.getMsg());
        }

        shipping.setUserId(userId);
      int count= shippingMapper.insert(shipping);

      if(count==0){
          //添加失败
          return ServerResponse.createServerResponseByFail(ResponseCode.SHIPPING_ADD_FAIL.getCode(),ResponseCode.SHIPPING_ADD_FAIL.getMsg());
      }
        return ServerResponse.createServerResponseBySucess(shipping);
    }
}
