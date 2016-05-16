
package com.hykj.gamecenter.account;

import java.util.ArrayList;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
//import com.niuwan.gamecenter.controller.PSFProtocolListener.LoginListener;
//import com.niuwan.gamecenter.controller.PSFProtocolListener.RegisterListener;
import com.hykj.gamecenter.db.CSSFContentProvider;
import com.hykj.gamecenter.db.CSSFDatabaseHelper.UserInfoColumns;
import com.hykj.gamecenter.protocol.UAC.AccountInfo;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.utils.Logger;

/**
 * 对CS账号进行统一管理的单例类
 */
public class CSAccountManager
{

    private static CSAccountManager mCSAccountManager = null;

    private final Context mContext;
    private final AccountManager mAccountManager;
    //    private final UserInfoManager mUserInfoManager;
//    private LoginListener loginListener = null;
//    private RegisterListener registerListener = null;

    // 登陆、注册结果状态
    public static final int LOGIN_NOT_CONNECTED = 2001;
    public static final int LOGIN_ACCOUNT_INVALID = 2002;
    public static final int LOGIN_PWD_MISMATCH = 2003;
    public static final int LOGIN_SUCCESS = 2004;
    // 其他错误，比如RSA-KEY过期等
    public static final int LOGIN_ACTION_FAIL = 2005;

    public static final int REGISTER_NOT_CONNECTED = 3001;
    public static final int REGISTER_NAME_INVALID = 3002;
    public static final int REGISTER_PWD_INVALID = 3003;
    public static final int REGISTER_SUCCESS = 3004;
    // 其他错误，比如RSA-KEY过期等
    public static final int REGISTER_ACTION_FAIL = 3005;

    private static final String KEY_PADA_ACCOUNT_NAME = "com.cs.account.name";
    private static final String KEY_PADA_ACCOUNT_AMOUNT = "com.cs.account.amount";

    private CSAccountManager(Context context)
    {
        mContext = context;
        mAccountManager = AccountManager.get(context);
        //        mUserInfoManager = UserInfoManager.getInstance();
    }

    public static synchronized CSAccountManager getInstance(Context context)
    {
        if (mCSAccountManager == null)
        {
            mCSAccountManager = new CSAccountManager(context);
        }
        return mCSAccountManager;
    }

    public boolean hasCSAccount()
    {
        Account[] accounts = mAccountManager.getAccountsByType(mContext
                .getString(R.string.cs_accounttype));
        return accounts.length > 0;
    }

    public void removeCSAccount()
    {
        Account[] accounts = mAccountManager.getAccountsByType(mContext
                .getString(R.string.cs_accounttype));
        for (int i = 0; i < accounts.length; i++)
        {
            mAccountManager.removeAccount(accounts[i], null, null);
        }
        //清空 openid 和  token 缓存
        Editor edit = App.getSharedPreference().edit();
        edit.putString(StatisticManager.KEY_OPENID, "");
        edit.putString(StatisticManager.KEY_TOKEN, "");
        edit.commit();
        //信鸽账号注销
//        XGPushManager.unregisterPush(mContext.getApplicationContext());
        UserInfoManager.getInstance().getCSUserInfo().reset();
    }

    /**
     * 增加loginListener，监听登录消息的结果
     * 
     * @param loginListener
     */
//    public void addLoginListener(LoginListener loginListener)
//    {
//        this.loginListener = loginListener;
//    }

    /**
     * 增加registerListener,监听消息注册的结果
     * 
     * @param registerListener
     */
//    public void addRegistListener(RegisterListener registerListener)
//    {
//        this.registerListener = registerListener;
//    }

    public void login(String name, String pwd, int chnNo)
    {
        sendLoginRequest(name, pwd, chnNo);
    }

    public void register(String pwd, int chnNo)
    {
        sendRegRequest(pwd, chnNo);
    }

    private void sendLoginRequest(String name, String pwd, int chnNo)
    {
        /* PSFLoginController controller = new PSFLoginController(name, pwd, loginListener);
         controller.setChnNo(chnNo);
         controller.doRequest();*/
    }

    private void sendRegRequest(String pwd, int chnNo)
    {
        /* PSFRegisterController contoller = new PSFRegisterController(pwd, registerListener);
         contoller.setChnNo(chnNo);
         contoller.doRequest();*/
    }

    public void updateAccountUserData( AccountInfo userInfo )
    {
    // 设置用户名到accountMananger，用于系统设置里显示
    Account [] csAccounts = mAccountManager.getAccountsByType( mContext.getString( R.string.cs_accounttype ) );
    if( csAccounts.length <= 0 )
    {
        Logger.e( "CSAccountManager" , "updateAccountUserData fail, csAccount is not exist!" );
        return;
    }
    mAccountManager.setUserData( csAccounts[0] , KEY_PADA_ACCOUNT_NAME , userInfo.nickName/*userName*/ );

    }

    public ArrayList<String> getLocalCSAccounts()
    {
        ArrayList<String> data = new ArrayList<String>();
        ContentResolver cr = App.getAppContext().getContentResolver();
        Cursor cursor = cr.query(CSSFContentProvider.USERINFO_CONTENT_URI, null, null, null,
                UserInfoColumns.USER_ACCOUNT_ACTIVATE_TIME + " DESC");
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                String name = cursor.getString(cursor.getColumnIndex(UserInfoColumns.USER_NAME));
                data.add(name);
                cursor.moveToNext();
            }
        }
        if (cursor != null)
        {
            cursor.close();
        }
        return data;
    }

}
