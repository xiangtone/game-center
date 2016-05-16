
package com.hykj.gamecenter.account;

import com.hykj.gamecenter.protocol.Pay.UserAccInfo;
import com.hykj.gamecenter.protocol.UAC.AccountInfo;

/**
 * 包含账号信息AccoutInfo{@link UserInfo} 和账户信息UserAccInfo {@link UserAccInfo}, 登陆
 * token
 * 
 * @author ####oddshou
 */
public class CSUserInfo
{

    // // 登录Token
    // private String mUserToken;

    // 用户基本信息
    private AccountInfo mAccountInfo;

    // 用户账户信息
    private UserAccInfo mAccInfo;

    // 用户头像(现在存在本地，所以暂时不使用UserInfo中的headPicUrl字段)
    private String mUserPic;

    public CSUserInfo()
    {
        // mUserInfo = UserInfo.newBuilder().build();
        // mAccountInfo = UserAccInfo.newBuilder().build();
    }

    // public String getUserToken()
    // {
    // return mUserToken;
    // }
    //
    // public void setUserToken( String mUserToken )
    // {
    // this.mUserToken = mUserToken;
    // }

    public AccountInfo getAccountInfo()
    {
        return mAccountInfo;
    }

    public void setAccountInfo(AccountInfo mUserInfo)
    {
        this.mAccountInfo = mUserInfo;
    }

    public UserAccInfo getAccInfo()
    {
        return mAccInfo;
    }

    public void setAccInfo(UserAccInfo mAccountInfo)
    {
        this.mAccInfo = mAccountInfo;
    }

    public String getUserPic()
    {
        return mUserPic;
    }

    public void setUserPic(String mUserPic)
    {
        this.mUserPic = mUserPic;
    }
    
    public void reset(){
        mAccInfo = null;
        mAccountInfo = null;
        mUserPic = null;
    }

    @Override
    public String toString()
    {
        // return "PadaUserInfo [mUserToken=" + mUserToken + ", mUserInfo=" +
        // mUserInfo + ", mAccountInfo=" + mAccountInfo + ", mUserPic=" +
        // mUserPic + "]";
        return "PadaUserInfo [mUserInfo=" + mAccountInfo + ", mAccountInfo=" + mAccInfo
                + ", mUserPic=" + mUserPic + "]";
    }
}
