package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.net.APNUtil;
import com.hykj.gamecenter.ui.InWifiDownLoadDialog;
import com.hykj.gamecenter.ui.InWifiDownLoadDialog.KnowBtnOnClickListener;
import com.hykj.gamecenter.ui.NoWifiContinueDownloadDialog;
import com.hykj.gamecenter.ui.widget.OnWifiClickListener.NoWifiContinueDownloadListener;
import com.hykj.gamecenter.ui.widget.OnWifiClickListener.WifiDownLoadOnClickListener;
import com.hykj.gamecenter.utils.Logger;

public class DownloadListButton extends Button {
	private static final String TAG = "DownLoadButton";

	private Context mContext = null;
	private WifiDownLoadOnClickListener mListener = null;
	private KnowBtnOnClickListener kListener = null;

	private ApkDownloadManager mApkDownloadManager = null;

	public DownloadListButton(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		mApkDownloadManager = DownloadService.getDownloadManager();
		setOnClickListener(mOnclickListener);
	}

	public DownloadListButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DownloadListButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public void setWifiLoadOnClickListener(WifiDownLoadOnClickListener listener) {
		mListener = listener;
	}

	public void setKownOnClickListener(KnowBtnOnClickListener listener) {
		kListener = listener;
	}

	OnClickListener mOnclickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

				boolean bWifiToDownload = App.getSettingContent().getSettingData().bWifiToDownload;
				Logger.i(TAG, "DownloadListButton = " + bWifiToDownload, "oddshou");

				DownloadTask task = null;
				if (getTag() instanceof DownloadTask) {
					task = (DownloadTask) getTag();
				}
				if (bWifiToDownload) {
					if (APNUtil.isMobileDataEnable(mContext)/*( APNUtil.getApnType( mContext ) == APNUtil.APNTYPE_3GNET || APNUtil.getApnType( mContext ) == APNUtil.APNTYPE_3GWAP )*/) {
						if (null != task) {
							Logger.e(TAG, "DownloadListButton task.getState =  " + task.getState(), "oddshou");
							if (task.getState().equals(DownloadTask.TaskState.SUCCEEDED)) {
								mListener.onWifiClickListener(DownloadListButton.this);
							} else if (task.getState().equals(DownloadTask.TaskState.STOPPED)
									|| task.getState().equals(DownloadTask.TaskState.FAILED_SERVER)) {
								showContinueDialog(task);
							} else if (task.getState().equals(DownloadTask.TaskState.FAILED_NETWORK)) {
								showContinueDialog(task);
							} else if (task.getState().equals(DownloadTask.TaskState.LOADING)
									|| task.getState().equals(DownloadTask.TaskState.STARTED)) {
								mListener.onWifiClickListener(DownloadListButton.this);
							}
							return ;
						}
						showDialog();
					} else {
						mListener.onWifiClickListener(DownloadListButton.this);
					}
				} else {
					mListener.onWifiClickListener(DownloadListButton.this);
				}
			}
	};

//    @Override
//    public boolean onTouchEvent( MotionEvent event ) {
//		boolean result =  super.onTouchEvent( event );
//	Rect rect = new Rect( );
//	rect.left = getLeft( );
//	rect.top = getTop( );
//	rect.right = getRight( );
//	rect.bottom = getBottom( );
//
//	if( event.getAction( ) == MotionEvent.ACTION_UP
//	/* && rect.contains((int) event.getX(), (int) event.getY()) */) {
//	    boolean bWifiToDownload = App.getSettingContent( ).getSettingData( ).bWifiToDownload;
//	    Logger.i( TAG , "DownloadListButton = " + bWifiToDownload, "oddshou");
//
//	    DownloadTask task = null;
//	    if( getTag( ) instanceof DownloadTask ) {
//		task = (DownloadTask)getTag( );
//	    }
//	    if( bWifiToDownload ) {
//		if( APNUtil.isMobileDataEnable( mContext )/*( APNUtil.getApnType( mContext ) == APNUtil.APNTYPE_3GNET || APNUtil.getApnType( mContext ) == APNUtil.APNTYPE_3GWAP )*/) {
//		    if( null != task ) {
//			Logger.e( TAG , "DownloadListButton task.getState =  " + task.getState( ), "oddshou");
//			if( task.getState( ).equals( TaskState.SUCCEEDED ) ) {
//			    mListener.onWifiClickListener( this );
//			}
//			else if( task.getState( ).equals( TaskState.STOPPED )
//				|| task.getState( ).equals( TaskState.FAILED_SERVER ) ) {
//			    showContinueDialog( task );
//			}
//			else if (task.getState().equals(TaskState.FAILED_NETWORK)) {
//                showContinueDialog(task);
//            }
//			else if( task.getState( ).equals( TaskState.LOADING )
//				|| task.getState( ).equals( TaskState.STARTED ) ) {
//			    mListener.onWifiClickListener( this );
//			}
//			return result;
//		    }
//		    showDialog( );
//		}
//		else {
//		    mListener.onWifiClickListener( this );
//		}
//	    }
//	    else {
//		mListener.onWifiClickListener( this );
//	    }
//	}
//	return result;
//    }

	private void showContinueDialog(final DownloadTask dinfo) {
		NoWifiContinueDownloadDialog dialog = new NoWifiContinueDownloadDialog(mContext, R.style.MyDialog);
		dialog.setView(this);
		dialog.setNoWifiContinueDownloadListener(new NoWifiContinueDownloadListener() {

			@Override
			public void NoWifiContinueListener(View v) {
				mApkDownloadManager.resumeDownload(dinfo);
			}
		});
		dialog.show();
	}

	private void showDialog() {
		InWifiDownLoadDialog dialog = new InWifiDownLoadDialog(mContext, R.style.MyDialog);
		dialog.setView(this);
		dialog.setKnowBtnOnClickListener(kListener);
		dialog.show();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		// canvas.translate(0,canvas.getHeight( )/4 - (int)
		// this.getTextSize()/2);
		super.onDraw(canvas);
	}

}
