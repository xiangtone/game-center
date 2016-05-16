
package com.hykj.gamecenter.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.ui.HorizonScrollLayout.OnTouchScrollListener;
import com.hykj.gamecenter.utils.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * @author Owenli
 */

public class HorizonScrollViewControllerManager
{
    private static final String TAG = "HomeCirculeAdvView";

    private static final int MSG_HOME_CIRCULE = 0;

    private static final int TIME_CIRCULE_START = 4 * 1000;

    /**
     * 从第几个位置开始循环标志
     */
    private static final int CIRCULE_FLAG = 0;

    private CirculeAdvListener listener;

    private DotProgressBar mDotProgressBar;
    private HorizonScrollLayout mHorizonScrollLayout;
    private Context context;

    private boolean isCircule = true;

    private int infoSize = 0;

    public interface CirculeAdvListener
    {
        void onCirCuleAdvListener(int i, GroupElemInfo info);
    }

    @SuppressLint("HandlerLeak")
    private Handler mScrollHandler = new Handler()
    {
        @Override
        public void handleMessage(android.os.Message msg)
        {
            //暂停消息发送的时候，不需处理
            switch (msg.what)
            {
                case MSG_HOME_CIRCULE:
                    if (isCircule && infoSize > 0)
                    {
                        if (mScrollHandler != null)
                        {
                            //TODO 循环下个列表
                            circuleAdv();
                            mScrollHandler.sendEmptyMessageDelayed(MSG_HOME_CIRCULE,
                                    TIME_CIRCULE_START);
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    };

    public HorizonScrollViewControllerManager(View view, Context context)
    {

        //	infoSize = infos.size( );
        this.context = context;

        mHorizonScrollLayout = (HorizonScrollLayout) view.findViewById(R.id.top_ad_1);
        mHorizonScrollLayout.setEnableOverScroll(false);
        mHorizonScrollLayout.setLockAllWhenTouch(true);
        mHorizonScrollLayout.setScrollSlop(1.75f);
        mHorizonScrollLayout.setCircle(true);
        //	scrollViewAddData( infos );

        mDotProgressBar = (DotProgressBar) view.findViewById(R.id.top_ad_dot);
        //	mDotProgressBar.setTotalNum( infos.size( ) );
        mDotProgressBar.setDotbarIconResource(R.drawable.home_scroll_ad_dot_white,
                R.drawable.home_scroll_ad_dot_black);
        mDotProgressBar.setVisibility(View.VISIBLE);
        mDotProgressBar.setCurProgress(CIRCULE_FLAG);
        //	mDotProgressBar.SetDotbarNum( infos.size( ) );

        mHorizonScrollLayout.setOnTouchScrollListener(new OnTouchScrollListener()
        {

            @Override
            public void onScrollStateChanged(int scrollState, int currentScreem)
            {
                // 当手滚动广告位是, 不播放
                if (OnTouchScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState)
                {
                    Log.i(TAG, "------home adv circule is stop circle");
                    stopCricule();
                }
                else if (OnTouchScrollListener.SCROLL_STATE_IDLE == scrollState)
                {
                    startCricle();
                }
            }

            @Override
            public void onScroll(View view, float leftX, float screemWidth)
            {
            }

            @Override
            public void onScreenChange(int displayScreem, Object obj)
            {
                mDotProgressBar.setCurProgress(displayScreem);
            }
        });

    }

    public HorizonScrollViewControllerManager(HorizonScrollLayout horizonScrollLayout,
            DotProgressBar dotProgressBar, Context context)
    {

        //	infoSize = infos.size( );
        this.context = context;

        //	mHorizonScrollLayout = (HorizonScrollLayout)view.findViewById( R.id.top_ad_1 );
        //	mDotProgressBar = (DotProgressBar)view.findViewById( R.id.top_ad_dot );

        mHorizonScrollLayout = horizonScrollLayout;
        mHorizonScrollLayout.setEnableOverScroll(false);
        mHorizonScrollLayout.setLockAllWhenTouch(true);
        mHorizonScrollLayout.setScrollSlop(1.75f);
        mHorizonScrollLayout.setCircle(false);
        //	scrollViewAddData( infos );

        mDotProgressBar = dotProgressBar;
        //	mDotProgressBar.setTotalNum( infos.size( ) );
        mDotProgressBar.setDotbarIconResource(R.drawable.home_scroll_ad_dot_white,
                R.drawable.home_scroll_ad_dot_black);
        mDotProgressBar.setVisibility(View.VISIBLE);
        mDotProgressBar.setCurProgress(CIRCULE_FLAG);
        //	mDotProgressBar.SetDotbarNum( infos.size( ) );

        mHorizonScrollLayout.setOnTouchScrollListener(new OnTouchScrollListener()
        {

            @Override
            public void onScrollStateChanged(int scrollState, int currentScreem)
            {
                // 当手滚动广告位是, 不播放
                if (OnTouchScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState)
                {
                    Log.i(TAG, "------home adv circule is stop circle");
                    stopCricule();
                }
                else if (OnTouchScrollListener.SCROLL_STATE_IDLE == scrollState)
                {
                    startCricle();
                }
            }

            @Override
            public void onScroll(View view, float leftX, float screemWidth)
            {
            }

            @Override
            public void onScreenChange(int displayScreem, Object obj)
            {
                mDotProgressBar.setCurProgress(displayScreem);
            }
        });

    }

    public void addAdvListener(CirculeAdvListener listener)
    {
        this.listener = listener;
    }

    private void circuleAdv()
    {
        if (infoSize > 0)
        {
            mHorizonScrollLayout.displayNextScreen();
            mDotProgressBar.setCurProgress(getCurScreen());
        }
    }

    public void scrollViewAddData(ArrayList<GroupElemInfo> infos)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.infoSize = infos.size();

        mDotProgressBar.setTotalNum(infos.size());
        mDotProgressBar.setDotbarNum(infos.size());

        for (int i = 0; i < infos.size(); i++)
        {
            final int j = i;
            Logger.i(TAG, "scrollViewAddData: j = " + j);
            final GroupElemInfo info = infos.get(i);
            ViewGroup view = (ViewGroup) inflater.inflate(R.layout.home_adv_child, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.home_first_adv_child);
            ImageLoader.getInstance().displayImage(info.adsPicUrl, imageView,
                    DisplayOptions.optionBigMapHomepage);
            view.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onCirCuleAdvListener(j, info);
                }
            });
            mHorizonScrollLayout.addView(view);
        }
    }

    public void appendData(ArrayList<ViewGroup> data)
    {
        //	LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        this.infoSize = data.size();

        mDotProgressBar.setTotalNum(infoSize);
        mDotProgressBar.setDotbarNum(infoSize);

        for (int i = 0; i < infoSize; i++)
        {
            final int j = i;
            Logger.i(TAG, "scrollViewAddData: j = " + j);
            final ViewGroup viewGroup = data.get(i);
            mHorizonScrollLayout.addView(viewGroup);
            //	    ImageView imageView = (ImageView)view.findViewById( R.id.home_first_adv_child );
            //	    ImageLoader.getInstance( ).displayImage( info.getAdsPicUrl( ) , imageView , DisplayOptions.optionBigMapHomepage );
            //	    view.setOnClickListener( new OnClickListener( )
            //	    {
            //		@Override
            //		public void onClick( View v )
            //		{
            //		    listener.onCirCuleAdvListener( j , info );
            //		}
            //	    } );
        }
    }

    public void stopCricule()
    {
        isCircule = false;
        if (mScrollHandler != null)
        {
            mScrollHandler.removeMessages(MSG_HOME_CIRCULE);
        }
    }

    private void startCricle()
    {
        isCircule = true;
        if (mScrollHandler != null)
        {
            mScrollHandler.removeMessages(MSG_HOME_CIRCULE);
            //	    mScrollHandler.sendEmptyMessageDelayed( MSG_HOME_CIRCULE , TIME_CIRCULE_START );
        }
    }

    /**
     * 得到当前是第几个屏幕
     * 
     * @return
     */
    public int getCurScreen()
    {
        return mHorizonScrollLayout.getCurScreen();
    }

    /**
     * 回复首页第一个广告位的循环
     */
    public void onReply()
    {
        startCricle();
    }

    /**
     * 停止广告位的循环动作
     */
    public void destoryCirclue()
    {
        mScrollHandler = null;
    }

}
