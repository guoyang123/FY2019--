package com.neuedu.exception;

public class BusinessException extends RuntimeException {

    private  String  msg;
    private int  id;

    public BusinessException(String msg){
        super(msg);

        this.msg=msg;
    }

    public BusinessException(int id,String msg){
        super(msg);
        this.id=id;
        this.msg=msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
