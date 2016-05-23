package com.hykj.gamecenter.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.utils.Utils;

import java.util.ArrayList;

/**
 * Created by OddsHou on 2016/1/5.
 */
public class TextNameFlagView extends LinearLayout {
	private int mNameColor;
	private float mNameSize;
	private TextView mTextName;
	private LayoutInflater mInflater;
	private Context mContext;
	private LinearLayout mFlagContainer;

	public TextNameFlagView(Context context) {
		this(context, null);
	}

	public TextNameFlagView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TextNameFlagView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextNameFlag, defStyle, 0);
		mNameColor = a.getColor(R.styleable.TextNameFlag_textTitleColor, 0);
		mNameSize = a.getDimension(R.styleable.TextNameFlag_textTitleSize, 0);
		initView(context);
	}

	private void initView(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(R.layout.layout_textname_flag, this);
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
	}

	public void setFlagVisible(int visible) {
		mFlagContainer.setVisibility(visible);

	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mTextName = (TextView) findViewById(R.id.app_name);
		mFlagContainer = (LinearLayout) findViewById(R.id.linear_flag);
		mFlagContainer.setOrientation(LinearLayout.HORIZONTAL);
		mFlagContainer.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 0;
		mFlagContainer.setLayoutParams(lp);
		if (mNameColor != 0) {
			mTextName.setTextColor(mNameColor);
		}
		if (mNameSize != 0) {
			mTextName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNameSize);
		}
	}

	public void setText(int textId) {
		mTextName.setText(textId);
		mTextName.requestLayout();
	}

	public void setText(CharSequence text) {
		mTextName.setText(text);
		mTextName.requestLayout();
	}

	public void addFlag(int flag) {
		mFlagContainer.removeAllViews();
		ArrayList<Integer> list = Utils.getRecommend(flag);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				int flags = list.get(i);
				TextView view = getFlagTextView(flags);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.normal_margin_padding_little5);
				mFlagContainer.addView(view, lp);
			}
		}
//		mTextName.requestLayout();
	}

	private TextView getFlagTextView(int flag) {
		TextView view = (TextView) mInflater.inflate(R.layout.app_recommend_flag, null);
		//1=官方 2=推荐 4=首发 8=免费 16=礼包 32=活动 64=内测 128=热门
		Resources resources = mContext.getResources();
		switch (flag) {
			case 1:
				view.setBackgroundResource(R.drawable.recommflag_official_bg);
				view.setTextColor(resources.getColor(R.color.color_official_text));
				view.setText(resources.getString(R.string.first_official));
				break;
			case 2:
				view.setBackgroundResource(R.drawable.recommflag_recommend_bg);
				view.setTextColor(resources.getColor(R.color.color_recommend_text));
				view.setText(resources.getString(R.string.home_recommed_label));
				break;
			case 4:
				view.setBackgroundResource(R.drawable.recommflag_firstpublish_bg);
				view.setTextColor(resources.getColor(R.color.color_firstpublish_text));
				view.setText(resources.getString(R.string.first_publish));
				break;
			case 8:
				view.setBackgroundResource(R.drawable.recommflag_firstpublish_bg);
				view.setTextColor(resources.getColor(R.color.color_firstpublish_text));
				view.setText(resources.getString(R.string.safe_free));
				break;
			case 16:
				view.setBackgroundResource(R.drawable.recommflag_hot_bg);
				view.setTextColor(resources.getColor(R.color.color_hot_text));
				view.setText(resources.getString(R.string.first_gift));
				break;
			case 32:
				view.setBackgroundResource(R.drawable.recommflag_recommend_bg);
				view.setTextColor(resources.getColor(R.color.color_recommend_text));
				view.setText(resources.getString(R.string.first_compaign));
				break;
			case 64:
				view.setBackgroundResource(R.drawable.recommflag_official_bg);
				view.setTextColor(resources.getColor(R.color.color_official_text));
				view.setText(resources.getString(R.string.first_beta));
				break;
			case 128:
				view.setBackgroundResource(R.drawable.recommflag_hot_bg);
				view.setTextColor(resources.getColor(R.color.color_hot_text));
				view.setText(resources.getString(R.string.hotapps));
				break;
		}
		return view;
	}


}
