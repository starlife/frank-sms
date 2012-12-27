package com.unicom.mm7.bean;

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
	// private Long id;
	// private MmsFile mmsFile;
	private String filename;
	private byte[] filedata;
	private Long filesize;
	private String charset;
	private Integer frameid;
	private String filetype;

	// Constructors

	/** default constructor */
	public UploadFile()
	{
	}

	/** minimal constructor */
	public UploadFile(String filename, byte[] filedata, Long filesize,
			Integer frameid, String filetype)
	{
		this.filename = filename;
		this.filedata = filedata;
		this.filesize = filesize;
		// this.uploadtime = uploadtime;
		this.frameid = frameid;
		this.filetype = filetype;
	}

	public String getFilename()
	{
		return this.filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public byte[] getFiledata()
	{
		return this.filedata;
	}

	public void setFiledata(byte[] filedata)
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
		sb.append("Filename:" + filename + "\r\n");
		sb.append("Filesize:" + filesize + "\r\n");
		sb.append("Filetype:" + filetype + "\r\n");
		sb.append("Frameid:" + frameid + "\r\n");
		sb.append("Charset:" +charset + "\r\n");
		return sb.toString();
	}

	public String getCharset()
	{
		return charset;
	}

	public void setCharset(String charset)
	{
		this.charset = charset;
	}

}
