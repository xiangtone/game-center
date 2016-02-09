package com.x.ui.activity.floating;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.x.R;
import com.x.business.localapp.sort.PinyinComparator;
import com.x.db.LocalAppEntityManager;
import com.x.publics.model.InstallAppBean;
import com.x.ui.adapter.FloatAppListAdapter;
import com.x.ui.view.floating.FloatPreferebce;

/**
 * 
* @ClassName: AppListAvticity
* @Description: APP列表窗口

* @date 2014-5-27 上午11:18:41
*
 */
public class AppListAvticity extends Activity {

	private ListView listView = null;
	private PinyinComparator pinyinComparator;
	private FloatAppListAdapter adapter = null;
	private List<InstallAppBean> localAppList = new ArrayList<InstallAppBean>();
	private String Item_id = null;
	//数据存储
	private FloatPreferebce	mfloatPreferebce = null;
	private int  version = 0; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applist);
		adapter = new FloatAppListAdapter(this);
		listView = (ListView) findViewById(R.id.app_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(clickListener);
	    version = Integer.valueOf(android.os.Build.VERSION.SDK_INT); 
		refreshData();
		if(getIntent() != null){
			Item_id = getIntent().getStringExtra("Item");
		}
		mfloatPreferebce = FloatPreferebce.getInstance(getApplicationContext());
	}

	private OnItemClickListener clickListener = new OnItemClickListener() {

		@SuppressLint("NewApi")
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			InstallAppBean appInfoBean = (InstallAppBean) adapter.getList().get(position);
			mfloatPreferebce.setTempSave(Item_id, appInfoBean.getPackageName());
			if(version < 16){
				finish();
			}else{
				finishAffinity();
			}
		}
	};

	private void refreshData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				sort();
			}
		}).start();
	}

	private void sort() {
		//实例化汉字转拼音类
		pinyinComparator = new PinyinComparator();
		localAppList = LocalAppEntityManager.getInstance().getAllLocalApps();
		// 根据a-z进行排序源数据
		Collections.sort(localAppList, pinyinComparator);

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				adapter.setList(localAppList);
			}
		});
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onPause() {
		if(version < 16){
			finish();
		}else{
			finishAffinity();
		}
		super.onPause();
	}

	@SuppressLint("NewApi")
	@Override
	public void onBackPressed() {
		
		if(version < 16){
			finish();
		}else{
			finishAffinity();
		}
		
		super.onBackPressed();
	}
}
