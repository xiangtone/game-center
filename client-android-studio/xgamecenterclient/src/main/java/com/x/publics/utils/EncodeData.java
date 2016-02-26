package com.x.publics.utils;
import android.util.Base64;


/**上传的POST参数或GET参数及服务器端下发的JSON协议进行加密及解密处理
 
 **/

public final class EncodeData {
	
	private byte m_u8Seed;
	
	public EncodeData(String strKey) {
		m_u8Seed = prvGetSeed(strKey.getBytes(),strKey.length());
	}
	
	private byte prvGetSeed(byte[] buffer,int uLen) {
		byte u8Seed = 0;
		int i;
		for ( i = 0 ; i < uLen ; i++ )
		{
			u8Seed += buffer[i];
		}
		if ( 0 == u8Seed || 0xFF == u8Seed )
			u8Seed = 0x55;
		return u8Seed;
	}
	
	public void Encode(byte[] data,int ulSize){
		Encode(data,m_u8Seed);
	}
	
	public void Encode(byte[] data,byte u8Seed) {
		int i;
		int iSize = data.length;

		for ( i = 0 ; i < iSize ; i++ )
			data[i] ^= u8Seed;
	}
	
	public void Decode(byte[] data) {
		Encode(data,m_u8Seed);
	}
	
	public void Decode(byte[] data,byte u8Seed) {
		Encode(data,u8Seed);
	}
	
	public String EncodeAndBase64(byte[] value) {
		byte buffers[] = new byte[value.length];
		System.arraycopy(value, 0,buffers,0,value.length);
		Encode(buffers,m_u8Seed);
		byte[] data = Base64.encode(buffers, Base64.DEFAULT);
		String str = new String(data);
		return str;
	}
	
	public byte[] DecodeAndBase64(char[] value) {
		String str = new String(value);
		byte []result = Base64.decode(str, Base64.DEFAULT);
		Decode(result,m_u8Seed);
		return result;
	}
	
	public byte[] EncodeAndBase64New(byte[] value) {
		byte []data = null;
		if(value!=null&&value.length>0)
		{
			try {
				Encode(value,m_u8Seed);
				data = Base64.encode(value, Base64.DEFAULT);
			} catch(IllegalArgumentException e) {
				LogUtil.getLogger().e("EncodeData", e.getMessage()) ;
				e.printStackTrace() ;
			} catch(Exception e) {
				LogUtil.getLogger().e("EncodeData", e.getMessage()) ;
				e.printStackTrace() ;
			}
		}
		else
		{
			data = value;
		}
		return data;
	}
	
	public byte[] DecodeAndBase64New(byte[] value) {
		byte[] result = null;
		if(value!=null&&value.length>0)
		{
			try {
				result = Base64.decode(value, Base64.DEFAULT);
				Decode(result,m_u8Seed);
			} catch(IllegalArgumentException e) {
				LogUtil.getLogger().e("EncodeData", e.getMessage()) ;
				e.printStackTrace() ;
			} catch(Exception e) {
				LogUtil.getLogger().e("EncodeData", e.getMessage()) ;
				e.printStackTrace() ;
			}
		}
		else
		{
			result = value;
		}
		return result;
	}
	
}
