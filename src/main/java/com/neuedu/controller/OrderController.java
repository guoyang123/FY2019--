package com.neuedu.controller;

import com.neuedu.common.Const;
import com.neuedu.service.IOrderService;
import com.neuedu.utils.ServerResponse;
import com.neuedu.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/portal/order/")
public class OrderController {

    @Autowired
    IOrderService orderService;
    @RequestMapping("create/{shippingid}")
    public ServerResponse createOrder(@PathVariable("shippingid")Integer shippingid, HttpSession session){

        UserVO userVO=(UserVO)session.getAttribute(Const.CURRENT_USER);
        return orderService.createOrder(shippingid,userVO.getId());

    }

}
