package com.mas.rave.common;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.res.AXmlResourceParser;

public class MasApkManifestParser {
	private static final String DEFAULT_XML = "AndroidManifest.xml";
	private String PACKAGE_NAME;
	private Integer VERSION_CODE;
	private String VERSION_NAME;
	private static final float[] RADIX_MULTS = { 0.0039063F, 3.051758E-005F,
			1.192093E-007F, 4.656613E-010F };

	private static final String[] DIMENSION_UNITS = { "px", "dip", "sp", "pt",
			"in", "mm", "", "" };

	private static final String[] FRACTION_UNITS = { "%", "%p", "", "", "", "",
			"", "" };

	public void parse(String apkPath) {
		File apkFile = new File(apkPath);
		parse(apkFile);
	}

	public void parse(File apkFile)throws RuntimeException {
		ZipFile file = null;
		try {
			file = new ZipFile(apkFile, 1);
			ZipEntry entry = file.getEntry("AndroidManifest.xml");

			AXmlResourceParser parser = new AXmlResourceParser();
			parser.open(file.getInputStream(entry));
			int type;
			while ((type = parser.next()) != 1) {
				switch (type) {
				case 0:
					break;
				case 2:
					int i = 0;
					for (int size = parser.getAttributeCount(); i != size; ++i) {
						if ("versionCode".equals(parser.getAttributeName(i))) {
							this.VERSION_CODE = Integer.valueOf(Integer
									.parseInt(getAttributeValue(parser, i)));
						}
						if ("versionName".equals(parser.getAttributeName(i))) {
							this.VERSION_NAME = getAttributeValue(parser, i);
						}
						if (!("package".equals(parser.getAttributeName(i))))
							continue;
						this.PACKAGE_NAME = getAttributeValue(parser, i);
					}

					continue;
				case 3:
				case 1:
				case 4:
				}

			}

			parser.close();
			file.close();
		} catch (Throwable e) {
			throw new RuntimeException("apk包解析异常",e);
		}finally{
			if(file != null){
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static String getAttributeValue(AXmlResourceParser parser, int index) {
		int type = parser.getAttributeValueType(index);
		int data = parser.getAttributeValueData(index);
		if (type == 3) {
			return parser.getAttributeValue(index);
		}
		if (type == 2) {
			return String.format("?%s%08X", new Object[] { getPackage(data),
					Integer.valueOf(data) });
		}
		if (type == 1) {
			return String.format("@%s%08X", new Object[] { getPackage(data),
					Integer.valueOf(data) });
		}
		if (type == 4) {
			return String.valueOf(Float.intBitsToFloat(data));
		}
		if (type == 17) {
			return String.format("0x%08X",
					new Object[] { Integer.valueOf(data) });
		}
		if (type == 18) {
			return ((data != 0) ? "true" : "false");
		}
		if (type == 5) {
			return Float.toString(complexToFloat(data))
					+ DIMENSION_UNITS[(data & 0xF)];
		}
		if (type == 6) {
			return Float.toString(complexToFloat(data))
					+ FRACTION_UNITS[(data & 0xF)];
		}
		if ((type >= 28) && (type <= 31)) {
			return String.format("#%08X",
					new Object[] { Integer.valueOf(data) });
		}
		if ((type >= 16) && (type <= 31)) {
			return String.valueOf(data);
		}
		return String.format("<0x%X, type 0x%02X>",
				new Object[] { Integer.valueOf(data), Integer.valueOf(type) });
	}

	private static String getPackage(int id) {
		if (id >>> 24 == 1) {
			return "android:";
		}
		return "";
	}

	public static float complexToFloat(int complex) {
		return ((complex & 0xFFFFFF00) * RADIX_MULTS[(complex >> 4 & 0x3)]);
	}

	public String getPackageName() {
		return this.PACKAGE_NAME;
	}

	public Integer getVersionCode() {
		return this.VERSION_CODE;
	}

	public String getVersionName() {
		return this.VERSION_NAME;
	}
}
