package com.neuedu.common;

public enum TradeStatusEnum {

    TRADE_FINISHED	("TRADE_FINISHED",50),
    TRADE_SUCCESS	("TRADE_SUCCESS",20),
    WAIT_BUYER_PAY	 ("WAIT_BUYER_PAY",10),
    TRADE_CLOSED("TRADE_CLOSED",60)
    ;

    private String trade;
    private int status;
    TradeStatusEnum(String trade,int staus){
        this.trade=trade;
        this.status=staus;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public static int  statusof(String trade){

        for(TradeStatusEnum statusEnum:values()){
            if(trade.equals(statusEnum.getTrade())){
                return statusEnum.getStatus();
            }
        }
        return 0;
    }

}
