
package com.hykj.gamecenter.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.SearchActivity;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;

public class HotSearchAdapter extends GridAdapter
{

    private static final String TAG = "HotSearchAdapter";

    private static final int ITEM_COUNT = 12;
    private static final int GROUP_NUMBER = 4;
    private final ArrayList<GroupElemInfo> mDataList = new ArrayList<GroupElemInfo>();
    private final LayoutInflater mInflater;
    private Handler mHandler;

    public HotSearchAdapter(Context context)
    {
        super(GROUP_NUMBER, context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public HotSearchAdapter(Context context, int number)
    {
        super(number, context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setHandler(Handler handler)
    {
        mHandler = handler;
    }

    public void removeAllData()
    {
        mDataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getAllCount()
    {
        return mDataList.size();
    }

    public void addData(List<GroupElemInfo> data)
    {
        if (data == null)
            return;
        mDataList.addAll(data);
        if (mDataList.size() > ITEM_COUNT)
        {
            mDataList.subList(ITEM_COUNT, mDataList.size()).clear();
        }
    }

    public void clearData()
    {
        mDataList.clear();
    }

    @Override
    public View getPeaceView(int positionInTotal, View convertView, ViewGroup parentView)
    {
        TextView textView;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.hot_search_item, null);
            textView = (TextView) convertView.findViewById(R.id.search_item);
            convertView.setTag(textView);
        }
        else
        {
            textView = (TextView) convertView.getTag();
        }

        final String strHistory = mDataList.get(positionInTotal).showName;
        textView.setText(strHistory);

        convertView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (mHandler != null)
                {
                    Message msg = mHandler.obtainMessage();
                    msg.what = SearchActivity.MSG_SEARCH_ITEM;
                    msg.obj = strHistory;
                    mHandler.sendMessage(msg);
                }
            }
        });

        return convertView;
    }
}
