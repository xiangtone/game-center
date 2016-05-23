
package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.hykj.gamecenter.ui.widget.CSLoadingView.ICSListViewLoadingRetry;
import com.hykj.gamecenter.utils.Logger;

public class CSLoadingUIListView extends CSPullListView implements
        ICSListViewLoadingRetry {
    private final String tag = "CSLoadingUIListView";

    protected CSLoadingView mLoadingView;
    private ICSLoadingViewListener mListener;

    private final int LLVS_LOADING = 0x01;
    private final int LLVS_NONETWORK = 0x02;
    private final int LLVS_NORMAL = 0x03;

    private boolean isShowingLoading = false;

    public CSLoadingUIListView(Context context) {
        super(context);
        initView(context);
    }

    public CSLoadingUIListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CSLoadingUIListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void setCSLoadingViewListener(ICSLoadingViewListener listener) {
        mListener = listener;
    }

    public void setLoadingTipText(String tip) {
        if (mLoadingView != null) {
            mLoadingView.setLoadingTipText(tip);
        }
    }

    public void hideLoadingUI() {
        if (mLoadingView != null) {
            mLoadingView.hide();
            isShowingLoading = false;
        }
    }

    public void showLoadingUI() {
        if (mLoadingView != null) {
            mLoadingView.showLoading();
            isShowingLoading = true;
        }
    }

    public boolean isNoNetworkShow() {

        if (mLoadingView != null) {
            return mLoadingView.isNoNetworkShow();
        }
        return false;
    }

    private void initView(Context context) {
        mLoadingView = new CSLoadingView(context);
        mLoadingView.setOnRetryListener(this);
    }

    /*
     * ��ʼ��ȡ��ݣ�ֻ�ܵ���һ��
     */
    public void initRequestData() {
        if (mListener == null)
            return;

        setViewStatus(LLVS_LOADING);
        mListener.onInitRequestData();
    }

    private void setViewStatus(int nStatus) {
        switch (nStatus) {
            case LLVS_LOADING:
                // setVisibility(View.GONE);
                mLoadingView.showLoading();
                isShowingLoading = true;
                break;
            case LLVS_NONETWORK:
                // setVisibility(View.GONE);
                mLoadingView.showNoNetwork();
                isShowingLoading = false;
                break;
            case LLVS_NORMAL:
                // setVisibility(View.VISIBLE);
                mLoadingView.hide();
                isShowingLoading = false;
                break;

            default:
                break;
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

        adapter.registerDataSetObserver(mDataSetObserver);

        ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup != null) {
            Logger.i("CSLoadingView", "initView add loadingView");
            viewGroup.addView(mLoadingView);
            setEmptyView(mLoadingView);
        }
    }

    private final DataSetObserver mDataSetObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            ListAdapter adapter = getAdapter();

            int nListCount = adapter.getCount();
            if (isAddedFooter())
                nListCount--;
            if (isAddedHeader())
                nListCount--;

            Log.d(tag, "onChanged nListCount=" + nListCount);
            if (nListCount > 0) {
                setViewStatus(LLVS_NORMAL);
            }
        }

        @Override
        public void onInvalidated() {
            // ListAdapter adapter = getAdapter();
            //
            // int nListCount = adapter.getCount();
            // if (isAddedFooter())
            // nListCount--;
            // if (isAddedHeader())
            // nListCount--;
            //
            // if (nListCount <= 0) {
            // setViewStatus(LLVS_NONETWORK);
            // }
            // getAdapter().isEmpty() ;

            Log.d(tag, "onInvalidated");
            setViewStatus(LLVS_NONETWORK);
        }
    };

    @Override
    public void onRetry() {
        setViewStatus(LLVS_LOADING);
        if (mListener == null)
            return;
        mListener.onRetryRequestData();
    }

    public boolean isShowLoading() {
        return isShowingLoading;
    }
}
