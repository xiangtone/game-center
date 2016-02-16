package com.x.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TextViewExtend extends TextView {
	private ImageView showImg; 
	public static final int LIMIT_LINE = 5;

	public TextViewExtend(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void setShowImg(ImageView img) {
		this.showImg  = img;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (getLineCount() > LIMIT_LINE) {
			if(showImg != null){
				showImg.setVisibility(View.VISIBLE);
			}
			setMaxLines(LIMIT_LINE);
			setEllipsize(TruncateAt.END);
		} 
		super.onDraw(canvas);
	}

}
