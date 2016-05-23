
package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.hykj.gamecenter.R;

/**
 * @author froyohuang 能够根据子view的高宽和个数均匀排列子view的自定义viewgroup，只支持单行
 */
public class RowCellLayout extends ViewGroup {

    private int mCellWidth;

    private int mCellHeight;

    private int mCellCountX;

    private int mWidthGap;

    public RowCellLayout(Context context) {
        super(context);

    }

    public RowCellLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RowCellLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final Resources resources = getContext().getResources();
        mCellWidth = resources.getDimensionPixelOffset(R.dimen.lifecard_width);
        mCellHeight = resources.getDimensionPixelOffset(R.dimen.lifecard_height);
        mCellCountX = resources.getInteger(R.integer.lifecard_column);

    }

    public void setUp(int cellWidth, int cellHeight, int count) {
        mCellWidth = cellWidth;
        mCellHeight = cellHeight;
        mCellCountX = count;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child != null && child.getVisibility() != GONE) {
                RowCellLayout.LayoutParams lp = (RowCellLayout.LayoutParams) child
                        .getLayoutParams();

                int childLeft = lp.x;
                int childTop = lp.y;
                child.layout(childLeft, childTop, childLeft + lp.width, childTop + lp.height);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        mWidthGap = (widthSpecSize - mCellCountX * mCellWidth) / (mCellCountX - 1);
        setMeasuredDimension(widthSpecSize, mCellHeight);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            setupChild(child);
            RowCellLayout.LayoutParams lp = (RowCellLayout.LayoutParams) child.getLayoutParams();
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
            int childheightMeasureSpec = MeasureSpec
                    .makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
            child.measure(childWidthMeasureSpec, childheightMeasureSpec);
        }
    }

    private void setupChild(View child) {
        RowCellLayout.LayoutParams lp = (RowCellLayout.LayoutParams) child.getLayoutParams();
        lp.setUp(mCellWidth, mCellHeight, mWidthGap);
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        /**
         * Horizontal location of the item in the grid.
         */
        public int cellX;

        // X coordinate of the view in the layout.
        public int x;

        // Y coordinate of the view in the layout.
        public int y;

        public LayoutParams(int cellX) {
            super(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            this.cellX = cellX;
        }

        public void setUp(int mCellWidth, int mCellHeight, int mWidthGap) {
            width = mCellWidth;
            height = mCellHeight;
            x = cellX * (mCellWidth + mWidthGap);
            y = 0;
        }
    }
}
