package com.frank.ylear.modules.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import com.frank.ylear.common.constant.Constants;
import com.frank.ylear.modules.base.action.BaseAction;

public class ImageAction extends BaseAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ������������֤������Դ
	 */

	private static final char[] source = new char[] {

	'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'

	};

	/**
	 *���ɫ
	 */

	private static final Color[] colors = new Color[] {

	Color.BLUE, Color.BLACK

	};

	/**
	 * �����֤�������
	 */

	private static final Font font = new Font("����", Font.ITALIC, 15);

	/**
	 * �����������������������
	 */

	private static final Random rdm = new Random();

	private String text = "";

	private byte[] bytes = null;

	private String contentType = "image/png";

	public byte[] getImageBytes()
	{
		return this.bytes;
	}

	public String getContentType()
	{
		return this.contentType;
	}

	public void setContentType(String value)
	{
		this.contentType = value;
	}

	public int getContentLength()
	{
		return bytes.length;
	}

	/**
	 *�
	 */

	private void generateText()
	{
		char[] source = new char[4];
		for (int i = 0; i < source.length; i++)
		{
			source[i] = ImageAction.source[rdm
					.nextInt(ImageAction.source.length)];
		}
		this.text = new String(source);
		// ����Session
		getSession().put(Constants.VALIDATE_CODE_KEY, this.text);
	}

	
	private BufferedImage createImage()
	{
		int width = 40;
		int height = 15;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.setFont(font);
		for (int i = 0; i < this.text.length(); i++)
		{
			g.setColor(colors[rdm.nextInt(colors.length)]);
			g.drawString(this.text.substring(i, i + 1), 2 + i * 8, 12);
		}
		g.dispose();
		return image;
	}

	
	private void generatorImageBytes(BufferedImage image)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			javax.imageio.ImageIO.write(image, "jpg", bos);
			this.bytes = bos.toByteArray();
		}
		catch (Exception ex)
		{
		}
		finally
		{
			try
			{
				bos.close();
			}
			catch (Exception ex1)
			{
			}
		}
	}

	
	public String doDefault()
	{
		this.generateText();
		BufferedImage image = this.createImage();
		this.generatorImageBytes(image);
		return "image";
	}

}
