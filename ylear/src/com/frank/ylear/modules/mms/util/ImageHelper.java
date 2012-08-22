package com.frank.ylear.modules.mms.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 图片工具类，完成图片的截取
 * 
 * @author XXXX created at 2008-6-19
 * @since 1.0
 * @version
 */
public class ImageHelper
{
	private static final Log log = LogFactory.getLog(MMSFileHelper.class);

	public static BufferedImage imageScale(BufferedImage image, int targetW)
	{
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		double sx = ((double) targetW) / width;
		double sy = sx;
		int targetH = (int) (sy * height);
		return imageScale(image, targetW, targetH);
	}

	public static BufferedImage imageScale(BufferedImage source, int targetW,
			int targetH)
	{
		Image image = source.getScaledInstance(targetW, targetH,
				Image.SCALE_AREA_AVERAGING);
		BufferedImage target = new BufferedImage(targetW, targetH,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = target.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		return target;

	}

	public static boolean resize(File image, int width)
	{
		try
		{
			BufferedImage sourceImage = ImageIO.read(image);
			// 如果原先图片大于width才进行缩放处理
			if (sourceImage.getWidth() > width)
			{
				BufferedImage targetImage = imageScale(sourceImage, width);
				ImageIO.write(targetImage, "jpg", image);
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			log.error(null,e);
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws IOException
	{
		String source = "F:/img_703.jpg";
		resize(new File(source), 260);
	}
}
