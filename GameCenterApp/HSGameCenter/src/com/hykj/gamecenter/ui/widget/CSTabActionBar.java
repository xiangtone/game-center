package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.utilscs.CSLibSettings;
import com.hykj.gamecenter.utilscs.LogUtils;

public class CSTabActionBar extends RelativeLayout implements OnClickListener {
	private OnActionBarClickListener mOnActionBarListener = null;
	private final Context mContext;
	private PagerSlidingTabStrip mCSPagerSlidingTabStrip;
	private ImageView mLogoImageView;
	private TextView mLogoTextView;
	private boolean mShowLogo = true;
	private final String mLogoTitle;
	private final int mLogoIcon;
	private final int mRightEditIcon;

	private TextView mTipView = null;

	private FrameLayout mReturnButton = null;
	private ImageView mDownloadButton = null;
	private ImageView mSettingButton = null;
	private ImageView mSettingTipView = null;
	private ImageView mSearchButton = null;

	private boolean mShowSetting = false;
	private boolean mShowManage = false;
	private boolean mShowSearch = false;
	private boolean mShowReturn = false;
	private boolean mShowLeftEdit = false;
	private boolean mShowRightEdit = false;
	private boolean mShowTitle = false;
	private boolean mShowTab = true;
	private boolean mShowLogoTextView = false;
	private View mRightEditButton;
	private Button mRightSelectButton;
	private Button mLeftCancelButton;
	private final CharSequence mTitle;
	private TextView mTitleView = null;
	private View mRightMoreButton;

	public CSTabActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CSTabActionBar, defStyle, 0);

		mShowTitle = a.getBoolean(R.styleable.CSCommonActionBar_comm_showTitle,
				false);
		mShowLogo = a.getBoolean(R.styleable.CSTabActionBar_tab_showLogo,
				CSLibSettings.ACTION_BAR_SHOW_LOGO_ICON);
		mLogoTitle = a.getString(R.styleable.CSTabActionBar_tab_logoTitle);
		mLogoIcon = a.getResourceId(R.styleable.CSTabActionBar_tab_logoIcon,
				R.drawable.csl_comm_actionbar_logo);
		mShowSetting = a.getBoolean(R.styleable.CSTabActionBar_tab_showSetting,
				false);
		mShowManage = a.getBoolean(R.styleable.CSTabActionBar_tab_showManage,
				false);
		mShowSearch = a.getBoolean(R.styleable.CSTabActionBar_tab_showSearch,
				false);
		mTitle = a.getString(R.styleable.CSTabActionBar_tab_title);
		mShowTab = a.getBoolean(R.styleable.CSTabActionBar_tab_showTabStrip,
				true);
		mShowReturn = a.getBoolean(R.styleable.CSTabActionBar_tab_showReturn,
				false);
		mShowLeftEdit = a.getBoolean(
				R.styleable.CSTabActionBar_tab_showLeftEdit, false);
		mShowRightEdit = a.getBoolean(
				R.styleable.CSTabActionBar_tab_showRightEdit, false);
		mShowLogoTextView = a.getBoolean(
				R.styleable.CSTabActionBar_tab_showLogoTitle,
				CSLibSettings.ACTION_BAR_SHOW_LOGO_TITLE);
		mRightEditIcon = a.getResourceId(
				R.styleable.CSTabActionBar_tab_rightEditIcon,
				R.drawable.csls_tab_actionbar_right_edit);
		LayoutInflater.from(context).inflate(R.layout.csl_cs_tab_actionbar,
				this);

		a.recycle();
	}

	public CSTabActionBar(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.plCSActionBarStyle);
		// TODO Auto-generated constructor stub
	}

	public CSTabActionBar(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClick(View view) {
		if (mOnActionBarListener == null)
			return;

		mOnActionBarListener.onActionBarClicked((Integer) view.getTag());

	}

	public void setOnActionBarClickListener(OnActionBarClickListener listener) {
		mOnActionBarListener = listener;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mCSPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tab_strip);
		mLogoImageView = (ImageView) findViewById(R.id.tab_logo);
		mLogoTextView = (TextView) findViewById(R.id.tab_logo_text);
		mLogoImageView.setImageResource(mLogoIcon);
		mTitleView = (TextView) findViewById(R.id.actionbar_title);
		mTitleView.setText(mTitle);

		initActionBarStyle();

		ViewGroup rightIconContainer = (ViewGroup) findViewById(R.id.tab_right_function_icons);
		addRightEditIcon(rightIconContainer);

		ViewGroup leftIconContainer = (ViewGroup) findViewById(R.id.tab_left_function_icons);
		addLeftButtonIcon(leftIconContainer);
		LogUtils.d("mLogoImageView : " + mLogoImageView + " mShowLogo  "
				+ mShowLogo);
		mLogoImageView.setVisibility(mShowLogo ? VISIBLE : GONE);
		mCSPagerSlidingTabStrip.setVisibility(mShowTab ? VISIBLE : GONE);
		mLogoTextView.setVisibility(mShowLogoTextView ? VISIBLE : GONE);
		mRightEditButton.setVisibility(mShowRightEdit ? VISIBLE : GONE);
		mTitleView.setVisibility(mShowTitle ? VISIBLE : GONE);
		mLeftCancelButton.setVisibility(mShowLeftEdit ? VISIBLE : GONE);
	}

	private void initActionBarStyle() {
		mCSPagerSlidingTabStrip
				.setUnderlineColorResource(R.color.csl_tab_underline_color);
		mCSPagerSlidingTabStrip
				.setIndicatorColorResource(R.color.csl_tab_indicator_color);
		mCSPagerSlidingTabStrip
				.setDividerColorResource(R.color.csl_tab_divider_color);

		mCSPagerSlidingTabStrip
				.setTextColorNormalResource(R.color.csl_tab_text_default_color);
		mCSPagerSlidingTabStrip
				.setTextColorSelectedResource(R.color.csl_tab_text_selector_color);
		mCSPagerSlidingTabStrip.setTextSize(getResources()
				.getDimensionPixelSize(R.dimen.csl_text_size_xlarge));
		int hight = getResources().getDimensionPixelSize(
				R.dimen.csl_tab_actionbar_divider);
		mCSPagerSlidingTabStrip.setDividerPadding(hight);
		int width = getResources().getDimensionPixelSize(
				R.dimen.csl_tab_actionbar_underline_width);
	}

	private void addRightEditIcon(ViewGroup container) {
		View iconView = LayoutInflater.from(mContext).inflate(
				R.layout.csl_cs_tab_actionbar_right_edit, container);
		// container.addView(iconView);

		mRightEditButton = iconView.findViewById(R.id.edit_icon_frame);
		mRightEditButton.setTag(OnActionBarClickListener.RIGHT_BNT);
		mRightEditButton.setOnClickListener(this);

		ImageView mEditIcon = (ImageView) iconView.findViewById(R.id.edit_icon);
		mEditIcon.setImageResource(mRightEditIcon);

		mRightSelectButton = (Button) iconView.findViewById(R.id.select_all);
		mRightSelectButton.setTag(OnActionBarClickListener.SELECT_BNT);
		mRightSelectButton.setOnClickListener(this);

		mRightMoreButton = iconView.findViewById(R.id.more_icon_frame);
		mRightMoreButton.setTag(OnActionBarClickListener.MORE_BNT);
		mRightMoreButton.setOnClickListener(this);

		/*
		 * int iPaddingWidth = getResources().getDimensionPixelSize(
		 * R.dimen.csl_cs_padding_half_size);
		 */

		if (mShowSearch) {
			addSearchIcon(container);

			/*
			 * if (mShowManage || mShowSetting) { TextView tvPadding = new
			 * TextView(mContext); tvPadding.setWidth(iPaddingWidth);
			 * container.addView(tvPadding); }
			 */
		}
		if (mShowManage) {
			addDownloadIcon(container);

			/*
			 * if (mShowSetting) { TextView tvPadding = new TextView(mContext);
			 * tvPadding.setWidth(iPaddingWidth); container.addView(tvPadding);
			 * }
			 */
		}
		if (mShowSetting) {
			addSettingIcon(container);
		}

	}

	private void addSearchIcon(ViewGroup container) {
		View iconView = LayoutInflater.from(mContext).inflate(
				R.layout.csl_cs_comm_actionbar_search, container);
		// container.addView(iconView);

		mSearchButton = (ImageView) iconView.findViewById(R.id.search_icon);
		mSearchButton.setTag(OnActionBarClickListener.SEARCH_BNT);
		mSearchButton.setOnClickListener(this);
	}

	private void addDownloadIcon(ViewGroup container) {
		View iconView = LayoutInflater.from(mContext).inflate(
				R.layout.csl_cs_comm_actionbar_manage, container);
		// container.addView(iconView);

		mDownloadButton = (ImageView) iconView.findViewById(R.id.manage_icon);
		mTipView = (TextView) findViewById(R.id.tip_accoute_view);

		mDownloadButton.setTag(OnActionBarClickListener.MANAGE_BNT);
		mDownloadButton.setOnClickListener(this);

	}

	private void addSettingIcon(ViewGroup container) {
		View iconView = LayoutInflater.from(mContext).inflate(
				R.layout.csl_cs_comm_actionbar_settting, container);
		// container.addView( iconView );

		mSettingTipView = (ImageView) iconView
				.findViewById(R.id.setting_tip_view);
		mSettingButton = (ImageView) iconView.findViewById(R.id.setting_icon);
		mSettingButton.setTag(OnActionBarClickListener.SETTING_BNT);
		mSettingButton.setOnClickListener(this);
	}

	private void addReturnIcon(ViewGroup container) {
		View iconView = LayoutInflater.from(mContext).inflate(
				R.layout.csl_cs_comm_actionbar_left_return, container);
		// container.addView(iconView);

		mReturnButton = (FrameLayout) iconView
				.findViewById(R.id.return_icon_frame);
		mReturnButton.setTag(OnActionBarClickListener.RETURN_BNT);
		mReturnButton.setOnClickListener(this);

		iconView.findViewById(R.id.return_icon).setTag(
				OnActionBarClickListener.RETURN_BNT);
		iconView.findViewById(R.id.return_icon).setOnClickListener(this);

	}

	private void addLeftButtonIcon(ViewGroup container) {
		View iconView = LayoutInflater.from(mContext).inflate(
				R.layout.csl_cs_tab_actionbar_left_button, container);
		mLeftCancelButton = (Button) iconView.findViewById(R.id.left_icon);
		mLeftCancelButton.setTag(OnActionBarClickListener.LEFT_BNT);
		mLeftCancelButton.setOnClickListener(this);
		if (mShowReturn) {
			addReturnIcon(container);
		}
	}

	public void setTitle(String title) {
		mTitleView.setText(title);
	}

	public void setLogoImageView(boolean isShow) {
		mLogoImageView.setVisibility(isShow ? VISIBLE : GONE);
	}

	public void setCSPagerSlidingTabStripVisibility(boolean isShow) {
		mCSPagerSlidingTabStrip.setVisibility(isShow ? VISIBLE : GONE);
	}

	public void setTitleViewVisibility(boolean isShow) {
		mTitleView.setVisibility(isShow ? VISIBLE : GONE);
	}

	public void setRightMoreButtonVisibility(boolean isShow) {
		mRightMoreButton.setVisibility(isShow ? VISIBLE : GONE);
	}

	public void setLeftButtonIconVisibility(boolean isShow) {
		mLeftCancelButton.setVisibility(isShow ? VISIBLE : GONE);
	}

	public void setRightEditButtonVisibility(boolean isShow) {
		mRightEditButton.setVisibility(isShow ? VISIBLE : GONE);
	}

	public void setSelectAllButtonVisibility(boolean isShow) {
		mRightSelectButton.setVisibility(isShow ? VISIBLE : GONE);
	}

	public void setSettingTipVisible(int visibility) {
		if (mSettingTipView != null) {
			mSettingTipView.setVisibility(visibility);
		}

	}
	
	//oddshou############
    public void setSettingImage(int resource){
        if (mShowSetting && mSettingButton != null) {
            mSettingButton.setImageResource(resource);
        }
    }
    //oddshou############

	public void setManageTipVisible(boolean visibility, int size) {
		if (mTipView != null) {
			mTipView.setVisibility(visibility ? View.VISIBLE : View.GONE);
			mTipView.setText(size + "");
		}

	}

	public interface OnActionBarClickListener {
		// public static final int HOMEUP_BNT = 0x01;
		int MANAGE_BNT = 0x02;
		int SETTING_BNT = 0x03;
		int SEARCH_BNT = 0x04;
		int RETURN_BNT = 0x05;
		int LEFT_BNT = 0x06;
		int RIGHT_BNT = 0x07;
		int SELECT_BNT = 0x08;
		int MORE_BNT = 0x09;

		void onActionBarClicked(int position);
	}

	public PagerSlidingTabStrip getPagerSlidingTabStrip() {
		return mCSPagerSlidingTabStrip;
	}

	// ************ add by August 09-29 ****************\\
	private boolean isMore;
	private TouchOutsideListener mTouchOutsideListener;

	public interface TouchOutsideListener {
		void onTouchOut();
	}

	public void setTouchOutsideListener(
			TouchOutsideListener touchOutsideListener) {
		mTouchOutsideListener = touchOutsideListener;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isMore) {
			mTouchOutsideListener.onTouchOut();
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isMore) {
			return true;
		}
		return super.onTouchEvent(ev);
	}

	public void setMore(boolean isMore) {
		this.isMore = isMore;
	}

	// ************ add by August 09-29 ****************\\
}
