
package com.hykj.gamecenter.adapter;

import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.ImproperReportActivity;
import com.hykj.gamecenter.activity.ImproperReportActivity.ImproperReportHandler;
import com.hykj.gamecenter.utils.Logger;

public class ImproperReportAdapter extends BaseAdapter {

    protected static final String TAG = "ImproperReportAdapter";
    private Context mContext;
    private List<String> mReports;
    private final LayoutInflater mInflater;
    private ViewHolder holder = null;
    protected LinkedHashMap<Integer, Boolean> mCheckBoxStatusMap = new LinkedHashMap<Integer, Boolean>();
    private ImproperReportHandler mHandler;
    private int mPosition = -1;

    public ImproperReportAdapter(Context context, List<String> reports,
            ImproperReportHandler handler) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mReports = reports;
        mHandler = handler;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 初始化checkbox的状态
        for (int i = 1; i <= mReports.size(); i++) {
            mCheckBoxStatusMap.put(i, false);
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mReports.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mReports.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.improper_report_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mReports.get(position));

        holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                Logger.i(TAG, "isChecked == " + isChecked);

                mPosition = position + 1;
                //                Logger.i(TAG, "mPosition == " + mPosition);
                if (mCheckBoxStatusMap.containsKey(mPosition)) {
                    mCheckBoxStatusMap.put(mPosition, isChecked);
                }
                //                Logger.i(TAG, "mCheckBoxStatusMap == " + mCheckBoxStatusMap);
                mHandler.obtainMessage(ImproperReportActivity.CHECK_STATE_CHANGE,
                        mCheckBoxStatusMap).sendToTarget();
            }
        });

        holder.frame.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CheckBox checkbox = (CheckBox) v.findViewById(R.id.item_checkbox);
                //                Logger.i(TAG, "checkbox  checkstate== " + checkbox.isChecked());
                if (!checkbox.isChecked()) {
                    checkbox.setChecked(true);
                } else {
                    checkbox.setChecked(false);
                }
            }
        });

        // 设置是否是勾选
        /*if (mCheckBoxStatusMap.get(position + 1) == false)
        {
            mCheckBoxStatusMap.put(position + 1, false);
            holder.checkbox.setChecked(false);
        }
        else
        {
            holder.checkbox.setChecked(mCheckBoxStatusMap.get(position + 1).booleanValue());
        }*/

        return convertView;
    }

    private class ViewHolder
    {
        public final View frame;
        public CheckBox checkbox;
        public TextView name;

        public ViewHolder(View view)
        {
            frame = view;
            name = (TextView) view.findViewById(R.id.item_name);
            checkbox = (CheckBox) view.findViewById(R.id.item_checkbox);
        }
    }

}
