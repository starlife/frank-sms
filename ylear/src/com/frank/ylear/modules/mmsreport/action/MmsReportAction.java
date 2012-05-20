package com.frank.ylear.modules.mmsreport.action;

import com.frank.ylear.modules.base.action.BaseAction;
import com.frank.ylear.modules.mmsreport.entity.ReportBean;
import com.frank.ylear.modules.mmsreport.entity.SubmitBean;
import com.frank.ylear.modules.mmsreport.service.MmsReportService;

public class MmsReportAction extends BaseAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MmsReportService mmsReportService;

	private SubmitBean queryBean = null;

	private String id = null;

	private String sessionid = null;

	private Integer status;// 接收页面传的查询参数 发送状态

	private ReportBean reportBean;

	public void setReportBean(SubmitBean submit)
	{
		this.setReportBean(mmsReportService.getReport(submit));

	}

	/**
	 * 列表显示,带查询功能
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception
	{
		if (queryBean == null)
		{
			queryBean = new SubmitBean();
		}
		if (this.getSessionid() != null)
		{
			queryBean.setSessionid(Long.parseLong(this.getSessionid()));
		}
		setReportBean(queryBean);
		mmsReportService.getList(queryBean, this.getStatus(), this.getPage());
		return SUCCESS;
	}

	public String del()
	{
		if (this.getId() != null)
		{
			mmsReportService.del(Long.parseLong(this.getId()));
		}
		return SUCCESS;
	}

	public MmsReportService getMmsReportService()
	{
		return mmsReportService;
	}

	public void setMmsReportService(MmsReportService mmsReportService)
	{
		this.mmsReportService = mmsReportService;
	}

	public SubmitBean getQueryBean()
	{
		return queryBean;
	}

	public void setQueryBean(SubmitBean queryBean)
	{
		this.queryBean = queryBean;
	}

	public ReportBean getReportBean()
	{
		return reportBean;
	}

	public void setReportBean(ReportBean reportBean)
	{
		this.reportBean = reportBean;
	}

	public String getSessionid()
	{
		return sessionid;
	}

	public void setSessionid(String sessionid)
	{
		this.sessionid = sessionid;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

}
