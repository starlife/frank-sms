package com.frank.ylear.modules.mms.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


/**
 * MmsFile entity. @author MyEclipse Persistence Tools
 */

public class MmsFile  implements java.io.Serializable {


    // Fields    

     private Long id;
     private String mmsName;
     private Integer frames;
     private String contentSmil;
     private String smilname;
     private Integer contentSize;
     private Integer mmsSize;
     private Date createtime;
     private Set UMmses = new HashSet(0);
     private Set uploadFiles = new HashSet(0);

	/* 下面是一些自定义属性 */
	private Map<Integer,MmsFrame> frameMap = new LinkedHashMap<Integer,MmsFrame>();// 帧集合
	
	private Integer currentFrameId;
	private Long currentFrameSize;
	private Integer currentDuringTime;
	private String currentFrameText;

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

	// Constructors
	public Integer getMmsSize()
	{
		return mmsSize;
	}

	public void setMmsSize(Integer mmsSize)
	{
		this.mmsSize = mmsSize;
	}

    /** default constructor */
    public MmsFile() {
    }

	/** minimal constructor */
    public MmsFile(Integer frames, String contentSmil, String smilname, Integer contentSize) {
        this.frames = frames;
        this.contentSmil = contentSmil;
        this.smilname = smilname;
        this.contentSize = contentSize;
    }
    
    /** full constructor */
    public MmsFile(String mmsName, Integer frames, String contentSmil, String smilname, Integer contentSize, Date createtime, Set UMmses, Set uploadFiles) {
        this.mmsName = mmsName;
        this.frames = frames;
        this.contentSmil = contentSmil;
        this.smilname = smilname;
        this.contentSize = contentSize;
        this.createtime = createtime;
        this.UMmses = UMmses;
        this.uploadFiles = uploadFiles;
    }

   
    // Property accessors

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getMmsName() {
        return this.mmsName;
    }
    
    public void setMmsName(String mmsName) {
        this.mmsName = mmsName;
    }

    public Integer getFrames() {
        return this.frames;
    }
    
    public void setFrames(Integer frames) {
        this.frames = frames;
    }

    public String getContentSmil() {
        return this.contentSmil;
    }
    
    public void setContentSmil(String contentSmil) {
        this.contentSmil = contentSmil;
    }

    public String getSmilname() {
        return this.smilname;
    }
    
    public void setSmilname(String smilname) {
        this.smilname = smilname;
    }

    public Integer getContentSize() {
        return this.contentSize;
    }
    
    public void setContentSize(Integer contentSize) {
        this.contentSize = contentSize;
    }

    public Date getCreatetime() {
        return this.createtime;
    }
    
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Set getUMmses() {
        return this.UMmses;
    }
    
    public void setUMmses(Set UMmses) {
        this.UMmses = UMmses;
    }

    public Set getUploadFiles() {
        return this.uploadFiles;
    }
    
    public void setUploadFiles(Set uploadFiles) {
        this.uploadFiles = uploadFiles;
    }
   

	public Map<Integer, MmsFrame> getFrameMap()
	{
		return frameMap;
	}



}