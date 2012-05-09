package com.frank.ylear.modules.mms.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * MmsFile entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class MmsFile implements java.io.Serializable
{

	// Fields

	private static final long serialVersionUID = 1L;
	private Long id;
	private String mmsName;
	private Integer frames;
	private String smilname;
	private String smildata;
	private Long smilsize;
	private Long mmsSize;
	private String createtime;
	private Set UMmses = new HashSet(0);
	private Set<UploadFile> uploadFiles = new HashSet<UploadFile>(0);

/* 下面是一些自定义属性 */
	private Map<Integer,MmsFrame> frameMap = new LinkedHashMap<Integer,MmsFrame>();// 帧集合
	
	private Integer currentFrameId;
	private Long currentFrameSize;
	private Integer currentDuringTime;
	private String currentFrameText;
	// Constructors

	/** default constructor */
	public MmsFile()
	{
	}

	/** minimal constructor */
	public MmsFile(Integer frames, String smilname, String smildata,
			Long smilsize, Long mmsSize)
	{
		this.frames = frames;
		this.smilname = smilname;
		this.smildata = smildata;
		this.smilsize = smilsize;
		this.mmsSize = mmsSize;
	}

	/** full constructor */
	public MmsFile(String mmsName, Integer frames, String smilname,
			String smildata, Long smilsize, Long mmsSize, String createtime,
			Set UMmses, Set<UploadFile> uploadFiles)
	{
		this.mmsName = mmsName;
		this.frames = frames;
		this.smilname = smilname;
		this.smildata = smildata;
		this.smilsize = smilsize;
		this.mmsSize = mmsSize;
		this.createtime = createtime;
		this.UMmses = UMmses;
		this.uploadFiles = uploadFiles;
	}

	// Property accessors

	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getMmsName()
	{
		return this.mmsName;
	}

	public void setMmsName(String mmsName)
	{
		this.mmsName = mmsName;
	}

	public Integer getFrames()
	{
		return this.frames;
	}

	public void setFrames(Integer frames)
	{
		this.frames = frames;
	}

	public String getSmilname()
	{
		return this.smilname;
	}

	public void setSmilname(String smilname)
	{
		this.smilname = smilname;
	}

	public String getSmildata()
	{
		return this.smildata;
	}

	public void setSmildata(String smildata)
	{
		this.smildata = smildata;
	}

	public Long getSmilsize()
	{
		return this.smilsize;
	}

	public void setSmilsize(Long smilsize)
	{
		this.smilsize = smilsize;
	}

	public Long getMmsSize()
	{
		return this.mmsSize;
	}

	public void autoSetMmsSize()
	{
		long mmsSize = 0;
		synchronized (uploadFiles)
		{
			Iterator<UploadFile> it = uploadFiles.iterator();
			while (it.hasNext())
			{
				UploadFile upload = it.next();
				mmsSize += upload.getFilesize();
			}
		}
		mmsSize += this.getSmilsize();
		this.mmsSize = mmsSize;
	}
	public void setMmsSize(Long mmsSize)
	{
		this.mmsSize = mmsSize;
	}

	public String getCreatetime()
	{
		return this.createtime;
	}

	public void setCreatetime(String createtime)
	{
		this.createtime = createtime;
	}

	public Set getUMmses()
	{
		return this.UMmses;
	}

	public void setUMmses(Set UMmses)
	{
		this.UMmses = UMmses;
	}
	public boolean addUploadFile(UploadFile upload)
	{
		return this.uploadFiles.add(upload);
	}

	
	public Set<UploadFile> getUploadFiles()
	{
		return this.uploadFiles;
	}

	public void setUploadFiles(Set<UploadFile> uploadFiles)
	{
		this.uploadFiles = uploadFiles;
	}
	
	public Map<Integer, MmsFrame> getFrameMap()
	{
		return frameMap;
	}
	
	public String getCurrentFrameText()
	{
		return currentFrameText;
	}

	public void setCurrentFrameText(String currentFrameText)
	{
		this.currentFrameText = currentFrameText;
	}

	public Integer getCurrentFrameId()
	{
		return currentFrameId;
	}

	public void setCurrentFrameId(Integer currentFrameId)
	{
		this.currentFrameId = currentFrameId;
	}

	public Long getCurrentFrameSize()
	{
		return currentFrameSize;
	}

	public void setCurrentFrameSize(Long currentFrameSize)
	{
		this.currentFrameSize = currentFrameSize;
	}

	public Integer getCurrentDuringTime()
	{
		return currentDuringTime;
	}

	public void setCurrentDuringTime(Integer currentDuringTime)
	{
		this.currentDuringTime = currentDuringTime;
	}
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("MmsName:" + mmsName + "\r\n");
		sb.append("Frames:" + frames + "\r\n");
		sb.append("Smilname:" + smilname + "\r\n");
		sb.append("Smildate:" + smildata + "\r\n");
		sb.append("Smilsize:" + smilsize + "\r\n");
		sb.append("MmsSize:" + mmsSize + "\r\n");
		sb.append("Createtime:" + createtime + "\r\n");
		sb.append("=============attachments=======\r\n");
		Iterator<UploadFile> it = uploadFiles.iterator();
		while (it.hasNext())
		{
			sb.append(it.next());
		}
		sb.append("=============end=======\r\n");
		return sb.toString();
	}
	

}
