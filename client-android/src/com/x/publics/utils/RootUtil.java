/**   
* @Title: RootUtil.java
* @Package com.mas.amineappstore.publics.utils
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-10 上午9:34:09
* @version V1.0   
*/

package com.x.publics.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.SystemClock;

import com.x.R;

/**
* @ClassName: RootUtil
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-10 上午9:34:09
* 
*/

public class RootUtil {

	/**
	 * 第一次获取root权限后，push 到 /system/bin/ 目录下的 二进制可执行程序 的名称
	 */
	public static final String ROOT_SU = "zapp";
	/**
	 * 调用 {@link #ROOT_SU}命令时传入的returnKey 返回值是通过该returnKey来获取
	 */
	public static final String RETURNCODE_MOVE_PACKAGE = "returnCode_move_package:";
	public static final String RETURN_CODE_ERROR = "return_code_error";

	/**
	 * 手机是否已root
	 * 
	 * @return boolean
	 */
	public static boolean checkRootPermission() {
		boolean root = false;
		try {
			if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
				root = false;
			} else {
				root = true;
			}
		} catch (Exception e) {
		}
		return root;
	}

	/**
	 * zapp是否获取最高权限
	 * 
	 * @return boolean
	 */
	public static boolean isAppRootPermission() {
		File f = new File("/system/bin/" + ROOT_SU);
		return f.exists();
	}

	/**
	 * 调用su获取root权限再把zappsu写到system/bin目录下
	 * 以便永久获取root权限（一旦获取成功，下次不再调用su）
	 * 
	 * @param ctx
	 */
	public static  boolean  preparezlsu(Context ctx) {
		try {
			File zlsu = new File("/system/bin/" + ROOT_SU);

			InputStream suStream = ctx.getResources().openRawResource(R.raw.zapp);
			if (zlsu.exists()) {
				if (zlsu.length() == suStream.available()) {
					suStream.close();
					return true;
				}
			}
			/**
			 * 先把zapp 写到/data/data/xxx.xxx.xxx中 然后再调用 su 权限 写到
			 * /system/bin目录下
			 */
			byte[] bytes = new byte[suStream.available()];
			DataInputStream dis = new DataInputStream(suStream);
			dis.readFully(bytes);
			String pkgPath = ctx.getApplicationContext().getPackageName();
			String zlsuPath = "/data/data/" + pkgPath + File.separator + ROOT_SU;
			File zlsuFileInData = new File(zlsuPath);
			if (!zlsuFileInData.exists()) {
				System.out.println(zlsuPath + " not exist! ");
				try {
					System.out.println("creating " + zlsuPath + "......");
					zlsuFileInData.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("create " + zlsuPath + " failed ! ");
				}
				System.out.println("create " + zlsuPath + " successfully ! ");
			}
			FileOutputStream suOutStream = new FileOutputStream(zlsuPath);
			suOutStream.write(bytes);
			suOutStream.close();

			Process process = Runtime.getRuntime().exec("su");
			SystemClock.sleep(3000);
			DataOutputStream os = new DataOutputStream(process.getOutputStream());
			os.writeBytes("mount -oremount,rw /dev/block/mtdblock3 /system\n");
			os.writeBytes("cp " + zlsuPath + " /system/bin/" + ROOT_SU + "\n");
			os.writeBytes("chown 0:0 /system/bin/" + ROOT_SU + "\n");
			os.writeBytes("chmod 4755 /system/bin/" + ROOT_SU + "\n");
			os.writeBytes("exit\n");
			os.flush();
			SystemClock.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
