
package com.hykj.gamecenter.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.DeviceInfo;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.statistic.ReportConstants;

public class StringUtils {

	private static AtomicInteger mRequestSeq = new AtomicInteger(0);

	private static DecimalFormat SIZ_DF = new DecimalFormat("###.00");
	private final static String[] UNITS = new String[]{
			"GB", "MB", "KB", "B"
	};
	private final static long[] SIZE_DIVIDERS = new long[]{
			1024 * 1024 * 1024, 1024 * 1024, 1024, 1
	};

	public static String byteToString(final long size) {
		if (size < 1)
			return "0B";
		String res = null;
		for (int i = 0; i < SIZE_DIVIDERS.length; i++) {
			final long divider = SIZE_DIVIDERS[i];
			if (size >= divider) {
				res = format(size, divider, UNITS[i]);
				break;
			}
		}
		return res;
	}

	private static String format(final long value, final long divider, final String unit) {
		final double result = divider > 1 ? (double) value / (double) divider : (double) value;
		String str = SIZ_DF.format(result) + unit;
		return str;
	}

	public static String byteToStringNoUnit(final long size) {
		if (size < 1)
			return "0B";
		String res = null;
		for (int i = 0; i < SIZE_DIVIDERS.length; i++) {
			final long divider = SIZE_DIVIDERS[i];
			if (size >= divider) {
				final double result = divider > 1 ? (double) size / (double) divider : (double) size;
				res = SIZ_DF.format(result);
				break;
			}
		}
		return res;
	}

	/**
	 * ���������ص�APKʱ����ԭʼ��URL���ϲ�������ϴ�(���ؼ�¼�ϱ��ϱ�)
	 *
	 * @param url  ��ʼ��URL
	 * @param info ������Ϣ
	 * @return ���ش�ϵͳ�ƵĲ���
	 */
	public static String getReportUrl(DownloadTask info, int appPosition) {
		String originalUrl = "";
		if (info.appDownloadURL != null && !info.appDownloadURL.equals("")) {
			originalUrl = info.appDownloadURL + "?appid=" + info.appId //Ӧ��ID
					+ "&packid=" + info.packId //��ID
					+ "&" + DeviceInfo.getDeviceInfo() //�豸UDI
					+ "&reqNo" + mRequestSeq.getAndIncrement() + "&chnNo=0" //�����ţ�Ĭ��ֵ��
					+ "&chnPos=0" //����λ��
					+ "&clientId=2" //Ӧ��ID��Ĭ��ֵΪ2����Ϸ���ģ������̶�ֵ
					+ "&clientVer=" + App.VersionName() //Ӧ�ð汾
					+ "&clientPos=" + ReportConstants.STAC_APP_POSITION_APP_DETAIL; //Ӧ��λ��
		}
		return originalUrl;
	}

	/**
	 * �ָ��ַ�
	 *
	 * @param url �ϱ����ص�URL
	 * @return ����ԭʼ��URL
	 */
	public static String getOriginalUrl(String url) {
		String URL = "";
		int index = url.indexOf("?");
		if (index > 0) {
			URL = url.substring(index);
		}
		System.out.println("------ originalUrl = " + url);
		return URL;
	}

	// 简单检查手机号码输入是否正确
	public static boolean invalidateNumber(CharSequence text) {
		String inputNumber = text.toString().trim();
		// 判断输入的号码是不是手机号，主要用1开头和为数
		if (TextUtils.isEmpty(inputNumber)) {
			return false;
		}
		return inputNumber.length() == 11;
	}

	// 简单检查验证码是否输入正确
	public static boolean invalidateCode(CharSequence text) {
		String valiCode = text.toString().trim();
		if (TextUtils.isEmpty(valiCode)) {
			return false;
		}
		return valiCode.length() == 4;
	}

	//判断是否为纯数字
	public static boolean isAllNumber(CharSequence text) {
		Pattern p = Pattern.compile("^[0-9]*$");
		Matcher m = p.matcher(text);
		return m.matches();
	}

	/**
	 * 设置 textview 部分字体颜色
	 *
	 * @param start
	 * @param end
	 * @param color
	 * @param textView
	 */
	public static void setTextColor(int start, int end, int color, TextView textView) {
		if (start < 0 || end < 0 || end < start) {
			return;
		}
		SpannableStringBuilder fontStyleBuilder = new SpannableStringBuilder(textView.getText());
		fontStyleBuilder.setSpan(new ForegroundColorSpan(color), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(fontStyleBuilder);
	}

	/**
	 * 获取字符长度，如果是标准的字符，Ascii的范围是0至255，如果是汉字或其他全角字符，Ascii会大于255
	 *
	 * @param s
	 * @return
	 */
	public static int getWordCount(String s) {
		int length = 0;
		for (int i = 0; i < s.length(); i++) {
			int ascii = Character.codePointAt(s, i);
			if (ascii >= 0 && ascii <= 255)
				length++;
			else
				length += 2;

		}
		return length;

	}

	public static String listToString(ArrayList<Integer> list) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			builder.append(list.get(i));
			if (i != list.size() - 1) {
				builder.append(",");
			}
		}
		return builder.toString();
	}

	/**
	 * 解析格式string “key1=value1&key2=value2....”
	 * @param appinfo
	 * @return
	 */
	public static HashMap<String, String> splitString(String appinfo) {
		HashMap<String, String> keyMap = new HashMap<String, String>();
		String[] split = appinfo.split("&");
		for (String values :
				split) {
			String[] keyValue = values.split("=");
			keyMap.put(keyValue[0], keyValue[1]);
		}
		return keyMap;
	}

	public static String bytesToHexString(byte[] src){
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

}
