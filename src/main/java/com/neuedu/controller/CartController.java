package com.neuedu.controller;

import com.neuedu.common.Const;
import com.neuedu.service.ICartService;
import com.neuedu.utils.ServerResponse;
import com.neuedu.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/portal/cart/")
public class CartController {

    @Autowired
    ICartService cartService;
 /**
  * 商品添加到购物车
  * */

 @RequestMapping("{productId}/{count}")
 public ServerResponse add(@PathVariable("productId") Integer productId,
                           @PathVariable("count")Integer count, HttpSession session){

    UserVO userVO=(UserVO) session.getAttribute(Const.CURRENT_USER);
  return cartService.add(productId, count,userVO.getId());

 }
 /**
  * 获取购物车列表
  * */
 @RequestMapping("list.do")
  public  ServerResponse list( HttpSession session){
     UserVO userVO=(UserVO) session.getAttribute(Const.CURRENT_USER);
     return cartService.list(userVO.getId());

  }


  /**
   * 选择某个、取消选择某个、全选、反选
   * */

  @RequestMapping("choice")
  public  ServerResponse choice(@RequestParam(value = "productId",required = false,defaultValue = "0")
                                          Integer  productId,HttpSession session){

      UserVO userVO=(UserVO) session.getAttribute(Const.CURRENT_USER);

      return cartService.choice(productId,userVO.getId());

  }


}
