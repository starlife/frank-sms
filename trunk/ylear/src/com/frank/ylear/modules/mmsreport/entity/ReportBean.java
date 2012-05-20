package com.frank.ylear.modules.mmsreport.entity;

public class ReportBean
{
	private Long allCount;
	private Long successCount;
	private Long failCount;
	private long unknowCount;
	public Long getAllCount()
	{
		return allCount;
	}
	public void setAllCount(Long allCount)
	{
		this.allCount = allCount;
	}
	public Long getSuccessCount()
	{
		return successCount;
	}
	public void setSuccessCount(Long successCount)
	{
		this.successCount = successCount;
	}
	public Long getFailCount()
	{
		return failCount;
	}
	public void setFailCount(Long failCount)
	{
		this.failCount = failCount;
	}
	public long getUnknowCount()
	{
		return unknowCount;
	}
	public void setUnknowCount(long unknowCount)
	{
		this.unknowCount = unknowCount;
	}
	
	public String toString()
	{
		return "Total:"+this.getAllCount()+" Success:"+this.getSuccessCount()
		+" Fail:"+this.getFailCount()+" Unknow:"+this.getUnknowCount();
	}
}
