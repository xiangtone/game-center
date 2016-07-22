package com.hykj.gamecenter.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.App;
import com.hykj.gamecenter.data.SettingContent.SettingListItem;
import com.hykj.gamecenter.data.SettingContent.SettingStringDefine;
import com.hykj.gamecenter.data.SettingContent.SettingText;
import com.hykj.gamecenter.data.SettingContent.SettingToggle;
import com.hykj.gamecenter.fragment.SettingListFragment.SettingDetailFragment;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.UpdateUtils;

public class SettingListAdapter extends BaseAdapter {
	private final static String TAG = "SettingListAdapter";
	private final LayoutInflater mInflater;
	private final Context context;
	private Handler mHandler;
	private final Resources mRes;

	public SettingListAdapter() {
		context = App.getAppContext();
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRes = context.getResources();
	}

	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	@Override
	public int getCount() {
		int size = App.getSettingContent().ITEMS.size();
		return size;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Logger.d(TAG, "position : " + position);
		SettingListItem settingListItem = App.getSettingContent().ITEMS
				.get(position);
		if (settingListItem == null)
			return null;

		switch (settingListItem.mType) {
		case toggle:
			CheckedTextView ckTextView;
			TextView tvTitle;
			TextView tvTitleDetails;
			ImageView mItemLabel;
			// SettingItemHolder holder;
			SettingToggle toggleData = (SettingToggle) settingListItem.mData;
			// if( convertView == null )
			// {
			// ckTextView = (CheckedTextView)
			// mInflater.inflate(R.layout.fragment_setting_detail_item_toggle,
			// null);
			// ckTextView.setTag(R.id.tag_itemView, ckTextView);
			// ckTextView.setTag(R.id.tag_itemData, toggleData);

			convertView = mInflater.inflate(
					R.layout.fragment_setting_detail_item_toggle, null);
			mItemLabel = (ImageView) convertView.findViewById(R.id.item_label);
			ckTextView = (CheckedTextView) convertView
					.findViewById(R.id.checkedTextView);
			tvTitle = (TextView) convertView.findViewById(R.id.title);
			tvTitleDetails = (TextView) convertView
					.findViewById(R.id.titledetails);

			// holder = new SettingItemHolder( convertView );
			mItemLabel.setImageResource(toggleData.getImageResourceID());
			// mItemLabel.setVisibility(View.VISIBLE);
			ckTextView.setText("");
			tvTitle.setText(context.getString(toggleData.getResourceID()));

			String strDetails = (0 == toggleData.getResourceID2() ? ""
					: context.getString(toggleData.getResourceID2()));

			if ("".equals(strDetails)) {
				tvTitleDetails.setVisibility(View.GONE);
			} else {
				tvTitleDetails.setVisibility(View.VISIBLE);
				tvTitleDetails.setText(strDetails);
			}

			ckTextView.setChecked(toggleData.isToggle());
			convertView.setTag(R.id.tag_itemView, ckTextView);
			convertView.setTag(R.id.tag_itemData, toggleData);
			// }
			// else
			// {
			// holder = (SettingItemHolder)convertView.getTag( R.id.tag_itemView
			// );
			// // ckTextView = (CheckedTextView)
			// convertView.getTag(R.id.tag_itemView);
			// }

			// ckTextView.setText(context.getString(toggleData.getResourceID()));

			// http://www.cnblogs.com/transmuse/archive/2010/12/01/1893799.html
			// http://www.cnblogs.com/onlylittlegod/archive/2011/05/19/2050623.html
			// 在ListView中CheckedTextView无法改变自身的状态，而ListView中有一个方法listView.setItemChecked(long
			// position, boolean
			// value)方法来改变处于ListView某个位置的控件的选中的状态，若控件不支持选中，将不会改变什么，反之，控件将改变其状态
			// 当listView的ChoiceMod设置成了CHOICE_MODE_MULTIPLE后,用((ListView)
			// parent).setItemChecked(position, toggleData.bToggle)来设置 是否选中
			// ckTextView.setChecked(toggleData.isToggle());
			// ((ListView) parent).setItemChecked(position, toggleData.bToggle);

			// ckTextView.setOnClickListener(onCheckTextViewClickListener);
			convertView.setOnClickListener(onCheckTextViewClickListener);
			// return ckTextView;
			// bindView( holder , toggleData );
			return convertView;
		case text:
			SettingText textData = (SettingText) settingListItem.mData;
			View view = mInflater.inflate(
					R.layout.fragment_setting_detail_item_text, null);

			ImageView updateIcon = (ImageView) view
					.findViewById(R.id.update_icon);
			tvTitle = (TextView) view.findViewById(R.id.title);
			tvTitleDetails = (TextView) view.findViewById(R.id.titledetails);
			updateIcon.setVisibility(UpdateUtils.hasUpdate() ? View.VISIBLE
					: View.GONE);
			tvTitle.setText(context.getString(textData.mResId));

			strDetails = (0 == textData.mContentStr2ResrouceID ? ""
					: (textData
							.IsEqualStringID(SettingStringDefine.SSD_CHECK_UPDATE) ? String
							.format(context
									.getString(textData.mContentStr2ResrouceID),
									App.VersionName())
							: context
									.getString(textData.mContentStr2ResrouceID)));
			if ("".equals(strDetails)) {
				tvTitleDetails.setVisibility(View.GONE);
			} else {
				tvTitleDetails.setVisibility(View.VISIBLE);
				tvTitleDetails.setText(strDetails);
			}

			if (textData.IsEqualStringID(SettingStringDefine.SSD_CHECK_UPDATE)) {
				view.setOnClickListener(onUpdateClickListener);
			} else if (textData.IsEqualStringID(SettingStringDefine.SSD_FEED_BACK)) {
				view.setOnClickListener(onFeedbackClickListener);
			} else {
				view.setOnClickListener(onCleanCacheClickListener);
			}
			return view;

		case about:
			SettingText aboutData = (SettingText) settingListItem.mData;
			View aboutView = mInflater.inflate(
					R.layout.fragment_setting_detail_item_about, null);
			TextView settingAboutText = (TextView) aboutView
					.findViewById(R.id.setting_text);
			settingAboutText.setText(context.getString(aboutData.mResId));
			aboutView.setOnClickListener(onAboutClickListener);
			return aboutView;

		default:
			break;
		}
		return null;
	}

	/*
	 * private void bindView(SettingItemHolder holder, SettingToggle toggleData)
	 * { // TODO Auto-generated method stub if (holder == null) { Logger.d(TAG,
	 * " holder == null"); } if (toggleData == null) { Logger.d(TAG,
	 * " toggleData == null"); } else if (holder.itemIcon == null) {
	 * Logger.d(TAG, " holder.itemIcon == null"); } else { Logger.d( TAG,
	 * " toggleData.getImageResourceID( ) : " +
	 * toggleData.getImageResourceID()); }
	 * 
	 * holder.itemIcon.setImageResource(toggleData.getImageResourceID());
	 * holder.itemIcon.setVisibility(View.VISIBLE); holder.ckTextView
	 * .setText(context.getString(toggleData.getResourceID()));
	 * holder.ckTextView.setChecked(toggleData.isToggle());
	 * 
	 * }
	 */

	private final View.OnClickListener onCheckTextViewClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.d("SettingListAdapter", v.toString());
			// SettingItemHolder holder;
			// holder = (SettingItemHolder)v.getTag( R.id.tag_itemView );

			CheckedTextView cktTextView = (CheckedTextView) v
					.getTag(R.id.tag_itemView);
			// CheckedTextView cktTextView = holder.ckTextView;

			// CheckedTextView cktTextView = (CheckedTextView) v;

			boolean bCheck = cktTextView.isChecked();
			cktTextView.setChecked(!bCheck);

			SettingToggle toggleData = (SettingToggle) v
					.getTag(R.id.tag_itemData);
			toggleData.setToggle(!bCheck);

		}
	};

	private final View.OnClickListener onFeedbackClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mHandler != null) {
				mHandler.sendEmptyMessage(SettingDetailFragment.MSG_FEED_BACK);
			}
		}
	};

	private final View.OnClickListener onUpdateClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mHandler != null) {
				mHandler.sendEmptyMessage(SettingDetailFragment.MSG_CHECK_UPDATE_LOAD);
			}
		}
	};

	private final View.OnClickListener onCleanCacheClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mHandler != null) {
				mHandler.sendEmptyMessage(SettingDetailFragment.MSG_CLEAN_CACHE);
			}
		}
	};

	private final View.OnClickListener onAboutClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mHandler != null) {
				mHandler.sendEmptyMessage(SettingDetailFragment.MSG_SHOW_ABOUT_DIALOG);
			}
		}
	};
}
