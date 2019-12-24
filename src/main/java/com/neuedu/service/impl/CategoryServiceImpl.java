package com.neuedu.service.impl;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.neuedu.common.ResponseCode;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import com.neuedu.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    CategoryMapper categoryMapper;
    @Override
    public ServerResponse findSubCategoryLogic(Integer categoryId) {

        if(categoryId==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMTER_NOT_EMPTY.getCode(),ResponseCode.PARAMTER_NOT_EMPTY.getMsg());
        }
        List<Category> categoryList=categoryMapper.findSubCategoryById(categoryId);


        return ServerResponse.createServerResponseBySucess(categoryList);
    }

    @Override
    public ServerResponse findAllChildCategoryLogic(Integer categoryId) {

        if(categoryId==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMTER_NOT_EMPTY.getCode(),ResponseCode.PARAMTER_NOT_EMPTY.getMsg());
        }
        Set<Category> set=new HashSet<>();
        Set<Category> categorySet= findAllChild(set,categoryId);

        return ServerResponse.createServerResponseBySucess(categorySet);
    }


    private Set<Category> findAllChild(Set<Category> categorySet,Integer categoryId){

      //step1:根据categoryId查询
      Category category= categoryMapper.selectByPrimaryKey(categoryId);
      if(category!=null){
          categorySet.add(category);
      }

      //查询category下的所有的一级子类别
        List<Category> categoryList=categoryMapper.findSubCategoryById(categoryId);
        for(Category category1:categoryList){
            findAllChild(categorySet,category1.getId());
        }
        return  categorySet;
    }

}
