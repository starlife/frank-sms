package com.frank.ylear.modules.mms.entity;

import java.sql.Blob;
import java.util.Date;

/**
 * UploadFile entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class UploadFile implements java.io.Serializable
{

	// Fields

	private Long id;
	private MmsFile mmsFile;
	private String filename;
	private Blob filedata;
	private String filetype;
	private Long filesize;
	private Date uploadtime;
	private Integer framenumber;

	// Constructors

	/** default constructor */
	public UploadFile()
	{
	}

	/** minimal constructor */
	public UploadFile(String filename, Blob filedata, Long filesize,
			Integer framenumber, String filetype)
	{
		this.filename = filename;
		this.filedata = filedata;
		this.filesize = filesize;
		this.framenumber = framenumber;
		this.filetype = filetype;
	}

	/** full constructor */
	public UploadFile(MmsFile mmsFile, String filename, Blob filedata,
			Long filesize, Date uploadtime, Integer framenumber, String filetype)
	{
		this.mmsFile = mmsFile;
		this.filename = filename;
		this.filedata = filedata;
		this.filesize = filesize;
		this.uploadtime = uploadtime;
		this.framenumber = framenumber;
		this.filetype = filetype;
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

	public MmsFile getMmsFile()
	{
		return this.mmsFile;
	}

	public void setMmsFile(MmsFile mmsFile)
	{
		this.mmsFile = mmsFile;
	}

	public String getFilename()
	{
		return this.filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public Blob getFiledata()
	{
		return this.filedata;
	}

	public void setFiledata(Blob filedata)
	{
		this.filedata = filedata;
	}

	public Long getFilesize()
	{
		return this.filesize;
	}

	public void setFilesize(Long filesize)
	{
		this.filesize = filesize;
	}

	public Date getUploadtime()
	{
		return this.uploadtime;
	}

	public void setUploadtime(Date uploadtime)
	{
		this.uploadtime = uploadtime;
	}

	public Integer getFramenumber()
	{
		return this.framenumber;
	}

	public void setFramenumber(Integer framenumber)
	{
		this.framenumber = framenumber;
	}

	public String getFiletype()
	{
		return this.filetype;
	}

	public void setFiletype(String filetype)
	{
		this.filetype = filetype;
	}

}
