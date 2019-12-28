package com.neuedu.service;

import com.neuedu.utils.ServerResponse;

import java.util.Date;

public interface IOrderService {

    /**
     * 创建订单
     * @param shippingId 收货地址
     * @Param userId
     * */
    public ServerResponse createOrder(Integer shippingId,Integer userId);

    /**
     * 根据orderNo查询订单信息
     * */

    public ServerResponse findOrderByOrdrno(Long orderNo);

    /**
     * 更新订单的支付状态
     * */
    public ServerResponse updateOrderByPayed(Long orderNo,Integer status,Date payedTime);
}
