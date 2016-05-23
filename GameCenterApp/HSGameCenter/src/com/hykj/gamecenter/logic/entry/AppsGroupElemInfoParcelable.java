
package com.hykj.gamecenter.logic.entry;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;

public class AppsGroupElemInfoParcelable implements Parcelable {

    // required int32 groupId = 1;
    public int groupId;

    // required int32 posId = 2;
    public int posId;

    // required int32 orderNo = 3;
    public int orderNo;

    // required int32 elemType = 4;
    public int elemType;

    // optional string showName = 5;
    public java.lang.String showName;

    // optional string recommWord = 6;
    public java.lang.String recommWord;

    // optional int32 recommFlag = 7;
    public int recommFlag;

    // optional int32 recommLevel = 8;
    public int recommLevel;

    // optional string iconUrl = 9;
    public String iconUrl;

    // optional string thumbPicUrl = 10;
    public java.lang.String thumbPicUrl;

    // optional string adsPicUrl = 11;
    public java.lang.String adsPicUrl;

    // optional string publishTime = 12;
    public java.lang.String publishTime;

    // optional int32 showType = 25;
    public int showType;

    // optional string startTime = 26;
    public java.lang.String startTime;

    // optional string endTime = 27;
    public java.lang.String endTime;

    // optional int32 appId = 13;
    public int appId;

    // optional string packName = 14;
    public java.lang.String packName;

    // optional int32 mainPackId = 15;
    public int mainPackId;

    // optional int32 mainVerCode = 16;
    public int mainVerCode;

    // optional string mainSignCode = 17;
    public java.lang.String mainSignCode;

    // optional string mainVerName = 18;
    public java.lang.String mainVerName;

    // optional int32 mainPackSize = 24;
    public int mainPackSize;

    // optional string appTypeName = 28;
    public java.lang.String appTypeName;

    // optional int32 downTimes = 29;
    public int downTimes;

    // optional int32 jumpLinkId = 19;
    public int jumpLinkId;

    // optional string jumpLinkUrl = 20;
    public java.lang.String jumpLinkUrl;

    // optional int32 jumpGroupId = 21;
    public int jumpGroupId;

    // optional int32 jumpGroupType = 22;
    public int jumpGroupType;

    // optional int32 jumpOrderType = 23;
    public int jumpOrderType;

    public AppsGroupElemInfoParcelable() {
        clear();
    }

    public AppsGroupElemInfoParcelable(GroupElemInfo info) {
        copyFromElemInfo(info);
    }

    public void copyFromElemInfo(GroupElemInfo info) {
        Log.e("AppGroupInfo ", info.toString());
        this.groupId = info.groupId;
        this.posId = info.posId;
        this.orderNo = info.orderNo;
        this.elemType = info.elemType;
        this.showName = info.showName;
        this.recommWord = info.recommWord;
        this.recommFlag = info.recommFlag;
        this.recommLevel = info.recommLevel;
        this.iconUrl = info.iconUrl;
        this.thumbPicUrl = info.thumbPicUrl;
        this.adsPicUrl = info.adsPicUrl;
        Log.e("AppGroupElemInfoParcelable ", "adsPicUrl " + adsPicUrl);
        this.publishTime = info.publishTime;
        this.showType = info.showType;
        this.startTime = info.startTime;
        this.endTime = info.endTime;
        this.appId = info.appId;
        this.packName = info.packName;
        this.mainPackId = info.mainPackId;
        this.mainVerCode = info.mainVerCode;
        this.mainSignCode = info.mainSignCode;
        this.mainVerName = info.mainVerName;
        this.mainPackSize = info.mainPackSize;
        this.appTypeName = info.appTypeName;
        this.downTimes = info.downTimes;
        this.jumpLinkId = info.jumpLinkId;
        this.jumpLinkUrl = info.jumpLinkUrl;
        this.jumpGroupId = info.jumpGroupId;
        this.jumpGroupType = info.jumpGroupType;
        this.jumpOrderType = info.jumpGroupType;
    }

    public AppsGroupElemInfoParcelable clear() {
        groupId = 0;
        posId = 0;
        orderNo = 0;
        elemType = 0;
        showName = "";
        recommWord = "";
        recommFlag = 0;
        recommLevel = 0;
        iconUrl = "";
        thumbPicUrl = "";
        adsPicUrl = "";
        publishTime = "";
        showType = 0;
        startTime = "";
        endTime = "";
        appId = 0;
        packName = "";
        mainPackId = 0;
        mainVerCode = 0;
        mainSignCode = "";
        mainVerName = "";
        mainPackSize = 0;
        appTypeName = "";
        downTimes = 0;
        jumpLinkId = 0;
        jumpLinkUrl = "";
        jumpGroupId = 0;
        jumpGroupType = 0;
        jumpOrderType = 0;
        return this;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(groupId);
        dest.writeInt(posId);
        dest.writeInt(orderNo);
        dest.writeInt(elemType);
        dest.writeString(showName);
        dest.writeString(recommWord);
        dest.writeInt(recommFlag);
        dest.writeInt(recommLevel);
        dest.writeString(iconUrl);
        dest.writeString(thumbPicUrl);
        dest.writeString(adsPicUrl);
        dest.writeString(publishTime);
        dest.writeInt(showType);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeInt(appId);
        dest.writeString(packName);
        dest.writeInt(mainPackId);
        dest.writeInt(mainVerCode);
        dest.writeString(mainSignCode);
        dest.writeString(mainVerName);
        dest.writeInt(mainPackSize);
        dest.writeString(appTypeName);
        dest.writeInt(downTimes);
        dest.writeInt(jumpLinkId);
        dest.writeString(jumpLinkUrl);
        dest.writeInt(jumpGroupId);
        dest.writeInt(jumpGroupType);
        dest.writeInt(jumpOrderType);
    }

    public static final Parcelable.Creator<AppsGroupElemInfoParcelable> CREATOR = new Parcelable.Creator<AppsGroupElemInfoParcelable>() {

        @Override
        public AppsGroupElemInfoParcelable[] newArray(int size) {
            // TODO Auto-generated method stub
            return new AppsGroupElemInfoParcelable[size];
        }

        @Override
        public AppsGroupElemInfoParcelable createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new AppsGroupElemInfoParcelable(source);
        }
    };

    private AppsGroupElemInfoParcelable(Parcel dest) {
        groupId = dest.readInt();
        posId = dest.readInt();
        orderNo = dest.readInt();
        elemType = dest.readInt();
        showName = dest.readString();
        recommWord = dest.readString();
        recommFlag = dest.readInt();
        recommLevel = dest.readInt();
        iconUrl = dest.readString();
        thumbPicUrl = dest.readString();
        adsPicUrl = dest.readString();
        publishTime = dest.readString();
        showType = dest.readInt();
        startTime = dest.readString();
        endTime = dest.readString();
        appId = dest.readInt();
        packName = dest.readString();
        mainPackId = dest.readInt();
        mainVerCode = dest.readInt();
        mainSignCode = dest.readString();
        mainVerName = dest.readString();
        mainPackSize = dest.readInt();
        appTypeName = dest.readString();
        downTimes = dest.readInt();
        jumpLinkId = dest.readInt();
        jumpLinkUrl = dest.readString();
        jumpGroupId = dest.readInt();
        jumpGroupType = dest.readInt();
        jumpOrderType = dest.readInt();
    }

    @Override
    public String toString() {
        return "AppsGroupElemInfoParcelable [groupId=" + groupId + ", posId=" + posId
                + ", orderNo=" + orderNo + ", elemType=" + elemType + ", showName=" + showName
                + ", recommWord=" + recommWord + ", recommFlag=" + recommFlag + ", recommLevel="
                + recommLevel + ", iconUrl=" + iconUrl + ", thumbPicUrl=" + thumbPicUrl
                + ", adsPicUrl=" + adsPicUrl + ", publishTime=" + publishTime + ", showType="
                + showType + ", startTime=" + startTime + ", endTime=" + endTime + ", appId="
                + appId + ", packName=" + packName + ", mainPackId=" + mainPackId
                + ", mainVerCode=" + mainVerCode + ", mainSignCode=" + mainSignCode
                + ", mainVerName=" + mainVerName + ", mainPackSize=" + mainPackSize
                + ", appTypeName=" + appTypeName + ", downTimes=" + downTimes + ", jumpLinkId="
                + jumpLinkId + ", jumpLinkUrl=" + jumpLinkUrl + ", jumpGroupId=" + jumpGroupId
                + ", jumpGroupType=" + jumpGroupType + ", jumpOrderType=" + jumpOrderType + "]";
    }

}
