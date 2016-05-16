
package com.hykj.gamecenter.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.ImageGalleryActivity;
import com.hykj.gamecenter.activity.PhoneAppInfoActivity;
import com.hykj.gamecenter.adapter.ImageGalleryAdapter;
import com.hykj.gamecenter.controller.HelpRequest;
import com.hykj.gamecenter.controller.HelpRequest.IReqAddCommentSucceed;
import com.hykj.gamecenter.controller.HelpRequest.IReqUserScoreInfoSucceed;
import com.hykj.gamecenter.logic.ApkInstalledManager;
import com.hykj.gamecenter.net.logic.ImageManager;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.protocol.Apps.UserCommentInfo;
import com.hykj.gamecenter.protocol.Apps.UserScoreInfo;
import com.hykj.gamecenter.statistic.MSG_CONSTANTS;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.ui.MyLoginProcessDialog;
import com.hykj.gamecenter.ui.ReboundHorizotalScrollView;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.DetailRecommedItem;
import com.hykj.gamecenter.ui.widget.RatingDialog;
import com.hykj.gamecenter.ui.widget.RatingDialog.ISubmit;
import com.hykj.gamecenter.utils.DateUtil;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.UITools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class AppInfoSoftWareDetailFragment extends Fragment {
	private static final String TAG = "AppInfoSoftWareDetailFragment";
	private AppInfo mAppInfo;

	private View mainView;
	private ScrollView mScrollView;

	// 应用信息
	// 版本信息
	private TextView mUpdatedDate;
	private TextView mAppDeveloper;

	// 介绍信息

	private TextView mAppUpgradeInfo; // 升级信息
	// private Button mAppUpgradeInfoShowMore; //升级信息更多
	private TextView mAppBusinessInfo; // 介绍信息


	private RelativeLayout mAppUpgradeRelativelayout;
	private FrameLayout mAppUpgradeInfoFramelayout;

	// gallery 显示
	private boolean canShowGallery = false;
	private View mLoadingContainer;
	private ReboundHorizotalScrollView mHorizontalScrollView;
	private LinearLayout mGalleyContainer;
	private final ArrayList<String> urlData = new ArrayList<String>();

	public static final int RESULT_CODE = 10;

	/**
	 * 正在登录中
	 */
	private boolean mIsLoading;

	private int SNAP_MAX_HEIGHT;
	private int SNAP_MAX_WIDTH;



	private Handler mUiHandler = new Handler() {
		public void handleMessage(Message msg) {
			Activity content = getActivity();
			switch (msg.what) {
				case HelpRequest.MSG_REQ_FAILED:
					if (content != null) {
						CSToast.showFailed(content, msg.arg1, msg.obj.toString());
					}
					dismissLoadingDialog();
					break;
				case HelpRequest.MSG_REQ_ERROR:
					if (content != null) {
						CSToast.showError(content);
					}
					dismissLoadingDialog();
					break;
				case HelpRequest.MSG_REQ_ADDCOMMENTSUCCEED:
					if (content != null) {
						CSToast.showNormal(content, getString(R.string.toast_thank_rate));
					}
					dismissLoadingDialog();
					updateUserScoreInfo(mGradeConstant, new UserCommentInfo());
					refreshGradeColumn();
					break;
				case HelpRequest.MSG_REQ_USERSCOREINFOSUCCEED:
					//更新评星信息
					UserScoreInfo userScoreInfo = (UserScoreInfo) msg.obj;
					if (userScoreInfo != null && userScoreInfo.userCommentInfo != null) {
						//当前账号已经评星过
						mUserScoreInfo = userScoreInfo;
						refreshGradeColumn();
					}
					if (userScoreInfo != null && userScoreInfo.userCommentInfo == null) {
						//当前账号没有评星过
						showRatingDialog();
					}
					break;
				default:
					break;
			}
		}

	};

	/**
	 * 升级介绍的截断字数
	 */
//	private static final int UPGRADE_OVER_SHOW = 20;
//	private static final int UPGRADE_MAX_LENGTH = 73;
	private static final int UPGRADE_OVER_SHOW = 10;
	private static final int UPGRADE_MAX_LENGTH = 30;

	//	private final WeakHashMap<String, ImageView> mURL2ImageViewMap = new WeakHashMap<String, ImageView>();
	private final HashMap<String, ImageView> mURL2ImageViewMap = new HashMap<String, ImageView>();

	private UserGradeHolder headerGradeHolder;
	private GridView mGridView;
	private HelpRequest mHelpRequest;
	private Button mAppUpgradeInfoShowMore;
	private Button mAppBusinessShowMore;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity != null) {
//			if (App.getDevicesType() == App.PHONE)
				((PhoneAppInfoActivity) activity).setmSoftWareDetailFragment(this);
//			else
//				((PadAppInfoActivity) activity).setmSoftWareDetailFragment(this);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mHelpRequest = new HelpRequest(new ControllCallback());
	}

	/**
	 * 是不是已经包含在activity中，防止调用getActivity为空
	 *
	 * @return
	 */
	private boolean isInActivity() {
		return getActivity() != null && !getActivity().isFinishing();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mainView = inflater.inflate(R.layout.app_software_detail_content, container, false);
		mScrollView = (ScrollView) mainView.findViewById(R.id.scrollView);
		mScrollView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
		mScrollView.fullScroll(ScrollView.FOCUS_UP);
		// 升级信息
		mAppUpgradeRelativelayout = (RelativeLayout) mainView
				.findViewById(R.id.app_upgrade_relativelayout);
		mAppUpgradeInfoFramelayout = (FrameLayout) mainView
				.findViewById(R.id.app_upgrade_info_framelayout);
		mAppUpgradeInfo = (TextView) mainView.findViewById(R.id.app_upgrade_info);
		mAppUpgradeInfoShowMore = (Button) mainView.findViewById(R.id.app_upgrade_info_show_more);
		mAppUpgradeInfoShowMore.setTag(false);
		mAppUpgradeInfo.setOnClickListener(readMoreClickListener);
		mAppBusinessInfo = (TextView) mainView.findViewById(R.id.app_business_info);
		mAppBusinessShowMore = (Button) mainView.findViewById(R.id.app_business_show_more);
		mAppBusinessShowMore.setTag(false);
		mAppBusinessInfo.setOnClickListener(readMoreClickListener);

		// 版本信息
		mUpdatedDate = (TextView) mainView.findViewById(R.id.app_update);
		mAppDeveloper = (TextView) mainView.findViewById(R.id.app_developer);

		mLoadingContainer = mainView.findViewById(R.id.loading_container);
		mHorizontalScrollView = (ReboundHorizotalScrollView) mainView.findViewById(R.id.scrollview);
		mHorizontalScrollView.setVisibility(View.GONE);
		mGalleyContainer = (LinearLayout) mainView.findViewById(R.id.gallery_container);

		SNAP_MAX_HEIGHT = getResources().getDimensionPixelSize(R.dimen.bitmap_snap_height);
		SNAP_MAX_WIDTH = getResources().getDimensionPixelSize(R.dimen.bitmap_snap_width);

		View headerGrade = mainView.findViewById(R.id.user_grade);
		headerGradeHolder = new UserGradeHolder(headerGrade);
		headerGradeHolder.commentButton.setOnClickListener(btnGradeListener);

		mGridView = (GridView) mainView.findViewById(R.id.gridView);

		return mainView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

//        if (mIsLoading && ApkInstalledManager.getInstance().isApkLocalInstalled(mAppInfo.packName)) {
//            
//        }
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		dismissLoadingDialog();
	}

	public void setHandler(Handler handler) {
		mUiHandler = handler;
	}

	/**
	 * 兼容 pad 版本
	 *
	 * @param info
	 */
	public void setData(AppInfo info) {
		Logger.i("AppInfoActivity", "AppInfoSoftWareDetailFragment,setData");
		mAppInfo = info;
		if (mAppInfo != null && isInActivity())
			bindData(null, null);
	}

	private UserScoreInfo mUserScoreInfo;
	private ArrayList<GroupElemInfo> mGroupElementList;
	private RecommedAdapter mGridAdapter;

	public void setData(AppInfo info, UserScoreInfo userScoreInfo, ArrayList<GroupElemInfo> list) {
		Logger.i("AppInfoActivity", "AppInfoSoftWareDetailFragment,setData");
		mAppInfo = info;
		mUserScoreInfo = userScoreInfo;
		mGroupElementList = list;
		if (mAppInfo != null && isInActivity())
			bindData(userScoreInfo, mGroupElementList);
	}

	private void bindData(UserScoreInfo userScoreInfo, ArrayList<GroupElemInfo> list) {

//        mAppVersion.setText(getString(R.string.app_software_detail_app_version_tag) + mAppInfo.verName);
//        mAppSize.setText(getString(R.string.app_software_detail_app_size_tag) + StringUtils.byteToString(mAppInfo.packSize));
		mUpdatedDate.setText(getString(R.string.app_software_detail_app_update_tag) + DateUtil.strToNeedStr(mAppInfo.publishTime));
		mAppDeveloper.setText(getString(R.string.app_software_detail_app_developer_tag) + mAppInfo.devName);

		// 升级介绍 设置,内容为“空”,或者“无”也不显示
		int appinfolength = mAppInfo.updateDesc.length();
		Logger.i("appinfolength", "appinfolength:" + appinfolength);
//        mAppUpgradeInfo.setText(mAppInfo.updateDesc.trim());
		if (appinfolength < UPGRADE_MAX_LENGTH + UPGRADE_OVER_SHOW) {
			mAppUpgradeInfo.setText(mAppInfo.updateDesc/*.trim()*/);
			mAppUpgradeInfoShowMore.setVisibility(View.GONE);
		} else {
			mAppUpgradeInfo.setText(mAppInfo.updateDesc.substring(0, UPGRADE_MAX_LENGTH)
					/*.trim()*/
					+ getString(R.string.more));
			mAppUpgradeInfoShowMore.setVisibility(View.VISIBLE);
			mAppUpgradeInfoShowMore.setTag(false);
			mAppUpgradeInfoShowMore.setOnClickListener(readMoreClickListener);
		}

		mAppUpgradeRelativelayout.setVisibility(appinfolength <= 0 ? View.GONE : View.VISIBLE);
		mAppUpgradeInfoFramelayout.setVisibility(appinfolength <= 0 ? View.GONE : View.VISIBLE);
		if (mAppInfo.updateDesc.equals("无") || mAppInfo.updateDesc.equals("空")) {
			mAppUpgradeRelativelayout.setVisibility(View.GONE);
			mAppUpgradeInfoFramelayout.setVisibility(View.GONE);
		}

		// 应用介绍 设置，设置默认收缩，点击摊开
		String mAppInfoma = mAppInfo.appDesc/*.trim()*/;
		if (!TextUtils.isEmpty(mAppInfoma)) {
			mAppBusinessInfo.setVisibility(View.VISIBLE);
			mAppBusinessInfo.setText(mAppInfo.appDesc/*.trim()*/);
		}
		int appDescLength = mAppInfo.appDesc.length();
		if (appDescLength < UPGRADE_MAX_LENGTH + UPGRADE_OVER_SHOW) {
			mAppBusinessInfo.setText(mAppInfo.appDesc/*.trim()*/);
			mAppBusinessShowMore.setVisibility(View.GONE);
		} else {
			mAppBusinessInfo.setText(mAppInfo.appDesc.substring(0, UPGRADE_MAX_LENGTH)
					/*.trim()*/
					+ getString(R.string.more));
			mAppBusinessShowMore.setVisibility(View.VISIBLE);
			mAppBusinessShowMore.setTag(false);
			mAppBusinessShowMore.setOnClickListener(readMoreClickListener);
		}

		if (mAppInfo.appPicUrl.length > 0) {
			urlData.clear();
			for (int i = 0; i < mAppInfo.appPicUrl.length; i++) {
				fillAppSnapshot(mAppInfo.appPicUrl[i]);
			}
		}

		if (mUserScoreInfo != null) {
			refreshGradeColumn();

		}

		//Logger.i(TAG, "bindData data list size " + list.size(), "oddshou");
		mGridAdapter = new RecommedAdapter(list, getActivity());
		mGridView.setAdapter(mGridAdapter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.i("AppInfoSoftWareDetailFragment", "onDestroy");

		for (String s : urlData) {
			ImageManager.getInstance().clearImage(s);
			ImageView v = mURL2ImageViewMap.get(s);
			v.setImageBitmap(null);
		}
		System.gc();
	}

	private void fillAppSnapshot(String url) {
		urlData.add(url);
		ImageView iv = new ImageView(App.getAppContext());
		iv.setOnClickListener(imgOnClickListener);
		iv.setScaleType(ScaleType.FIT_CENTER);
		mURL2ImageViewMap.put(url, iv);
		Bitmap bitmap = ImageManager.getInstance().getImage(url, imageHandler, false);
		if (bitmap != null) {
//            bitmap = ImageUtils.getGalleryBitmap(bitmap, SNAP_MAX_HEIGHT);
//            if (null != bitmap) {
			LinearLayout.LayoutParams lp = null;
			if (App.getDevicesType() == App.PHONE){
				if(bitmap.getWidth() < bitmap.getHeight() ){
					lp = new LinearLayout.LayoutParams(bitmap.getWidth() * SNAP_MAX_HEIGHT / bitmap.getHeight(),
							SNAP_MAX_HEIGHT);
				}else {
					int padding = (int) App.getAppContext().getResources()
							.getDimension(R.dimen.csl_cs_padding_size);
					int totalWid = UITools.getWindowsWidthPixel() - (padding * 2);
					Logger.e(TAG, "bitmap.getWidth()  = = " + bitmap.getWidth() , "tomqian");
					Logger.e(TAG, "bitmap.getHeight()  = = " + bitmap.getHeight() , "tomqian");
					Logger.e(TAG, "totalWid  = = " + totalWid , "tomqian");
//									Logger.e(TAG, "getWindowsHeightPixel  = = " + UITools.getWindowsHeightPixel(), "tomqian");
//									Logger.e(TAG, "getWindowsWidthPixel  = = " + UITools.getWindowsWidthPixel(), "tomqian");
//									Logger.e(TAG, "getWindowsDensity  = = " + UITools.getWindowsDensity(), "tomqian");
					lp = new LinearLayout.LayoutParams(totalWid ,bitmap.getHeight() * totalWid / bitmap.getWidth());
				}
			}else {
				lp = new LinearLayout.LayoutParams(bitmap.getWidth() * SNAP_MAX_HEIGHT / bitmap.getHeight(),
						SNAP_MAX_HEIGHT);
			}
			lp.rightMargin = (int) App.getAppContext().getResources()
					.getDimension(R.dimen.csl_cs_padding_half_size);
			iv.setLayoutParams(lp);
			iv.setImageBitmap(bitmap);
			mGalleyContainer.addView(iv);
//            }

//            else {  //这段代码加的很奇怪
//                mHorizontalScrollView.setVisibility(View.GONE);
//                mLoadingContainer.setVisibility(View.VISIBLE);
//            }
			if (!canShowGallery) {
				canShowGallery = true;
				mLoadingContainer.setVisibility(View.GONE);
				mHorizontalScrollView.setVisibility(View.VISIBLE);
			}
		}

		if (null != bitmap && bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	private MyLoginProcessDialog mLoadingDialog;
	private int mGradeConstant;
	/**
	 * 提交评星，回调
	 */
	private ISubmit mSubmit = new ISubmit() {

		@Override
		public void submit(int rating) {
			// TODO Auto-generated method stub
			if (mLoadingDialog == null) {
				mLoadingDialog = new MyLoginProcessDialog(getActivity(),
						"");
			}
			mLoadingDialog.show();
			mHelpRequest.reqAddComment("", mAppInfo.appId, rating, mAppInfo.verCode, mAppInfo.verName, "", 0);
			mGradeConstant = rating;
		}
	};

	private void dismissLoadingDialog() {
		if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}

	private final OnClickListener btnGradeListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!ApkInstalledManager.getInstance().isApkLocalInstalled(mAppInfo.packName)) {
				CSToast.show(App.getAppContext(),
						getString(R.string.app_user_comment_tip));
				// mUserCommentDialog = new UserCommentDialog( getActivity( ) ,
				// getString( R.string.app_user_comment_tip ) );
				// mUserCommentDialog.setLintener( new CommentDialogListener( )
				// );
				// mUserCommentDialog.show( );
				return;
			}
			//如果账号没有登录，则进入登录界面
//			if (!CSAccountManager.getInstance(getActivity()).hasCSAccount()) {
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), PersonLogin.class);
//				startActivityForResult(intent, RESULT_CODE);
//				return;
//			}

			showRatingDialog();
			return;
		}
	};

	private final OnClickListener imgOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String url = null;
			ImageView iv = (ImageView) v;
			Iterator<Entry<String, ImageView>> iter = mURL2ImageViewMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				if (val == iv) {
					url = (String) key;
					break;
				}
			}
			//有可能出现url == null 找不到crash
			if (url == null)
				return;

			Intent intent = new Intent(App.getAppContext(), ImageGalleryActivity.class);
			intent.putExtra(ImageGalleryActivity.URL, url);
			intent.putExtra(ImageGalleryActivity.URL_DATA, urlData);
			Bitmap bitmap = ImageManager.getInstance().getImage(url, null, false);
			if (bitmap != null && bitmap.getWidth() > bitmap.getHeight()) {
				intent.putExtra(ImageGalleryActivity.ORIENTATION, ImageGalleryAdapter.LANDSCAPE);
			} else {
				intent.putExtra(ImageGalleryActivity.ORIENTATION, ImageGalleryAdapter.PORTRAIT);
			}
			startActivity(intent);
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case RESULT_CODE:
				//已经登录成功，需要更新信息 是否 已经评论过
				mHelpRequest.reqUserScore(mAppInfo.appId);
				break;

			default:
				break;
		}

	}

	private final Handler imageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_CONSTANTS.MSG_IMAGE_DECODE_FINISH:
					if (!canShowGallery) {
						canShowGallery = true;
						mLoadingContainer.setVisibility(View.GONE);
						mHorizontalScrollView.setVisibility(View.VISIBLE);
					}
					Object[] obj = (Object[]) msg.obj;
					ImageView iV = mURL2ImageViewMap.get(obj[0]);
					if (iV != null) {
						Bitmap bitmap = (Bitmap) obj[1];
//                        FileUtils.saveBitmapDebug(bitmap, (String) obj[0]); //调试代码，发布时务必关闭
						if (null != bitmap) {
							LinearLayout.LayoutParams lp = null;
							if (App.getDevicesType() == App.PHONE){
								if(bitmap.getWidth() < bitmap.getHeight() ){
									lp = new LinearLayout.LayoutParams(bitmap.getWidth() * SNAP_MAX_HEIGHT / bitmap.getHeight(),
											SNAP_MAX_HEIGHT);
								}else {
									int padding = (int) App.getAppContext().getResources()
											.getDimension(R.dimen.csl_cs_padding_size);
									int totalWid = UITools.getWindowsWidthPixel() - (padding * 2);
									Logger.e(TAG, "bitmap.getWidth()  = = " + bitmap.getWidth() , "tomqian");
									Logger.e(TAG, "bitmap.getHeight()  = = " + bitmap.getHeight() , "tomqian");
									Logger.e(TAG, "totalWid  = = " + totalWid , "tomqian");
//									Logger.e(TAG, "getWindowsHeightPixel  = = " + UITools.getWindowsHeightPixel(), "tomqian");
//									Logger.e(TAG, "getWindowsWidthPixel  = = " + UITools.getWindowsWidthPixel(), "tomqian");
//									Logger.e(TAG, "getWindowsDensity  = = " + UITools.getWindowsDensity(), "tomqian");
									lp = new LinearLayout.LayoutParams(totalWid ,bitmap.getHeight() * totalWid / bitmap.getWidth());
								}
							}else {
								lp = new LinearLayout.LayoutParams(bitmap.getWidth() * SNAP_MAX_HEIGHT / bitmap.getHeight(),
										SNAP_MAX_HEIGHT);
							}

							lp.rightMargin = (int) App.getAppContext().getResources()
									.getDimension(R.dimen.csl_cs_padding_half_size);
							iV.setLayoutParams(lp);
							iV.setImageBitmap(bitmap);
							if(iV.getParent() != null){
								((LinearLayout)iV.getParent()).removeView(iV);
								mGalleyContainer.addView(iV);
							}else {
								mGalleyContainer.addView(iV);
							}
						} else {
							mHorizontalScrollView.setVisibility(View.GONE);
							mLoadingContainer.setVisibility(View.VISIBLE);
						}
						if (null != bitmap && bitmap.isRecycled()) {
							bitmap.recycle();
							bitmap = null;
						}
					}
					// iV.setImageBitmap((Bitmap) obj[1]);
					break;
				default:
					break;
			}
		}
	};

	/**
	 * 超过字数就省略，点击按钮展开
	 */
	private final OnClickListener readMoreClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			//收缩内容的textview
			TextView infoview = null;
			//需要显示的内容
			String msg = null;
			//更多按钮
			TextView showView = null;
			boolean isFull = false;
//            boolean isFull = (Boolean) view.getTag();
			if (view.getId() == R.id.app_upgrade_info_show_more || view.getId() == R.id.app_upgrade_info) {
				showView = mAppUpgradeInfoShowMore;
				infoview = mAppUpgradeInfo;
				msg = mAppInfo.updateDesc;
				isFull = (Boolean) mAppUpgradeInfoShowMore.getTag();
			} else if (view.getId() == R.id.app_business_show_more || view.getId() == R.id.app_business_info) {
				showView = mAppBusinessShowMore;
				infoview = mAppBusinessInfo;
				msg = mAppInfo.appDesc;
				isFull = (Boolean) mAppBusinessShowMore.getTag();
			}

			if (infoview == null || showView == null || TextUtils.isEmpty(msg)) {
				return;
			}
			if (msg.length() < UPGRADE_MAX_LENGTH + UPGRADE_OVER_SHOW) {
				return;
			}
			// 如果已经展开，则收起
			if (isFull) {
				infoview.setText(msg.substring(0, UPGRADE_MAX_LENGTH) + getString(R.string.more));
				// showView.setText(R.string.detail_show_more);
				showView.setTag(false);
				showView.setVisibility(View.VISIBLE);
				// 点击收起时，恢复
				// int height = infoview.getMeasuredHeight();
				// int scrollY = mScrollView.getScrollY();
				// mScrollY = scrollY - (height - mHeight);
				// 取消收缩 滚动 ############oddshou
				// imageHandler.post(scrollRunnable);
			} else { // 如果已经收起，则点击展开
				infoview.setText(msg);
				// showView.setText(R.string.detail_hide_more);
				showView.setTag(true);
				showView.setVisibility(View.GONE);
				// 记录点击更多时滑动的mScrollY ,更多的x坐标值mX，升级介绍内容的高度mHeight
				// int[] location = new int[2];
				// mAppUpgradeInfoShowMore.getLocationOnScreen(location);
				// mX = location[0];
				// mHeight = infoview.getMeasuredHeight();
				// mScrollY = mScrollView.getScrollY();
			}
		}
	};

	private int mX = 0;

	private int mHeight;
	private int mScrollY = 0;

	private Runnable scrollRunnable = new Runnable() {
		@Override
		public void run() {
			// TranslateAnimation ta = new TranslateAnimation( 0 , 0 , 0 ,
			// mScrollY );
			// ta.setDuration( 300 );
			// mScrollView.startAnimation( ta );
			mScrollView.scrollTo(mX, mScrollY);
			mX = 0;
			mScrollY = 0;

		}
	};

	public void scrollToBottom() {
		if (mScrollView != null) {
			mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
		}
	}

	private class UserGradeHolder {
		/**
		 * 平均评分
		 */
		private final TextView grade;
		/**
		 * 评星
		 */
		private final RatingBar rating;
		/**
		 * 总评分次数
		 */
		private final TextView commentSum;
		private final ProgressBar progressFive;
		private final ProgressBar progressFour;
		private final ProgressBar progressThree;
		private final ProgressBar progressTwo;
		private final ProgressBar progressOne;
		private final Button commentButton;

		private UserGradeHolder(View view) {
			grade = (TextView) view.findViewById(R.id.avg_grade);
			rating = (RatingBar) view.findViewById(R.id.app_rating);
			commentSum = (TextView) view.findViewById(R.id.app_comment_sum);
			progressFive = (ProgressBar) view
					.findViewById(R.id.user_comment_progress_five);
			progressFour = (ProgressBar) view
					.findViewById(R.id.user_comment_progress_four);
			progressThree = (ProgressBar) view
					.findViewById(R.id.user_comment_progress_three);
			progressTwo = (ProgressBar) view
					.findViewById(R.id.user_comment_progress_two);
			progressOne = (ProgressBar) view
					.findViewById(R.id.user_comment_progress_one);
			commentButton = (Button) view.findViewById(R.id.btnGrade);
		}
	}

	private void refreshGradeColumn() {

		if (!isInActivity())
			return;
		if (mUserScoreInfo != null) {
			//更新评论按钮
			if (mUserScoreInfo.userCommentInfo != null) {
				headerGradeHolder.commentButton.setEnabled(false);
				headerGradeHolder.commentButton.setText(getString(R.string.already_grade));
				headerGradeHolder.commentButton
						.setBackgroundResource(R.drawable.btn_gray_selector);
				headerGradeHolder.commentButton
						.setTextColor(getResources().getColorStateList(R.color.btn_gray_color));
			} else {
				headerGradeHolder.commentButton.setEnabled(true);
				headerGradeHolder.commentButton.setText(getString(R.string.grade_app));
				headerGradeHolder.commentButton
						.setBackgroundResource(R.drawable.btn_green_selector);
				headerGradeHolder.commentButton
						.setTextColor(getResources().getColorStateList(R.color.btn_green_color));
			}
			//即使是0次评星也需要做一次初始化
			headerGradeHolder.commentSum.setText(getString(
					R.string.app_user_comment_sum, mUserScoreInfo.scoreTimes));
			if (mUserScoreInfo.scoreTimes == 0) {
				return;
			}
			headerGradeHolder.grade.setText(((float) (Math.round(((float) mUserScoreInfo.scoreSum)
					/ mUserScoreInfo.scoreTimes * 10)) / 10) + "");
			int grade = mUserScoreInfo.scoreSum / mUserScoreInfo.scoreTimes;
			if (grade < 1) {
				grade = 1;
			}
			headerGradeHolder.rating
					.setRating(grade);
//            headerGradeHolder.commentSum.setVisibility(View.VISIBLE);

			headerGradeHolder.progressFive.setProgress(mUserScoreInfo.scoreTime5
					* 100 / mUserScoreInfo.scoreTimes);
			headerGradeHolder.progressFour.setProgress(mUserScoreInfo.scoreTime4
					* 100 / mUserScoreInfo.scoreTimes);
			headerGradeHolder.progressThree.setProgress(mUserScoreInfo
					.scoreTime3 * 100 / mUserScoreInfo.scoreTimes);
			headerGradeHolder.progressTwo.setProgress(mUserScoreInfo.scoreTime2
					* 100 / mUserScoreInfo.scoreTimes);
			headerGradeHolder.progressOne.setProgress(mUserScoreInfo.scoreTime1
					* 100 / mUserScoreInfo.scoreTimes);
		}

	}


	// 当提交评论的时候，更新用户评分栏的相关参数
	private void updateUserScoreInfo(int score, UserCommentInfo userCommentInfo) {
//        UserScoreInfo mUserScoreInfo = new UserScoreInfo();
		mUserScoreInfo.userCommentInfo = userCommentInfo;
		mUserScoreInfo.commentTimes = mUserScoreInfo.commentTimes + 1;
		mUserScoreInfo.scoreTimes = mUserScoreInfo.scoreTimes + 1;
		mUserScoreInfo.scoreSum = mUserScoreInfo.scoreSum + score;
		mUserScoreInfo.scoreAvg = mUserScoreInfo.scoreSum / mUserScoreInfo.commentTimes * 2;
		switch (score) {
			case 5:
				mUserScoreInfo.scoreTime5 = (mUserScoreInfo.scoreTime5 + 1);
				break;
			case 4:
				mUserScoreInfo.scoreTime4 = (mUserScoreInfo.scoreTime4 + 1);

				break;
			case 3:
				mUserScoreInfo.scoreTime3 = (mUserScoreInfo.scoreTime3 + 1);

				break;
			case 2:
				mUserScoreInfo.scoreTime2 = (mUserScoreInfo.scoreTime2 + 1);

				break;
			case 1:
				mUserScoreInfo.scoreTime1 = (mUserScoreInfo.scoreTime1 + 1);

				break;

			default:
				break;
		}
	}

	private RatingDialog mRatingDialog;

	private void showRatingDialog() {
		if (mRatingDialog == null) {
			mRatingDialog = new RatingDialog(getActivity(), mAppInfo.showName);
			mRatingDialog.setSubmitListen(mSubmit);
		}
		mRatingDialog.show();
	}


	private class RecommedAdapter extends BaseAdapter {

		private ArrayList<GroupElemInfo> mAdapterGroupElementList;
		private Context mContext;

		public RecommedAdapter(ArrayList<GroupElemInfo> groupList, Context context) {
			// TODO Auto-generated constructor stub
			mAdapterGroupElementList = groupList;
			mContext = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if( null != mAdapterGroupElementList){
				return mAdapterGroupElementList.size();
			}
			return 0;
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
			if (convertView == null) {
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.detail_grid_item, null, false);
				convertView = new DetailRecommedItem(mContext);
			}
			((DetailRecommedItem) convertView).bindItem(mAdapterGroupElementList.get(position), ReportConstants.STATIS_TYPE.APP_INFO_RECOMMED);

			return convertView;
		}

	}

	class ControllCallback implements IReqAddCommentSucceed, IReqUserScoreInfoSucceed {

		@Override
		public void onNetError(int errCode, String errorMsg) {
			// TODO Auto-generated method stub
			Logger.e(TAG, "onNetError " + errCode + " errorMsg " + errorMsg, "oddshou");
			mUiHandler.sendEmptyMessage(HelpRequest.MSG_REQ_ERROR);
		}

		@Override
		public void onReqFailed(int statusCode, String errorMsg) {
			// TODO Auto-generated method stub
			Logger.e(TAG, "onReqFailed " + statusCode + " errorMsg " + errorMsg, "oddshou");
			Message msg = mUiHandler.obtainMessage();
			msg.what = HelpRequest.MSG_REQ_FAILED;
			msg.arg1 = statusCode;
			msg.obj = errorMsg;
			mUiHandler.sendMessage(msg);
		}

		@Override
		public void onReqAddCommentSucceed(long commentId, String userName) {
			// TODO Auto-generated method stub
			mUiHandler.sendEmptyMessage(HelpRequest.MSG_REQ_ADDCOMMENTSUCCEED);
		}

		@Override
		public void onReqUserScoreInfoSucceed(UserScoreInfo userScoreInfo) {
			// TODO Auto-generated method stub
			Message msg = mUiHandler.obtainMessage();
			msg.what = HelpRequest.MSG_REQ_USERSCOREINFOSUCCEED;
			msg.obj = userScoreInfo;
			mUiHandler.sendMessage(msg);
		}

	}

}
