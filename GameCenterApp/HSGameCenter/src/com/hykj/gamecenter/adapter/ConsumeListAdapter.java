
package com.hykj.gamecenter.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.utils.DateUtil;
import com.hykj.gamecenter.utils.PackageUtils;
import com.hykj.gamecenter.utils.PayConstants;

public class ConsumeListAdapter extends NewGeneralGridAdapter {
    private static final String TAG = "ConsumeListAdapter";
    //    private final ArrayList<AccRechargeInfo> mRechargeList = new ArrayList<AccRechargeInfo>();
    private final Context mContext;
    private final LayoutInflater mInflater;
    private Handler mHandler;
    private ArrayList<HashMap<String, Object>> mConsumeHistoryList;

    public ConsumeListAdapter(Context context, int appType, int columnCount,
            boolean bSubjectAppList, ArrayList<HashMap<String, Object>> mHistoryList) {
        super(context, appType, columnCount, bSubjectAppList);
        setData(mHistoryList);
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getAllCount() {
        // TODO Auto-generated method stub
        return mConsumeHistoryList.size();
    }

    @Override
    public View getPeaceView(int position, View convertView, ViewGroup parentView) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.consume_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        bindItemView(position, holder, mConsumeHistoryList.get(position));
        return convertView;
    }

    private void bindItemView(final int position, ViewHolder holder,
            final HashMap<String, Object> info) {
        // TODO Auto-generated method stub

        String packname = (String) info.get(PayConstants.CONSUME_LIST_PACKNAME);
        Drawable drawable = PackageUtils.getAppIcon(mContext, packname);
        if (null != drawable) {
            holder.icon.setImageDrawable(drawable); // 此处需要获取应用icon,从packname中
        } else {
            holder.icon
                    .setImageDrawable(mContext.getResources().getDrawable(
                            R.drawable.app_icon_default));
        }

        holder.name.setText((String) info
                .get(PayConstants.CONSUME_LIST_APPNAME));//来源应用的名称

        holder.count.setText((String) info
                .get(PayConstants.CONSUME_LIST_PRODUCTNAME));// 消费商品的名称  如元宝或者道具类

        String time = DateUtil.changeDateFormat((String) info
                .get(PayConstants.CONSUME_LIST_CONSUME_TIME));// 消费时间，yyyyMMddHHmmss 
        holder.time.setText(time);

        String count = String.valueOf(info.get(PayConstants.CONSUME_LIST_CONSUMECOIN));
        holder.order.setText(count + " " + mContext.getApplicationContext().getResources()
                .getString(R.string.recharge_item_count_unit));// 消费New币(游戏中心的货币)

        String orderNo = String.valueOf(info.get(PayConstants.CONSUME_LIST_ORDERNO));
        holder.orderno.setText(orderNo);

        //        int status = (Integer) info.get(PayConstants.CONSUME_LIST_CONSUME_STATUS);
        //  消费状态，1:消费失败,2:消费成功

        // 得到当前所在行
        /*int nCurCount = (int) Math.ceil((position + 1) * 1.0
                / mColumnCount);

        // 最后一行不显示divider
        if (nCurCount == getCount()) {
            holder.dividerLine.setVisibility(View.GONE);
        } else {
            holder.dividerLine.setVisibility(View.VISIBLE);
        }*/

    }

    /*public void appendData(List<AccRechargeInfo> infoes, boolean isClear) {
        if (isClear) {
            mRechargeList.clear();
        }
        mRechargeList.addAll(infoes);
        notifyDataSetChanged();
    }*/

    public void setData(ArrayList<HashMap<String, Object>> historyList) {
        mConsumeHistoryList = historyList;
        notifyDataSetChanged();
    }

    public void removeAllData() {
        mConsumeHistoryList.clear();
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    private class ViewHolder
    {
        private final View frame;
        private ImageView icon;
        private TextView name;
        private TextView time;
        private TextView count;
        private TextView order;
        private TextView orderno;
        private TextView dividerLine;

        public ViewHolder(View view)
        {
            frame = view;
            icon = (ImageView) view.findViewById(R.id.item_icon);
            name = (TextView) view.findViewById(R.id.item_name);
            time = (TextView) view.findViewById(R.id.item_time);
            count = (TextView) view.findViewById(R.id.item_count);
            order = (TextView) view.findViewById(R.id.item_order);
            orderno = (TextView) view.findViewById(R.id.item_orderno);
            dividerLine = (TextView) view.findViewById(R.id.item_divider);
        }
    }

}
