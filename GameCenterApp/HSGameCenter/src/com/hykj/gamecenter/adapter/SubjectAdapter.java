
package com.hykj.gamecenter.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.GroupAppListActivity;
import com.hykj.gamecenter.controller.ProtocolListener.ITEM_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.data.TopicInfo;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.SubjectImageView;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utils.UITools;
import com.hykj.gamecenter.utilscs.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author owenli
 */
public class SubjectAdapter extends BaseAdapter {

	private static String TAG = "SearchRecommendAdapter";
	protected final Context mContext;

	private static final int GROUP_ITEM_MAX_SIZE = 2;
	private static final int HOT_GAMES_MAX_SIZE = 8;
	private static final int HOT_SEARCH_MAX_SIZE = 12;

	private final LayoutInflater mInflater;

	private final ArrayList<GroupInfo> infoList = new ArrayList<GroupInfo>();

	private int mColumnCount = 3;

	private final int mColumnWidth = 0;

	private Handler mHandler;
	protected Resources mRes = null;

	private static final LinearLayout.LayoutParams llp = new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	private static final LinearLayout.LayoutParams llp_padding = new LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	/**
	 * 空白视图，用于充填没有数据的地方
	 */
	private final ArrayList<View> mBlankView;

	public SubjectAdapter(Context context, int columnCount) {
		super();
		mContext = context;
		mRes = context.getResources();

		mBlankView = new ArrayList<View>();
		this.mColumnCount = columnCount;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		llp.weight = 1;

		// int marginsTop = (int) mRes
		// .getDimension(R.dimen.list_view_item_spacing);
		// int margin =(int)
		// mRes.getDimension(R.dimen.subject_adapter_view_item_spacing);
		// llp.setMargins(0, marginsTop, 0, margin);
		llp.setMargins(0, 0, 0, 0);
	}

	public void setColumnCount(int count) {
		this.mColumnCount = count;
	}

	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	//
	// public void addGroupNameData( List< String > data )
	// {
	// mGroupNameList.addAll( data );
	// if( mGroupNameList.size( ) > GROUP_ITEM_MAX_SIZE )
	// {
	// mGroupNameList.subList( GROUP_ITEM_MAX_SIZE , mGroupNameList.size( )
	// ).clear( );
	// }
	// }
	//
	// public void clearGroupNameData()
	// {
	// mGroupNameList.clear( );
	// }

	public void addData(ArrayList<GroupInfo> infos) {
		LogUtils.e("infos size:  " + infos.size());
		infoList.addAll(infos);
		// 现在在列表中显示 //infoList.remove( 0 ); //第一个为当前主题，用在头部，不再列表中
		// if( mColumnCount == 3 )
		// {
		// infoList.remove( 0 );
		// infoList.remove( 0 );
		// }
		// else
		// {
		// infoList.remove( 0 );
		// }

	}

	public void clearData() {
		infoList.clear();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout lineContainer = (LinearLayout) convertView;
		// LinearLayout topContent = null;

		// 首先创建带有上下分割线的的整行的Layout

		if (lineContainer == null) {
			lineContainer = new LinearLayout(mContext);
			lineContainer.setOrientation(LinearLayout.HORIZONTAL);
			lineContainer.setGravity(Gravity.CENTER_VERTICAL);
			int marginsTop = (int) mRes
					.getDimension(R.dimen.list_view_item_spacing);
			lineContainer.setPadding(0, marginsTop, 0, 0);
		} else {
			lineContainer.removeAllViews();
		}

		// 获取或者初始化重用的itemview和divider的view
		ArrayList<View> viewHolderList = (ArrayList<View>) lineContainer
				.getTag(R.id.tag_viewHolder);
		if (viewHolderList == null) {
			viewHolderList = new ArrayList<View>();
			lineContainer.setTag(R.id.tag_viewHolder, viewHolderList);
		}

		// 对每一行，生成单独的实际itemview加入上面创建的layout中
		int index = position * mColumnCount;
		int end = index + mColumnCount;
		int nFillItem = 0;
		for (; index < end && index < getAllCount(); index++) {
			int posInLine = mColumnCount - (end - index);
			View itemView = null;

			if (viewHolderList.size() > posInLine) {
				itemView = viewHolderList.get(posInLine);
				itemView = getPeaceView(index, itemView, lineContainer);
			} else {
				itemView = getPeaceView(index, itemView, lineContainer);
				viewHolderList.add(itemView);
			}

			if (itemView != null) {
				itemView.setTag(R.id.tag_blankView, Boolean.FALSE);
				itemView.setTag(R.id.tag_positionView, Integer.valueOf(index));

				// llp.setMargins(6, 6, 6, 6);

				int screenWidth = Tools.getDisplayWidth(mContext);

				// getDimension返回的是绝对尺寸，而不是相对尺寸（dp\sp等)
				int margins = (int) mRes
						.getDimension(R.dimen.subject_adapter_view_item_spacing);
				int ParentWidth = screenWidth - 2 * margins; // 当前
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

                /*
				 * int marginsTop = (int) mRes
                 * .getDimension(R.dimen.list_view_item_spacing); int
                 * marginBottom = (int) mRes .getDimension(R.dimen.
                 * subject_adapter_view_item_spacing) 0;
                 * llp_appFixedWidth.setMargins(0, marginsTop, 0, marginBottom);
                 */
				llp_appFixedWidth.setMargins(0, 0, 0, 0);
				lineContainer.addView(itemView, llp_appFixedWidth);

				// 加入填充布局
				if (index >= 0 && index < end - 1) {
					TextView tv = new TextView(mContext);
					tv.setWidth(iPaddingWidth);
					// tv.setBackgroundResource(R.color.cs_btn_red_normal);
					lineContainer.addView(tv, llp_padding);
				}
				nFillItem++;
			}
		}

		lineContainer.setTag(R.id.tag_fillItems, nFillItem);

		// 当一行未填满，填充空白view
		if (index < end) {
			for (int blankNum = end - index - 1; blankNum >= 0; blankNum--) {
				View bk = null;
				for (int j = mBlankView.size() - 1; j >= 0; j--) {

					if (mBlankView.get(j).getParent() == null) {
						bk = mBlankView.get(j);
						break;
					}
				}
				if (bk == null) {
					bk = genBlankView();
					mBlankView.add(bk);
				}
				lineContainer.addView(bk, llp);
			}
		}

		return lineContainer;
	}

	public int getAllCount() {

		return infoList.size();

	}

	private class ItemHolder {
		View frame;
		SubjectImageView icon;
		TextView title;

		public ItemHolder(View view) {
			frame = view;
			icon = (SubjectImageView) view.findViewById(R.id.topic_item);
			title = (TextView) view.findViewById(R.id.item_title);
		}
	}

	/**
	 * 获取每个数据项的视图
	 *
	 * @param positionInTotal
	 * @param convertView
	 * @param parentView
	 * @return
	 */
	public View getPeaceView(int childPositionInTotal, View convertView,
							 ViewGroup parentView) {

		ItemHolder itemHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.topic_content_item, null);

			itemHolder = new ItemHolder(convertView);

			convertView.setTag(itemHolder);

		} else {
			itemHolder = (ItemHolder) convertView.getTag();
		}

		bindItemView(itemHolder, childPositionInTotal);

		return convertView;
	}

	private void bindItemView(ItemHolder holder, int position) {
		final GroupInfo info = infoList.get(position);
		Logger.d(TAG, "info.groupPicUrl : " + info.groupPicUrl);
		ImageLoader.getInstance().displayImage(info.groupPicUrl, holder.icon,
				DisplayOptions.optionSubject);
		// if( position == 0 )
		// {
		// holder.current_icon.setVisibility( View.VISIBLE );
		// }
		// else
		// {
		// holder.current_icon.setVisibility( View.GONE );
		// }

		// holder.name.setText( info.getShowName( ) );

		holder.title.setText(info.groupName);

		holder.frame.setVisibility(View.VISIBLE);
		// TODO 进入游戏分类中的具体某一类
		holder.frame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (UITools.isFastDoubleClick())

					return;

				Intent intentAppList = new Intent(mContext,
						GroupAppListActivity.class);
				intentAppList.putExtra(KEY.BK_COLOR, R.color.subject_color);
				intentAppList.putExtra(KEY.ORDERBY, info.orderType);
				intentAppList.putExtra(KEY.GROUP_ID, info.groupId);
				intentAppList.putExtra(KEY.GROUP_CLASS, info.groupClass);
				intentAppList.putExtra(KEY.GROUP_TYPE, info.groupType);

                /*
				 * intentAppList.putExtra(KEY.CATEGORY_NAME,
                 * mContext.getString(R.string.topic_game_label));
                 */
				intentAppList.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.TOPIC);
				intentAppList
						.putExtra(KEY.ITEM_TYPE, ITEM_TYPE.UNSHOW_SNAPSHOT);
				intentAppList.putExtra(StatisticManager.APP_POS_TYPE,
						ReportConstants.STATIS_TYPE.SUBJECT);

				TopicInfo topicInfo = new TopicInfo();
				topicInfo.mAppCount = info.recommWrod;
				topicInfo.mTopic = info.groupName;
				topicInfo.mTip = info.groupDesc;
				topicInfo.mPicUrl = info.groupPicUrl;

				intentAppList.putExtra(KEY.TOPIC_INFO, topicInfo);
				intentAppList.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				mContext.startActivity(intentAppList);
			}
		});
	}

	private View getHorizontalDivider() {
		ImageView imageView = new ImageView(mContext);
		// imageView.setBackgroundResource( R.drawable.divider_horizontal );
		// imageView.setBackgroundResource( R.color.search_divider_color );
		LinearLayout.LayoutParams llp = getHorizontalDiveiderLayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		imageView.setLayoutParams(llp);
		return imageView;
	}

	private LinearLayout.LayoutParams getHorizontalDiveiderLayoutParams(
			int nWidth) {
		int hight = mContext.getResources().getDimensionPixelSize(
				R.dimen.csl_cs_padding_half_size);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
				nWidth > 0 ? nWidth
						: android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				hight);
		return llp;
	}

	private View getVerticalDivider() {
		ImageView imageView = new ImageView(mContext);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(1,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		imageView.setBackgroundResource(R.color.divider);
		// imageView.setBackgroundResource( R.drawable.divider_vertical );
		imageView.setLayoutParams(llp);
		return imageView;
	}

	private View genBlankView() {
		View blankView = new LinearLayout(mContext);
		blankView.setTag(R.id.tag_blankView, Boolean.TRUE);
		return blankView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getCount() {
		int nCount = (int) Math.ceil(getAllCount() * 1.0 / mColumnCount);

		return nCount;
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

}
