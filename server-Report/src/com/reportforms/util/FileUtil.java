package com.reportforms.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.DecimalFormat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件工具类
 * 
 * @author liwei.sz
 * 
 */
public class FileUtil {
	static Logger logger = Logger.getLogger(FileUtil.class);

	// 文件上传
	public static void copyInputStream(InputStream stream, File file) throws IOException {
		FileUtils.copyInputStreamToFile(stream, file);
	}

	/**
	 * 复制文件或者目录,复制前后文件完全一样。
	 * 
	 * @param resFilePath
	 *            源文件路径
	 * @param distFolder
	 *            目标文件夹
	 * @IOException 当操作发生异常时抛出
	 */
	public static String copyFile(String resFilePath, String distFolder) throws IOException {
		File resFile = new File(resFilePath);
		File distFile = new File(distFolder);
		if (resFile.isDirectory()) {
			FileUtils.copyDirectoryToDirectory(resFile, distFile);
		} else if (resFile.isFile()) {
			FileUtils.copyFileToDirectory(resFile, distFile, true);
		}
		return distFile.getPath()+File.separator+resFile.getName();
	}


	/**
	 * 移动文件或者目录,移动前后文件完全一样,如果目标文件夹不存在则创建。
	 * 
	 * @param resFilePath
	 *            源文件路径
	 * @param distFolder
	 *            目标文件夹
	 * @IOException 当操作发生异常时抛出
	 */
	public static void moveFile(String resFilePath, String distFolder) throws IOException {
		File resFile = new File(Constant.LOCAL_FILE_PATH + resFilePath);
		File distFile = new File(Constant.LOCAL_FILE_PATH + distFolder);
		FileUtils.moveFile(resFile, distFile);
	}


	/**
	 * 读取文件或者目录的大小
	 * 
	 * @param distFilePath
	 *            目标文件或者文件夹
	 * @return 文件或者目录的大小，如果获取失败，则返回-1
	 */
	public static long genFileSize(String distFilePath) {
		File distFile = new File(distFilePath);
		if (distFile.isFile()) {
			return distFile.length();
		} else if (distFile.isDirectory()) {
			return FileUtils.sizeOfDirectory(distFile);
		}
		return -1L;
	}

	/**
	 * 判断一个文件是否存在
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 存在返回true，否则返回false
	 */
	public static boolean isExist(String filePath) {
		return new File(filePath).exists();
	}

	/**
	 * 创建目录
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean mkdirs(String dir) {
		File f = new File(dir);
		if (!f.exists()) {
			return f.mkdirs();
		}
		return false;
	}

	/**
	 * 本地某个目录下的文件列表（不递归）
	 * 
	 * @param folder
	 *            ftp上的某个目录
	 * @param suffix
	 *            文件的后缀名（比如.mov.xml)
	 * @return 文件名称列表
	 */
	public static String[] listFilebySuffix(String folder, String suffix) {
		IOFileFilter fileFilter1 = new SuffixFileFilter(suffix);
		IOFileFilter fileFilter2 = new NotFileFilter(DirectoryFileFilter.INSTANCE);
		FilenameFilter filenameFilter = new AndFileFilter(fileFilter1, fileFilter2);
		return new File(folder).list(filenameFilter);
	}

	/**
	 * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功！)
	 * 
	 * @param res
	 *            原字符串
	 * @param filePath
	 *            文件路径
	 * @return 成功标记
	 */
	public static boolean string2File(String res, String filePath) {
		boolean flag = true;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			File distFile = new File(filePath);
			if (!distFile.getParentFile().exists())
				distFile.getParentFile().mkdirs();
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new FileWriter(distFile));
			char buf[] = new char[1024]; // 字符缓冲区
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	// 获取文件后缀
	public static String getFileSuffix(String fileName) {
		if ((fileName != null) && (fileName.length() > 0)) {
			int dot = fileName.lastIndexOf('.');
			if ((dot > -1) && (dot < (fileName.length() - 1))) {
				return fileName.substring(dot + 1);
			}
		}
		return null;
	}

	// 去除文件后缀
	public static String trimExtension(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');
			if ((i > -1) && (i < (filename.length()))) {
				return filename.substring(0, i);
			}
		}
		return filename;
	}

	/**
	 * 判断上传的文件的编码格式
	 * 
	 * @param htmlFile
	 *            :上传的文件
	 * @return 文件编码格式
	 * @throws Exception
	 */
	public static String codeString(MultipartFile htmlFile) throws Exception {
		BufferedInputStream bin = new BufferedInputStream(htmlFile.getInputStream());
		int p = (bin.read() << 8) + bin.read();
		String code = null;
		switch (p) {
		case 0xefbb:
			code = "UTF-8";
			break;
		case 0xfffe:
			code = "Unicode";
			break;
		case 0xfeff:
			code = "UTF-16BE";
			break;
		default:
			code = "GBK";
		}

		return code;
	}

	// 计算文件大小
	public static String getFileSize(int size) {
		if (size < 1024) {
			// 显示kb
			return size + "kb";
		} else {
			// 计算mb以上
			return size / 1024 + "mb";
		}
	}

	public static String sizeFormat(int size) {
		DecimalFormat df = new DecimalFormat("###.##");
		float f;
		if (size < 1024 * 1024) {
			f = (float) ((float) size / (float) 1024);
			return (df.format(Float.valueOf(f).doubleValue() + 0) + "KB");
		} else {
			f = (float) ((float) size / (float) (1024 * 1024));
			return (df.format(Float.valueOf(f).doubleValue()) + "MB");
		}
	}
	
}
