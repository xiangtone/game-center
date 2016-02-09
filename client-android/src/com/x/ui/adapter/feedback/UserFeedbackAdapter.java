/**   
* @Title: UserAdapter.java
* @Package com.mas.amineappstore.ui.adapter.feedback
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-7-22 下午5:56:32
* @version V1.0   
*/

package com.x.ui.adapter.feedback;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.x.R;
import com.x.publics.http.model.UserFeedbackBean;
import com.x.ui.adapter.ArrayListBaseAdapter;

/**
 * @ClassName: UserAdapter
 * @Description: TODO(feedback 用户反馈界面的适配器)
 
 * @date 2014-7-22 下午5:56:32
 * 
 */

public class UserFeedbackAdapter extends ArrayListBaseAdapter<UserFeedbackBean> {

	public UserFeedbackAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.feedback_user_item, null);
		}
		final UserFeedbackBean userFeedback = mList.get(position);
		final UserFeedbackViewHoler viewHolder = new UserFeedbackViewHoler(convertView);
		convertView.setTag(userFeedback.getQuestion());
		viewHolder.initData(userFeedback);
		viewHolder.setSkinTheme(context);
		return convertView;
	}
}
