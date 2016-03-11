/**   
* @Title: ApplockerPwdSetter.java
* @Package com.x.ui.activity.applocker
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-11 上午9:34:49
* @version V1.0   
*/

package com.x.ui.activity.applocker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.x.R;
import com.x.business.account.AccountHelper;
import com.x.business.applocker.LockManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.view.editview.ClearEditText;

/**
* @ClassName: ApplockerPwdSetter
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-11 上午9:34:49
* 
*/

@SuppressLint("HandlerLeak")
public class ApplockerPwdSetterActivity extends BaseActivity implements View.OnClickListener {
	/**
	 * which page start this activity
	 * 1:ApplockerMainActivity
	 * 2:Advance Setting
	 */
	public static int START_SRC = 0;
	private static final int UPDATE_STATE_CONFIRM_PWD = 0;
	private static final int UPDATE_STATE_SECURITY = 1;
	/*
	 *views 
	 */
	//password views
	private Button[] buttons = new Button[12];
	private ImageView[] startsView = new ImageView[4];
	private ImageView inputGuideStepOneIv, inputGuideStepTwoIv, inputGuideStepThreeIv;
	private TextView inputErrorInfoTv;
	private LinearLayout keyboardLl, keyboardLineLl;
	//security views
	private RelativeLayout securityRl;
	private ClearEditText securityAnswerEt;
	private EditText securityQuestionEt;
	private Button confirmBtn;
	private ImageButton securityQuestionBtn;
	private TextView illegalInfoTv;
	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	static final int[] aa = { R.id.applocker_pwd_setter_keyboard_0, R.id.applocker_pwd_setter_keyboard_1,
			R.id.applocker_pwd_setter_keyboard_2, R.id.applocker_pwd_setter_keyboard_3,
			R.id.applocker_pwd_setter_keyboard_4, R.id.applocker_pwd_setter_keyboard_5,
			R.id.applocker_pwd_setter_keyboard_6, R.id.applocker_pwd_setter_keyboard_7,
			R.id.applocker_pwd_setter_keyboard_8, R.id.applocker_pwd_setter_keyboard_9,
			R.id.applocker_pwd_setter_keyboard_find_pwd, R.id.applocker_pwd_setter_keyboard_delete };
	/*
	 * variables
	 */
	private int inputCount = 0;
	private int inputState = 0;//input state:which step you step in .1: confirm password;2 confirm safety email.
	private int[] starsCtrl = { 0, 0, 0, 0 };
	private int[] password = { 0, 0, 0, 0 };
	private String password1;
	private String password2;
	private int questionWhat = 0;
	//security questions
	private String[] defaultQuestions = new String[5];
	/*
	 * methods
	 */
	private MyKeyBoardListener myKeyBoardListener = new MyKeyBoardListener();
	private MyHandler myHandler = new MyHandler();
	private LockManager lockManager = new LockManager(this);
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applocker_password_setter);

		initViews();
		setViews();
		initTitle();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
	}

	/**
	 * get all views' instance 
	* @Title: initViews 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void initViews() {
		//password input page views container
		keyboardLl = (LinearLayout) findViewById(R.id.applocker_pwd_setter_keyboard_ll);
		keyboardLineLl = (LinearLayout) findViewById(R.id.applocker_pwd_setter_keyboard_line_ll);
		//security questions set page views container
		securityRl = (RelativeLayout) findViewById(R.id.applocker_pwd_setter_security_rl);
		/*
		 * keyboard
		 */
		for (int i = 0; i < 12; i++) {
			buttons[i] = (Button) findViewById(aa[i]);
		}
		/*
		 * stars
		 */
		startsView[0] = (ImageView) findViewById(R.id.applocker_pwd_setter_star_1);
		startsView[1] = (ImageView) findViewById(R.id.applocker_pwd_setter_star_2);
		startsView[2] = (ImageView) findViewById(R.id.applocker_pwd_setter_star_3);
		startsView[3] = (ImageView) findViewById(R.id.applocker_pwd_setter_star_4);
		//shows the input password error info
		inputErrorInfoTv = (TextView) findViewById(R.id.applocker_pwd_setter_error_info);
		//guide which step you are in
		inputGuideStepOneIv = (ImageView) findViewById(R.id.applocker_pwd_setter_guide_step_one_iv);
		inputGuideStepTwoIv = (ImageView) findViewById(R.id.applocker_pwd_setter_guide_step_two_iv);
		inputGuideStepThreeIv = (ImageView) findViewById(R.id.applocker_pwd_setter_guide_step_three_iv);
		/*
		 * security views
		 */
		securityQuestionEt = (EditText) findViewById(R.id.applocker_pwd_setter_security_question_et);
		securityAnswerEt = (ClearEditText) findViewById(R.id.applocker_pwd_setter_security_answer_et);
		confirmBtn = (Button) findViewById(R.id.applocker_pwd_setter_security_confirm_btn);
		securityQuestionBtn = (ImageButton) findViewById(R.id.applocker_pwd_setter_security_question_btn);
		illegalInfoTv = (TextView) findViewById(R.id.applocker_pwd_setter_security_illegal_info_tv);
		/*
		 * security questions
		 */
		defaultQuestions[0] = getResources().getString(R.string.favorite_color);
		defaultQuestions[1] = getResources().getString(R.string.love_people);
		defaultQuestions[2] = getResources().getString(R.string.birthday);
		defaultQuestions[3] = getResources().getString(R.string.where_born);
		defaultQuestions[4] = getResources().getString(R.string.custom_question);
	}

	private void initTitle() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mNavigationView.setOnClickListener(this);
		//set title
		if (lockManager.isFirstUse()) {
			mTitleTv.setText(R.string.set_password);
		} else {
			mTitleTv.setText(R.string.reset_password);
		}
	}

	/**
	 * 
	* @Title: initSecurityViews 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void initSecurityViews() {
		/*
		 * set question input EditText
		 */
		securityQuestionEt.setFocusable(false);//this shouldn't be able to input while initialized.
		if (TextUtils.isEmpty(lockManager.getSecurityQuestion())) {
			securityQuestionEt.setText(defaultQuestions[1]);
		} else {
			securityQuestionEt.setText(lockManager.getSecurityQuestion());
		}

		/*
		 * set answer input EditText
		 */
		securityAnswerEt.requestFocus();
		securityAnswerEt.setFocusableInTouchMode(true);
		InputMethodManager inputManager = (InputMethodManager) securityAnswerEt.getContext().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(securityAnswerEt, 0);
		securityAnswerEt.setText(lockManager.getSecurityAnswer());
		AccountHelper.editEndFocus(securityAnswerEt);
		//set question btn listener
		securityQuestionBtn.setOnClickListener(new MySecurityBtnListener());
		//set confirm btn listener
		confirmBtn.setOnClickListener(new MySecurityBtnListener());

		questionWhat = getQuestionPosition();

	}

	/**
	 * save security Q & A into sp.
	* @Title: confirmQandA 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void confirmQandA() {
		lockManager.saveSecurityQuestion(securityQuestionEt.getText().toString());
		lockManager.saveSecurityAnswer(securityAnswerEt.getText().toString());
	}

	/**
	 * check legality of user input answer 
	* @Title: checkSecurityAnswer 
	* @Description: TODO 
	* @param @return    
	* @return boolean
	 */
	private boolean checkSecurityAnswer() {
		String question = securityQuestionEt.getText().toString();
		String answer = securityAnswerEt.getText().toString();
		int qCount = question.length();
		int aCount = answer.length();
		if ((qCount > 0 && qCount <= 30) && (aCount > 0 && aCount <= 30)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * get the positioin at default question array
	* @Title: getQuestionPosition 
	* @Description: TODO 
	* @param @return    
	* @return int
	 */
	private int getQuestionPosition() {
		int i = 0;
		for (String string : defaultQuestions) {
			if (lockManager.getSecurityQuestion().equals(string)) {
				return i;
			}
			i++;
		}
		return i;
	}

	/**
	 * show my default questions list
	* @Title: myQuestionDialog 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void myQuestionDialog() {
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setItems(defaultQuestions, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (which == 4) {//custom question
					securityQuestionEt.setText("");
					securityQuestionEt.setFocusable(true);
					securityQuestionEt.setFocusableInTouchMode(true);
					securityQuestionEt.requestFocus();
					securityQuestionEt.requestFocusFromTouch();
					securityAnswerEt.setText("");
				} else {
					if (which != questionWhat)//choose a different question,clear input answer.
						securityAnswerEt.setText("");
					securityQuestionEt.setFocusable(false);
					securityQuestionEt.setText(defaultQuestions[which]);
				}
				questionWhat = which;
			}
		});
		dialog.show();
	}

	/**
	 * set dispaly views
	* @Title: setViews 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void setViews() {
		/*
		 * set keyboard listener
		 */
		setKeyBoardBtnListener();

		inputErrorInfoTv.setText("");

		/*
		 * set stars
		 */
		for (int i = 0; i < starsCtrl.length; i++) {
			if (starsCtrl[i] == 1) {
				startsView[i].setImageResource(R.drawable.ic_password_point);
				startsView[i].setBackgroundResource(R.drawable.ic_input_box_green);
			}
			if (starsCtrl[i] == 0) {
				startsView[i].setImageResource(R.drawable.ic_password_point_withe);
				startsView[i].setBackgroundResource(R.drawable.ic_input_box_grey);
			}
		}

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
	 * compare two passwords
	* @Title: comparePwd 
	* @Description: TODO 
	* @param @return    
	* @return boolean
	 */
	private boolean comparePwd() {
		return password1.equals(password2);

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
		switch (inputState) {
		case UPDATE_STATE_CONFIRM_PWD:
			password1 = String.valueOf(password[0]) + String.valueOf(password[1]) + String.valueOf(password[2])
					+ String.valueOf(password[3]);

			initVariable();
			break;
		case UPDATE_STATE_SECURITY:
			password2 = String.valueOf(password[0]) + String.valueOf(password[1]) + String.valueOf(password[2])
					+ String.valueOf(password[3]);
			initVariable();
			break;
		default:
			break;
		}
		message.arg1 = inputState;
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
		case UPDATE_STATE_CONFIRM_PWD:
			inputGuideStepOneIv.setImageResource(R.drawable.ic_set_password_unselected);
			inputGuideStepTwoIv.setImageResource(R.drawable.ic_confirm_password);
			setViews();
			inputState = 1;
			break;
		case UPDATE_STATE_SECURITY:
			//			Toast.makeText(ApplockerPwdSetter.this, "" + starsCtrl[0], Toast.LENGTH_SHORT).show();
			setViews();
			inputGuideStepTwoIv.setImageResource(R.drawable.ic_confirm_password_unselected);
			if (comparePwd()) {
				inputGuideStepThreeIv.setImageResource(R.drawable.ic_security_question);
				inputState++;
				keyboardLl.setVisibility(View.GONE);
				keyboardLineLl.setVisibility(View.GONE);
				securityRl.setVisibility(View.VISIBLE);
				initSecurityViews();
			} else {
				inputErrorInfoTv.setText(R.string.password_match_fail);
				inputGuideStepOneIv.setImageResource(R.drawable.ic_set_password);
				inputState = 0;
			}
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
			case R.id.applocker_pwd_setter_keyboard_0:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_setter_keyboard_1:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_setter_keyboard_2:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_setter_keyboard_3:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_setter_keyboard_4:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_setter_keyboard_5:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_setter_keyboard_6:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_setter_keyboard_7:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_setter_keyboard_8:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_setter_keyboard_9:
				inputPwd(Integer.valueOf(((Button) v).getText().toString()));
				break;
			case R.id.applocker_pwd_setter_keyboard_find_pwd:

				break;
			case R.id.applocker_pwd_setter_keyboard_delete:
				deletePwd();
				break;

			default:
				break;
			}
		}
	}

	private class MySecurityBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.applocker_pwd_setter_security_confirm_btn:
				if (checkSecurityAnswer()) {
					confirmQandA();
					if (lockManager.isFirstUse()) {
						switch (START_SRC) {
						case 1:
							lockManager.saveLockStatus(true);
							lockManager.startApplockerComponent();
							break;
						default:
							break;
						}

					}
					lockManager.changeFirstUseState();
					lockManager.savePassword(password2);
					ApplockerPwdSetterActivity.this.finish();
				} else {
					illegalInfoTv.setText(R.string.input_illegal);
				}
				break;
			case R.id.applocker_pwd_setter_security_question_btn:
				myQuestionDialog();
				break;

			default:
				break;
			}
		}

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
		SkinConfigManager.getInstance().setViewBackground(context, securityQuestionBtn,
				SkinConstan.BTN_AND_PROGRESS_THEME_BG);
	}
}
