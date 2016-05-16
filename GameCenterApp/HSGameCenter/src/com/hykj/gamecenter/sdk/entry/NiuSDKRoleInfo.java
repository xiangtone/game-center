package com.hykj.gamecenter.sdk.entry;

import android.os.Parcel;
import android.os.Parcelable;

public class NiuSDKRoleInfo implements Parcelable {

	private String uId;
	private String uToken;
	private String roleId;
	private String roleName;
	private String roleToken;
	private String uName;

	public NiuSDKRoleInfo() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeInt(uId);
	    dest.writeString(uId);
		dest.writeString(uToken);
		dest.writeString(roleId);
		dest.writeString(roleName);
		dest.writeString(roleToken);
		dest.writeString(uName);
	}

	public static final Parcelable.Creator<NiuSDKRoleInfo> CREATOR = new Creator<NiuSDKRoleInfo>() {

		@Override
		public NiuSDKRoleInfo createFromParcel(Parcel source) {
			NiuSDKRoleInfo entry = new NiuSDKRoleInfo();
			entry.setuId(source.readString());
			entry.setuToken(source.readString());
			entry.setRoleId(source.readString());
			entry.setRoleName(source.readString());
			entry.setRoleToken(source.readString());
			entry.setuName(source.readString());
			return entry;
		}

		@Override
		public NiuSDKRoleInfo[] newArray(int size) {
			return new NiuSDKRoleInfo[size];
		}

	};

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getuToken() {
		return uToken;
	}

	public void setuToken(String uToken) {
		this.uToken = uToken;
	}
	
	   public String getuName() {
	        return uName;
	    }

	    public void setuName(String uName) {
	        this.uName = uName;
	    }

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleToken() {
		return roleToken;
	}

	public void setRoleToken(String roleToken) {
		this.roleToken = roleToken;
	}

	@Override
	public String toString() {
		return "NiuSDKRoleInfo [uid=" +uId+ ", uToken=" + uToken + ", uName=" + uName +", roleId=" + roleId + ", roleName=" + roleName + ", roleToken=" + roleToken + "]";
	}
}
