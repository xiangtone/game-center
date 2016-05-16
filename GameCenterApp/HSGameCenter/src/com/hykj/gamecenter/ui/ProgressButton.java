package com.hykj.gamecenter.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
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

public class ProgressButton extends View implements OnTouchListener {

	private static final String TAG = ProgressButton.class.getName();
	private Paint mPaint = null;
	private Paint mPaintBg = null;
	private Paint mPaintBorder = null;
	private Paint mTextPaint = null;
	private int progress = 0;

	private float mRadius = 8f;
	private float mBorder = 1f;
	private final RectF rectProcess = new RectF();
	private final RectF rect = new RectF();
	NinePatch npMaskBmp = null;

	private Resources mRes = null;
	GradientDrawable mProgressDrawable = null;
	Bitmap mBitmap = null;
	ColorStateList mColorStateList = null;

	public ProgressButton(Context context) {
		super(context);
		initData(context);

	}

	public ProgressButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
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
//		mBitmap = BitmapFactory.decodeResource(getResources(),
//				R.drawable.btn_greenline_normal);
//		npMaskBmp = new NinePatch(mBitmap, mBitmap.getNinePatchChunk(), null);

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
		mPaintBorder.setColor(mRes.getColor(R.color.color_green));
		mPaintBorder.setStyle(Style.STROKE);// 空心
		mPaintBorder.setStrokeWidth(1.0f);

		// 背景Paint
		mPaintBg = new Paint();
		mPaintBg.setAntiAlias(true);
		mPaintBg.setColor(mRes.getColor(R.color.white));

		mTextPaint = new Paint();
		mTextPaint.setColor(mRes.getColor(R.color.color_green));
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(mRes.getDimension(R.dimen.csl_button_text_size));
		// mTextPaint.setTextAlign(Align.CENTER);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		/*
		 * 画圆角矩形方法1：优点：可显示背景色 Path mPath = new Path(); mPath.addRoundRect
		 * canvas.clipPath(mPath, Region.Op.INTERSECT);
		 * //DIFFERENCE是第一次不同于第二次的部分显示出来 //REPLACE是显示第二次的 //REVERSE_DIFFERENCE
		 * 是第二次不同于第一次的部分显示 //INTERSECT交集显示 //UNION全部显示 //XOR补集 就是全集的减去交集生育部分显示
		 */

		/*
		 * 画圆角矩形方法2：优点：可显示背景色 Bitmap bitmap = Bitmap.createBitmap(10, 100,
		 * Bitmap.Config.ARGB_8888); Canvas c = new Canvas(bitmap);
		 * super.dispatchDraw(c);//图像渲染 BitmapShader shader = new
		 * BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		 * 
		 * Paint paint = new Paint(); paint.setAntiAlias(true);
		 * paint.setShader(shader);
		 * 
		 * canvas.drawRoundRect(new RectF(1, 1, 1, 1), 1, 1, paint);
		 * 这个在外面用一个framelayout用这种方法，形成一个类似于蒙版的效果
		 */

		// 画圆角矩形方法3：蒙版，缺点蒙版背景不透明，不能显示背景色
		rect.left = getLeft();
		rect.top = getTop();
		rect.right = getRight();
		rect.bottom = getBottom();

		// 有进度但看不见时，显示进度宽为1
		int ProcessWidth = (int) ((progress * rect.width() / 100f) + 0.5f);
		ProcessWidth = progress > 0 && ProcessWidth < 1 ? 1 : ProcessWidth;
		rectProcess.set(rect.left, rect.top, rect.left + ProcessWidth,
				rect.bottom);

		/*
		 * canvas.drawRect(new RectF(rect.left + mBorder, rect.top + mBorder,
		 * rect.right - mBorder, rect.bottom - mBorder), mPaintBg);
		 */

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
//		npMaskBmp.draw(canvas, rect);

		// 描边,画在蒙版后，否则会被蒙版覆盖
		// canvas.drawRoundRect(rect, 12, 12, mPaintBorder);

		String strProcess = progress + "%";
		FontMetrics fontMetrics = mTextPaint.getFontMetrics();
		// 得到字体的实际高度
		double dFontHeight = Math.ceil(fontMetrics.bottom - fontMetrics.ascent);
		double dFontWidth = mTextPaint.measureText(strProcess);
		/*
		 * Log.d(TAG, "rect.top =" + rect.top + "rect.height() = " +
		 * rect.height() + "dFontHeight = " + dFontHeight);
		 */
		// 画进度百分比
		canvas.drawText(
				strProcess,
				(float) (rect.left + (rect.width() - dFontWidth) / 2),
				rect.top
						+ (rect.height() - fontMetrics.bottom + fontMetrics.top)
						/ 2 - fontMetrics.top, mTextPaint);

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