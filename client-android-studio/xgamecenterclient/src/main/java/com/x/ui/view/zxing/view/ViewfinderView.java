/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.x.ui.view.zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.x.R;
import com.x.ui.view.zxing.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 */
public final class ViewfinderView extends View {
	/**
	 * 刷新界面的时间
	 */
	private static final long ANIMATION_DELAY = 10L;
	private static final int OPAQUE = 0xFF;

	/**
	 * 四个绿色边角对应的长度
	 */
	private int screenRate = 8;

	/**
	 * 四个绿色边角对应的宽度
	 */
	private int cornerWidth = 6;
	
	/**
	 * 扫描框中的中间线的宽度
	 */
	private int middleLineWidth = 5;

	/**
	 * 扫描框中的中间线的与扫描框左右的间隙
	 */
	private static final int MIDDLE_LINE_PADDING = -3;

	/**
	 * 中间那条线每次刷新移动的距离
	 */
	private static final int SPEEN_DISTANCE = 4;

	/**
	 * 手机的屏幕密度
	 */
	private static float density;
	
	/**
	 * 字体大小
	 */
	private int textSize = 16;
	
	/**
	 * 字体距离扫描框下面的距离
	 */
	private float textMarginTop = 30.0f;

	/**
	 * 画笔对象的引用
	 */
	private Paint paint;

	/**
	 * 中间滑动线的最顶端位置
	 */
	private int slideTop;

	/**
	 * 中间滑动线的最底端位置
	 */
	private int slideBottom;

	/**
	 * 将扫描的二维码拍下来，这里没有这个功能，暂时不考虑
	 */
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;

	private final int resultPointColor;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

	boolean isFirst;

	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		density = context.getResources().getDisplayMetrics().density;
		//将像素转换成dp
		//screenRate = (int)(20 * density);
		screenRate = (int) getResources().getDimension(R.dimen.screen_rate_distance);

		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);

		resultPointColor = resources.getColor(R.color.possible_result_points);
		possibleResultPoints = new HashSet<ResultPoint>(5);
	}

	@Override
	public void onDraw(Canvas canvas) {
		//中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}

		//初始化中间线滑动的最上边和最下边
		if (!isFirst) {
			isFirst = true;
			slideTop = frame.top;
			slideBottom = frame.bottom;
		}

		//获取屏幕的宽和高
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		paint.setColor(resultBitmap != null ? resultColor : maskColor);

		//画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
		//扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {

			cornerWidth = (int) getResources().getDimension(R.dimen.corner_width);

			//画扫描框边上的角，总共8个部分
			paint.setColor(getResources().getColor(R.color.canvas_rect_color));
			canvas.drawRect(frame.left, frame.top, frame.left + screenRate, frame.top + cornerWidth, paint);
			canvas.drawRect(frame.left, frame.top, frame.left + cornerWidth, frame.top + screenRate, paint);
			canvas.drawRect(frame.right - screenRate, frame.top, frame.right, frame.top + cornerWidth, paint);
			canvas.drawRect(frame.right - cornerWidth, frame.top, frame.right, frame.top + screenRate, paint);
			canvas.drawRect(frame.left, frame.bottom - cornerWidth, frame.left + screenRate, frame.bottom, paint);
			canvas.drawRect(frame.left, frame.bottom - screenRate, frame.left + cornerWidth, frame.bottom, paint);
			canvas.drawRect(frame.right - screenRate, frame.bottom - cornerWidth, frame.right, frame.bottom, paint);
			canvas.drawRect(frame.right - cornerWidth, frame.bottom - screenRate, frame.right, frame.bottom, paint);

			//绘制中间的线,每次刷新界面，中间的线往下移动SPEEN_DISTANCE
			slideTop += SPEEN_DISTANCE;
			if (slideTop >= frame.bottom) {
				slideTop = frame.top;
			}

			// 中间扫描线
			Rect lineRect = new Rect();
			middleLineWidth = (int) getResources().getDimension(R.dimen.middle_line_width);
			lineRect.left = frame.left + MIDDLE_LINE_PADDING;
			lineRect.right = frame.right - MIDDLE_LINE_PADDING;
			lineRect.top = slideTop - middleLineWidth / 2;
			lineRect.bottom = slideTop + middleLineWidth / 2;
			canvas.drawBitmap(drawable2Bitmap(getResources().getDrawable(R.drawable.ic_scanner_line)), null, lineRect,
					paint);

			//画扫描框下面的字
			paint.setColor(Color.WHITE);
			textSize = (int) getResources().getDimension(R.dimen.scan_text_size);
			paint.setTextSize(textSize);
			paint.setAlpha(0x90);
			paint.setTypeface(Typeface.create("System", Typeface.BOLD));
			paint.setTextAlign(Align.CENTER); //调整字体位置
			
			// 扫描框与文字的距离
			textMarginTop = getResources().getDimension(R.dimen.scan_text_margin_top);
			canvas.drawText(getResources().getString(R.string.scan_text), frame.centerX(),
					(float) (frame.bottom + (float) textMarginTop * density), paint);

			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints = new HashSet<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
				}
			}
			if (currentLast != null) {
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
				}
			}

			//只刷新扫描框的内容，其他地方不刷新
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);

		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

	/** 
	 * Drawable 转 bitmap 
	 * @param drawable 
	 * @return 
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
					drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}
}
