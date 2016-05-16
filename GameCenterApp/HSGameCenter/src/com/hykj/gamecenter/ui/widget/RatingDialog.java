package com.hykj.gamecenter.ui.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.hykj.gamecenter.R;

public class RatingDialog extends Dialog {
    private RatingBar mRatingBar;
    private TextView mTipTextView;
    private TextView mTitleTextView;
    private String mTitle;
    private String mToastTip;
    private android.view.View.OnClickListener mOnclickListen;
    private Button mSubmitBtn;
    private Context mContext;
    private TextView mTextHint;
    private String [] mRatingHint;
    
    public RatingDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public RatingDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
    }

    public RatingDialog(Context context, String title) {
        // TODO Auto-generated constructor stub
        super(context, R.style.CSAlertDialog);
        mTitle = title;
        this.mContext = context;
    }
    public void setTips(String tisp){
        mToastTip = tisp;
        if (mTipTextView != null) {
            mTipTextView.setText( mToastTip );
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_dialog_layout);
        mRatingBar = (RatingBar)findViewById(R.id.app_rating);
        mRatingBar.setOnRatingBarChangeListener(mRatingChangeListen);
        mTipTextView = (TextView) findViewById(R.id.tip);
        mTitleTextView = (TextView) findViewById(R.id.title);
        if (!TextUtils.isEmpty(mTitle))
        {
            mTitleTextView.setText(mTitle);
            mTitleTextView.setVisibility(View.VISIBLE);
        }else
        {
            mTitleTextView.setVisibility(View.GONE);
        }
        
        if (!TextUtils.isEmpty(mToastTip)) {
            mTipTextView.setText( mToastTip );
            mTipTextView.setVisibility(View.VISIBLE);
        }else{
            mTipTextView.setVisibility(View.GONE);
        }
        
        mTextHint = (TextView)findViewById(R.id.textHint);
        
        mSubmitBtn = (Button)findViewById(R.id.btnSubmit);
        mSubmitBtn.setOnClickListener(mClickListen);
         
    }
    
    private OnRatingBarChangeListener mRatingChangeListen = new OnRatingBarChangeListener() {
        
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            // TODO Auto-generated method stub
            if (mRatingHint == null) {
                mRatingHint = mContext.getResources().getStringArray(R.array.rating_hint);
            }
            mTextHint.setText(mRatingHint [(int)rating]);
            mTextHint.setTextColor(mContext.getResources().getColor(R.color.holo_gray));
        }
    };
    
    private android.view.View.OnClickListener mClickListen = new android.view.View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (mRatingBar.getRating() < 1) {
                mTextHint.setTextColor(mContext.getResources().getColor(R.color.red));
                return;
            }
            
            if (mSubmitListen != null) {
                mSubmitListen.submit((int)mRatingBar.getRating());
            }
            dismiss();
        }

    };
    
    public interface ISubmit{
        void submit(int rating);
    }
    
    private ISubmit mSubmitListen;
    public void setSubmitListen(ISubmit submitListen){
        mSubmitListen = submitListen;
    }

}
