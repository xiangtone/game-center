package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class GridViewNoScroll extends GridView {

    public GridViewNoScroll(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public GridViewNoScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public GridViewNoScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        //设置 改控件的高度为内容高度，即不再需要滚动
        int expandSpec = MeasureSpec.makeMeasureSpec(   
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
