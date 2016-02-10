/**   
* @Title: UserFeedbackViewHoler.java
* @Package com.x.ui.adapter.feedback
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-7-23 上午9:43:46
* @version V1.0   
*/

package com.x.ui.adapter.feedback;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.x.R;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.publics.http.model.FeedbackDialogueBean;
import com.x.publics.http.model.FeedbackDialogueResponse;
import com.x.publics.utils.Utils;

/**
 * @ClassName: SubmitFeedbackViewHoler
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-7-23 上午9:43:46
 * 
 */

public class SubmitFeedbackViewHoler implements Serializable {
	private static final long serialVersionUID = 5L;
	private TextView serverReplyTv, serverTimeTv;
	private TextView userQuestionTv, userTimeTv;
	private ImageView userIconIv;
	private LinearLayout server_ll, user_ll;
	public ImageView resendIv;

	public SubmitFeedbackViewHoler(View view) {
		if (view != null) {
			server_ll = (LinearLayout) view.findViewById(R.id.server_ll);
			serverReplyTv = (TextView) view.findViewById(R.id.serverReplyTv);
			serverTimeTv = (TextView) view.findViewById(R.id.serverTimeTv);
			user_ll = (LinearLayout) view.findViewById(R.id.user_ll);
			userQuestionTv = (TextView) view.findViewById(R.id.submit_userQuestionTv);
			userTimeTv = (TextView) view.findViewById(R.id.userTimeTv);
			resendIv = (ImageView) view.findViewById(R.id.resendIv);
			userIconIv = (ImageView) view.findViewById(R.id.userIconIv);
		}
	}

	public void initData(Context context, FeedbackDialogueBean submitFeedbackBean) {

		if (submitFeedbackBean.getFeedbackType() == 2)//服务端回复
		{
			server_ll.setVisibility(View.VISIBLE);
			user_ll.setVisibility(View.GONE);
			if (submitFeedbackBean.getContent() != null)
				serverReplyTv.setText(submitFeedbackBean.getContent());
			if (submitFeedbackBean.getSendTime() != 0)
				serverTimeTv.setText(Utils.converCurrentTime(submitFeedbackBean.getSendTime()));
		} else {//用户回复
			server_ll.setVisibility(View.GONE);
			user_ll.setVisibility(View.VISIBLE);
			if (submitFeedbackBean.getContent() != null)
				userQuestionTv.setText(submitFeedbackBean.getContent());
			if (submitFeedbackBean.getSendTime() != 0)
				userTimeTv.setText(Utils.converCurrentTime(submitFeedbackBean.getSendTime()));
			userIconIv.setImageResource(ZeroDataResourceHelper.getSelfZerodataHeadPortrait(context));
			if (submitFeedbackBean.isShow() == true)//防止ListView数据及状态错乱
			{
				resendIv.setVisibility(View.VISIBLE);
			} else {
				resendIv.setVisibility(View.GONE);
			}
		}
	}
}
