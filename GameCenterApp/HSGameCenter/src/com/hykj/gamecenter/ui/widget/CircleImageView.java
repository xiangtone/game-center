
package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.hykj.gamecenter.utilscs.BitmapUtils;

/**
 * 限制该view大小为背景大小
 * @author oddshou
 *
 */
public class CircleImageView extends ImageView
{
    private int mWidth;
    private int mHeight;
    private Context mContext;
    private Drawable mDrawableBG;

    public CircleImageView(Context context)
    {
        super(context);
        mContext = context;
        mDrawableBG = getBackground();
    }

    public CircleImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        mDrawableBG = getBackground();
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mContext = context;
        mDrawableBG = getBackground();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mDrawableBG != null) {
            int height = mDrawableBG.getIntrinsicHeight();
            int width = mDrawableBG.getIntrinsicWidth();
            // Get the max possible width given our constraints
            int widthSize = resolveAdjustedSize(width /* + pleft + pright */, Integer.MAX_VALUE,
                    widthMeasureSpec);

            // Get the max possible height given our constraints
            int heightSize = resolveAdjustedSize(height /* + ptop + pbottom */, Integer.MAX_VALUE,
                    heightMeasureSpec);
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    private int resolveAdjustedSize(int desiredSize, int maxSize,
            int measureSpec) {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                /*
                 * Parent says we can be as big as we want. Just don't be larger
                 * than max size imposed on ourselves.
                 */
                result = Math.min(desiredSize, maxSize);
                break;
            case MeasureSpec.AT_MOST:
                // Parent says we can be as big as we want, up to specSize.
                // Don't be larger than specSize, and don't be larger than
                // the max size imposed on ourselves.
                result = Math.min(Math.min(desiredSize, specSize), maxSize);
                break;
            case MeasureSpec.EXACTLY:
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    public void setImageBitmap(Bitmap bm)
    {
        bm = BitmapUtils.getCircleBitmap(bm);
        super.setImageBitmap(bm);
    }
}
