/**   
* @Title: FindPasswordActivity.java
* @Package com.mas.amineappstore.ui.activity.applocker
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-15 下午4:36:13
* @version V1.0   
*/

package com.x.ui.activity.applocker;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.applocker.LockManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.ui.activity.base.BaseActivity;

/**
* @ClassName: FindPasswordActivity
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-15 下午4:36:13
* 
*/

public class AppLockerFindPasswordActivity extends BaseActivity implements View.OnClickListener {
	private static final long TIME_DELAY = 300L;
	/*
	 * views
	 */
	private Context context = this;
	private TextView securityQuestionTv;
	private EditText securityAnswerEt;
	private Button confirmBtn;
	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;
	/*
	 * method
	 */
	private LockManager lockManager = new LockManager(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applocker_find_password);
		initViews();
		initTitle();
		setViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.finish();
	}

	/**
	 * init views
	* @Title: initViews 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void initViews() {
		securityAnswerEt = (EditText) findViewById(R.id.applocker_find_pwd_answer_et);
		securityQuestionTv = (TextView) findViewById(R.id.applocker_find_pwd_question_tv);
		confirmBtn = (Button) findViewById(R.id.applocker_find_pwd_confirm_btn);

	}

	private void initTitle() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mNavigationView.setOnClickListener(this);

		mTitleTv.setText(R.string.page_applocker_find_password);
	}

	/**
	 * set views
	* @Title: setViews 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void setViews() {
		securityQuestionTv.setText(lockManager.getSecurityQuestion());
		confirmBtn.setOnClickListener(new MyBtnListener());
		securityAnswerEt.setFocusable(true);
		securityAnswerEt.setFocusableInTouchMode(true);
		securityAnswerEt.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) securityAnswerEt.getContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(securityAnswerEt, 0);
			}
		}, TIME_DELAY);
	}

	/**
	 * check whether the input answer matching saved answer
	* @Title: checkSecurityAnswer 
	* @Description: TODO 
	* @param @return    
	* @return boolean
	 */
	private boolean checkSecurityAnswer() {
		String inputAnswer = securityAnswerEt.getText().toString();//get input answer.
		String defaultAnswer = lockManager.getSecurityAnswer();//get answer in sp.
		return defaultAnswer.equals(inputAnswer);
	}

	/**
	 * deal confirm btn click event
	* @Title: confirmLogicProcess 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void confirmLogicProcess() {
		if (checkSecurityAnswer()) {
			startActivity(new Intent(AppLockerFindPasswordActivity.this, ApplockerPwdSetterActivity.class));
			AppLockerFindPasswordActivity.this.finish();
		} else {
			Toast.makeText(AppLockerFindPasswordActivity.this, R.string.wrong_answer, Toast.LENGTH_SHORT).show();
			securityAnswerEt.setText("");
		}
	}

	/**
	 * my btns listener
	* @ClassName: MyBtnListener
	* @Description: TODO(这里用一句话描述这个类的作用)
	
	* @date 2014-10-15 下午5:23:01
	*
	 */
	private class MyBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.applocker_find_pwd_confirm_btn:
				confirmLogicProcess();
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.mh_navigate_ll:
			onBackPressed();
			break;

		default:
			break;
		}
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, confirmBtn, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
	}

}
