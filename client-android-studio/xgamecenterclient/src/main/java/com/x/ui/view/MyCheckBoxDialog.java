package com.x.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.Utils;

/**
 * @ClassName: MyChexkBoxDialog
 * @Desciption: TODO
 
 * @Date: 2014-3-1 下午5:38:32
 */
public class MyCheckBoxDialog extends Dialog {

	private static CheckBox cbExit;
	private static View dividerLineView;
	private static TextView btnLeft, btnRight, tvTitle, tvContent, tvExitContent;

	public MyCheckBoxDialog(Context context) {
		super(context);
	}

	public MyCheckBoxDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private String title; // 标题
		private String message;// 内容
		private String queTips;// 问题提示
		private Context mContext; // 上下文
		private String positiveButtonText; // 按钮文字
		private String negativeButtonText; // 按钮文字

		private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener;

		public Builder(Context context) {
			this.mContext = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setMessage(int message) {
			this.message = (String) mContext.getText(message);
			return this;
		}

		public Builder setTitle(int title) {
			this.title = (String) mContext.getText(title);
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setQueTips(int queTips) {
			this.queTips = (String) mContext.getText(queTips);
			return this;
		}

		public Builder setQueTips(String queTips) {
			this.queTips = queTips;
			return this;
		}

		public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) mContext.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public MyCheckBoxDialog create() {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final MyCheckBoxDialog dialog = new MyCheckBoxDialog(mContext, R.style.MyDialog);
			View layout = inflater.inflate(R.layout.common_checkbox_dialog_layout, null);
			btnLeft = (TextView) layout.findViewById(R.id.btn_left);
			btnRight = (TextView) layout.findViewById(R.id.btn_right);
			tvTitle = (TextView) layout.findViewById(R.id.tv_title);
			tvContent = (TextView) layout.findViewById(R.id.tv_content);
			dividerLineView = layout.findViewById(R.id.divider_line);
			cbExit = (CheckBox) layout.findViewById(R.id.exit_checkBox);
			tvExitContent = (TextView) layout.findViewById(R.id.exit_checkBox_content);
			cbExit.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					SharedPrefsUtil.putValue(mContext, "edcl_continue_download_cb", isChecked);
				}
			});

			tvTitle.setText(title);
			tvContent.setText(message);
			btnLeft.setText(positiveButtonText);
			btnRight.setText(negativeButtonText);
			tvExitContent.setText(ResourceUtil.getString(mContext, R.string.dialog_app_exit_tips,ResourceUtil.getString(mContext, R.string.app_name)));
			setSkinTheme();

			if (positiveButtonClickListener != null) {
				btnLeft.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
					}
				});
			}

			if (negativeButtonClickListener != null) {
				btnRight.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
					}
				});
			}

			dialog.setContentView(layout);
			dialog.setCanceledOnTouchOutside(false); // 设置点击对话框以外的区域,对话框不消失

			Window dialogWindow = dialog.getWindow();
			WindowManager m = dialogWindow.getWindowManager();
			Display display = m.getDefaultDisplay(); // 获取屏幕宽、高
			WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
			// 屏幕宽度
			float screenWidth = display.getWidth();
			// 屏幕高度
			//float screenHeight = display.getHeight();

			// 判断设备是平板、手机
			if (Utils.isTablet(mContext)) {
				// Log.i("is Tablet!");
				p.width = (int) (screenWidth * 0.70); // 宽度设置为屏幕的0.70
			} else {
				// Log.i("is phone!");
				p.width = (int) (screenWidth * 0.85); // 宽度设置为屏幕的0.85
			}

			dialogWindow.setAttributes(p);

			return dialog;
		}

		/** 
		* @Title: setSkinTheme 
		* @Description: TODO 
		* @param     
		* @return void    
		*/
		private void setSkinTheme() {
			SkinConfigManager.getInstance().setViewBackground(mContext, btnLeft, SkinConstan.ITEM_THEME_BG);
			SkinConfigManager.getInstance().setViewBackground(mContext, btnRight, SkinConstan.ITEM_THEME_BG);
			SkinConfigManager.getInstance().setViewBackground(mContext, dividerLineView, SkinConstan.TITLE_BAR_BG);
			SkinConfigManager.getInstance().setCheckBoxBtnDrawable(mContext, cbExit, SkinConstan.CB_EXIT_BTN);
		}
	}

}
