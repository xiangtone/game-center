
package com.hykj.gamecenter.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hykj.gamecenter.R;

public class CSAlertDialog extends Dialog
{

    private Context mContext;
    private TextView mTipTextView;
    private TextView mTitleTextView;
    private CheckBox mCheck;
    private LinearLayout mCheckBoxLayout;
    private TextView mCheckTitleView;

    //设置title
    private String mTitle;
    private String mToastTip;
    private Button mLeftBtn;
    private String mLeftBtnTitle;
    private String mCheckTitle;

    private Button mRightBtn;
    private String mRightBtnTitle;

    private View.OnClickListener mLeftBtnListener;
    private View.OnClickListener mRightBtnListener;

    private View.OnClickListener mCheckBoxListener;

    private LinearLayout mPlAlertDialogLayout;

    private boolean b = false;

    private boolean isChecked = true;

    private int mPlAlertDialogLayoutHeight = 0;

    public CSAlertDialog(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CSAlertDialog(Context context, String tip, boolean b, boolean checked)
    {
        super(context, R.style.CSAlertDialog);
        this.mContext = context;
        this.mToastTip = tip;
        this.b = b;
        this.isChecked = checked;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }
    public void setTips(String tisp){
        mToastTip = tisp;
        if (mTipTextView != null) {
            mTipTextView.setText( mToastTip );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.csl_cs_alert_dialog);

        mPlAlertDialogLayout = (LinearLayout) findViewById(R.id.csl_alert_dialog_layout);

        mTipTextView = (TextView) findViewById(R.id.tip);
        mTipTextView.setText(mToastTip);
        mTitleTextView = (TextView) findViewById(R.id.title);

        int lineCount = mTipTextView.getLineCount();
        Log.e("scroll", "lineCount = " + lineCount);

        if (mTitle != null && !"".equals(mTitle))
        {
            mTitleTextView.setText(mTitle);
            mTitleTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            mTitleTextView.setVisibility(View.GONE);
        }

        mCheck = (CheckBox) findViewById(R.id.check);
        mCheckBoxLayout = (LinearLayout) findViewById(R.id.check_box_layout);
        mCheckBoxLayout.setVisibility(b ? View.VISIBLE : View.GONE);
        if (b) {
            mCheck.setChecked(isChecked);
        }
        mCheck.setOnClickListener(mCheckBoxListener == null ? mCheckBoxDefaultListener
                : mCheckBoxListener);
        mCheckTitleView = (TextView) findViewById(R.id.check_title);
        mCheckTitleView.setText(mCheckTitle == null ? mContext
                .getString(R.string.csl_check_default_title) : mCheckTitle);

        mLeftBtn = (Button) findViewById(R.id.left);
        mLeftBtn.setText(mLeftBtnTitle == null ? mContext.getString(R.string.csl_cancel)
                : mLeftBtnTitle);
        mRightBtn = (Button) findViewById(R.id.right);
        mRightBtn.setText(mRightBtnTitle == null ? mContext.getString(R.string.csl_sure)
                : mRightBtnTitle);
        mLeftBtn.setOnClickListener(mLeftBtnListener == null ? mLeftBtnDefautListener
                : mLeftBtnListener);
        mRightBtn.setOnClickListener(mRightBtnListener == null ? mRightBtnDefautListener
                : mRightBtnListener);

    }

    private View.OnClickListener mLeftBtnDefautListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            dismiss();

        }
    };

    private View.OnClickListener mRightBtnDefautListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            dismiss();
        }
    };

    private View.OnClickListener mCheckBoxDefaultListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            if (!mCheck.isChecked())
            {
                mCheck.setChecked(false);
            }
            else
            {
                mCheck.setChecked(true);
            }
        }
    };

    public void addLeftBtnListener(View.OnClickListener listener)
    {
        this.mLeftBtnListener = listener;
    }

    public void addRightBtnListener(View.OnClickListener listener)
    {
        this.mRightBtnListener = listener;
    }

    public void addCheckBoxListener(View.OnClickListener listener)
    {
        this.mCheckBoxListener = listener;
    }

    public void setmLeftBtnTitle(String mLeftBtnTitle)
    {
        this.mLeftBtnTitle = mLeftBtnTitle;
    }

    public void setmRightBtnTitle(String mRightBtnTitle)
    {
        this.mRightBtnTitle = mRightBtnTitle;
    }

    public void setmCheckTitle(String mCheckTitle)
    {
        this.mCheckTitle = mCheckTitle;
    }

    @Override
    public void onBackPressed()
    {
        //	if( isShowing( ) )
        //	{
        //	    return;
        //	}
        super.onBackPressed();
    }

}
