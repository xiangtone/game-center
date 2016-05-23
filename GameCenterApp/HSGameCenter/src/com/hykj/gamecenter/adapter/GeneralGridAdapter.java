
package com.hykj.gamecenter.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.utils.Tools;

import java.util.ArrayList;

/**
 * @author owenli 所有一行多个item的adapter的基类
 */
public abstract class GeneralGridAdapter extends BaseAdapter {

    protected final Context mContext;
    // private View titleView;
    // protected Handler mHandler;
    protected int mColumnCount = 3;
    protected final int mAppType;
    protected Resources mRes = null;
    private static final LinearLayout.LayoutParams llp = new LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    private static final LinearLayout.LayoutParams llp_padding = new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    /**
     * 空白视图，用于充填没有数据的地方
     */
    private final ArrayList<View> mBlankView;
    boolean mbSubjectAppList = false;// 是否是专题app list （热门，最新，专题，必备）

    public GeneralGridAdapter(Context context, int appType, int columnCount,
            boolean bSubjectAppList) {
        super();

        mContext = context;
        mColumnCount = columnCount;
        mBlankView = new ArrayList<View>();
        mAppType = appType;
        mbSubjectAppList = bSubjectAppList;
        mRes = context.getResources();

        llp.weight = 1;
        llp.setMargins(0, 0, 0, 0);
    }

    @Override
    public int getCount() {
        int nCount = (int) Math.ceil(getAllCount() * 1.0 / mColumnCount);
        // Log.d("GridAdapter", "getCount =" + nCount);
        return nCount;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setColumnCount(int count) {
        mColumnCount = count;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout lineContainer = (LinearLayout) convertView;

        if (lineContainer == null) {
            lineContainer = new LinearLayout(mContext);
            lineContainer.setOrientation(LinearLayout.HORIZONTAL);
            lineContainer.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            lineContainer.removeAllViews();
        }

        int margins = (int) mRes.getDimension(R.dimen.list_view_item_spacing);
        int margin1 = (int) mRes.getDimension(R.dimen.list_view_item_spacing);
        int margin2 = (int) mRes
                .getDimension(R.dimen.recommended_adv_subject_entry_padding);
            lineContainer
                    .setPadding(margins, 0/* margin2 */, margins, 0/* margin2 */);
//        if (0 == position) {
//            /*
//             * int paddingTop = mContext.getResources().getDimensionPixelSize(
//             * R.dimen.csl_cs_padding_half_size);
//             */
//            if (mbSubjectAppList) {
//                lineContainer
//                        .setPadding(margins, 0/* margin2 */, margins, 0/* margin2 */);
//            }
//
//        } else {
//            lineContainer.setPadding(margins, 0, margins, 0/* margin2 */);
//            if (mbSubjectAppList) {
//                lineContainer.setPadding(margins, 0, margins, 0/* margin2 */);
//            }
//        }

        // 获取或者初始化重用的itemview和divider的view
        ArrayList<View> viewHolder = (ArrayList<View>) lineContainer
                .getTag(R.id.tag_viewHolder);
        if (viewHolder == null) {
            viewHolder = new ArrayList<View>();
            lineContainer.setTag(R.id.tag_viewHolder, viewHolder);
        }

        // 对每一行，生成单独的实际itemview加入上面创建的layout中
        int index = position * mColumnCount;
        int end = index + mColumnCount;
        int nFillItem = 0;
        for (; index < end && index < getAllCount(); index++) {
            int posInLine = mColumnCount - (end - index);
            View itemView = null;

            if (viewHolder.size() > posInLine) {
                itemView = viewHolder.get(posInLine);
                itemView = getPeaceView(index, itemView, lineContainer);
            } else {
                itemView = getPeaceView(index, itemView, lineContainer);
                viewHolder.add(itemView);
            }

            if (itemView != null) {
                itemView.setTag(R.id.tag_blankView, Boolean.FALSE);
                itemView.setTag(R.id.tag_positionView, Integer.valueOf(index));

                // llp.setMargins( 6 , 0 , 6 , 0 );

                int screenWidth = Tools.getDisplayWidth(mContext);

                // getDimension返回的是绝对尺寸，而不是相对尺寸（dp\sp等)

                if (mbSubjectAppList) {
                    margins = (int) mRes
                            .getDimension(R.dimen.list_view_item_spacing);
                } else {
                    margins = (int) mRes
                            .getDimension(R.dimen.list_view_item_spacing);
                }
                int ParentWidth = screenWidth - 2 * margins; // 当前
                // listItem间距
                int iPaddingWidth = 0;
                if (mColumnCount > 1) {
                    iPaddingWidth = (int) mRes
                            .getDimension(R.dimen.list_view_item_spacing);
                } else {
                    iPaddingWidth = 0;
                }

                int MaginW = 0;
                // item宽度
                int iItemWidth = (int) ((ParentWidth - MaginW * mColumnCount - iPaddingWidth
                        * (mColumnCount - 1))
                        * 1.0f / mColumnCount + 0.5f);
                // 固定宽布局
                LinearLayout.LayoutParams llp_appFixedWidth = new LayoutParams(
                        iItemWidth, LayoutParams.WRAP_CONTENT);

                if (1 == mColumnCount) {
                    llp_appFixedWidth.gravity = Gravity.CENTER;// 只有一列居中
                }

                margin1 = (int) mRes
                        .getDimension(R.dimen.list_view_item_nospacing);
                margin2 = (int) mRes
                        .getDimension(R.dimen.recommended_adv_subject_entry_padding);

                llp_appFixedWidth.setMargins(0, 0, 0, 0);

                lineContainer.addView(itemView, llp_appFixedWidth);

                // 加入填充布局
                if (index >= 0 && index < end - 1) {
                    TextView tv = new TextView(mContext);
                    tv.setWidth(iPaddingWidth);
                    // tv.setBackgroundResource(R.color.cs_btn_red_normal);
                    lineContainer.addView(tv, llp_padding);
                }

                nFillItem++;
            }
        }

        lineContainer.setTag(R.id.tag_fillItems, nFillItem);

        // 当一行未填满，填充空白view
        if (index < end) {
            for (int blankNum = end - index - 1; blankNum >= 0; blankNum--) {
                View bk = null;
                for (int j = mBlankView.size() - 1; j >= 0; j--) {

                    if (mBlankView.get(j).getParent() == null) {
                        bk = mBlankView.get(j);
                        break;
                    }
                }
                if (bk == null) {
                    bk = genBlankView();
                    mBlankView.add(bk);
                }
                lineContainer.addView(bk, llp);
            }
        }

        return lineContainer;
    }

    /**
     * 获取列表数据项的总长度
     * 
     * @return
     */
    public abstract int getAllCount();

    /**
     * 获取每个数据项的视图
     * 
     * @param positionInTotal
     * @param convertView
     * @param parentView
     * @return
     */
    public abstract View getPeaceView(int positionInTotal, View convertView,
            ViewGroup parentView);

    protected View genBlankView() {
        View blankView = new LinearLayout(mContext);
        blankView.setTag(R.id.tag_blankView, Boolean.TRUE);
        return blankView;
    }
}
