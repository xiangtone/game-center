package com.x.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.FileBean;
import com.x.publics.utils.NetworkImageUtils;

/**
 * 图片 Holder
 * 
 
 * 
 */
public class PictureHolder {
	public ImageView image;
	public CheckBox checkBox;
	public TextView textView;

	public PictureHolder(View view) {
		if (view != null) {
			image = (ImageView) view.findViewById(R.id.pic_image);
			checkBox = (CheckBox) view.findViewById(R.id.pic_check);
			textView = (TextView) view.findViewById(R.id.text_back);
		}
	}

	public void initData(Context con, FileBean pictureBean, final GridView mGridView) {
		textView.setVisibility(View.GONE);
		String path = pictureBean.getFilePath();
		image.setTag(path);
		if (pictureBean.getStatus() == 1) {
			checkBox.setVisibility(View.VISIBLE);
		} else {
			checkBox.setVisibility(View.GONE);
		}
		checkBox.setChecked(pictureBean.getIscheck() == 1 ? true : false);
		NetworkImageUtils.load(con, ImageType.LOCALPIC, path, R.drawable.banner_default_picture,
				R.drawable.banner_default_picture, image);
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param @param context    
	* @return void    
	*/
	public void setSkinTheme(Context context) {
		SkinConfigManager.getInstance().setCheckBoxBtnDrawable(context, checkBox, SkinConstan.OPTION_BTN);
	}
}
