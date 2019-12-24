package com.neuedu.common;

public enum  CheckEnum {

    CART_PRODUCT_CHECKED(1),
    CART_PRODUCT_UNCHECK(0),
    ;



    private int  check;

     CheckEnum(int check){
        this.check=check;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}
