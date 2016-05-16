package com.hykj.gamecenter.logic.entry;

/**
 * Created by win7 on 2016/4/8.
 */
public class MsgDownload {
    // 消息标识
    public final static int DOWNLOAD_SIZE_EMPTY = 2001;
    public final static int DOWNLOAD_SIZE_NOT_EMPTY = 2002;
    public final static int DOWNLOAD_DEL_REMOVE = 2003;
    public final static int DOWNLOAD_DEL_ADD = 2004;

    public final static int HISTORY_DOWNLOAD_DEL_REMOVE = 2005;
    public final static int HISTORY_DOWNLOAD_DEL_ADD = 2006;

    public final static int INSTALL_FINISH_TO_DO = 3001;

    public final static int MSG_SCROLL_TO_APP_UPGRADE = 3002;
    public final static int MSG_SCROLL_TO_DOWNLOAD_MANAGER = 3003;
    public final static int MSG_FINISH_EDIT = 3004;
    public final static int MSG_ACTION_EDIT = 3005;
    public final static int MSG_REFRESH_DOWNLOAD_LIST_FRAGMENT = 3006;
    public final static int MSG_REFRESH_DOWNLOAD_LIST_ADAPTER = 3007;
}
