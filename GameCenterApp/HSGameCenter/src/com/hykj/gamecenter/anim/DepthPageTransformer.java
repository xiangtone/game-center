package com.hykj.gamecenter.anim;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 *
 */
public class DepthPageTransformer implements ViewPager.PageTransformer {
	@SuppressLint("NewApi")
	@Override
        /*
         * 关键是要理解transformPage(View view, float position)的参数。
         * view理所当然就是滑动中的那个view，position这里是float类型， 不是平时理解的int位置，而是当前滑动状态的一个表示，
         * 比如当滑动到正全屏时，position是0， 而向左滑动，使得右边刚好有一部被进入屏幕时，
         * position是1，如果前一页和下一页基本各在屏幕占一半时， 前一页的position是-0.5，后一页的position是0.5，
         * 所以根据position的值我们就可以自行设置需要的alpha，x/y信息。 向左滑动时，（开始时左边全屏，右边看不见）左边 0-->
         * -1 ,右边 1--> 0
         */
	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();
		int halfPageWidth = pageWidth >> 1;
		if (position < -1) { // [-Infinity,-1)
			// This page is way off-screen to the left.
			view.setAlpha(0);
		} else if (position <= 0) { // [-1,0]
			// Use the default slide transition when
			// moving to the left page
			view.setAlpha(1);
			view.setTranslationX(0);
			view.setScaleX(1);
			view.setScaleY(1);
		} else if (position <= 1) { // (0,1]
			// Fade the page out.
			view.setAlpha(1 - position);
			// Counteract the default slide transition
			// view.setTranslationX( pageWidth * -position );
			view.setTranslationX(-(halfPageWidth * position));// 如：向左滑动时相对与左边(即前一页)的位置(会被左边遮盖的宽度)
			// Scale the page down (between MIN_SCALE and 1)
			// float scaleFactor = MIN_SCALE + ( 1 - MIN_SCALE ) * ( 1 -
			// Math.abs( position ) );
			// view.setScaleX( scaleFactor );
			// view.setScaleY( scaleFactor );
		} else { // (1,+Infinity]
			// This page is way off-screen to the right.
			view.setAlpha(0);
		}
	}

}