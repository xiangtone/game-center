
package com.hykj.gamecenter.ui.widget;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hykj.gamecenter.R;

public class GiftDialog extends Dialog {

    private Context context;
    private String mGiftCode;
    private TextView mTextIdCode;
    private ImageView mImgClose;


    public GiftDialog(Context context, String giftCode) {
        this(context, R.style.CSAlertDialog, giftCode);
        this.context = context;
        // TODO Auto-generated constructor stub
        this.mGiftCode = giftCode;

    }

    public GiftDialog(Context context, int theme, String giftCode) {
        super(context, theme);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gift);
//        mPlAlertDialogLayout = (LinearLayout) findViewById(R.id.csl_alert_dialog_layout);
        mTextIdCode = (TextView) findViewById(R.id.textIdCode);
        mTextIdCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(null, mTextIdCode.getText());
                cm.setPrimaryClip(clipData);
                CSToast.show(context, context.getString(R.string.idcode_copy_successed));
                return false;
            }
        });
        if (!TextUtils.isEmpty(mGiftCode)) {
            mTextIdCode.setText(mGiftCode);
        }

        mImgClose = (ImageView) findViewById(R.id.imgClose);
        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiftDialog.this.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
