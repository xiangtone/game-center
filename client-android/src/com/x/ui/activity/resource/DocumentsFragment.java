package com.x.ui.activity.resource;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.x.publics.model.FileBean;
import com.x.publics.utils.IntentUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ThreadUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.DocumentAdapter;

/**
 * 
* @ClassName: DocumentsFragment
* @Description: 本地文件分类

* @date 2014-4-8 上午10:41:18
*
 */
public class DocumentsFragment extends BaseFragment implements OnClickListener {

	private RelativeLayout bot_layout, check = null;
	private ImageButton share, del, back;
	private CheckBox all = null;
	private ListView listView = null;
	private DocumentAdapter documentAdapter = null;
	private View emptyView, loadingView;
	private View loadingPb, loadingLogo;
	private int mode;
	private ArrayList<FileBean> documentList = new ArrayList<FileBean>();
	private ArrayList<FileBean> listPath = new ArrayList<FileBean>();

	public static Fragment newInstance(Bundle bundle) {
		DocumentsFragment fragment = new DocumentsFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View converView = inflater.inflate(R.layout.fragment_documents, null);
		mode = getArguments().getInt("MODE", NativeResourceConstant.DEF_MODE);
		loadingView = converView.findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
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

		listView = (ListView) converView.findViewById(R.id.doc_list);
		listView.setOnItemClickListener(itemClickListener);

		documentAdapter = new DocumentAdapter(mActivity, documentList);
		if (mode == NativeResourceConstant.SHARE_MODE) {
			documentAdapter.setMode(NativeResourceConstant.EDIT);
		} else {
			listView.setOnItemLongClickListener(longClickListener);
		}
		listView.setAdapter(documentAdapter);
		return converView;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (documentList != null && documentList.size() == 0)
				initAdapter();
		}
		if (mode == NativeResourceConstant.DEF_MODE && listView != null) {
			DataEyeManager.getInstance().module(ModuleName.MYCONTENTS_DOCUMENT, isVisibleToUser);
		}
	}

	private void initAdapter() {
		loadingView.setVisibility(View.VISIBLE);
		ThreadUtil.start(new Runnable() {

			@Override
			public void run() {
				documentList = ResourceManage.getInstance(mActivity).queryDocment();
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (documentList == null || documentList.size() == 0) { // show emptyView
							showEmptyView();
						} else {
							documentAdapter.setAllList(documentList);
						}
						loadingView.setVisibility(View.GONE);
					}
				});
			}
		});
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (all.isChecked())
				setCheckAll(false);
			else
				setCheckAll(true);
		}
	};

	private OnItemLongClickListener longClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			documentAdapter.setMode(NativeResourceConstant.EDIT);
			setViewHeight(true);
			bot_layout.setVisibility(View.VISIBLE);
			return false;
		}
	};

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			FileBean bean = (FileBean) parent.getAdapter().getItem(position);
			if (documentAdapter.getMode() != NativeResourceConstant.NORMAL) {
				documentAdapter.setMode(NativeResourceConstant.EDIT);
				if (bean.getIscheck() == 0) {
					bean.setIscheck(1);
					listPath.add(bean);
				} else {
					bean.setIscheck(0);
					listPath.remove(bean);
				}
				all.setChecked(getCheckStatus());
				documentAdapter.notifyDataSetChanged();
				if (mode == NativeResourceConstant.SHARE_MODE) {
					((ResourceManagementActivity) mActivity).refreshFeed(NativeResourceConstant.DOC_FRAGMENT);
				}
			} else {
				IntentUtil.openFile(mActivity, bean.getFilePath());
			}
		}
	};

	public void setViewHeight(boolean isShow) {
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		if (isShow) {
			float height = getResources().getDimension(R.dimen.bot_tools_layout_h);
			((MarginLayoutParams) params).setMargins(0, 0, 0, (int) height);
		} else {
			((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
		}
		listView.setLayoutParams(params);
	}

	public ArrayList<FileBean> getCheckList() {
		return listPath;
	}

	public int getCheckListSize() {
		return listPath.size();
	}

	public void setCheckAll(boolean isChecked) {
		for (FileBean bean : documentList) {
			bean.setIscheck(isChecked == true ? 1 : 0);
		}
		if (isChecked) {
			listPath.removeAll(listPath);
			listPath.addAll(documentList);
		} else {
			listPath.removeAll(listPath);
		}
		all.setChecked(getCheckStatus());
		documentAdapter.notifyDataSetChanged();
		if (mode == NativeResourceConstant.SHARE_MODE) {
			((ResourceManagementActivity) mActivity).refreshFeed(NativeResourceConstant.DOC_FRAGMENT);
		}
	}

	public boolean getCheckStatus() {
		if (listPath.size() == 0 && documentList.size() == 0)
			return false;
		if (listPath.size() == documentList.size()) {
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_bot_back:
			listPath.removeAll(listPath);
			all.setChecked(false);
			bot_layout.setVisibility(View.GONE);
			documentAdapter.setMode(NativeResourceConstant.NORMAL);
			setViewHeight(false);
			break;
		case R.id.re_bot_share:
			if (listPath.size() > 0) {
				share.setEnabled(false);
				IntentUtil.shareFile(mActivity, listPath);
			} else {
				ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.share_toast), Toast.LENGTH_SHORT);
			}
			break;
		case R.id.re_bot_del:
			if (listPath.size() > 0) {
				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						loadingView.setVisibility(View.VISIBLE);
						ResourceManage.getInstance(mActivity).deleteAll(mActivity, listPath, loadMessage, Category.DOC);
					}
				};
				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};
				if (listPath.size() > 1) {
					String toast = ResourceUtil.getString(mActivity, R.string.documents);
					Utils.showDialog(
							mActivity,
							ResourceUtil.getString(mActivity, R.string.warm_tips),
							ResourceUtil.getString(mActivity, R.string.re_dialog_delete_prompt_all,
									"" + listPath.size(), " " + toast),
							ResourceUtil.getString(mActivity, R.string.confirm), positiveListener,
							ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);
				} else {
					String toast = ResourceUtil.getString(mActivity, R.string.document);
					Utils.showDialog(
							mActivity,
							ResourceUtil.getString(mActivity, R.string.warm_tips),
							ResourceUtil.getString(mActivity, R.string.re_dialog_delete_prompt_sigin,
									"" + listPath.size(), " " + toast),
							ResourceUtil.getString(mActivity, R.string.confirm), positiveListener,
							ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);
				}

			}
			break;
		}
	}

	private void showEmptyView() {
		emptyView.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
		bot_layout.setVisibility(View.GONE);
	}

	private onRefreshMessage loadMessage = new onRefreshMessage() {

		@Override
		public void onMessage(boolean result) {
			loadingView.setVisibility(View.GONE);
			if (!result) {
				ToastUtil.show(mActivity, ResourceUtil.getString(mActivity, R.string.resource_del), Toast.LENGTH_SHORT);
				return;
			}
			initAdapter();
			listPath.removeAll(listPath);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		// 刷新界面
		if (documentAdapter != null) {
			documentAdapter.notifyDataSetChanged();
		}
		if (share != null) {
			share.setEnabled(true);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

	}

	@Override
	public String getPage() {
		return StatisticConstan.Pape.DOCUMENTSFRAGMENT;
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
