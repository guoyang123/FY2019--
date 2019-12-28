package com.neuedu.service.impl;

import com.neuedu.common.OrderStatusEnum;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.TradeStatusEnum;
import com.neuedu.dao.PayInfoMapper;
import com.neuedu.pojo.Order;
import com.neuedu.pojo.PayInfo;
import com.neuedu.service.IOrderService;
import com.neuedu.service.IPayService;
import com.neuedu.utils.DateUtil;
import com.neuedu.utils.OrderInfoUtil2_0;
import com.neuedu.utils.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class PayServiceImpl implements IPayService {



    @Value("${alipay.appid}")
    private    String APPID;
    @Value("${alipay.rsa2_private}")
    private String RSA2_PRIVATE;

    @Value("${alipay.notify_url}")
   private String NOTIFY_URL;

    @Autowired
    IOrderService orderService;

    @Autowired
    PayInfoMapper payInfoMapper;


    @Override
    public ServerResponse pay(Long orderNo) {

        //生成支付信息

        if (StringUtils.isBlank(APPID) || (StringUtils.isBlank(RSA2_PRIVATE) )) {

            return ServerResponse.createServerResponseByFail(ResponseCode.PAY_PARAM_ERROR.getCode(),ResponseCode.PAY_PARAM_ERROR.getMsg());
        }

      //根据订单号orderno查询订单信息


      ServerResponse serverResponse=   orderService.findOrderByOrdrno(orderNo);
        if(!serverResponse.isSucess()){
            return serverResponse;
        }
        Order order=(Order)serverResponse.getData();
        Map<String,String> params= OrderInfoUtil2_0.buildOrderParamMap(APPID,true,order,NOTIFY_URL);

        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);


        String sign = OrderInfoUtil2_0.getSign(params, RSA2_PRIVATE, true);
        final String orderInfo = orderParam + "&" + sign;



        return ServerResponse.createServerResponseBySucess(orderInfo);
    }

    @Override
    public String callbackLogic(Map<String, String> singMap) {

        //step1：获取订单编号orderno

        Long orderNo=Long.valueOf(singMap.get("out_trade_no"));


        //step2: 校验orderno是否存在
        ServerResponse serverResponse= orderService.findOrderByOrdrno(orderNo);
        if(!serverResponse.isSucess()){
            return "success";
        }

        //step3:判断订单是否被修改
          Order order=(Order)serverResponse.getData();


        //step4: 修改订单状态
        if(order.getStatus()>=OrderStatusEnum.ORDER_PAYED.getStatus()){
            //订单已经支付
            return  "success";
        }

        //获取订单的支付状态
        String tarde_status=(String) singMap.get("trade_status");
        Date payed_time= DateUtil.string2Date((String)singMap.get("gmt_payment"));


        ServerResponse serverResponse1=orderService.updateOrderByPayed(orderNo, TradeStatusEnum.statusof(tarde_status),payed_time);

        if(!serverResponse1.isSucess()){
            return "fail";
        }
        //step5:插入或更新支付信息

        PayInfo payInfo=new PayInfo();
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setUserId(order.getUserId());
        payInfo.setPayPlatform(1);
        payInfo.setPlatformNumber(singMap.get("trade_no"));
        payInfo.setPlatformStatus(tarde_status);

        PayInfo payInfo1=findPayInfoByOrderNO(orderNo);
        int count=0;
        if(payInfo1==null){
            //插入
           count= payInfoMapper.insert(payInfo);
        }else{
            //更新
            payInfo.setId(payInfo1.getId());
           count= payInfoMapper.updateByPrimaryKey(payInfo);
        }
        if(count==0){
            return "fail";
        }
        //step6:返回结果

        return "success";
    }

    //根据订单号查询支付信息
    private  PayInfo findPayInfoByOrderNO(Long orderNo){
        PayInfo payInfo=payInfoMapper.selectByOrderNo(orderNo);
        return payInfo;
    }

}
