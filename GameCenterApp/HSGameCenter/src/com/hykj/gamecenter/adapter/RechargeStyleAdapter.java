
package com.hykj.gamecenter.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.AliPayActivity;
import com.hykj.gamecenter.activity.MobilePayActivity;
import com.hykj.gamecenter.activity.RechargeActivity;
import com.hykj.gamecenter.activity.UnionPayActivity;
import com.hykj.gamecenter.activity.WeChatPayActivity;
import com.hykj.gamecenter.utils.PayConstants;

public class RechargeStyleAdapter extends BaseAdapter {
    private RechargeActivity mContext;
    private final LayoutInflater inflater;
    private ArrayList<Integer> mTypeList;

    private final int icons[] = new int[] {
            R.drawable.icon_alipay, R.drawable.icon_wechat, R.drawable.icon_unionpay,
            R.drawable.icon_mobilepay, R.drawable.icon_mobilepay
    };

    private final String names[] = new String[] {
            App.getAppContext().getString(R.string.recharge_style_alipay),
            App.getAppContext().getString(R.string.recharge_style_wechatpay),
            App.getAppContext().getString(R.string.recharge_style_unionpay)
            , App.getAppContext().getString(R.string.recharge_style_mobilepay)
            , App.getAppContext().getString(R.string.recharge_style_mobilepay)
    };

    public RechargeStyleAdapter(Activity context, ArrayList<Integer> typeList) {
        setTypeList(typeList);
        mContext = (RechargeActivity) context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTypeList(ArrayList<Integer> typeList) {
        mTypeList = typeList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.activity_recharge_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        bindData(holder, position);
        return convertView;
    }

    private void bindData(ViewHolder holder, final int position) {
        final Intent intent = new Intent();
        final int mPosition = mTypeList.get(position) - 1;
        holder.icon.setImageResource(icons[mPosition]);
        holder.name.setText(names[mPosition]);
        holder.item.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (mPosition)
                {
                    case 0:
                        intent.setClass(mContext, AliPayActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(mContext, WeChatPayActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(mContext, UnionPayActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(mContext, MobilePayActivity.class);
                        intent.putExtra(PayConstants.KEY_PAYTYPE_MOBILE, mTypeList.get(position));
                        mContext.startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(mContext, MobilePayActivity.class);
                        intent.putExtra(PayConstants.KEY_PAYTYPE_MOBILE, mTypeList.get(position));
                        mContext.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private class ViewHolder
    {
        private final View frame;
        private ImageView icon;
        private TextView name;
        private View item;

        public ViewHolder(View view)
        {
            frame = view;
            icon = (ImageView) view.findViewById(R.id.item_icon);
            name = (TextView) view.findViewById(R.id.item_name);
            item = view.findViewById(R.id.item);
        }
    }

}
