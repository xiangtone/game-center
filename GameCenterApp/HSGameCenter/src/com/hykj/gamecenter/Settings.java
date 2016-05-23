/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hykj.gamecenter;

import android.content.Context;
import android.content.res.Resources;

import com.hykj.gamecenter.utilscs.CSLibSettings;

/**
 * Settings related utilities.
 */
public class Settings {
    // Number of pixels to add to the dragged item for scaling

    // 屏幕缩略模式下文件夹位置调整
    public static boolean ACTION_BAR_SHOW_LOGO_ICON = true;//0.不显示;1.显示
    public static boolean ACTION_BAR_SHOW_LOGO_TITLE = true;//0.不显示;1.显示
    public static boolean ACTION_BAR_SHOW_INDICATE = true;//0.不显示;1.显示
    public static int ACTION_BAR_INDICATE_TYPE = 0; //0.矩形;1.图片;
    public static boolean ACTION_BAR_DRAW_UNDERLINE = true;

    public static void loadSettings( Context context ) {
	Resources resources = context.getResources( );

	ACTION_BAR_SHOW_LOGO_ICON = resources.getBoolean( R.bool.action_bar_show_logo_icon );
	ACTION_BAR_SHOW_LOGO_TITLE = resources.getBoolean( R.bool.action_bar_show_logo_title );
	ACTION_BAR_SHOW_INDICATE = resources.getBoolean( R.bool.action_bar_show_indicate );
	ACTION_BAR_INDICATE_TYPE = resources.getInteger( R.integer.action_bar_indicate_type );
	ACTION_BAR_DRAW_UNDERLINE = resources.getBoolean( R.bool.action_bar_draw_underline );
	//初始化
	CSLibSettings.ACTION_BAR_INDICATE_TYPE = ACTION_BAR_INDICATE_TYPE;
	CSLibSettings.ACTION_BAR_SHOW_INDICATE = ACTION_BAR_SHOW_INDICATE;
	CSLibSettings.ACTION_BAR_SHOW_LOGO_ICON = ACTION_BAR_SHOW_LOGO_ICON;
	CSLibSettings.ACTION_BAR_SHOW_LOGO_TITLE = ACTION_BAR_SHOW_LOGO_TITLE;
	CSLibSettings.ACTION_BAR_DRAW_UNDERLINE = ACTION_BAR_DRAW_UNDERLINE;

    }
}
