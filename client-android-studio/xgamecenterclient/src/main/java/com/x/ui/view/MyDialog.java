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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.utils.Utils;

/**
 * @ClassName: MyDialog
 * @Desciption: 自定义Dialog
 
 * @Date: 2014-2-15 下午4:34:32
 */
public class MyDialog extends Dialog {

	private static View dividerLineView;
	private static TextView btnLeft, btnRight, tvTitle, tvContent;

	public MyDialog(Context context) {
		super(context);
	}

	public MyDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * Settings 多选  Dialog
	 
	 */
	public static class Settings {
		private String title; // 标题
		private Context mContext; // 上下文
		private String positiveButtonText; // 按钮文字
		private String negativeButtonText; // 按钮文字
		private String[] itemNames; //选项名
		private boolean[] itemChoice; //选中状态

		private DialogInterface.OnMultiChoiceClickListener multiChoiceClickListener;

		private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener;

		public Settings(Context context) {
			this.mContext = context;
		}

		public Settings setTitle(int title) {
			this.title = (String) mContext.getText(title);
			return this;
		}

		public Settings setTitle(String title) {
			this.title = title;
			return this;
		}

		public Settings setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) mContext.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Settings setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) mContext.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Settings setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Settings setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Settings setMultiChoiceItems(DialogInterface.OnMultiChoiceClickListener multiChoiceClickListener) {
			this.multiChoiceClickListener = multiChoiceClickListener;
			return this;
		}

		public Settings setChoiceItem(String[] arrays) {
			itemNames = arrays;
			return this;
		}

		public Settings setChoiceStatus(boolean[] arrays) {
			itemChoice = arrays;
			return this;
		}

		public MyDialog create() {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final MyDialog dialog = new MyDialog(mContext, R.style.MyDialog);
			View layout = inflater.inflate(R.layout.settings_dialog_view, null);
			LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.layout_array);

			for (int i = 0; i < itemNames.length; i++) {
				View view = inflater.inflate(R.layout.settings_dialog_array, null);
				((TextView) view.findViewById(R.id.settings_id)).setText(i + "");//装载ID
				((TextView) view.findViewById(R.id.settings_text)).setText(itemNames[i]);
				CheckBox mCheckBox = (CheckBox) view.findViewById(R.id.settings_check);
				// set skin theme
				SkinConfigManager.getInstance().setCheckBoxBtnDrawable(mContext, mCheckBox, SkinConstan.OPTION_BTN);
				mCheckBox.setChecked(itemChoice[i]);

				if (i == (itemNames.length - 1))
					((View) view.findViewById(R.id.settings_view)).setVisibility(View.INVISIBLE);

				RelativeLayout mLayout = (RelativeLayout) view.findViewById(R.id.settings_layout);
				mLayout.setTag(mLayout);
				mLayout.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						RelativeLayout view = (RelativeLayout) v.getTag();
						TextView id = (TextView) view.findViewById(R.id.settings_id);
						CheckBox box = (CheckBox) view.findViewById(R.id.settings_check);
						if (box.isChecked())
							box.setChecked(false);
						else
							box.setChecked(true);
						multiChoiceClickListener.onClick(dialog, Integer.parseInt(id.getText().toString()),
								box.isChecked());
					}
				});
				linearLayout.addView(view);
			}

			btnLeft = (TextView) layout.findViewById(R.id.btn_left);
			btnRight = (TextView) layout.findViewById(R.id.btn_right);
			tvTitle = (TextView) layout.findViewById(R.id.tv_title);
			dividerLineView = layout.findViewById(R.id.divider_line);
			tvTitle.setText(title);
			btnLeft.setText(positiveButtonText);
			btnRight.setText(negativeButtonText);
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
		* @return void    
		*/
		private void setSkinTheme() {
			SkinConfigManager.getInstance().setViewBackground(mContext, btnLeft, SkinConstan.ITEM_THEME_BG);
			SkinConfigManager.getInstance().setViewBackground(mContext, btnRight, SkinConstan.ITEM_THEME_BG);
			SkinConfigManager.getInstance().setViewBackground(mContext, dividerLineView, SkinConstan.TITLE_BAR_BG);
		}
	}

	public static class Builder {
		private String title; // 标题
		private String message;// 内容
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

		public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) mContext.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) mContext.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
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

		public MyDialog create() {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final MyDialog dialog = new MyDialog(mContext, R.style.MyDialog);
			View layout = inflater.inflate(R.layout.common_dialog_layout, null);

			btnLeft = (TextView) layout.findViewById(R.id.btn_left);
			btnRight = (TextView) layout.findViewById(R.id.btn_right);
			tvTitle = (TextView) layout.findViewById(R.id.tv_title);
			tvContent = (TextView) layout.findViewById(R.id.tv_content);
			dividerLineView = layout.findViewById(R.id.divider_line);
			tvTitle.setText(title);
			tvContent.setText(message);
			btnLeft.setText(positiveButtonText);
			btnRight.setText(negativeButtonText);
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
		* @return void    
		*/
		private void setSkinTheme() {
			SkinConfigManager.getInstance().setViewBackground(mContext, btnLeft, SkinConstan.ITEM_THEME_BG);
			SkinConfigManager.getInstance().setViewBackground(mContext, btnRight, SkinConstan.ITEM_THEME_BG);
			SkinConfigManager.getInstance().setViewBackground(mContext, dividerLineView, SkinConstan.TITLE_BAR_BG);
		}
	}

}
