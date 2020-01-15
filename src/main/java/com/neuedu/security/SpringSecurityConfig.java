package com.neuedu.security;

import com.neuedu.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter  {
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return  new MyLoginAuthenticationEntryPoint();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/","/portal/user/login.do","/portal/product/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().permitAll()
                .and()
                .formLogin()
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
        ;
        http.csrf().disable();//关闭csrf

    }
    @Autowired
    UserService userService;
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new MyPasswordEncoder());
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**","/css/**","/images/**");
    }


    //    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
////        auth.inMemoryAuthentication()
////                .passwordEncoder(new BCryptPasswordEncoder())
////                .withUser("admin")
////                .password(new BCryptPasswordEncoder().encode("admin"))
////                .roles("ADMIN");
////        auth.inMemoryAuthentication()
////                .passwordEncoder(new BCryptPasswordEncoder())
////                .withUser("zhangsan")
////                .password(new BCryptPasswordEncoder().encode("123456"))
////                .roles("USER");
//
//    }
}
