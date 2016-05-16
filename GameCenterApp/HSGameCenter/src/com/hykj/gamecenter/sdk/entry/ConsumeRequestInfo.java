package com.hykj.gamecenter.sdk.entry;

/*
 *
 //============================================ 消费
message ReqConsume{
    required string           openId = 1;                   // 用户OpenId
    required string           token = 2;                    // 用户Token  
    required int32            appId = 3;                    // 来源应用id
    required string           appToken = 4;                 // 应用Token
    required string           roleId = 5;                   // 角色id
    required string           roleToken = 6;                // 角色Token 
    required int32            consumeNewCoin = 7;           // 消费New币
    optional string           cpOrderNo = 8;                // 商户订单号
    optional string           productCode = 9;              // 消费商品的代码
    optional string           productName = 10;             // 消费商品的名称
    optional int32            productCount = 11;            // 消费商品的数量 
    optional string           packName = 12;                // 包名
    optional string           exInfo = 13;                  // 附加信息，CP定义，服务端可以返回该值
}
message RspConsume{
    required int32            rescode = 1;                  // 响应码（定义：0=成功，1=消费失败，2=参数错误，3=余额不足）
    required string           resmsg = 2;                   // 消息串 
    optional int32            newCoin = 3;                  // new币余额
    optional int32            consumeNewCoin = 4;           // 消费new币 
    optional string           orderNo = 5;                  // 订单号
    optional string           orderDate = 6;                // 订单日期，格式：yyyyMMDDHHmmss
}

 */
public class ConsumeRequestInfo
{
    public String openId;
    public String token;
    public int appId;
    public String appToken;
    public String roleId;
    public String roleToken;
    public int consumeNewCoin;
    public String cpOrderNo;
    public String productCode;
    public String productName;
    public int productCount;
    public String packName;
    public String exInfo;
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "ConsumeRequestInfo [ openId = " + openId + ", token = " + token+ ", appId = " + appId+ ", appToken = " + appToken
                + ", roleId = " + roleId+ ", roleToken = " + roleToken+ ", consumeNewCoin = " + consumeNewCoin
                +", cpOrderNo = " + cpOrderNo+ ", productCode = " + productCode
                +", productName = " + productName+ ", productCount = " + productCount+ ", packName = " + packName+ ", exInfo = " + exInfo;
    }

}
