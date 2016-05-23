
package com.hykj.gamecenter.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.protocol.Apps.UserCommentInfo;
import com.hykj.gamecenter.utils.DateUtil;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utilscs.LogUtils;

public class UserCommentListAdapter extends BaseAdapter
{
    private final static String TAG = "UserCommentListAdapter";
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final int verCode;
    private final ArrayList<UserCommentInfo> mUserCommentList = new ArrayList<UserCommentInfo>();
    private static final int FIRST_INDEX = 0;
    private boolean isFirstSubmit = true;

    public UserCommentListAdapter(Context context, int verCode)
    {
        mContext = context;
        this.verCode = verCode;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mUserCommentList.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        Holder holder;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.user_comment_content, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }
        bindView(holder, position, mUserCommentList.get(position));
        return convertView;
    }

    private void bindView(Holder holder, int position, UserCommentInfo info)
    {
        // TODO Auto-generated method stub
        holder.content.setText(info.comments);
        //	holder.userName.setText( ( info.getUserName( ).trim( ).isEmpty( ) ? mContext.getString( R.string.app_user_comment_anonymous ) : info.getUserName( ).trim( ) ) );
        LogUtils.d("UserId :" + info.userId);
        LogUtils.d("userName :" + info.userName.trim());
        if (info.userId == 0)
        {
            holder.userName.setText(mContext.getString(R.string.app_user_comment_anonymous));
            holder.productModel.setText(info.userName.trim().isEmpty() ? "" : mContext.getString(
                    R.string.app_user_comment_product_model, info.userName.trim()));
        }
        else
        {
            holder.userName.setText(info.userName.trim());
        }

        Logger.d(TAG, "CommentTime : " + DateUtil.serverDateToApp(info.commentTime));
        holder.date.setText(DateUtil.serverDateToApp(info.commentTime));
        holder.version.setText(verCode > info.localVerCode ? R.string.app_user_comment_old_version
                : R.string.app_user_comment_current_version);
        holder.rating.setRating(info.userScore);
    }

    public void appendFirstData(UserCommentInfo infoes)
    {
        if (isSubmitMore())
        {
            mUserCommentList.remove(FIRST_INDEX);
        }
        mUserCommentList.add(FIRST_INDEX, infoes);
        Logger.d("UserCommentListAdapter", "mUserCommentList.size   == " + mUserCommentList.size());
    }

    public void removeData(int pos)
    {
        mUserCommentList.remove(pos);
    }

    private boolean isSubmitMore()
    {
        return !isFirstSubmit;
    }

    public void setIsFirstSubmit(boolean isFirstSubmit)
    {
        this.isFirstSubmit = isFirstSubmit;
    }

    public void appendData(List<UserCommentInfo> infoes)
    {
        mUserCommentList.addAll(infoes);
        Logger.d("UserCommentListAdapter", "mUserCommentList.size   == " + mUserCommentList.size());
    }

    private class Holder
    {
        private TextView content;
        private TextView userName;
        private TextView date;
        private TextView productModel;
        private TextView version;
        private RatingBar rating;

        private Holder(View view)
        {
            content = (TextView) view.findViewById(R.id.comment_content);
            userName = (TextView) view.findViewById(R.id.user_name);
            date = (TextView) view.findViewById(R.id.comment_date);
            version = (TextView) view.findViewById(R.id.vesion_name);
            rating = (RatingBar) view.findViewById(R.id.app_rating);
            productModel = (TextView) view.findViewById(R.id.product_model);
        }

    }

}
