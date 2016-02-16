/**   
* @Title: AudioEffectManager.java
* @Package com.x.business.audio
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-6-12 下午1:38:05
* @version V1.0   
*/

package com.x.business.audio;

import android.content.Context;
import android.media.MediaPlayer;

import com.x.R;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.Utils;

/**
* @ClassName: AudioEffectManager
* @Description: 音效

* @date 2014-6-12 下午1:38:05
* 
*/

public class AudioEffectManager {
	private MediaPlayer mediaPlayer = null;
	private MediaPlayer scanResultmediaPlayer = null;
	public static AudioEffectManager audioEffectManager;

	public static AudioEffectManager getInstance() {
		if (audioEffectManager == null)
			audioEffectManager = new AudioEffectManager();
		return audioEffectManager;
	}

	public AudioEffectManager() {

	}

	/** 
	 * 初始化音乐播放器
	*/
	private void initMusicPlayer(Context context, int rawId, boolean loop) {
		releaseMusicPlayer();
		mediaPlayer = MediaPlayer.create(context, rawId);
		mediaPlayer.setLooping(loop);
	}

	private void initscanResultMusicPlayer(Context context, int rawId, boolean loop) {
		releaseMusicPlayer();
		scanResultmediaPlayer = MediaPlayer.create(context, rawId);
		scanResultmediaPlayer.setLooping(loop);
	}

	/** 
	 * 播放音乐
	*/
	private void playMusic(Context context, int rawId, boolean loop) {
		initMusicPlayer(context, rawId, loop);
		try {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				mediaPlayer.seekTo(0);
				mediaPlayer.start();
			} else {
				mediaPlayer.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	 * 播放音乐
	*/
	private void playscanResultMusic(Context context, int rawId, boolean loop) {
		initscanResultMusicPlayer(context, rawId, loop);
		try {
			if (scanResultmediaPlayer.isPlaying()) {
				scanResultmediaPlayer.pause();
				scanResultmediaPlayer.seekTo(0);
				scanResultmediaPlayer.start();
			} else {
				scanResultmediaPlayer.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** 
	* @Title: releaseMusicPlayer 
	* @Description: 释放资源 
	* @param     
	* @return void    
	*/

	public void releaseMusicPlayer() {
		try {
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
				}
				mediaPlayer.release();
				mediaPlayer = null;
			}
			if (scanResultmediaPlayer != null) {
				if (scanResultmediaPlayer.isPlaying()) {
					scanResultmediaPlayer.pause();
				}
				scanResultmediaPlayer.release();
				scanResultmediaPlayer = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** 
	* @Title: 摇一摇音效
	* @Description: TODO 
	* @param @param context    
	* @return void    
	*/

	public void playShakeAudioEffect(Context context) {
		if (Utils.getSettingModel(context).isSoundEffect())
			playMusic(context, R.raw.shake, false);
	}

	/** 
	* @Title: playCleanAudioEffect 
	* @Description: 清理音效 
	* @param @param context    
	* @return void    
	*/

	public void playCleanAudioEffect(Context context) {
		if (Utils.getSettingModel(context).isSoundEffect())
			playMusic(context, R.raw.clean, false);
	}

	/** 
	* @Title: playQrCodeAudioEffect 
	* @Description: 二维码结果音效 
	* @param @param context    
	* @return void    
	*/

	public void playQrCodeAudioEffect(Context context) {
		if (Utils.getSettingModel(context).isSoundEffect())
			playMusic(context, R.raw.qrcode, false);
	}

	/** 
	* @Title: playRefreshAudioEffect 
	* @Description: 刷新音效 
	* @param @param context    
	* @return void    
	*/

	public void playRefreshAudioEffect(Context context) {
		if (Utils.getSettingModel(context).isSoundEffect())
			playMusic(context, R.raw.refresh, false);
	}

	/** 
	* @Title: playBeginScanAudioEffect 
	* @Description: 开始零流量扫描好友音效 
	* @param @param context    
	* @return void    
	*/

	public void playBeginScanAudioEffect(Context context) {
		if (Utils.getSettingModel(context).isSoundEffect())
			playMusic(context, R.raw.scan_begin, true);
	}

	/** 
	* @Title: playScanResultAudioEffect 
	* @Description: 零流量扫描出好友音效  
	* @param @param context    
	* @return void    
	*/

	public void playScanResultAudioEffect(Context context) {
		if (Utils.getSettingModel(context).isSoundEffect())
			playscanResultMusic(context, R.raw.scan_someone, false);
	}

	/** 
	* @Title: playReceiveSuccessAudioEffect 
	* @Description: 零流量接受成功音效
	* @param @param context    
	* @return void    
	*/

	public void playReceiveSuccessAudioEffect(Context context) {
		if (Utils.getSettingModel(context).isSoundEffect())
			playMusic(context, R.raw.receive_success, false);
	}
}
