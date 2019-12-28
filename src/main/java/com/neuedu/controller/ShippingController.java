package com.neuedu.controller;

import com.neuedu.common.Const;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IUserShippingService;
import com.neuedu.utils.ServerResponse;
import com.neuedu.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/portal/shipping/")
public class ShippingController {

    @Autowired
    IUserShippingService userShippingService;

    @RequestMapping("add.do")
    public ServerResponse add(Shipping shipping, HttpSession session){


        UserVO userVO=(UserVO) session.getAttribute(Const.CURRENT_USER);

        return userShippingService.add(shipping,userVO.getId());
    }

}
