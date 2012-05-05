package com.vasp.mm7.database.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * MmsFile entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class MmsFile implements java.io.Serializable
{

	// Fields

	private Long id;
	private String mmsName;
	private Integer frames;
	private String contentSmil;
	private String smilname;
	private Integer contentSize;
	private Date createtime;
	private Set uploadFiles = new HashSet(0);

	// Constructors

	/** default constructor */
	public MmsFile()
	{
	}

	/** minimal constructor */
	public MmsFile(String contentSmil, String smilname)
	{
		this.contentSmil = contentSmil;
		this.smilname = smilname;
	}

	/** full constructor */
	public MmsFile(String mmsName, Integer frames, String contentSmil,
			String smilname, Integer contentSize, Date createtime,
			Set uploadFiles)
	{
		this.mmsName = mmsName;
		this.frames = frames;
		this.contentSmil = contentSmil;
		this.smilname = smilname;
		this.contentSize = contentSize;
		this.createtime = createtime;
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

	public String getContentSmil()
	{
		return this.contentSmil;
	}

	public void setContentSmil(String contentSmil)
	{
		this.contentSmil = contentSmil;
	}

	public String getSmilname()
	{
		return this.smilname;
	}

	public void setSmilname(String smilname)
	{
		this.smilname = smilname;
	}

	public Integer getContentSize()
	{
		return this.contentSize;
	}

	public void setContentSize(Integer contentSize)
	{
		this.contentSize = contentSize;
	}

	public Date getCreatetime()
	{
		return this.createtime;
	}

	public void setCreatetime(Date createtime)
	{
		this.createtime = createtime;
	}

	public Set getUploadFiles()
	{
		return this.uploadFiles;
	}

	public void setUploadFiles(Set uploadFiles)
	{
		this.uploadFiles = uploadFiles;
	}

}
