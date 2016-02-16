package com.zbar.lib;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.audio.AudioEffectManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.business.zerodata.connection.manager.ConnectHotspotManage;
import com.x.publics.utils.Constan;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.scan.ScanResultActivity;
import com.x.ui.activity.zerodata.ZeroDataClientActivity;
import com.x.ui.activity.zerodata.ZeroDataQrConnectingActivity;
import com.x.ui.activity.zerodata.ZeroDataShareActivity;
import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;
import com.zbar.lib.decode.InactivityTimer;

public class CaptureActivity extends BaseActivity implements Callback, OnClickListener {

	private CaptureActivityHandler handler;
	private boolean hasSurface;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.50f;
	private boolean vibrate;
	private int x = 0;
	private int y = 0;
	private int cropWidth = 0;
	private int cropHeight = 0;
	private FrameLayout mContainer = null;
	private RelativeLayout mCropLayout = null;
	private boolean isNeedCapture = false;
	private Context context;
	private String fromActivity = null;
	private TextView scanTipsTv;
	private ImageView mQrLineView;
	private TranslateAnimation mAnimation;
	private boolean initingCamera;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	public boolean isNeedCapture() {
		return isNeedCapture;
	}

	public void setNeedCapture(boolean isNeedCapture) {
		this.isNeedCapture = isNeedCapture;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		fromActivity = getIntent().getStringExtra("fromActivity");
		setTabTitle(R.string.page_qr_scan);
		setContentView(R.layout.activity_qr_scan);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		initUi();
		initNavigation();
	}

	/**
	* @Title: initNavigation 
	* @Description: 初始化导航栏 
	* @param     
	* @return void
	 */
	private void initNavigation() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
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

	private void initUi() {
		mContainer = (FrameLayout) findViewById(R.id.capture_containter);
		mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);
		scanTipsTv = (TextView) findViewById(R.id.aqs_tips);

		mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
		mAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
		mAnimation.setDuration(1500);
		mAnimation.setRepeatCount(-1);
		mAnimation.setRepeatMode(Animation.REVERSE);
		mAnimation.setInterpolator(new LinearInterpolator());
	}

	boolean flag = true;

	protected void light() {
		if (flag == true) {
			flag = false;
			// 开闪光灯
			CameraManager.get().openLight();
		} else {
			flag = true;
			// 关闪光灯
			CameraManager.get().offLight();
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		try {
			if (mQrLineView != null)
				mQrLineView.setVisibility(View.INVISIBLE);
			if (scanTipsTv != null)
				scanTipsTv.setText(R.string.scan_prepare);
			// 初始化 CameraManager
			CameraManager.init(getApplication());

			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			if (hasSurface) {
				initingCamera = true;
				initCamera(surfaceHolder);
			} else {
				surfaceHolder.addCallback(CaptureActivity.this);
				surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			}
			DataEyeManager.getInstance().module(ModuleName.QR_CODE, true);
		} catch (Exception e) {
			e.printStackTrace();
			errorExit();
		}

	}

	/**
	 * 
	 * @Title: errorExit
	 * @Description: 异常退出
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void errorExit() {
		initingCamera = false;
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ToastUtil.show(context, ResourceUtil.getString(context, R.string.init_camera_error),
						ToastUtil.LENGTH_SHORT);
			}
		});
		onBackPressed();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
		if (mQrLineView != null && mAnimation != null) {
			mQrLineView.setVisibility(View.INVISIBLE);
			mQrLineView.clearAnimation();
		}
		DataEyeManager.getInstance().module(ModuleName.QR_CODE, false);
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	public void handleDecode(String result) {
		inactivityTimer.onActivity();
		// playBeepSoundAndVibrate();
		// Toast.makeText(getApplicationContext(), result,
		// Toast.LENGTH_SHORT).show();

		// 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
		// handler.sendEmptyMessage(R.id.restart_preview);

		// 扫描结果
		String resultString = result;

		if (resultString.equals("")) {
			ToastUtil.show(this, ResourceUtil.getString(this, R.string.scan_failure_tips), Toast.LENGTH_SHORT);
			CaptureActivity.this.finish();
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
			CaptureActivity.this.finish();
			return;
		}

		// 弹框处理
		Intent resultIntent = new Intent(this, ScanResultActivity.class);
		resultIntent.putExtra("result", resultString);
		startActivity(resultIntent);
		CaptureActivity.this.finish();
	}

	private void initCamera(final SurfaceHolder surfaceHolder) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					CameraManager.get().openDriver(surfaceHolder);

					Point point = CameraManager.get().getCameraResolution();
					int width = point.y;
					int height = point.x;

					int x = mCropLayout.getLeft() * width / mContainer.getWidth();
					int y = mCropLayout.getTop() * height / mContainer.getHeight();

					int cropWidth = mCropLayout.getWidth() * width / mContainer.getWidth();
					int cropHeight = mCropLayout.getHeight() * height / mContainer.getHeight();

					setX(x);
					setY(y);
					setCropWidth(cropWidth);
					setCropHeight(cropHeight);
					// 设置是否需要截图
					setNeedCapture(true);
				} catch (Exception e) {
					errorExit();
					return;
				}
				if (handler == null) {
					Looper.prepare();
					handler = new CaptureActivityHandler(CaptureActivity.this);
					initingCamera = false;
					Looper.loop();
				}
			}
		}).start();

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

	public Handler getHandler() {
		return handler;
	}

	public Handler uiHanlder = new Handler() {
		public void handleMessage(android.os.Message msg) {
			initCameraSuccess();
		};
	};

	public void initCameraSuccess() {
		if (scanTipsTv != null) {
			scanTipsTv.setText(R.string.scan_text);
		}
		if (mQrLineView != null && mAnimation != null) {
			mQrLineView.setVisibility(View.VISIBLE);
			mQrLineView.setAnimation(mAnimation);
		}
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
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

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void onBackPressed() {
		if (initingCamera)
			return;
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
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, mCropLayout, SkinConstan.SCANNER_FRAME);
		SkinConfigManager.getInstance().setViewBackground(context, mQrLineView, SkinConstan.SCANNER_LINE);
	}
}