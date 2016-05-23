package com.hykj.gamecenter.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.GameClassifyActivity;
import com.hykj.gamecenter.activity.GroupAppListActivity;
import com.hykj.gamecenter.controller.ProtocolListener.CLASSIFY_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ClassifyListAdapter extends GroupListAdapter {
	private final ImageLoader imageLoader;
	private final LayoutInflater mInflater;
	private final Context mContext;
	private final ArrayList<GroupInfo> mDataList = new ArrayList<GroupInfo>();
	private final boolean isDisplayImage = false;
	private int mAppPosType = 0;

	private int mClassifyType = CLASSIFY_TYPE.APP_CLASSIFY;

	Resources mRes = null;

	public ClassifyListAdapter(Context context, int appType, int appPosType,
			int columnCount, int classifyType) {
		super(context, appType, columnCount);
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();
		mAppPosType = appPosType;
		mClassifyType = classifyType;

		mRes = context.getResources();

	}

	public void removeAllData() {
		mDataList.clear();
	}

	public void appendData(ArrayList<GroupInfo> infoes) {
		mDataList.addAll(infoes);
	}

	@Override
	public int getAllCount() {
		// TODO Auto-generated method stub
		return mDataList.size();
	}

	@Override
	public View getPeaceView(int positionInTotal, View convertView,
			ViewGroup parentView) {
		ItemHolder holder;
		if (convertView == null) {
			// if( mAppType == MAIN_TYPE.TOPIC )
			// {
			// convertView = mInflater.inflate( R.layout.topic_content_item ,
			// null );
			// }
			// else
			// {
			convertView = mInflater.inflate(R.layout.category_content_item,
					null);
			// }

			holder = new ItemHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}

		bindItemView(holder, positionInTotal);
		return convertView;
	}

	private void bindItemView(ItemHolder holder, int position) {
		final GroupInfo info = mDataList.get(position);
		// final Bitmap iconBitmap =
		// ImageManager.getInstance().getImage(info.getGroupImgUrl(),
		// imageHandler, false);
		// if (iconBitmap != null) {
		// holder.icon.setImageBitmap(iconBitmap);
		// }

		// if( mAppType == MAIN_TYPE.TOPIC )
		// {
		// ImageLoader.getInstance( ).displayImage( info.groupPicUrl ,
		// holder.iconTopic , DisplayOptions.optionsSnapshot );
		// }
		// else
		// {
		// if( isDisplayImage )
		// {
		ImageLoader.getInstance().displayImage(info.groupPicUrl, holder.icon,
				DisplayOptions.optionsIcon);
		// }
		// }

		holder.name.setText(info.groupName);
		holder.tip.setText(info.groupDesc);
		holder.count.setText(info.recommWrod);
		if (position == 0 && mClassifyType == CLASSIFY_TYPE.APP_CLASSIFY) {
			holder.tip.setVisibility(View.GONE);
			holder.count.setVisibility(View.GONE);
		} else {
			holder.tip.setVisibility(View.VISIBLE);
			holder.count.setVisibility(View.VISIBLE);
		}

		holder.frame.setVisibility(View.VISIBLE);
		// TODO 进入游戏分类中的具体某一类
		holder.frame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// if( UITools.isFastDoubleClick( ) )
				// return;
				if (info.groupType == GROUP_TYPE.CLASSIFY_GAME_TYPE) {
					Intent intentAppList = new Intent(mContext,
							GameClassifyActivity.class);
					mContext.startActivity(intentAppList);
				} else {
					Intent intentAppList = new Intent(mContext,
							GroupAppListActivity.class);
					intentAppList.putExtra(KEY.MAIN_TYPE, mAppType);
					intentAppList.putExtra(KEY.ORDERBY, info.orderType);
					intentAppList.putExtra(KEY.GROUP_ID, info.groupId);
					intentAppList.putExtra(KEY.GROUP_CLASS, info.groupClass);
					intentAppList.putExtra(KEY.GROUP_TYPE, info.groupType);
					intentAppList.putExtra(KEY.CATEGORY_NAME, info.groupName);
					intentAppList.putExtra(StatisticManager.APP_POS_TYPE,
							mAppPosType);
					mContext.startActivity(intentAppList);
				}

				// if( mMainType == MAIN_TYPE.TOPIC )
				// {
				// TopicInfo topicInfo = new TopicInfo( );
				// topicInfo.mAppCount = info.getAppCount( );
				// topicInfo.mTitle = info.getGroupName( );
				// topicInfo.mTip = info.getGroupDesc( );
				// topicInfo.mPicURL = info.getGroupImgUrl( );
				// intentAppList.putExtra( KEY.TOPIC_INFO , topicInfo );
				// }

			}
		});

		// 得到当前所在行
		int nCurCount = (int) Math.ceil((position + 1) * 1.0 / mColumnCount);

		// 最后一行不显示divider
		if (nCurCount == getCount()) {
			holder.dividerLine.setVisibility(View.GONE);
		} else {
			holder.dividerLine.setVisibility(View.VISIBLE);
		}
	}

	// public void setDisplayImage( boolean isDisplayImage )
	// {
	// this.isDisplayImage = isDisplayImage;
	// }

	private class ItemHolder {
		View frame;
		ImageView icon;
		// FitWidthImageView iconTopic;
		TextView name;
		TextView count;
		TextView tip;
		public TextView dividerLine;

		public ItemHolder(View view) {
			frame = view;
			// if( mAppType == MAIN_TYPE.TOPIC )
			// {
			// iconTopic = (FitWidthImageView)view.findViewById( R.id.group_icon
			// );
			// iconTopic.setScaleType( ImageView.ScaleType.MATRIX );
			// }
			// else
			// {
			icon = (ImageView) view.findViewById(R.id.group_icon);
			// }
			name = (TextView) view.findViewById(R.id.group_name);
			count = (TextView) view.findViewById(R.id.group_app_count);
			tip = (TextView) view.findViewById(R.id.group_tip);
			dividerLine = (TextView) view.findViewById(R.id.list_item_divider);
		}
	}
}
