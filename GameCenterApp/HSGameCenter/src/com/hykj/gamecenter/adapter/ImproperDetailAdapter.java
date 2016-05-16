
package com.hykj.gamecenter.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.logic.entry.AuthorityInfo;

public class ImproperDetailAdapter extends BaseAdapter {

    protected static final String TAG = "ImproperDetailAdapter";
    private Context mContext;
    private final LayoutInflater mInflater;
    private ViewHolder holder = null;
    private int mPosition = -1;
    private List<AuthorityInfo> mInfos;

    public ImproperDetailAdapter(Context context, List<AuthorityInfo> infos) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mInfos = infos;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mInfos.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mInfos.get(position);
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
            convertView = mInflater.inflate(R.layout.improper_detail_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        AuthorityInfo info = mInfos.get(position);
        holder.name.setText(info.name);
        holder.desp.setText(info.desp);

        return convertView;
    }

    private class ViewHolder
    {
        private final View frame;
        private TextView name;
        private TextView desp;

        public ViewHolder(View view)
        {
            frame = view;
            name = (TextView) view.findViewById(R.id.item_name);
            desp = (TextView) view.findViewById(R.id.item_desp);
        }
    }

}
