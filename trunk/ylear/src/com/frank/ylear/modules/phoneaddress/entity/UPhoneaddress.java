package com.frank.ylear.modules.phoneaddress.entity;

import com.frank.ylear.modules.unitInfo.entity.TPosition;

/**
 * UPhoneaddress entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class UPhoneaddress implements java.io.Serializable
{

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String phonenumber;
	private String name;
	private String department;
	private String area;
	private String post;
	private TPosition TPosition;//单位
	private String homenumber;
	// Constructors

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getHomenumber() {
		return homenumber;
	}

	public void setHomenumber(String homenumber) {
		this.homenumber = homenumber;
	}

	/** default constructor */
	public UPhoneaddress()
	{
	}

	/** minimal constructor */
	public UPhoneaddress(String phonenumber)
	{
		this.phonenumber = phonenumber;
	}


	// Property accessors

	public UPhoneaddress(Long id, String phonenumber, String name,
			String department, String area, String post, TPosition TPosition,
			String homenumber) {
		super();
		this.id = id;
		this.phonenumber = phonenumber;
		this.name = name;
		this.department = department;
		this.area = area;
		this.post = post;
		TPosition = TPosition;
		this.homenumber = homenumber;
	}

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

	public TPosition getTPosition() {
		return TPosition;
	}

	public void setTPosition(TPosition tPosition) {
		TPosition = tPosition;
	}

}
