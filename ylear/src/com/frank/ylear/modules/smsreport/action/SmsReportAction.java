package com.frank.ylear.modules.smsreport.action;

import com.frank.ylear.modules.base.action.BaseAction;
import com.frank.ylear.modules.mmsreport.entity.ReportBean;
import com.frank.ylear.modules.smsreport.entity.SmsSubmitBean;
import com.frank.ylear.modules.smsreport.service.SmsReportService;

public class SmsReportAction extends BaseAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SmsReportService smsReportService;

	private SmsSubmitBean queryBean = null;

	private String id = null;

	private String sessionid = null;

	private Integer status;// 接收页面传的查询参数 发送状态

	private ReportBean reportBean;

	public void setReport(SmsSubmitBean submit)
	{
		this.setReportBean(smsReportService.getReport(submit));

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
			queryBean = new SmsSubmitBean();
		}
		if (this.getSessionid() != null)
		{
			queryBean.setSessionid(Long.parseLong(this.getSessionid()));
		}
		if((queryBean.getDestId()==null||queryBean.getDestId().equals(""))&&(this.getStatus()==null||this.getStatus()==-1))
		{
			setReport(queryBean);
		}
		smsReportService.getList(queryBean, this.getStatus(), this.getPage());
		return SUCCESS;
	}

	public String del()
	{
		if (this.getId() != null)
		{
			smsReportService.del(Long.parseLong(this.getId()));
		}
		return SUCCESS;
	}

	public SmsReportService getSmsReportService()
	{
		return smsReportService;
	}

	public void setSmsReportService(SmsReportService smsReportService)
	{
		this.smsReportService = smsReportService;
	}

	public SmsSubmitBean getQueryBean()
	{
		return queryBean;
	}

	public void setQueryBean(SmsSubmitBean queryBean)
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
