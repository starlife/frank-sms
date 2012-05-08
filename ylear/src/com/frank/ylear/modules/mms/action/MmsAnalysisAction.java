package com.frank.ylear.modules.mms.action;

import com.frank.ylear.modules.base.action.BaseAction;
import com.frank.ylear.modules.mms.entity.AnalysisBean;
import com.frank.ylear.modules.mms.entity.SLogMmssubmit;
import com.frank.ylear.modules.mms.service.MmsAnalysisService;
public class MmsAnalysisAction extends BaseAction
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MmsAnalysisService mmsAnalysisService;
	
	private SLogMmssubmit queryBean = null;
	
	private String id=null;
	
	private String ummsid=null;
	
	private Integer status;//����ҳ�洫�Ĳ�ѯ���� ����״̬
	private Integer readstatus;//����ҳ�洫�Ĳ�ѯ���� �û���״̬
	
	private AnalysisBean analysisBean;
	
	
	
	public AnalysisBean getAnalysisBean()
	{
		return analysisBean;
	}



	public void setAnalysisBean(AnalysisBean analysisBean)
	{
		this.analysisBean = analysisBean;
	}
	
	public void setAnalysisBean(SLogMmssubmit submit)
	{
		if(analysisBean==null)
		{
			analysisBean=new AnalysisBean();
		}
		analysisBean.setAllCount(mmsAnalysisService.getAllCount(submit));
		analysisBean.setSuccessCount(mmsAnalysisService.getSuccessCount(submit));
		analysisBean.setFailCount(mmsAnalysisService.getFailedCount(submit));
		analysisBean.setUnknowCount(analysisBean.getAllCount()-
				analysisBean.getSuccessCount()-analysisBean.getFailCount());
		
	}



	public String getUmmsid()
	{
		return ummsid;
	}



	public void setUmmsid(String ummsid)
	{
		this.ummsid = ummsid;
	}



	


	public Integer getStatus()
	{
		return status;
	}



	public void setStatus(Integer status)
	{
		this.status = status;
	}



	public Integer getReadstatus()
	{
		return readstatus;
	}



	public void setReadstatus(Integer readstatus)
	{
		this.readstatus = readstatus;
	}



	public String getId()
	{
		return id;
	}



	public void setId(String id)
	{
		this.id = id;
	}



	/**
	 * �б���ʾ,����ѯ����
	 * @return
	 * @throws Exception
	 */
	public String list()
			throws Exception
	{
		if(queryBean==null)
		{
			queryBean=new SLogMmssubmit();
		}
		if(this.getUmmsid()!=null)
		{
			queryBean.setUmmsid(Long.parseLong(this.getUmmsid()));
		}
		setAnalysisBean(queryBean);
		mmsAnalysisService.getList(queryBean,this.getStatus(),this.getReadstatus(),this.getPage());
		return SUCCESS;
	}
	
	public String del()
	{
		if(this.getId()!=null)
		{
			mmsAnalysisService.del(Long.parseLong(this.getId()));
		}
		return SUCCESS;
	}
	
	

	public MmsAnalysisService getMmsAnalysisService()
	{
		return mmsAnalysisService;
	}

	public void setMmsAnalysisService(MmsAnalysisService mmsAnalysisService)
	{
		this.mmsAnalysisService = mmsAnalysisService;
	}

	public SLogMmssubmit getQueryBean()
	{
		return queryBean;
	}

	public void setQueryBean(SLogMmssubmit queryBean)
	{
		this.queryBean = queryBean;
	}
	
	
	
	

	

}
