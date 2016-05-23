package com.hykj.gamecenter.data;

import android.os.Parcel;
import android.os.Parcelable;

public class TopicInfo implements Parcelable
{
    public String mTopic;
    public String mAppCount;
    public String mTip;
    public String mPicUrl;

    /* (non-Javadoc)
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents()
    {
	return 0;
    }

    /* (non-Javadoc)
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     */
    @Override
    public void writeToParcel( Parcel dest , int flags )
    {
	dest.writeString( mTopic );
	dest.writeString( mTip );
	dest.writeString( mAppCount );
	dest.writeString( mPicUrl );
    }

    public static final Parcelable.Creator< TopicInfo > CREATOR = new Creator< TopicInfo >( )
    {
	@Override
	public TopicInfo createFromParcel( Parcel source )
	{
	    TopicInfo info = new TopicInfo( );
	    info.mTopic = source.readString( );
	    info.mTip = source.readString( );
	    info.mAppCount = source.readString( );
	    info.mPicUrl = source.readString( );
	    return info;
	}

	@Override
	public TopicInfo [] newArray( int size )
	{
	    return new TopicInfo [size];
	}
    };
}
