package com.hykj.gamecenter.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.protocol.Apps;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utils.UITools;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OddsHou on 2015/12/26.
 */
public class NoviceGridAdapter extends BaseAdapter {
	private Context mContext;
	protected ImageLoader imageLoader;
	private final ArrayList<Apps.GroupElemInfo> mAppList = new ArrayList<Apps.GroupElemInfo>();
	private final ArrayList<Boolean> mAppListCheckedState = new ArrayList<Boolean>();
	IListItemCheckedClickListener mItemCheckedClickListener = null;
	private View mConvertView;

	public NoviceGridAdapter(Context context, int appType) {
		// TODO Auto-generated constructor stub
		mContext = context;
		imageLoader = ImageLoader.getInstance();
	}

	public View getConvertView(){
		return LayoutInflater.from(mContext).inflate(R.layout.novice_guidance_grid_item, null);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAppList.size();
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
	public View getView(int positionInTotal, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = getConvertView();

			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		bindItemView(holder, positionInTotal);

		return convertView;
	}

	private void bindItemView(final ViewHolder holder, final int positionInTotal) {
		// TODO Auto-generated method stub
		Apps.GroupElemInfo infoes = mAppList.get(positionInTotal);
		if (TextUtils.isEmpty(infoes.showName)) {
			//不够，填空
			holder.checkBox.setVisibility(View.INVISIBLE);
			holder.icon.setVisibility(View.INVISIBLE);
			holder.iconMask.setVisibility(View.INVISIBLE);
			holder.appname.setVisibility(View.INVISIBLE);
			holder.mAppDownload.setVisibility(View.INVISIBLE);
			holder.frame.setClickable(false);
			return;
		}
		final int appId = infoes.appId;
		String appName = infoes.showName;
		holder.appname.setText(appName);
		holder.mAppDownload.setText(Tools.showDownTimes(infoes.downTimes, mContext));
		imageLoader.displayImage(infoes.iconUrl, holder.icon,
				DisplayOptions.optionsIcon);
		holder.frame.setVisibility(View.VISIBLE);
		holder.frame.setLongClickable(false);

		// 设置选项checkBox
		holder.checkBox.setChecked(mAppListCheckedState.get(positionInTotal));

		holder.checkBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAppListCheckedState.set(positionInTotal,
						!mAppListCheckedState.get(positionInTotal));

				holder.checkBox.setChecked(mAppListCheckedState.get(positionInTotal));
				mItemCheckedClickListener.OnListItemCheckedClick(holder.checkBox.isChecked());
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

				holder.checkBox.setChecked(mAppListCheckedState.get(positionInTotal));
				mItemCheckedClickListener.OnListItemCheckedClick(holder.checkBox.isChecked());

			}
		});

		// 缓存ItemViewHolder 开始
		holder.frame.setTag(holder);

	}

	private class ViewHolder{
		public View frame;
		public ImageView icon;
		public ImageView iconMask;
		public TextView appname;
		public TextView mAppDownload;
		public CheckBox checkBox;

		public ViewHolder(View view) {
			frame = view;
			appname = (TextView) view.findViewById(R.id.app_name);
			mAppDownload = (TextView) view.findViewById(R.id.app_download);
			icon = (ImageView) view.findViewById(R.id.app_icon);
			iconMask = (ImageView) view.findViewById(R.id.app_icon_mask);
			// 得到选项checkBox
			checkBox = (CheckBox) view.findViewById(R.id.edit_check_box);
		}
	}


	public void initAppCheckStateList() {
		mAppListCheckedState.clear();

		for (int i = 0; i < mAppList.size(); i++) {
			mAppListCheckedState.add(true);
		}
	}

	public void appendData(List<Apps.GroupElemInfo> infoes, boolean isClear) {
		if (isClear) {
			mAppList.clear();
			mAppListCheckedState.clear();
		}
		while (infoes.size() < UITools.getNoviceGuidanceSize(mContext)){
			Apps.GroupElemInfo groupElemInfo = new Apps.GroupElemInfo();
			infoes.add(groupElemInfo);
		}
		mAppList.addAll(infoes);
		initAppCheckStateList();

		notifyDataSetChanged();
	}

//	public void removeAllData() {
//		mAppList.clear();
//		initAppCheckStateList();
//	}

	public void setListItemCheckedClickListener(
			IListItemCheckedClickListener itemCheckedClickListener) {
		mItemCheckedClickListener = itemCheckedClickListener;
	}

	public ArrayList<Boolean> getCheckList(){
		return mAppListCheckedState;
	}

}
