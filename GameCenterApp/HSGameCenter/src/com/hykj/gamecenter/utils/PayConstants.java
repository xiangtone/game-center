
package com.hykj.gamecenter.utils;

public class PayConstants {

    public static final int KEY_PAY_TYPE_ALIPAY = 1;//支付方式(1:默认支付宝)
    public static final int KEY_PAY_TYPE_WECHATPAY = 2; //支付方式(2:微信支付)
    public static final int KEY_PAY_TYPE_UNIONPAY = 3; //支付方式(3:银联支付)
    public static final int KEY_PAY_TYPE_MOBILEPAYFIR = 4; //支付方式(4:手机充值卡支付 易宝渠道)
    public static final int KEY_PAY_TYPE_MOBILEPAYSEC = 5; //支付方式(5:手机充值卡支付 神州付渠道)

    public static final String KEY_PAY_TYPE_STRING_ALIPAY = "1";//支付方式(1:默认支付宝)
    public static final String KEY_PAY_TYPE_STRING_WECHATPAY = "2"; //支付方式(2:微信支付)
    public static final String KEY_PAY_TYPE_STRING_UNIONPAY = "3"; //支付方式(3:银联支付)

    public static final String KEY_PAYTYPE_MOBILE = "key_paytype_mobile";

    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_CHECK_FLAG = 2;
    /** 微信 参数 */
    //    public static final String APP_ID = "wx4164917f32b41662"; //  应用从官方网站申请到的合法appId
    //    public static final String PARTNER_ID = "1233332202";//   商家向财付通申请的商家id  

    /** 业务类型 (1:话费充值,2:电影,3:团购,4:游戏中心充值,5:游戏中心消费) */
    public static final int KEY_PAY_OPERATION_RECHARGE = 4;
    public static final int KEY_PAY_OPERATION_CONSUME = 5;

    /** 充值记录map所需的string 参数 */
    public static final String RECHARGE_LISTITEM_ORDERNO = "recharge_listitem_orderno";
    public static final String RECHARGE_LISTITEM_NEWCOIN = "recharge_listitem_newcoin";
    public static final String RECHARGE_LISTITEM_TYPE = "recharge_listitem_type";
    public static final String RECHARGE_LISTITEM_TYPE_DES = "recharge_listitem_type_des";
    public static final String RECHARGE_LISTITEM_ACCOUNT = "recharge_listitem_account";
    public static final String RECHARGE_LISTITEM_SUBMITTIME = "recharge_listitem_submittime";
    public static final String RECHARGE_LISTITEM_STATUS = "recharge_listitem_status";

    /** 消费记录map所需的string 参数 */
    public static final String CONSUME_LIST_ORDERNO = "consume_listitem_orderno";
    public static final String CONSUME_LIST_PACKNAME = "consume_listitem_packname";
    public static final String CONSUME_LIST_APPNAME = "consume_listitem_appname";
    public static final String CONSUME_LIST_PRODUCTNAME = "consume_listitem_productname";
    public static final String CONSUME_LIST_CONSUMECOIN = "consume_listitem_consumecoin";
    public static final String CONSUME_LIST_CONSUME_TIME = "consume_listitem_time";
    public static final String CONSUME_LIST_CONSUME_STATUS = "consume_listitem_status";

    /** 保存界面切换时订单数据 */
    public static final String RECHARGE_STORE_AMOUNT = "recharge_store_amount";
    public static final String RECHARGE_STORE_ORDERNO = "recharge_store_orderno";
}
