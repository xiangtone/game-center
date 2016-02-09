/**   
 * @Title: ZeroDataTransferHistoryActivity.java
 * @Package com.mas.amineappstore.ui.activity.zerodata
 * @Description: TODO 
 
 * @date 2014-3-31 下午04:42:26
 * @version V1.0   
 */

package com.x.ui.activity.zerodata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.RecyclerListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.x.R;
import com.x.business.localapp.sort.CharacterParser;
import com.x.business.localapp.sort.PinYin;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.zerodata.helper.PinyinComparator;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.business.zerodata.history.TransferHistoryManager;
import com.x.db.resource.NativeResourceDBHelper;
import com.x.db.zerodata.TransferHistory;
import com.x.publics.utils.MediaPlayerUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.adapter.ZeroDataTransferHistoryAdapter;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView.OnActionClickListener;

/**
 * @ClassName: ZeroDataTransferHistoryActivity
 * @Description: 零流量分享--收发记录
 
 * @date 2014-3-31 下午04:42:26
 * 
 */

public class ZeroDataTransferHistoryActivity extends BaseActivity implements OnClickListener {

	private ActionSlideExpandableListView mTransferHistoryGv;
	private ZeroDataTransferHistoryAdapter zdthAdapter;
	private List<TransferHistory> mList = new ArrayList<TransferHistory>();
	private boolean inited = false;
	private TextView savedDataTv, deleteAllTv;
	private RelativeLayout showDataRel, loadingRel, empty_rl;
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private SharedPreferences preferences = null;
	private Editor editor = null;
	private List<TransferHistory> tfhList = new ArrayList<TransferHistory>();
	private Context context;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View loadingPb, loadingLogo;
	private View mNavigationView, mTitleView, mTitlePendant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTabTitle(R.string.page_share_receive_history);
		setContentView(R.layout.activity_zero_data_transfer_history);
		context = this;
		preferences = getSharedPreferences(ZeroDataConstant.ZERO_DATA_RESOURCE_PREFES, MODE_PRIVATE);
		editor = preferences.edit();
		init();
		refreshData();
		clearZeroDataSharePrefs();
		initNavigation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
	}

	/**
	* @Title: initNavigation 
	* @Description: 初始化导航栏 
	* @param     
	* @return void
	 */
	private void initNavigation() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mTitleTv.setText(R.string.page_share_receive_history);
		mNavigationView.setOnClickListener(this);
	}

	private void init() {
		loadingPb = findViewById(R.id.loading_progressbar);
		loadingLogo = findViewById(R.id.loading_logo);
		showDataRel = (RelativeLayout) findViewById(R.id.zdth_showData_Rel);
		loadingRel = (RelativeLayout) findViewById(R.id.zdth_loading_Rel);
		empty_rl = (RelativeLayout) findViewById(R.id.zdth_empty_Rel);
		savedDataTv = (TextView) findViewById(R.id.zdth_savedData_Tv);
		deleteAllTv = (TextView) findViewById(R.id.zdth_deleteAll_tv);
		deleteAllTv.setOnClickListener(this);
		mTransferHistoryGv = (ActionSlideExpandableListView) findViewById(R.id.zdth_lv);
		zdthAdapter = new ZeroDataTransferHistoryAdapter(ZeroDataTransferHistoryActivity.this);
		zdthAdapter.setListView(mTransferHistoryGv);
		mTransferHistoryGv.setItemActionListener(actionClickListener, R.id.zdth_share_rel, R.id.zdth_delete_rel);
		mTransferHistoryGv.setRecyclerListener(recyclerListener);

	}

	/**
	 * 弹出项点击处理[分享、删除]
	 */
	private OnActionClickListener actionClickListener = new OnActionClickListener() {

		@Override
		public void onClick(View itemView, View clickedView, int position) {
			TransferHistory transferHistory = (TransferHistory) zdthAdapter.getList().get(position);
			switch (clickedView.getId()) {
			case R.id.zdth_share_rel:

				editor.putString(ZeroDataConstant.ZERO_DATA_RESOURCE_KEY, transferHistory.getFileSavePath());
				editor.commit();
				ZeroDataResourceHelper
						.saveFromActivity(ZeroDataTransferHistoryActivity.this,
								ZeroDataConstant.ZERO_DATA_SERVER_ACTIVITY_KEY,
								ZeroDataTransferHistoryActivity.class.getName());
				// 文件全路径
				Intent shareIntent = new Intent(ZeroDataTransferHistoryActivity.this,
						ZeroDataShareConfirmActivity.class);
				shareIntent.putExtra("history", true);
				startActivity(shareIntent);
				finish();
				break;
			case R.id.zdth_delete_rel:
				deleteHistory(transferHistory);
				break;
			}
		}
	};

	private void deleteHistory(final TransferHistory transferHistory) {
		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String filePath = transferHistory.getFileSavePath();
				TransferHistoryManager.getInstance().deleteReceiveHistoryBySavePath(filePath);
				// 删除所在文件夹的实际数据
				Utils.deleteFile(filePath);
				// 删除已下载文件，通知系统更新
				NativeResourceDBHelper.getInstance(context).notifyFileSystemChanged(filePath);
				tfhList.clear();
				if (mTransferHistoryGv != null)
					mTransferHistoryGv.collapse();
				refreshData();
			}
		};

		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips),
				ResourceUtil.getString(context, R.string.share_history_dialog_delete_one),
				ResourceUtil.getString(context, R.string.confirm), positiveListener,
				ResourceUtil.getString(context, R.string.cancel), negativeListener);

	}

	@SuppressLint("HandlerLeak")
	private Handler uiHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				long fileSize = statisticalFileSize();
				if (fileSize == 0) {
					empty_rl.setVisibility(View.VISIBLE);
					loadingRel.setVisibility(View.GONE);
					showDataRel.setVisibility(View.GONE);
				} else {
					showDataRel.setVisibility(View.VISIBLE);
					loadingRel.setVisibility(View.GONE);
				}
				colorOfDigital(fileSize);
				zdthAdapter.setList(tfhList);
				mTransferHistoryGv.setAdapter(zdthAdapter, R.id.zdth_top_rl, R.id.zdth_expand_ll, null);
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 给TextView的数字设置颜色
	 */
	private void colorOfDigital(long fileSize) {
		savedDataTv.setVisibility(View.VISIBLE);
		String size = Utils.sizeFormat(fileSize);
		String text = ResourceUtil.getString(context, R.string.share_history_file_size, size);
		int len = size.length();

		int start = text.indexOf(size);
		SpannableStringBuilder builder = new SpannableStringBuilder(text);

		int textColor = SkinConfigManager.getInstance().getStringColor(context, SkinConstan.APP_THEME_COLOR);

		ForegroundColorSpan blueSpan = new ForegroundColorSpan(textColor);
		builder.setSpan(blueSpan, start, start + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		savedDataTv.setText(builder);
	}

	private void refreshData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				sort();
			}
		}).start();
	}

	private void sort() {
		// 查询数据
		mList = TransferHistoryManager.getInstance().findAllReceiveHistory();
		resetTransferHistory(mList);
		uiHandler.sendEmptyMessage(0);
	}

	/**
	 * 按时间的排序设置
	 */
	private void resetTransferHistory(List<TransferHistory> tempList) {
		List<TransferHistory> list = new ArrayList<TransferHistory>();
		list.clear();
		Collections.reverse(tempList);
		String str = "", tempStr = "";
		for (int i = 0; i < tempList.size(); i++) {
			if (i == 0) {
				str = tempList.get(0).getFinishTime();
				tempStr = str;
				list.add(tempList.get(0));
			} else {
				str = tempStr;
				if (str.equals(tempList.get(i).getFinishTime())) {
					list.add(tempList.get(i));
				} else {
					List<TransferHistory> aList = sortSetting(list);// 按字母排序
					List<TransferHistory> bList = setTimeForTransferHistory(aList);
					tfhList.addAll(bList);// 在某一时间段里对数据重新设置
					tempStr = tempList.get(i).getFinishTime();
					list.clear();
					list.add(tempList.get(i));
				}
			}
		}
		List<TransferHistory> oList = sortSetting(list);// 按字母排序
		List<TransferHistory> pList = setTimeForTransferHistory(oList);
		tfhList.addAll(pList);// 在某一时间段里对数据重新设置
	}

	/**
	 * 字母的排序设置
	 */
	private List<TransferHistory> sortSetting(List<TransferHistory> tempList) {
		for (int i = 0; i < tempList.size(); i++) {
			try {
				// 汉字转换成拼音
				String pinyin = PinYin.getPinYin(tempList.get(i).getFileName());
				tempList.get(i).setNameSort(tempList.get(i).getFileName());
				String sortString = pinyin.substring(0, 1);
				// 正则表达式，判断首字母是否是英文字母
				if (sortString.matches("[A-Z]")) {
					tempList.get(i).setFileName(pinyin.toUpperCase());
				} else {
					tempList.get(i).setFileName("#");
				}
			} catch (Exception e) {
				e.printStackTrace();
				tempList.get(i).setFileName("#");
			}
		}
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		// 根据a-z进行排序源数据
		Collections.sort(tempList, pinyinComparator);
		return tempList;
	}

	/**
	 * 为某一段时间里的记录设置状态
	 */
	private List<TransferHistory> setTimeForTransferHistory(List<TransferHistory> list) {

		for (int j = 0; j < list.size(); j++) {
			if (j == 0) {
				list.get(j).setFinishTimeStatus(true);
			} else {
				list.get(j).setFinishTimeStatus(false);
			}
		}
		return list;
	}

	/**
	 * 图片资源回收
	 */
	private RecyclerListener recyclerListener = new RecyclerListener() {

		@Override
		public void onMovedToScrapHeap(View view) {
			// ImageView iv = (ImageView) view
			// .findViewById(R.id.zdth_file_icon_iv);
			// iv.setImageBitmap(null);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.mh_navigate_ll:
			onBackPressed();
			break;
		case R.id.zdth_deleteAll_tv:
			String title = getResources().getString(R.string.warm_tips);
			String content = getResources().getString(R.string.share_history_dialog_delete_all);
			Utils.showDialog(this, title, content, ResourceUtil.getString(this, R.string.confirm),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							deleteAll();
						};
					}, ResourceUtil.getString(this, R.string.cancel), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			break;
		default:
			break;
		}

	}

	/**
	 * 删除本地所有的实际数据及数据库记录
	 */
	private void deleteAll() {
		empty_rl.setVisibility(View.VISIBLE);
		showDataRel.setVisibility(View.GONE);
		TransferHistoryManager.getInstance().deleteAllReceiveHistory();
		for (int i = 0; i < mList.size(); i++) {
			String filePath = mList.get(i).getFileSavePath();
			Utils.deleteFile(filePath);
		}

	}

	/**
	 * 统计已接收文件总大小
	 */
	private long statisticalFileSize() {

		long fileSize = 0;
		if (tfhList.size() == 0 || tfhList == null) {
			return fileSize;
		}
		for (int i = 0; i < tfhList.size(); i++) {
			fileSize += tfhList.get(i).getFileSize();
		}
		return fileSize;
	}

	/**
	 * 清除上一次零流量分享的数据
	 */
	public void clearZeroDataSharePrefs() {
		ZeroDataResourceHelper.getInstance(this).prefers.edit().putString(ZeroDataConstant.ZERO_DATA_RESOURCE_KEY, "")
				.clear();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MediaPlayerUtil.getInstance(context).release();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			onBackPressed();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(context, ZeroDataShareActivity.class));
		finish();
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, deleteAllTv, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(context, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}
}
