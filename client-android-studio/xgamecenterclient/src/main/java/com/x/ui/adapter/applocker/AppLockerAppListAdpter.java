/**   
 * @Title: AppLockerAppListAdpter.java
 * @Package com.x.ui.activity.applocker
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2014-10-10 下午3:48:04
 * @version V1.0   
 */

package com.x.ui.adapter.applocker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SectionIndexer;
import android.widget.Toast;

import com.x.R;
import com.x.business.applocker.LockManager;
import com.x.publics.model.AppLockerBean;
import com.x.publics.utils.ToastUtil;
import com.x.ui.adapter.ArrayListBaseAdapter;

/**
 * @ClassName: AppLockerAppListAdpter
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-10-10 下午3:48:04
 * 
 */

public class AppLockerAppListAdpter extends ArrayListBaseAdapter<AppLockerBean> implements SectionIndexer {
	// 记录checkBox的点击情况
	public List<String> isSelected = new ArrayList<String>();
	// private int sort = -1; // app列表 应用是否重复出现 标志
	private Context context;
	private AppLockerViewHolder holder;
	int beanPosition; // 拿来装 bean的Position
	List<AppLockerBean> sameNameBeans;

	public AppLockerAppListAdpter(Activity context) {
		super(context);
		this.context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.applocker_listitem, null);
			}

			final AppLockerBean appLockerBean = mList.get(position);

			holder = new AppLockerViewHolder(convertView);

			// 根据position获取分类的首字母的Char ascii值
			int section = getSectionForPosition(position);

			// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
			if (position == getPositionForSection(section)) {
				holder.llCategoryLayout.setVisibility(View.VISIBLE); // 显示
																		// 列表app上面分类名字布局
				holder.frequentAppTv.setText(appLockerBean.getLockerSortTypeName());
				// holder.frequentAppTv.setVisibility(View.VISIBLE);
			} else {
				// 重复 隐藏
				holder.llCategoryLayout.setVisibility(View.GONE); // 隐藏
																	// 列表app上面分类名字布局
				// holder.frequentAppTv.setVisibility(View.GONE);
			}

			// convertView.setTag(appLockerBean.getOriginalUrl());// 全量包url
			try {

				holder.initData(appLockerBean, context);
				holder.setSkinTheme(context);

			} catch (Exception e) {
				System.out.println("报错appsBean为空");
				e.printStackTrace();
			}

			// holder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_down);

			// 监听App列表item点击 ，勾选CheckBox
			final CheckBox checkbox = holder.appLocker_isLock;
			final LockManager lockManager = LockManager.getInstance(context);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 取消勾选
					// 已经选择了的ID和选中的checkbox，才进入判断
					if (isSelected.contains(appLockerBean.getId() + "") || checkbox.isChecked()) {

						checkbox.setChecked(false);
						appLockerBean.setLocked(false);
						isSelected.remove(appLockerBean.getId() + "");

						if (appLockerBean.isGroupApp) {
							updateGroupApp(appLockerBean, false);
						} else {
							lockManager.unLockApp(appLockerBean.getPackageName());
							ToastUtil.show(context, context.getString(R.string.app_lock_commonlock_unlocked_toast)
									+ appLockerBean.getAppName(), Toast.LENGTH_SHORT);
							//ToastUtil.show(context, ResourceUtil.getString(context, R.string.list_mode_tips), Toast.LENGTH_SHORT);

						}

					} else {
						//选中
						checkbox.setChecked(true);
						appLockerBean.setLocked(true);
						isSelected.add(appLockerBean.getId() + "");
						if (appLockerBean.isGroupApp) {
							updateGroupApp(appLockerBean, true);
						} else {
							lockManager.lockApp(appLockerBean.getPackageName());
							ToastUtil.show(context, context.getString(R.string.app_lock_commonlock_locked_toast)
									+ appLockerBean.getAppName(), Toast.LENGTH_SHORT);
						}

					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	/**
	 * 判断是否是同一个包名,设置选中/取消
	 * */
	public boolean isSamePackageName(AppLockerBean appLockerBean) {
		// 所有应用列表
		List<AppLockerBean> beans = LockManager.getInstance(context).getAppLockerList();
		// 专门用于装相同名字的bean列表
		sameNameBeans = new ArrayList<AppLockerBean>();
		// 将此传入的appbean的包名 同 所有应用 遍历出来的包名进行比较
		for (AppLockerBean bean : beans) {
			if (appLockerBean != bean && appLockerBean.getPackageName().equals(bean.getPackageName())) {
				sameNameBeans.add(bean);
				sameNameBeans.add(appLockerBean);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: updateGroupApp
	 * @Description: 同步勾选/取消勾选 具有同包名的app
	 * @param @param appLockerBean
	 * @param @param isCheck
	 * @return void
	 */
	public void updateGroupApp(AppLockerBean appLockerBean, boolean isCheck) {
		String str = "";
		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i).getPackageName().equals(appLockerBean.getPackageName())) {
				mList.get(i).setLocked(isCheck);
				if (isCheck) {
					LockManager.getInstance(context).lockApp(mList.get(i).getPackageName());
				} else {
					LockManager.getInstance(context).unLockApp(mList.get(i).getPackageName());
				}
				str += mList.get(i).getAppName() + ",";
			}
		}
		str = str.substring(0, str.length() - 1);
		if (isCheck) {
			str = context.getString(R.string.app_lock_commonlock_locked_toast) + str; //已加锁
		} else {
			str = context.getString(R.string.app_lock_commonlock_unlocked_toast) + str; //已解锁
		}
		ToastUtil.show(context, str, Toast.LENGTH_SHORT);
		notifyDataSetChanged();
	}

	//ToastUtil.show(context, ResourceUtil.getString(context, R.string.list_mode_tips), Toast.LENGTH_SHORT);
	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			int sortType = mList.get(i).getLockerSortType();
			if (sortType == section) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * (非 Javadoc) <p>Title: getSectionForPosition</p> <p>Description: </p>
	 * 
	 * @param position
	 * 
	 * @return
	 * 
	 * @see android.widget.SectionIndexer#getSectionForPosition(int)
	 */

	@Override
	public int getSectionForPosition(int position) {
		return mList.get(position).getLockerSortType();
	}
}
