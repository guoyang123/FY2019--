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


    /**
     * 根据商品id更新库存
     * */
    public ServerResponse updateProductStock(int productId,int stock);


    /**获取轮播图*/

    public ServerResponse getCarsouel();
}
