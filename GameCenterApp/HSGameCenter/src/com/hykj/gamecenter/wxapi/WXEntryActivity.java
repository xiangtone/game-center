
package com.hykj.gamecenter.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.utils.Logger;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class WXEntryActivity extends Activity {

    private static final String TAG = "WXEntryActivity";
    private static final String WEIXIN_APP_ID = "wx380b8664b7e36848";

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    
    public static final int MSG_AUTH_SUCCEED = 0X01;
    public static final int MSG_AUTH_FAILED = 0X02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_wxentry);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
        Logger.i(TAG, "handleIntent "+ resp.errCode + " errStr "+ resp.errStr, "oddshou");
        String mmcode = "";
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            //用户授权成功,保存 code 到 preference
//            Logger.i(TAG, "handleIntent " + resp.code, "oddshou");
//            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+WEIXIN_APP_ID+
//                    "&secret="+WEIXIN_APP_SECRET+"&code="+resp.code+"&grant_type=authorization_code";
//            String string = getWebContent(url);
//            Logger.i(TAG, "handleIntent "+ string, "oddshou");
            mmcode = resp.code;
        }else{
            mmcode = "";
        }
        App.getSharedPreference().edit().putString(StatisticManager.KEY_MM_CODE, mmcode).commit();
        finish();

    }
    
    public String getWebContent(String url) {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String responseInfo) {
                try {
                    JSONTokener jsonParser = new JSONTokener(responseInfo);
                    JSONObject mmInfo = (JSONObject) jsonParser.nextValue();
                    String openid = mmInfo.getString("openid");
                    String accessToken = mmInfo.getString("access_token");
                    Logger.i(TAG, "onSuccess " + openid + " accessToken "+ accessToken, "oddshou");

                    finish();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });
        return null;
    }

}
