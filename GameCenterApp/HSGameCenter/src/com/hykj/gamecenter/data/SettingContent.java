
package com.hykj.gamecenter.data;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingContent {

    protected static final String TAG = "SettingContent";
    public List<SettingListItem> ITEMS = new ArrayList<SettingListItem>();
    public Map<String, SettingListItem> ITEM_MAP = new HashMap<String, SettingListItem>();

    private static SettingContent instance = null;
    private static SettingData settingData = null;

    public class SettingData {
        public boolean bDownloadedToSetup = true;// 下载完启动安装，默认打开，设置中不需要显示
        public boolean bDeletePackage = true;
        public boolean bWifiToDownload = true; // 默认关闭 ，设置中不需要显示
        public boolean bWifiAutoDownload = true; // 默认关闭 ，设置中不需要显示
        public boolean bAutoUpdate = true; // wifi网络下，待机时自动升级
    }

    // static {
    // // addItem(new SettingListItem("setting_only_wifi_download",
    // SettingListItem.DataType.toggle, new
    // SettingToggleData(R.string.setting_only_wifi_download,
    // // false)));
    //
    // // addItem(new SettingListItem("setting_boot_download_unfinish_download",
    // SettingListItem.DataType.toggle, new SettingToggleData(
    // // R.string.setting_boot_download_unfinish_download, false)));
    // //
    // // addItem(new SettingListItem("setting_downloaded_voice_tips",
    // SettingListItem.DataType.toggle, new SettingToggleData(
    // // R.string.setting_downloaded_voice_tips, false)));
    //
    // }

    public static class SettingStringDefine {
        public static String SSD_DOWNLOADED_TOSETUP = "setting_downloaded_tosetup";     //下载完成自动安装
        public static String SSD_WIFIAUTO_DOWNLOADED_TOSETUP = "setting_wifiauto_downloaded_tosetup";   //wifi环境下闲时自动下载
        public static String SSD_DELETE_PACKAGE = "setting_setup_delete_package";   //安装完成删除安装包
        public static String SSD_CHECK_UPDATE = "setting_check_update";     //检查更新
        public static String SSD_WIFI_TODOWNLOAD = "setting_wifi_todownloaded";     //只在wifi下下载
        public static String SSD_SHOW_ABOUT = "setting_show_about";
        public static String SSD_ALL_INSTALLED_APPS_AUTO_UPDATE = "setting_all_installed_apps_auto_update";
        public static String SSD_CLEAN_CACHE = "setting_clean_cache";
        public static String SSD_FEED_BACK = "setting_feed_back";       //用户反馈

    }

    public SettingData getSettingData() {
        return settingData;
    }

    public static synchronized SettingContent getInstance() {
        if (instance == null) {
            instance = new SettingContent();
            instance.init();
        }

        return instance;
    }

    private void init() {

        settingData = new SettingContent.SettingData();

        new Thread(new Runnable() {

            @Override
            public void run() {
                SharedPreferences preferences = App.getSharedPreference();


                //Log.d( "BulkDownloadListButton" , "no2 bWifiToDownload = " + settingData.bWifiToDownload );
                boolean bValue = preferences.getBoolean(SettingStringDefine.SSD_WIFI_TODOWNLOAD,
                        settingData.bWifiToDownload);
                // 默认关闭 ，设置中不需要显示
                settingData.bWifiToDownload =  bValue ;
                Logger.i(TAG, "wifi todownload "+ bValue, "oddshou");
                addItem(new SettingListItem(SettingStringDefine.SSD_WIFI_TODOWNLOAD,
                        SettingListItem.DataType.toggle, new SettingToggle(R.drawable.icon_wifi,
                                R.string.setting_only_wifi_download,
                                R.string.setting_only_wifi_download_detail_text,
                                SettingStringDefine.SSD_WIFI_TODOWNLOAD, bValue, changelistener)));
                //-------------------------------
                bValue = preferences.getBoolean(SettingStringDefine.SSD_DOWNLOADED_TOSETUP, true);

                // 下载完启动安装，默认打开，设置中不需要显示
                settingData.bDownloadedToSetup = /* bValue */true;
                /*
                 * addItem(new SettingListItem(
                 * SettingStringDefine.SSD_DOWNLOADED_TOSETUP,
                 * SettingListItem.DataType.toggle, new SettingToggle(
                 * R.drawable.icon_installation,
                 * R.string.setting_downloaded_tosetup,
                 * SettingStringDefine.SSD_DOWNLOADED_TOSETUP, bValue,
                 * changelistener)));
                 */


                bValue = preferences.getBoolean(
                        SettingStringDefine.SSD_WIFIAUTO_DOWNLOADED_TOSETUP, true);
                // 在wifi环境下自动开启下载任务
                settingData.bWifiAutoDownload =  bValue ;
                addItem(new SettingListItem(SettingStringDefine.SSD_WIFIAUTO_DOWNLOADED_TOSETUP,
                        SettingListItem.DataType.toggle, new SettingToggle(R.drawable.icon_wifi,
                                R.string.setting_wifiauto_download,
                                R.string.setting_wifiauto_download_detail_text,
                                SettingStringDefine.SSD_WIFIAUTO_DOWNLOADED_TOSETUP, bValue,
                                changelistener)));



                bValue = preferences.getBoolean(
                        SettingStringDefine.SSD_ALL_INSTALLED_APPS_AUTO_UPDATE, true);
                settingData.bAutoUpdate = bValue;
                /*
                 * 暂时屏蔽 addItem(new SettingListItem(
                 * SettingStringDefine.SSD_ALL_INSTALLED_APPS_AUTO_UPDATE,
                 * SettingListItem.DataType.toggle, new SettingToggle(
                 * R.drawable.icon_delete, R.string.setting_auto_update_text,
                 * R.string.setting_auto_update_details_text,
                 * SettingStringDefine.SSD_ALL_INSTALLED_APPS_AUTO_UPDATE,
                 * bValue, changelistener)));
                 */


                bValue = preferences.getBoolean(SettingStringDefine.SSD_DELETE_PACKAGE, true);
                settingData.bDeletePackage = bValue;
                addItem(new SettingListItem(SettingStringDefine.SSD_DELETE_PACKAGE,
                        SettingListItem.DataType.toggle,
                        new SettingToggle(R.drawable.icon_delete,
                                R.string.setting_setup_delete_package,
                                R.string.setting_delete_pkg_details_text,
                                SettingStringDefine.SSD_DELETE_PACKAGE,
                                bValue, changelistener)));

                //################oddshou 暂时不加 清除缓存，本身 安装包 安装完成就会删除，没有必要清除缓存2015.07.15
                //		addItem( new SettingListItem( SettingStringDefine.SSD_CLEAN_CACHE , SettingListItem.DataType.text ,
                //			new SettingText( SettingStringDefine.SSD_CLEAN_CACHE , R.string.setting_clean_cache , 0 ) ) );

                addItem(new SettingListItem(SettingStringDefine.SSD_FEED_BACK, SettingListItem.DataType.text,
                        new SettingText(SettingStringDefine.SSD_FEED_BACK,
                                R.string.title_feedback,
                                0)));

                addItem(new SettingListItem(SettingStringDefine.SSD_CHECK_UPDATE,
                        SettingListItem.DataType.text,
                        new SettingText(SettingStringDefine.SSD_CHECK_UPDATE,
                                R.string.setting_check_update_text,
                                R.string.setting_check_update_details_text)));

                /*
                 * addItem(new
                 * SettingListItem(SettingStringDefine.SSD_SHOW_ABOUT,
                 * SettingListItem.DataType.about, new SettingText(
                 * SettingStringDefine.SSD_SHOW_ABOUT,
                 * R.string.setting_show_about)));
                 */

            }
        }).start();
    }

    private final SettingValueChangeListener changelistener = new SettingValueChangeListener() {

        @Override
        public void OnBooleanValueChange(String id, Boolean bNewValue) {
            if (id.equals(SettingStringDefine.SSD_DOWNLOADED_TOSETUP)) {
                settingData.bDownloadedToSetup = bNewValue;
            }
            else if (id.equals(SettingStringDefine.SSD_DELETE_PACKAGE)) {
                settingData.bDeletePackage = bNewValue;
            }
            else if (id.equals(SettingStringDefine.SSD_WIFI_TODOWNLOAD)) {
                settingData.bWifiToDownload = bNewValue;
                //Log.e( "DownLoadButton" , "settingData.bWifiToDownload = " + settingData.bWifiToDownload );
            }
            else if (id.equals(SettingStringDefine.SSD_WIFIAUTO_DOWNLOADED_TOSETUP)) {
                settingData.bWifiAutoDownload = bNewValue;
            }
            else if (id.equals(SettingStringDefine.SSD_ALL_INSTALLED_APPS_AUTO_UPDATE)) {
                settingData.bAutoUpdate = bNewValue;
            }

        }
    };

    public void savePreferences() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                SharedPreferences preferences = App.getSharedPreference();
                Editor editor = preferences.edit();

                SettingListItem item = ITEM_MAP.get(SettingStringDefine.SSD_DOWNLOADED_TOSETUP);
                if (item != null) {
                    editor.putBoolean(SettingStringDefine.SSD_DOWNLOADED_TOSETUP,
                            ((SettingToggle) item.mData).isToggle());
                }

                item = ITEM_MAP.get(SettingStringDefine.SSD_DELETE_PACKAGE);
                if (item != null) {
                    editor.putBoolean(SettingStringDefine.SSD_DELETE_PACKAGE,
                            ((SettingToggle) item.mData).isToggle());
                }

                item = ITEM_MAP.get(SettingStringDefine.SSD_WIFI_TODOWNLOAD);
                if (item != null) {
                    editor.putBoolean(SettingStringDefine.SSD_WIFI_TODOWNLOAD,
                            ((SettingToggle) item.mData).isToggle());
                }

                item = ITEM_MAP.get(SettingStringDefine.SSD_WIFIAUTO_DOWNLOADED_TOSETUP);
                if (item != null) {
                    editor.putBoolean(SettingStringDefine.SSD_WIFIAUTO_DOWNLOADED_TOSETUP,
                            ((SettingToggle) item.mData).isToggle());
                }

                item = ITEM_MAP.get(SettingStringDefine.SSD_ALL_INSTALLED_APPS_AUTO_UPDATE);

                if (item != null) {
                    editor.putBoolean(SettingStringDefine.SSD_ALL_INSTALLED_APPS_AUTO_UPDATE,
                            ((SettingToggle) item.mData).isToggle());
                }
                editor.commit();
            }
        }).start();
    }

    private void addItem(SettingListItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.mID, item);
    }

    public interface SettingValueChangeListener {
        void OnBooleanValueChange(String id, Boolean bNewValue);
    }

    public abstract class SettingView {
        protected final String mID;

        public SettingView(String id) {
            mID = id;
        }

        public boolean IsEqualStringID(String sID) {
            return (mID != null && mID.equals(sID));
        }
    }

    public class SettingToggle extends SettingView {
        private final int mContentStrResrouceID;
        private final int mContentStr2ResrouceID; // 对于mContentStrResrouceID的解释
        private final int mContentImageResrouceID;
        private boolean bToggle;
        private final SettingValueChangeListener mListener;

        public int getResourceID() {
            return mContentStrResrouceID;
        }

        public int getResourceID2() {
            return mContentStr2ResrouceID;
        }

        public int getImageResourceID() {
            return mContentImageResrouceID;
        }

        public Boolean isToggle() {
            return bToggle;
        }

        public SettingToggle(int rscImageID, int rscID, int detailofResId, String id,
                boolean toggle,
                SettingValueChangeListener listener) {
            super(id);
            mListener = listener;
            mContentStrResrouceID = rscID;
            mContentStr2ResrouceID = detailofResId;
            mContentImageResrouceID = rscImageID;
            bToggle = toggle;
        }

        public void setToggle(Boolean bToggle) {
            this.bToggle = bToggle;
            if (mListener != null)
                mListener.OnBooleanValueChange(mID, bToggle);
        }
    }

    public class SettingText extends SettingView {

        public final int mResId;
        public final int mContentStr2ResrouceID; // 对于mContentStrResrouceID的解释

        public SettingText(String id, int resId, int detailofResId) {
            super(id);
            mResId = resId;
            mContentStr2ResrouceID = detailofResId;
        }

    }

    public static class SettingListItem {
        public String mID;
        public DataType mType;
        public SettingView mData;

        public enum DataType {
            toggle, text, about
        }

        public SettingListItem(String id, DataType type, SettingView data) {
            mID = id;
            mType = type;
            mData = data;
        }

    }
}
