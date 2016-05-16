package com.hykj.gamecenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hykj.gamecenter.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/28.
 */
public class MenuAdapter implements IMenuAdapter, View.OnClickListener {

    private final Context mContext;
    private final ArrayList<String> mDataList;
    private final IMenuItemClickListen mListener;
    private final int mWith;
    private final int mHeight;

    public interface IMenuItemClickListen{
        void onMenuItemClick(View view, int position);
    }

    public MenuAdapter(Context context, ArrayList<String> dataList, IMenuItemClickListen listener, int pxWith,int pxHeight) {
        this.mContext = context;
        this.mDataList = dataList;
        this.mListener = listener;
        this.mWith = pxWith;
        this.mHeight = pxHeight;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public View getView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_menu, parent, false);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = mWith;
        layoutParams.height = mHeight;
        view.setLayoutParams(layoutParams);
        TextView textView = (TextView) view.findViewById(R.id.textMenu);
        textView.setText(mDataList.get(position));
        textView.setOnClickListener(this);
        textView.setTag(position);
        return view;
    }

    @Override
    public View getDivider() {
        View view = new View(mContext);
        view.setLayoutParams(new LinearLayout.LayoutParams(mWith, 1));
        view.setBackgroundColor(mContext.getResources().getColor(R.color.divider));
        return view;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onMenuItemClick(view, (Integer) view.getTag());
        }
    }
}
