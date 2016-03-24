package com.mas.rave.util;
import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import javax.imageio.ImageIO;

public class Image {
	private BufferedImage bufferedImage;
	private String fileName;
	public Image(File imageFile)
	  {
	    try
	    {
	      this.bufferedImage = ImageIO.read(imageFile);
	      this.fileName = imageFile.getAbsolutePath();
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      this.bufferedImage = null;
	      imageFile = null;
	    }
	  }
	public Image(String imageFilePath)
	{
	    this(new File(imageFilePath));
	}
	public void resize(int newWidth, int newHeight)
	  {
	    int oldWidth = this.bufferedImage.getWidth();
	    int oldHeight = this.bufferedImage.getHeight();
	    if ((newWidth == -1) || (newHeight == -1)) {
	      if (newWidth == -1)
	      {
	        if (newHeight == -1) {
	          return;
	        }
	        newWidth = newHeight * oldWidth / oldHeight;
	      }
	      else
	      {
	        newHeight = newWidth * oldHeight / oldWidth;
	      }
	    }
	    BufferedImage result = 
	      new BufferedImage(newWidth, newHeight, 4);
	    
	    int widthSkip = oldWidth / newWidth;
	    int heightSkip = oldHeight / newHeight;
	    if (widthSkip == 0) {
	      widthSkip = 1;
	    }
	    if (heightSkip == 0) {
	      heightSkip = 1;
	    }
	    for (int x = 0; x < oldWidth; x += widthSkip) {
	      for (int y = 0; y < oldHeight; y += heightSkip)
	      {
	        int rgb = this.bufferedImage.getRGB(x, y);
	        if ((x / widthSkip < newWidth) && (y / heightSkip < newHeight)) {
	          result.setRGB(x / widthSkip, y / heightSkip, rgb);
	        }
	      }
	    }
	    this.bufferedImage = result;
	  }
	
	 public void saveAs(String fileName)
	  {
	    saveImage(new File(fileName));
	    this.fileName = fileName;
	  }
	 
	 private void saveImage(File file)
	  {
	    try
	    {
	      ImageIO.write(this.bufferedImage, getFileType(file), file);
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	  }
	 
	 private String getFileType(File file)
	  {
	    String fileName = file.getName();
	    int idx = fileName.lastIndexOf(".");
	    if (idx == -1) {
	      throw new RuntimeException("Invalid file name");
	    }
	    return fileName.substring(idx + 1);
	  }
}
