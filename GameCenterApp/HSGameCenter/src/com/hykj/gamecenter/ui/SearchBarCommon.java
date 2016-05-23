package com.hykj.gamecenter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hykj.gamecenter.R;

/**
 * Created by OddsHou on 2016/3/9.
 */
public class SearchBarCommon extends LinearLayout implements View.OnClickListener{


	private LinearLayout mLayoutSearch;
	private ImageView mSettingButton;
	private ImageView mSettingTipView;
	private TextView mTextRecommedWords;

	public SearchBarCommon(Context context) {
		this(context, null);
	}

	public SearchBarCommon(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SearchBarCommon(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context){
		LayoutInflater.from(context).inflate(R.layout.searchbar_common, this);
		mLayoutSearch = (LinearLayout)findViewById(R.id.layoutSearch);
		mLayoutSearch.setOnClickListener(this);
		mTextRecommedWords = (TextView)findViewById(R.id.textRecommed);

		mSettingTipView = (ImageView)findViewById(R.id.setting_tip_view);
		mSettingButton = (ImageView)findViewById(R.id.setting_icon);
		mSettingButton.setOnClickListener(this);
	}


	public void setText(CharSequence text) {
		mTextRecommedWords.setText(text);
	}

	public void setText(int text) {
		mTextRecommedWords.setText(text);
	}

	/**
	 * Called when a view has been clicked.
	 *
	 * @param v The view that was clicked.
	 */
	@Override
	public void onClick(View v) {
		if (mSearchBarCommonListen == null) {
			throw new IllegalArgumentException("you need interface SearchBarCommonListen listen this event");
		}
		switch (v.getId()) {
			case R.id.layoutSearch:
				mSearchBarCommonListen.clickSearch(v);
				break;
			case R.id.setting_icon:
				mSearchBarCommonListen.clickSettingImage((ImageView) v);
				break;
		}
	}

	public void setSettingTipVisible(int visibility) {
		mSettingTipView.setVisibility(visibility);

	}
	private SearchBarCommonListen mSearchBarCommonListen;

	public void setSearchBarCommonListen(SearchBarCommonListen searchBarCommonListen) {
		mSearchBarCommonListen = searchBarCommonListen;
	}

	public interface SearchBarCommonListen{
		void clickSearch(View view);

		void clickSettingImage(ImageView imageView);
	}
}
