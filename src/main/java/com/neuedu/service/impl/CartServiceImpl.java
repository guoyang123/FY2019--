package com.neuedu.service.impl;

import com.neuedu.common.CheckEnum;
import com.neuedu.common.ResponseCode;
import com.neuedu.dao.CartMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICartService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.BigDecimalUtil;
import com.neuedu.utils.ServerResponse;
import com.neuedu.vo.CartProductVO;
import com.neuedu.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class CartServiceImpl implements ICartService {
    @Autowired
    CartMapper cartMapper;

    @Autowired
    IProductService productService;

    @Override
    public ServerResponse add(Integer productId, Integer count,Integer userId) {

        if(productId==null||count==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMTER_NOT_EMPTY.getCode(),ResponseCode.PARAMTER_NOT_EMPTY.getMsg());
        }

        //查询购物车中是否有该商品
        Cart cart=cartMapper.findCartByProductId(productId);
        int result=0;
        if(cart!=null){
            //更新数量
            cart.setQuantity(cart.getQuantity()+count);
             result=cartMapper.updateByProductId(cart.getProductId(),cart.getQuantity());
            if(result==0){
                return ServerResponse.createServerResponseByFail(ResponseCode.CART_UPDATE_FAIL.getCode(),ResponseCode.CART_UPDATE_FAIL.getMsg());
            }
        }else{
            //添加
            Cart insertCart=new Cart();
            insertCart.setProductId(productId);
            insertCart.setQuantity(count);
            insertCart.setChecked(CheckEnum.CART_PRODUCT_CHECKED.getCheck());
            insertCart.setUserId(userId);
            result=cartMapper.insert(insertCart);
            if(result<=0){
                //添加失败
                return ServerResponse.createServerResponseByFail(ResponseCode.CART_ADD_FAIL.getCode(),ResponseCode.CART_ADD_FAIL.getMsg());
            }
        }

        //CartVO
       CartVO cartVO=getCartVo(userId);


        return ServerResponse.createServerResponseBySucess(cartVO);
    }

    @Override
    public ServerResponse list(Integer userId) {
        return ServerResponse.createServerResponseBySucess(getCartVo(userId));
    }

    @Override
    public ServerResponse choice(Integer productId,Integer userId) {

        if(productId==0){
            //全选或者反选

            //查看当前购物车是否处于全选状态
           int count=cartMapper.unCheckedCount(userId);
           if(count==0){//全选的状态
               //更新为全不选
                cartMapper.updateCheckedByUserId(userId,CheckEnum.CART_PRODUCT_UNCHECK.getCheck());
           }else{
               //更新为全选
               cartMapper.updateCheckedByUserId(userId,CheckEnum.CART_PRODUCT_CHECKED.getCheck());
           }

        }else{
            //单选操作
          int result= cartMapper.findCheckByProductId(productId, userId);
          if(result==0){//处于未选中状态
              //更新为选择状态
              cartMapper.updateCheckedByUserIdAndProductId(userId,productId,CheckEnum.CART_PRODUCT_CHECKED.getCheck());
          }else{
              //更新为未选择
              cartMapper.updateCheckedByUserIdAndProductId(userId,productId,CheckEnum.CART_PRODUCT_UNCHECK.getCheck());

          }

        }

        return ServerResponse.createServerResponseBySucess(getCartVo(userId));
    }

    @Override
    public ServerResponse findCartByUserIdAndChecked(Integer userId) {


        List<Cart> cartList=cartMapper.findCartByUserIdAndChecked(userId);
        if(cartList==null||cartList.size()==0){
            return ServerResponse.createServerResponseByFail(ResponseCode.CART_NOT_SELECTED.getCode(),ResponseCode.CART_NOT_SELECTED.getMsg());
        }
        return ServerResponse.createServerResponseBySucess(cartList);
    }

    @Override
    public ServerResponse deleteByIds(List<Integer> ids) {

        if(ids==null||ids.size()==0){
            return  ServerResponse.createServerResponseByFail(ResponseCode.ILLEGAL_PARAM.getCode(),ResponseCode.ILLEGAL_PARAM.getMsg());
        }

       int count= cartMapper.deleteByIds(ids);
        if(count!=ids.size()){
            return ServerResponse.createServerResponseByFail(ResponseCode.CART_CLEAR_FAIL.getCode(),ResponseCode.CART_CLEAR_FAIL.getMsg());
        }
        return ServerResponse.createServerResponseBySucess();
    }


    public CartVO getCartVo(Integer userId){
        CartVO cartVO=new CartVO();
        //step1：查询购物车信息

        List<Cart> cartList=cartMapper.selectAllByUserid(userId);
        if(cartList==null||cartList.size()==0){
            return  cartVO;
        }
        List<CartProductVO> cartProductVOList=new ArrayList<>();
        BigDecimal cartTotalPrice=new BigDecimal("0");
        //step2;遍历购物车，根据商品id查询商品信息
        for(Cart cart:cartList){
            CartProductVO cartProductVO=new CartProductVO();
            cartProductVO.setId(cart.getId());
            cartProductVO.setQuantity(cart.getQuantity());
            cartProductVO.setProductChecked(cart.getChecked());
            cartProductVO.setUserId(cart.getUserId());

            //根据商品id查询商品信息
            ServerResponse serverResponse= productService.findProductById(cart.getProductId());
            if(!serverResponse.isSucess()){
                //将不存在的商品移除
                cartMapper.deleteByPrimaryKey(cart.getId());
            }
            Product product=(Product)serverResponse.getData();
            cartProductVO.setProductId(cart.getProductId());
            cartProductVO.setProductMainImage(product.getMainImage());
            cartProductVO.setProductName(product.getName());
            cartProductVO.setProductPrice(product.getPrice());
            cartProductVO.setProductStatus(product.getStatus());
            int stock=product.getStock();
            cartProductVO.setProductStock(stock);
            cartProductVO.setProductSubtitle(product.getSubtitle());

            if(stock>cart.getQuantity()){
                cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
            }else{
                cartProductVO.setLimitQuantity("LIMIT_NUM_FAIL");
                //
                cartMapper.updateByProductId(product.getId(),product.getStock());
            }

            //quantity*price
            //计算总的价格
            cartProductVO.setProductTotalPrice(BigDecimalUtil.multi(String.valueOf(product.getPrice().doubleValue()),String.valueOf(cart.getQuantity())));
            if(cart.getChecked()==CheckEnum.CART_PRODUCT_CHECKED.getCheck()){
                //被选中
                cartTotalPrice= BigDecimalUtil.add(String.valueOf(cartTotalPrice.doubleValue()),String.valueOf(cartProductVO.getProductTotalPrice().doubleValue()));
            }
            cartProductVOList.add(cartProductVO);

        }


        cartVO.setCartProductVOList(cartProductVOList);
        //判断用户是否全选
        int unCheckedCount=cartMapper.unCheckedCount(userId);
        if(unCheckedCount==0){
            cartVO.setAllChecked(true);
        }else{
            cartVO.setAllChecked(false);
        }

        //计算总价格
           cartVO.setCartTotalPrice(cartTotalPrice);


        return cartVO;

    }

}
