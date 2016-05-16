package com.hykj.gamecenter.sdk.entry;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author froyohuang CP的订单信息
 * 
 */
public class NiuOrderInfo implements Parcelable
{

    private String itemName = ""; // 订单的物品名称
    private String itemCode = ""; // 订单的Niuwan物品编号
    private int itemCount = 0; // 订单的物品购买数量
    private String cpOrderId = ""; // 游戏调用时传入的订单号
    private String exInfo = ""; // 游戏传入的透传参数
    private String niuOrderId = ""; // 服务器响应后回传的niuwan的订单号
    private int value = 0; // 订单的niuwan币价值

    @Override
    public int describeContents()
    {
	return 0;
    }

    @Override
    public void writeToParcel( Parcel dest , int flags )
    {
	dest.writeString( itemName );
	dest.writeString( itemCode );
	dest.writeInt( itemCount );
	dest.writeString( cpOrderId );
	dest.writeString( exInfo );
	dest.writeString( niuOrderId );
	dest.writeInt( value );
    }

    public static final Parcelable.Creator< NiuOrderInfo > CREATOR = new Creator< NiuOrderInfo >( )
    {

	@Override
	public NiuOrderInfo createFromParcel( Parcel source )
	{
	    NiuOrderInfo entry = new NiuOrderInfo( );
	    entry.setItemName( source.readString( ) );
	    entry.setItemCode( source.readString( ) );
	    entry.setItemCount( source.readInt( ) );
	    entry.setCpOrderId( source.readString( ) );
	    entry.setExInfo( source.readString( ) );
	    entry.setNiuOrderId( source.readString( ) );
	    entry.setValue( source.readInt( ) );
	    return entry;
	}

	@Override
	public NiuOrderInfo [] newArray( int size )
	{
	    return new NiuOrderInfo [size];
	}

    };

    public String getItemName()
    {
	return itemName;
    }

    public void setItemName( String itemName )
    {
	this.itemName = itemName;
    }

    public String getCpOrderId()
    {
	return cpOrderId;
    }

    public void setCpOrderId( String cpOrderId )
    {
	this.cpOrderId = cpOrderId;
    }

    public String getExInfo()
    {
	return exInfo;
    }

    public void setExInfo( String exInfo )
    {
	this.exInfo = exInfo;
    }

    public int getValue()
    {
	return value;
    }

    public void setValue( int value )
    {
	this.value = value;
    }

    public String getNiuOrderId()
    {
	return niuOrderId;
    }

    public void setNiuOrderId( String niuOrderId )
    {
	this.niuOrderId = niuOrderId;
    }

    public String getItemCode()
    {
	return itemCode;
    }

    public void setItemCode( String itemCode )
    {
	this.itemCode = itemCode;
    }

    public int getItemCount()
    {
	return itemCount;
    }

    public void setItemCount( int itemCount )
    {
	this.itemCount = itemCount;
    }

    @Override
    public String toString()
    {
	return "NiuOrderInfo [itemName=" + itemName + ", itemCode=" + itemCode + ", itemCount=" + itemCount + ", cpOrderId=" + cpOrderId + ", exInfo=" + exInfo + ", niuOrderId=" + niuOrderId
		+ ", value=" + value + "]";
    }
}
