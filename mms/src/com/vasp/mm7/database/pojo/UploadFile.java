package com.vasp.mm7.database.pojo;

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
	private String filedata;
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
	public UploadFile(String filename, String filedata, Long filesize,
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
	public UploadFile(MmsFile mmsFile, String filename, String filedata,
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

	public String getFiledata()
	{
		return this.filedata;
	}

	public void setFiledata(String filedata)
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

}
