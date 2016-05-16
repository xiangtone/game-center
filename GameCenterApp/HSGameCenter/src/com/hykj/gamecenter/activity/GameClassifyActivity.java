
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.adapter.ClassifyListAdapter;
import com.hykj.gamecenter.controller.ProtocolListener.CLASSIFY_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_CLASS;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.ORDER_BY;
import com.hykj.gamecenter.controller.ProtocolListener.SUB_TYPE;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.db.CSACContentProvider;
import com.hykj.gamecenter.db.CSACDatabaseHelper.GroupInfoColumns;
import com.hykj.gamecenter.logic.entry.ISaveInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSLoadingUIListView;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.SystemBarTintManager;
import com.hykj.gamecenter.utils.UITools;
import com.hykj.gamecenter.utilscs.LogUtils;

import java.util.ArrayList;

public class GameClassifyActivity extends Activity
{

    private static final String TAG = "ClassifyFragment";

    public static final int CATEGORY_GAME_REPORT = 6000000;
    public static final int TIP_GAME_REPORT = 7000000;

    private ArrayList<GroupInfo> mGroupInfoList = new ArrayList<GroupInfo>();

    private CSLoadingUIListView mListView;
    private CSCommonActionBar mActionBar;

    //    private GroupListAdapter mAdapter;
    private ClassifyListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.i("ClassifyFragment", "onCreateView");

        if (App.getDevicesType() == App.PHONE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.game_classify_activity);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar);
        mActionBar.SetOnActionBarClickListener(actionBarListener);
        mActionBar.setTitle(getString(R.string.game_classify_title));
        //	mActionBar.setLogoTitle( getString( R.string.game_classify_title ) );
        mListView = (CSLoadingUIListView) findViewById(R.id.app_list);
        mListView.setFooterPullEnable(false);
        mListView.setHeaderPullEnable(false);

        LogUtils.e("MAIN_TYPE.GAME_CLASS=" + MAIN_TYPE.GAME_CLASS);
        LogUtils.e("STATIS_TYPE.CLASSIFY=" + ReportConstants.STATIS_TYPE.CLASSIFY);

        mAdapter = new ClassifyListAdapter(this, MAIN_TYPE.GAME_CLASS, ReportConstants.STATIS_TYPE.CLASSIFY,
                UITools.getColumnNumber(this), CLASSIFY_TYPE.GAME_CLASSIFY);
        mListView.setAdapter(mAdapter);

        if (null != savedInstanceState)
        {
            mGroupInfoList = (ArrayList<GroupInfo>) savedInstanceState
                    .getSerializable(ISaveInfo.GROUP_ELEM_INFO);
            //	    savedInstanceState.clear( );
        }
        if (mGroupInfoList.size() <= 0)
        {
            reqDataList();
        }

        mAdapter.appendData(mGroupInfoList);
        mAdapter.notifyDataSetChanged();

    }

    private void handleAction()
    {
        String action = getIntent().getAction();
        if (action != null && !action.isEmpty()
                && action.equals("com.hykj.gamecenter.activity.GameClassifyActivity"))
        {

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putSerializable(ISaveInfo.GROUP_ELEM_INFO, mGroupInfoList);
        super.onSaveInstanceState(outState);
    }

    private final OnActionBarClickListener actionBarListener = new OnActionBarClickListener()
    {

        @Override
        public void onActionBarClicked(int position, View view)
        {

            switch (position)
            {
                case CSCommonActionBar.OnActionBarClickListener.RETURN_BNT:
                    onBackPressed();
                    break;
                case CSCommonActionBar.OnActionBarClickListener.MANAGE_BNT:
//                    Intent intentManage = new Intent(GameClassifyActivity.this,
//                            AppManageActivity.class);
//                    startActivity(intentManage);
                    Intent intent = new Intent(GameClassifyActivity.this, HomePageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(HomePageActivity.KEY_SELECT_ITEM, HomePageActivity.PAGE_INDEX.INDEX_UPDATE);
                    startActivity(intent);
                    break;
                case CSCommonActionBar.OnActionBarClickListener.SETTING_BNT:
                    Intent intentSetting = new Intent(GameClassifyActivity.this,
                            SettingListActivity.class);
                    startActivity(intentSetting);
                    break;
                case CSCommonActionBar.OnActionBarClickListener.SEARCH_BNT:
                    Intent intentSearch = new Intent(GameClassifyActivity.this,
                            SearchActivity.class);
                    intentSearch.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.ALL);
                    intentSearch.putExtra(KEY.SUB_TYPE, SUB_TYPE.ALL);
                    intentSearch.putExtra(KEY.ORDERBY, ORDER_BY.DOWNLOAD);
                    startActivity(intentSearch);
                    break;
                default:
                    break;
            }
        }
    };

    private void reqDataList()
    {
        mGroupInfoList.clear();
        Cursor classifyCursor = getContentResolver().query(
                CSACContentProvider.GROUPINFO_CONTENT_URI,
                null,
                GroupInfoColumns.GROUP_CLASS + "=" + GROUP_CLASS.GAME_CLASSIFY_CLASS + " and "
                        + GroupInfoColumns.GROUP_TYPE + ">"
                        + GROUP_TYPE.ALL_ONLY_GAMES_TYPE, null, " order_no asc");
        if (classifyCursor != null && classifyCursor.moveToNext())
        {
            while (!classifyCursor.isAfterLast())
            {
                GroupInfo groupInfo = new GroupInfo();
                groupInfo.groupId = classifyCursor.getInt(classifyCursor
                        .getColumnIndex(GroupInfoColumns.GROUP_ID));
                groupInfo.groupClass = classifyCursor.getInt(classifyCursor
                        .getColumnIndex(GroupInfoColumns.GROUP_CLASS));
                groupInfo.groupType = classifyCursor.getInt(classifyCursor
                        .getColumnIndex(GroupInfoColumns.GROUP_TYPE));
                groupInfo.orderType = classifyCursor.getInt(classifyCursor
                        .getColumnIndex(GroupInfoColumns.ORDER_TYPE));
                groupInfo.orderNo = classifyCursor.getInt(classifyCursor
                        .getColumnIndex(GroupInfoColumns.ORDER_NO));
                groupInfo.recommWrod = classifyCursor.getString(classifyCursor
                        .getColumnIndex(GroupInfoColumns.RECOMM_WORD));
                groupInfo.groupName = classifyCursor.getString(classifyCursor
                        .getColumnIndex(GroupInfoColumns.GROUP_NAME));
                groupInfo.groupDesc = classifyCursor.getString(classifyCursor
                        .getColumnIndex(GroupInfoColumns.GROUP_DESC));
                groupInfo.groupPicUrl = classifyCursor.getString(classifyCursor
                        .getColumnIndex(GroupInfoColumns.GROUP_PIC_URL));
                groupInfo.startTime = classifyCursor.getString(classifyCursor
                        .getColumnIndex(GroupInfoColumns.START_TIME));
                groupInfo.endTime = classifyCursor.getString(classifyCursor
                        .getColumnIndex(GroupInfoColumns.END_TIME));
                //		Logger.d( TAG , "groupInfo.groupId --- " + groupInfo.groupId );
                //		Logger.d( TAG , "groupInfo.groupClass --- " + groupInfo.groupClass );
                //		Logger.d( TAG , "groupInfo.groupType --- " + groupInfo.groupType );
                //		Logger.d( TAG , "groupInfo.orderType --- " + groupInfo.orderType );
                //		Logger.d( TAG , "groupInfo.orderNo --- " + groupInfo.orderNo );
                //		Logger.d( TAG , "groupInfo.recommWrod --- " + groupInfo.recommWrod );
                //		Logger.d( TAG , "groupInfo.groupName --- " + groupInfo.groupName );
                //		Logger.d( TAG , "groupInfo.groupDesc --- " + groupInfo.groupDesc );
                //		Logger.d( TAG , "groupInfo.groupClass --- " + groupInfo.groupClass );
                LogUtils.d("groupInfo.groupId : " + groupInfo.groupId + "groupInfo.groupName : "
                        + groupInfo.groupName);
                //		if( groupInfo.groupId == 128 )
                //		{
                //		    LogUtils.d( "groupInfo.groupId : " + groupInfo.groupId );
                //		    mGroupInfoList.add( 0 , groupInfo );
                //		}
                //		else
                //		{
                mGroupInfoList.add(groupInfo);
                //		}

                classifyCursor.moveToNext();
            }
            classifyCursor.close();
        }
    }

    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        //	mUiHandler.sendEmptyMessageDelayed( MSG_REFRESH_ADAPTER , 100 );
        super.onResume();
    }

    //    private static final int MSG_REFRESH_ADAPTER = 10002;
    //    private Handler mUiHandler = new Handler( )
    //    {
    //	@Override
    //	public void handleMessage( android.os.Message msg )
    //	{
    //	    switch ( msg.what )
    //	    {
    //		case MSG_REFRESH_ADAPTER :
    //		    if( mAdapter != null )
    //		    {
    //			//			mAdapter.setDisplayImage( true );
    //			mAdapter.notifyDataSetChanged( );
    //		    }
    //		default :
    //		    break;
    //	    }
    //	};
    //    };

    @Override
    public void onDestroy()
    {
        Logger.i("ClassifyFragment", "onDestory");
        super.onDestroy();
    }

    /**
     * 获取横竖屏需要保存的数据
     */
    public ArrayList<GroupInfo> saveInfo()
    {
        return mGroupInfoList;
    }

}
