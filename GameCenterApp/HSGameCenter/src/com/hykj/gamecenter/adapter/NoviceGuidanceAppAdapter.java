
package com.hykj.gamecenter.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.ui.entry.ItemViewHolder;
import com.hykj.gamecenter.utils.StringUtils;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utilscs.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NoviceGuidanceAppAdapter extends GeneralGridAdapter implements
        IUpdateSingleView<Integer>,
        IListAdapterInteraction {

    private final int M_SIZE = 1024 * 1024 * 1;
    private final ArrayList<GroupElemInfo> mAppList = new ArrayList<GroupElemInfo>();
    private final ArrayList<Boolean> mAppListCheckedState = new ArrayList<Boolean>();
    private final LayoutInflater mInflater;
    private final Context mContext;

    protected ImageLoader imageLoader;
    private boolean isDisplayImage = true;

    private boolean mShowSnapShot = false;

    public static final String APP_TYPE = "app_type";
    private int appPosType;

    IListItemCheckedClickListener mItemCheckedClickListener = null;

    public void setListItemCheckedClickListener(
            IListItemCheckedClickListener itemCheckedClickListener) {
        mItemCheckedClickListener = itemCheckedClickListener;
    }

    public void setAppPosType(int appPosType) {
        this.appPosType = appPosType;
    }

    public void initAppCheckStateList() {
        mAppListCheckedState.clear();

        for (int i = 0; i < mAppList.size(); i++) {
            mAppListCheckedState.add(true);
        }
    }

    protected HashMap<Integer, View> mViewMap = new HashMap<Integer, View>();

    public NoviceGuidanceAppAdapter(Context context, int appType, int columnCount) {
        super(context, appType, columnCount, false);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = ImageLoader.getInstance();
    }

    public void showSnapShot(boolean bshow) {
        mShowSnapShot = bshow;
    }

    @Override
    public int getAllCount() {
        return mAppList.size();
    }

    @Override
    public View getPeaceView(int positionInTotal, View convertView, ViewGroup parentView) {
        ItemViewHolder holder;
        if (convertView == null) {
            if (mShowSnapShot) {
                convertView = mInflater.inflate(R.layout.show_snapshot_content_item, null);
            }
            else {
                convertView = mInflater.inflate(R.layout.novice_guidance_list_item, null);
            }

            holder = new ItemViewHolder(convertView, mShowSnapShot);
            convertView.setTag(holder);
        }
        else {
            holder = (ItemViewHolder) convertView.getTag();
        }

        bindItemView(positionInTotal, holder, mAppList.get(positionInTotal));

        return convertView;
    }

    private void bindItemView(final int positionInTotal, ItemViewHolder holder,
            final GroupElemInfo infoes) {
        // TODO Auto-generated method stub
        final int appId = infoes.appId;
        String appName = null;
        if (mAppType == MAIN_TYPE.RANKING) {
            appName = positionInTotal + 1 + "  " + infoes.showName;
        }
        else {
            appName = infoes.showName;
        }

        holder.appname.setText(appName);
        //        holder.mAppDownload.setText(Tools.showDownTimes(infoes.downTimes, mContext));
        //        holder.mAppSize.setText(StringUtils.byteToString(infoes
        //                .mainPackSize));
        //        holder.appInfo.setText(Tools.showDownTimes(infoes.downTimes, mContext) + " | "
        //                + String.format("%.2f", infoes.mainPackSize * 1.0f / M_SIZE) + "MB");
        holder.mAppDownload.setText(Tools.showDownTimes(infoes.downTimes, mContext));
        holder.mAppSize.setText(StringUtils.byteToString(infoes
                .mainPackSize));
        //        holder.appInfo.setText(Tools.showDownTimes(infoes.downTimes, mContext) + " | "
        //                + StringUtils.byteToString(infoes
        //                        .mainPackSize));
        holder.rating.setRating(infoes.recommLevel / 2);
        holder.recommendInfo.setText(infoes.recommWord);

        holder.rating.setVisibility(View.GONE);
        holder.recommendInfo.setVisibility(View.VISIBLE);

        /*
         * if (UITools.isPortrait()) {
         * holder.rating.setVisibility(View.VISIBLE);
         * holder.recommendInfo.setVisibility(View.GONE); } else {
         * holder.rating.setVisibility(View.GONE);
         * holder.recommendInfo.setVisibility(View.VISIBLE); }
         */

        if (mShowSnapShot) {
            holder.snapshot.setVisibility(View.VISIBLE);
            if (isDisplayImage) {
                imageLoader.displayImage(infoes.thumbPicUrl, holder.snapshot,
                        DisplayOptions.optionsSnapshot);
            }
        }
        else {
            if (isDisplayImage) {
                imageLoader.displayImage(infoes.iconUrl, holder.icon,
                        DisplayOptions.optionsIcon);

                LogUtils.i("groupInfo.groupPicUrl : " + infoes.iconUrl);
            }
        }
        holder.frame.setVisibility(View.VISIBLE);
        holder.frame.setLongClickable(false);

        // 得到当前所在行
        int nCurCount = (int) Math.ceil((positionInTotal + 1) * 1.0 / mColumnCount);

        // 最后一行不显示divider
        if (nCurCount == getCount()) {
            holder.dividerLine.setVisibility(View.GONE);
        }
        else {
            holder.dividerLine.setVisibility(View.VISIBLE);
        }

        // 设置选项checkBox
        holder.checkBox.setChecked(mAppListCheckedState.get(positionInTotal));

        final ItemViewHolder holder_ = holder;
        holder.checkBox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mAppListCheckedState.set(positionInTotal,
                        !mAppListCheckedState.get(positionInTotal));

                holder_.checkBox.setChecked(mAppListCheckedState.get(positionInTotal));
                mItemCheckedClickListener.OnListItemCheckedClick(holder_.checkBox.isChecked());
            }
        });

        // 跳转到详情
        final int position = positionInTotal + 1;
        holder.frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if( UITools.isFastDoubleClick( ) )
                // return;

                /*
                 * 当前设置应用不能点击 Intent intentDetail = new Intent( mContext,
                 * App.getDevicesType() == App.PHONE ?
                 * PhoneAppInfoActivity.class : PadAppInfoActivity.class); //
                 * intentDetail.putExtra( APP_TYPE , 0 );
                 * intentDetail.putExtra(KEY.GROUP_INFO, infoes); intentDetail
                 * .putExtra(StatisticManager.APP_POS_TYPE, appPosType); if
                 * (appPosType == STATIS_TYPE.RANKING) {
                 * intentDetail.putExtra(MTAConstants.KEY_DETAIL_PAGE_FROM,
                 * MTAConstants.DETAIL_RANKING + position); } if (appPosType ==
                 * STATIS_TYPE.NEW_PERSON_RECOM) {
                 * intentDetail.putExtra(StatisticManager.APP_POS_POSITION,
                 * infoes.getPosId()); } mContext.startActivity(intentDetail);
                 */

                // TODO Auto-generated method stub
                mAppListCheckedState.set(positionInTotal,
                        !mAppListCheckedState.get(positionInTotal));

                holder_.checkBox.setChecked(mAppListCheckedState.get(positionInTotal));
                mItemCheckedClickListener.OnListItemCheckedClick(holder_.checkBox.isChecked());

            }
        });

        // 缓存ItemViewHolder 开始
        holder.frame.setTag(holder);
        // 改为id 缓存view,而不使用packName ,因为有可能出现同包名但不同版本的应用apk
        setViewForSingleUpdate(appId, holder.frame);

    }

    public boolean isDuplicateData(List<GroupElemInfo> infoes) {
        if (infoes.size() == mAppList.size()) {
            for (int i = 0; i < infoes.size(); i++) {
                if (infoes.get(i).appId != mAppList.get(i).appId) {
                    return false;
                }
            }
        }
        else {
            return false;
        }
        return true;
    }

    public void setDisplayImage(boolean isDisplayImage) {
        this.isDisplayImage = isDisplayImage;
    }

    @Override
    public View getViewByKey(Integer key) {
        View view = mViewMap.get(key);
        return view;
    }

    @Override
    public void setViewForSingleUpdate(Integer key, View view) {
        if (view == null) {
            return;
        }
        mViewMap.put(key, view);
    }

    @Override
    public void removeViewForSingleUpdate(View view) {

    }

    @Override
    public void removeViewForSingleUpdate(Integer key) {
        mViewMap.remove(key);
    }

    public void removeAll() {

        mViewMap.clear();
    }

    public void appendData(List<GroupElemInfo> infoes, boolean isClear) {
        if (isClear) {
            mAppList.clear();
            mAppListCheckedState.clear();
        }
        mAppList.addAll(infoes);
        initAppCheckStateList();

        notifyDataSetChanged();
    }

    public void removeAllData() {
        mAppList.clear();
        initAppCheckStateList();
    }

    public boolean getAppCheckedState(int positionInTotal) {
        return mAppListCheckedState.get(positionInTotal);
    }

    @Override
    public Object GetObject() {
        // TODO Auto-generated method stub
        return mAppListCheckedState;
    }
}
