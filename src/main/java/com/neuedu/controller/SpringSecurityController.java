package com.neuedu.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityController {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/auth")
    public String  adminAuth(){
        return "admin Auth";
    }
}
