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
