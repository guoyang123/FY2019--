package com.neuedu.service.impl;

import com.neuedu.common.OrderStatusEnum;
import com.neuedu.common.ResponseCode;
import com.neuedu.dao.OrderItemMapper;
import com.neuedu.dao.OrderMapper;
import com.neuedu.exception.BusinessException;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Order;
import com.neuedu.pojo.OrderItem;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICartService;
import com.neuedu.service.IOrderService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.BigDecimalUtil;
import com.neuedu.utils.DateUtil;
import com.neuedu.utils.ServerResponse;
import com.neuedu.vo.OrderItemVO;
import com.neuedu.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    ICartService cartService;
    @Autowired
    IProductService productService;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    OrderMapper orderMapper;


    @Override
    @Transactional(rollbackFor = {BusinessException.class})
    public ServerResponse createOrder(Integer shippingId, Integer userId) {

        if(shippingId==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMTER_NOT_EMPTY.getCode(),ResponseCode.PARAMTER_NOT_EMPTY.getMsg());
        }

        //step1: 去用户购物车中查询已经选中的商品信息

       ServerResponse serverResponse= cartService.findCartByUserIdAndChecked(userId);

        if(!serverResponse.isSucess()){
            return serverResponse;
        }

        List<Cart> cartList=(List<Cart>)serverResponse.getData();

        List<OrderItem> orderItemList=new ArrayList<>();
        Long orderNo=generateOrderNo();
        for(Cart cart:cartList){
           OrderItem orderItem= cart2OrderItem(cart,orderNo);
           orderItemList.add(orderItem);
        }
        //将List<OrderItem>插入到订单明细库中
        int count=orderItemMapper.insertBatch(orderItemList);
        if(count!=orderItemList.size()){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.ORDERITEM_INSERT_FAIL.getCode(),ResponseCode.ORDERITEM_INSERT_FAIL.getMsg());
        }

        //计算订单总价格

        BigDecimal totalPrice=getOrderTotalPrice(orderItemList);

      //创建订单
        ServerResponse serverResponse1=createOrder(shippingId,totalPrice,userId,orderItemList.get(0).getOrderNo());

        if(!serverResponse1.isSucess()){
            return serverResponse1;
        }

        Order order=(Order) serverResponse1.getData();

        //扣库存
        reduceProductStock(orderItemList);





       // 清空购物车中已购买的商品
        clearCart(cartList);
      // 构建OrderVO
        OrderVO orderVO=getOrderVO(order,orderItemList,shippingId);
        if(orderVO==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ORDER_CREATE_FAIL.getCode(),ResponseCode.ORDER_CREATE_FAIL.getMsg());

        }
        return ServerResponse.createServerResponseBySucess(orderVO);
    }

    @Override
    public ServerResponse findOrderByOrdrno(Long orderNo) {

        if(orderNo==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ORDER_NO_NOT_EMPTY.getCode(),ResponseCode.ORDER_NO_NOT_EMPTY.getMsg());
        }

        Order order=orderMapper.findOrderByOrderNo(orderNo);
        if(order==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ORDER_NOT_EXISTS.getCode(),ResponseCode.ORDER_NOT_EXISTS.getMsg());
        }

        return ServerResponse.createServerResponseBySucess(order);
    }

    @Override
    public ServerResponse updateOrderByPayed(Long orderNo,Integer status, Date payedTime) {

        int count=orderMapper.updateOrderBypayed(orderNo, payedTime, status);
        if(count==0){
            //更新失败
            return ServerResponse.createServerResponseByFail(ResponseCode.ORDER_UPDATE_FAIL.getCode(),ResponseCode.ORDER_UPDATE_FAIL.getMsg());
        }
        return ServerResponse.createServerResponseBySucess();
    }


    private OrderVO getOrderVO(Order order,List<OrderItem> orderItemList,Integer shippingid){
        OrderVO orderVO=new OrderVO();
         orderVO.setOrderNo(order.getOrderNo());
         orderVO.setShippingid(shippingid);
         orderVO.setCloseTime(DateUtil.date2String(order.getCloseTime()));
         orderVO.setCreateTime(DateUtil.date2String(order.getCreateTime()));
         orderVO.setEndTime(DateUtil.date2String(order.getEndTime()));
         orderVO.setPayment(order.getPayment());
         orderVO.setPaymentTime(DateUtil.date2String(order.getPaymentTime()));
         orderVO.setPaymentType(order.getPaymentType());
         orderVO.setPostage(order.getPostage());
         orderVO.setSendTime(DateUtil.date2String(order.getSendTime()));
         orderVO.setOrderItemVOList(getOrderItemVOList(orderItemList));
         orderVO.setStatus(order.getStatus());
         orderVO.setStatusDesc(OrderStatusEnum.getStatusDesc(order.getStatus()));
          orderVO.setShippingVO(null);
        return orderVO;
      }


      private List<OrderItemVO> getOrderItemVOList(List<OrderItem> orderItemList){
        if(orderItemList==null||orderItemList.size()==0){
            throw new BusinessException("订单明细为空");
        }
        List<OrderItemVO> orderItemVOList=new ArrayList<>();
        for(OrderItem orderItem:orderItemList){
            OrderItemVO orderItemVO=new OrderItemVO();
             orderItemVO.setCreateTime(DateUtil.date2String(orderItem.getCreateTime()));
             orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
             orderItemVO.setOrderNo(orderItem.getOrderNo());
             orderItemVO.setProductId(orderItem.getProductId());
             orderItemVO.setProductImage(orderItem.getProductImage());
             orderItemVO.setProductName(orderItem.getProductName());
             orderItemVO.setQuantity(orderItem.getQuantity());
             orderItemVO.setTotalPrice(orderItem.getTotalPrice());
            orderItemVOList.add(orderItemVO);
        }
        return orderItemVOList;
      }


    /**
     * 清空购物车
     * */

    public void  clearCart(List<Cart> cartList){
        //清空购物车中已经购买的商品
        List<Integer> ids=new ArrayList<>();
        for(Cart cart:cartList){
            ids.add(cart.getId());
        }
        ServerResponse serverResponse1=cartService.deleteByIds(ids);
        if(!serverResponse1.isSucess()){
            throw  new BusinessException("删除购物车失败");
        }
    }

    //扣库存方法
    private  void  reduceProductStock(List<OrderItem> orderItems){

        for(OrderItem orderItem:orderItems){

           ServerResponse serverResponse=productService.findProductById(orderItem.getProductId());
           if(!serverResponse.isSucess()){
               throw new BusinessException("商品不存在");
           }
           Product product=(Product) serverResponse.getData();

           //购买数量
           int quantity= orderItem.getQuantity();
           if(product.getStock()<quantity){
               //商品库存不足
               throw new BusinessException("商品库存不足");
           }
           //更新商品库存数
           ServerResponse serverResponse1= productService.updateProductStock(product.getId(),product.getStock()-quantity);

           if(!serverResponse1.isSucess()){
               throw new BusinessException("商品扣库存失败");
           }

        }

    }


    private  BigDecimal getOrderTotalPrice(List<OrderItem> orderItems){

        BigDecimal totalPrice=new BigDecimal("0");
        for(OrderItem orderItem:orderItems){
            totalPrice= BigDecimalUtil.add(String.valueOf(totalPrice.doubleValue()),String.valueOf(orderItem.getTotalPrice().doubleValue()));
        }
        return totalPrice;
    }

    private ServerResponse createOrder(Integer shippingId,BigDecimal orderTotalPrice,Integer userId, Long orderNo){
        Order order=new Order();
        order.setOrderNo(orderNo);
        order.setPayment(orderTotalPrice);
        order.setPaymentType(1);
        order.setPostage(0);
        order.setShippingId(shippingId);
        order.setUserId(userId);
        order.setStatus(OrderStatusEnum.ORDER_NO_PAYED.getStatus());



        //将订单order插入到订单库中
        int ordercount= orderMapper.insert(order);
        if(ordercount==0){
            return ServerResponse.createServerResponseByFail(ResponseCode.ORDER_CREATE_FAIL.getCode(),ResponseCode.ORDER_CREATE_FAIL.getMsg());
        }

        //根据orderNo查询订单
        Order orderResult=orderMapper.findOrderByOrderNo(orderNo);
        if(orderResult==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ORDER_CREATE_FAIL.getCode(),ResponseCode.ORDER_CREATE_FAIL.getMsg());
        }


        return ServerResponse.createServerResponseBySucess(orderResult);

    }

    /**
     * Cart转OrderItem
     * */
    private OrderItem cart2OrderItem(Cart cart,Long orederNo){

        if(cart==null){
            return null;
        }
        OrderItem orderItem=new OrderItem();
        orderItem.setOrderNo(orederNo);
        orderItem.setProductId(cart.getProductId());
        //根据productid查询商品信息
        ServerResponse serverResponse=productService.findProductById(cart.getProductId());
        if(!serverResponse.isSucess()){
            //商品不存在
           throw  new BusinessException("商品不存在");
        }
        Product product=(Product) serverResponse.getData();
        orderItem.setProductImage(product.getMainImage());
        orderItem.setProductName(product.getName());
        orderItem.setCurrentUnitPrice(product.getPrice());
        orderItem.setQuantity(cart.getQuantity());
        orderItem.setTotalPrice(BigDecimalUtil.multi(String.valueOf(product.getPrice().doubleValue()),
                String.valueOf(cart.getQuantity())));
        orderItem.setUserId(cart.getUserId());

        return orderItem;
    }


    public  Long  generateOrderNo(){
        return System.currentTimeMillis();
    }

}
