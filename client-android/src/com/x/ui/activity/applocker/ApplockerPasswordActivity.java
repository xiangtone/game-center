/**   
* @Title: ApplockerPassword.java
* @Package com.x.business.applocker
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-9 下午2:43:59
* @version V1.0   
*/

package com.x.ui.activity.applocker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.x.business.applocker.LockManager;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.ModuleName;

/**
* @ClassName: ApplockerPassword
* @Description: TODO(被加锁的应用在该页面输入密码解锁当前应用)

* @date 2014-10-9 下午2:43:59
* 
*/

public class ApplockerPasswordActivity extends Activity {
	private static final int UPDATE_STATE_RESET = 0;
	private static final int UPDATE_STATE_START_ACTIVITY = 1;
	/**onKeyDown()不同复写方式标记*/
	public static Boolean flag = false;
	public static String CLOSE_APPLOCKER_MAIN_ACTION = "com.mas.applocker.ACTION_CLOSE_APPLOCK_MAIN";
	private MyKeyBoardListener myKeyBoardListener = new MyKeyBoardListener();
	private MyHandler myHandler = new MyHandler();
	private LockManager lockManager = new LockManager(this);

	private Button[] buttons = new Button[12];
	private ImageView[] inputBoxViews = new ImageView[4];
	private ImageView lockLogoIv;
	private TextView inputErrorTv;
	static final int[] pwdKeyboard = { R.id.applocker_pwd_keyboard_0, R.id.applocker_pwd_keyboard_1,
			R.id.applocker_pwd_keyboard_2, R.id.applocker_pwd_keyboard_3, R.id.applocker_pwd_keyboard_4,
			R.id.applocker_pwd_keyboard_5, R.id.applocker_pwd_keyboard_6, R.id.applocker_pwd_keyboard_7,
			R.id.applocker_pwd_keyboard_8, R.id.applocker_pwd_keyboard_9, R.id.applocker_pwd_keyboard_find_pwd,
			R.id.applocker_pwd_keyboard_delete };

	int inputCount = 0;
	int[] starsCtrl = { 0, 0, 0, 0 };
	int[] password = { 0, 0, 0, 0 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applocker_password);
		initViews();
		setViews();
		//		Log.d("applocker", "ApplockerPassword ---onCreate");
	}

	/**
	 * 初始化View
	* @Title: initViews 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void initViews() {
		/*
		 * keyboard
		 */
		for (int i = 0; i < 12; i++) {
			buttons[i] = (Button) findViewById(pwdKeyboard[i]);
		}
		/*
		 * stars
		 */
		inputBoxViews[0] = (ImageView) findViewById(R.id.applocker_pwd_star_1);
		inputBoxViews[1] = (ImageView) findViewById(R.id.applocker_pwd_star_2);
		inputBoxViews[2] = (ImageView) findViewById(R.id.applocker_pwd_star_3);
		inputBoxViews[3] = (ImageView) findViewById(R.id.applocker_pwd_star_4);
		/*
		 * shows input password error info 
		 */
		inputErrorTv = (TextView) findViewById(R.id.applocker_pwd_error_info);
		lockLogoIv = (ImageView) findViewById(R.id.applocker_pwd_lock_iv);
	}

	private void setViews() {
		/*
		 * set keyboard listener
		 */
		setKeyBoardBtnListener();

		/*
		 * set stars
		 */
		for (int i = 0; i < starsCtrl.length; i++) {
			if (starsCtrl[i] == 1) {
				inputBoxViews[i].setImageResource(R.drawable.ic_password_point);
				inputBoxViews[i].setBackgroundResource(R.drawable.ic_input_box_green);
				if (i == 3) {
					lockLogoIv.setImageResource(R.drawable.ic_applocker_lock_green);
				}
			}
			if (starsCtrl[i] == 0) {
				lockLogoIv.setImageResource(R.drawable.ic_applocker_lock);
				inputBoxViews[i].setImageResource(R.drawable.ic_password_point_withe);
				inputBoxViews[i].setBackgroundResource(R.drawable.ic_input_box_grey);
			}

		}
		//display input error info
		inputErrorTv.setText("");

	}

	/**
	 * change 0 to 1,1 represent a *.
	* @Title: starsIncrease 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void starsIncrease() {
		for (int i = 0; i < 4; i++) {
			if (starsCtrl[i] == 0) {
				starsCtrl[i] = 1;
				break;
			}
		}
	}

	/**
	 * change 1 to 0,1 represent a *.
	* @Title: starsReduce 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void starsReduce() {
		for (int i = 3; i >= 0; i--) {
			if (starsCtrl[i] == 1) {
				starsCtrl[i] = 0;
				break;
			}
		}
	}

	/**
	 * use an array save the input password
	* @Title: getInputPassword 
	* @Description: TODO 
	* @param @param a    
	* @return void
	 */
	private void getInputPassword(int a) {
		for (int i = 3; i >= 0; i--) {
			if (starsCtrl[i] == 1) {
				password[i] = a;
				break;
			}
		}
		//		for (int i = 0; i < 4; i++) {
		//			if (starsCtrl[i] == 1) {
		//				Log.d("pwd", password[i] + "");
		//			}
		//		}
		//		Log.d("pwd", "-----------");
	}

	/**
	 * control input & set views
	* @Title: inputPwd 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void inputPwd(int a) {
		starsIncrease();
		if (inputCount < 4) {
			inputCount++;
			getInputPassword(a);
		}

		setViews();
		if (inputCount == 4)
			ctrlHandler();

	}

	/**
	 * control delete & set views
	* @Title: deletePwd 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void deletePwd() {
		if (inputCount > 0)
			inputCount--;
		starsReduce();
		setViews();

	}

	/**
	 * send a msg,notice the handler update views.
	* @Title: ctrlHandler 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void ctrlHandler() {
		cancelKeyBoardBtnListener();
		Message message = myHandler.obtainMessage();
		String pwdTemp = String.valueOf(password[0]) + String.valueOf(password[1]) + String.valueOf(password[2])
				+ String.valueOf(password[3]);
		if (lockManager.checkPassword(pwdTemp)) {
			message.arg1 = UPDATE_STATE_START_ACTIVITY;

		} else {
			message.arg1 = UPDATE_STATE_RESET;
		}
		myHandler.sendMessageDelayed(message, 500);
	}

	/**
	 * initialize variables
	* @Title: initVariable 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void initVariable() {
		inputCount = 0;
		for (int i = 0; i < password.length; i++) {
			password[i] = 0;
			starsCtrl[i] = 0;
		}

	}

	/**
	 * synchronize views with current step
	* @Title: updateView 
	* @Description: TODO 
	* @param @param state    
	* @return void
	 */
	private void updateView(int state) {
		switch (state) {
		case UPDATE_STATE_RESET:
			initVariable();
			setViews();
			inputErrorTv.setText(R.string.password_unlock_fail);
			break;
		case UPDATE_STATE_START_ACTIVITY:
			ApplockerPasswordActivity.this.finish();
			break;
		default:
			break;
		}
		setKeyBoardBtnListener();
	}

	private void setKeyBoardBtnListener() {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setOnClickListener(myKeyBoardListener);
		}
	}

	private void cancelKeyBoardBtnListener() {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setOnClickListener(null);
		}
	}

	private class MyKeyBoardListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.applocker_pwd_keyboard_0:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_keyboard_1:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_keyboard_2:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_keyboard_3:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_keyboard_4:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_keyboard_5:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_keyboard_6:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_keyboard_7:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_keyboard_8:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_keyboard_9:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_keyboard_find_pwd:
				startActivity(new Intent(ApplockerPasswordActivity.this, AppLockerFindPasswordActivity.class));
				ApplockerPasswordActivity.this.finish();
				break;
			case R.id.applocker_pwd_keyboard_delete:
				deletePwd();
				break;

			default:
				break;
			}
		}
	}

	int count = 0;
	int firstClickTime = 0;
	int secClickTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (flag) {
				sendBroadcast(new Intent(CLOSE_APPLOCKER_MAIN_ACTION));
				this.finish();
			} else {
				Intent localIntent = new Intent("android.intent.action.MAIN");
				localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				localIntent.addCategory("android.intent.category.HOME");
				this.startActivity(localIntent);
			}
		}
		return true;

	}

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			updateView(msg.arg1);

		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		//		Log.d("applocker", "ApplockerPassword ---onResume");
		initVariable();
		setViews();
		DataEyeManager.getInstance().module(ModuleName.APPLOCK_LOCKED, true);
	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().module(ModuleName.APPLOCK_LOCKED, false);
	}
}
