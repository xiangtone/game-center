/**   
* @Title: SkinConfiueManager.java
* @Package com.mas.amineappstore.business.skin
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-11-25 下午5:30:00
* @version V1.0   
*/

package com.x.business.skin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.json.JSONObject;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.x.business.country.CountryManager;
import com.x.business.update.OriginalFileNotFountException;
import com.x.business.update.UpdateManage;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.Pager;
import com.x.publics.http.model.SkinAttentionRequest;
import com.x.publics.http.model.SkinAttentionResponse;
import com.x.publics.http.model.SkinDownloadRequest;
import com.x.publics.http.model.SkinListRequest;
import com.x.publics.http.model.SkinListResponse;
import com.x.publics.http.model.SkinDownloadRequest.SkinData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.Utils;
import com.x.ui.view.RoundProgress;
import com.x.ui.view.circularseekbar.CircularSeekBar;

/**
* @ClassName: SkinConfiueManager
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-11-25 下午5:30:00
* 
*/

public class SkinConfigManager {
	public static final int onSuccess = 200;
	public static final int onFailure = 500;
	public static final int onRefreshUi = 100;
	private static SkinConfigManager skinConfigManager;
	private Resources mResources;
	private String lastSkinPath;
	public static final String SKIN_PACKAGENAME = "com.mas.amineappstore.skin.black";
	private static final String TYPE_DRAWABLE = "drawable";
	private static final String TYPE_COLOR = "color";

	public static SkinConfigManager getInstance() {
		if (skinConfigManager == null)
			skinConfigManager = new SkinConfigManager();
		return skinConfigManager;
	}

	public SkinConfigManager() {
	}

	/** 
	* @Title: getDrawable 
	* @Description: 根据资源id获取未安装apk中drawable资源 
	* @param @param context
	* @param @param id
	* @param @return    
	* @return Drawable    
	*/

	public Drawable getDrawable(Context context, String id) {
		mResources = getPackageResource(context, getCurrentSkinApkPath(context));
		int drawableId = getDrawableId(context, mResources, id);
		Drawable drawable = null;
		try {
			drawable = mResources.getDrawable(drawableId);
		} catch (Exception e) {
			// e.printStackTrace();
			// if has exception,use default resources
			Resources res = context.getResources();
			drawableId = getDefaultDrawableId(context, res, id);
			drawable = res.getDrawable(drawableId);
		}
		return drawable;
	}

	/** 
	* @Title: getStringColor 
	* @Description: 根据资源id获取未安装apk中color资源  
	* @param @param context
	* @param @param id
	* @param @return    
	* @return int    -1为不存在资源
	*/

	public int getStringColor(Context context, String id) {
		mResources = getPackageResource(context, getCurrentSkinApkPath(context));
		int colorId = getColorId(context, mResources, id);
		int stringColorId = -1;
		try {
			stringColorId = mResources.getColor(colorId);
		} catch (Exception e) {
			// e.printStackTrace();
			// if has exception,use default resources
			Resources res = context.getResources();
			colorId = getDefaultColorId(context, res, id);
			stringColorId = res.getColor(colorId);
		}
		return stringColorId;
	}

	/**
	* @Title: getSelectorColor 
	* @Description: TODO 
	* @param @param context
	* @param @param id
	* @param @return    
	* @return ColorStateList
	 */
	public ColorStateList getSelectorColor(Context context, String id) {
		mResources = getPackageResource(context, getCurrentSkinApkPath(context));
		int colorId = getColorId(context, mResources, id);
		ColorStateList csl = null;
		try {
			csl = mResources.getColorStateList(colorId);
		} catch (Exception e) {
			// e.printStackTrace();
			// if has exception,use default resources
			Resources res = context.getResources();
			colorId = getDefaultColorId(context, res, id);
			csl = res.getColorStateList(colorId);
		}
		return csl;
	}

	private int getDrawableId(Context context, Resources resources, String drawableId) {
		return resources.getIdentifier(drawableId, TYPE_DRAWABLE, getCurrentSkinPackageName(context));
	}

	private int getColorId(Context context, Resources resources, String paramString) {
		return resources.getIdentifier(paramString, TYPE_COLOR, getCurrentSkinPackageName(context));
	}

	private int getDefaultDrawableId(Context context, Resources resources, String drawableId) {
		return resources.getIdentifier(drawableId, TYPE_DRAWABLE, context.getPackageName());
	}

	private int getDefaultColorId(Context context, Resources resources, String paramString) {
		return resources.getIdentifier(paramString, TYPE_COLOR, context.getPackageName());
	}

	/** 
	* @Title: getPackageResource 
	* @Description: 获取未安装apk的resource对象
	* @param @param context
	* @param @param apkPath
	* @param @return    
	* @return Resources    
	*/

	private Resources getPackageResource(Context context, String apkPath) {
		try {
			if (mResources != null && lastSkinPath.equals(apkPath)) {
				return mResources;
			}
			if (apkPath == null || !new File(apkPath).exists()) {
				return context.getResources();
			}
			lastSkinPath = apkPath;
			// 反射出资源管理器  
			Class<?> class_AssetManager = Class.forName("android.content.res.AssetManager");
			// 创建类的实例  
			Object assetMag = class_AssetManager.newInstance();
			// 声明方法，因为addAssetPath是被隐藏的，所以只能通过反射调用  
			Method method_addAssetPath = class_AssetManager.getDeclaredMethod("addAssetPath", String.class);
			// 执行方法  
			method_addAssetPath.invoke(assetMag, apkPath);
			// 为下一行传递参数用的  
			Resources res = context.getResources();
			// 确定用哪个构造函数  
			Constructor<?> constructor_Resources = Resources.class.getConstructor(class_AssetManager, res
					.getDisplayMetrics().getClass(), res.getConfiguration().getClass());
			// 执行构造函数  
			res = (Resources) constructor_Resources.newInstance(assetMag, res.getDisplayMetrics(),
					res.getConfiguration());
			// 返回apkPath的resource实例  
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void copyFile(Context context, String oldPath, String newPath) {
		/*	File newFile = new File(newPath);
			File newParentFile = newFile.getParentFile();
			if (!newParentFile.exists()) {
				newParentFile.mkdirs();
			}
			try {
				int byteread = 0;
				File oldFile = new File(oldPath);
				if (oldFile.exists()) {
					System.out.println("");
				}
				InputStream inStream = context.getAssets().open(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				fs.close();
				inStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		try {
			UpdateManage.getInstance(context).backupApplication(oldPath, newPath);
		} catch (OriginalFileNotFountException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getCurrentSkinApkPath(Context context) {
		return SharedPrefsUtil.getValue(context, "current_skin_apk_path", getDefaultSkinApkPath(context));
	}

	public void setCurrentSkinApkPath(Context context, String path) {
		SharedPrefsUtil.putValue(context, "current_skin_apk_path", path);
	}

	public String getCurrentSkinPackageName(Context context) {
		return SharedPrefsUtil.getValue(context, "current_skin_packagename", context.getPackageName());
	}

	public void setCurrentSkinPackageName(Context context, String packageName) {
		SharedPrefsUtil.putValue(context, "current_skin_packagename", packageName);
	}

	public String getDefaultSkinApkPath(Context context) {
		return ZeroDataResourceHelper.getInstance(context).getZappSourceDir();
	}

	/**
	* @Title: setTextViewDrawableTop 
	* @Description: setter textView drawableTop
	* @param @param context
	* @param @param textView
	* @param @param id    
	* @return void
	 */
	public void setTextViewDrawableTop(Context context, TextView textView, String id) {
		if (SkinConstan.skinEnabled) {
			Drawable drawable = getDrawable(context, id);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			textView.setCompoundDrawables(null, drawable, null, null);
		}
	}

	/**
	* @Title: setTextViewDrawableLeft 
	* @Description: setter textView drawableLeft
	* @param @param context
	* @param @param textView
	* @param @param id    
	* @return void
	 */
	public void setTextViewDrawableLeft(Context context, TextView textView, String id) {
		if (SkinConstan.skinEnabled) {
			Drawable drawable = getDrawable(context, id);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			textView.setCompoundDrawables(drawable, null, null, null);
		}
	}

	public void setTextViewDrawable(Context context, TextView textView, String id) {
		if (SkinConstan.skinEnabled) {
			Drawable drawable = getDrawable(context, id);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			int bottom = textView.getPaddingBottom();
			int top = textView.getPaddingTop();
			int right = textView.getPaddingRight();
			int left = textView.getPaddingLeft();
			setViewBackground(context, textView, id);
			textView.setPadding(left, top, right, bottom);
		}
	}

	public void setTextViewColor(Context context, TextView textView, String id) {
		if (SkinConstan.skinEnabled) {
			ColorStateList colorStateList = getSelectorColor(context, id);
			textView.setTextColor(colorStateList);
		}
	}

	public void setTextViewStringColor(Context context, TextView textView, String id) {
		if (SkinConstan.skinEnabled) {
			textView.setTextColor(getStringColor(context, id));
		}
	}

	public void setViewBackground(Context context, View view, String id) {
		if (SkinConstan.skinEnabled) {
			Drawable drawable = getDrawable(context, id);
			Utils.setBackgroundDrawable(view, drawable);
		}
	}

	public void setIndeterminateDrawable(Context context, ProgressBar pb, String id) {
		if (SkinConstan.skinEnabled) {
			Drawable drawable = getDrawable(context, id);
			Drawable indeterminateDrawable = pb.getIndeterminateDrawable();
			Rect bounds = indeterminateDrawable.getBounds();
			drawable.setBounds(bounds);
			pb.setIndeterminateDrawable(drawable);
		}
	}

	public void setProgressDrawable(Context context, ProgressBar pb, String id) {
		if (SkinConstan.skinEnabled) {
			Drawable drawable = getDrawable(context, id);
			pb.setProgressDrawable(drawable);
		}
	}

	public void setRadioBtnDrawable(Context context, RadioButton radioButton, String id) {
		if (SkinConstan.skinEnabled) {
			Drawable drawable = getDrawable(context, id);
			radioButton.setButtonDrawable(drawable);
		}
	}

	public void setRadioBtnDrawableTop(Context context, RadioButton radioButton, String id) {
		if (SkinConstan.skinEnabled) {
			ColorStateList csl = getSelectorColor(context, SkinConstan.RB_TEXT_COLOR);
			radioButton.setTextColor(csl);

			Drawable drawable = getDrawable(context, id);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			radioButton.setCompoundDrawables(null, drawable, null, null);
		}
	}

	/**
	* @Title: setRoundProgressColor 
	* @Description: TODO 
	* @param @param context
	* @param @param roundProgress
	* @param @param id    
	* @return void
	 */
	public void setRoundProgressColor(Context context, RoundProgress roundProgress, String id) {
		if (SkinConstan.skinEnabled) {
			roundProgress.setFillColor(getStringColor(context, id));
		}
	}

	/**
	* @Title: setTitleSkin 
	* @Description: TODO 
	* @param @param context
	* @param @param mTitle
	* @param @param mItem    
	* @return void
	 */
	public void setTitleSkin(Context context, View mTitle, View mItem1, View mItem2, View mItem3) {
		if (SkinConstan.skinEnabled) {
			setViewBackground(context, mTitle, SkinConstan.TITLE_BAR_BG);
			setViewBackground(context, mItem1, SkinConstan.ITEM_THEME_BG);
			setViewBackground(context, mItem2, SkinConstan.TITLE_PENDANT_BG);
			if (mItem3 != null) {
				setViewBackground(context, mItem3, SkinConstan.ITEM_THEME_BG);
			}
		}
	}

	/**
	* @Title: setCircleProgressColor 
	* @Description: TODO 
	* @param @param context
	* @param @param csb1
	* @param @param csb2    
	* @return void
	 */
	public void setCircleProgressColor(Context context, CircularSeekBar csb1, CircularSeekBar csb2) {
		if (SkinConstan.skinEnabled) {
			csb1.setPointerColor(getStringColor(context, SkinConstan.POINTER_COLOR));
			csb1.setPointerHaloColor(getStringColor(context, SkinConstan.POINTER_HALO_COLOR));
			csb1.setCircleProgressColor(getStringColor(context, SkinConstan.CIRCLE_PROGRESS_COLOR));

			csb2.setPointerColor(getStringColor(context, SkinConstan.POINTER_COLOR));
			csb2.setPointerHaloColor(getStringColor(context, SkinConstan.POINTER_HALO_COLOR));
			csb2.setCircleProgressColor(getStringColor(context, SkinConstan.CIRCLE_PROGRESS_COLOR));
		}
	}

	/**
	* @Title: setDownloadAllSkin 
	* @Description: one click download skin
	* @param @param context
	* @param @param textView    
	* @return void
	 */
	public void setDownloadAllSkin(Context context, TextView textView) {
		if (SkinConstan.skinEnabled) {
			setTextViewDrawableLeft(context, textView, SkinConstan.ONE_CLICK_DOWNLOAD_IMG);
			ColorStateList csl = getSelectorColor(context, SkinConstan.ONE_CLICK_DOWNLOAD_TEXT);
			textView.setTextColor(csl);
		}
	}

	public void setCheckBoxBtnDrawable(Context context, CheckBox checkBox, String id) {
		if (SkinConstan.skinEnabled) {
			Drawable drawable = getDrawable(context, id);
			checkBox.setButtonDrawable(drawable);
		}
	}

	public void setSkinCode(Context context, int skinCode) {
		SharedPrefsUtil.putValue(context, "skinCode", skinCode);
	}

	public int getSkinCode(Context context) {
		return SharedPrefsUtil.getValue(context, "skinCode", 0);
	}

	public void setSkinIsRead(Context context, boolean isRead) {
		SharedPrefsUtil.putValue(context, "skinIsRead", isRead);
	}

	public boolean getSkinIsRead(Context context) {
		return SharedPrefsUtil.getValue(context, "skinIsRead", false);
	}

	/** 
	* @Title: getSkinAttention 
	* @Description: Skin更新提醒 
	* @param @param context
	* @param @param handler    
	* @return void    
	*/

	public void getSkinAttention(Context context, Handler handler) {
		SkinAttentionRequest request = new SkinAttentionRequest();
		request.setSkinCode(getSkinCode(context));
		SkinAttentionLister skinAttentionLister = new SkinAttentionLister(handler);
		DataFetcher.getInstance().getSkinAttention(request, skinAttentionLister, skinAttentionLister, false);
	}

	private class SkinAttentionLister implements Listener<JSONObject>, ErrorListener {
		private Handler handler;

		public SkinAttentionLister(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void onResponse(JSONObject response) {
			if (handler == null) {
				return;
			}
			SkinAttentionResponse attentionResponse = (SkinAttentionResponse) JsonUtil.jsonToBean(response,
					SkinAttentionResponse.class);
			if (attentionResponse != null && attentionResponse.state.code == 200) {
				handler.obtainMessage(onSuccess, attentionResponse).sendToTarget();
			} else {
				handler.obtainMessage(onFailure).sendToTarget();
			}
		}

		@Override
		public void onErrorResponse(VolleyError error) {
			if (handler == null) {
				return;
			}
			handler.obtainMessage(onFailure).sendToTarget();
		}

	}

	/** 
	* @Title: getSkinList 
	* @Description: Skin列表获取 
	* @param @param context
	* @param @param handler    
	* @return void    
	*/

	public void getSkinList(Context context, Handler handler) {
		SkinListRequest request = new SkinListRequest();
		Pager pager = new Pager(1);
		request.setPager(pager);
		request.setRaveId(CountryManager.getInstance().getCountryId(context));
		SkinListLister skinListLister = new SkinListLister(handler);
		DataFetcher.getInstance().getSkinList(request, skinListLister, skinListLister, false);
	}

	private class SkinListLister implements Listener<JSONObject>, ErrorListener {
		private Handler handler;

		public SkinListLister(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void onResponse(JSONObject response) {
			if (handler == null) {
				return;
			}
			SkinListResponse skinListResponse = (SkinListResponse) JsonUtil
					.jsonToBean(response, SkinListResponse.class);
			if (skinListResponse != null && skinListResponse.state.code == 200) {
				handler.obtainMessage(onSuccess, skinListResponse).sendToTarget();
			} else {
				handler.obtainMessage(onFailure).sendToTarget();
			}
		}

		@Override
		public void onErrorResponse(VolleyError error) {
			if (handler == null) {
				return;
			}
			handler.obtainMessage(onFailure).sendToTarget();
		}

	}

	public void skinDownloadStatistic(Context context, SkinData skinData) {
		SkinDownloadRequest request = new SkinDownloadRequest();
		request.setData(skinData);
		SkinDownloadStatisticListLister statisticListLister = new SkinDownloadStatisticListLister();
		DataFetcher.getInstance().skinDownloadStatistic(request, statisticListLister, statisticListLister);
	}

	private class SkinDownloadStatisticListLister implements Listener<JSONObject>, ErrorListener {

		public SkinDownloadStatisticListLister() {
		}

		@Override
		public void onResponse(JSONObject response) {
		}

		@Override
		public void onErrorResponse(VolleyError error) {
		}
	}

	/**
	* @Title: getNewPath 
	* @Description: 新文件路径 
	* @param @param context
	* @param @param packageName
	* @param @return    
	* @return String
	 */
	public String getNewPath(Context context, String loaclPath) {
		File file = new File(loaclPath);
		return context.getDir("skin", Context.MODE_PRIVATE).getAbsolutePath() + File.separator + file.getName();
	}

	/**
	* @Title: isExists 
	* @Description: 判断文件是否存在
	* @param @param context
	* @param @param packageName
	* @param @return    
	* @return boolean
	 */
	public boolean isExists(Context context, String localPath) {
		File file = new File(getNewPath(context, localPath));
		return file.exists();
	}

	/**
	* @Title: setSkinParams 
	* @Description: 设置皮肤包参数
	* @param @param context
	* @param @param packageName
	* @param @param localPath    
	* @return void
	 */
	public void setSkinParams(Context context, Handler handler, String packageName, String localPath) {

		if (TextUtils.isEmpty(localPath)) {
			setCurrentSkinApkPath(context, null);
		} else {
			// 1.copy file to data/data
			copyFile(context, localPath, getNewPath(context, localPath));
			// 2.set skin params
			if (isExists(context, localPath)) {
				setCurrentSkinApkPath(context, getNewPath(context, localPath));
			} else {
				setCurrentSkinApkPath(context, localPath);
			}
		}

		setCurrentSkinPackageName(context, packageName);

		// 3.handler to ui
		handler.obtainMessage(SkinConfigManager.onRefreshUi).sendToTarget();
	}
}
