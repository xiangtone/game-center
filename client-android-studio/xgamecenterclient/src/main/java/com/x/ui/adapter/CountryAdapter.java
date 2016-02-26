package com.x.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.x.R;
import com.x.business.country.CountryManager;
import com.x.publics.model.CountryBean;
import com.x.publics.utils.SharedPrefsUtil;

/**
 * 
* @ClassName: CountryAdapter
* @Description: 多国家适配器adapter

* @date 2014-5-21 下午1:23:03
*
 */
public class CountryAdapter extends ArrayListBaseAdapter<CountryBean> {

	private int autoCountryId;

	// constructor
	public CountryAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.country_item_layout, null);

		// initialize CommentBean Object to used
		final CountryBean countryBean = mList.get(position);
		final CountryViewHolder holder = new CountryViewHolder(convertView);
		holder.initData(countryBean, context);
		holder.setSkinTheme(context);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 保存选择的国家信息  			   	
				if (countryBean.getId() == 0) {
					if (autoCountryId != 0) {
						CountryManager.getInstance().saveCountryId(context, autoCountryId, true);
					} else {
						CountryManager.getInstance().saveCountryId(context, 1, true);
					}
				} else {
					CountryManager.getInstance().saveCountryId(context, countryBean.getId(), false);
				}
				SharedPrefsUtil.putValue(context, "COUNTRY_URL", countryBean.getUrl());
				CountryAdapter.this.notifyDataSetChanged();
			}
		});

		return convertView;
	}

	public void setAutoCountryId(int autoCountryId) {
		this.autoCountryId = autoCountryId;
	}

}
