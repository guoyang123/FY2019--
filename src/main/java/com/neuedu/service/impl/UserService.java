package com.neuedu.service.impl;

import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.dao.UserMapper;
import com.neuedu.pojo.User;
import com.neuedu.service.IUserService;
import com.neuedu.utils.DateUtil;
import com.neuedu.utils.MD5Utils;
import com.neuedu.utils.ServerResponse;
import com.neuedu.vo.UserVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    UserMapper userMapper;
    @Override
    public ServerResponse loginLogic(String username, String password) {

        //step1: 用户名和密码的非空判断

         if(StringUtils.isBlank(username)){//null length=0 含有空格 tab制表符
             //
             return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EMPTY.getCode(),ResponseCode.USERNAME_NOT_EMPTY.getMsg());
         }
        if(StringUtils.isBlank(password)){
            //


            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_NOT_EMPTY.getCode(),ResponseCode.PASSWORD_NOT_EMPTY.getMsg());
        }
        //step2:查看用户名是否存在
        Integer count= userMapper.findByUsername(username);
        if(count==0){
            //用户名不存在
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EXISTS.getCode(),ResponseCode.USERNAME_NOT_EXISTS.getMsg());
        }

        //step3: 根据用户名和密码查询
          User user=userMapper.findByUsernameAndPassword(username, MD5Utils.getMD5Code(password));

        if(user==null){
          return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_ERROR.getCode(),ResponseCode.PASSWORD_ERROR.getMsg());
        }
        //step4:返回结果



        return ServerResponse.createServerResponseBySucess(convert(user));
    }

     private UserVO convert(User user){
        UserVO userVO=new UserVO();
         userVO.setId(user.getId());
         userVO.setUsername(user.getUsername());
         userVO.setEmail(user.getEmail());
         userVO.setPhone(user.getPhone());
         userVO.setCreateTime(DateUtil.date2String(user.getCreateTime()));
         userVO.setUpdateTime(DateUtil.date2String(user.getUpdateTime()));

        return userVO;
     }

    @Override
    public ServerResponse registerLogic(User user) {

        if(user==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMTER_NOT_EMPTY.getCode(),ResponseCode.PARAMTER_NOT_EMPTY.getMsg());
        }

        String username=user.getUsername();
        String password=user.getPassword();
        String email=user.getEmail();
        String question=user.getQuestion();
        String answer=user.getAnswer();
        String phone=user.getPhone();

        if(username==null||username.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EMPTY.getCode(),ResponseCode.USERNAME_NOT_EMPTY.getMsg());
        }
        if(password==null||password.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_NOT_EMPTY.getCode(),ResponseCode.PASSWORD_NOT_EMPTY.getMsg());
        }


        if(email==null||email.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.EMAIL_NOT_EMPTY.getCode(),ResponseCode.EMAIL_NOT_EMPTY.getMsg());
        }

        if(question==null||question.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.QUESTION_NOT_EMPTY.getCode(),ResponseCode.QUESTION_NOT_EMPTY.getMsg());
        }

        if(answer==null||answer.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.ANSWER_NOT_EMPTY.getCode(),ResponseCode.ANSWER_NOT_EMPTY.getMsg());
        }

        if(phone==null||phone.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.PHONE_NOT_EMPTY.getCode(),ResponseCode.PHONE_NOT_EMPTY.getMsg());
        }

   //STEP2:判断用户名是否存在
        Integer count=userMapper.findByUsername(username);
        if(count>0){//用户名存在
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_EXISTS.getCode(),ResponseCode.USERNAME_EXISTS.getMsg());
        }

        //step3:判断邮箱是否存在
        Integer email_count=userMapper.findByEmail(email);
        if(email_count>0){
            //邮箱存在
            return ServerResponse.createServerResponseByFail(ResponseCode.EMAIL_EXISTS.getCode(),ResponseCode.EMAIL_EXISTS.getMsg());
        }

        //step4:注册
        user.setPassword(MD5Utils.getMD5Code(user.getPassword()));
        user.setRole(Const.NORMAL_USER);//设置用户的角色
        Integer result=userMapper.insert(user);
        if(result==0){
            //注册失败
            return ServerResponse.createServerResponseByFail(ResponseCode.REGISTER_FAIL.getCode(),ResponseCode.REGISTER_FAIL.getMsg());
        }
        return ServerResponse.createServerResponseBySucess();
    }
}
