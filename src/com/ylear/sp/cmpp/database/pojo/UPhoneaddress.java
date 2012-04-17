package com.ylear.sp.cmpp.database.pojo;

/**
 * UPhoneaddress entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class UPhoneaddress implements java.io.Serializable
{

	// Fields

	private Long id;
	private String phonenumber;
	private String name;
	private String department;
	private String area;

	// Constructors

	/** default constructor */
	public UPhoneaddress()
	{
	}

	/** minimal constructor */
	public UPhoneaddress(String phonenumber)
	{
		this.phonenumber = phonenumber;
	}

	/** full constructor */
	public UPhoneaddress(String phonenumber, String name, String department,
			String area)
	{
		this.phonenumber = phonenumber;
		this.name = name;
		this.department = department;
		this.area = area;
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

	public String getPhonenumber()
	{
		return this.phonenumber;
	}

	public void setPhonenumber(String phonenumber)
	{
		this.phonenumber = phonenumber;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDepartment()
	{
		return this.department;
	}

	public void setDepartment(String department)
	{
		this.department = department;
	}

	public String getArea()
	{
		return this.area;
	}

	public void setArea(String area)
	{
		this.area = area;
	}

}
