package com.neuedu.controller;

import com.neuedu.service.IProductService;
import com.neuedu.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portal/product/")
public class ProductController {

    @Autowired
    IProductService productService;

    @RequestMapping("list.do")
    public ServerResponse list(Integer categoryId,
                               String keyword,
                              @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                               String orderby){



            return productService.list(categoryId, keyword, pageNum, pageSize, orderby);

    }

    /**
     * 获取轮播图
     * */
    @RequestMapping("carsouel.do")
    public  ServerResponse getCarsouel(){

        return productService.getCarsouel();

    }


}
