package com.neuedu.service;

import com.neuedu.utils.ServerResponse;

public interface ICategoryService {


    public ServerResponse findSubCategoryLogic(Integer categoryId);

    /**
     * 查询categoryId下的所有子类
     * */
    public ServerResponse findAllChildCategoryLogic(Integer categoryId);
}
