package com.x.publics.utils;

import java.io.File;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.x.R;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.publics.model.RingtonesBean;

/**
 * 媒体播放工具类
 * 
 
 * 
 */
public class MediaPlayerUtil {
	private static final String TAG = "MediaPlayerUtil";
	private MediaPlayer mMediaPlayer = null;
	private static Context mContext = null;
	private static MediaPlayerUtil mediaPlayerUtil = null;
	private static Handler handler = null;
	private RingtonesBean mBean = null;
	private boolean isPrepared = false; //加载网络歌曲时，锁住状态
	private boolean isPause = false; //暂停状态
	private onRingtonesListener listener;
	private String mTag = null;

	private MediaPlayerUtil() {}
	public static MediaPlayerUtil getInstance(Context context) {
		if (mediaPlayerUtil == null) {
			mContext = context;
			handler = new Handler();
			mediaPlayerUtil = new MediaPlayerUtil();
		}
		return mediaPlayerUtil;
	}
	
	public MediaPlayer getMediaPlayer(){
		return mMediaPlayer;
	}
	
	public boolean getIsPlaying(){
		if(mMediaPlayer == null)
			return false;
		return mMediaPlayer.isPlaying();
	}
	
	public String getTag(){
		return mTag;
	}
	
	private void setTag(String tag){
		this.mTag = tag;
	}
	
	/**
	* @Title: start 
	* @Description: 开始播放 
	* @throws
	 */
	public boolean start(RingtonesBean bean) {
		//检测文件是否正常
		if(bean.getFilepath() != null){
			File file = new File(bean.getFilepath());
			if(!file.exists()){
				return false;
			}
		}
		String url = bean.getUrl() == null ? bean.getFilepath() : bean.getUrl();
		if(mBean == null){
			setTag(url);
			mBean = bean;
			prepare(bean);
		}else{
			if(url.equals(mBean.getUrl() == null ? mBean.getFilepath() : mBean.getUrl())){
				if (mMediaPlayer != null) {
					if(isPrepared){
						isPause = false;
						if(mMediaPlayer.isPlaying()){
							pause();
						}else{
							handler.post(runnable);
							mMediaPlayer.start();
							if(bean.isFromMain())
							{
								DataEyeManager.getInstance().view(StatisticConstan.FileType.RINGTONES, bean.getCategoryId(),
										bean.getMusicName(), bean.getFileSize(), null, null);		
							}
							
						}
					}else{
						Log.i(TAG, "------------loading...-----------");
					}
				}else{
					isPause = false;
					isPrepared = false;
					stop(); 
					setTag(url); //update
					mBean = bean;
					prepare(bean);
				}
			}else{
				isPause = false;
				isPrepared = false;
				stop(); 
				setTag(url); //update
				mBean = bean;
				prepare(bean);
			}
		}
		return true;
	}

	/**
	* @Title: pause 
	* @Description: 暂停播放 
	* @throws
	 */
	public void pause() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			isPause = true;
			mMediaPlayer.pause();
			listener.onRingtonesPause(mTag, mMediaPlayer.getCurrentPosition(),
					mMediaPlayer.getDuration());
//			handler.removeCallbacks(runnable);
		}
	}

	/**
	* @Title: stop 
	* @Description: 停止播放 
	* @throws
	 */
	public void stop() {
		if (mMediaPlayer != null) {
			handler.removeCallbacks(runnable);
			listener.onRingtonesStop(mTag, mBean.getDuration());
		}
	}
	
	/**
	 * 释放资源
	 */
	public void release() {
		if (mMediaPlayer != null) {
			isPause = false;
			isPrepared = false;
			mMediaPlayer.stop();
			mMediaPlayer.release();
			stop();
			mMediaPlayer = null;
		}
	}
	
	private void prepare(RingtonesBean bean) {
		// check network
		if (mBean.getFilepath() == null
				&& !NetworkUtils.isNetworkAvailable(mContext)) {
			ToastUtil.show(mContext, mContext.getResources().getString(
					R.string.network_canot_work), Toast.LENGTH_SHORT);
			return;
		}
		handler.post(runnable); //start loading
		String mPath = mBean.getFilepath() == null ? mBean.getUrl() : mBean.getFilepath();
		try {
			if (mMediaPlayer == null) {
				mMediaPlayer = new MediaPlayer();
				mMediaPlayer.setOnPreparedListener(onPreparedListener);
				mMediaPlayer.setOnErrorListener(onErrorListener);
			} else {
				// 复用
				mMediaPlayer.reset();
			}
			mMediaPlayer.setDataSource(mPath);
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.prepareAsync(); // Async
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private OnPreparedListener onPreparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			isPrepared = true;
//			listener.onRingtonesPrepareCom(mTag);
			start(mBean);
//			handler.post(runnable);
		}
	};

	private OnErrorListener onErrorListener = new OnErrorListener() {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			Log.i(TAG, "------------onErrorListener-----------");
//			stop(); // extra stop play
			return false;
		}
	};

	private Runnable runnable = new Runnable() {
		public void run() {
			if(!isPrepared){
				listener.onRingtonesLoading(mBean.getUrl());
			}else{
				if (mMediaPlayer != null) {
					if(isPause){
						listener.onRingtonesPause(mTag, mMediaPlayer.getCurrentPosition(),
								mMediaPlayer.getDuration());
					}else{
						if (mMediaPlayer.isPlaying()) {
							listener.onRingtonesPlayer(mTag, mMediaPlayer.getCurrentPosition(),
									mMediaPlayer.getDuration());
						} else {
							handler.removeCallbacks(runnable);
							stop();
							return;
						}
					}
				}
			}
			handler.postDelayed(runnable, 1000);
		}
	};

	/**
	 * 铃声监听
	 
	 * 
	 */
	public interface onRingtonesListener {
		
		/**
		 * 加载网络歌曲，Loading状态
		 */
		void onRingtonesLoading(String mTag);

		/**
		 * 播放状态
		 * @param cur 当前时长
		 * @param dur 总时长
		 */
		void onRingtonesPlayer(String mTag, int cur, int dur);

		/**
		 * 暂停
		 * @param cur 当前时长
		 * @param dur 总时长
		 */
		void onRingtonesPause(String mTag, int cur, int dur);

		/**
		 * 停止播放
		 */
		void onRingtonesStop(String mTag, int defDuration);
	}

	public void setRingtonesListener(onRingtonesListener listener) {
		this.listener = listener;
	}
}
