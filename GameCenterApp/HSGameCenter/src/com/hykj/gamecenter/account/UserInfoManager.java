
package com.hykj.gamecenter.account;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.db.CSSFContentProvider;
import com.hykj.gamecenter.db.CSSFDatabaseHelper.UserInfoColumns;
import com.hykj.gamecenter.protocol.Pay.UserAccInfo;
import com.hykj.gamecenter.protocol.UAC.AccountInfo;
import com.hykj.gamecenter.utils.Logger;

/**
 * @author oddshou
 * @version 1.0 管理当前登录态的用户信息 {@link CSUserInfo} 及相关操作（初始化、读取、更改，记录进数据库等）
 */
public class UserInfoManager
{

    private static final String TAG = "UserInfoManager";

    private static UserInfoManager mInstance;

    // 保存当前登录态的用户信息（id,name,token,账户信息等）
    private final CSUserInfo mCSUserInfo;

    private UserInfoManager()
    {
        mCSUserInfo = new CSUserInfo();
    }

    public static UserInfoManager getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new UserInfoManager();
        }
        return mInstance;
    }
    // 更新 username, nickName
    public void updateAccountName(String name, AccountInfo oldAccount){
        String openId = oldAccount.openId;
        ContentResolver cr = App.getAppContext().getContentResolver();
        Cursor cursor = cr.query(CSSFContentProvider.USERINFO_CONTENT_URI, 
            new String[] {UserInfoColumns.OPEN_ID}, 
            UserInfoColumns.OPEN_ID + " = " + "'" + openId + "'", 
            null, 
            null
        );
        // 说明用户已存在，只用更新信息
        if (cursor != null && cursor.getCount() > 0)
        {
            ContentValues values = new ContentValues();
            values.put(UserInfoColumns.OPEN_ID, oldAccount.openId);
            values.put(UserInfoColumns.UESR_TOKEN, oldAccount.token);
            values.put(UserInfoColumns.USER_NAME, name);
            values.put(UserInfoColumns.HEAD_PIC_URL, oldAccount.headImgUrl);
            values.put(UserInfoColumns.USER_ACCOUNT_ACTIVATE_TIME, System.currentTimeMillis());
            cr.update(CSSFContentProvider.USERINFO_CONTENT_URI, values, UserInfoColumns.OPEN_ID
                    + " = " + "'" + openId + "'", null);
//            mCSUserInfo.getAccountInfo().userName = name;
            mCSUserInfo.getAccountInfo().nickName = name;
        }
        if (cursor != null)
        {
            cursor.close();
        }
    }

    public void setAccountInfo(AccountInfo userInfo/*, String token*/)
    {
        long a = java.lang.System.currentTimeMillis();

        ContentResolver cr = App.getAppContext().getContentResolver();
        Cursor cursor = cr.query(CSSFContentProvider.USERINFO_CONTENT_URI, 
            new String[] {UserInfoColumns.OPEN_ID}, 
            UserInfoColumns.OPEN_ID + " = " + "'" + userInfo.openId + "'", 
            null, 
            null
        );

        ContentValues values = new ContentValues();
        values.put(UserInfoColumns.OPEN_ID, userInfo.openId);
        values.put(UserInfoColumns.UESR_TOKEN, userInfo.token);
//        values.put(UserInfoColumns.to, value);
        values.put(UserInfoColumns.USER_NAME, userInfo.nickName/*userName*/);
        values.put(UserInfoColumns.USER_ACCOUNT_ACTIVATE_TIME, System.currentTimeMillis());
        values.put(UserInfoColumns.HEAD_PIC_URL, userInfo.headImgUrl);
//        if (!TextUtils.isEmpty(token))
//        {
//            values.put(UserInfoColumns.UESR_TOKEN, token);
//            mCSUserInfo.setUserToken(token);
//        }

        // 说明用户已存在，只用更新信息
        if (cursor != null && cursor.getCount() > 0)
        {
            cr.update(CSSFContentProvider.USERINFO_CONTENT_URI, values, UserInfoColumns.OPEN_ID
                    + " = " + "'" + userInfo.openId + "'", null);
            Logger.d(TAG, "setAccountInfo  update account " + userInfo.openId, "oddshou");
        }
        else
        {
            cr.insert(CSSFContentProvider.USERINFO_CONTENT_URI, values);
            Logger.d(TAG, "setAccountInfo insert account " + userInfo.openId, "oddshou");
        }
        if (cursor != null)
        {
            cursor.close();
        }
        mCSUserInfo.setAccountInfo(userInfo);
        long b = java.lang.System.currentTimeMillis();
        Logger.e("UM", "setUserInfoInDB:" + (b - a));

    }

//    public void setUserHeadPic(String headPic)
//    {
//        mCSUserInfo.setUserPic(headPic);
//        ContentResolver cr = App.getAppContext().getContentResolver();
//        ContentValues values = new ContentValues();
//        values.put(UserInfoColumns.HEAD_PIC_URL, headPic);
//        cr.update(CSSFContentProvider.USERINFO_CONTENT_URI, values, UserInfoColumns.OPEN_ID + " = "
//                + "'" + mCSUserInfo.getUserInfo().openId + "'", null);
//    }
    /**
     * 从数据库缓存中初始化 账号，账户 数据,如果已经初始化则不再初始化
     */
    public void initUserInfo()
    {
        if (mCSUserInfo.getAccountInfo() != null)
        {
            return;
        }
        ContentResolver cr = App.getAppContext().getContentResolver();
        Cursor cursor = cr.query(CSSFContentProvider.USERINFO_CONTENT_URI, null, null, null,
                UserInfoColumns.USER_ACCOUNT_ACTIVATE_TIME + " DESC");
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst())
        {
            String openId = cursor.getString(cursor.getColumnIndex(UserInfoColumns.OPEN_ID));
            String username = cursor.getString(cursor.getColumnIndex(UserInfoColumns.USER_NAME));
            String token = cursor.getString(cursor.getColumnIndex(UserInfoColumns.UESR_TOKEN));
            String headPic = cursor.getString(cursor.getColumnIndex(UserInfoColumns.HEAD_PIC_URL));
            int niucoint = cursor.getInt(cursor.getColumnIndex(UserInfoColumns.ACCOUNT_PACOIN));
            int consumeCount = cursor.getInt(cursor.getColumnIndex(UserInfoColumns.CONSUME_COUNT));
            int rechargeCount = cursor.getInt(cursor.getColumnIndex(UserInfoColumns.RECHARGE_COUNT));
            // UserInfo.Builder builder = UserInfo.newBuilder( );
            // builder.setUid( uid );
            // builder.setUserName( username );
            AccountInfo builder = new AccountInfo();
            builder.openId = openId;
//            builder.userName = username;
            builder.nickName = username;
            builder.headImgUrl = headPic;
            builder.token = token;

            // UserAccInfo.Builder accBuilder = UserAccInfo.newBuilder( );
            UserAccInfo accBuilder = new UserAccInfo();
            accBuilder.openId = openId;
            accBuilder.newCoin = niucoint;
            accBuilder.rechargeCount = rechargeCount;
            accBuilder.consumeCount = consumeCount;

            mCSUserInfo.setAccountInfo( /* builder.build( ) */builder);
            mCSUserInfo.setAccInfo(accBuilder);
//            mCSUserInfo.setUserToken(token);
            mCSUserInfo.setUserPic(headPic);
            cursor.close();
        }
        if (cursor != null)
        {
            cursor.close();
        }
    }

    public void setAccInfo(UserAccInfo accInfo)
    {
        ContentResolver cr = App.getAppContext().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(UserInfoColumns.ACCOUNT_PACOIN, accInfo.newCoin);
        values.put(UserInfoColumns.RECHARGE_COUNT, accInfo.rechargeCount);
        values.put(UserInfoColumns.CONSUME_COUNT, accInfo.consumeCount);
        cr.update(CSSFContentProvider.USERINFO_CONTENT_URI, values, UserInfoColumns.OPEN_ID + " = "
                +"'"+ mCSUserInfo.getAccountInfo().openId + "'", null);
        mCSUserInfo.setAccInfo(accInfo);
    }

//    public void setUserToken(String token)
//    {
//        ContentResolver cr = App.getAppContext().getContentResolver();
//        ContentValues values = new ContentValues();
//        values.put(UserInfoColumns.UESR_TOKEN, token);
//        cr.update(CSSFContentProvider.USERINFO_CONTENT_URI, values, UserInfoColumns.OPEN_ID + " = "
//                +"'"+ mCSUserInfo.getUserInfo().openId + "'", null);
//        mCSUserInfo.setUserToken(token);
//    }

    public CSUserInfo getCSUserInfo()
    {
        return mCSUserInfo;
    }

    
//    public void resetUserInfo(){
//        mCSUserInfo = null;
//    }
//    public void setUserHeadpic(String headPic)
//    {
//        mCSUserInfo.setUserPic(headPic);
//    }
}
