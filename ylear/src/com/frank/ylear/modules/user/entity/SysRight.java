package com.frank.ylear.modules.user.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * SysRight entity. @author MyEclipse Persistence Tools
 */

public class SysRight  implements java.io.Serializable {


    // Fields    

     private Long id;
     private String rightCode;
     private String rightParentCode;
     private String rightType;
     private String rightText;
     private String rightUrl;
     private String rightTip;
     private Set sysRoleRights = new HashSet(0);

	private String isSelected = "";
	private List<SysRight> childRights;
	private boolean isParent;

    // Constructors

    /** default constructor */
    public SysRight() {
    }

	/** minimal constructor */
    public SysRight(String rightCode) {
        this.rightCode = rightCode;
    }
    
    /** full constructor */
    public SysRight(String rightCode, String rightParentCode, String rightType, String rightText, String rightUrl, String rightTip, Set sysRoleRights) {
        this.rightCode = rightCode;
        this.rightParentCode = rightParentCode;
        this.rightType = rightType;
        this.rightText = rightText;
        this.rightUrl = rightUrl;
        this.rightTip = rightTip;
        this.sysRoleRights = sysRoleRights;
    }

   
    // Property accessors

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getRightCode() {
        return this.rightCode;
    }
    
    public void setRightCode(String rightCode) {
        this.rightCode = rightCode;
    }

    public String getRightParentCode() {
        return this.rightParentCode;
    }
    
    public void setRightParentCode(String rightParentCode) {
        this.rightParentCode = rightParentCode;
    }

    public String getRightType() {
        return this.rightType;
    }
    
    public void setRightType(String rightType) {
        this.rightType = rightType;
    }

    public String getRightText() {
        return this.rightText;
    }
    
    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public String getRightUrl() {
        return this.rightUrl;
    }
    
    public void setRightUrl(String rightUrl) {
        this.rightUrl = rightUrl;
    }

    public String getRightTip() {
        return this.rightTip;
    }
    
    public void setRightTip(String rightTip) {
        this.rightTip = rightTip;
    }

    public Set getSysRoleRights() {
        return this.sysRoleRights;
    }
    
    public void setSysRoleRights(Set sysRoleRights) {
        this.sysRoleRights = sysRoleRights;
    }
   
	public String getIsSelected()
	{
		return isSelected;
	}

	public void setIsSelected(String isSelected)
	{
		this.isSelected = isSelected;
	}

	public List<SysRight> getChildRights()
	{
		return childRights;
	}

	public void setChildRights(List<SysRight> childRights)
	{
		this.childRights = childRights;
	}

	public boolean isParent()
	{
		return isParent;
	}

	public void setParent(boolean isParent)
	{
		this.isParent = isParent;
	}
	
	public String toString()
	{
		return this.getRightCode()+" "+this.getRightText()+" ";
	}








}