
package com.hykj.gamecenter.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.fragment.RechargeListFragment;
import com.hykj.gamecenter.utils.DateUtil;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.PayConstants;

public class RechargeListAdapter extends NewGeneralGridAdapter {
    private static final String TAG = "RechargeListAdapter";
    //    private final ArrayList<AccRechargeInfo> mRechargeList = new ArrayList<AccRechargeInfo>();
    private final Context mContext;
    private final LayoutInflater mInflater;
    private Handler mHandler;
    private ArrayList<HashMap<String, Object>> mRechargeHistoryList;
    private final int icons[] = new int[] {
            R.drawable.icon_alipay, R.drawable.icon_wechat, R.drawable.icon_unionpay,
            R.drawable.icon_mobilepay, R.drawable.icon_mobilepay
    };
    private static long lastClickTime = 0;
    private boolean isLoaded = true;

    private final String names[] = new String[] {
            App.getAppContext().getString(R.string.recharge_style_alipay),
            App.getAppContext().getString(R.string.recharge_style_wechatpay),
            App.getAppContext().getString(R.string.recharge_style_unionpay)
            , App.getAppContext().getString(R.string.recharge_style_mobilepay)
            , App.getAppContext().getString(R.string.recharge_style_mobilepay)
    };

    public RechargeListAdapter(Context context, int appType, int columnCount,
            boolean bSubjectAppList, ArrayList<HashMap<String, Object>> mHistoryList) {
        super(context, appType, columnCount, bSubjectAppList);
        if (mHistoryList != null) {
            setData(mHistoryList);
        }
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getAllCount() {
        // TODO Auto-generated method stub
        return mRechargeHistoryList.size();
    }

    @Override
    public View getPeaceView(int position, View convertView, ViewGroup parentView) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.recharge_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        bindItemView(position, holder, mRechargeHistoryList.get(position));
        return convertView;
    }

    private void bindItemView(final int position, final ViewHolder holder,
            final HashMap<String, Object> info) {
        // TODO Auto-generated method stub
        //  info.orderNO;  string  充值订单号，唯一标识该条充值记录
        //info.channelType;  int   渠道类型，1:支付宝，2:微信   决定icon图标和name名字  还有order显示卡号还是订单号
        //info.confirmNewCoin  int  确认New币   确定充值的金币
        //info.submitTime   string  订单创建时间，yyyyMMddHHmmss
        //info.rechargeStatus  int  充值状态，1:未处理,2:处理中,3:交易成功,4:交易失败,5:已退款
        //info.chargeAccount  string 充值卡号，如移动充值卡,点卡卡号等,非支付宝微信银联等
        int type = (Integer) info.get(PayConstants.RECHARGE_LISTITEM_TYPE);

        Logger.i(TAG, "bindItemView type=" + type);
        holder.icon.setImageResource(icons[type - 1]);
        holder.name.setText(names[type - 1]);

        //        holder.time.setText(info.submitTime);

        String time = DateUtil.changeDateFormat((String) info
                .get(PayConstants.RECHARGE_LISTITEM_SUBMITTIME));
        //        Logger.i(TAG, "bindItemView time=" + time);
        holder.time.setText(time);

        String count = String.valueOf(info.get(PayConstants.RECHARGE_LISTITEM_NEWCOIN));
        holder.count.setText(count + " " + mContext.getApplicationContext().getResources()
                .getString(R.string.recharge_item_count_unit));

        //        holder.order.setText((String) info.get(PayConstants.RECHARGE_LISTITEM_ORDERNO));
        /*  if (account.length()>25) {
              
          }else{
              
          }*/

        if (type == PayConstants.KEY_PAY_TYPE_MOBILEPAYFIR
                || type == PayConstants.KEY_PAY_TYPE_MOBILEPAYSEC) {
            holder.linear_vi.setVisibility(View.VISIBLE);
            holder.order_vi.setText((String) info.get(PayConstants.RECHARGE_LISTITEM_ACCOUNT));
            String account = (String) info.get(PayConstants.RECHARGE_LISTITEM_ORDERNO);
            if (account.contains("-")) {
                String[] str = account.split("-");
                holder.order.setText(str[2]);
            } else {
                holder.order.setText(account);
            }
        } else {
            holder.linear_vi.setVisibility(View.GONE);
            holder.order.setText((String) info.get(PayConstants.RECHARGE_LISTITEM_ORDERNO));
        }

        int status = (Integer) info.get(PayConstants.RECHARGE_LISTITEM_STATUS);
        //        String statusText = (String) info.get(PayConstants.RECHARGE_LISTITEM_TYPE_DES);
        //        holder.state.setText(statusText);
        // 充值状态，1:未处理,2:充值中,3:充值成功,4:充值失败,5:已退款         状态1 不显示
        switch (status) {
            case 2:
                holder.state.setText("充值中");
                holder.mProgressLogin.setVisibility(View.GONE);
                holder.frameLayout.setBackground(mContext.getApplicationContext().getResources()
                        .getDrawable(
                                R.drawable.btn_recharge_check_selector));
                holder.state.setTextColor(mContext.getApplicationContext().getResources()
                        .getColor(R.color.white));
                if (isLoaded) {
                    holder.state.setEnabled(true);
                    holder.state.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            /*if (isFastDoubleClick()) {
                                CSToast.show(mContext.getApplicationContext(), "10秒之后才能点击");
                            } else {
                                
                            }*/
                            holder.mProgressLogin.setVisibility(View.VISIBLE);
                            isLoaded = false;
                            mHandler.obtainMessage(RechargeListFragment.MSG_REQ_CHECK, position)
                                    .sendToTarget();
                        }
                    });
                }
                break;
            case 3:
                holder.state.setText("充值成功");
                holder.mProgressLogin.setVisibility(View.GONE);
                holder.frameLayout.setBackground(null);
                holder.state.setTextColor(mContext.getApplicationContext().getResources()
                        .getColor(R.color.nui_text_color_black_60));
                holder.state.setEnabled(false);
                break;
            case 4:
                holder.state.setText("充值失败");
                holder.mProgressLogin.setVisibility(View.GONE);
                holder.frameLayout.setBackground(null);
                holder.state.setTextColor(mContext.getApplicationContext().getResources()
                        .getColor(R.color.nui_text_color_black_60));
                holder.state.setEnabled(false);
                break;
            case 5:
                holder.state.setText("已退款");
                holder.mProgressLogin.setVisibility(View.GONE);
                holder.frameLayout.setBackground(null);
                holder.state.setTextColor(mContext.getApplicationContext().getResources()
                        .getColor(R.color.nui_text_color_black_60));
                holder.state.setEnabled(false);
                break;
        }

        /* if (status == 2 && isLoaded) {
             holder.state.setEnabled(true);
             holder.state.setOnClickListener(new OnClickListener() {

                 @Override
                 public void onClick(View v) {
                     // TODO Auto-generated method stub
                     if (isFastDoubleClick()) {
                         CSToast.show(mContext.getApplicationContext(), "10秒之后才能点击");
                     } else {
                         
                     }
                     holder.mProgressLogin.setVisibility(View.VISIBLE);
                     isLoaded = false;
                     mHandler.obtainMessage(RechargeListFragment.MSG_REQ_CHECK, position)
                             .sendToTarget();
                 }
             });
         } else {
             holder.state.setEnabled(false);
         }*/

        // 得到当前所在行
        /*int nCurCount = (int) Math.ceil((position + 1) * 1.0
                / mColumnCount);*/

        // 最后一行不显示divider
        /* if (nCurCount == getCount()) {
             holder.dividerLine.setVisibility(View.GONE);
         } else {
             holder.dividerLine.setVisibility(View.VISIBLE);
         }*/

    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    public void setData(ArrayList<HashMap<String, Object>> historyList) {
        mRechargeHistoryList = historyList;
        notifyDataSetChanged();
    }

    public void removeAllData() {
        mRechargeHistoryList.clear();
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public static synchronized boolean isFastDoubleClick() {
        return isFastDoubleClick(10000);
    }

    public static synchronized boolean isFastDoubleClick(int respTime) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        Logger.i(TAG, "timeD====" + timeD);
        if (0 < timeD && timeD < respTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private class ViewHolder
    {
        private final View frame;
        private ImageView icon;
        private TextView name;
        private TextView time;
        private TextView count;
        private TextView order;
        private TextView dividerLine;
        private Button state;
        private RelativeLayout frameLayout;
        private ProgressBar mProgressLogin;
        private LinearLayout linear_vi;
        private TextView order_vi;

        public ViewHolder(View view)
        {
            frame = view;
            icon = (ImageView) view.findViewById(R.id.item_icon);
            name = (TextView) view.findViewById(R.id.item_name);
            time = (TextView) view.findViewById(R.id.item_time);
            count = (TextView) view.findViewById(R.id.item_count);
            order = (TextView) view.findViewById(R.id.item_order);
            state = (Button) view.findViewById(R.id.item_state);
            dividerLine = (TextView) view.findViewById(R.id.item_divider);
            frameLayout = (RelativeLayout) view.findViewById(R.id.item_frame);
            mProgressLogin = (ProgressBar) view.findViewById(R.id.progressLogin);
            linear_vi = (LinearLayout) view.findViewById(R.id.item_linear_vi);
            order_vi = (TextView) view.findViewById(R.id.item_order_vi);
        }
    }

}
