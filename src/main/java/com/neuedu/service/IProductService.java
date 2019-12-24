package com.neuedu.service;

import com.neuedu.pojo.Product;
import com.neuedu.utils.ServerResponse;

public interface IProductService {

    public ServerResponse list(Integer categoryId,
                               String keyword,
                               Integer pageNum,
                               Integer pageSize,
                               String orderby);

    public ServerResponse findProductById(Integer productId);

}
