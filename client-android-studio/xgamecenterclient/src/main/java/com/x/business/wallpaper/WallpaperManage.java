package com.x.business.wallpaper;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Toast;

import com.x.R;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.WallpaperRequest;
import com.x.publics.http.model.WallpaperResponse;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.WallpaperBean;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.ProgressDialogUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Constan.MediaType;

/**
 * @ClassName: WallpaperManage
 * @Desciption: 壁纸模块、逻辑处理
 
 * @Date: 2014-4-2 上午9:22:34
 */
public class WallpaperManage {

	private Handler handler;
	public static Context mContext;
	private static WallpaperManage wallpaperManage;

	public WallpaperManage(Context context) {
		mContext = context;
	}

	/**
	 * 构建实例，单例模式
	 
	 * @param context
	 * @return object
	 */
	public static WallpaperManage getInstance(Context context) {
		mContext = context;
		if (wallpaperManage == null) {
			wallpaperManage = new WallpaperManage(context);
		}
		return wallpaperManage;
	}

	/**
	 * 下载所有的图片
	 * @param mContext
	 * @param column
	 * @return
	 */
	public void downloadAll(int themeId, Handler handler) {
		this.handler = handler;
		ProgressDialogUtil.openProgressDialog(mContext, ResourceUtil.getString(mContext, R.string.loading));

		WallpaperRequest request = new WallpaperRequest();
		request.setThemeId(themeId);
		DataFetcher.getInstance().getWallpaperAlbumDetail(request, myResponseListent, myErrorListener, true);
	}

	/**
	 * 数据响应
	 * @return
	 */
	public Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			if (!ProgressDialogUtil.progressDialog.isShowing()) {
				handler.sendEmptyMessage(0);
				return;
			}

			ProgressDialogUtil.closeProgressDialog(); // 数据响应，关闭对话框
			LogUtil.getLogger().d("response==>" + response.toString());
			final WallpaperResponse wallpaperResponse = (WallpaperResponse) JsonUtil.jsonToBean(response,
					WallpaperResponse.class);
			if (wallpaperResponse != null && wallpaperResponse.state != null && wallpaperResponse.state.code == 200) {
				if (wallpaperResponse.imagelist != null && !wallpaperResponse.imagelist.isEmpty()) {
					if (!DownloadManager.getInstance().canDownload(mContext))
						return;
					handler.sendEmptyMessage(1);
					onkeyDownload(wallpaperResponse.imagelist);
				}
			} else {
				handler.sendEmptyMessage(0);
				ToastUtil.show(mContext, ResourceUtil.getString(mContext, R.string.one_click_download_error),
						Toast.LENGTH_SHORT);
			}
		}
	};

	/**
	 * 数据响应，异常处理
	 * @return
	 */
	public ErrorListener myErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
			ToastUtil.show(mContext, ResourceUtil.getString(mContext, R.string.one_click_download_error),
					Toast.LENGTH_SHORT);
			ProgressDialogUtil.closeProgressDialog();
			handler.sendEmptyMessage(0);
		}
	};

	/** 
	* @Title: onkeyDownload 
	* @Description: 一键下载 
	* @param @param wallpaperBeans    
	* @return void    
	*/

	private void onkeyDownload(final ArrayList<WallpaperBean> wallpaperBeans) {
		// 开启线程，进行下载操作
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (WallpaperBean wallpaperBean : wallpaperBeans) {
					SystemClock.sleep(200);

					addDownload(wallpaperBean, true);
				}
			}
		}).start();
	}

	/**
	* @Title: addDownload 
	* @Description: 添加壁纸下载任务，判断是否需要下载
	* @param @param wpBean    
	* @return void   
	* @throws
	 */
	public void addDownload(WallpaperBean wpBean, boolean isBatchDownload) {

		DownloadBean downloadBean = DownloadEntityManager.getInstance()
				.getDownloadBeanByResId(wpBean.getImageId() + "");
		// 判断downloadBean是否存在
		if (downloadBean == null) {
			toDownloadFile(wpBean, isBatchDownload);
		} else {
			// 判断已下载的文件是否存在
			String localPathUrl = downloadBean.getLocalPath();
			if (localPathUrl != null) {
				File file = new File(localPathUrl);
				if (!file.exists()) {
					toDownloadFile(wpBean, isBatchDownload);
				}
			}
		}
	}

	/**
	* @Title: toDownloadFile 
	* @Description: 壁纸文件下载
	* @param @param wpBean    
	* @return void   
	* @throws
	 */
	public void toDownloadFile(WallpaperBean wpBean, boolean isBatchDownload) {
		DownloadBean download = new DownloadBean(wpBean.getUrl(), wpBean.getBiglogo(), wpBean.getImageName(),
				wpBean.getFileSize(), 0, MediaType.IMAGE, DownloadTask.TASK_DOWNLOADING, wpBean.getFileSize(), false,
				wpBean.getImageId(), wpBean.getCategoryId());
		if (isBatchDownload) {
			DataEyeManager.getInstance().source(StatisticConstan.SrcName.WALLPAPER_ALBUM, 0, null, 0L, null, null,
					false);
			DownloadManager.getInstance().addDownloads(mContext, download);
		} else {
			DownloadManager.getInstance().addDownload(mContext, download);
		}
	}
}
