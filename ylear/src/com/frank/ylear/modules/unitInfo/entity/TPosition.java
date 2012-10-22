package com.frank.ylear.modules.unitInfo.entity;

/**
 * TUnit entity. @author MyEclipse Persistence Tools
 */

public class TPosition implements java.io.Serializable {

	// Fields

	private Integer unitid;
	private String unitname;
	private String unitcode;
	private TPosition TPositionParent;

	// Constructors

	/** default constructor */
	public TPosition() {
	}

	/** minimal constructor */
	public TPosition(String unitname, String unitcode) {
		this.unitname = unitname;
		this.unitcode = unitcode;
	}

	/** full constructor */
	public TPosition(String unitname, String unitcode, TPosition TPositionParent) {
		this.unitname = unitname;
		this.unitcode = unitcode;
		this.TPositionParent = TPositionParent;
	}
	
	public TPosition(Integer unitid, String unitname) {
		super();
		this.unitid = unitid;
		this.unitname = unitname;
	}
	// Property accessors

	public Integer getUnitid() {
		return this.unitid;
	}

	public void setUnitid(Integer unitid) {
		this.unitid = unitid;
	}

	public String getUnitname() {
		return this.unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getUnitcode() {
		return this.unitcode;
	}

	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}

	public TPosition getTPositionParent() {
		return TPositionParent;
	}

	public void setTPositionParent(TPosition tPositionParent) {
		TPositionParent = tPositionParent;
	}

}