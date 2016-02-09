/**   
* @Title: UserFeedbackViewHoler.java
* @Package com.mas.amineappstore.ui.adapter.feedback
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-7-23 上午9:43:46
* @version V1.0   
*/

package com.x.ui.adapter.feedback;

import java.io.Serializable;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.http.model.UserFeedbackBean;

/**
 * @ClassName: UserFeedbackViewHoler
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-7-23 上午9:43:46
 * 
 */

public class UserFeedbackViewHoler implements Serializable {
	private static final long serialVersionUID = 1L;
	public TextView questionTv, timeTv, replyTv;
	public ImageView arrowIv;

	public UserFeedbackViewHoler(View view) {
		if (view != null) {
			replyTv = (TextView) view.findViewById(R.id.userReplyTv);
			questionTv = (TextView) view.findViewById(R.id.userQuestionTv);
			timeTv = (TextView) view.findViewById(R.id.userTimeTv);
			arrowIv = (ImageView) view.findViewById(R.id.feedback_arrow_iv);
		}
	}

	public void initData(UserFeedbackBean userFeedbackBean) {
		CharSequence question = "", time = "", reply = "";
		if (userFeedbackBean.getQuestion() != null)
			question = userFeedbackBean.getQuestion();
		if (userFeedbackBean.getCreateTime() != null)
			time = userFeedbackBean.getCreateTime();
		if (userFeedbackBean.getReplyContent() != null)
			reply = userFeedbackBean.getReplyContent();
		questionTv.setText(question);
		timeTv.setText(time);
		replyTv.setText(reply);
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param @param context    
	* @return void    
	*/
	public void setSkinTheme(Context context) {
		SkinConfigManager.getInstance().setTextViewStringColor(context, questionTv, SkinConstan.APP_THEME_COLOR);
		SkinConfigManager.getInstance().setTextViewStringColor(context, timeTv, SkinConstan.APP_THEME_COLOR);
	}
}
