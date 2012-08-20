package com.frank.ylear.modules.mms.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 图片工具类，完成图片的截取
 * 
 * @author XXXX created at 2008-6-19
 * @since 1.0
 * @version
 */
public class ImageHelper
{

	/**
	 * 判断一个文件类型名称，是否为图片类型
	 * 
	 * @param fileType
	 *            文件类型名称
	 * @return 是PNG, GIF, JPG图片类型为true否则为false
	 */
	public static boolean isImage(String fileType)
	{
		if (isGif(fileType) || isJpg(fileType) || isPng(fileType))
			return true;
		else
			return false;
	}

	public static boolean isGif(String fileType)
	{
		if (fileType != null && fileType.trim().equalsIgnoreCase("GIF"))
			return true;
		else
			return false;
	}

	public static boolean isJpg(String fileType)
	{
		if (fileType != null && fileType.trim().equalsIgnoreCase("JPG"))
			return true;
		else
			return false;
	}

	public static boolean isPng(String fileType)
	{
		if (fileType != null && fileType.trim().equalsIgnoreCase("PNG"))
			return true;
		else
			return false;
	}

	/**
	 * 获取图片文件的文件类型，JPG, PNG, GIF，如果非图片类型那么返回Undefined
	 * 
	 * @param srcFileName
	 *            图片全路明
	 * @return 图片文件类型JPG, PNG, GIF 如果非图片则为Undefined
	 */
	public static String getImageType(String srcFileName)
	{
		FileInputStream imgFile = null;
		byte[] b = new byte[10];
		int l = -1;
		try
		{
			imgFile = new FileInputStream(srcFileName);
			l = imgFile.read(b);
			imgFile.close();
		}
		catch (Exception e)
		{
			return "Undefined";
		}
		if (l == 10)
		{
			byte b0 = b[0];
			byte b1 = b[1];
			byte b2 = b[2];
			byte b3 = b[3];
			byte b6 = b[6];
			byte b7 = b[7];
			byte b8 = b[8];
			byte b9 = b[9];
			if (b0 == (byte) 'G' && b1 == (byte) 'I' && b2 == (byte) 'F')
			{
				return "GIF";
			}
			else if (b1 == (byte) 'P' && b2 == (byte) 'N' && b3 == (byte) 'G')
			{
				return "PNG";
			}
			else if (b6 == (byte) 'J' && b7 == (byte) 'F' && b8 == (byte) 'I'
					&& b9 == (byte) 'F')
			{
				return "JPG";
			}
			else
			{
				return "Undefined";
			}
		}
		else
		{
			return "Undefined";
		}
	}

	/**
	 * JPG文件Resize方法
	 * 
	 * @param source
	 *            源文件地址
	 * @param dist
	 *            目标文件地址
	 * @param width
	 *            目标宽度
	 * @param height
	 *            目标高度
	 * @throws Exception
	 *             异常信息
	 */
	public static void resizeJPG(String source, String dist, int width,
			int height) throws Exception
	{
		if (!isJpg(getImageType(source)))
			throw new Exception(
					"The source file is not a jpeg file or not exist!");
		BufferedImage sourceImage = ImageIO.read(new File(source));
		BufferedImage targetImage = resize(sourceImage, width);
		ImageIO.write(targetImage, "jpg", new File(dist));
	}

	/**
	 * 实现图像的等比缩放,以targetW为准
	 * 
	 * @param source
	 * @param targetW
	 * @return
	 */
	public static BufferedImage resize(BufferedImage source, int targetW)
	{
		// targetW，targetH分别表示目标长和宽
		int type = source.getType();
		BufferedImage target = null;
		double sx = (double) targetW / source.getWidth();
		double sy = sx;
		int targetH = (int) (sy * source.getHeight());
		
		if (type == BufferedImage.TYPE_CUSTOM)
		{ // handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetW,
					targetH);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		}
		else
			target = new BufferedImage(targetW, targetH, type);
		Graphics2D g = target.createGraphics();
		// smoother than exlax:
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return target;
	}

	public static boolean  resize(File image,int width)
	{
		//File source=image;
		//String dest=image.getAbsolutePath();
		BufferedImage sourceImage=null;
		try
		{
			sourceImage = ImageIO.read(image);
			//如果原先图片大于width才进行缩放处理
			if(sourceImage.getWidth()>=width)
			{
				BufferedImage targetImage = resize(sourceImage,width);
				ImageIO.write(targetImage, "jpg",image);
			}		
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) throws IOException
	{
		String source="F:/img_703.jpg";
		resize(new File(source),260);
	}
}
