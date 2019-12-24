package com.neuedu.controller;

import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.pojo.User;
import com.neuedu.service.IUserService;
import com.neuedu.utils.ServerResponse;
import com.neuedu.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/portal/")
public class UserController
{

    @Autowired
    IUserService userService;

    @RequestMapping(value = "user/login.do")
   public ServerResponse login(String username, String password, HttpSession session){

       ServerResponse serverResponse= userService.loginLogic(username, password);
       if(serverResponse.isSucess()){
           session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
       }
       return serverResponse;

   }

   @RequestMapping("user/register.do")
   public ServerResponse register(User user){

       return  userService.registerLogic(user);
   }


   @RequestMapping("user/update.do")
   public  ServerResponse updateUser(User user,HttpSession session){

        //判断用户是否登录
       UserVO userInfo=(UserVO)session.getAttribute(Const.CURRENT_USER);
//        if(userInfo==null){
//            return ServerResponse.createServerResponseByFail(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
//        }
//        if(user==null){
//            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMTER_NOT_EMPTY.getCode(),ResponseCode.PARAMTER_NOT_EMPTY.getMsg());
//        }
        user.setId(userInfo.getId());

        ServerResponse serverResponse= userService.updateUserLogic(user);
        if(serverResponse.isSucess()){//更新session中的用户信息
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }

        return serverResponse;


   }


   /**
    * 退出登录
    * */

   @RequestMapping("user/logout.do")
   public  ServerResponse logout(HttpSession session){

       session.removeAttribute(Const.CURRENT_USER);
       return ServerResponse.createServerResponseBySucess();

   }


}
