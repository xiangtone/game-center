
package com.hykj.gamecenter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.HomePageActivity;
import com.hykj.gamecenter.activity.SettingAboutActivity;
import com.hykj.gamecenter.adapter.SettingListAdapter;
import com.hykj.gamecenter.controller.ProtocolListener.ReqUpdateListener;
import com.hykj.gamecenter.controller.ReqUpdateController;
import com.hykj.gamecenter.net.logic.UpdateDownloadController;
import com.hykj.gamecenter.protocol.Updater.RspUpdate;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.MyLoginProcessDialog;
import com.hykj.gamecenter.ui.widget.CSAlertDialog;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.UpdateDialog;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.UpdateUtils;

import java.util.ArrayList;

//Fragment,activity都是MVC 中的C，Fragment是把包含较多的逻辑的Activity分解为多个逻辑（比如多tab页时）
public class SettingListFragment extends Fragment {
    private View mView;
    private ViewGroup mMenuParent;
    private final ArrayList<MenuItem> leftMenus = new ArrayList<MenuItem>();
    private OnNaviChangeListener mOnNaviChangeListener;
    private final int mCurrentNaviId = -1;
    private View mCurrentItemView;

    public final static int SETTING = 0x00;
    // public final static int FEEDBACK = 0x01;
    public final static int ABOUT = 0x01;

    public interface OnNaviChangeListener {
        void onNaviMenuSelected(int index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_setting_list, null);
        return mView;
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // setupMenu( );
    }

    public void setNaviChangeListener(OnNaviChangeListener listener) {
        mOnNaviChangeListener = listener;
    }

    // private final OnTouchListener mOnNaviTouchListener = new OnTouchListener(
    // )
    // {
    //
    // @Override
    // public boolean onTouch( View v , MotionEvent event )
    // {
    // if( event.getAction( ) == MotionEvent.ACTION_DOWN )
    // {
    // Integer menuId = (Integer)v.getTag( );
    // if( mCurrentNaviId != menuId )
    // {
    // selectMenu( menuId );
    // }
    // }
    // return false;
    // }
    // };

    // private void selectMenu( int menuId )
    // {
    // mCurrentItemView = mMenuParent.getChildAt( mCurrentNaviId );
    // setViewSelected( leftMenus.get( mCurrentNaviId ) , false );
    // mCurrentItemView = mMenuParent.getChildAt( menuId );
    // mCurrentNaviId = menuId;
    // setViewSelected( leftMenus.get( menuId ) , true );
    // mOnNaviChangeListener.onNaviMenuSelected( menuId );
    // }

    // private void setViewSelected( MenuItem item , boolean selected )
    // {
    // ImageView menuIcon = (ImageView)mCurrentItemView.findViewById(
    // R.id.left_menu_icon );
    // TextView menuText = (TextView)mCurrentItemView.findViewById(
    // R.id.left_menu_text );
    // mCurrentItemView.setBackgroundResource( selected ?
    // R.drawable.navi_menu_bg_pressed : R.drawable.navi_menu_bg );
    // menuIcon.setImageResource( selected ? item.iconSelectedId : item.iconId
    // );
    // menuText.setTextColor( selected ? getResources( ).getColor(
    // R.color.text_green_high ) : getResources( ).getColor(
    // R.color.csl_cs_sf_gray_tip ) );
    // }

    // private void setupMenu()
    // {
    // initialMenuItem( );
    // mMenuParent = (ViewGroup)mView.findViewById( R.id.menuRoot );
    // LinearLayout.LayoutParams llp = new LayoutParams(
    // android.view.ViewGroup.LayoutParams.WRAP_CONTENT ,
    // android.view.ViewGroup.LayoutParams.WRAP_CONTENT );
    // LayoutInflater inflater = getActivity( ).getLayoutInflater( );
    // for( MenuItem item : leftMenus )
    // {
    // View itemView = inflater.inflate( R.layout.home_navigator_item , null );
    // ImageView menuIcon = (ImageView)itemView.findViewById(
    // R.id.left_menu_icon );
    // TextView menuText = (TextView)itemView.findViewById( R.id.left_menu_text
    // );
    // menuIcon.setImageResource( item.iconId );
    // menuText.setText( item.label );
    // itemView.setTag( item.menuId );
    // itemView.setOnTouchListener( mOnNaviTouchListener );
    // mMenuParent.addView( itemView , llp );
    // }
    // mCurrentNaviId = 0;
    // mCurrentItemView = mMenuParent.getChildAt( 0 );
    // setViewSelected( leftMenus.get( 0 ) , true );
    // }

    // private void initialMenuItem()
    // {
    // Resources res = getResources( );
    // MenuItem item = new MenuItem( res.getString( R.string.csl_setting ) ,
    // SETTING , R.drawable.icon_set_setting ,
    // R.drawable.icon_set_setting_pressed );
    // leftMenus.add( item );
    // // item = new MenuItem(res.getString(R.string.csl_feedback), FEEDBACK,
    // // R.drawable.icon_set_feedback, R.drawable.icon_set_feedback_pressed);
    // // leftMenus.add(item);
    // item = new MenuItem( res.getString( R.string.csl_about ) , ABOUT ,
    // R.drawable.icon_set_about , R.drawable.icon_set_about_pressed );
    // leftMenus.add( item );
    // }

    // private class MenuItem
    // {
    // public String label;
    // public int menuId;
    // public int iconId;
    // public int iconSelectedId;
    //
    // public MenuItem( String label , int menuId , int iconId , int
    // iconSelectedId )
    // {
    // this.label = label;
    // this.menuId = menuId;
    // this.iconId = iconId;
    // this.iconSelectedId = iconSelectedId;
    // }
    // }

    public static class SettingDetailFragment extends Fragment {

        private ListView settingItemToggleView;
        private SettingListAdapter settingListAdapter;

        // private CSLoadingDialog mLoadingDialog;
        private UpdateDialog mUpdateDialog;
        private MyLoginProcessDialog mCheckingDialog;
        CSAlertDialog mCleanCacheDialogialog = null;

        public static final int MSG_CHECK_UPDATE_LOAD = 1201;
        private static final int MSG_CHECK_UPDATE = 1202;
        private static final int MSG_SHOW_UPDATE_DIALOG = 1203;
        private static final int MSG_SHOWTOAST = 1204;
        public static final int MSG_SHOW_ABOUT_DIALOG = 1205;
        public static final int MSG_REFRESH_LIST = 1206;
        public static final int MSG_CLEAN_CACHE = 1207;
        protected static final String TAG = "SettingDetailFragment";
        private final Handler mSettingUiHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_CHECK_UPDATE_LOAD:
                        if (mCheckingDialog == null) {
                            mCheckingDialog = new MyLoginProcessDialog(getActivity(),
                                    getString(R.string.checkupdate));
                        }
                        mCheckingDialog.show();
                        mSettingUiHandler.sendEmptyMessageDelayed(MSG_CHECK_UPDATE, 1000);
                        break;
                    case MSG_CHECK_UPDATE:
                        sendCheckUpdate();
                        break;

                    case MSG_CLEAN_CACHE:// 清除缓存
                        //                        ShowCleanCacheDialog();
                        break;
                    case MSG_SHOW_UPDATE_DIALOG:
                        if (settingListAdapter != null) {
                            settingListAdapter.notifyDataSetChanged();
                        }
                        if (getActivity() == null) {
                            return;
                        }

                        if (mUpdateDialog == null) {
                            mUpdateDialog = new UpdateDialog(getActivity(), (RspUpdate) msg.obj);
                        }
                        mUpdateDialog.setListeners(mUpdateListener, mUpdateCancelListener);
                        mUpdateDialog.show();

                        break;
                    case MSG_SHOWTOAST:
                        String strMsg = msg.getData().getString("msg");
                        CSToast.show(getActivity(), strMsg);
                        break;
                    case MSG_SHOW_ABOUT_DIALOG:
                        Intent intent = new Intent(getActivity(), SettingAboutActivity.class);
                        // intent.setClass( getActivity( ) , AboutActivity.class );
                        startActivity(intent);
                        // if( mAboutDialog == null )
                        // {
                        // mAboutDialog = new CSAboutDialog( getActivity( ) ,
                        // R.style.MyDialog );
                        // }
                        // mAboutDialog.setListener( mSureListener );
                        // mAboutDialog.show( );
                        break;
                    case MSG_REFRESH_LIST:
                        settingListAdapter.notifyDataSetChanged();
                        break;
                    case UpdateDownloadController.MSG_UPDATE_PROGRESS:
                        Bundle bundle = msg.getData();
                        long all_size = bundle.getLong(HomePageActivity.UPDATE_ALL);
                        long current_size = bundle.getLong(HomePageActivity.UPDATE_CURRENT);
                        int progress = (int) ((current_size * 100 / all_size));
                        if (mUpdateDialog != null) {
                            mUpdateDialog.setProgress(progress);
                        }
                        break;
                    case UpdateDownloadController.MSG_SET_PATH:
                        Bundle bundleP = msg.getData();
                        if (mUpdateDialog != null) {
                            mUpdateDialog.setAppPath(bundleP.getString(HomePageActivity.APP_PATH));
                        }
                    case UpdateDownloadController.MSG_DOWNLOAD_FAIL:
                        if (mUpdateDialog.isShowing()) {
                            mUpdateDialog.setDownloadFailStatus();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        private Button mBtnLoginOut;

        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        // public static final String ARG_ITEM_ID = "item_id";

        /**
         * The dummy content this fragment is presenting.
         */
        // private DummyContent.DummyItem mItem;

        /**
         * Mandatory empty constructor for the fragment manager to instantiate
         * the fragment (e.g. upon screen orientation changes).
         */
        public SettingDetailFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // if( getArguments( ).containsKey( ARG_ITEM_ID ) )
            // {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            // mItem = DummyContent.ITEM_MAP.get( getArguments( ).getString(
            // ARG_ITEM_ID ) );
            // }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_setting_detail, container, false);

            // ArrayList<String> toggleList = getToggleStringList();
            // ArrayAdapter<String> settingItemViewAdapter = new
            // ArrayAdapter<String>(getActivity(),
            // R.layout.fragment_setting_detail_item_toggle, toggleList);

            settingItemToggleView = (ListView) rootView.findViewById(R.id.setting_item_toggle);

            settingListAdapter = new SettingListAdapter();

            settingListAdapter.setHandler(mSettingUiHandler);

            settingItemToggleView.setAdapter(settingListAdapter);

            // settingItemToggleView.setItemChecked(0, true);
            // settingItemToggleView.setOnItemClickListener(listListener);

            mBtnLoginOut = (Button)rootView.findViewById(R.id.btnLoginOut);
            int uuid = App.getSharedPreference().getInt(StatisticManager.KEY_WIFI_UUID, 0);
            mBtnLoginOut.setEnabled(uuid != 0);
            mBtnLoginOut.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //退出登录
                    App.getSharedPreference().edit()
                            .putInt(StatisticManager.KEY_WIFI_UUID, 0)
                            .apply();
                    getActivity().finish();
                }
            });

            return rootView;
        }

        @Override
        public void onDestroyView() {
            // TODO Auto-generated method stub
            super.onDestroyView();

            if (mCleanCacheDialogialog != null && mCleanCacheDialogialog.isShowing()) {
                mCleanCacheDialogialog.dismiss();
                mCleanCacheDialogialog = null;
            }

            if (mUpdateDialog != null && mUpdateDialog.isShowing()) {
                mUpdateDialog.dismiss();
                mUpdateDialog = null;
            }

            dismissLoadingDialog();
            /*if( mCheckingDialog != null && mCheckingDialog.isShowing( ) )
              {
            mCheckingDialog.dismiss( );
            mCheckingDialog = null;
              }*/
        }

        private void sendCheckUpdate() {
            ReqUpdateController controller = new ReqUpdateController(mReqUpdateListener);
            controller.doRequest();
        }

        /*private void ShowCleanCacheDialog() {
            if (null == mCleanCacheDialogialog) {
                mCleanCacheDialogialog = new CSAlertDialog(getActivity(),
                        getString(R.string.tip_clean_cache),
                        false);
            }
            mCleanCacheDialogialog.setTitle(getString(R.string.clean_cache));
            mCleanCacheDialogialog.setmLeftBtnTitle(getString(R.string.do_clean));
            mCleanCacheDialogialog.setmRightBtnTitle(getString(R.string.do_no));
            mCleanCacheDialogialog.addLeftBtnListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    DataCleanManager.cleanApplicationData(getActivity());

                    mCleanCacheDialogialog.dismiss();
                }

            });
            mCleanCacheDialogialog.setCanceledOnTouchOutside(true);

            if (this.isVisible()) {
                mCleanCacheDialogialog.show();
            }

        }*/

        private final ReqUpdateListener mReqUpdateListener = new ReqUpdateListener() {

            @Override
            public void onNetError(int errCode, String errorMsg) {
                dismissLoadingDialog();
                showToast(getString(R.string.csl_error_msg_net_refresh));
            }

            @Override
            public void onReqFailed(int statusCode, String errorMsg) {
                dismissLoadingDialog();
                showToast(errorMsg);
            }

            @Override
            public void onRequpdateSucceed(RspUpdate updateInfo) {
                // Logger.d( "SettingDetailFragment" ,
                // " updateInfo.getUpdateType : " + updateInfo.getUpdateType( )
                // );
                // Logger.d( "SettingDetailFragment" , " updateInfo: " +
                // updateInfo.toString( ) );
                if (updateInfo.updateType == 0) {
                    UpdateUtils.setUpdatePreference(false, updateInfo.updateType);
                    showToast(getString(R.string.checkupdate_noupdate));
                    mSettingUiHandler.sendEmptyMessage(MSG_REFRESH_LIST);
                }
                else {
                    UpdateUtils.setUpdatePreference(true, updateInfo.updateType);
                    Message msg = mSettingUiHandler.obtainMessage();
                    msg.what = MSG_SHOW_UPDATE_DIALOG;
                    msg.obj = updateInfo;
                    mSettingUiHandler.sendMessage(msg);
                }
                dismissLoadingDialog();
            }

        };

        private void showToast(String strMsg) {
            Message msg = mSettingUiHandler.obtainMessage(MSG_SHOWTOAST);
            Bundle bundle = new Bundle();
            bundle.putString("msg", strMsg);
            msg.setData(bundle);
            mSettingUiHandler.sendMessage(msg);
        }

        private void dismissLoadingDialog() {
            if (mCheckingDialog != null && mCheckingDialog.isShowing()) {
                mCheckingDialog.dismiss();
            }
        }

        private final OnClickListener mUpdateListener = new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mUpdateDialog != null && mUpdateDialog.isShowing()) {
                    mUpdateDialog.dismiss();
                }
                if (UpdateUtils.getUpdateState()) // 已经在更新应用的话，就直接返回
                {
                    return;
                }
                Logger.d(TAG, "mUpdateListener click");
                UpdateUtils.saveUpdateState(true); // 保存应用正在更新的状态
                UpdateUtils.setUpdatePreference(false, 3);
                UpdateDownloadController controller = UpdateDownloadController.getInstance();
                //oddshou#########bugs### 自更新时设置 handler,修改了 homepage的一个handler到UpdateDownloadController中
                controller.setmHandler(mSettingUiHandler);
                //oddshou############
                controller.startDonwloadApk((RspUpdate) view.getTag());
            }
        };

        private final OnClickListener mUpdateCancelListener = new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mUpdateDialog != null && mUpdateDialog.isShowing()) {
                    mUpdateDialog.dismiss();
                }
            }
        };

    }
}
