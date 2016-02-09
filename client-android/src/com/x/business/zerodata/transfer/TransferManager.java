package com.x.business.zerodata.transfer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;

import com.x.R;
import com.x.business.zerodata.client.ClientManager;
import com.x.business.zerodata.client.http.model.Transfers;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ToastUtil;
import com.x.ui.activity.zerodata.ZeroDataClientTransferActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TransferManager {

	public static TransferManager transferManager;

	public static TransferManager getInstance() {
		if (transferManager == null)
			transferManager = new TransferManager();
		return transferManager;
	}

	public boolean canDownload(Context mContext) {

		if (!StorageUtils.isSDCardPresent()) {
			ToastUtil.show(mContext, mContext.getResources().getString(R.string.sdcard_not_found), Toast.LENGTH_SHORT);
			return false;
		}

		if (!StorageUtils.isSdCardWrittenable()) {
			ToastUtil.show(mContext, mContext.getResources().getString(R.string.sdcard_cannot_use), Toast.LENGTH_SHORT);
			return false;
		}

		return true;
	}

	/** 
	* @Title: addDownload 
	* @Description: 添加下载任务 
	* @param @param mContext
	* @param @param downloadBean     
	* @return void    
	* @throws 
	*/

	public void addDownload(final Context mContext, final TransferBean transferBean) {

		if (!canDownload(mContext))
			return;
		doAddDownload(mContext, transferBean);
	}

	private void doAddDownload(final Context mContext, final TransferBean transferBean) {
		Intent downloadIntent = new Intent(TransferIntent.INTENT_DOWNLOADSERVICE);
		downloadIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.ADD);
		Bundle bundle = new Bundle();
		bundle.putParcelable(TransferIntent.DOWNLOADBEAN, transferBean);
		downloadIntent.putExtras(bundle);
		mContext.startService(downloadIntent);
	}
	
	public void addDownloads(final Context mContext, final ArrayList<TransferBean> transferList) {

		if (!canDownload(mContext))
			return;
		doAddDownloads(mContext, transferList);
	}

	private void doAddDownloads(final Context mContext, final ArrayList<TransferBean> transferList) {
		Intent downloadIntent = new Intent(TransferIntent.INTENT_DOWNLOADSERVICE);
		downloadIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.ADD_SOME);
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(TransferIntent.DOWNLOADBEAN_LIST, transferList);
		downloadIntent.putExtras(bundle);
		mContext.startService(downloadIntent);
	}

	/** 
	* @Title: deleteDownload 
	* @Description: 删除下载 
	* @param @param mContext
	* @param @param url     
	* @return void    
	* @throws 
	*/

	public void deleteDownload(Context mContext, String url) {
		Intent downloadIntent = new Intent(TransferIntent.INTENT_DOWNLOADSERVICE);
		downloadIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.DELETE);
		downloadIntent.putExtra(TransferIntent.URL, url);
		mContext.startService(downloadIntent);
		DownloadEntityManager.getInstance().deleteByUrl(url);
	}

	/** 
	* @Title: deleteAllDownload 
	* @Description: 删除所有下载 
	* @param @param mContext
	* @param @param type     
	* @return void    
	* @throws 
	*/

	public void deleteAllDownload(Context mContext) {
		Intent downloadIntent = new Intent(TransferIntent.INTENT_DOWNLOADSERVICE);
		downloadIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.DELETE_ALL_DOWNLOADING);
		mContext.startService(downloadIntent);
	}
	
	public void stopDownload(Context mContext){
		Intent downloadIntent = new Intent(TransferIntent.INTENT_DOWNLOADSERVICE);
		downloadIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.STOP);
		mContext.startService(downloadIntent);
	
	}

	/** 
	* @Title: pauseAllDownload 
	* @Description: 暂停所有下载 
	* @param @param context     
	* @return void    
	* @throws 
	*/
	public void pauseAllDownload(Context context) {
		Intent downloadIntent = new Intent(TransferIntent.INTENT_DOWNLOADSERVICE);
		downloadIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.PAUSE_ALL);
		context.startService(downloadIntent);
	}

	/** 
	* @Title: launchTransferProcess 
	* @Description: 启动传输界面 
	* @param @param context
	* @param @param data     
	* @return void    
	*/

	public void launchTransferProcess(Context context, String data, String ip) {
		sendKillActivity();
		Intent intent = new Intent(context, ZeroDataClientTransferActivity.class);
		intent.putExtra(ZeroDataClientTransferActivity.TRANSFER_DATA, data);
		intent.putExtra(ZeroDataClientTransferActivity.SERVER_IP, ip);
		context.startActivity(intent);

	}

	private void sendKillActivity() {
		Intent intent = new Intent(ZeroDataConstant.ACTION_HTTP_REQUEST_FINSISH);
		BroadcastManager.sendBroadcast(intent);
	}

	/** 
	* @Title: addDownloadList 
	* @Description: 添加多个传输任务 
	* @param @param context
	* @param @param list     
	* @return void    
	*/

	public void addDownloadList(final Context context, final List<TransferBean> list) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if (list == null)
					return;
				ArrayList<TransferBean> transferList = new ArrayList<TransferBean>();
				for (TransferBean transferBean : list) {
					if (transferBean.getFileStatus() == TransferTask.TASK_FINISH)
						continue;
					transferList.add(transferBean);
				}
				if(!transferList.isEmpty()){
					addDownloads(context, transferList);
				}
			}
		}).start();

	}

	public TransferBean get(Transfers transfers, String serverIp, Context context) {
		String fileUrl = ClientManager.getInstance(context).getServerUrl(serverIp) + transfers.getFileUrl();
		TransferBean transferBean = new TransferBean(transfers.getFileRawPath(), transfers.getFileType(),
				transfers.getFileSuffix(), transfers.getFileSize(), transfers.getFileName(),
				"", TransferTask.TASK_WAITING, fileUrl, transfers.getExAttribute());
		return transferBean;
	}

	public String getSavePath(String name) {
		int i = 0;
		String prefix = "";
		String suffix = "";
		File f;
		String path;
		if (name.contains(".")) {
			int lastdox = name.lastIndexOf(".");

			prefix = name.substring(0, lastdox);
			suffix = name.substring(lastdox, name.length());
		} else {
			prefix = name;
		}

		path = StorageUtils.FILE_ZERO_SHARE_PATH + name;
		f = new File(path);
		while (f.exists()) {
			path = StorageUtils.FILE_ZERO_SHARE_PATH + prefix + "-" + i + suffix;
			f = new File(path);
			i++;
		}
		return path;
	}

}
