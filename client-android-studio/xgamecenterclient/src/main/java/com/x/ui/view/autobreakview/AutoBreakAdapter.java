package com.x.ui.view.autobreakview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.KeywordBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.ResourceUtil;

/**
 * 
 * @ClassName: AutoBreakAdapter
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-8-4 上午11:01:40
 * 
 */
public class AutoBreakAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private Animation scaleAnimation;
	private List<KeywordBean> mList = new ArrayList<KeywordBean>();

	public AutoBreakAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return getList().size();
	}

	@Override
	public Object getItem(int position) {
		return getList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class ViewHolder {
		public TextView tv;
		public ImageView iv1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();

			if (position == 0) {
				convertView = inflater.inflate(R.layout.auto_break_refresh_item, null);
				// set skin theme
				SkinConfigManager.getInstance().setViewBackground(context, convertView,
						SkinConstan.SEARCH_REFRESH_BTN_BG);
			} else {
				convertView = inflater.inflate(R.layout.auto_break_item, null);
			}

			vh.tv = (TextView) convertView.findViewById(R.id.res_name);
			vh.iv1 = (ImageView) convertView.findViewById(R.id.res_logo);

			convertView.setTag(vh);

		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		KeywordBean keywordBean = mList.get(position);

		vh.tv.setText(keywordBean.getKeyword());

		if (position == 0) {
			// ToDo
		} else if (keywordBean.getResLogo() != null && keywordBean.getActionRc() == Constan.Rc.SEARCH_APP_DETAIL) {
			if (position == 1) {
				vh.tv.setMaxEms(ResourceUtil.getInteger(context, R.integer.item1_with_icon_max_ems));
			} else {
				vh.tv.setMaxEms(ResourceUtil.getInteger(context, R.integer.item2_with_icon_max_ems));
			}
			vh.iv1.setVisibility(View.VISIBLE);
			NetworkImageUtils.load(context, ImageType.NETWORK, keywordBean.getResLogo(),
					R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, vh.iv1);
		} else {
			vh.iv1.setVisibility(View.GONE);
			if (keywordBean.getKeyword().getBytes().length >= 14)
				if (position == 1) {
					vh.tv.setMaxEms(ResourceUtil.getInteger(context, R.integer.item1_with_text_max_ems));
				} else {
					vh.tv.setMaxEms(ResourceUtil.getInteger(context, R.integer.item2_with_text_max_ems));
				}
		}

		// 缩放动画
		convertView.setAnimation(scaleAnimation);

		return convertView;
	}

	public void setList(List<KeywordBean> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	public List<KeywordBean> getList() {
		return mList;
	}

	/**
	 * 
	* @Title: setAnim 
	* @Description: 设置缩放动画效果
	* @param     
	* @return void
	 */
	public void setAnim() {
		int num = (int) ((Math.random()) * 2);
		if (num == 0) {
			scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_anim_from_center);
		} else {
			scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_anim_from_left);
		}
	}
}