package com.x.ui.adapter;

import java.io.Serializable;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.country.CountryManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.CountryBean;
import com.x.publics.utils.NetworkImageUtils;

/**
 * 
* @ClassName: CountryViewHolder
* @Description: 多国家，数据包装类

* @date 2014-5-21 上午11:27:27
*
 */
public class CountryViewHolder implements Serializable {

	private static final long serialVersionUID = 1L;
	public ImageView countryIcon;
	public TextView countryName;
	public RadioButton radioButton;

	/* the method for initialize components */
	public CountryViewHolder(View view) {
		if (view != null) {
			countryIcon = (ImageView) view.findViewById(R.id.country_icon);
			countryName = (TextView) view.findViewById(R.id.country_name);
			radioButton = (RadioButton) view.findViewById(R.id.rb_country_item);
			radioButton.setChecked(false);
		}
	}

	/* the method for setter Data to components */
	public void initData(CountryBean countryBean, Context context) {
		countryName.setText(countryBean.getName());
		NetworkImageUtils.load(context, ImageType.NETWORK, countryBean.getUrl(), R.drawable.ic_screen_default_picture,
				R.drawable.ic_screen_default_picture, countryIcon);
		// 从配置文件中读取
		int countryId = CountryManager.getInstance().getCountryId(context);
		boolean isAutoCountry = CountryManager.getInstance().isAutoCountry(context);
		if (isAutoCountry) {
			if (countryBean.getId() == 0) {
				radioButton.setChecked(true);
			}
		} else if (countryBean.getId() == countryId) {
			radioButton.setChecked(true);
		}
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param @param context    
	* @return void    
	*/
	public void setSkinTheme(Context context) {
		SkinConfigManager.getInstance().setRadioBtnDrawable(context, radioButton, SkinConstan.OPTION_BTN);
	}
}
