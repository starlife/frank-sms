package com.frank.ylear.modules.mms.entity;

import com.frank.ylear.common.constant.Constants;

/**
 * �����࣬��ʾÿһ֡
 * 
 * @author Administrator
 */
public class MmsFrame implements java.io.Serializable
{
	
	private String image;// �ļ������õ�ַ������

	private long imageFileSize = 0;

	private String imageFileName;// ������������ļ���
	
	private String imageFileType;//��image/gif,image/jpeg

	private String audio;// �ļ������õ�ַ������

	private long audioFileSize = 0;

	private String audioFileName;// ������������ļ���
	
	private String audioFileType;//��audio/mid  audio/amr

	private String text;// �ı�ֱ��д����

	private long textFileSize = 0;

	private String textFileName;// ������������ļ���
	
	private String textFileType="text/plain";//��text/plain

	private int duringTime = Constants.FRAME_DURING;// ֡�������λ��

	private long frameSize = 0;

	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

	public long getImageFileSize()
	{
		return imageFileSize;
	}

	public void setImageFileSize(long imageFileSize)
	{
		this.imageFileSize = imageFileSize;
	}

	public String getImageFileName()
	{
		return imageFileName;
	}

	public void setImageFileName(String imageFileName)
	{
		this.imageFileName = imageFileName;
	}

	public String getAudio()
	{
		return audio;
	}

	public void setAudio(String audio)
	{
		this.audio = audio;
	}

	public long getAudioFileSize()
	{
		return audioFileSize;
	}

	public void setAudioFileSize(long audioFileSize)
	{
		this.audioFileSize = audioFileSize;
	}

	public String getAudioFileName()
	{
		return audioFileName;
	}

	public void setAudioFileName(String audioFileName)
	{
		this.audioFileName = audioFileName;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public long getTextFileSize()
	{
		return textFileSize;
	}

	public void setTextFileSize(long textFileSize)
	{
		this.textFileSize = textFileSize;
	}

	public String getTextFileName()
	{
		return textFileName;
	}

	public void setTextFileName(String textFileName)
	{
		this.textFileName = textFileName;
	}

	public int getDuringTime()
	{
		return duringTime;
	}

	public void setDuringTime(int durTime)
	{
		this.duringTime = durTime;
	}

	public long getFrameSize()
	{
		return frameSize;
	}

	public void setFrameSize(long frameSize)
	{
		this.frameSize = frameSize;
	}

	public String getImageFileType()
	{
		return imageFileType;
	}

	public void setImageFileType(String imageFileType)
	{
		this.imageFileType = imageFileType;
	}

	public String getAudioFileType()
	{
		return audioFileType;
	}

	public void setAudioFileType(String audioFileType)
	{
		this.audioFileType = audioFileType;
	}

	public String getTextFileType()
	{
		return textFileType;
	}

	public void setTextFileType(String textFileType)
	{
		this.textFileType = textFileType;
	}

}
