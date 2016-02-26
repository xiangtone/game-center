package com.x.ui.activity.resource;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
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
import android.widget.Toast;

import com.x.R;
import com.x.business.resource.ResourceManage;
import com.x.business.resource.ResourceManage.onRefreshMessage;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.db.resource.NativeResourceConstant;
import com.x.db.resource.NativeResourceConstant.Category;
import com.x.publics.model.AlbumBean;
import com.x.publics.model.FileBean;
import com.x.publics.utils.IntentUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.ThreadUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.PictureBackAdapter;
import com.x.ui.adapter.PictureFolderAdapter;
import com.x.ui.adapter.PictureFolderHolder;
import com.x.ui.adapter.PictureHolder;

/**
 * 
* @ClassName: PictrueFolderFragment
* @Description: 相册分类浏览

* @date 2014-4-8 上午10:42:50
*
 */
public class PictureFolderFragment extends BaseFragment implements OnClickListener, OnLongClickListener {

	private GridView gridView = null;
	private RelativeLayout bot_layout, check = null;
	private ImageButton share, del, back;
	private CheckBox all = null;
	private View emptyView, loadingView;
	private int showMode;
	private int seleteMode;
	private int mode;
	private PictureFolderAdapter pictureFolderAdaper = null;
	private ArrayList<AlbumBean> listImage = new ArrayList<AlbumBean>();
	private ArrayList<AlbumBean> checkAlbumlist = new ArrayList<AlbumBean>();
	private PictureBackAdapter pictureBackAdapter = null;
	private ArrayList<FileBean> piclist = new ArrayList<FileBean>();
	private ArrayList<FileBean> checklist = new ArrayList<FileBean>();
	private View loadingPb, loadingLogo;

	private static Dialog mDialog = null;
	private ImageView guideResourceIv;
	private boolean resourceFirst = true, shareFirst = true;
	private boolean mIsVisibleToUser = false;
	/**记录是否是第一次打开零流量图片Fragment*/
	private final static String SHAREFIRST = "shareFirst";
	/**记录是否是第一次打开资源管理图片Fragment*/
	private final static String RESFIRST = "resFirst";

	public static Fragment newInstance(Bundle bundle) {
		PictureFolderFragment fragment = new PictureFolderFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View converView = inflater.inflate(R.layout.fragment_picturefolder, null);
		mode = getArguments().getInt("MODE", NativeResourceConstant.DEF_MODE);
		loadingView = converView.findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		loadingView.setVisibility(View.VISIBLE);
		bot_layout = (RelativeLayout) converView.findViewById(R.id.re_bot_layout);
		back = (ImageButton) converView.findViewById(R.id.re_bot_back);
		back.setOnClickListener(this);
		check = (RelativeLayout) converView.findViewById(R.id.re_bot_check);
		all = (CheckBox) converView.findViewById(R.id.re_bot_all);
		check.setOnClickListener(clickListener);
		share = (ImageButton) converView.findViewById(R.id.re_bot_share);
		share.setOnClickListener(this);
		del = (ImageButton) converView.findViewById(R.id.re_bot_del);
		del.setOnClickListener(this);
		emptyView = converView.findViewById(R.id.empty_rl);
		gridView = (GridView) converView.findViewById(R.id.pic_gridview);
		gridView.setVerticalSpacing((int) getResources().getDimension(R.dimen.grid_vertical_space));
		gridView.setHorizontalSpacing((int) getResources().getDimension(R.dimen.grid_horizontal_space));
		gridView.setOnItemClickListener(itemClickListener);
		gridView.setRecyclerListener(new RecyclerListener() {

			@Override
			public void onMovedToScrapHeap(View view) {
				ImageView image = (ImageView) view.findViewById(R.id.pic_image);
				Utils.recycleImageView(image);
			}
		});
		pictureFolderAdaper = new PictureFolderAdapter(getActivity(), listImage, gridView);
		pictureFolderAdaper.setMode(NativeResourceConstant.NORMAL);
		gridView.setNumColumns(2);
		gridView.setAdapter(pictureFolderAdaper);
		gridView.setOnItemLongClickListener(longClickListener);
		showMode = NativeResourceConstant.FOLDER_MODE;
		seleteMode = NativeResourceConstant.SELECT_FOLDER;
		return converView;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		mIsVisibleToUser = isVisibleToUser;
		if (isVisibleToUser) {
			showGuideDialog();
			if (listImage != null && listImage.size() == 0)
				loadAlbumImage();
		} else {
			if (mDialog != null && mDialog.isShowing() && mActivity != null) {
				mDialog.cancel();
			}
			mDialog = null;
		}
		if (mode == NativeResourceConstant.DEF_MODE && gridView != null) {
			DataEyeManager.getInstance().module(ModuleName.MYCONTENTS_PICTURE, isVisibleToUser);
		}
	}

	/**
	 * 显示新手引导的对话框
	 */
	private void showGuideDialog() {
		if (mIsVisibleToUser == true)//为显示当前的Frament
		{
			shareFirst = SharedPrefsUtil.getValue(mActivity, SHAREFIRST, true);
			resourceFirst = SharedPrefsUtil.getValue(mActivity, RESFIRST, true);
			if (mode == NativeResourceConstant.SHARE_MODE && shareFirst == true)//从零流量分享进入
			{
				if (listImage.size() > 0 && listImage.size() < 3)//相册文件夹只有一行，引导的点击图标放置第一格
				{
					setGuideDialog(R.layout.guide_pic_folder_fragment, false);
				} else if (listImage.size() >= 3) {
					setGuideDialog(R.layout.guide_pic_folder_fragment, true);
				}
			} else if (mode == NativeResourceConstant.DEF_MODE && resourceFirst == true)//从资源管理进入
			{
				if (listImage.size() > 0 && listImage.size() < 3)//相册文件夹只有一行，引导的点击图标放置第一格
				{
					setGuideDialog(R.layout.guide_pic_folder_fragment, false);
				} else if (listImage.size() >= 3) {
					setGuideDialog(R.layout.guide_pic_folder_fragment, true);
				}
			}
		}
	}

	/**
	* @param @param layout 布局文件
	* @param @param boo false是只有一行相册，图标在左上角； true有两行以上的相册，图标居中 
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setGuideDialog(int layout, boolean boo) {
		//防止第一次进入应用之后，直接按home键退出出现的创建两个Dialog而无法
		if (mDialog != null)
			return;
		mDialog = new Dialog(mActivity, R.style.guideDialog);
		mDialog.setContentView(layout);
		//获得当前窗体
		Window window = mDialog.getWindow();
		int width = window.getWindowManager().getDefaultDisplay().getWidth();
		int height = window.getWindowManager().getDefaultDisplay().getHeight();
		//重新设置
		WindowManager.LayoutParams lp = window.getAttributes();
		window.setGravity(Gravity.LEFT | Gravity.TOP);
		if (boo == false) {
			lp.x = width / 6; // 新位置X坐标
			lp.y = height / 6; // 新位置Y坐标
		} else {
			lp.x = width / 5; // 新位置X坐标
			lp.y = (int) (height / 2.5); // 新位置Y坐标
		}
		window.setAttributes(lp);
		guideResourceIv = (ImageView) mDialog.findViewById(R.id.guide_resource_iv);
		guideResourceIv.setOnClickListener(this);
		guideResourceIv.setOnLongClickListener(this);
		mDialog.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域不关闭Dialog
		// 增加监听mialog back事件重置第一次进入状态值
		mDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
					changeFirstStatue();
					dialog.dismiss();
				}
				return false;
			}
		});

		mDialog.show();
	}

	/**
	 * 已经不是第一次进入，改变记录第一次保存在SharedPrefs值状态
	* @Title: changeFirstStatue 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void changeFirstStatue() {
		if (mode == NativeResourceConstant.SHARE_MODE && shareFirst == true)//从零流量分享进入
		{
			shareFirst = false;
			SharedPrefsUtil.putValue(mActivity, SHAREFIRST, shareFirst);
		} else if (mode == NativeResourceConstant.DEF_MODE && resourceFirst == true) {
			resourceFirst = false;
			SharedPrefsUtil.putValue(mActivity, RESFIRST, resourceFirst);
		}
	}

	public static Dialog getDialog() {
		return mDialog;
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (all.isChecked()) {
				for (AlbumBean bean : listImage) {
					bean.setIscheck(0);
				}
				checklist.removeAll(checklist);
				checkAlbumlist.removeAll(checkAlbumlist);
				all.setChecked(false);
			} else {
				checklist.removeAll(checklist);
				for (AlbumBean bean : listImage) {
					List<FileBean> list = bean.getList();
					bean.setIscheck(1);
					for (int i = 0; i < list.size(); i++) {
						checklist.add(list.get(i));
					}
				}
				checkAlbumlist.removeAll(checkAlbumlist);
				checkAlbumlist.addAll(listImage);
				all.setChecked(true);
			}
			pictureFolderAdaper.notifyDataSetChanged();
		}
	};

	private OnItemLongClickListener longClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			if (mode == NativeResourceConstant.SHARE_MODE) {
				if (showMode == NativeResourceConstant.FOLDER_MODE) {
					if (seleteMode == NativeResourceConstant.SELECT_FOLDER) {
						pictureFolderAdaper.setMode(NativeResourceConstant.EDIT);
						pictureFolderAdaper.notifyDataSetChanged();
						//						gridView.setAdapter(pictureFolderAdaper);
						seleteMode = NativeResourceConstant.SELECT_FOLDER;
					}
				}
			} else {
				pictureFolderAdaper.setMode(NativeResourceConstant.EDIT);
				setViewHeight(true);
				bot_layout.setVisibility(View.VISIBLE);
			}
			return true;
		}
	};

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Object object = parent.getAdapter().getItem(position);
			if (object instanceof AlbumBean) {
				AlbumBean albumBean = listImage.get(position);
				PictureFolderHolder folderHolder = (PictureFolderHolder) view.getTag();
				if (pictureFolderAdaper.getMode() != NativeResourceConstant.NORMAL) {
					pictureFolderAdaper.setMode(NativeResourceConstant.EDIT);
					List<FileBean> list = albumBean.getList();
					if (albumBean.getIscheck() == 0) {
						albumBean.setIscheck(1);
						folderHolder.checkBox.setChecked(true);
						checkAlbumlist.add(albumBean);
						for (int i = 0; i < list.size(); i++) {
							checklist.add(list.get(i));
						}
					} else {
						albumBean.setIscheck(0);
						folderHolder.checkBox.setChecked(false);
						checkAlbumlist.remove(albumBean);
						for (int i = 0; i < list.size(); i++) {
							checklist.remove(list.get(i));
						}
					}
					all.setChecked(getCheckAlbumStatus());
					if (mode == NativeResourceConstant.SHARE_MODE) {
						((ResourceManagementActivity) mActivity).refreshFeed(NativeResourceConstant.PIC_FRAGMENT);
					}
				} else {
					piclist = (ArrayList<FileBean>) albumBean.getList();
					if (mode == NativeResourceConstant.SHARE_MODE) {
						pictureBackAdapter = new PictureBackAdapter(getActivity(), piclist, gridView);
						pictureBackAdapter.setMode(NativeResourceConstant.EDIT);
						gridView.setNumColumns(3);
						gridView.setAdapter(pictureBackAdapter);
						all.setChecked(getCheckAlbumStatus());
						showMode = NativeResourceConstant.PIC_MODE;
						((ResourceManagementActivity) mActivity).refreshFeed(NativeResourceConstant.PIC_FRAGMENT);
					} else {
						Intent intent = new Intent(mActivity, PictureActivity.class);
						intent.putExtra("title", albumBean.getName_album());
						intent.putParcelableArrayListExtra("data", piclist);
						startActivityForResult(intent, 0);
					}
				}
			} else if (object instanceof FileBean) {
				if (position == 0) {
					pictureFolderAdaper = new PictureFolderAdapter(getActivity(), listImage, gridView);
					pictureFolderAdaper.setMode(NativeResourceConstant.NORMAL);
					gridView.setNumColumns(2);
					gridView.setAdapter(pictureFolderAdaper);
					all.setChecked(getCheckAlbumStatus());
					showMode = NativeResourceConstant.FOLDER_MODE;
					((ResourceManagementActivity) mActivity).refreshFeed(NativeResourceConstant.PIC_FRAGMENT);
					return;
				}
				FileBean fileBean = (FileBean) object;
				PictureHolder pictureHolder = (PictureHolder) view.getTag();
				pictureBackAdapter.setMode(NativeResourceConstant.EDIT);
				if (fileBean.getIscheck() == 0) {
					fileBean.setIscheck(1);
					pictureHolder.checkBox.setChecked(true);
					checklist.add(fileBean);
				} else {
					fileBean.setIscheck(0);
					pictureHolder.checkBox.setChecked(false);
					checklist.remove(fileBean);
				}
				if (checklist.size() > 0) {
					seleteMode = NativeResourceConstant.SELECT_PIC;
				} else {
					seleteMode = NativeResourceConstant.SELECT_FOLDER;
				}
				all.setChecked(getCheckStatus());
				if (mode == NativeResourceConstant.SHARE_MODE) {
					((ResourceManagementActivity) mActivity).refreshFeed(NativeResourceConstant.PIC_FRAGMENT);
				}
			}
		}
	};

	/**
	 * 加载相册
	 */
	public void loadAlbumImage() {
		if (loadingView != null && (listImage == null || listImage.size() == 0))
			loadingView.setVisibility(View.VISIBLE);

		ThreadUtil.start(new Runnable() {

			@Override
			public void run() {
				listImage = ResourceManage.getInstance(mActivity).queryAlbum();
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (listImage == null || listImage.size() == 0) {
							showEmptyView();
						} else {
							pictureFolderAdaper.setAllList(listImage);
							loadingView.setVisibility(View.GONE);
							showGuideDialog();
						}
					}
				});
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != -1) {
			loadAlbumImage();
		}
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

	public ArrayList<FileBean> getCheckList() {
		return checklist;
	}

	public int getCheckListSize() {
		return checklist.size();
	}

	public void setCheckAll(boolean isChecked, boolean isAll) {
		if (isAll) {//清除所有
			for (FileBean bean : checklist) {
				bean.setIscheck(0);
			}
			for (AlbumBean bean : checkAlbumlist) {
				bean.setIscheck(0);
			}
			checklist.removeAll(checklist);
			checkAlbumlist.removeAll(checkAlbumlist);
			seleteMode = NativeResourceConstant.SELECT_FOLDER;
			if (pictureFolderAdaper != null) {
				pictureFolderAdaper.setMode(NativeResourceConstant.NORMAL);
				pictureFolderAdaper.notifyDataSetChanged();
			}
			if (pictureBackAdapter != null)
				pictureBackAdapter.notifyDataSetChanged();
			((ResourceManagementActivity) mActivity).refreshFeed(NativeResourceConstant.PIC_FRAGMENT);
			return;
		}
		if (showMode == NativeResourceConstant.FOLDER_MODE) {
			if (pictureFolderAdaper.getMode() == NativeResourceConstant.EDIT) {
				for (AlbumBean bean : listImage) {
					bean.setIscheck(isChecked == true ? 1 : 0);
				}
				if (isChecked) {
					checklist.removeAll(checklist);
					for (AlbumBean bean : listImage) {
						List<FileBean> list = bean.getList();
						for (int i = 0; i < list.size(); i++) {
							checklist.add(list.get(i));
						}
					}
					checkAlbumlist.removeAll(checkAlbumlist);
					checkAlbumlist.addAll(listImage);
				} else {
					checkAlbumlist.removeAll(checkAlbumlist);
					checklist.removeAll(checklist);
				}
				pictureFolderAdaper.notifyDataSetChanged();
				((ResourceManagementActivity) mActivity).refreshFeed(NativeResourceConstant.PIC_FRAGMENT);
			} else {
				if (seleteMode == NativeResourceConstant.SELECT_FOLDER) {
					//					ToastUtil.show(mActivity, "请长按进入编辑状态", Toast.LENGTH_SHORT);
				} else {
					//.... 只能进入子目录选择
				}
			}
		} else {
			if (isChecked) {
				for (FileBean bean : piclist) {
					if (bean.getIscheck() != 1) {
						bean.setIscheck(1);
						checklist.add(bean);
					}
				}
			} else {
				for (FileBean bean : piclist) {
					if (bean.getIscheck() == 1) {
						bean.setIscheck(0);
						checklist.remove(bean);
					}
				}
			}
			pictureBackAdapter.notifyDataSetChanged();
			((ResourceManagementActivity) mActivity).refreshFeed(NativeResourceConstant.PIC_FRAGMENT);
		}
	}

	public boolean getCheckStatus() {
		if (showMode == NativeResourceConstant.FOLDER_MODE) {
			return getCheckAlbumStatus();
		} else {
			int temp = 0;
			for (FileBean bean : piclist) {
				if (bean.getIscheck() == 1) {
					temp++;
				}
			}
			if (temp == piclist.size()) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getCheckAlbumStatus() {
		if (checkAlbumlist.size() == 0 && listImage.size() == 0)
			return false;
		if (checkAlbumlist.size() == listImage.size()) {
			return true;
		}
		return false;
	}

	private void showEmptyView() {
		emptyView.setVisibility(View.VISIBLE);
		gridView.setVisibility(View.GONE);
		bot_layout.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_bot_back:
			checkAlbumlist.removeAll(checkAlbumlist);
			all.setChecked(false);
			bot_layout.setVisibility(View.GONE);
			pictureFolderAdaper.setMode(NativeResourceConstant.NORMAL);
			setViewHeight(false);
			break;
		case R.id.re_bot_share:
			if (checkAlbumlist.size() > 0) {
				share.setEnabled(false);
				ArrayList<FileBean> list = new ArrayList<FileBean>();
				for (int i = 0; i < checkAlbumlist.size(); i++) {
					list.addAll(checkAlbumlist.get(i).getList());
				}
				IntentUtil.shareFile(mActivity, list);
			} else {
				ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.share_toast), Toast.LENGTH_SHORT);
			}
			break;
		case R.id.re_bot_del:
			if (checkAlbumlist.size() > 0) {
				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						loadingView.setVisibility(View.VISIBLE);
						ResourceManage.getInstance(mActivity)
								.deleteAll(mActivity, checklist, message, Category.PICTURE);
					}
				};
				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};
				if (checkAlbumlist.size() < 2) {
					String toast = ResourceUtil.getString(mActivity, R.string.album);

					Utils.showDialog(
							mActivity,
							ResourceUtil.getString(mActivity, R.string.warm_tips),
							ResourceUtil.getString(mActivity, R.string.re_dialog_delete_prompt_sigin, ""
									+ checkAlbumlist.size(), " " + toast),
							ResourceUtil.getString(mActivity, R.string.confirm), positiveListener,
							ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);
				} else {
					String toast = ResourceUtil.getString(mActivity, R.string.albums);
					Utils.showDialog(
							mActivity,
							ResourceUtil.getString(mActivity, R.string.warm_tips),
							ResourceUtil.getString(mActivity, R.string.re_dialog_delete_prompt_all,
									"" + checkAlbumlist.size(), " " + toast),
							ResourceUtil.getString(mActivity, R.string.confirm), positiveListener,
							ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);
				}
			}
			break;
		case R.id.guide_resource_iv://第一次安装应用时候出现的新手引导点击圈圈的事件
			changeFirstStatue();
			if (mDialog != null && mDialog.isShowing() && mActivity != null) {
				mDialog.cancel();
				mDialog = null;
			}
			break;
		}
	}

	/**
	 * 更新
	 */
	private onRefreshMessage message = new onRefreshMessage() {

		@Override
		public void onMessage(boolean result) {
			loadingView.setVisibility(View.GONE);
			if (!result) {
				ToastUtil.show(mActivity, ResourceUtil.getString(mActivity, R.string.resource_del), Toast.LENGTH_SHORT);
				return;
			}
			loadAlbumImage();
			checkAlbumlist.removeAll(checkAlbumlist);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();

		// 刷新界面
		if (pictureFolderAdaper != null) {
			pictureFolderAdaper.notifyDataSetChanged();
		}

		if (share != null) {
			share.setEnabled(true);
		}
	}

	/**
	 * 新手引导中的长按事件;
	 */
	@Override
	public boolean onLongClick(View v) {
		if (mDialog == null)
			return false;
		changeFirstStatue();
		if (mDialog != null && mDialog.isShowing() && mActivity != null) {
			mDialog.cancel();
		}
		return false;
	};

	@Override
	public String getPage() {
		return StatisticConstan.Pape.PICTRUEFOLDERFRAGMENT;
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setViewBackground(mActivity, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(mActivity, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
		SkinConfigManager.getInstance().setCheckBoxBtnDrawable(mActivity, all, SkinConstan.OPTION_BTN);
	}

}
