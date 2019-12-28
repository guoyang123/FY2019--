package com.neuedu.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.neuedu.service.IPayService;
import com.neuedu.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.expression.Maps;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/portal/pay/")
public class AliPayController {


    @Autowired
    IPayService payService;

    @Value("${alipay.publickey}")
    private String alipyPublicKey;
    /**
     * 支付接口
     * */

    @RequestMapping("pay.do")
    public ServerResponse pay(Long orderNo){




        return  payService.pay(orderNo);


    }


    @RequestMapping("callback.do")
    public  String  alipayCallBack(HttpServletRequest request){



        Map<String,String[]> params=request.getParameterMap();

        if(params==null||params.size()==0){
            return "fail";
        }

        Map<String,String> signParam=new HashMap<>();

        Set<String> keys=params.keySet();
        Iterator<String> iterator=keys.iterator();

        while (iterator.hasNext()){
            String key=iterator.next();
            String[] values=params.get(key);
            StringBuilder stringBuilder=new StringBuilder();
            for(String value:values){
                stringBuilder.append(value+",");
            }
            String value=stringBuilder.toString();
           value= value.substring(0,value.length()-1);
            signParam.put(key,value);

        }

        //验证签名
        try {
            signParam.remove("sign_type");
            boolean checkSign=AlipaySignature.rsaCheckV2(signParam,alipyPublicKey,"utf-8","RSA2");
            if(checkSign){
                //通过
                //处理业务逻辑
                System.out.println("验证签名通过");
                 return payService.callbackLogic(signParam);

            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        String sucess="success";
        return sucess;

    }
}
