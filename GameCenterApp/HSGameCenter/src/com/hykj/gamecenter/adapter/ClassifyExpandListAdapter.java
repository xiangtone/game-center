
package com.hykj.gamecenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.GameClassifyActivity;
import com.hykj.gamecenter.activity.GroupAppListActivity;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.Tools;

import java.util.ArrayList;

public class ClassifyExpandListAdapter extends BaseExpandableListAdapter {

    public static final String TAG = "ExpandListView";
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final ArrayList<GroupInfo> mAppList = new ArrayList<GroupInfo>();
    private final ArrayList<GroupInfo> mGameList = new ArrayList<GroupInfo>();
    private final ArrayList<Integer> mAppTypeList = new ArrayList<Integer>();
    private final ArrayList<String> mGroupNameList = new ArrayList<String>();
    private int mAppPosType = 0;
    private int mColumnCount;

    private static final LinearLayout.LayoutParams llp_padding = new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    private static final LinearLayout.LayoutParams llp = new LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    private final ArrayList<ArrayList<GroupInfo>> childs = new ArrayList<ArrayList<GroupInfo>>();
    protected final int mAppType = 0;
    protected Resources mRes = null;
    /**
     * 空白视图，用于充填没有数据的地方
     */
    private final ArrayList<View> mBlankView = new ArrayList<View>();

    public ClassifyExpandListAdapter(Context context, int mAppPosType,
                                     int mColumnCount) {
        super();

        this.mContext = context;
        this.mAppPosType = mAppPosType;
        if (!App.ismAllGame()) {
            childs.add(mAppList);
        }
        childs.add(mGameList);
        this.mColumnCount = mColumnCount;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Log.i(TAG, "constructor");

        mRes = context.getResources();
        llp.weight = 1;
        int margins = (int) mRes
                .getDimension(R.dimen.recommended_adv_subject_entry_padding);
        int marginBottom = (int) mRes
                .getDimension(R.dimen.recommended_adv_subject_entry_padding);
        llp.setMargins(0, 0/* margins */, 0, 0/* marginBottom */);// 只填充了左margin
    }

    public void setColumnCount(int count) {
        mColumnCount = count;
    }

    public void expandAllGroups() {
        for (int i = 0; i != childs.size(); i++) {
        }
    }

    public void addAppTypeList(Integer appType) {
        mAppTypeList.add(appType);
    }

    public void addGroupName(String groupName) {
        mGroupNameList.add(groupName);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return childs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return childs.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return childs.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childs.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (App.ismAllGame()) {
            TextView view = new TextView(mContext);
            view.setHeight((int) mRes
                    .getDimension(R.dimen.normal_margin_padding_micro));
            return view;
        }

        View view = LinearLayout.inflate(mContext, R.layout.group_cancle_headview,
                null);
        TextView nameView = (TextView) view.findViewById(R.id.category_title);
        if (!App.ismAllGame()) {
            nameView.setText((mGroupNameList.size() > groupPosition ? (mGroupNameList
                    .get(groupPosition)) : ""));
        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        LinearLayout lineContainer = (LinearLayout) convertView;

        if (lineContainer == null) {
            lineContainer = new LinearLayout(mContext);
            lineContainer.setOrientation(LinearLayout.HORIZONTAL);
            lineContainer.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            lineContainer.removeAllViews();
        }

        // 获取或者初始化重用的itemview和divider的view
        ArrayList<View> viewHolder = (ArrayList<View>) lineContainer
                .getTag(R.id.tag_viewHolder);
        if (viewHolder == null) {
            viewHolder = new ArrayList<View>();
            lineContainer.setTag(R.id.tag_viewHolder, viewHolder);
        }

        /*
         * 因为该函数getChildView return的是lineContainer ，
         * lineContainer只存了每一行最多mColumnCount个应用数， 所以childPosition是行索引
         */
        // 根据每行需要显示的条目数目来显示每行数据
        // index表示该行的初始id 是 在当前组的id的值
        int index = childPosition * mColumnCount; // 行初始索引 不同组的 childPosition初始值
        // 是从0开始，因为groupPosition不同
        int end = index + mColumnCount; // 行结束索引
        int nFillItem = 0;
        // comment by firewang
        for (; index < end && index < getChildrenCount(groupPosition); index++) {
            int posInLine = mColumnCount - (end - index);
            View itemView = null;
            // 每行一个viewHolder
            if (viewHolder.size() > posInLine) {
                itemView = viewHolder.get(posInLine);
                itemView = getPeaceView(groupPosition, index, itemView,
                        lineContainer);
            } else {
                itemView = getPeaceView(groupPosition, index, itemView,
                        lineContainer);
                viewHolder.add(itemView);
            }

            if (itemView != null) {
                itemView.setTag(R.id.tag_blankView, Boolean.FALSE);
                itemView.setTag(R.id.tag_positionView, Integer.valueOf(index));
                llp.weight = 1;

                int screenWidth = Tools.getDisplayWidth(mContext);

                // getDimension返回的是绝对尺寸，而不是相对尺寸（dp\sp等)
                int margins = (int) mRes
                        .getDimension(R.dimen.list_view_item_spacing);
                int ParentWidth = screenWidth - 2 * margins;
                // listItem间距
                int iPaddingWidth = 0;
                if (mColumnCount > 1) {
                    iPaddingWidth = (int) mRes
                            .getDimension(R.dimen.list_view_item_spacing);
                } else {
                    iPaddingWidth = 0;
                }

                int MaginW = 0;
                // item宽度
                int iItemWidth = (int) ((ParentWidth - MaginW * mColumnCount - iPaddingWidth
                        * (mColumnCount - 1))
                        * 1.0f / mColumnCount + 0.5f);
                // 固定宽布局
                LinearLayout.LayoutParams llp_appFixedWidth = new LayoutParams(
                        iItemWidth, LayoutParams.WRAP_CONTENT);

                int marginBottom = (int) mRes
                        .getDimension(R.dimen.recommended_adv_subject_entry_padding);
//                llp_appFixedWidth
//                        .setMargins(0, 0/* marginBottom */, 0, 154 );
                lineContainer.addView(itemView, llp_appFixedWidth);

                // 加入填充布局
                if (index >= 0 && index < end - 1) {
                    TextView tv = new TextView(mContext);
                    tv.setWidth(iPaddingWidth);
                    // tv.setBackgroundResource(R.color.cs_btn_red_normal);
                    lineContainer.addView(tv, llp_padding);
                }

                nFillItem++;
            }
        }

        lineContainer.setTag(R.id.tag_fillItems, nFillItem);

        // 当一行未填满，使用空白的View进行填充
        if (index < end) {
            for (int blankNum = end - index - 1; blankNum >= 0; blankNum--) {
                View bk = null;
                for (int j = mBlankView.size() - 1; j >= 0; j--) {

                    if (mBlankView.get(j).getParent() == null) {
                        bk = mBlankView.get(j);
                        break;
                    }
                }
                if (bk == null) {
                    bk = genBlankView();
                    mBlankView.add(bk);
                }
                lineContainer.addView(bk, llp);
            }
        }

        return lineContainer;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return false;
    }

    public void appendAppList(ArrayList<GroupInfo> appInfoes) {
        mAppList.addAll(appInfoes);
    }

    public void appendGameList(ArrayList<GroupInfo> gameInfos) {
        mGameList.addAll(gameInfos);
    }

    public void removeAllData() {
        mAppList.clear();
        mGameList.clear();
    }

    //每个item view 并绑定数据
    public View getPeaceView(int groupPosition, int positionInGroup,
                             View convertView, ViewGroup parentView) {
        ItemHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.category_list_item,
                    null);
            holder = new ItemHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ItemHolder) convertView.getTag();
        }

        bindItemView(holder, groupPosition, positionInGroup);
        return convertView;
    }

    private void bindItemView(ItemHolder holder, int groupPosition, int position) {
        final GroupInfo info = childs.get(groupPosition).get(position);

//        ImageLoader.getInstance().displayImage(info.groupPicUrl, holder.icon,
//                DisplayOptions.optionsIcon);

        holder.name.setText(info.groupName);
        holder.tip.setText(info.recommWrod);

        holder.frame.setVisibility(View.VISIBLE);
        final int appType = mAppTypeList.get(groupPosition);
        // TODO 进入某一类应用列表界面
        holder.frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if( UITools.isFastDoubleClick( ) )
                // return;
                Logger.i(TAG, "bindItemView " + mAppPosType, "oddshou");
                if (info.groupType == GROUP_TYPE.CLASSIFY_GAME_TYPE) {
                    Intent intentAppList = new Intent(mContext,
                            GameClassifyActivity.class);
                    mContext.startActivity(intentAppList);
                } else {
                    Intent intentAppList = new Intent(mContext,
                            GroupAppListActivity.class);
                    intentAppList.putExtra(KEY.MAIN_TYPE, appType);
                    intentAppList.putExtra(KEY.ORDERBY, info.orderType);
                    intentAppList.putExtra(KEY.GROUP_ID, info.groupId);
                    intentAppList.putExtra(KEY.GROUP_CLASS, info.groupClass);
                    intentAppList.putExtra(KEY.GROUP_TYPE, info.groupType);
                    intentAppList.putExtra(KEY.CATEGORY_NAME, info.groupName);
                    intentAppList.putExtra(StatisticManager.APP_POS_TYPE,
                            mAppPosType);
                    intentAppList.putExtra(KEY.SUBJECT_APPLIST, true);
                    mContext.startActivity(intentAppList);
                }
            }
        });

//        // 得到当前所在行
//        int nCurCount = (int) Math.ceil((position + 1) * 1.0 / mColumnCount);
//
//        // 最后一行的索引
//        int nLastCount = (int) Math.ceil(childs.get(groupPosition).size() * 1.0
//                / mColumnCount);// 行数
//        // 最后一行不显示divider
//        if (nCurCount == nLastCount) {
//            holder.dividerLine.setVisibility(View.GONE);
//        } else {
//            holder.dividerLine.setVisibility(View.VISIBLE);
//        }
    }

    protected View genBlankView() {
        View blankView = new LinearLayout(mContext);
        blankView.setTag(R.id.tag_blankView, Boolean.TRUE);
        return blankView;
    }

    private class ItemHolder {
        View frame;
        // FitWidthImageView iconTopic;
        TextView name;
        TextView tip;

        public ItemHolder(View view) {
            frame = view;
            name = (TextView) view.findViewById(R.id.group_name);
            tip = (TextView) view.findViewById(R.id.group_tip);
        }
    }

}
