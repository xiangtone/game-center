package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.PhoneAppInfoActivity;
import com.hykj.gamecenter.controller.ProtocolListener.ELEMENT_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.protocol.Reported.ReportedInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.utils.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 注意 需要调用 {@link #bindItem(AppInfo)}方法
 * @author oddshou
 *
 */
public class DetailRecommedItem extends FrameLayout implements OnClickListener{
    private ImageView mImgIcon;
    private TextView mTextName;
    private TextView mDownloadTimes;
    private BtnDownloadUpdate mBtnInstall;
    private AppInfo mAppInfo;
    private Context mContext;
    private GroupElemInfo mGroupInfo;
    private int mAppPosType;

    public DetailRecommedItem(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);

    }

    public DetailRecommedItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }
    
    private void init(Context context){
        mContext = context;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.detail_recommed_item, this);
        mImgIcon = (ImageView) findViewById(R.id.app_icon);
        mTextName = (TextView) findViewById(R.id.textName);
        mDownloadTimes = (TextView) findViewById(R.id.download_times);
        mBtnInstall = (BtnDownloadUpdate) findViewById(R.id.btnDownloadUpdate);
//        mBtnInstall.setOnClickListener(this);
        view.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnDownloadUpdate:
                //点击下载按钮

                break;

            default:
                //可能是进入详情操作
                Intent intentDetail = new Intent(
                        mContext,PhoneAppInfoActivity.class
                        /*App.getDevicesType() == App.PHONE ? PhoneAppInfoActivity.class
                                : PadAppInfoActivity.class*/);
                //                AppsGroupElemInfoParcelable info = new AppsGroupElemInfoParcelable(infoes);
                intentDetail.putExtra(KEY.GROUP_INFO, mGroupInfo);
                intentDetail.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.GAME);
                intentDetail
                        .putExtra(StatisticManager.APP_POS_TYPE, mAppPosType);
                intentDetail.putExtra(StatisticManager.APP_POS_POSITION,
                        mGroupInfo.posId);
                // 广告位点击上报
                ReportedInfo build = new ReportedInfo();
                int posId = mGroupInfo.posId + ReportConstants.STAC_APP_POSITION_GAME_PAGE;
                int type = mGroupInfo.elemType;
                int elemId = 0;
                switch (type) {
                    case ELEMENT_TYPE.TYPE_APP:
                        elemId = mGroupInfo.appId;
                        break;

                    case ELEMENT_TYPE.TYPE_LINK:
                        elemId = mGroupInfo.jumpLinkId;
                        break;
                    case ELEMENT_TYPE.TYPE_SKIP_LOCAL_OR_ONLINE:
                    case ELEMENT_TYPE.TYPE_SKIP_TIP:
                    case ELEMENT_TYPE.TYPE_SKIP_CLASS:
                        elemId = mGroupInfo.jumpGroupId;
                        break;
                    default:
                        break;
                }
                build.statActId = ReportConstants.STATACT_ID_ADV;
                build.statActId2 = 1; //首页元素
                build.ext1 = "" + posId;
                build.ext2 = "" + type;
                build.ext3 = "" + elemId;
                build.ext4 = "" + mGroupInfo.showType; //showType 1 广告位, 0 表示其他

                //暂时只生成统计数据，不上报################oddshou
//                StatisticManager.getInstance().reportReportedInfo(build);
                mContext.startActivity(intentDetail);

                break;
        }
    }

    public void bindItem(GroupElemInfo appInfo, int appPositionType){
        mGroupInfo = appInfo;
        mAppInfo = Tools.createAppInfo(appInfo);
        ImageLoader.getInstance().displayImage(mAppInfo.iconUrl, mImgIcon,
                DisplayOptions.optionsIcon);
        mTextName.setText(mAppInfo.showName);
        mDownloadTimes.setText(Tools.showDownTimes(appInfo.downTimes, mContext));
        mAppPosType = appPositionType;
        mBtnInstall.bindData(mAppInfo, appPositionType);
    }

}
