package com.x.ui.activity.floating;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * 
* @ClassName: RadialMenuItem
* @Description: 菜单对应Item项

* @date 2014-6-3 下午5:34:02
*
 */
public class RadialMenuItem {

	private String mMenuID;
	private Drawable mIcon;
	private Drawable mDel;
	private boolean isDel;
	private String packages = null;
	private int mBackgroundColor;
	private int mMenuNormalColor;
	private int mMenuSelectedColor;
	private RectF mBounds;
	private OnSemiCircularRadialMenuPressed mCallback;
	private int mIconDimen;
	
	public interface OnSemiCircularRadialMenuPressed {
		public void onMenuItemPressed(boolean isEdit);
	}

	/**
	 * @param mIcon
	 * @param mText
	 */
	public RadialMenuItem(String id, Drawable mIcon, Drawable mDel) {
		super();
		this.mMenuID = id;
		this.mIcon = mIcon;
		this.mMenuNormalColor = Color.WHITE;
		this.mMenuSelectedColor = Color.LTGRAY;
		this.mBackgroundColor = mMenuNormalColor;
		this.mIconDimen = mIcon.getIntrinsicWidth();
		mBounds = new RectF();
		
		this.mDel = mDel;
	}
		
	/**
	 * @return the mMenuID
	 */
	public String getMenuID() {
		return mMenuID;
	}
	
	/**
	 * @return the mIconDimen
	 */
	public int getIconDimen() {
		return mIconDimen;
	}

	/**
	 * @param mIconDimen the mIconDimen to set
	 */
	public void setIconDimen(int mIconDimen) {
		this.mIconDimen = mIconDimen;
	}

	/**
	 * @return the mBounds
	 */
	public RectF getBounds() {
		return mBounds;
	}

	/**
	 * @param mPath
	 *            the mBounds to set
	 */
	public void setMenuPath(float StartArc, float ArcWidth, float radius, Point anchorPoint) {
		int left, right, top, bottom;
		left = right = top = bottom= 0;
		//Get the drawable bounds
		Point drawableCenter = pointOnCircle((radius - (radius/4)), 
				StartArc + (ArcWidth/2),
				anchorPoint);
		
		left = (int) drawableCenter.x - (mIconDimen/2);
		top = (int) drawableCenter.y - (mIconDimen/2);
		right = left + (mIconDimen);
		bottom = top + (mIconDimen);
		mBounds = new RectF(left, top, right, bottom);
		mIcon.setBounds(left, top, right, bottom);
		//X坐标
		int del_x = right - mDel.getIntrinsicWidth() /2;
		//Y坐标
		int del_y = top + mDel.getIntrinsicHeight() /2;
		setDelPath(del_x, del_y);
	}
	
	/**
	 * 绘制小红叉 
	 */
	public void setDelPath(int x, int y){
		int left, right, top, bottom;
		left = right = top = bottom= 0;
		
		left = x - (mDel.getIntrinsicWidth()/2);
		top = y - (mDel.getIntrinsicHeight()/2);
		right = left + (mDel.getIntrinsicWidth());
		bottom = top + (mDel.getIntrinsicHeight());
		
		mDel.setBounds(left, top, right, bottom);
	}
	
	private Point pointOnCircle(float radius, float angleInDegrees, Point origin) {    
        int x = (int)(radius * Math.cos(angleInDegrees * Math.PI / 180F)) + origin.x;
        int y = (int)(radius * Math.sin(angleInDegrees * Math.PI / 180F)) + origin.y;
        return new Point(x, y);
    }
	
	/**
	 * @return the mIcon
	 */
	public Drawable getIcon() {
		return mIcon;
	}
	
	public void setIcon(Drawable drawable){
		mIcon = drawable;
	}
	
	public boolean isDel() {
		return isDel;
	}

	public void setDel(boolean isDel) {
		this.isDel = isDel;
	}

	public Drawable getmDel() {
		return mDel;
	}

	public void setmDel(Drawable mDel) {
		this.mDel = mDel;
	}

	public String getPackages() {
		return packages;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}

	/**
	 * @param mCallback the mCallback to set
	 */
	public void setOnSemiCircularRadialMenuPressed(OnSemiCircularRadialMenuPressed mCallback) {
		this.mCallback = mCallback;
	}

	/**
	 * @return the mCallback
	 */
	public OnSemiCircularRadialMenuPressed getCallback() {
		return mCallback;
	}
	
	/**
	 * @return the mColor
	 */
	public int getBackgroundColor() {
		return mBackgroundColor;
	}
	
	public void setBackgroundColor(int color) {
		this.mBackgroundColor = color;
	}

	/**
	 * @return the mMenuNormalColor
	 */
	public int getMenuNormalColor() {
		return mMenuNormalColor;
	}

	/**
	 * @param mMenuNormalColor the mMenuNormalColor to set
	 */
	public void setMenuNormalColor(int mMenuNormalColor) {
		this.mMenuNormalColor = mMenuNormalColor;
	}

	/**
	 * @return the mMenuSelectedColor
	 */
	public int getMenuSelectedColor() {
		return mMenuSelectedColor;
	}

	/**
	 * @param mMenuSelectedColor the mMenuSelectedColor to set
	 */
	public void setMenuSelectedColor(int mMenuSelectedColor) {
		this.mMenuSelectedColor = mMenuSelectedColor;
	}
}
