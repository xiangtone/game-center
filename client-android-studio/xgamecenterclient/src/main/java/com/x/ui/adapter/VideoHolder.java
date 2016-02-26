package com.x.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.FileBean;
import com.x.publics.utils.NativeImageLoader;
import com.x.publics.utils.NativeImageLoader.NativeImageCallBack;
import com.x.ui.view.MyImageView;
import com.x.ui.view.MyImageView.OnMeasureListener;

/**
 * 视频 Holder
 * 
 
 * 
 */
public class VideoHolder {
	private Point mPoint = new Point(0, 0);// 封装ImageView的宽和高
	private MyImageView imageView = null;
	private TextView name = null;
	private ImageView video_def = null;
	public CheckBox checkBox = null;

	public VideoHolder(View view) {
		if (view != null) {
			imageView = (MyImageView) view.findViewById(R.id.vid_image);
			name = (TextView) view.findViewById(R.id.vid_name);
			checkBox = (CheckBox) view.findViewById(R.id.vid_check);
			video_def = (ImageView) view.findViewById(R.id.video_def);
			imageView.setOnMeasureListener(new OnMeasureListener() {

				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});
		}
	}

	public void initData(final Context con, final FileBean bean, final GridView gridView) {
		name.setText(bean.getFileName());
		checkBox.setChecked(bean.getIscheck() == 1 ? true : false);
		checkBox.setVisibility(bean.getStatus() == 1 ? View.VISIBLE : View.INVISIBLE);
		imageView.setImageDrawable(null);
		imageView.setTag(bean.getFilePath());
		Bitmap bitmap = NativeImageLoader.getInstance().loadNativeThumbnails(con, bean, new NativeImageCallBack() {

			@Override
			public void onImageLoader(Bitmap bitmap, String path) {
				MyImageView imageView = (MyImageView) gridView.findViewWithTag(path);
				if (bitmap != null && imageView != null)
					imageView.setImageBitmap(bitmap);
			}
		});
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		}
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
