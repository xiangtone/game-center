package com.hykj.gamecenter.sdk.aidl;
import com.hykj.gamecenter.sdk.aidl.ICallBackInterface;
import com.hykj.gamecenter.sdk.entry.NiuAppInfo;
import com.hykj.gamecenter.sdk.entry.NiuOrderInfo;
import com.hykj.gamecenter.sdk.entry.NiuSDKRoleInfo;

interface SDKServiceInterface {     
    void login(in NiuAppInfo NiuAppInfo);
    void pay(in NiuAppInfo NiuAppInfo, in NiuOrderInfo NiuOrderInfo, in NiuSDKRoleInfo NiuSDKRoleInfo);
    void registerCallBack(in ICallBackInterface iCallBackInterface);  
}