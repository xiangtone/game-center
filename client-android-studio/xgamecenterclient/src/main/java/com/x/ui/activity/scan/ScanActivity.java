package com.x.ui.activity.scan;

import java.io.IOException;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.x.R;
import com.x.business.audio.AudioEffectManager;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.business.zerodata.connection.manager.ConnectHotspotManage;
import com.x.publics.utils.Constan;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.zerodata.ZeroDataClientActivity;
import com.x.ui.activity.zerodata.ZeroDataQrConnectingActivity;
import com.x.ui.activity.zerodata.ZeroDataShareActivity;
import com.x.ui.view.zxing.camera.CameraManager;
import com.x.ui.view.zxing.decoding.CaptureActivityHandler;
import com.x.ui.view.zxing.decoding.InactivityTimer;
import com.x.ui.view.zxing.view.ViewfinderView;

/**
 * @ClassName: ScanActivity
 * @Desciption: 二维码扫描
 
 * @Date: 2014-3-10 上午11:15:02
 */

public class ScanActivity extends BaseActivity implements Callback, OnClickListener {

	private MediaPlayer mediaPlayer;
	private ViewfinderView viewfinderView;
	private CaptureActivityHandler handler;
	private InactivityTimer inactivityTimer;
	private Vector<BarcodeFormat> decodeFormats;

	private boolean vibrate;
	private boolean playBeep;
	private boolean hasSurface;
	private String characterSet;
	private Context context = this;
	private String fromActivity = null;
	private static final float BEEP_VOLUME = 0.10f;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTabTitle(R.string.page_qr_scan);
		setContentView(R.layout.activity_scan);

		initNavigation();
		fromActivity = getIntent().getStringExtra("fromActivity");
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					CameraManager.init(getApplication());
					viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
					hasSurface = false;
					inactivityTimer = new InactivityTimer(ScanActivity.this);
				} catch (Exception e) {
					errorExit();
				}
			}
		}).start();

	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			if (hasSurface) {
				initCamera(surfaceHolder);
			} else {
				surfaceHolder.addCallback(this);
				surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			}
			decodeFormats = null;
			characterSet = null;

			playBeep = true;
			AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
			if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
				playBeep = false;
			}
			initBeepSound();
			vibrate = true;
			DataEyeManager.getInstance().module(ModuleName.QR_CODE, true);
		} catch (Exception e) {
			errorExit();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
		DataEyeManager.getInstance().module(ModuleName.QR_CODE, false);
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 扫描结果
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		//		playBeepSoundAndVibrate();

		// 扫描结果
		String resultString = result.getText();
		if (resultString.equals("")) {
			ToastUtil.show(this, ResourceUtil.getString(this, R.string.scan_failure_tips), Toast.LENGTH_SHORT);
			ScanActivity.this.finish();
			return;
		}

		AudioEffectManager.getInstance().playQrCodeAudioEffect(context);
		// 零流量分享
		if (resultString.contains(Constan.ZERO_START)) {

			resultString = resultString.substring(Constan.ZERO_START.length(), resultString.length());

			if (ZeroDataClientActivity.class.getName().equals(fromActivity)) {
				ConnectHotspotManage.getInstance(this).saveNetworkState(this);
			}
			Intent intent = new Intent(this, ZeroDataQrConnectingActivity.class);
			intent.putExtra("SSID", resultString);
			startActivity(intent);
			ScanActivity.this.finish();
			return;
		}

		// 弹框处理
		Intent resultIntent = new Intent(this, ScanResultActivity.class);
		resultIntent.putExtra("result", resultString);
		startActivity(resultIntent);
		ScanActivity.this.finish();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void onBackPressed() {
		if (!TextUtils.isEmpty(fromActivity)) {
			ConnectHotspotManage.getInstance(this).resumeClientNetwork();
			startActivity(new Intent(this, ZeroDataShareActivity.class));
		}
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			onBackPressed();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 
	* @Title: errorExit 
	* @Description: 异常退出
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void errorExit() {
		ToastUtil.show(context, ResourceUtil.getString(context, R.string.init_camera_error), ToastUtil.LENGTH_SHORT);
		onBackPressed();
	}

	/**
	* @Title: initNavigation 
	* @Description: 初始化导航栏 
	* @param     
	* @return void
	 */
	private void initNavigation() {
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mTitleTv.setText(R.string.page_qr_scan);
		mNavigationView.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.mh_navigate_ll:
			onBackPressed();
			break;

		default:
			break;
		}

	}
}