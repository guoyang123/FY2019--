package com.neuedu.service;

import com.neuedu.pojo.User;
import com.neuedu.utils.ServerResponse;
import org.apache.catalina.Server;

public interface IUserService {

    /**
     * 登录
     * */
    public ServerResponse loginLogic(String username,String password);

    /**
     * 注册
     * */

    public ServerResponse registerLogic(User user);


    /**
     * 修改用户信息
     * */
    public ServerResponse updateUserLogic(User user);

}
