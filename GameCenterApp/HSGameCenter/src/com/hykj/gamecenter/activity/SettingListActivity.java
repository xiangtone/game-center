
package com.hykj.gamecenter.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.fragment.SettingListFragment;
import com.hykj.gamecenter.fragment.SettingListFragment.SettingDetailFragment;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.utils.SystemBarTintManager;

/**
 * An activity representing a list of Setting. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link SettingDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link SettingListFragment} and the item details (if present) is a
 * {@link SettingDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link SettingListFragment.Callbacks} interface to listen for item
 * selections.
 */
// public class SettingListActivity extends FragmentActivity implements
// OnNaviChangeListener, OnActionBarClickListener {
public class SettingListActivity extends FragmentActivity implements
        OnActionBarClickListener {
    private final SettingDetailFragment detailFagment = null;
    // private final SettingFeedBackFragment feedBackFragment = null;
    // private SettingAboutFragment aboutFragment = null;
    private CSCommonActionBar mActionBar = null;

    @Override
    public void onActionBarClicked(int position, View view) {
        switch (position) {
            case CSCommonActionBar.OnActionBarClickListener.RETURN_BNT:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (App.getDevicesType() == App.PHONE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_setting_twopane);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        Resources res = getResources();
        if ((mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar)) != null) {
            mActionBar.SetOnActionBarClickListener(this);
            mActionBar.setTitle(res.getString(R.string.setting));
        }

        // if (detailFagment == null)
        // detailFagment = new SettingDetailFragment();
        // getSupportFragmentManager().beginTransaction().replace(R.id.setting_detail_container,
        // detailFagment).commit();

        // if (findViewById(R.id.setting_detail_container) != null) {
        // // The detail container view will be present only in the
        // // large-screen layouts (res/values-large and
        // // res/values-sw600dp). If this view is present, then the
        // // activity should be in two-pane mode.
        // // mTwoPane = true;
        //
        // // In two-pane mode, list items should be given the
        // // 'activated' state when touched.
        // // (
        // ((SettingListFragment)
        // getSupportFragmentManager().findFragmentById(R.id.setting_list)).setNaviChangeListener(this);
        // onNaviMenuSelected(SettingListFragment.SETTING);
        // }
        // TODO: If exposing deep links into your app, handle intents here.
    }

    // @Override
    // public void onNaviMenuSelected(int index) {
    // switch (index) {
    // case SettingListFragment.SETTING:
    // if (detailFagment == null)
    // detailFagment = new SettingDetailFragment();
    // getSupportFragmentManager().beginTransaction().replace(R.id.setting_detail_container,
    // detailFagment).commit();
    //
    // break;
    // // case SettingListFragment.FEEDBACK:
    // // if (feedBackFragment == null)
    // // feedBackFragment = new SettingFeedBackFragment();
    // //
    // getSupportFragmentManager().beginTransaction().replace(R.id.setting_detail_container,
    // // feedBackFragment).commit();
    // // break;
    // case SettingListFragment.ABOUT:
    // if (aboutFragment == null)
    // aboutFragment = new SettingAboutFragment();
    // getSupportFragmentManager().beginTransaction().replace(R.id.setting_detail_container,
    // aboutFragment).commit();
    // break;
    //
    // default:
    // break;
    // }
    // }

    @Override
    public void onBackPressed() {
        App.getSettingContent().savePreferences();
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        App.getSettingContent().savePreferences();
        super.onPause();
        // finish( );
    }

    /**
     * Callback method from {@link SettingListFragment.Callbacks} indicating
     * that the item with the given ID was selected.
     */
    // @Override
    // public void onItemSelected( String id )
    // {
    // if( mTwoPane )
    // {
    // // In two-pane mode, show the detail view in this activity by
    // // adding or replacing the detail fragment using a
    // // fragment transaction.
    // Bundle arguments = new Bundle( );
    // arguments.putString( SettingDetailFragment.ARG_ITEM_ID , id );
    // SettingDetailFragment fragment = new SettingDetailFragment( );
    // fragment.setArguments( arguments );
    // getSupportFragmentManager( ).beginTransaction( ).replace(
    // R.id.setting_detail_container , fragment ).commit( );
    //
    // }
    // else
    // {
    // // In single-pane mode, simply start the detail activity
    // // for the selected item ID.
    // Intent detailIntent = new Intent( this , SettingDetailActivity.class );
    // detailIntent.putExtra( SettingDetailFragment.ARG_ITEM_ID , id );
    // startActivity( detailIntent );
    // }
    // }
}
