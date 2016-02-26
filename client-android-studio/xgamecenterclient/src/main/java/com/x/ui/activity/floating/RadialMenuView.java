package com.x.ui.activity.floating;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.x.R;
import com.x.ui.view.floating.FloatPreferebce;
import com.x.ui.view.floating.FloatPreferebce.MConstants;

/**
 * 
* @ClassName: RadialMenu
* @Description: 扇型菜单View

* @date 2014-5-23 下午2:49:17
*
 */
public class RadialMenuView extends View {
	private Context context = null;
	//数据存储
	private FloatPreferebce	mfloatPreferebce = null;
	private int mDiameter = 0;	
	private float mRadius = 0.0f;	
	//起点角度
	private int mStartAngle = 0;	
	private RectF mMenuRect;
	private RectF mMenuButtonRect;	
	private Paint mRadialMenuPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Point mViewAnchorPoints;	
	private int mOrientation;	
	//背景色
	private int mBgColor = 0x50000000;//Color.BLACK;//Color.GRAY;	
    private static final int TOUCH_SLOP = 50;  //至少包含图标的2/1
    //是否编辑状态
    private boolean isEditStatus = false;
    //首次编辑
    private boolean firstEdit = false; 
    private int mLastMotionX = 0;  
    private int mLastMotionY = 0;
	private String mPressedMenuItemID = null;
	private String mLastMenuItemID = null;
	private Drawable drawable = null;
	private HashMap<String, RadialMenuItem> mMenuItems = new HashMap<String, RadialMenuItem>();
	private onCloseView closeView;
	private onMenuButtonClick buttonClick;
	private long lastTime = 0; //item点击时间，避免快速连点
	private final long INTERVAL = 600; //间隔600毫秒
	
	public RadialMenuView(Context context) {
		super(context);
		init(context); 
	}
	public RadialMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public RadialMenuView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/*
	 * 初始化
	 */
	private void init(Context context) {
		this.context = context;
		mfloatPreferebce = FloatPreferebce.getInstance(context);
		mOrientation = mfloatPreferebce.getOrientation();
		mRadialMenuPaint.setDither(true);
		mRadialMenuPaint.setAntiAlias(true);
		mRadialMenuPaint.setColor(mBgColor);
		drawable = context.getResources().getDrawable(R.drawable.desktop_search_normal);
		vRecord = new VelocityRecord();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//默认大小：(半径)=屏幕的3分之1
		if(w > h) {
			mDiameter = h;
			mRadius = mDiameter/3;
		} else {
			mDiameter = w;
			mRadius = mDiameter/3;
		}
		//Init the draw arc Rect object
		mMenuRect = getRadialMenuRect(false);
		mMenuButtonRect = getRadialMenuRect(true);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawArc(mMenuRect, mStartAngle, 180, true, mRadialMenuPaint);
		//See if there is any item in the collection
		if(mMenuItems.size() > 0) {
			//Get the sweep angles based on the number of menu items
			float mSweep = 360/mMenuItems.size();
			for(RadialMenuItem item : mMenuItems.values()) {
				item.setMenuPath(deta_degree, mSweep, 
						mRadius, mViewAnchorPoints);
				item.getIcon().draw(canvas); // Draw Icon
				if(isEditStatus && item.getPackages() != null){
					item.getmDel().draw(canvas);
				}
				deta_degree += mSweep;
			}
		}
		drawable.setBounds((int)mMenuButtonRect.left, (int)mMenuButtonRect.top, 
				(int)mMenuButtonRect.right, (int)mMenuButtonRect.bottom);
		drawable.draw(canvas);
	}
    
    private Runnable mLongPressRunnable = new Runnable() {
		
		@Override
		public void run() {
			isEditStatus = true;
			firstEdit = true;
			mLastMenuItemID = null; //清除
			invalidate();
		}
	};
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//菜单
            down_x = event.getX();
            down_y = event.getY();
            current_degree = detaDegree(mMenuRect.centerX(), 
            		mMenuRect.centerY(), down_x, down_y);
            vRecord.reset();
            a = a_max;
			//点击处理
			mPressedMenuItemID = null;
			mLastMotionX = x;  
            mLastMotionY = y; 
			if(mMenuItems.size() > 0) {
				if(mMenuButtonRect.contains(mLastMotionX, mLastMotionY)){
					drawable = context.getResources().getDrawable(R.drawable.desktop_search_press);
					invalidate();
				}
				for(RadialMenuItem item : mMenuItems.values()) {
					if(mMenuRect.contains((int) x, (int) y)){
						if(item.getBounds().contains((int) x, (int) y)) {
							mPressedMenuItemID = item.getMenuID();
							break;
						}
					}
				}
				if(mPressedMenuItemID != null){
					if(mMenuItems.get(mPressedMenuItemID).getPackages() == null){
						mMenuItems.get(mPressedMenuItemID).setIcon(
								getResources().getDrawable(R.drawable.desktop_add_press));
						invalidate();
					}else{
						if(!isEditStatus){
							isEditStatus = false;
				            postDelayed(mLongPressRunnable, ViewConfiguration.getLongPressTimeout());
						}
					}
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
            if(Math.abs(mLastMotionX-x) > TOUCH_SLOP   
                    || Math.abs(mLastMotionY-y) > TOUCH_SLOP) {  
                removeCallbacks(mLongPressRunnable);  
                //释放，恢复图标
    			if(! mMenuButtonRect.contains((int) x, (int) y)){
    				drawable = context.getResources().getDrawable(R.drawable.desktop_search_normal);
    				invalidate();
    			}
				if(mPressedMenuItemID != null){
					if(mMenuItems.get(mPressedMenuItemID).getPackages() == null){
						mMenuItems.get(mPressedMenuItemID).setIcon(
								getResources().getDrawable(R.drawable.desktop_add_normal));
						invalidate();
					}else{
						//恢复图标。。。。
					}
				}
				mPressedMenuItemID = null;
				//菜单
				if (mMenuRect.contains((int) x, (int) y)) {
					down_x = target_x = event.getX();
					down_y = target_y = event.getY();
					float degree = detaDegree(mMenuRect.centerX(), mMenuRect.centerY(), 
							target_x, target_y);
					float dete = degree - current_degree;
					if (dete < -270) {
						dete = dete + 360;
					} else if (dete > 270) {
						dete = dete - 360;
					}
					lastMoveTime = System.currentTimeMillis();
					vRecord.add(dete, lastMoveTime);
					updateDegree(dete);
					current_degree = degree;
					postInvalidate();
				}
            } 
			break;
		case MotionEvent.ACTION_UP:
            a = a_min;
            speed = speed + vRecord.getSpeed();
            if (speed > 0) {
                speed = Math.min(VelocityRecord.max_speed, speed);
            } else {
                speed = Math.max(-VelocityRecord.max_speed, speed);
            }
            if (speed > 0) {
                isClockWise = true;
            } else {
                isClockWise = false;
            }
            currentTime = System.currentTimeMillis();
            handler.sendEmptyMessage(play);
            //抬起
            removeCallbacks(mLongPressRunnable);
			if(mMenuButtonRect.contains(mLastMotionX, mLastMotionY)){
				drawable = context.getResources().getDrawable(R.drawable.desktop_search_normal);
				buttonClick.onMenuButton();
				invalidate();
			}
			if(mPressedMenuItemID != null && !firstEdit){
				if(isEditStatus && mMenuItems.get(mPressedMenuItemID).getPackages() != null){ //编辑状态
					if(mMenuItems.get(mPressedMenuItemID).getCallback() != null) {
						mMenuItems.get(mPressedMenuItemID).getCallback().onMenuItemPressed(true);
					}
				}else{//单击状态
					if(mMenuItems.get(mPressedMenuItemID).getCallback() != null) {
						if(mLastMenuItemID != null) {
							if(mLastMenuItemID.equals(mPressedMenuItemID)){
								long interval = System.currentTimeMillis() - lastTime;
								if(interval < INTERVAL){
									if(mMenuItems.get(mPressedMenuItemID).getPackages() == null){
										mMenuItems.get(mPressedMenuItemID).setIcon(
												getResources().getDrawable(R.drawable.desktop_add_normal));
										invalidate();
									}else{
										//添加图标点击效果
									}
									break;
								}
							}
						}
						mMenuItems.get(mPressedMenuItemID).getCallback().onMenuItemPressed(false);
						mLastMenuItemID = mPressedMenuItemID;
						lastTime = System.currentTimeMillis();  
					}
					if(mMenuItems.get(mPressedMenuItemID).getPackages() == null){
						mMenuItems.get(mPressedMenuItemID).setIcon(
								getResources().getDrawable(R.drawable.desktop_add_normal));
						invalidate();
					}else{
						//添加图标点击效果
					}
				}	
			}else if(!firstEdit){ 
				if(! mMenuRect.contains(mLastMotionX, mLastMotionY)){
					if(isEditStatus){
						setEditStatus(false);
					}else{
						closeView.closeView();
					}
				}
			}
			mLastMotionX = 0;  
            mLastMotionY = 0; 
            firstEdit = false;
			break;
		}
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(isEditStatus){
				setEditStatus(false);
			}else{
				closeView.closeView();
			}
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * Get the arc drawing rects
	 * @param isCenterButton
	 * @return
	 */
	private RectF getRadialMenuRect(boolean isButtom) {
		int left, right, top, bottom;
		left = right = top = bottom = 0;
		int height = getHeight();
		float y = FloatPreferebce.getInstance(context).getFloatY();
		switch(mOrientation) {
		case MConstants.VERTICAL_RIGHT:
			if(y >= mRadius && y <= (height - mRadius)){
				left = getWidth() - (int) mRadius;
				right = getWidth() + (int) mRadius;
				top = (int) y - (int) mRadius;
				bottom = (int) y + (int) mRadius;
				mViewAnchorPoints = new Point(getWidth(), (int) y);
			}else{
				int tempPoints = 0;
				if(y > (height / 2)){
					tempPoints = height;
					left = getWidth() - (int) mRadius;
					right = getWidth() + (int) mRadius;
					top = tempPoints - (int) mRadius * 2;
					bottom = tempPoints;
					mViewAnchorPoints = new Point(getWidth(), (tempPoints - (int) mRadius));
				}else{
					tempPoints = 0;
					left = getWidth() - (int) mRadius;
					right = getWidth() + (int) mRadius;
					top = tempPoints;
					bottom = (int) mRadius * 2;
					mViewAnchorPoints = new Point(getWidth(), tempPoints + (int) mRadius);
				}
			}
			if(isButtom){
				int center = top + (int) mRadius;
				left = getWidth() - drawable.getIntrinsicWidth();
				right = getWidth();
				top = center - (drawable.getIntrinsicHeight() / 2);
				bottom = top + drawable.getIntrinsicHeight();
				mMenuButtonRect = new RectF(left, top, right, bottom);
			}
			mStartAngle = 90;
			break;
		case MConstants.VERTICAL_LEFT:
			if(y >= mRadius && y <= (height - mRadius)){
				left = -(int) mRadius;
				right = (int) mRadius;
				top = (int) y - (int) mRadius;
				bottom = (int) y + (int) mRadius;
				mViewAnchorPoints = new Point(0, (int) y);
			}else{
				int tempPoints = 0;
				if(y > (height / 2)){
					tempPoints = height;
					left = -(int) mRadius;
					right = (int) mRadius;
					top = tempPoints - (int) mRadius * 2;
					bottom = tempPoints;
					mViewAnchorPoints = new Point(0, (tempPoints - (int) mRadius));
				}else{
					tempPoints = 0;
					left = -(int) mRadius;
					right = (int) mRadius;
					top = tempPoints;
					bottom = (int) mRadius * 2;
					mViewAnchorPoints = new Point(0, tempPoints + (int) mRadius);
				}
			}
			if(isButtom){
				int center = top + (int) mRadius;
				left = 0;
				right = drawable.getIntrinsicWidth();
				top = center - (drawable.getIntrinsicHeight() / 2);
				bottom = top + drawable.getIntrinsicHeight();
				mMenuButtonRect = new RectF(left, top, right, bottom);
			}
			mStartAngle = 270;
			break;
		}
		float defree = mfloatPreferebce.getDegree();
		if(defree != -1){
			deta_degree = defree;
		}else{
			deta_degree = mStartAngle;
		}
		Rect rect = new Rect(left, top, right, bottom);
		return new RectF(rect); 
	}
	
    /**
     * 更新弧度
     */
    private void updateDegree(float added) {
        deta_degree += added;
        if (deta_degree > 360 || deta_degree < -360) {
            deta_degree = deta_degree % 360;
        }
    }
	
    private float detaDegree(float src_x, float src_y, float target_x, float target_y) {
        float detaX = target_x - src_x;
        float detaY = target_y - src_y;
        double d;
        if (detaX != 0) {
            float tan = Math.abs(detaY / detaX);
            if (detaX > 0) {
                if (detaY >= 0) {
                    d = Math.atan(tan);
                } else {
                    d = 2 * Math.PI - Math.atan(tan);
                }
            } else {
                if (detaY >= 0) {
                    d = Math.PI - Math.atan(tan);
                } else {
                    d = Math.PI + Math.atan(tan);
                }
            }
        } else {
            if (detaY > 0) {
                d = Math.PI / 2;
            } else {
                d = -Math.PI / 2;
            }
        }
        return (float)((d * 180) / Math.PI);
    }
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            double detaTime = System.currentTimeMillis() - currentTime;
            switch (msg.what) {
                case play: {
                    if (isClockWise) {
                        speed = speed - a * detaTime;
                        if (speed <= 0) {
                        	mfloatPreferebce.setDegree(deta_degree);
                            return;
                        } else {
                            handler.sendEmptyMessageDelayed(play, delayedTime);
                        }
                    } else {
                        speed = speed + a * detaTime;
                        if (speed >= 0) {
                        	mfloatPreferebce.setDegree(deta_degree);
                            return;
                        } else {
                            handler.sendEmptyMessageDelayed(play, delayedTime);
                        }
                    }
                    updateDegree((float)(speed * detaTime + (a * detaTime * detaTime) / 2));
                    currentTime = System.currentTimeMillis();
                    invalidate();
                    break;
                }
                case stop: {
                    speed = 0;
                    handler.removeMessages(play);
                }
            }
            super.handleMessage(msg);
        }
    };
    
	/*****************************************************
	 * Variable
	 *****************************************************/
    private float down_x;
    private float down_y;
    private float target_x;
    private float target_y;
    /**
     * 当前的弧度
     */
    private float current_degree;

    /**
     * 圆盘所转的弧度
     */
    private float deta_degree;

    private double lastMoveTime = 0;
    private final float a_min = 0.001f;

    /**
     * 加速度
     */
    private float a = a_min;
    private final float a_max = a_min * 5;
    private double speed = 0;
    private VelocityRecord vRecord;

    /**
     * 旋转方向
     */
    private boolean isClockWise;
    private int delayedTime = 20;
    private final int play = 0;
    private final int stop = 1;
    private double currentTime = 0;
	
	/**
	 * 速度
	 **/
    private class VelocityRecord {
    	private static final double max_speed = 0.6;
        int addCount;
        private static final int length = 8;
        double[][] record = new double[length][2];

        public void add(double detadegree, double time) {
            for (int i = length - 1; i > 0; i--) {
                record[i][0] = record[i - 1][0];
                record[i][1] = record[i - 1][1];
            }
            record[0][0] = detadegree;
            record[0][1] = time;
            addCount++;
        }

        public double getSpeed() {
            if (addCount == 0) {
                return 0;
            }
            int maxIndex = Math.min(addCount, length) - 1;

            if ((record[0][1] - record[maxIndex][1]) == 0) {
                return 0;
            }

            double detaTime = record[0][1] - record[maxIndex][1];
            double sumdegree = 0;
            for (int i = 0; i < length - 1; i++) {
                sumdegree += record[i][0];
            }
            double result = sumdegree / detaTime;
            if (result > 0) {
                return Math.min(result, max_speed);
            } else {
                return Math.max(result, -max_speed);
            }
        }

        public void reset() {
            addCount = 0;
            for (int i = length - 1; i > 0; i--) {
                record[i][0] = 0;
                record[i][1] = 0;
            }
        }
    }
	
	/*****************************************************
	 * Interface
	 *****************************************************/
	public interface onCloseView{
		public void closeView();
	}
	/**
	 * 中心按钮单击
	 */
	public interface onMenuButtonClick{
		public void onMenuButton();
	}
	
	/*****************************************************
	 * Getter and setter methods
	 *****************************************************/
	/**
	 * Set the orientation the semi-circular radial menu.
	 * There are four possible orientations only
	 * VERTICAL_RIGHT , VERTICAL_LEFT
	 * @param orientation
	 */
	public void setOrientation(int orientation) {
		mOrientation = orientation;
		mMenuRect = getRadialMenuRect(false);
		mMenuButtonRect = getRadialMenuRect(true);
		invalidate();
	}
	
	/**
	 * @return the mOrientation
	 */
	public int getOrientation() {
		return mOrientation;
	}
	
	/**
	 * Add a menu item with it's identifier tag
	 * @param idTag - Menu item identifier id
	 * @param mMenuItem - RadialMenuItem object
	 */
	public void addMenuItem(String idTag, RadialMenuItem mMenuItem) {
		mMenuItems.put(idTag, mMenuItem);
		invalidate();
	}
	
	/**
	 * Remove a menu item with it's identifier tag
	 * @param idTag  - Menu item identifier id
	 */
	public void removeMenuItemById(String idTag) {
		mMenuItems.remove(idTag);
		invalidate();
	}
	
	/**
	 * Remove a all menu items
	 */
	public void removeAllMenuItems() {
		mMenuItems.clear();
		invalidate();
	}
	
	/**
	 * Update view
	 */
	public void updateView(){
		invalidate();
	}
	
	public boolean getEditStatus() {
		return isEditStatus;
	}
	/**
	 * 设置显示模式，true=编辑,false=正常
	 */
	public void setEditStatus(boolean isEditStatus) {
		this.isEditStatus = isEditStatus;
		invalidate();
	}
	
	/**
	 * 关闭窗口
	 */
	public void setOnCloseViewListener(onCloseView closeView){
		this.closeView = closeView;
	}
	
	/**
	 * 中心按钮单击监听
	 */
	public void setonMenuButtonListener(onMenuButtonClick buttonClick){
		this.buttonClick = buttonClick;
	}
}
