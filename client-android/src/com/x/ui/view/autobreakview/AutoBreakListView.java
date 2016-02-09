package com.x.ui.view.autobreakview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.x.R;
import com.x.publics.utils.ResourceUtil;

/**
 * 
 * @ClassName: AutoBreakListView
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-8-19 下午3:24:07
 * 
 */
@SuppressLint({ "DrawAllocation", "UseSparseArrays" })
public class AutoBreakListView extends RelativeLayout {

	private Context context;
	private BaseAdapter baseAdapter;
	private String TAG = AutoBreakListView.class.getSimpleName();

	public AutoBreakListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@Override
	protected void onLayout(boolean arg0, int argLeft, int argTop, int argRight, int argBottom) {
		Log.i(TAG, "L:" + argLeft + " T:" + argTop + " R:" + argRight + " B:" + argBottom);
		final int count = getChildCount();
		int line = 0;
		int lengthX = 0;
		int lengthY = 0;

		// count 必须大于0，否则没有子元素
		if (count > 0) {

			Map<Integer, List<View>> lineMap = new HashMap<Integer, List<View>>();
			List<View> lineList = new ArrayList<View>();
			lineMap.put(0, lineList);

			Map<Integer, List<BoxLayout>> boxLayoutMap = new HashMap<Integer, List<BoxLayout>>();
			List<BoxLayout> boxLayoutList = new ArrayList<BoxLayout>();
			boxLayoutMap.put(0, boxLayoutList);

			for (int i = 0; i < count; i++) {

				final View child = this.getChildAt(i);
				int width = child.getMeasuredWidth();
				// int height = child.getMeasuredHeight();
				int height = ResourceUtil.getDimensionPixelSize(context, R.dimen.keyword_layout_height);

				if (lengthX == 0) {
					lengthX += width;
				} else {
					lengthX += (width + getDividerWidth());
				}

				if ((i == 0 && lengthX <= argRight - argLeft)) {
					lengthY += height;
				}

				if ((lengthX > argRight - argLeft)) {
					lengthX = width;
					lengthY += getDividerHeight() + height;

					// 拉伸处理
					line++;
					lineMap.put(line, new ArrayList<View>());
					boxLayoutMap.put(line, new ArrayList<BoxLayout>());
				}

				// 存储对象Map
				BoxLayout boxChild = new BoxLayout(lengthX - width, lengthY - height, lengthX, lengthY);
				boxLayoutMap.get(line).add(boxChild);
				// 拉伸处理
				lineMap.get(line).add(child);
			}

			// 重新计算、排序第一行
			if (!lineMap.isEmpty()) {
				List<View> newlist = lineMap.get(0);
				newlist.add(newlist.get(0));
				newlist.remove(0);

				lineMap.put(0, new ArrayList<View>());
				boxLayoutMap.put(0, new ArrayList<BoxLayout>());

				int x = 0;

				for (int i = 0; i < newlist.size(); i++) {

					final View child = newlist.get(i);
					int width = child.getMeasuredWidth();
					// int height = child.getMeasuredHeight();
					int height = ResourceUtil.getDimensionPixelSize(context, R.dimen.keyword_layout_height);

					if (x == 0) {
						x += width;
					} else {
						x += (width + getDividerWidth());
					}

					// 存储对象Map
					BoxLayout boxChild = new BoxLayout(x - width, 0, x, height);
					boxLayoutMap.get(0).add(boxChild);

					// 拉伸处理
					lineMap.get(0).add(child);
				}
			}

			// 循环拉伸每个View,重新布局
			for (int i = 0; i <= line; i++) {

				// 该行最后一个位置
				int len = lineMap.get(i).size();
				if (len == 0)
					return;
				BoxLayout endBox = boxLayoutMap.get(i).get(len - 1);
				int emptyWidth = argRight - argLeft - endBox.right;
				int padding = emptyWidth / len;

				int endPoint = 0;
				int startPoint = 0;

				for (int j = 0; j < len; j++) {

					// 第一行，特别处理
					if (i == 0 && line > 0) {
						//第一行，最后一个的起点
						int leftLength = emptyWidth - getDividerWidth();
						if (leftLength >= 0 && (len - 1) != 0) {
							padding = leftLength / (len - 1);
						}
					}

					View childView = lineMap.get(i).get(j);
					BoxLayout childBox = boxLayoutMap.get(i).get(j);

					startPoint = childBox.left + j * padding;

					// 判断，最后一行，不拉伸处理
					if (i == line) {
						startPoint = childBox.left;
						endPoint = childBox.right;
					} else {
						endPoint = childBox.right + (j + 1) * padding;
						// 第一行，特别处理
						if (i == 0 && j == len - 1) {
							//第一行，最后一个的终点
							endPoint = argRight - argLeft;
						}
					}

					// 得到平均值进行布局
					childView.layout(startPoint, childBox.top, endPoint, childBox.bottom);
				}

			}

			android.view.ViewGroup.LayoutParams lp = AutoBreakListView.this.getLayoutParams();
			lp.height = lengthY;
			AutoBreakListView.this.setLayoutParams(lp);
			if (isAddChildType()) {
				new Thread(new RefreshCustomThread()).start();
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);

		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private static boolean addChildType;

	final static boolean isAddChildType() {
		return addChildType;
	}

	public static void setAddChildType(boolean addChildType) {
		AutoBreakListView.addChildType = addChildType;
	}

	private int dividerHeight = 0;

	final int getDividerHeight() {
		return dividerHeight;
	}

	public void setDividerHeight(int dividerHeight) {
		this.dividerHeight = dividerHeight;
	}

	private int dividerWidth = 0;

	final int getDividerWidth() {
		return dividerWidth;
	}

	public void setDividerWidth(int dividerWidth) {
		this.dividerWidth = dividerWidth;
	}

	public void setAdapter(BaseAdapter adapter) {
		this.baseAdapter = adapter;
		setAddChildType(true);
		adapter.notifyCustomListView(AutoBreakListView.this);
	}

	/**
	 * 
	 * @param listener
	 */
	public void setOnItemClickListener(AutoBreakOnItemClickListener listener) {
		baseAdapter.setOnItemClickListener(listener);
	}

	/**
	 * Corresponding Item long click event
	 * 
	 * @param listener
	 */
	public void setOnItemLongClickListener(AutoBreakOnItemLongClickListener listener) {
		baseAdapter.setOnItemLongClickListener(listener);
	}

	private final Handler handler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				if (msg.getData().containsKey("getRefreshThreadHandler")) {
					setAddChildType(false);
					baseAdapter.notifyCustomListView(AutoBreakListView.this);
				}
			} catch (Exception e) {
				Log.w(TAG, e);
			}
		}

	};

	private final class RefreshCustomThread implements Runnable {

		@Override
		public void run() {
			Bundle b = new Bundle();
			try {
				Thread.sleep(50);
			} catch (Exception e) {

			} finally {
				b.putBoolean("getRefreshThreadHandler", true);
				sendMsgHanlder(handler, b);
			}
		}
	}

	private final void sendMsgHanlder(Handler handler, Bundle data) {
		Message msg = handler.obtainMessage();
		msg.setData(data);
		handler.sendMessage(msg);
	}

	class BoxLayout {
		int left;
		int top;
		int right;
		int bottom;

		public BoxLayout(int left, int top, int right, int bottom) {
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
		}
	}
}