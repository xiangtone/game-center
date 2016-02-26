package com.x.ui.activity.guide;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.x.R;
import com.x.business.statistic.DataEyeManager;
import com.x.publics.utils.Utils;

public class GuideMainActivity extends Activity {

	private ImageView guideDrawerIv, guideBodyIv, guideTipIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_main);
		Utils.addFinishAct(this);
		guideDrawerIv = (ImageView) findViewById(R.id.guide_drawer_iconIv);
		guideDrawerIv.setOnTouchListener(new OnTouchListener());
		guideBodyIv = (ImageView) findViewById(R.id.guide_main_body);
		guideBodyIv.setOnTouchListener(new OnTouchListener());
		guideTipIv = (ImageView) findViewById(R.id.guide_drawer_icontipIv);
		guideTipIv.setOnTouchListener(new OnTouchListener());

	}

	class OnTouchListener implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent mEvent) {
			switch (v.getId()) {
			case R.id.guide_drawer_iconIv:
				if (mEvent.getAction() == MotionEvent.ACTION_UP) {
					GuideMainActivity.this.finish();
				}
				return true;
			case R.id.guide_main_body:
				if (mEvent.getAction() == MotionEvent.ACTION_UP) {
					GuideMainActivity.this.finish();
				}
				return true;
			case R.id.guide_drawer_icontipIv:
				if (mEvent.getAction() == MotionEvent.ACTION_UP) {
					GuideMainActivity.this.finish();
				}
				return true;
			default:
				return true;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		DataEyeManager.getInstance().onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().onPause(this);
	}
}
