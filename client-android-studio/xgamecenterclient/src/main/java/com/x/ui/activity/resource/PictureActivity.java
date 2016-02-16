package com.x.ui.activity.resource;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView.RecyclerListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.resource.ResourceManage;
import com.x.business.resource.ResourceManage.onRefreshMessage;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.db.resource.NativeResourceConstant;
import com.x.db.resource.NativeResourceConstant.Category;
import com.x.publics.model.FileBean;
import com.x.publics.utils.IntentUtil;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.adapter.PictureAdapter;

/**
 * 
* @ClassName: PictrueActivity
* @Description: 图片小图浏览

* @date 2014-4-8 上午10:42:34
*
 */
public class PictureActivity extends BaseActivity implements OnClickListener {

	private GridView gridView = null;
	private RelativeLayout check, bot_layout = null;
	private ImageButton share, del, back;
	private CheckBox all = null;
	private View emptyView, loadingView;
	private ArrayList<FileBean> list = null;
	private PictureAdapter pictureAdaper = null;
	private ArrayList<FileBean> checkList = new ArrayList<FileBean>();
	private Activity mActivity = this;
	private int isUpdate = -1; // 退出后是否更新

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;
	private View loadingPb, loadingLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String title = getIntent().getStringExtra("title");
		setTabTitle(title);
		setContentView(R.layout.activity_picture);
		list = getIntent().getParcelableArrayListExtra("data");
		initView();
		initNavigation();
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
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mTitleTv.setText(R.string.page_camera);
		mNavigationView.setOnClickListener(this);
	}

	private void initView() {
		gridView = (GridView) findViewById(R.id.pic_gridview);
		loadingView = findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		loadingView.setVisibility(View.GONE);
		bot_layout = (RelativeLayout) findViewById(R.id.re_bot_layout);
		back = (ImageButton) findViewById(R.id.re_bot_back);
		back.setOnClickListener(this);
		check = (RelativeLayout) findViewById(R.id.re_bot_check);
		all = (CheckBox) findViewById(R.id.re_bot_all);
		check.setOnClickListener(clickListener);
		share = (ImageButton) findViewById(R.id.re_bot_share);
		share.setOnClickListener(this);
		del = (ImageButton) findViewById(R.id.re_bot_del);
		del.setOnClickListener(this);
		emptyView = findViewById(R.id.empty_rl);

		pictureAdaper = new PictureAdapter(PictureActivity.this, list, gridView);
		gridView.setAdapter(pictureAdaper);
		gridView.setOnItemClickListener(listener);
		gridView.setOnItemLongClickListener(longClickListener);
		gridView.setRecyclerListener(new RecyclerListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onMovedToScrapHeap(View view) {
				ImageView image = (ImageView) view.findViewById(R.id.pic_image);
				Utils.recycleImageView(image);
			}
		});
	}

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			FileBean pictureBean = (FileBean) parent.getAdapter().getItem(position);
			if (pictureAdaper.getMode() != NativeResourceConstant.NORMAL) {

				if (pictureBean.getIscheck() == 0) {
					pictureBean.setIscheck(1);
					checkList.add(pictureBean);
				} else {
					pictureBean.setIscheck(0);
					checkList.remove(pictureBean);
				}
				all.setChecked(getCheckStatus());
				pictureAdaper.notifyDataSetChanged();
			} else {
				Intent intent = new Intent(PictureActivity.this, PhotoActivity.class);
				intent.putParcelableArrayListExtra("data", list);
				intent.putExtra("Item_id", position);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, 0);
			}
		}
	};

	private OnItemLongClickListener longClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			pictureAdaper.setMode(NativeResourceConstant.EDIT);
			setViewHeight(true);
			bot_layout.setVisibility(View.VISIBLE);
			return false;
		}
	};

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (all.isChecked()) {
				setCheckAll(false);
			} else {
				setCheckAll(true);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mh_navigate_ll:
			onBackPressed();
			break;
		case R.id.re_bot_back:
			checkList.removeAll(checkList);
			all.setChecked(false);
			bot_layout.setVisibility(View.GONE);
			pictureAdaper.setMode(NativeResourceConstant.NORMAL);
			setViewHeight(false);
			break;
		case R.id.re_bot_share:
			if (checkList.size() > 0) {
				share.setEnabled(false);
				IntentUtil.shareFile(mActivity, checkList);
			} else {
				ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.share_toast), Toast.LENGTH_SHORT);
			}
			break;
		case R.id.re_bot_del:
			if (checkList.size() > 0) {
				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						loadingView.setVisibility(View.VISIBLE);
						ResourceManage.getInstance(mActivity)
								.deleteAll(mActivity, checkList, message, Category.PICTURE);
					}
				};
				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};

				if (checkList.size() > 1) {
					String toast = ResourceUtil.getString(mActivity, R.string.pictrues);
					Utils.showDialog(
							mActivity,
							ResourceUtil.getString(mActivity, R.string.warm_tips),
							ResourceUtil.getString(mActivity, R.string.re_dialog_delete_prompt_all,
									"" + checkList.size(), " " + toast),
							ResourceUtil.getString(mActivity, R.string.confirm), positiveListener,
							ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);
				} else {
					String toast = ResourceUtil.getString(mActivity, R.string.pictrue);
					Utils.showDialog(
							mActivity,
							ResourceUtil.getString(mActivity, R.string.warm_tips),
							ResourceUtil.getString(mActivity, R.string.re_dialog_delete_prompt_sigin,
									"" + checkList.size(), " " + toast),
							ResourceUtil.getString(mActivity, R.string.confirm), positiveListener,
							ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);
				}
			}
			break;
		}
	}

	public boolean getCheckStatus() {
		if (checkList.size() == list.size()) {
			return true;
		}
		return false;
	}

	public void setCheckAll(boolean isChecked) {
		for (FileBean bean : list) {
			bean.setIscheck(isChecked == true ? 1 : 0);
		}
		if (isChecked) {
			checkList.removeAll(checkList);
			checkList.addAll(list);
		} else {
			checkList.removeAll(checkList);
		}
		all.setChecked(getCheckStatus());
		pictureAdaper.notifyDataSetChanged();
	}

	public void setViewHeight(boolean isShow) {
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		if (isShow) {
			float height = getResources().getDimension(R.dimen.bot_tools_layout_h);
			((MarginLayoutParams) params).setMargins(0, 0, 0, (int) height);
		} else {
			((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
		}
		gridView.setLayoutParams(params);
	}

	private onRefreshMessage message = new onRefreshMessage() {

		@Override
		public void onMessage(boolean result) {
			loadingView.setVisibility(View.GONE);
			if (!result) {
				ToastUtil.show(mActivity, ResourceUtil.getString(mActivity, R.string.resource_del), Toast.LENGTH_SHORT);
				return;
			}
			for (int j = 0; j < checkList.size(); j++) {
				list.remove(checkList.get(j));
			}
			if (list.size() <= 0) {
				finish();
			} else {
				pictureAdaper.setAllList(list);
				pictureAdaper.notifyDataSetChanged();
				checkList.removeAll(checkList); // 清空
				isUpdate = 0;
			}
		}
	};

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
		setResult(isUpdate); // update
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		try {
			int count = gridView.getChildCount();
			for (int i = 0; i < count; i++) {
				ImageView iv = (ImageView) gridView.getChildAt(i).findViewById(R.id.pic_image);
				NetworkImageUtils.cancleDisplay(iv, this);
				Utils.recycleImageView(iv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		if (share != null) {
			share.setEnabled(true);
		}
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(mActivity, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(mActivity, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(mActivity, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
		SkinConfigManager.getInstance().setCheckBoxBtnDrawable(mActivity, all, SkinConstan.OPTION_BTN);
	}
}
