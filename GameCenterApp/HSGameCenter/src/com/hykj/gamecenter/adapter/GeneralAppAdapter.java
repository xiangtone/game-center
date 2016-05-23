
package com.hykj.gamecenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.PhoneAppInfoActivity;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.mta.MTAConstants;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.entry.ItemViewHolder;
import com.hykj.gamecenter.utils.StringUtils;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utilscs.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GeneralAppAdapter extends GeneralGridAdapter implements
		IUpdateSingleView<Integer> {

	private final ArrayList<GroupElemInfo> mGameList = new ArrayList<GroupElemInfo>();
	private final LayoutInflater mInflater;
	private final Context mContext;

	protected ImageLoader imageLoader;
	private boolean isDisplayImage = true;

	private boolean mShowSnapShot = false;

	public static final String APP_TYPE = "app_type";
	private static final String TAG = "GeneralAppAdapter";
	private int appPosType;

	public void setAppPosType(int appPosType) {
		this.appPosType = appPosType;
	}

	boolean mbSubjectAppList = false;// 是否是专题app list （热门，最新，专题，必备）
	protected HashMap<Integer, View> mViewMap = new HashMap<Integer, View>();

	public GeneralAppAdapter(Context context, int appType, int columnCount,
							 boolean bSubjectAppList) {
		super(context, appType, columnCount, bSubjectAppList);
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();
		mbSubjectAppList = bSubjectAppList;
	}

	public void showSnapShot(boolean bshow) {
		mShowSnapShot = bshow;
	}

	@Override
	public int getAllCount() {
		return mGameList.size();
	}

	@Override
	public View getPeaceView(int positionInTotal, View convertView,
							 ViewGroup parentView) {
		ItemViewHolder holder;
		if (convertView == null) {
			if (mShowSnapShot) {
				convertView = mInflater.inflate(
						R.layout.show_snapshot_content_item, null);
			} else {
				convertView = mInflater.inflate(R.layout.recommend_list_item,
						null);
			}

			holder = new ItemViewHolder(convertView, mShowSnapShot);
			convertView.setTag(holder);
		} else {
			holder = (ItemViewHolder) convertView.getTag();
		}

		bindItemView(positionInTotal, holder, mGameList.get(positionInTotal));

		return convertView;
	}

	private void bindItemView(int positionInTotal, ItemViewHolder holder,
							  final GroupElemInfo infoes) {
		// TODO Auto-generated method stub
		final int appId = infoes.appId;
		String appName = null;

//		if (mAppType == MAIN_TYPE.RANKING) {
//			appName = positionInTotal + 1 + "  " + infoes.showName;
//		} else {
//			appName = infoes.showName;
//		}
//		holder.appname.setText(appName);

		if (mAppType == MAIN_TYPE.RANKING) {
			holder.apprank.setVisibility(View.VISIBLE);
			String rank = "" + (positionInTotal + 1);
//			Logger.i(TAG, "positionInTotal ==== " + positionInTotal);
			switch (positionInTotal){
				case 0:
					holder.apprank.setBackgroundResource(R.drawable.apprank_back_first_bg);
					holder.apprank.setTextColor(mContext.getResources().getColor(R.color.white));
					break;
				case 1:
					holder.apprank.setBackgroundResource(R.drawable.apprank_back_second_bg);
					holder.apprank.setTextColor(mContext.getResources().getColor(R.color.white));
					break;
				case 2:
					holder.apprank.setBackgroundResource(R.drawable.apprank_back_third_bg);
					holder.apprank.setTextColor(mContext.getResources().getColor(R.color.white));
					break;
				default:
					holder.apprank.setBackgroundResource(R.drawable.apprank_back_default_bg);
					holder.apprank.setTextColor(mContext.getResources().getColor(R.color.csl_black_7f));
					break;
			}
			holder.apprank.setText(rank);
		}
		holder.appname.setText(infoes.showName);
//		Logger.i("GameFragment", "infoes.downTimes====" + (infoes.downTimes));
		holder.mAppDownload.setText(Tools.showDownTimes(infoes.downTimes, mContext));
		holder.mAppSize.setText(StringUtils.byteToString(infoes
				.mainPackSize));
		//        holder.appInfo.setText(infoes.appTypeName);
		holder.rating.setRating(infoes.recommLevel / 2);
		holder.recommendInfo.setText(infoes.recommWord);

		holder.recommendInfo.setVisibility(View.GONE);
		if (mShowSnapShot) {
			holder.snapshot.setVisibility(View.VISIBLE);
			if (isDisplayImage) {
				imageLoader.displayImage(infoes.thumbPicUrl,
						holder.snapshot, DisplayOptions.optionsSnapshot);
			}
		} else {
			if (isDisplayImage) {
				imageLoader.displayImage(infoes.iconUrl, holder.icon,
						DisplayOptions.optionsIcon);

				LogUtils.i("groupInfo.groupPicUrl : " + infoes.iconUrl);
			}
		}
		holder.frame.setVisibility(View.VISIBLE);
		holder.frame.setLongClickable(false);
		// 跳转到详情
		final int position = positionInTotal + 1;
		holder.frame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// if( UITools.isFastDoubleClick( ) )
				// return;
				Intent intentDetail = new Intent(
						mContext,PhoneAppInfoActivity.class
						/*App.getDevicesType() == App.PHONE ? PhoneAppInfoActivity.class
								: PadAppInfoActivity.class*/);
				// intentDetail.putExtra( APP_TYPE , 0 );
				//                AppsGroupElemInfoParcelable info = new AppsGroupElemInfoParcelable(infoes);
				intentDetail.putExtra(KEY.GROUP_INFO, infoes);
				intentDetail
						.putExtra(StatisticManager.APP_POS_TYPE, appPosType);
				Log.i("GroupAppListActivity", "click  mAppPosType  = " + appPosType);
				if (appPosType == ReportConstants.STATIS_TYPE.RANKING) {
					intentDetail.putExtra(MTAConstants.KEY_DETAIL_PAGE_FROM,
							MTAConstants.DETAIL_RANKING + position);
				}
//                if (appPosType == ReportConstants.STATIS_TYPE.RECOM) {
				intentDetail.putExtra(StatisticManager.APP_POS_POSITION,
						infoes.posId);
//                }
//
//                intentDetail.putExtra(KEY.APPDETAIL_POSITION_SOURCE, pos);

				mContext.startActivity(intentDetail);
			}
		});

		// 得到当前所在行
		int nCurCount = (int) Math.ceil((positionInTotal + 1) * 1.0
				/ mColumnCount);

		// 最后一行不显示divider
		if (nCurCount == getCount()) {
			holder.dividerLine.setVisibility(View.GONE);
		} else {
			holder.dividerLine.setVisibility(View.VISIBLE);
		}

		// 缓存ItemViewHolder 开始
		holder.frame.setTag(holder);
		// 改为id 缓存view,而不使用packName ,因为有可能出现同包名但不同版本的应用apk
		setViewForSingleUpdate(appId, holder.frame);

//            holder.downStateView.setAppPosType(pos);
		holder.downStateView.setAppPosition(ReportConstants.reportPos(
				appPosType) + infoes.posId);
		holder.downStateView.setGroupElemInfo(infoes);
	}

	public boolean isDuplicateData(List<GroupElemInfo> infoes) {
		if (infoes.size() == mGameList.size()) {
			for (int i = 0; i < infoes.size(); i++) {
				if (infoes.get(i).appId != mGameList.get(i).appId) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	public void setDisplayImage(boolean isDisplayImage) {
		this.isDisplayImage = isDisplayImage;
	}

	@Override
	public View getViewByKey(Integer key) {
		View view = mViewMap.get(key);
		return view;
	}

	@Override
	public void setViewForSingleUpdate(Integer key, View view) {
		if (view == null) {
			return;
		}
		mViewMap.put(key, view);
	}

	@Override
	public void removeViewForSingleUpdate(View view) {

	}

	@Override
	public void removeViewForSingleUpdate(Integer key) {
		mViewMap.remove(key);
	}

	public void removeAll() {

		mViewMap.clear();
	}

	public void appendData(List<GroupElemInfo> infoes, boolean isClear) {
		if (isClear) {
			mGameList.clear();
		}
		mGameList.addAll(infoes);
		notifyDataSetChanged();
	}

	public void removeAllData() {
		mGameList.clear();
	}
}
