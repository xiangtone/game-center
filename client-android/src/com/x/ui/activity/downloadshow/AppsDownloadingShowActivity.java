/**   
* @Title: AppsDownloadingShowActivity.java
* @Package com.x.ui.activity
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-3-17 下午4:37:31
* @version V1.0   
*/

package com.x.ui.activity.downloadshow;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;

import com.x.R;
import com.x.business.dynamiclistview.DynamicListViewManager;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.ui.view.dynamic.DynamicListView;
import com.x.ui.view.dynamic.DynamicListViewAdapter;
import com.x.ui.view.dynamic.DynamicListViewItem;
import com.x.ui.view.dynamic.OnRowAdditionAnimationListener;

/**
* @ClassName: AppsDownloadingShowActivity
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-3-17 下午4:37:31
* 
*/

public class AppsDownloadingShowActivity extends Activity {

	private Timer timer;
	private Animation animation;
	private int index, listSize;
	private Context context = this;
	private View topView1, topView2;
	private ImageView clockHourHand;
	private boolean visibleFirstItem = false;
	private ArrayList<DynamicListViewItem> lists;

	private DynamicListView dynamicListView;
	private DynamicListViewAdapter dynamicListViewAdapter;

	private static final int ADD_ITEM = 201;
	private static final int DEL_ITEM = 202;
	private static final long TIME_DURATION = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dynamiclistview_detail);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null == animation && null == timer && visibleFirstItem) {
			startClockAnimation();
			startScrollTask(TIME_DURATION);
		}
		DataEyeManager.getInstance().module(ModuleName.APPS_DOWNLOADING_SHOW, true);
		DataEyeManager.getInstance().source(StatisticConstan.SrcName.APPS_DOWNLOADING_SHOW, 0, null, 0, null, null,
				false);
		DataEyeManager.getInstance().onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopScrollTask();
		DataEyeManager.getInstance().module(ModuleName.APPS_DOWNLOADING_SHOW, false);
		DataEyeManager.getInstance().onPause(this);
	}

	/** 
	* @Title: initView 
	* @Description: TODO 
	* @param     
	* @return void    
	*/
	private void initView() {
		index = getIntent().getIntExtra("index", 0);
		listSize = getIntent().getIntExtra("listSize", 0) - 1;
		lists = getIntent().getParcelableArrayListExtra("lists");

		topView1 = findViewById(R.id.ll_top_view1);
		topView2 = findViewById(R.id.ll_top_view2);
		clockHourHand = (ImageView) findViewById(R.id.clock_hour_hand);
		DynamicListViewManager.getInstance().setBackgroudResource(context, topView1, topView2);
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		List<DynamicListViewItem> objs = new ArrayList<DynamicListViewItem>();
		dynamicListView = (DynamicListView) findViewById(R.id.dynamiclistview);
		dynamicListViewAdapter = new DynamicListViewAdapter(context, R.layout.dynamiclistview_detail_item, objs);
		dynamicListView.setAdapter(dynamicListViewAdapter);
		dynamicListView.setData(objs);
		dynamicListView.setOnScrollListener(new MyScrollListener());
		dynamicListView.setRowAdditionAnimationListener(new OnRowAdditionAnimationListener() {

			@Override
			public void onRowAdditionAnimationStart() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(DEL_ITEM);
			}

			@Override
			public void onRowAdditionAnimationEnd() {
				// TODO Auto-generated method stub
				startClockAnimation();
			}
		});

		startScrollTask(0); // 开始滚动任务
	}

	class MyScrollListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			if (firstVisibleItem == 0) {
				visibleFirstItem = true;
			} else {
				visibleFirstItem = false;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				if (visibleFirstItem) {
					startClockAnimation();
					startScrollTask(TIME_DURATION);
				}
				break;

			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			case OnScrollListener.SCROLL_STATE_FLING:
				stopScrollTask();
				break;
			}
		}
	}

	/**
	* @Title: startClockAnimation 
	* @Description: 开始闹钟旋转动画
	* @param     
	* @return void
	 */
	private void startClockAnimation() {
		animation = AnimationUtils.loadAnimation(context, R.anim.rotate_up);
		clockHourHand.startAnimation(animation);
	}

	/**
	* @Title: stopClockAnimation 
	* @Description: 停止闹钟旋转动画
	* @param     
	* @return void
	 */
	private void stopClockAnimation() {
		if (animation != null) {
			clockHourHand.clearAnimation();
			animation.cancel();
			animation = null;
		}
	}

	/**
	* @Title: startScrollTask 
	* @Description: 开始滚动任务
	* @param @param delay    
	* @return void
	 */
	private void startScrollTask(long delay) {
		if (null == timer) {
			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					handler.sendEmptyMessage(ADD_ITEM);
				}
			}, delay, TIME_DURATION);
		}
	}

	/**
	* @Title: stopScrollTask 
	* @Description: 停止滚动任务
	* @param     
	* @return void
	 */
	private void stopScrollTask() {
		if (null != timer) {
			timer.cancel();
			timer = null;
		}
		stopClockAnimation();
	}

	/**
	* @Title: addListViewItem 
	* @Description: 添加动态item
	* @param     
	* @return void
	 */
	private void addListViewItem() {
		DynamicListViewItem listItemObject = lists.get(index);
		if (listItemObject != null)
			dynamicListView.addListViewItem(listItemObject);

		// index = 递增/重置
		if (index < listSize) {
			index++;
		} else {
			index = 0;
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ADD_ITEM:
				addListViewItem();
				break;

			case DEL_ITEM:
				dynamicListView.showItemCount(listSize);
				break;
			}
		}
	};

}
