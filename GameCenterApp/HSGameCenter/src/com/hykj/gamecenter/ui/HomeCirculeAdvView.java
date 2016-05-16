
package com.hykj.gamecenter.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.ui.HorizonScrollLayout.OnTouchScrollListener;
import com.hykj.gamecenter.utils.UITools;
import com.hykj.gamecenter.utilscs.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * @author greatzhang
 */

public class HomeCirculeAdvView {
    private static final String TAG = "HomeCirculeAdvView";

    private static final int MSG_HOME_CIRCULE = 0;

    public static final int MSG_HOME_REFRESH = 1;

    private static final int TIME_CIRCULE_START = 4 * 1000;

    /**
     * 从第几个位置开始循环标志
     */

    private CirculeAdvListener listener;

    private DotProgressBar mDotProgressBar;
    private DotProgressBar mDotProgressBar2;
    private HorizonScrollLayout mHorizonScrollLayout;
    private FrameLayout mAdvInfoFrameLayout;
    private TextView mAdvDesc;
    private TextView mAdvName;
    // private ImageView mAdvVirtual;
    private Context context;

    private boolean isCircule = true;

    private int infoSize = 0;

    private int mCurrentPage = 0;

    public void setmCurrentPage(int mCurrentPage) {
        this.mCurrentPage = mCurrentPage;
    }

    public interface CirculeAdvListener {
        void onCirCuleAdvListener(int i, GroupElemInfo info);
    }

    @SuppressLint("HandlerLeak")
    private Handler mScrollHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 暂停消息发送的时候，不需处理
            switch (msg.what) {
                case MSG_HOME_CIRCULE:
                    if (isCircule && infoSize > 0) {
                        if (mScrollHandler != null) {
                            // TODO 循环下个列表
                            circuleAdv();
                            mScrollHandler.sendEmptyMessageDelayed(
                                    MSG_HOME_CIRCULE, TIME_CIRCULE_START);
                        }
                    }
                    break;
                case MSG_HOME_REFRESH:
                    if (!UITools.isPortrait()) {
                        mAdvInfoFrameLayout.setVisibility(View.VISIBLE);
                        mDotProgressBar2.setVisibility(View.VISIBLE);
                        mDotProgressBar.setVisibility(View.GONE);
                        mDotProgressBar2.setCurProgress(getCurScreen());
                    } else {
                        mAdvInfoFrameLayout.setVisibility(View.GONE);
                        mDotProgressBar.setVisibility(View.VISIBLE);
                        mDotProgressBar2.setVisibility(View.GONE);
                        mDotProgressBar.setCurProgress(getCurScreen());
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public Handler getHandler() {
        return mScrollHandler;
    }

    public HomeCirculeAdvView(View view, Context context) {
        // infoSize = infos.size( );
        this.context = context;
        mAdvInfoFrameLayout = (FrameLayout) view
                .findViewById(R.id.adv_info_framelayout);
        mAdvDesc = (TextView) view.findViewById(R.id.adv_desc);
        mAdvName = (TextView) view.findViewById(R.id.adv_name);

        // mAdvVirtual = (ImageView)view.findViewById( R.id.adv_virtual );

        if (!UITools.isPortrait()) {
            mAdvInfoFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mAdvInfoFrameLayout.setVisibility(View.GONE);
        }

        // 捕获touch事件
        mAdvInfoFrameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        mHorizonScrollLayout = (HorizonScrollLayout) view
                .findViewById(R.id.top_ad_1);
        mHorizonScrollLayout.setEnableOverScroll(false);
        mHorizonScrollLayout.setLockAllWhenTouch(true);
        mHorizonScrollLayout.setScrollSlop(1.75f);
        mHorizonScrollLayout.setCircle(true);

        // 竖屏的时候启用
        mDotProgressBar = (DotProgressBar) view.findViewById(R.id.top_ad_dot);
        // mDotProgressBar.setTotalNum( infos.size( ) );
        mDotProgressBar.setDotbarIconResource(
                R.drawable.home_scroll_ad_dot_white,
                R.drawable.home_scroll_ad_dot_black);

        // 横屏的时候启用
        mDotProgressBar2 = (DotProgressBar) view.findViewById(R.id.top_ad_dot2);
        mDotProgressBar2.setDotbarIconResource(
                R.drawable.home_scroll_ad_dot_white,
                R.drawable.home_scroll_ad_dot_black);

        if (!UITools.isPortrait()) {
            mDotProgressBar2.setVisibility(View.VISIBLE);
            mDotProgressBar.setVisibility(View.GONE);
        } else {
            mDotProgressBar.setVisibility(View.VISIBLE);
            mDotProgressBar2.setVisibility(View.GONE);
        }

        mHorizonScrollLayout
                .setOnTouchScrollListener(new OnTouchScrollListener() {

                    @Override
                    public void onScrollStateChanged(int scrollState,
                            int currentScreem) {
                        // 当手滚动广告位是, 不播放
                        if (OnTouchScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState) {
                            // 滑动过程中停止循环
                            stopCricule();
                        } else if (OnTouchScrollListener.SCROLL_STATE_IDLE == scrollState) {
                            // 滑动暂停循环
                            startCricle();
                        }
                    }

                    @Override
                    public void onScroll(View view, float leftX,
                            float screemWidth) {
                    }

                    @Override
                    public void onScreenChange(int displayScreem, Object obj) {

                        if (!UITools.isPortrait()) {
                            mDotProgressBar2.setCurProgress(displayScreem);
                        } else {
                            mDotProgressBar.setCurProgress(displayScreem);
                        }

                        // 改变遮罩层中的内容
                        if (obj == null)
                            return;
                        GroupElemInfo elemInfo = (GroupElemInfo) obj;
                        mAdvName.setText(elemInfo.showName);
                        mAdvDesc.setText(getAdcDesc(elemInfo.recommWord));
                    }
                });

    }

    // 对游戏内容进行分行处理
    private String getAdcDesc(String advdesc) {
        StringBuffer sbf = new StringBuffer("");
        if (!"".equals(advdesc) && null != advdesc) {
            int descLength = advdesc.length();
            int start = 0;
            int line = descLength / 10;
            if (line == 0) {
                return advdesc;
            }
            for (int i = 1; i <= line; i++) {
                sbf.append(advdesc.substring(start, i * 10)).append("\n");
                start = i * 10;
            }
            sbf.append(advdesc.substring(start));
        }

        return sbf.toString();
    }

    // private void refreshScrollView()
    // {
    //
    // mHorizonScrollLayout.removeAllViews( );
    //
    // LogUtils.e( "linkedListCache size = " + linkedListCache.size( ) );
    //
    // for( GroupElemInfo ginfo : linkedListCache )
    // {
    // LogUtils.e( "ginfo's appId = " + ginfo.getAppId( ) );
    // }
    //
    // LayoutInflater inflater = (LayoutInflater)context.getSystemService(
    // Context.LAYOUT_INFLATER_SERVICE );
    // this.infoSize = linkedListCache.size( );
    //
    // mDotProgressBar.setTotalNum( linkedListCache.size( ) );
    // mDotProgressBar.setDotbarNum( linkedListCache.size( ) );
    //
    // for( int i = 0 ; i < linkedListCache.size( ) ; i++ )
    // {
    // final int j = i;
    // final GroupElemInfo info = linkedListCache.get( i );
    // ViewGroup view = (ViewGroup)inflater.inflate( R.layout.home_adv_child ,
    // null );
    // mHorizonScrollLayout.addView( view );
    // ImageView imageView = (ImageView)view.findViewById(
    // R.id.home_first_adv_child );
    // ImageLoader.getInstance( ).displayImage( info.getAdsPicUrl( ) , imageView
    // , DisplayOptions.optionBigMapHomepage );
    //
    // // if(!UITools.isPortrait( ) && i == 0){
    // // ImageLoader.getInstance( ).displayImage( info.getAdsPicUrl( ) ,
    // mAdvVirtual , DisplayOptions.optionBigMapHomepage );
    // // }
    //
    // view.setOnClickListener( new OnClickListener( )
    // {
    // @Override
    // public void onClick( View v )
    // {
    // listener.onCirCuleAdvListener( j , info );
    // }
    // } );
    // }
    // }

    public void addAdvListener(CirculeAdvListener listener) {
        this.listener = listener;
    }

    private void circuleAdv() {

        // LogUtils.e( "circuleAdv 循环播放！" );
        if (infoSize > 0) {
            mHorizonScrollLayout.displayNextScreen();
            if (!UITools.isPortrait()) {
                mDotProgressBar2.setCurProgress(getCurScreen());
            } else {
                mDotProgressBar.setCurProgress(getCurScreen());
            }

        }
    }

    public void scrollViewAddData(ArrayList<GroupElemInfo> infos, int current) {
        // mBeforeScreen = cureen;
        // LogUtils.e( "mBeforeScreen =" + mBeforeScreen );
        // LinkedList< GroupElemInfo > groupInfos = init( infos , cureen );
        LogUtils.e("scrollViewAddData,往轮播当中设置数据！");
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.infoSize = infos.size();

        mDotProgressBar2.setTotalNum(infos.size());
        mDotProgressBar2.setDotbarNum(infos.size());
        mDotProgressBar.setTotalNum(infos.size());
        mDotProgressBar.setDotbarNum(infos.size());

        mScrollHandler.sendEmptyMessage(MSG_HOME_REFRESH);

        for (int i = 0; i < infos.size(); i++) {
            final int j = i;
            final GroupElemInfo info = infos.get(i);

            if (i == current) {
                mAdvName.setText(info.showName);
                mAdvDesc.setText(getAdcDesc(info.recommWord));
            }

            ViewGroup view = (ViewGroup) inflater.inflate(
                    R.layout.home_adv_child, null);
            mHorizonScrollLayout.addView(view);
            ImageView imageView = (ImageView) view
                    .findViewById(R.id.home_first_adv_child);
            ImageLoader.getInstance().displayImage(info.adsPicUrl,
                    imageView, DisplayOptions.optionBigMapHomepage);
            view.setTag(info);

            // if(!UITools.isPortrait( ) && i == 0){
            // ImageLoader.getInstance( ).displayImage( info.getAdsPicUrl() ,
            // mAdvVirtual , DisplayOptions.optionBigMapHomepage );
            // }

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCirCuleAdvListener(j, info);
                }
            });
        }

    }

    // //初始化
    // private LinkedList< GroupElemInfo > linkedListCache = new LinkedList<
    // GroupElemInfo >( );
    //
    // public LinkedList< GroupElemInfo > init( ArrayList< GroupElemInfo > infos
    // , int screen )
    // {
    // LogUtils.e( "现在是第几屏,screen =" + screen );
    // for( GroupElemInfo gInfo : infos )
    // {
    // linkedListCache.add( gInfo );
    // }
    //
    // for( GroupElemInfo ginfo : linkedListCache )
    // {
    //
    // LogUtils.e( "ginfo's appId = " + ginfo.getAppId( ) );
    // }
    //
    // if( linkedListCache.size( ) > 0 )
    // {
    // for( int i = 1 ; i <= screen ; i++ )
    // {
    // GroupElemInfo gei = linkedListCache.getFirst( );
    // linkedListCache.removeFirst( );
    // linkedListCache.add( gei );
    // }
    // }
    //
    // for( GroupElemInfo ginfo : linkedListCache )
    // {
    //
    // LogUtils.e( "ginfo's appId = " + ginfo.getAppId( ) );
    // }
    // return linkedListCache;
    // }
    //
    // //获取
    // public void getGroupInfos( boolean b )
    // {
    //
    // if( linkedListCache.size( ) > 0 )
    // {
    // if( b )
    // {
    // GroupElemInfo finfo = linkedListCache.getFirst( );
    // linkedListCache.removeFirst( );
    // linkedListCache.add( finfo );
    // }
    // else
    // {
    // GroupElemInfo linfo = linkedListCache.getLast( );
    // linkedListCache.removeLast( );
    // linkedListCache.addFirst( linfo );
    // }
    // }
    //
    // }

    public void stopCricule() {
        LogUtils.e("stopCricule");
        isCircule = false;
        if (mScrollHandler != null) {
            mScrollHandler.removeMessages(MSG_HOME_CIRCULE);
        }
    }

    public boolean isCricle() {
        return isCircule;
    }

    private void startCricle() {
        isCircule = true;
        if (mScrollHandler != null) {
            mScrollHandler.removeMessages(MSG_HOME_CIRCULE);
            mScrollHandler.sendEmptyMessageDelayed(MSG_HOME_CIRCULE,
                    TIME_CIRCULE_START);
        }
    }

    public void setCurScreen(int position) {
        mHorizonScrollLayout.setDefaultScreem(position);
        if (!UITools.isPortrait()) {
            mDotProgressBar2.setCurProgress(position);
        } else {
            mDotProgressBar.setCurProgress(position);
        }

    }

    /**
     * 得到当前是第几个屏幕
     * 
     * @return
     */
    public int getCurScreen() {
        return mHorizonScrollLayout.getCurScreen();
    }

    /**
     * 回复首页第一个广告位的循环
     */
    public void onReply() {
        LogUtils.e("onReply");
        startCricle();
    }

    /**
     * 停止广告位的循环动作
     */
    public void destoryCirclue() {
        mScrollHandler = null;
    }

}
