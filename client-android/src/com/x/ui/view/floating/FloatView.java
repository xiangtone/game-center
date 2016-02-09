package com.x.ui.view.floating;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.x.R;
import com.x.ui.view.floating.FloatPreferebce.MConstants;

/**
 * 
* @ClassName: FloatView
* @Description: 自定义浮动窗

* @date 2014-5-20 下午4:27:20
 *
 */
public class FloatView extends ImageView {
	private int mOrientation = MConstants.VERTICAL_LEFT; //默认左边
	private float mTouchX;
	private float mTouchY;
	private float x;
	private float y;
	private OnClickListener mClickListener;
	private onTouchViewListener listener;
	private boolean isMove = false;
	private int defaultResource = R.drawable.desktop_btn;
	//数据存储
	private FloatPreferebce	mfloatPreferebce = null;
	
	public FloatView(Context context) {
		super(context);
		setImageResource(defaultResource);
		isMove = false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mfloatPreferebce = FloatPreferebce.getInstance(getContext());
		Rect frame = new Rect();  
		getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		x = event.getRawX();
		y = event.getRawY() - statusBarHeight; //去除状态栏高度
		int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
//		int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mTouchX = event.getX();
			mTouchY = event.getY();
			isMove = false;
			break;
		case MotionEvent.ACTION_MOVE:
			if(x > getWidth() && (screenWidth - x) > getWidth()) {
				isMove = true;
				listener.onTouch((int) (x - mTouchX), (int) (y - mTouchY));
			}else{
				float diff = 0;
				if(mTouchY > event.getY()){
					diff = mTouchY - event.getY();
				}else{
					diff = event.getY() - mTouchY;
				}
				if(diff > 10){
					isMove = true;
					listener.onTouch((int) (x - mTouchX), (int) (y - mTouchY));
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if(isMove) {
				isMove = false;
				float halfScreenX = screenWidth/2;
				if(x <= halfScreenX) {
					mOrientation = MConstants.VERTICAL_LEFT;
					x = 0;
				} else {
					mOrientation = MConstants.VERTICAL_RIGHT;
					x = screenWidth;
				}
				listener.onTouch((int) (x - mTouchX), (int) (y - mTouchY));
				mfloatPreferebce.setFloatX(x); 
				mfloatPreferebce.setFloatY(y);
				mfloatPreferebce.setOrientation(mOrientation);
			} else {
				if (mClickListener != null) {
					mClickListener.onClick(this);
				}
			}
			mTouchX = mTouchY = 0;
			break;
		}
		return true;
	}
	
	@Override
	public void setOnClickListener(OnClickListener clickListener) {
		this.mClickListener = clickListener;
	}
	
	public void setOnTouchViewListener(onTouchViewListener touchViewListener) {
		listener = touchViewListener;
	}
	
	public interface onTouchViewListener{
		void onTouch(int x, int y);
	}
	
	public int getOrientation(){
		return mOrientation;
	}
	
	public float getTouchY(){
		return y;
	}
}
