package com.frank.ylear.modules.mms.util;

import com.frank.ylear.common.constant.Constants;

/**
 * 辅助类，表示每一帧
 * 
 * @author Administrator
 */
public class MmsFrame implements java.io.Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String image;// 文件内容用地址来代替

	private long imageFileSize = 0;

	private String imageFileName;// 服务器分配的文件名
	
	private String imageFileType;//如image/gif,image/jpeg

	private String audio;// 文件内容用地址来代替

	private long audioFileSize = 0;

	private String audioFileName;// 服务器分配的文件名
	
	private String audioFileType;//如audio/mid  audio/amr

	private String text;// 文本直接写内容

	private long textFileSize = 0;

	private String textFileName;// 服务器分配的文件名
	
	private String textFileType="text/plain";//如text/plain

	private int duringTime = Constants.FRAME_DURING;// 帧间隔，单位秒

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
	
	public String toString()
	{
		StringBuffer sb=new StringBuffer();
		sb.append("image="+image+Constants.NEWLINE);
		sb.append("imageFileSize="+imageFileSize+Constants.NEWLINE);
		sb.append("imageFileName="+imageFileName+Constants.NEWLINE);
		sb.append("imageFileType="+imageFileType+Constants.NEWLINE);
		sb.append("audio="+audio+Constants.NEWLINE);
		sb.append("audioFileSize="+audioFileSize+Constants.NEWLINE);
		sb.append("audioFileName="+audioFileName+Constants.NEWLINE);
		sb.append("audioFileType="+audioFileType+Constants.NEWLINE);
		
		sb.append("text="+text+Constants.NEWLINE);
		sb.append("textFileSize="+textFileSize+Constants.NEWLINE);
		sb.append("textFileName="+textFileName+Constants.NEWLINE);
		sb.append("textFileType="+textFileType+Constants.NEWLINE);
		
		sb.append("duringTime="+duringTime+Constants.NEWLINE);
		sb.append("frameSize="+frameSize+Constants.NEWLINE);
		
		return sb.toString();
	}

}
