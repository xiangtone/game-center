package com.hykj.gamecenter.net;

import android.content.Context;
import android.util.Log;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xutils.common.Callback;

/**
 * Created by Administrator on 2016/6/21.
 */
public abstract class JsonCallback implements Callback.CommonCallback<String> {
    private static final String TAG = "JsonCallback";
    private final Context mContext;
    private String mUrl;

    public JsonCallback(String url, Context context) {
        mUrl = url;
        mContext = context;
    }

    @Override
    public void onSuccess(String responseInfo) {
        //json 解析 responseInfo
        JSONTokener responseData = new JSONTokener(responseInfo);
        try {
            JSONObject objData = (JSONObject) responseData.nextValue();
            JSONObject hdata = objData.getJSONObject("hdata");
            int errcode = hdata.getInt("errcode");
            int errmsg = hdata.getInt("errmsg");
            if (errcode != 0) {
                onFaild(errcode, String.valueOf(errmsg));
            } else {
                JSONObject ddata = objData.getJSONObject("ddata");
                String code = ddata.getString("code");
                String codemsg = ddata.getString("codemsg");
                if (!code.equals("99")) {
                    onException(code, codemsg, mUrl);
                } else {
                    handleSucced(ddata, mUrl);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Logger.i(TAG, "onSuccess " + responseInfo);
    }

    @Override
    public void onError(Throwable throwable, boolean b) {
        Logger.i(TAG, "onError " + throwable.getMessage());
        //网络异常
    }

    @Override
    public void onCancelled(CancelledException e) {

    }

    @Override
    public void onFinished() {

    }

    //参数异常
    private void onFaild(int errcode, String errmsg) {
        if (errcode != 0) {
            String errString = errmsg;
            switch (errcode) {
                case 1:
                    errString = mContext.getString(R.string.wifi_error_code_1);
                    break;
                case 2:
                    errString = mContext.getString(R.string.wifi_error_code_2);
                    break;
                case 3:
                    errString = mContext.getString(R.string.wifi_error_code_3);
                    break;
                case 4:
                    errString = mContext.getString(R.string.wifi_error_code_4);     //需要重连，所以回传
                    onException(String.valueOf(errcode), errmsg, mUrl);
                    break;
                case 5:
                    errString = mContext.getString(R.string.wifi_error_code_5);
                    break;
                case 6:
                    errString = mContext.getString(R.string.wifi_error_code_6);
                    break;
            }
            Log.e(TAG, errString);
        }
    }
    protected abstract void handleSucced(JSONObject ddata, String url);

    protected abstract void onException(String code, String codemsg, String url);
}
