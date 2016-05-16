package com.hykj.gamecenter.ui.entry;

import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.hykj.gamecenter.R;

public class SettingItemHolder
{
    public ImageView itemIcon;
    public CheckedTextView ckTextView;

    public SettingItemHolder( View view )
    {
	itemIcon = (ImageView)view.findViewById( R.id.item_label );
	ckTextView = (CheckedTextView)view.findViewById( R.id.checkedTextView );
    }

}
