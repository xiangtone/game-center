package com.x.ui.view;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @ClassName: MyListView
 * @Desciption: 让MyListView可以做ScrollView的子控件，但尺寸不会减小
 
 * @Date: 2014-1-16 上午8:41:21
 */
public class MyListView extends ListView {  
      
    public MyListView(Context context) {    
        super(context);    
    }    
      
    public MyListView(Context context, AttributeSet attrs) {    
        super(context, attrs);    
    }    
      
    public MyListView(Context context, AttributeSet attrs, int defStyle) {    
        super(context, attrs, defStyle);    
    }    
    
    @Override    
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {    
    
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,    
                MeasureSpec.AT_MOST);    
        super.onMeasure(widthMeasureSpec, expandSpec);    
    }      
}   