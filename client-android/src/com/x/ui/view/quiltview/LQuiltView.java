package com.x.ui.view.quiltview;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.x.R;

/**
 * @ClassName: LQuiltView
 * @Desciption: 规则瀑布流布局（左边一张大图，右边两张小图）
 
 * @Date: 2014-3-26 下午6:41:03
 */

public class LQuiltView extends FrameLayout implements OnGlobalLayoutListener {

	public QuiltViewBase quilt;
	public ViewGroup scroll;
	public int padding = 5;
	public boolean isVertical = false;
	public ArrayList<View> views;
	private Adapter adapter;
	
	public LQuiltView(Context context,boolean isVertical) {
		super(context);
		this.isVertical = isVertical;
		setup();
	}
	
	public LQuiltView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
			    R.styleable.QuiltView);
			 
		String orientation = a.getString(R.styleable.QuiltView_scrollOrientation);
		if(orientation != null){
			if(orientation.equals("vertical")){
				isVertical = true;
			} else {
				isVertical = false;
			}
		}
		setup();
	}
	
	public void setup(){
		views = new ArrayList<View>();
		
		if(isVertical){
			scroll = new ScrollView(this.getContext());
		} else {
			scroll = new HorizontalScrollView(this.getContext());
		}
		quilt = new QuiltViewBase(getContext(), isVertical, true);
		scroll.addView(quilt);
		this.addView(scroll);
		
	}
	
	private DataSetObserver adapterObserver = new DataSetObserver(){
		public void onChanged(){
			super.onChanged();
			onDataChanged();
		}
		
		public void onInvalidated(){
			super.onInvalidated();
			onDataChanged();
		}
		
		public void onDataChanged(){
			setViewsFromAdapter(adapter);
		}
	};
	
	public void setAdapter(Adapter adapter){
		this.adapter = adapter;
		adapter.registerDataSetObserver(adapterObserver);
		setViewsFromAdapter(adapter);
	}

	private void setViewsFromAdapter(Adapter adapter) {
		this.removeAllViews();
		for(int i = 0; i < adapter.getCount(); i++){
			quilt.addPatch(adapter.getView(i, null, quilt));
		}
	}
	
	public void addPatchImages(ArrayList<ImageView> images,Activity mActivity){
		
		for(ImageView image: images){
			addPatchImage(image,mActivity);
		}
	}

	public void addPatchImage(ImageView image,Activity mActivity){

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		image.setLayoutParams(params);
        
		LinearLayout wrapper = new LinearLayout(mActivity);
		wrapper.setGravity(Gravity.CENTER);
		//wrapper.setPadding(0, padding, padding, padding);
		if(image.getParent() != null) { // 防止元素重复的错误。。
			((LinearLayout)image.getParent()).removeView(image);
		}
		wrapper.addView(image);
		quilt.addPatch(wrapper);
	}

	public void addPatchViews(ArrayList<View> views_a){
		for(View view: views_a){
			quilt.addPatch(view);
		}
	}

	public void addPatchView(View view){
		quilt.addPatch(view);
	}

	public void addPatchesOnLayout(){
		for(View view: views){
			quilt.addPatch(view);
		}
	}
	
	public void removeQuilt(View view){
		quilt.removeView(view);
	}
	
	public void setChildPadding(int padding){
		this.padding = padding;
	}
	
	public void refresh(){
		quilt.refresh();
	}
	
	public void setOrientation(boolean isVertical){
		this.isVertical = isVertical;
	}

	
	@Override
	public void onGlobalLayout() {
		//addPatchesOnLayout();
	}
	
	
	/**
	 * @return void
	 * @description 清除界面布局元素 
	 
	 */
	public void clear() {
		quilt.clearAllViews();
	}
	
}
