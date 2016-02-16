/**   
* @Title: UserAdapter.java
* @Package com.x.ui.adapter.feedback
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-7-22 下午5:56:32
* @version V1.0   
*/

package com.x.ui.adapter.feedback;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.x.R;
import com.x.publics.http.model.FeedbackDialogueBean;
import com.x.ui.activity.feedback.SubmitFeedbackActivity;
import com.x.ui.adapter.ArrayListBaseAdapter;

/**
 * @ClassName: UserAdapter
 * @Description: TODO(提交反馈 界面的适配器)
 
 * @date 2014-7-22 下午5:56:32
 * 
 */

public class SubmitFeedbackAdapter extends ArrayListBaseAdapter<FeedbackDialogueBean> {

	private Activity context;

	public SubmitFeedbackAdapter(Activity context) {
		super(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.feedback_submit_item, null);

		final FeedbackDialogueBean sfb = mList.get(position);
		SubmitFeedbackViewHoler holder = new SubmitFeedbackViewHoler(convertView);
		convertView.setTag(sfb.getFeedbackType());//全量包url
		SubmitFeedbackActivity sub = (SubmitFeedbackActivity) context;
		holder.resendIv.setOnClickListener(new MyClickListner(holder, sub, sfb));
		if (sfb.isShow() == true) {
			holder.resendIv.setVisibility(View.VISIBLE);
		}
		holder.initData(context,sfb);
		return convertView;
	}

	public class MyClickListner implements OnClickListener {
		private SubmitFeedbackActivity sub;
		private SubmitFeedbackViewHoler holder;
		private FeedbackDialogueBean sfb;

		public MyClickListner(SubmitFeedbackViewHoler holder, SubmitFeedbackActivity sub, 
				FeedbackDialogueBean sfb ) {
			this.holder = holder;
			this.sub = sub;
			this.sfb = sfb;
		}

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.resendIv:
				holder.resendIv.setVisibility(View.GONE);
				sfb.setShow(false);
				sub.sendData(false, sfb);
				break;

			default:
				break;
			}
		}
	}
}
