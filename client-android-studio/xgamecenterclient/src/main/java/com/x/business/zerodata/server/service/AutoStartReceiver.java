

package com.x.business.zerodata.server.service;


import com.x.business.zerodata.helper.IServiceHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.RemoteException;

public class AutoStartReceiver extends BroadcastReceiver{

	public static final String TAG = "AutoStartReceiver";


	protected IServiceHelper serviceHelper;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			serviceHelper.connect(new Runnable() {
				@Override
				public void run() {
					try {
						serviceHelper.startServer() ;
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			});
		} else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
			if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI
					) {
				serviceHelper.connect(new Runnable() {
					@Override
					public void run() {
						try {
							if (serviceHelper.getServiceController().getStatus() == ServerValues.STATUS_RUNNING) {
								serviceHelper.stopServer();
							}
							serviceHelper.startServer() ;
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

}
