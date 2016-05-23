
package com.hykj.gamecenter.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.GroupAppListActivity;
import com.hykj.gamecenter.activity.PhoneAppInfoActivity;
import com.hykj.gamecenter.controller.ProtocolListener.ELEMENT_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.HOME_PAGE_POSITION;
import com.hykj.gamecenter.controller.ProtocolListener.ITEM_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.ORDER_BY;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.data.TopicInfo;
import com.hykj.gamecenter.db.CSACContentProvider;
import com.hykj.gamecenter.db.CSACDatabaseHelper.GroupInfoColumns;
import com.hykj.gamecenter.db.DatabaseUtils;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.mta.MTAConstants;
import com.hykj.gamecenter.mta.MtaUtils;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.protocol.Reported.ReportedInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.entry.ItemViewHolder;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.StringUtils;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utilscs.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MixGridAdapter extends BaseAdapter {
	protected final Context mContext;
	private final LayoutInflater mInflater;
	protected int mColumnCount = 4;
	protected final int mAppType;
	protected ImageLoader imageLoader;
	private static final LinearLayout.LayoutParams llp_app = new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	private static final LinearLayout.LayoutParams llp_padding = new LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	private static final LinearLayout.LayoutParams llp_ad = new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	private final ArrayList<GroupElemInfo> mAppList = new ArrayList<GroupElemInfo>();
	private static final int MAX_ADV_ONE_LINE = 100;
	private static final String TAG = "MixGridAdapter";
	private int appPosType;
	Resources mRes = null;
	/**
	 * 内部广告位 2行
	 */
	private int mAdvLineCounts = 2;
	/**
	 * 内部广告位 每行 2个
	 */
	private int mInlineAdvCounts = 2;

	// mAppInfoSonList 中存不同类型(根据 AppInfoSon.mType 区分)的
	// AppList(AppInfoSon.AppList)
	protected ArrayList<AppInfoSon> mAppInfoSonList = new ArrayList<AppInfoSon>();

	public void setAppPosType(int appPosType) {
		this.appPosType = appPosType;
	}

	public MixGridAdapter(Context context, int appType, int columnCount) {
		super();
		imageLoader = ImageLoader.getInstance();
		mContext = context;
		mColumnCount = columnCount;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mAppType = appType;
		mRes = context.getResources();

		llp_app.weight = 1;
		int margins = (int) mRes
				.getDimension(R.dimen.recommended_adv_subject_entry_padding);
		int marginBottom = (int) mRes
				.getDimension(R.dimen.recommended_adv_subject_entry_padding);
		llp_app.setMargins(0, 0/* margins */, 0, 0/* marginBottom */);// 只填充了左margin

		llp_ad.weight = 1;
		llp_ad.setMargins(0, 0, 0, 0);
		if (App.getDevicesType() == App.PHONE) {
			mAdvLineCounts = 2;    //广告位2行
			mInlineAdvCounts = 2; //广告位每行2个
		} else {
			mAdvLineCounts = 1;    //广告位只有1行
			mInlineAdvCounts = MAX_ADV_ONE_LINE; //每行多个
		}
	}

	/*
	 * 重(@Override)写 getViewTypeCount() – 返回你有多少个不同的布局 重写 getItemViewType(int) –
	 * 由position返回view type id
	 */
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return super.getViewTypeCount();
	}

	@Override
	public int getCount() {
		int nCount = (int) Math.ceil(getAllCount() * 1.0 / mColumnCount);// 行数
		return nCount;
	}

	@Override
	public Object getItem(int position) {
		Log.e(TAG, "getItem");
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void setColumnCount(int count) {
		mColumnCount = count;
		updateAppInfoSonLineSize();
	}

	/**
	 * 这个算法效率 有问题，每次 新添加数据，都需要将之前的数据 从新 分组
	 * <p/>
	 * oddshou
	 *
	 * @param infoes
	 */
	public void appendData(List<GroupElemInfo> infoes) {
		mAppList.addAll(infoes);
		AppInfoDivision(mAppList);
	}

	public void removeAllData() {
		mAppList.clear();
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/* Log.e(TAG, "getView"); */
		LinearLayout lineContainer = getLineContainer(convertView);
		//2.获取listview 当前 position所需要的数据  LineData
		LineData lineData = getLineData(position);

		return loadLineData(lineData, lineContainer);
	}

	private LinearLayout getLineContainer(View convertView) {
		LinearLayout lineContainer = (LinearLayout) convertView;

		if (lineContainer == null) {
			lineContainer = new LinearLayout(mContext);
			lineContainer.setOrientation(LinearLayout.HORIZONTAL);
			lineContainer.setGravity(Gravity.CENTER_VERTICAL);
		} else {
			lineContainer.removeAllViews();
		}
		return lineContainer;
	}

	private View loadLineData(LineData lineData, LinearLayout lineContainer) {
		if (lineData == null)
			return lineContainer;

		if (lineData.getType() == HOME_PAGE_POSITION.SHOW_TYPE_ADV) {
			//一下区分好像没什么用，处理方式 是一样的 ##############oddshou
			if (appPosType == ReportConstants.STATIS_TYPE.GAME)
				loadAdData(lineContainer, lineData, App.getAppContext()
						.getString(R.string.newest_game_label));
			else
				loadAdData(lineContainer, lineData, App.getAppContext()
						.getString(R.string.newest_app_label));
		} else {
			loadAppData(lineContainer, lineData);
		}
		return lineContainer;
	}

	/**
	 * 获取列表数据项的总长度
	 *
	 * @return
	 */
	public int getAllCount() {
		return mAppList.size();
	}

	/**
	 * 获取每个数据项的视图
	 *
	 * @param positionInTotal
	 * @param convertView
	 * @param parentView
	 * @return
	 */
	protected View getAppView(int positionInTotal, final GroupElemInfo infoes,
							  View convertView) {
		ItemViewHolder holder;

		if (infoes == null)
			return null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.recommend_list_item, null);

			holder = new ItemViewHolder(convertView, false);
			convertView.setTag(holder);
		} else {
			holder = (ItemViewHolder) convertView.getTag();
		}

		bindAppItemView(positionInTotal, holder, infoes);

		return convertView;
	}

	private void bindAppItemView(int positionInTotal, ItemViewHolder holder,
								 final GroupElemInfo infoes) {
		// TODO Auto-generated method stub
		Logger.e("GameFragment", "recommFlag  ==  " + infoes.recommFlag);
		//        holder.appInfo.setText(infoes.appTypeName);
		holder.mAppDownload.setText(Tools.showDownTimes(infoes.downTimes, mContext));
		holder.mAppSize.setText(StringUtils.byteToString(infoes
				.mainPackSize));
		holder.rating.setRating(infoes.recommLevel / 2);
		holder.recommendInfo.setText(infoes.recommWord);

		holder.rating.setVisibility(View.GONE);
		holder.recommendInfo.setVisibility(View.VISIBLE);
		holder.appname.setText(infoes.showName);
		Logger.i(TAG, "appname  width ===  " + (holder.appname.getWidth()) + "   positionInTotal = "+positionInTotal, "tom");

		holder.appname.setText(infoes.showName);
//		if(infoes.recommFlag > 0){
//			ArrayList<Integer> list = Utils.getRecommend(infoes.recommFlag);
//			//推荐标签，编辑指定，1=官方 2=推荐 4=首发 8=免费 16=礼包 32=活动 64=内测 128=热门
//			//免费和首发   4,8  使用 firstpublish_bg
//			//内测和官方   64,1  使用official_bg
//			//热门和礼包   16,128  使用hot_bg
//			//推荐和活动   2,32   使用recommend_bg
//			Logger.e("GameFragment", "list  ==  " + list.toString());
//			if(list.size()>0){
//				holder.apprecommlinear.setVisibility(View.VISIBLE);
//				holder.apprecommlinear.removeAllViews();
//				for(int i = 0;i<list.size();i++) {
//					int flag = list.get(i);
//					TextView view = getFlagTextView(flag);
//
//					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//					lp.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.normal_margin_padding_little5);
//					lp.weight = 0;
//					holder.apprecommlinear.setOrientation(LinearLayout.HORIZONTAL);
//					holder.apprecommlinear.setGravity(Gravity.CENTER_VERTICAL);
//					holder.apprecommlinear.addView(view, lp);
//				}
//			}
//		}else {
//			holder.apprecommlinear.setVisibility(View.GONE);
//		}
		if(infoes.recommFlag > 0){
			holder.appname.addFlag(infoes.recommFlag);
			holder.appname.setFlagVisible(View.VISIBLE);
		}else {
			holder.appname.setFlagVisible(View.GONE);

		}

		imageLoader.displayImage(infoes.iconUrl, holder.icon,
				DisplayOptions.optionsIcon);

		holder.frame.setVisibility(View.VISIBLE);
		holder.frame.setLongClickable(false);
		// 跳转到详情
		holder.frame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentDetail = new Intent(
						mContext, PhoneAppInfoActivity.class
						/*App.getDevicesType() == App.PHONE ? PhoneAppInfoActivity.class
                                : PadAppInfoActivity.class*/);
				//                AppsGroupElemInfoParcelable info = new AppsGroupElemInfoParcelable(infoes);
				intentDetail.putExtra(KEY.GROUP_INFO, infoes);
				intentDetail.putExtra(KEY.MAIN_TYPE, mAppType);
				intentDetail
						.putExtra(StatisticManager.APP_POS_TYPE, appPosType);
				//普通位置也要上报 位置 id    oddshou
				//                if (appPosType == STATIS_TYPE.RECOM) {
				intentDetail.putExtra(StatisticManager.APP_POS_POSITION,
						infoes.posId);
				//                }

				// 广告位点击上报
				ReportedInfo build = new ReportedInfo();
				int posId = infoes.posId + ReportConstants.STAC_APP_POSITION_GAME_PAGE;
				int type = infoes.elemType;
				int elemId = 0;
				switch (type) {
					case ELEMENT_TYPE.TYPE_APP:
						elemId = infoes.appId;
						break;

					case ELEMENT_TYPE.TYPE_LINK:
						elemId = infoes.jumpLinkId;
						break;
					case ELEMENT_TYPE.TYPE_SKIP_LOCAL_OR_ONLINE:
					case ELEMENT_TYPE.TYPE_SKIP_TIP:
					case ELEMENT_TYPE.TYPE_SKIP_CLASS:
						elemId = infoes.jumpGroupId;
						break;
					default:
						break;
				}
				build.statActId = ReportConstants.STATACT_ID_ADV;
				build.statActId2 = 1; //首页元素
				build.ext1 = "" + posId;
				build.ext2 = "" + type;
				build.ext3 = "" + elemId;
				build.ext4 = "" + infoes.showType; //showType 1 广告位, 0 表示其他
				ReportConstants.getInstance().reportReportedInfo(build);
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

		// 设置按钮下载状态
		int pos = infoes.posId + ReportConstants.reportPos(appPosType);
		//        holder.downStateView.setAppPosType(pos);
		holder.downStateView.setAppPosition(pos);
		Logger.e("DownloadStateViewCustomization", "appPosType====" + pos);
		holder.downStateView.setGroupElemInfo(infoes);
	}



	/**
	 * 当前方法 convertView 没有重用，convertView可能为空,可能不为空，目前统一做 为空的处理
	 *
	 * @param info
	 * @param convertView
	 * @param index
	 * @return
	 */
	public View getAdvView(final GroupElemInfo info, View convertView, int index) {

		ImageView adView = null;
		if (convertView != null) {
			adView = (ImageView) convertView.getTag(R.id.tag_adv_view_item);
			//            adView = (ImageView) convertView;
		}
		if (adView == null) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.recommend_list_ad, null);
			}
			adView = (ImageView) convertView.findViewById(R.id.ad);
			convertView.setTag(R.id.tag_adv_view_item, adView);
		}

		//        ImageView adView = null;
		// 处理广告被分为两页的情况，若用上面的会报错
		//        convertView = mInflater.inflate(R.layout.recommend_list_ad, null);
		//        adView = (ImageView) convertView.findViewById(R.id.ad);

		FrameLayout frameLayout = (FrameLayout) convertView
				.findViewById(R.id.frameLayout);
		int iImageViewWidth = 0;
		if (App.getDevicesType() == App.PHONE) {
			// 计算ImageView宽,以下计算方式 适用于 2*2
			int iMarginWidth = (int) mRes
					.getDimension(R.dimen.list_view_item_spacing);

			int iImageViewPaddingWidth = (int) mRes
					.getDimension(R.dimen.recommended_adv_subject_entry_padding);

			int screenWidth = Tools.getDisplayWidth(mContext);
			iImageViewWidth = (int) ((screenWidth - 2 * iMarginWidth - (mInlineAdvCounts - 1)
					* iImageViewPaddingWidth)
					* 1.0 / mInlineAdvCounts + 0.5f);
		} else {
			//适用于平板，单行模式
			iImageViewWidth = (int) mRes
					.getDimension(R.dimen.recommend_adv_width);
		}
		int iImageViewHeight = (int) mRes
				.getDimension(R.dimen.recommend_adv_height);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				iImageViewWidth, iImageViewHeight);
		// 设置ImageView宽
		// adView.setLayoutParams(params);
		frameLayout.setLayoutParams(params);

		ImageLoader.getInstance().displayImage(info.adsPicUrl, adView,
				DisplayOptions.optionsHomepage);
		Logger.i(TAG, "getAdvView " + info.showName + "  "+ index, "oddshou");
		adView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				advOnClick(info);
			}
		});
		return convertView;
	}

	private void advOnClick(GroupElemInfo info) {

		if (info == null) {
			return;
		}
		// int appPosType = 0;
		int position = info.posId;
		Log.e("GameFragment", "position====" + position);
		// 广告位点击上报
		int posId = 0;
		int elemId = 0;
		boolean isApp = false;
		if (mAppType == MAIN_TYPE.APP) {
			posId = position + ReportConstants.STAC_APP_POSITION_RECOMM_PAGE;
			isApp = true;
		} else {
			posId = position + ReportConstants.STAC_APP_POSITION_GAME_PAGE;
			isApp = false;
		}
		int type = info.elemType;
		Intent intent = new Intent();
		switch (type) {
			case ELEMENT_TYPE.TYPE_APP:
				intent.setClass(
						mContext, PhoneAppInfoActivity.class
                        /*App.getDevicesType() == App.PHONE ? PhoneAppInfoActivity.class
                                : PadAppInfoActivity.class*/);
				//                AppsGroupElemInfoParcelable infos = new AppsGroupElemInfoParcelable(info);
				intent.putExtra(KEY.GROUP_INFO, info);
				intent.putExtra(KEY.APP_POSITION, position);
				intent.putExtra(StatisticManager.APP_POS_TYPE, appPosType);
				intent.putExtra(StatisticManager.APP_POS_POSITION, position);
				intent.putExtra(MTAConstants.KEY_DETAIL_PAGE_FROM,
						isApp ? MTAConstants.DETAIL_RECOMMEND_PAGE_FIXED_ADV
								: MTAConstants.DETAIL_GAME_PAGE_FIXED_ADV);
				elemId = info.appId;
				break;

			case ELEMENT_TYPE.TYPE_LINK:
				String link = info.jumpLinkUrl;
				Uri web = Uri.parse(link);
				intent = new Intent(Intent.ACTION_VIEW, web);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				elemId = info.jumpLinkId;
				break;

			case ELEMENT_TYPE.TYPE_SKIP_LOCAL_OR_ONLINE:
			case ELEMENT_TYPE.TYPE_SKIP_TIP:
			case ELEMENT_TYPE.TYPE_SKIP_CLASS:
				intent = jumpToTopicOrClass(intent, info, type, appPosType);
				elemId = info.jumpGroupId;
				break;
			default:
				break;
		}
		// 广告位点击上报
		ReportedInfo build = new ReportedInfo();
		build.statActId = ReportConstants.STATACT_ID_ADV;
		build.statActId2 = 1; //首页元素
		build.ext1 = posId + "";
		build.ext2 = type + "";
		build.ext3 = elemId + "";
		build.ext4 = 1 + ""; //showType 1 广告位, 0 表示其他
		build.ext5 = "";
		build.actionTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
				.format(new Date());

		ReportConstants.getInstance().reportReportedInfo(build);
		MtaUtils.trackAdvClick(isApp, "FixedAdv " + position);
		mContext.startActivity(intent);
	}

	private Intent jumpToTopicOrClass(Intent intent, GroupElemInfo info,
									  int type, int appPosType) {
		if (info.jumpGroupId > 0) {
			GroupInfo mGroupInfo = new GroupInfo();
			mGroupInfo = getGroupIDByDB(info);

			TopicInfo topicInfo = new TopicInfo();
			topicInfo.mAppCount = mGroupInfo.recommWrod;
			topicInfo.mTopic = mGroupInfo.groupName;
			topicInfo.mTip = mGroupInfo.groupDesc;
			topicInfo.mPicUrl = mGroupInfo.groupPicUrl;

			intent.putExtra(KEY.TOPIC_INFO, topicInfo);

			intent.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.TOPIC);
			intent.putExtra(KEY.ITEM_TYPE, ITEM_TYPE.UNSHOW_SNAPSHOT);

			intent.putExtra(KEY.GROUP_ID, mGroupInfo.groupId);
			intent.putExtra(KEY.GROUP_CLASS, mGroupInfo.groupClass);
			intent.putExtra(KEY.GROUP_TYPE, mGroupInfo.groupType);
			intent.putExtra(KEY.ORDERBY, getGroupIDByDB(info).orderType);
			intent.putExtra(KEY.CATEGORY_NAME,
					mContext.getString(R.string.topic_game_label));
		} else {
			intent.putExtra(KEY.GROUP_ID, getGroupIDByDB(info).groupId);
			intent.putExtra(KEY.GROUP_CLASS, getGroupIDByDB(info).groupClass);
			intent.putExtra(KEY.GROUP_TYPE, getGroupIDByDB(info).groupType);
			intent.putExtra(KEY.ORDERBY, getGroupIDByDB(info).orderType);
			intent.putExtra(KEY.CATEGORY_NAME, getGroupIDByDB(info).groupName);
			intent.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.JING_PIN);
		}

		intent.putExtra(KEY.ORDERBY, info.jumpOrderType);
		intent.putExtra(StatisticManager.APP_POS_TYPE, appPosType);
		intent.setClass(App.getAppContext(), GroupAppListActivity.class);

		return intent;
	}

	private GroupInfo getGroupIDByDB(GroupElemInfo info) {
		GroupInfo groupInfo = new GroupInfo();
		Cursor cursor = mContext.getContentResolver().query(
				CSACContentProvider.GROUPINFO_CONTENT_URI,
				null,
				GroupInfoColumns.GROUP_TYPE + " =? and "
						+ GroupInfoColumns.GROUP_ID + " =?",
				new String[]{
						info.jumpGroupType + "",
						info.jumpGroupId + ""
				}, null);
		if (cursor != null && cursor.moveToNext()) {
			while (!cursor.isAfterLast()) {
				groupInfo.groupId = cursor.getInt(cursor
						.getColumnIndex(GroupInfoColumns.GROUP_ID));
				groupInfo.groupClass = cursor.getInt(cursor
						.getColumnIndex(GroupInfoColumns.GROUP_CLASS));
				groupInfo.groupType = cursor.getInt(cursor
						.getColumnIndex(GroupInfoColumns.GROUP_TYPE));
				groupInfo.groupName = cursor.getString(cursor
						.getColumnIndex(GroupInfoColumns.GROUP_NAME));
				groupInfo.recommWrod = cursor.getString(cursor
						.getColumnIndex(GroupInfoColumns.RECOMM_WORD));
				groupInfo.groupDesc = cursor.getString(cursor
						.getColumnIndex(GroupInfoColumns.GROUP_DESC));
				cursor.moveToNext();
			}
		}
		if (null != cursor) {
			cursor.close();
		}
		return groupInfo;
	}

	protected View getBlankView() {
		View blankView = new LinearLayout(mContext);
		return blankView;
	}

	private void addSubjectTitle(View adViewLayout, final String titleStr) {

		View subjectTitle;
		subjectTitle = (View) adViewLayout.getTag(R.id.tag_subjectItem);
		if (subjectTitle == null) {
			subjectTitle = adViewLayout.findViewById(R.id.group_head_view);
			adViewLayout.setTag(R.id.tag_subjectItem, subjectTitle);
		}

		TextView title = (TextView) subjectTitle
				.findViewById(R.id.category_title);
		Button more = (Button) subjectTitle.findViewById(R.id.more);
		more.setVisibility(View.VISIBLE);
		title.setText(titleStr);

		more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpToAppListActivity(appPosType);
			}
		});
	}

	private void jumpToAppListActivity(int appPosType) {
		boolean isRecom;
		isRecom = (appPosType == ReportConstants.STATIS_TYPE.RECOM) ? true : false;
		Intent intent = new Intent(mContext, GroupAppListActivity.class);
		int ints[] = DatabaseUtils.getGroupIdByDB(
				isRecom ? GROUP_TYPE.ALL_APP_AND_GAME_TYPE
						: GROUP_TYPE.ALL_ONLY_GAMES_TYPE, ORDER_BY.TIME);
		LogUtils.d("GROUP_ID = " + ints[0] + "GROUP_CLASS = " + ints[1]
				+ "GROUP_TYPE = " + ints[2] + "ORDERBY = " + ints[3]);
		intent.putExtra(KEY.GROUP_ID, ints[0]);
		intent.putExtra(KEY.GROUP_CLASS, ints[1]);
		intent.putExtra(KEY.GROUP_TYPE, ints[2]);
		intent.putExtra(KEY.ORDERBY, ints[3]);
		intent.putExtra(KEY.CATEGORY_NAME, mContext
				.getString(isRecom ? R.string.newest_app_label
						: R.string.newest_game_label));
		intent.putExtra(KEY.MAIN_TYPE, isRecom ? MAIN_TYPE.APP_CLASS
				: MAIN_TYPE.GAME_CLASS);
		// 统计位置
		intent.putExtra(StatisticManager.APP_POS_TYPE,
				isRecom ? ReportConstants.STATIS_TYPE.RECOM_NEWEST : ReportConstants.STATIS_TYPE.GAME_NEWEST);
		mContext.startActivity(intent);
	}

	//    // 垂直Gllery容器:包含 mAdvLineCounts 行，水平 LinearLayout 容器，每行个数平分
	//    private LinearLayout getInnerHorizontalGlleryContainer(
	//            LinearLayout glleryVerticalContainer, int lineCount, int lineSize) {
	//        //这个判断 是有问题的，  lineCount >= lineSize  return
	//        if (lineSize < 0 || lineCount < 0 || lineCount >= lineSize) {
	//            return null;
	//        }
	//        LinearLayout glleryInnerHorizontalContainer;
	//
	////        glleryInnerHorizontalContainer = (LinearLayout) glleryVerticalContainer
	////                .getTag(R.id.tag_adGlleryHorizontalContainer + lineCount);
	//
	////        if (glleryInnerHorizontalContainer == null) {
	//
	////            glleryVerticalContainer.setTag(R.id.tag_adGlleryHorizontalContainer
	////                    + lineCount, glleryInnerHorizontalContainer);
	//
	////        } else {
	////            glleryInnerHorizontalContainer.removeAllViews();
	////        }
	//        return glleryInnerHorizontalContainer;
	//    }

	// 垂直Gllery容器:包含 mAdvLineCounts 行，水平 LinearLayout 容器，每行个数平分
	private LinearLayout getInnerHorizontalGlleryContainer() {

		LinearLayout glleryInnerHorizontalContainer;
		glleryInnerHorizontalContainer = new LinearLayout(mContext);
		glleryInnerHorizontalContainer.setOrientation(LinearLayout.HORIZONTAL);
		glleryInnerHorizontalContainer.setGravity(Gravity.CENTER_VERTICAL);

		return glleryInnerHorizontalContainer;
	}

	/**
	 * 这个方法处理内嵌广告位，但是重用是肯定有问题的
	 *
	 * @param glleryContainer
	 * @param lineData
	 */
	private void glleryViewbindData(LinearLayout glleryContainer, LineData lineData) {
		int index = 0;
		int end = MAX_ADV_ONE_LINE;
		int size = lineData.size();
		int startPosition = lineData.getStartPosition();
		// 得到水平布局
		LinearLayout glleryInnerHorizontalContainer = null;
		int iLineCount = 0; // 行计数
		int iLineInnerCount = 0; // 行内计数
		//这里面需要多个行的水平 layout
		ArrayList<LinearLayout> viewHolderList = (ArrayList<LinearLayout>) glleryContainer
				.getTag(R.id.tag_adGlleryHorizontalContainer);
		if (viewHolderList == null) {
			viewHolderList = new ArrayList<LinearLayout>();
			glleryContainer.setTag(R.id.tag_adGlleryHorizontalContainer,
					viewHolderList);
		}
		ArrayList<View> glleryViewHolder = (ArrayList<View>) glleryContainer
				.getTag(R.id.tag_adGlleryViewHolder);
		if (glleryViewHolder == null) {
			glleryViewHolder = new ArrayList<View>();
			glleryContainer.setTag(R.id.tag_adGlleryViewHolder,
					glleryViewHolder);
		}
		Logger.e(TAG, "glleryViewholder " + glleryViewHolder, "oddshou");
		for (index = 0; index < end && startPosition + index < size
				&& iLineCount < mAdvLineCounts; index++) {
			if (iLineInnerCount == 0) {
				//表示换行 获取或创建一个水平行的容器
				if (viewHolderList.size() > iLineCount) {
					glleryInnerHorizontalContainer = viewHolderList.get(iLineCount);
					glleryInnerHorizontalContainer.removeAllViews();
				} else {
					glleryInnerHorizontalContainer = new LinearLayout(mContext);
					glleryInnerHorizontalContainer
							.setOrientation(LinearLayout.HORIZONTAL);
					glleryInnerHorizontalContainer
							.setGravity(Gravity.CENTER_HORIZONTAL);
					viewHolderList.add(glleryInnerHorizontalContainer);
					//                    glleryInnerHorizontalContainer.setBackgroundColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
				}
				// 加入行水平空白
				if (iLineCount > 0) {
					int iPaddingHeight = (int) mRes
							.getDimension(R.dimen.recommended_adv_subject_entry_padding);
					TextView tv = new TextView(mContext);
					tv.setHeight(iPaddingHeight);
					glleryContainer.addView(tv);
				}
				// 加入水平布局
				glleryContainer.addView(glleryInnerHorizontalContainer);
			}
			View itemView = null;
			if (glleryViewHolder.size() > index) {
				itemView = glleryViewHolder.get(index);
				//                itemView = getAdvView(lineData.getData(index), itemView, index);
				//oddshou 修改
				//                itemView = getAdvView(lineData.getData(startPosition + index), itemView, iLineInnerCount);
			} else {
				itemView = mInflater.inflate(R.layout.recommend_list_ad, null);
				glleryViewHolder.add(itemView);

			}
			//绑定数据
			itemView = getAdvView(
					lineData.getData(startPosition + index),
					itemView, iLineCount * 10 +iLineInnerCount);

			// 加入填充布局
			if (iLineInnerCount > 0) {
				TextView tv = new TextView(mContext);
				int iPaddingWith = (int) mRes
						.getDimension(R.dimen.recommended_adv_subject_entry_padding);
				tv.setWidth(iPaddingWith);
				glleryInnerHorizontalContainer.addView(tv, llp_ad);
			}
			glleryInnerHorizontalContainer.addView(itemView);

            /* 前 iModCount 行是 iLineSize + 1 个 数据， 剩余行 每行iLineSize个 */
			iLineInnerCount++;
			if (iLineInnerCount >= mInlineAdvCounts) {
				iLineCount++;
				iLineInnerCount = 0;
			}
		}
	}

	private ArrayList<View> getGlleryViewHolder(LinearLayout glleryContainer) {
		ArrayList<View> glleryViewHolder = (ArrayList<View>) glleryContainer
				.getTag(R.id.tag_adGlleryViewHolder);
		if (glleryViewHolder == null) {
			glleryViewHolder = new ArrayList<View>();
			glleryContainer.setTag(R.id.tag_adGlleryViewHolder,
					glleryViewHolder);
		}
		return glleryViewHolder;
	}

	private void loadAdData(LinearLayout lineContainer, LineData lineData,
							final String titleStr) {
		//包含光广告行的头和 广告内容 容器
		View adViewLayout;
		adViewLayout = (View) lineContainer.getTag(R.id.tag_adViewLayout);
		if (adViewLayout == null) {
			//平板广告可以滑动，手机不可用滑动
			if (App.PHONE == App.getDevicesType()) {
				adViewLayout = mInflater.inflate(
						R.layout.mix_grid_ad_layout_phone, null);
			} else {
				adViewLayout = mInflater.inflate(R.layout.mix_grid_ad_layout,
						null);
			}
			lineContainer.setTag(R.id.tag_adViewLayout, adViewLayout);
		}
		// getDimension返回的是绝对尺寸，而不是相对尺寸（dp\sp等)
		int margins = (int) mRes.getDimension(R.dimen.list_view_item_spacing);

		lineContainer.setPadding(margins, 0, margins, 0);

		lineContainer.addView(adViewLayout);

		addSubjectTitle(adViewLayout, titleStr);
		// 垂直布局
		LinearLayout glleryContainer = getGlleryContainer(adViewLayout);
		glleryViewbindData(glleryContainer, lineData);
	}


	// 垂直Gllery容器:包含 mAdvLineCounts 行，水平 LinearLayout 容器，每行个数平分
	private LinearLayout getGlleryContainer(View adViewLayout) {
		LinearLayout glleryContainer;
		glleryContainer = (LinearLayout) adViewLayout
				.getTag(R.id.tag_adGlleryContainer);
		if (glleryContainer == null) {
			glleryContainer = (LinearLayout) adViewLayout
					.findViewById(R.id.gallery_container);
			adViewLayout.setTag(R.id.tag_adGlleryContainer, glleryContainer);

			// getDimension返回的是绝对尺寸，而不是相对尺寸（dp\sp等)
			int margin = (int) mRes
					.getDimension(R.dimen.recommended_adv_subject_entry_padding);
			int marginLR = 0;// (int)
			// mRes.getDimension(R.dimen.list_view_item_spacing);
			//这个设置padding好像没什么用
			glleryContainer.setPadding(marginLR, margin, marginLR, margin);
			glleryContainer.setGravity(Gravity.CENTER_HORIZONTAL);
		} else {
			glleryContainer.removeAllViews();
		}
		return glleryContainer;
	}

	/**
	 * 加载 非广告位 行的数据
	 *
	 * @param lineContainer
	 * @param lineData
	 */
	private void loadAppData(LinearLayout lineContainer, LineData lineData) {
		int index = 0;
		int end = mColumnCount;
		int allCount = lineData.size();
		// 获取或者初始化重用的itemview和divider的view
		//一行显示多少数据，就是这个 ArrayList的 size， 这似乎是没必要的，因为一行内的每个项目都相同
		ArrayList<View> appViewHolder = (ArrayList<View>) lineContainer
				.getTag(R.id.tag_viewHolder);
		if (appViewHolder == null) {
			appViewHolder = new ArrayList<View>();
			lineContainer.setTag(R.id.tag_viewHolder, appViewHolder);
		}

		// getDimension返回的是绝对尺寸px，而不是相对尺寸（dp\sp等)
		int margins = (int) mRes.getDimension(R.dimen.list_view_item_spacing);

		lineContainer.setPadding(margins, 0, margins, 0);

		for (index = 0; index < end
				&& (lineData.getStartPosition() + index) < allCount; index++) {
			View itemView = null;
			int positionInTotal = lineData.getStartPosition() + index;
			if (appViewHolder.size() > index) {
				itemView = appViewHolder.get(index);
				itemView = getAppView(positionInTotal,
						lineData.getData(positionInTotal), itemView);
			} else {
				itemView = getAppView(positionInTotal,
						lineData.getData(positionInTotal), itemView);
				appViewHolder.add(itemView);
			}

			if (itemView != null) {
                /*
                 * int w = View.MeasureSpec.makeMeasureSpec(0,
                 * View.MeasureSpec.UNSPECIFIED);
                 * 
                 * int h = View.MeasureSpec.makeMeasureSpec(0,
                 * View.MeasureSpec.UNSPECIFIED);
                 * 
                 * itemView.measure(w, w); // int height =
                 * itemView.getMeasuredHeight(); int width =
                 * itemView.getMeasuredWidth();
                 * 
                 * 不需要测量，固定宽度
                 */

				int screenWidth = Tools.getDisplayWidth(mContext);

				int ParentWidth = screenWidth - 2 * margins;
				// listItem间距
				int iPaddingWidth = 0;
				if (mColumnCount > 1) {
					iPaddingWidth = (int) mRes
							.getDimension(R.dimen.list_view_item_spacing);
				} else {
					iPaddingWidth = 0;
				}

				int MaginW = 0;
				// item宽度
				int iItemWidth = (int) ((ParentWidth - MaginW * mColumnCount - iPaddingWidth
						* (mColumnCount - 1))
						* 1.0f / mColumnCount + 0.5f);
				// 固定宽布局
				LinearLayout.LayoutParams llp_appFixedWidth = new LayoutParams(
						iItemWidth, LayoutParams.WRAP_CONTENT);

				margins = (int) mRes
						.getDimension(R.dimen.recommended_adv_subject_entry_padding);
				int marginBottom = (int) mRes
						.getDimension(R.dimen.recommended_adv_subject_entry_padding);
				llp_appFixedWidth
						.setMargins(0, 0/* margins */, 0, 0/* marginBottom */);// 只填充了左margin

				lineContainer.addView(itemView, llp_appFixedWidth);

				// 加入填充布局
				if (index >= 0 && index < end - 1) {
					TextView tv = new TextView(mContext);
					tv.setWidth(iPaddingWidth);
					// tv.setBackgroundResource(R.color.cs_btn_red_normal);
					lineContainer.addView(tv, llp_padding);
				}
			}
		}

		// 当一行未填满，填充空白view
		if (index < end) {
			for (int blankNum = end - index - 1; blankNum >= 0; blankNum--) {
				View bk = getBlankView();
				lineContainer.addView(bk, llp_app);
			}
		}
	}

	/**
	 * 一行的数据类型
	 */
	class LineData {
		/**
		 * 包含有当前行的数据的 mAppInfoSon
		 */
		public AppInfoSon mAppInfoSon;
		/**
		 * 当前行的数据 从 mAppInfoSon 开始的位置
		 */
		public int position;

		public ArrayList<GroupElemInfo> getAllData() {
			if (mAppInfoSon == null)
				return null;
			else
				return mAppInfoSon.mAppList;
		}

		public int getStartPosition() {
			return position;
		}

		/**
		 * 使用这个方法需要注意，size并不表示当前行有多少数据，而是当前AppInfoSon 有多少条数据
		 *
		 * @return
		 */
		public int size() {
			return mAppInfoSon.mItemSize;
		}

		public GroupElemInfo getData(int index) {
			if (mAppInfoSon.mAppList.size() <= index)
				return null;
			return getAllData().get(index);
		}

		public int getType() {
			return mAppInfoSon.mType;
		}
	}

	/**
	 * 将原数据分段，相连的同种类型分成一个 {@link AppInfoSon}
	 *
	 * @author oddshou
	 */
	class AppInfoSon {
		public int mType = HOME_PAGE_POSITION.SHOW_TYPE_ADV;
		/**
		 * 对应于 mAppList.size()
		 */
		public int mItemSize = 0;
		/**
		 * 需要多少行 listView的 装载这些数据，不一定能全部填满
		 */
		public int mLineSize = 0;
		public ArrayList<GroupElemInfo> mAppList = new ArrayList<GroupElemInfo>();

		public void reset() {
			mType = HOME_PAGE_POSITION.SHOW_TYPE_ADV;
			mItemSize = 0;
			mLineSize = 0;
			mAppList.clear();
		}

		public void updateLineSize() {
			switch (mType) {
				case HOME_PAGE_POSITION.SHOW_TYPE_ADV:
					//如果是广告位只需要一行listview  item 可以加载完
					mLineSize = (int) Math.ceil(mItemSize * 1.0 / MAX_ADV_ONE_LINE);
					break;

				case HOME_PAGE_POSITION.SHOW_TYPE_APP:
					mLineSize = (int) Math.ceil(mItemSize * 1.0 / mColumnCount);
					break;

				default:
					break;
			}
		}

		public int getColumn() {
			switch (mType) {
				case HOME_PAGE_POSITION.SHOW_TYPE_ADV:
					return MAX_ADV_ONE_LINE;

				case HOME_PAGE_POSITION.SHOW_TYPE_APP:
					return mColumnCount;

				default:
					return 0;
			}
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "AppInfoSon [mItemSize = " + mItemSize + " mLineSize = " + mLineSize
					+ " -----------------------]";
		}

	}

	public void AppInfoDivision(ArrayList<GroupElemInfo> appList) {
		int size = appList.size();
		int nowType;
		mAppInfoSonList.clear();
		if (size == 0)
			return;
		AppInfoSon appInfoSon = new AppInfoSon();
		nowType = appList.get(0).showType;
		appInfoSon.mType = nowType;
		for (int count = 0; count < size; count++) {
			if (appList.get(count).showType == nowType) {
				appInfoSon.mItemSize++;
				appInfoSon.mAppList.add(appList.get(count));
			} else {
				mAppInfoSonList.add(appInfoSon);
				nowType = appList.get(count).showType;
				appInfoSon = new AppInfoSon();
				appInfoSon.reset();
				appInfoSon.mType = nowType;
				appInfoSon.mItemSize++;
				appInfoSon.mAppList.add(appList.get(count));
			}
		}
		mAppInfoSonList.add(appInfoSon);
		updateAppInfoSonLineSize();
		Logger.d("mAppInfoSonList", mAppInfoSonList.toString(), "oddshou");
	}

	public void updateAppInfoSonLineSize() {
		int listSize = mAppInfoSonList.size();
		if (listSize <= 0)
			return;
		for (int count = 0; count < listSize; count++) {
			mAppInfoSonList.get(count).updateLineSize();
		}
	}

	/**
	 * 获取 listview 第 line行 对应的数据
	 *
	 * @param line
	 * @return
	 */
	public LineData getLineData(int line) {
		LineData lineData = new LineData();
		int lineCount = 0;
		int appInfoSonCount = 0;
		int listViewLine = line + 1;// 加1因为行数line从0开始
		int size = mAppInfoSonList.size();
		if (size <= 0)
			return null;
		for (appInfoSonCount = 0; appInfoSonCount < size; appInfoSonCount++) {
			lineCount += mAppInfoSonList.get(appInfoSonCount).mLineSize;// 行数
			// (第几行)
			if (lineCount >= listViewLine)
				break;
		}
		if (appInfoSonCount >= size)
			return null;

		AppInfoSon appInfoSon = mAppInfoSonList.get(appInfoSonCount);
		lineData.mAppInfoSon = appInfoSon;
		lineData.position = (appInfoSon.mLineSize - (lineCount - listViewLine) - 1)
				* appInfoSon.getColumn();// 当前显示到多少行
		return lineData;
	}
}
