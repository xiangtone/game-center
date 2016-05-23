package com.hykj.gamecenter.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.hykj.gamecenter.R;

public class ProgressButtonShowStatus extends View implements OnTouchListener {

	private static final String TAG = ProgressButtonShowStatus.class.getName();
	private Paint mPaint = null;
	private Paint mPaintBg = null;
	private Paint mPaintBorder = null;
	private Paint mTextPaint = null;
	private int progress = 0;

	private String mStrText = "";
	private boolean mProgressVisiable = false;
	private float mRadius = 8f;
	private float mBorder = 1f;
	private final RectF rectProcess = new RectF();
	private final RectF rect = new RectF();
	NinePatch npMaskBmp = null;

	private Resources mRes = null;
	GradientDrawable mProgressDrawable = null;
	Bitmap mBitmap = null;
	ColorStateList mColorStateList = null;

	public ProgressButtonShowStatus(Context context) {
		super(context);
		initData(context);

	}

	public ProgressButtonShowStatus(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
	}

	public void setProgressVisiable(boolean bVisiable) {
		mProgressVisiable = bVisiable;
		invalidate();
	}

	public void setText(String strText) {
		mStrText = strText;
		invalidate();
	}

	public void setTextColor(ColorStateList colorStateList) {
		mColorStateList = colorStateList;
	}

	private void initData(Context context) {
		mRes = context.getResources();
		mRadius = mRes.getDimension(R.dimen.radius);
		mBorder = mRes.getDimension(R.dimen.border_width);
		mPaint = new Paint();
		mPaint.setColor(mRes.getColor(R.color.transparent_color_process));
		mPaint.setAntiAlias(true);

		// .9图
		mBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_greenline_mengban);
		npMaskBmp = new NinePatch(mBitmap, mBitmap.getNinePatchChunk(), null);

		// 非.9图
		/*
		 * mBitmap = ((BitmapDrawable) mRes
		 * .getDrawable(R.drawable.btn_greenline_mengban)).getBitmap();
		 */

		// mProgressDrawable = (GradientDrawable) mRes.getDrawable(
		// R.drawable.rect_progress).mutate();

		// 空心画笔描边
		mPaintBorder = new Paint();
		mPaintBorder.setAntiAlias(true);
		mPaintBorder.setColor(mRes.getColor(R.color.transparent_color_process));
		mPaintBorder.setStyle(Style.STROKE);// 空心
		mPaintBorder.setStrokeWidth(1.0f);

		// 背景Paint
		mPaintBg = new Paint();
		mPaintBg.setAntiAlias(true);
		mPaintBg.setColor(mRes.getColor(R.color.transparent));

		mTextPaint = new Paint();
		mTextPaint.setColor(mRes.getColor(R.color.color_green));
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(mRes.getDimension(R.dimen.csl_button_text_size));
		// mTextPaint.setTextAlign(Align.CENTER);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		rect.left = getLeft();
		rect.top = getTop();
		rect.right = getRight();
		rect.bottom = getBottom();

		FontMetrics fontMetrics = mTextPaint.getFontMetrics();
		// 得到字体的实际高度
		double dFontHeight = Math.ceil(fontMetrics.bottom - fontMetrics.ascent);
		double dFontWidth = 0;

		if (mProgressVisiable) {
			// 有进度但看不见时，显示进度宽为1
			int ProcessWidth = (int) ((progress * rect.width() / 100f) + 0.5f);
			ProcessWidth = progress > 0 && ProcessWidth < 1 ? 1 : ProcessWidth;
			rectProcess.set(rect.left+1.0f, rect.top+1.0f, rect.left + ProcessWidth-1.0f,
					rect.bottom-1.0f);

			/*
			 * canvas.drawRect(new RectF(rect.left + mBorder, rect.top +
			 * mBorder, rect.right - mBorder, rect.bottom - mBorder), mPaintBg);
			 */


			// 描边 btn_greenline_normal图本身带边，不用描边
//			 canvas.drawRect(rect, mPaintBorder);

			// 画背景
			canvas.drawRect(rect, mPaintBg);
			// 画进度条
			canvas.drawRect(rectProcess, mPaint);

			/*
			 * mProgressDrawable.setBounds(rect.left, rect.top, rect.right,
			 * rect.bottom); mProgressDrawable.draw(canvas);
			 */

			// canvas.drawRoundRect(new RectF(rect), mRadius, mRadius,
			// mPaintBorder);

			// 画掩码图 (如果不用掩码图，也可用九宫格画出进度条)
			/*
			 * Rect rectMask = new Rect(0, 0, mBitmap.getWidth(),
			 * mBitmap.getHeight());
			 * 
			 * //非.9图 canvas.drawBitmap(mBitmap, rectMask,rect, null);
			 */

			// .9图

//			npMaskBmp.draw(canvas, new RectF(rect.left - mBorder, rect.top
//					- mBorder, rect.right + mBorder, rect.bottom + mBorder));

			// npMaskBmp.draw(canvas, rect);
			/*
			 * Log.d(TAG, "rect.top =" + rect.top + "rect.height() = " +
			 * rect.height() + "dFontHeight = " + dFontHeight);
			 */
			/*
			 * String strProcess = progress + "%"; dFontWidth =
			 * mTextPaint.measureText(strProcess); // 画进度百分比 canvas.drawText(
			 * strProcess, (float) (rect.left + (rect.width() - dFontWidth) /
			 * 2), rect.top + (rect.height() - fontMetrics.bottom +
			 * fontMetrics.top) / 2 - fontMetrics.top, mTextPaint);
			 */
		}

		// 画出按钮文字
		if (mStrText != null) {
			dFontWidth = mTextPaint.measureText(mStrText);
			// 画进度百分比
			canvas.drawText(
					mStrText,
					(float) (rect.left + (rect.width() - dFontWidth) / 2),
					rect.top
							+ (rect.height() - fontMetrics.bottom + fontMetrics.top)
							/ 2 - fontMetrics.top, mTextPaint);
		}
		super.onDraw(canvas);
	}

	public void setProgress(int progress) {
		this.progress = progress;
		invalidate();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.d(TAG, "onTouch");
		int[] stateSet = new int[1];
		switch (event.getAction()) {
		case (MotionEvent.ACTION_DOWN):
			if (mTextPaint != null) {
				if (mColorStateList != null) {
					stateSet[0] = android.R.attr.state_pressed;
					mTextPaint.setColor(mColorStateList.getColorForState(
							stateSet, mRes.getColor(R.color.color_green)));
					Log.d(TAG,
							"mTextPaint color 1= "
									+ mColorStateList.getColorForState(
											stateSet,
											mRes.getColor(R.color.color_green)));
					invalidate();
				}
			}
			break;
		case (MotionEvent.ACTION_UP):
			if (mTextPaint != null) {
				if (mColorStateList != null) {
					stateSet[0] = android.R.attr.state_empty;
					mTextPaint.setColor(mColorStateList.getColorForState(
							stateSet, mRes.getColor(R.color.color_green)));
					Log.d(TAG,
							"mTextPaint color = "
									+ mColorStateList.getColorForState(
											stateSet,
											mRes.getColor(R.color.color_green)));
					invalidate();
				}
			}

			break;
		case (MotionEvent.ACTION_MOVE):
			if (mTextPaint != null) {
				if (mColorStateList != null) {
					stateSet[0] = android.R.attr.state_pressed;
					mTextPaint.setColor(mColorStateList.getColorForState(
							stateSet, mRes.getColor(R.color.color_green)));
					Log.d(TAG,
							"mTextPaint color = "
									+ mColorStateList.getColorForState(
											stateSet,
											mRes.getColor(R.color.color_green)));
					invalidate();
				}
			}

			break;
		default:
			resetValues();
			break;
		}
		return false;

	}

	private void resetValues() {
		mTextPaint.setColor(mRes.getColor(R.color.color_green));
		invalidate();
	}

}