package com.mas.rave.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import com.aviyehuda.easyimage.Image;

/**
 * 图片操作类
 * 
 * @author liwei.sz
 * 
 */
public class PicUtil {
	public static String[] getPic(File picture) {
		String[] pics = new String[3];
		try {
			FileInputStream fis = new FileInputStream(picture);
			BufferedImage sourceImg = ImageIO.read(fis);
			//pics[0] = String.format("%.1f", picture.length() / 1024.0);
			pics[0] = String.valueOf(picture.length());
			pics[1] = String.valueOf(sourceImg.getWidth());
			pics[2] = String.valueOf(sourceImg.getHeight());
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pics;
	}

	// 等比缩放图片
	public static void zoomPic(String oldPath, String newPath, int newWidth, int newHeight) {
		Image image = new Image(oldPath);
		image.resize(newWidth, newHeight);
		image.saveAs(newPath);
	}

	public static void main(String[] args) {
		PicUtil.zoomPic("d:\\te.png", "d:\\te.png", 480, 800);
	}
}
