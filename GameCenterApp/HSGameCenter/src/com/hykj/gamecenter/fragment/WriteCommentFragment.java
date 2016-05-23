
package com.hykj.gamecenter.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.WriteCommentActivity;
import com.hykj.gamecenter.protocol.Apps.UserCommentInfo;
import com.hykj.gamecenter.utils.UITools;

import java.util.Random;

public class WriteCommentFragment extends Fragment
{
    private final static String TAG = "WriteCommentFragment";
    private final static int MAX_INPUT_NUMBER = 150;
    private final static int RESULT_GROUP_NUMBER_PORTRAIT = 2;
    private final static int RESULT_GROUP_NUMBER_LANDSCAPE = 3;
    private View mainView;
    private GridView gridView;
    public EditText inputView;
    private TextView countView;
    private RatingBar starBar;
    private String[] gridStrArray;
    private String content = null;
    private int rating;
    private WriteCommentActivity mActivity;
    private int appId;
    private String contentKey;
    private String ratingKey;
    public LinearLayout gridContainer;
    public RelativeLayout inputContainer;
    private ArrayAdapter mAdapter;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        //	UserCommentInfo info = ( (WriteCommentActivity)getActivity( ) ).getmUserCommentInfo( );
        mActivity = (WriteCommentActivity) getActivity();
        appId = mActivity.getIntent().getIntExtra("appId", -1);
        mHandler = new Handler();
        initKey();

        getServerOrLocalComment();

        initRecommendComment();
        super.onCreate(savedInstanceState);
    }

    private void initKey()
    {
        StringBuffer contentBuf = new StringBuffer();
        contentBuf.append(appId).append("content");
        contentKey = contentBuf.toString();

        StringBuffer ratingBuf = new StringBuffer();
        ratingBuf.append(appId).append("rating");
        ratingKey = ratingBuf.toString();

    }

    private void getServerOrLocalComment()
    {
        UserCommentInfo info = null;
        try
        {
            if (mActivity.getIntent().getByteArrayExtra("userCommentInfo") != null)
            {
                info = UserCommentInfo.parseFrom(mActivity.getIntent().getByteArrayExtra(
                        "userCommentInfo"));
                content = info.comments;
                rating = info.userScore;
            }

        } catch (InvalidProtocolBufferNanoException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (info == null)
        {
            getCommentPreferences();
        }
    }

    private void initRecommendComment()
    {
        Random random = new Random();
        String[] strArray0 = getResources().getStringArray(
                R.array.app_comment_recommend_game_graphics);
        String[] strArray1 = getResources().getStringArray(R.array.app_comment_recommend_game_play);
        String[] strArray2 = getResources().getStringArray(
                R.array.app_comment_recommend_game_operate);
        String[] strArray3 = getResources().getStringArray(
                R.array.app_comment_recommend_game_player);
        String[] strArray4 = getResources().getStringArray(
                R.array.app_comment_recommend_game_stability);
        String[] strArray5 = getResources()
                .getStringArray(R.array.app_comment_recommend_game_story);
        int size = strArray0.length;
        gridStrArray = new String[] {
                strArray0[random.nextInt(size)], strArray1[random.nextInt(size)],
                strArray2[random.nextInt(size)], strArray3[random.nextInt(size)], strArray4[random
                        .nextInt(size)], strArray5[random.nextInt(size)]
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        mainView = inflater.inflate(R.layout.write_comment_fragment, null);
        gridView = (GridView) mainView.findViewById(R.id.input_hint);
        inputView = (EditText) mainView.findViewById(R.id.writeComment);
        countView = (TextView) mainView.findViewById(R.id.inputNum);
        starBar = (RatingBar) mainView.findViewById(R.id.app_rating);
        starBar.setOnRatingBarChangeListener(ratingBarlistener);
        gridContainer = (LinearLayout) mainView.findViewById(R.id.grid);
        inputContainer = (RelativeLayout) mainView.findViewById(R.id.input_edit);
        //	gridContainer.setBackgroundResource( R.drawable.grid_background_green );
        gridView.setNumColumns(UITools.isPortrait() ? RESULT_GROUP_NUMBER_PORTRAIT
                : RESULT_GROUP_NUMBER_LANDSCAPE);

        mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.user_comment_grid_cell,
                gridStrArray);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(gridViewListener);
        inputView.addTextChangedListener(inputWatcher);
        initContent();
        onConfig();
        return mainView;
    }

    private void initContent()
    {
        //	mHandler.postDelayed( new Runnable( )
        //	{
        //
        //	    @Override
        //	    public void run()
        //	    {
        if (content != null && !content.trim().isEmpty())
        {
            inputView.setText(content);
            inputView.setSelection(content.length());
        }
        if (rating != 0)
        {
            starBar.setRating(rating);
        }

        //	    }
        //	} , 100 );

    }

    private void onConfig()
    {
        if (UITools.getWindowsWidthPixel() < 850 || UITools.getWindowsHeightPixel() < 850)
        {
            int left = 0;
            int right = 0;
            if (UITools.isPortrait())
            {
                left = App.getAppContext().getResources()
                        .getDimensionPixelSize(R.dimen.write_user_comment_port_left);
                right = App.getAppContext().getResources()
                        .getDimensionPixelSize(R.dimen.write_user_comment_port_right);
            }
            else
            {
                left = App.getAppContext().getResources()
                        .getDimensionPixelSize(R.dimen.write_user_comment_land_left);
                right = App.getAppContext().getResources()
                        .getDimensionPixelSize(R.dimen.write_user_comment_land_right);
            }
            mainView.setPadding(left, 0, right, 0);

        }
    }

    private OnItemClickListener gridViewListener = new OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long rowId)
        {
            // TODO Auto-generated method stub
            //	    Logger.d( TAG , "storePos size :" + storePos.size( ) + " ----  " + storePos );
            //	    if( storePos.contains( position ) )
            //		return;
            //	    storePos.add( position );
            boolean isClick = (view.getTag() == null ? true
                    : (Boolean) view.getTag());
            if (isClick)
            {
                CharSequence chr = ((TextView) view).getText();

                //		Logger.d( TAG , "position: " + position + "---rowId: " + rowId + "--------- " + chr );
                inputView.append(chr);
                inputView.append("，");
            }

        }

    };

    private TextWatcher inputWatcher = new TextWatcher()
    {

        @Override
        public void afterTextChanged(Editable s)
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            if (s.length() > MAX_INPUT_NUMBER)
            {
                s = s.subSequence(0, MAX_INPUT_NUMBER);
                inputView.setText(s);
                inputView.setSelection(s.length());
            }
            if (inputView.getText().length() >= MAX_INPUT_NUMBER)
            {
                countView.setTextColor(getResources().getColor(R.color.red));
            }
            else
            {
                countView.setTextColor(getResources().getColor(R.color.csl_black_7f));
            }
            countView.setText(getActivity().getString(R.string.app_user_comment_char_number,
                    s.length()));
            //	    gridViewClickManager( s );
            delayRunGridView(s);
        }

    };

    private void gridViewClickManager(CharSequence s)
    {
        for (int i = 0; i < gridStrArray.length; i++)
        {
            CharSequence chs = ((TextView) gridView.getChildAt(i)).getText();
            if (s.toString().contains(chs))
            {
                gridView.getChildAt(i)
                        .setBackgroundResource(R.drawable.csl_button_white_press);
                ((TextView) gridView.getChildAt(i)).setTextColor(getResources().getColor(
                        R.color.csl_white));
                gridView.getChildAt(i).setTag(false);
            }
            else
            {
                gridView.getChildAt(i)
                        .setBackgroundResource(R.drawable.background_white);
                ((TextView) gridView.getChildAt(i)).setTextColor(getResources().getColor(
                        R.color.csl_black_7f));
                gridView.getChildAt(i).setTag(true);
            }
        }
    }

    private OnRatingBarChangeListener ratingBarlistener = new OnRatingBarChangeListener()
    {

        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
        {
            // TODO Auto-generated method stub
            //	    Logger.d( "OnRatingBarChangeListener" , "rating : " + rating );
            if ((int) rating == 0)
            {
                rating = (float) 1.0;
            }

            ratingBar.setRating(rating);

        }

    };

    public String getCommentContent()
    {
        return inputView.getText().toString();
    }

    public int getStarRating()
    {
        return (int) starBar.getRating();
    }

    private void getCommentPreferences()
    {

        //	new Thread( new Runnable( )
        //	{
        //
        //	    @Override
        //	    public void run()
        //	    {
        //	Logger.d( TAG , "getCommentPreferences" );
        //	String contentKey = mActivity.getContentKey( );
        //	String ratingKey = mActivity.getRatingKey( );
        //	Logger.d( TAG , "contentKey :　" + contentKey );
        //	Logger.d( TAG , "ratingKey :　" + ratingKey );

        SharedPreferences preferences = App.getSharedPreference();

        if (preferences.contains(contentKey) || preferences.contains(ratingKey))
        {
            //	    Logger.d( TAG , "preferences.contains( contentKey ) || preferences.contains( ratingKey )　" );
            if (preferences.contains(contentKey))
            {
                content = preferences.getString(contentKey, "");
                //		Logger.d( TAG , "content: " + content );
            }

            if (preferences.contains(ratingKey))
            {
                rating = preferences.getInt(ratingKey, 0);
                //		Logger.d( TAG , "rating: " + rating );
            }

        }
        //	    }

        //	} ).start( );
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        // TODO Auto-generated method stub
        gridView.setNumColumns(UITools.isPortrait() ? RESULT_GROUP_NUMBER_PORTRAIT
                : RESULT_GROUP_NUMBER_LANDSCAPE);
        mAdapter.notifyDataSetChanged();
        delayRunGridView(inputView.getText());
        onConfig();
        super.onConfigurationChanged(newConfig);
    }

    private void delayRunGridView(final CharSequence str)
    {
        mHandler.postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                gridViewClickManager(str);
            }
        }, 100);
    }
}
