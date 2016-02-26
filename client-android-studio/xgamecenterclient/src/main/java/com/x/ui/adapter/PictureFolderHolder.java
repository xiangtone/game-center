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
import com.x.publics.model.AlbumBean;
import com.x.publics.utils.NetworkImageUtils;

/**
 * 相册 Holder
 
 *
 */
public class PictureFolderHolder {
	public ImageView image;
	public TextView name_num;
	public CheckBox checkBox;

	public PictureFolderHolder(View view) {
		if (view != null) {
			image = (ImageView) view.findViewById(R.id.pic_image);
			name_num = (TextView) view.findViewById(R.id.pic_name_num);
			checkBox = (CheckBox) view.findViewById(R.id.pic_check);
		}
	}

	public void initData(Context con, AlbumBean albumBean, final GridView mGridView) {
		String path = albumBean.getPath_top_file();
		name_num.setText(albumBean.getName_album() + "(" + albumBean.getList().size() + ")");
		image.setTag(path); //保存Tag
		if (albumBean.getStatus() == 1) {
			checkBox.setVisibility(View.VISIBLE);
		} else {
			checkBox.setVisibility(View.INVISIBLE);
		}
		checkBox.setChecked(albumBean.getIscheck() == 1 ? true : false);
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
