package com.x.ui.adapter;

import java.io.Serializable;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.publics.model.ThemeBean;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.SharedPrefsUtil;

/**
 * @ClassName: WallpaperAlbumViewHolder
 * @Desciption: 壁纸专题 ViewHolder
 
 * @Date: 2014-3-14 下午1:49:44
 */

public class WallpaperAlbumViewHolder implements Serializable {

	private static final long serialVersionUID = 1L;
	public ImageView logo;
	public View albumView;
	public TextView name, brief, tvDownloadAll;
	public LinearLayout oneClickNormalBtn, oneClickPressedBtn;
	public TextView  wallPaperAblumNum;//图片专辑数量textview

	/* the method for initialize components */
	public WallpaperAlbumViewHolder(View view) {
		if (view != null) {
			albumView = view.findViewById(R.id.wp_album_ll);
			name = (TextView) view.findViewById(R.id.wp_tv_name);
			brief = (TextView) view.findViewById(R.id.wp_tv_brief);
			logo = (ImageView) view.findViewById(R.id.wp_img_logo);
			wallPaperAblumNum=(TextView) view.findViewById(R.id.wp_ablum_num_tv); //图片数量textView
			tvDownloadAll = (TextView) view.findViewById(R.id.tv_download_all);
			oneClickNormalBtn = (LinearLayout) view.findViewById(R.id.btn_one_click_download_normal);
			oneClickPressedBtn = (LinearLayout) view.findViewById(R.id.btn_one_click_download_pressd);
		}
	}

	/* the method for setter Data to components */
	public void initData(ThemeBean themeBean, Context context) {
		name.setText(themeBean.getName());
		brief.setText(themeBean.getDescription());
		wallPaperAblumNum.setText(themeBean.getWallpaperNum()+" "+
		context.getResources().getString(R.string.wallpaper_ablum_num_text)); //设置专辑图片数量文字
		NetworkImageUtils.load(context, ImageType.NETWORK, themeBean.getBigicon(), R.drawable.banner_default_picture,
				R.drawable.banner_default_picture, logo);
		if (SharedPrefsUtil.getThemeValue(context, "theme_" + themeBean.getThemeId(), false)) {
			oneClickNormalBtn.setVisibility(View.GONE);
			oneClickPressedBtn.setVisibility(View.VISIBLE);
		} else {
			oneClickNormalBtn.setVisibility(View.VISIBLE);
			oneClickPressedBtn.setVisibility(View.GONE);
		}
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param     
	* @return void    
	*/
	public void setSkinTheme(Context context) {
		SkinConfigManager.getInstance().setDownloadAllSkin(context, tvDownloadAll);
	}

}
