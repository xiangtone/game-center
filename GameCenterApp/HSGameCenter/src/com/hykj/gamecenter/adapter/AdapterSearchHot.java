package com.hykj.gamecenter.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.utils.Logger;

import java.util.List;

public class AdapterSearchHot extends BaseAdapter {
    
    protected static final String TAG = "AdapterSearchHot";
    private Context mContext;
    private List<GroupElemInfo> mGroupInfos;
    

    public AdapterSearchHot(Context context, List<GroupElemInfo> infoes) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mGroupInfos = infoes;
    }
    
    

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mGroupInfos.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mGroupInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Button button;
        if (convertView == null) {
            button = new Button(mContext);
//            int margins = (int) mRes.getDimension(R.dimen.list_view_item_spacing);
            Resources res = mContext.getResources();
//            int width = (int)res.getDimension(R.dimen.search_grid_w);
            int height = (int)res.getDimension(R.dimen.search_grid_h);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,height);
            button.setGravity(Gravity.CENTER);
            button.setLayoutParams(params);
            button.setBackground(res.getDrawable(R.drawable.btn_green_white_selector));
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.nui_text_size_small));
            button.setTextColor(mContext.getResources().getColor(R.color.csl_black_cc));
            button.setSingleLine();
            button.setEllipsize(TruncateAt.END);
        }else{
            button = (Button)convertView;
        }
        GroupElemInfo elemInfo = mGroupInfos.get(position);
        button.setText(elemInfo.showName);
        button.setTag(elemInfo);
        button.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                GroupElemInfo elemInfo =  (GroupElemInfo)v.getTag();
                Logger.d(TAG, "groupElement showName "+ elemInfo.showName, "oddshou");
                if (hotWordClickListen != null) {
                    hotWordClickListen.onclickHotWord(v, elemInfo.showName);
                }
            }
        });
        
        
        return button;
    }
    
    private HotWordClickListen hotWordClickListen;
    public interface HotWordClickListen{
        void onclickHotWord(View v, String word);
    }
    public void setHotWordClickListen(HotWordClickListen listen){
        this.hotWordClickListen = listen;
    }

}
