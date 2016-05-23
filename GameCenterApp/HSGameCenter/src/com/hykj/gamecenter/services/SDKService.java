package com.hykj.gamecenter.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SDKService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return new SDKServiceInterfaceImpl(this);
	}

}
