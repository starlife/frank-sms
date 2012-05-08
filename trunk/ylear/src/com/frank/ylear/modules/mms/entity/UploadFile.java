package com.frank.ylear.modules.mms.entity;

import java.sql.Blob;

/**
 * UploadFile entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class UploadFile implements java.io.Serializable
{

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private MmsFile mmsFile;
	private String filename;
	private Blob filedata;
	private Long filesize;
	private String uploadtime;
	private Integer frameid;
	private String filetype;

	// Constructors

	/** default constructor */
	public UploadFile()
	{
	}

	/** minimal constructor */
	public UploadFile(String filename, Blob filedata, Long filesize,
			String uploadtime, Integer frameid, String filetype)
	{
		this.filename = filename;
		this.filedata = filedata;
		this.filesize = filesize;
		this.uploadtime = uploadtime;
		this.frameid = frameid;
		this.filetype = filetype;
	}

	/** full constructor */
	public UploadFile(MmsFile mmsFile, String filename, Blob filedata,
			Long filesize, String uploadtime, Integer frameid, String filetype)
	{
		this.mmsFile = mmsFile;
		this.filename = filename;
		this.filedata = filedata;
		this.filesize = filesize;
		this.uploadtime = uploadtime;
		this.frameid = frameid;
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

	public String getUploadtime()
	{
		return this.uploadtime;
	}

	public void setUploadtime(String uploadtime)
	{
		this.uploadtime = uploadtime;
	}

	public Integer getFrameid()
	{
		return this.frameid;
	}

	public void setFrameid(Integer frameid)
	{
		this.frameid = frameid;
	}

	public String getFiletype()
	{
		return this.filetype;
	}

	public void setFiletype(String filetype)
	{
		this.filetype = filetype;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Mmsid:" + (mmsFile != null ? mmsFile.getId() : "") + "\r\n");
		sb.append("Filename:" + filename + "\r\n");
		sb.append("Filesize:" + filesize + "\r\n");
		sb.append("Filetype:" + filetype + "\r\n");
		sb.append("Frameid:" + frameid + "\r\n");
		sb.append("Uploadtime:" + uploadtime + "\r\n");
		return sb.toString();
	}

}
