package com.hykj.gamecenter.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.hykj.gamecenter.R;

public class UserCommentSubmitDialog extends Dialog
{

    public UserCommentSubmitDialog( Context context )
    {
	super( context , R.style.MyDialog );
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
	super.onCreate( savedInstanceState );
	setContentView( R.layout.user_comment_submit_dialog );
    }

}
