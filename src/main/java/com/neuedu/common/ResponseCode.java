package com.neuedu.common;

public enum ResponseCode {

     USERNAME_NOT_EMPTY(1,"用户名不能为空"),
     PASSWORD_NOT_EMPTY(2,"密码不能为空"),
    USERNAME_NOT_EXISTS(3,"用户名不存在"),
    PASSWORD_ERROR(4,"密码错误"),
    PARAMTER_NOT_EMPTY(5,"参数不能为空"),
    EMAIL_NOT_EMPTY(6,"邮箱不能为空"),
    PHONE_NOT_EMPTY(7,"联系方式不能为空"),
    QUESTION_NOT_EMPTY(8,"密保问题不能为空"),
    ANSWER_NOT_EMPTY(9,"密保答案不能为空"),
    USERNAME_EXISTS(10,"用户名存在"),
    EMAIL_EXISTS(11,"邮箱存在"),
    REGISTER_FAIL(12,"注册失败"),
    NEED_LOGIN(13,"未登录"),
    USERINFO_UPDATE_FAIL(14,"用户信息修改失败"),
    ILLEGAL_PARAM(15,"非法参数"),
    CART_UPDATE_FAIL(16,"购物车商品更新失败"),
    CART_ADD_FAIL(17,"购物车商品添加失败"),
    PRODUCT_NOT_EXISTS(18,"商品不存在"),
    CART_NOT_SELECTED(19,"购物车为空或者没有被选中的商品"),
    ORDERITEM_INSERT_FAIL(20,"订单明细插入失败"),
    ORDER_CREATE_FAIL(21,"订单创建失败"),
    PRODUCT_STOCK_UPDATE_FAIL(22,"商品库存更新失败"),
    CART_CLEAR_FAIL(23,"清空购物车失败"),
    SHIPPING_ADD_FAIL(24,"地址添加失败"),
    PAY_PARAM_ERROR(25,"支付参数有误"),
    ORDER_NO_NOT_EMPTY(26,"订单编号不能为空"),
    ORDER_NOT_EXISTS(27,"订单不存在"),
    ORDER_UPDATE_FAIL(28,"订单更新失败"),
    CARSOUEL_EMPTY(29,"没有轮播图"),
    ;


    private  int code;
    private String msg;

     ResponseCode(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
