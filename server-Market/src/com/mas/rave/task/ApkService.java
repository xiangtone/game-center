package com.mas.rave.task;

import com.cundong.utils.DiffUtils;
import com.cundong.utils.PatchUtils;

public class ApkService {

	static {
		//System.out.println("@@@@@@@@@@java.library.path="+System.getProperty("java.library.path"));
		System.loadLibrary("AppUpdate");
	}
	
	public static boolean diff(String oldPath, String newPath, String patchPath) {
		// 1.
		try{
			long start = System.currentTimeMillis();
			System.out.println("开始生成差异包，请等待...");
			System.out.println("oldPath=" + oldPath);
			System.out.println("newPath=" + newPath);
			DiffUtils.genDiff(oldPath, newPath, patchPath);
			long end = System.currentTimeMillis();
			System.out.println("生成差异包成功，" + patchPath + "，耗时：" + (end - start)
					/ 1000 + "秒！！");
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void patch(String oldPath, String thenewPath, String patchPath) {
		// 2.
		System.out.println("开始合成新包.");
		System.out.println("oldPath=" + oldPath);
		System.out.println("patchPath=" + patchPath);
		long start = System.currentTimeMillis();
		PatchUtils.patch(oldPath, thenewPath, patchPath);
		long end = System.currentTimeMillis();
		System.out.println("合成新包成功，" + thenewPath + "，耗时：" + (end - start)
				/ 1000 + "秒！！");
	}
}
