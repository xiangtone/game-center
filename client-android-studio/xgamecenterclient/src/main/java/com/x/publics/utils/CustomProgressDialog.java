package com.x.publics.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;

/**
 * @ClassName: CustomProgressDialog
 * @Desciption: TODO
 
 * @Date: 2014-4-14 下午2:11:47
 */
public class CustomProgressDialog extends Dialog {

	private static TextView loadingMsg;
	private static View loadingPb, loadingLogo;
	private static CustomProgressDialog customProgressDialog;

	public CustomProgressDialog(Context context) {
		super(context);

	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static CustomProgressDialog createDialog(Context context) {
		customProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.common_dialog_loading);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		loadingPb = customProgressDialog.findViewById(R.id.loading_prograssbar);
		loadingLogo = customProgressDialog.findViewById(R.id.loading_logo);
		loadingMsg = (TextView) customProgressDialog.findViewById(R.id.tv_message);
		setSkinTheme(context);
		return customProgressDialog;
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param @param context    
	* @return void    
	*/
	private static void setSkinTheme(Context context) {
		SkinConfigManager.getInstance().setViewBackground(context, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(context, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
		SkinConfigManager.getInstance().setTextViewStringColor(context, loadingMsg, SkinConstan.APP_THEME_COLOR);
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (customProgressDialog == null) {
			return;
		}
	}

	/**
	 * @param strTitle
	 * @return
	 */
	public CustomProgressDialog setTitile(String strTitle) {
		return customProgressDialog;
	}

	/**
	 * @param strMessage
	 * @return
	 */
	public CustomProgressDialog setMessage(String strMessage) {
		if (loadingMsg != null) {
			loadingMsg.setText(strMessage);
		}
		return customProgressDialog;
	}

}
