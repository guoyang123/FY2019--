package com.neuedu;

import com.neuedu.inteceptors.PortalLoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@SpringBootConfiguration
public class SpringBootConfig  implements WebMvcConfigurer{


    @Autowired
    PortalLoginCheckInterceptor portalLoginCheckInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

         List<String> includeUrl=new ArrayList<>();
         includeUrl.add("/portal/user/**");
        includeUrl.add("/portal/cart/**");

        List<String> exclueUrl=new ArrayList<>();
        exclueUrl.add("/portal/user/login.do");
        exclueUrl.add("/portal/user/register.do");

        registry.addInterceptor(portalLoginCheckInterceptor).addPathPatterns(includeUrl) //添加需要拦截的路径
                .excludePathPatterns(exclueUrl); //排除不需要拦截的路径
    }
}
