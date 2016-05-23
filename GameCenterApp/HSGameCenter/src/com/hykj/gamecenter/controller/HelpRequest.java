
package com.hykj.gamecenter.controller;

import com.hykj.gamecenter.controller.ProtocolListener.ReqAddCommentListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqBindListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqConsumeListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqFeedbackListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqGetUserAppRolesListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqOpenIdListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqSetUserNameListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqThirdLoginListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqUserAccInfoListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqUserScoreInfoListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqValidateListener;
import com.hykj.gamecenter.protocol.Apps.UserScoreInfo;
import com.hykj.gamecenter.protocol.Pay.RspConsume;
import com.hykj.gamecenter.protocol.Pay.UserAccInfo;
import com.hykj.gamecenter.protocol.Pay.UserAppRoleInfo;
import com.hykj.gamecenter.protocol.UAC.AccountInfo;
import com.hykj.gamecenter.sdk.entry.ConsumeRequestInfo;
import com.hykj.gamecenter.utils.Logger;

/**
 * 网络请求辅助类 2015.07.01<p>
 * 添加步骤：1.添加请求在最后<p>
 * 2.添加接口继承 {@link IHelpRequest}
 * ########可以优化点 ： 所有的listener 格式 是固定的，可以尝试优化
 * 
 * @author oddshou
 */
public class HelpRequest {
    protected static final String TAG = "HelpRequest";
    private IHelpRequest mIHelpRequest;

    public static final int MSG_REQ_FAILED = 0X01;
    public static final int MSG_REQ_ERROR = 0X02;
    public static final int MSG_REQ_VALIDATESUCCEED = 0X03;
    public static final int MSG_REQ_BINDSUCCEED = 0X04;
    public static final int MSG_REQ_APPROLESSUCCEED = 0X05;
    public static final int MSG_REQ_CONSUMESUCCEED = 0X06;
    public static final int MSG_REQ_SETUSERNAMESUCCEED = 0X07;
    public static final int MSG_REQ_ACCINFOSUCCEED = 0X08;
    public static final int MSG_REQ_OPENIDSUCCEED = 0X09;
    public static final int MSG_REQ_FEEDBACKSUCCEED = 0X0A;
    public static final int MSG_REQ_ADDCOMMENTSUCCEED = 0X0B;
    public static final int MSG_REQ_USERSCOREINFOSUCCEED = 0x0C;

    public HelpRequest(IHelpRequest IRequset) {
        // TODO Auto-generated constructor stub
        this.mIHelpRequest = IRequset;
    }

    public interface IHelpRequest {
        void onNetError(int errCode, String errorMsg);

        void onReqFailed(int statusCode, String errorMsg);
    }

    public interface IReqThirdLoginSucceed extends IHelpRequest {
        void onReqThirdLoginSucceed(AccountInfo account);
    }

    public interface IReqSetUserNameSucceed extends IHelpRequest {
        /**
         * 修改用户名成功
         */
        void onReqSetUserNameSucceed();
    }

    public interface IReqAccInfoSucceed extends IHelpRequest {
        /**
         * 获取账号信息成功
         * 
         * @param accInfo
         */
        void onReqUserAccInfoSucceed(UserAccInfo accInfo);
    }

    public interface IReqValidateSucceed extends IHelpRequest {
        /**
         * 获取验证码成功
         */
        void onReqValidateSucceed();
    }

    public interface IReqBindSucceed extends IHelpRequest {
        /**
         * 绑定账号成功
         * 
         * @param account
         */
        void onReqBindSucceed(AccountInfo account);
    }

    public interface IReqAppRolesSucceed extends IHelpRequest {
        /**
         * 获取游戏角色信息成功
         * 
         * @param roleInfo
         */
        void onReqGetUserAppRolesSucceed(UserAppRoleInfo roleInfo);
    }

    public interface IReqConsumeSucceed extends IHelpRequest {
        /**
         * 请求消费成功
         * 
         * @param rspData
         */
        void onConsumeSucceed(RspConsume rspData);
    }

    public interface IReqOpenIdSucceed extends IHelpRequest {
        /**
         * 请求 openId 成功
         * 
         * @param accountInfo
         */
        void onReqOpenIdSucceed(AccountInfo accountInfo);
    }

     public interface IReqFeedbackSucceed extends IHelpRequest{
         /**
          * 反馈信息成功
          */
          void onReqFeedbackSucceed();
     }
     
     public interface IReqAddCommentSucceed extends IHelpRequest{
         /**
          * 添加或更新评论成功
          */
         void onReqAddCommentSucceed(long commentId, String userName);
     }
     
     public interface IReqUserScoreInfoSucceed extends IHelpRequest{
         /**
          * 获取评分成功
          * 
          * @param userScoreInfo
          */
         void onReqUserScoreInfoSucceed(UserScoreInfo userScoreInfo); 
     }

    /**
     * 请求 openId
     */
    public void reqOpenid() {
        ReqOpenIdController reqOpenid = new ReqOpenIdController("", "", mReqOpenIdListen);
        reqOpenid.doRequest();
    }

    private ReqOpenIdListener mReqOpenIdListen = new ReqOpenIdListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onNetError " + errCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onNetError(errCode, errorMsg);
        }

        @Override
        public void onReqOpenIdSucceed(AccountInfo accountInfo) {
            // TODO Auto-generated method stub
            Logger.d(TAG, "onReqReqOpenIdSucceed " + accountInfo, "oddshou");
            // mAccountInfo = accountInfo;
            // 将 openId token 保存在preference
            // if (accountInfo.openId.length() > 0 && accountInfo.token.length()
            // > 0) {
            //
            // Editor edit = App.getSharedPreference().edit();
            // edit.putString(StatisticManager.KEY_OPENID, accountInfo.openId);
            // edit.putString(StatisticManager.KEY_TOKEN, accountInfo.token);
            // edit.commit();
            //
            // reqValidate(accountInfo.openId, accountInfo.token);
            // }
            if (mIHelpRequest instanceof IReqOpenIdSucceed) {
                ((IReqOpenIdSucceed) mIHelpRequest).onReqOpenIdSucceed(accountInfo);
            }

        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onReqFailed " + statusCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onReqFailed(statusCode, errorMsg);
        }
    };

    /**
     * 请求验证码
     * 
     * @param openId
     * @param mobile
     */
    public void reqValidate(String openId, String mobile) {
        ReqValidateController reqValidate = new ReqValidateController(mReqValidateListen, openId,
                mobile);
        reqValidate.doRequest();
    }

    private ReqValidateListener mReqValidateListen = new ReqValidateListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onNetError " + errCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onNetError(errCode, errorMsg);

        }

        @Override
        public void onReqValidateSucceed() {
            // TODO Auto-generated method stub
            // mIHelpRequest.onReqValidateSucceed();
            if (mIHelpRequest instanceof IReqValidateSucceed) {
                ((IReqValidateSucceed) mIHelpRequest).onReqValidateSucceed();
            }
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onReqFailed " + statusCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onReqFailed(statusCode, errorMsg);

        }
    };

    /**
     * 请求绑定手机号
     * 
     * @param openId
     * @param mobile
     * @param verCode
     */
    public void reqBind(String openId, String mobile, String verCode, int source) {
        ReqBindController reqBind = new ReqBindController(mReqBindListen, openId, mobile, verCode,
                source);
        reqBind.doRequest();
    }

    private ReqBindListener mReqBindListen = new ReqBindListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Logger.e(TAG, "onNetError " + errCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onNetError(errCode, errorMsg);

        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            Logger.e(TAG, "onReqFailed " + statusCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onReqFailed(statusCode, errorMsg);

        }

        @Override
        public void onReqBindSucceed(AccountInfo account) {
            // mIHelpRequest.onReqBindSucceed(account);
            if (mIHelpRequest instanceof IReqBindSucceed) {
                ((IReqBindSucceed) mIHelpRequest).onReqBindSucceed(account);
            }
        }
    };

    /**
     * 请求角色信息
     * 
     * @param openId
     * @param token
     * @param appId
     * @param appToken
     */
    public void reqAppRoles(String openId, String token, int appId, String appToken) {
        ReqGetUserAppRolesController reqAppRoles = new ReqGetUserAppRolesController(openId, token,
                appId, appToken, mAppRolesListener);
        reqAppRoles.doRequest();
    }

    private ReqGetUserAppRolesListener mAppRolesListener = new ReqGetUserAppRolesListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onNetError " + errCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onNetError(errCode, errorMsg);
        }

        @Override
        public void onReqGetUserAppRolesSucceed(UserAppRoleInfo roleInfo) {
            // TODO Auto-generated method stub
            if (mIHelpRequest instanceof IReqAppRolesSucceed) {
                ((IReqAppRolesSucceed) mIHelpRequest).onReqGetUserAppRolesSucceed(roleInfo);
            }
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onReqFailed " + statusCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onReqFailed(statusCode, errorMsg);
        }
    };

    /**
     * 请求消费
     * 
     * @param consumeRequestInfo
     */
    public void reqConsume(ConsumeRequestInfo consumeRequestInfo) {
        ReqConsumeController reqConsumeController = new ReqConsumeController(consumeRequestInfo,
                mConsumeListener);
        reqConsumeController.doRequest();
    }

    private ReqConsumeListener mConsumeListener = new ReqConsumeListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onNetError " + errCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onNetError(errCode, errorMsg);
        }

        @Override
        public void onConsumeSucceed(RspConsume rspData) {
            // TODO Auto-generated method stub
            if (mIHelpRequest instanceof IReqConsumeSucceed) {
                ((IReqConsumeSucceed) mIHelpRequest).onConsumeSucceed(rspData);
            }
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onReqFailed " + statusCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onReqFailed(statusCode, errorMsg);
        }
    };

    /**
     * 请求账户信息
     * 
     * @param openId
     * @param token
     */
    public void reqUserAccInfo(String openId, String token) {
        ReqUserAccInfoController controller = new ReqUserAccInfoController(mReqUserAccInfoListen,
                openId, token);
        controller.doRequest();
    }

    private ReqUserAccInfoListener mReqUserAccInfoListen = new ReqUserAccInfoListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onNetError " + errCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onNetError(errCode, errorMsg);
        }

        @Override
        public void onReqUserAccInfoSucceed(UserAccInfo accInfo) {
            // TODO Auto-generated method stub
            Logger.i(TAG, "onReqUserAccInfoSucceed " + accInfo, "oddshou");
            if (mIHelpRequest instanceof IReqAccInfoSucceed) {
                ((IReqAccInfoSucceed) mIHelpRequest).onReqUserAccInfoSucceed(accInfo);
            }
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onReqFailed " + statusCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onReqFailed(statusCode, errorMsg);
        }
    };

    public void reqSetUserName(String userName, AccountInfo account) {
        ReqSetUserNameController controller = new ReqSetUserNameController(account.openId,
                account.token,
                userName, mReqSetUserNameListener);
        controller.doRequest();
    }

    private ReqSetUserNameListener mReqSetUserNameListener = new ReqSetUserNameListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onNetError " + errCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onNetError(errCode, errorMsg);
        }

        @Override
        public void onReqSetUserNameSucceed() {
            // TODO Auto-generated method stub
            if (mIHelpRequest instanceof IReqSetUserNameSucceed) {
                ((IReqSetUserNameSucceed) mIHelpRequest).onReqSetUserNameSucceed();
            }
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onReqFailed " + statusCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onReqFailed(statusCode, errorMsg);
        }
    };

    public void reqThirdLogin(String openId, String type, String nickName,
            String headImgUrl, int age, int gender, int source) {
        ReqThirdLoginControll controller = new ReqThirdLoginControll(mReqThirdLoginListener,
                openId, type,
                nickName, headImgUrl, age, gender, source);
        controller.doRequest();
    }

    private ReqThirdLoginListener mReqThirdLoginListener = new ReqThirdLoginListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onNetError " + errCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onNetError(errCode, errorMsg);
        }

        @Override
        public void onReqThirdLoginSucceed(AccountInfo account) {
            // TODO Auto-generated method stub
            if (mIHelpRequest instanceof IReqThirdLoginSucceed) {
                ((IReqThirdLoginSucceed) mIHelpRequest).onReqThirdLoginSucceed(account);
            }
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onReqFailed " + statusCode + " errorMsg " + errorMsg, "oddshou");
            mIHelpRequest.onReqFailed(statusCode, errorMsg);
        }
    };

    public void reqFeedback(String content, String userContact) {
        ReqFeedbackController controller = new ReqFeedbackController(content, userContact, mReqFeedbackListener);
        controller.doRequest();
    }

    private ReqFeedbackListener mReqFeedbackListener = new ReqFeedbackListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub
            mIHelpRequest.onNetError(errCode, errorMsg);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub
            mIHelpRequest.onReqFailed(statusCode, errorMsg);
        }

        @Override
        public void onReqFeedbackSucceed() {
            // TODO Auto-generated method stub
            if (mIHelpRequest instanceof IReqFeedbackSucceed) {
                ((IReqFeedbackSucceed) mIHelpRequest).onReqFeedbackSucceed();
            }
        }

    };
    
    public void reqAddComment(
            String userName,
            int appId, 
            int userScore, 
            int userVerCode,
            String userVerName, 
            String comments,
            int commentId) {
        ReqAddCommentController controller = new ReqAddCommentController("",
                appId, userScore, userVerCode, userVerName, comments,0,
                mReqAddCommentListener);
        controller.doRequest();
    }

    private ReqAddCommentListener mReqAddCommentListener = new ReqAddCommentListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub
            mIHelpRequest.onNetError(errCode, errorMsg);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub
            mIHelpRequest.onReqFailed(statusCode, errorMsg);
        }

        @Override
        public void onReqAddCommentSucceed(long commentId, String userName) {
            // TODO Auto-generated method stub
            if (mIHelpRequest instanceof IReqAddCommentSucceed) {
                ((IReqAddCommentSucceed) mIHelpRequest).onReqAddCommentSucceed(commentId, userName);
            }
        }

    };
    
    // 请求评分信息
    public void reqUserScore(int appId) {
        ReqUserScoreInfoController scoreController = new ReqUserScoreInfoController(
                appId, mReqUserScoreInfoListener);
        scoreController.doRequest();
    }

    private final ReqUserScoreInfoListener mReqUserScoreInfoListener = new ReqUserScoreInfoListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            mIHelpRequest.onNetError(errCode, errorMsg);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            mIHelpRequest.onReqFailed(statusCode, errorMsg);
        }

        @Override
        public void onReqUserScoreInfoSucceed(UserScoreInfo userScoreInfo) {
            if (mIHelpRequest instanceof IReqUserScoreInfoSucceed) {
                ((IReqUserScoreInfoSucceed) mIHelpRequest).onReqUserScoreInfoSucceed(userScoreInfo);
            }
        }

    };
    
}
