package com.frank.ylear.modules.mms.action;

import com.frank.ylear.common.constant.Constants;
import com.frank.ylear.common.util.DateUtils;
import com.frank.ylear.modules.base.action.BaseAction;
import com.frank.ylear.modules.mms.entity.UMms;
import com.frank.ylear.modules.mms.service.MmsService;

public class MmsAction extends BaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MmsService mmsService;
	private UMms queryBean = null;
	private UMms mms = null;
	private String id = null;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public MmsService getMmsService()
	{
		return mmsService;
	}

	public void setMmsService(MmsService mmsService)
	{
		this.mmsService = mmsService;
	}

	/**
	 * �б���ʾ,����ѯ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception
	{
		mmsService.getMmsList(this.getQueryBean(), this.getPage());
		return SUCCESS;
	}

	/**
	 * ��ӻ��޸ģ���ת���÷���
	 */
	public String input() throws Exception
	{
		if (this.getId() != null)
		{
			UMms fetched = mmsService.getMms(Long.parseLong(id));
			if (fetched != null)
			{
				this.setMms(fetched);
			}
		}
		return "input";

	}

	/**
	 * ɾ��
	 */
	public String del() throws Exception
	{
		if (this.getId() != null)
		{
			mmsService.delMms(Long.parseLong(id));
		}
		return SUCCESS;
	}

	public void validateSave()
	{
		if (this.hasErrors())
		{
			return;
		}

		if (this.getMms().getSendtime() == null
				|| this.getMms().getSendtime().trim().equals(""))
		{
			this.getMms().setSendtime(DateUtils.getTimestamp14());
		}
		else
		{
			// ���ʱ���ֵ��ʽ�Ƿ���ȷ
			String sendtime=DateUtils.getTimestamp14(this.getMms().getSendtime());
			if(sendtime==null)
			{
				this.addFieldError("mms.sendtime",
						getText("mms.sendtime.error"));
				return;
			}
			this.getMms().setSendtime(sendtime);
		}
		mms.setStatus(0);// 0��ʾδ���ͣ�1��ʾ�ѷ���
	}

	/**
	 * ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception
	{
		if (this.getMms() != null)
		{
			mmsService.saveMms(mms);
		}
		return Constants.SUCCESS;
	}

	public UMms getMms()
	{
		return mms;
	}

	public void setMms(UMms mms)
	{
		this.mms = mms;
	}

	public void setQueryBean(UMms queryBean)
	{
		this.queryBean = queryBean;
	}

	public UMms getQueryBean()
	{
		return queryBean;
	}

}
