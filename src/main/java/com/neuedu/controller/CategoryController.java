package com.neuedu.controller;

import com.neuedu.service.ICategoryService;
import com.neuedu.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portal/category/")
public class CategoryController {


    @Autowired
    ICategoryService categoryService;
    @RequestMapping("sub/{categoryId}")
    public ServerResponse findSubCategory(@PathVariable("categoryId") Integer categoryId){


        return categoryService.findSubCategoryLogic(categoryId);
    }


    @RequestMapping("all/{categoryId}")
    public ServerResponse findAllChildCategory(@PathVariable("categoryId") Integer categoryId){


        return categoryService.findAllChildCategoryLogic(categoryId);
    }


}
