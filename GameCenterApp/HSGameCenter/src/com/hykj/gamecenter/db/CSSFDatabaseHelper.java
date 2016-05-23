
package com.hykj.gamecenter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class CSSFDatabaseHelper extends SQLiteOpenHelper
{

    private static final String DB_NAME = "cssf.db";
    /**
     * 1.版本 2.版本 修改了 UserInfo 表 , 修改 UserInfo uid -> openid int -> text
     */
    // 
    private static final int DB_VERSION_1 = 1;
    // 
    private static final int DB_VERSION_2 = 2;
    // 
    private static final int DB_VERSION_3 = 3;
    //当前数据版本
    private static final int DB_VERSION = 2;

    public interface Tables
    {
        String ConsumeRecords = "consume_records"; // 消费记录表
        String RechargeRecords = "recharge_records"; // 充值记录表
        String PlayedGames = "played_game"; // 玩过的游戏表
        String UserInfo = "user_info"; // 登录的用户信息
        String RechargeAmounts = "recharge_amounts"; // 充值额度
    }

    /*public interface ConsumerRecordsColumns
    {
        public static final String CONSUME_ID = "consume_id"; // 消费订单ID
        public static final String APP_ID = "app_id";
        public static final String APP_NAME = "app_name"; // 消费APP的名字
        public static final String APP_PACKAGE_NAME = "app_package_name"; // 消费APP的包名(用于后续查询图标等信息)
        public static final String ROLE_ID = "role_id"; // 游戏帐号id
        public static final String ROLE_NAME = "role_name"; // 游戏帐号名字
        public static final String ITEM_NAME = "item_name"; // 消费的物品名称
        public static final String ITEM_VALUE = "item_value"; // 消费的金额（以Pa币为单位）
        public static final String CONSUME_DATE = "consume_date"; // 消费时间
    }*/

    public interface ConsumerRecordsColumns
    {
        String CONSUME_ORDERNO = "consume_orderno"; // 消费ID，唯一标识该条消费记录
        String CONSUME_OPENID = "consume_openid"; //
        String CONSUME_ROLEID = "consume_roleid"; // 角色ID
        String CONSUME_CPORDER = "consume_cporder"; // 商户订单号
        String CONSUME_APPID = "consume_appid"; // 来源应用
        String CONSUME_APPNAME = "consume_appname"; // 来源应用的名称
        String CONSUME_PACKNAME = "consume_packname"; // 包名
        String CONSUME_CONSUMECOIN = "consume_consumecoin"; // 消费New币
        String CONSUME_PRODUCTCODE = "consume_productcode"; // 消费商品的代码
        String CONSUME_PRODUCTNAME = "consume_productname"; // 消费商品的名称
        String CONSUME_PRODUCTCOUNT = "consume_productcount"; // 消费商品的数量
        String CONSUME_TIME = "consume_time"; // 消费时间，yyyyMMddHHmmss
        String CONSUME_STATUS = "consume_status"; // 消费状态，1:消费失败,2:消费成功
    }

    /*public interface RechargeRecordsColumns
    {
        public static final String RECHARGE_ID = "recharge_id"; // 充值订单号ID
        public static final String RECHARGE_FLAG = "recharge_flag"; // 充值类型（充值卡、支付宝等）
        public static final String RECHARGE_VALUE = "recharge_value"; // 充值金额（以Pa币为单位）
        public static final String RECHARGE_CARD_NUM = "recharge_card_num"; // 充值卡号
        public static final String RECHARGE_DATE = "recharge_date"; // 充值时间
        public static final String RECHARGE_STATUS = "recharge_status"; // 充值单状态
    }*/

    public interface RechargeRecordsColumns
    {
        String RECHARGE_ORDERNO = "recharge_orderno"; // 充值订单号，唯一标识该条充值记录
        String RECHARGE_OPENID = "recharge_openid"; //
        String RECHARGE_AMT = "recharge_amt"; // 充值金额，单位：分
        String RECHARGE_CONFIRMAMT = "recharge_confirmamt"; //  确认金额，单位：分
        String RECHARGE_CONFIRMCOIN = "recharge_confirmcoin"; //  确认游戏币 单位：(New币)
        String RECHARGE_TYPE = "recharge_type"; // 渠道类型，1:支付宝，2:微信,3=银联，4=手机充值卡
        String RECHARGE_FLAG = "recharge_flag"; // 渠道标识
        String RECHARGE_ACCOUNT = "recharge_account"; // 充值帐号，如点卡卡号、电话充值卡卡号等
        String RECHARGE_SUBMITTIME = "recharge_submittime"; // 提交时间，yyyyMMddHHmmss
        String RECHARGE_CONFIRMTIME = "recharge_confirmtime"; // 确认时间，yyyyMMddHHmmss
        String RECHARGE_STATUS = "recharge_status"; // 充值状态，1:未处理,2:处理中,3:交易成功,4:交易失败,5:已退款
    }

    public interface PlayedGamesColumns
    {
        String APP_ID = "app_id"; // APP ID
        String APP_NAME = "app_name"; // APP名字
        String APP_PACKAGE_NAME = "app_package_name"; // 包名,在此表中，一个app只有一条记录
        String APP_SUB_ACCOUNT = "app_sub_account"; // 该游戏在当前账号下的子账号数目
        String APP_LAST_OPENED = "app_last_opened"; // 最近一次打开该游戏的时间
        String APP_ICON = "app_icon"; // 游戏icon，存储以保证该游戏在本地已经被删除是能显示icon信息
    }

    public interface UserInfoColumns
    {
        String OPEN_ID = "open_id"; // 用户id
        String USER_NAME = "name"; // 用户名
        String USER_SEX = "sex"; // 用户性别
        String BIND_MAIL = "mail"; // 绑定邮箱
        String BIND_MOBILE = "mobile"; // 绑定手机
        String HEAD_PIC_URL = "head_pic_url"; // 头像图片地址URL
        String LAST_LOG_TIME = "last_log_time"; // 上次登录时间
        String REGISTER_TIME = "reg_time"; // 注册时间
        String USER_STATUS = "user_status"; // 用户状态（正常/禁用）
        String USER_ACCOUNT_STATUS = "user_account_status"; // 用户账户状态（正常/禁用）
        String USER_ACCOUNT_ACTIVATE_TIME = "user_account_activate_time"; // 账户激活时间
        String ACCOUNT_PACOIN = "account_pacoin"; // 账户niu币余额
        String UESR_TOKEN = "token"; // 登陆Token
        // oddshou############
        String RECHARGE_COUNT = "recharge_count"; //充值次数
        String CONSUME_COUNT = "consume_count"; //消费次数
        // oddshou############
    }

    public interface RechargeAmountsColumns
    {
        String RECHARGE_AMOUNTS_TYPE = "recharge_amounts_type"; //类型标识,如支付宝微信等
        String RECHARGE_AMOUNTS_PRICE = "recharge_amounts_price";//具体的充值面额
        String RECHARGE_AMOUNTS_SHOWVALUE = "recharge_amounts_showvalue";//定义是否为选中的面额 1为选中
    }

    /* private static final String CREATE_TABLE_CONSUMERECORDS = "CREATE TABLE IF NOT EXISTS "
             + Tables.ConsumeRecords + "(" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
             + ConsumerRecordsColumns.CONSUME_ID + " TEXT," + ConsumerRecordsColumns.APP_ID
             + " INTEGER NOT NULL DEFAULT 0," + ConsumerRecordsColumns.APP_NAME + " TEXT,"
             + ConsumerRecordsColumns.APP_PACKAGE_NAME + " TEXT," + ConsumerRecordsColumns.ROLE_ID
             + " TEXT," + ConsumerRecordsColumns.ROLE_NAME + " TEXT,"
             + ConsumerRecordsColumns.ITEM_NAME
             + " TEXT," + ConsumerRecordsColumns.ITEM_VALUE + " INTEGER NOT NULL DEFAULT 0,"
             + ConsumerRecordsColumns.CONSUME_DATE + " INTEGER NOT NULL DEFAULT 0" + ");";*/

    private static final String CREATE_TABLE_CONSUMERECORDS = "CREATE TABLE IF NOT EXISTS "
            + Tables.ConsumeRecords + "(" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ConsumerRecordsColumns.CONSUME_ORDERNO + " TEXT,"
            + ConsumerRecordsColumns.CONSUME_OPENID + " TEXT,"
            + ConsumerRecordsColumns.CONSUME_ROLEID + " INTEGER NOT NULL DEFAULT 0,"
            + ConsumerRecordsColumns.CONSUME_CPORDER + " TEXT,"
            + ConsumerRecordsColumns.CONSUME_APPID + " INTEGER NOT NULL DEFAULT 0,"
            + ConsumerRecordsColumns.CONSUME_APPNAME + " TEXT,"
            + ConsumerRecordsColumns.CONSUME_PACKNAME + " TEXT,"
            + ConsumerRecordsColumns.CONSUME_CONSUMECOIN + " INTEGER NOT NULL DEFAULT 0,"
            + ConsumerRecordsColumns.CONSUME_PRODUCTCODE + " TEXT,"
            + ConsumerRecordsColumns.CONSUME_PRODUCTNAME + " TEXT,"
            + ConsumerRecordsColumns.CONSUME_PRODUCTCOUNT + " INTEGER NOT NULL DEFAULT 0,"
            + ConsumerRecordsColumns.CONSUME_TIME + " TEXT,"
            + ConsumerRecordsColumns.CONSUME_STATUS + " INTEGER NOT NULL DEFAULT 0" + ");";

    /*private static final String CREATE_TABLE_RECHARGERECORDS = "CREATE TABLE IF NOT EXISTS "
            + Tables.RechargeRecords + "(" + BaseColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + RechargeRecordsColumns.RECHARGE_ID + " TEXT,"
            + RechargeRecordsColumns.RECHARGE_FLAG
            + "  TEXT," + RechargeRecordsColumns.RECHARGE_VALUE + "  INTEGER NOT NULL DEFAULT 0,"
            + RechargeRecordsColumns.RECHARGE_CARD_NUM + " TEXT,"
            + RechargeRecordsColumns.RECHARGE_DATE + " INTEGER NOT NULL DEFAULT 0,"
            + RechargeRecordsColumns.RECHARGE_STATUS
            + " INTEGER NOT NULL DEFAULT 0" + ");";*/

    private static final String CREATE_TABLE_RECHARGERECORDS = "CREATE TABLE IF NOT EXISTS "
            + Tables.RechargeRecords + "(" + BaseColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + RechargeRecordsColumns.RECHARGE_ORDERNO + " TEXT,"
            + RechargeRecordsColumns.RECHARGE_OPENID + " TEXT,"
            + RechargeRecordsColumns.RECHARGE_AMT + "  INTEGER NOT NULL DEFAULT 0,"
            + RechargeRecordsColumns.RECHARGE_CONFIRMAMT + "  INTEGER NOT NULL DEFAULT 0,"
            + RechargeRecordsColumns.RECHARGE_CONFIRMCOIN + "  INTEGER NOT NULL DEFAULT 0,"
            + RechargeRecordsColumns.RECHARGE_TYPE + "  INTEGER NOT NULL DEFAULT 0,"
            + RechargeRecordsColumns.RECHARGE_FLAG + "  TEXT,"
            + RechargeRecordsColumns.RECHARGE_ACCOUNT + "  TEXT,"
            + RechargeRecordsColumns.RECHARGE_SUBMITTIME + "  TEXT,"
            + RechargeRecordsColumns.RECHARGE_CONFIRMTIME + "  TEXT,"
            + RechargeRecordsColumns.RECHARGE_STATUS
            + " INTEGER NOT NULL DEFAULT 0" + ");";

    private static final String CREATE_TABLE_RECHARGEAMOUNTS = "CREATE TABLE IF NOT EXISTS "
            + Tables.RechargeAmounts + "(" + BaseColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + RechargeAmountsColumns.RECHARGE_AMOUNTS_TYPE + "  INTEGER NOT NULL DEFAULT 0,"
            + RechargeAmountsColumns.RECHARGE_AMOUNTS_PRICE
            + " INTEGER NOT NULL DEFAULT 0,"
            + RechargeAmountsColumns.RECHARGE_AMOUNTS_SHOWVALUE
            + " TEXT" + ");";

    private static final String CREATE_TABLE_PLAYEDGAMES = "CREATE TABLE IF NOT EXISTS "
            + Tables.PlayedGames + "(" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PlayedGamesColumns.APP_ID + " INTEGER NOT NULL DEFAULT 0,"
            + PlayedGamesColumns.APP_NAME + " TEXT," + PlayedGamesColumns.APP_PACKAGE_NAME
            + " TEXT," + PlayedGamesColumns.APP_SUB_ACCOUNT
            + "  INTEGER NOT NULL DEFAULT 1," + PlayedGamesColumns.APP_LAST_OPENED
            + "  INTEGER NOT NULL DEFAULT 0," + PlayedGamesColumns.APP_ICON + " BLOB" + ");";

    private static final String CREATE_TABLE_USERINFO = "CREATE TABLE IF NOT EXISTS "
            + Tables.UserInfo + "(" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + UserInfoColumns.OPEN_ID
            + " TEXT," + UserInfoColumns.USER_NAME + " TEXT," + UserInfoColumns.USER_SEX
            + " INTEGER NOT NULL DEFAULT 0," + UserInfoColumns.BIND_MAIL + " TEXT,"
            + UserInfoColumns.BIND_MOBILE + " TEXT," + UserInfoColumns.HEAD_PIC_URL + " TEXT,"
            + UserInfoColumns.LAST_LOG_TIME + " INTEGER," + UserInfoColumns.REGISTER_TIME
            + " INTEGER,"
            + UserInfoColumns.USER_STATUS + " INTEGER," + UserInfoColumns.USER_ACCOUNT_STATUS
            + " INTEGER," + UserInfoColumns.USER_ACCOUNT_ACTIVATE_TIME + " INTEGER,"
            + UserInfoColumns.ACCOUNT_PACOIN
            + " INTEGER NOT NULL DEFAULT 0," + UserInfoColumns.UESR_TOKEN + " TEXT,"
            + UserInfoColumns.RECHARGE_COUNT + " INTEGER NOT NULL DEFAULT 0,"
            + UserInfoColumns.CONSUME_COUNT
            + " INTEGER NOT NULL DEFAULT 0" + ");";

    private static final String DELETE_USERINFO_TABLE = "DROP TABLE IF EXISTS " + Tables.UserInfo
            + ";";

    public CSSFDatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_CONSUMERECORDS);
        db.execSQL(CREATE_TABLE_RECHARGERECORDS);
        db.execSQL(CREATE_TABLE_PLAYEDGAMES);
        db.execSQL(CREATE_TABLE_USERINFO);
        db.execSQL(CREATE_TABLE_RECHARGEAMOUNTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (newVersion >= 2) {
            db.execSQL(DELETE_USERINFO_TABLE);
            db.execSQL(CREATE_TABLE_USERINFO);
        }

    }

}
