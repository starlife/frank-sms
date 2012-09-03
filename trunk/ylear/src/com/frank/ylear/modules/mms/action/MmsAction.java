package com.frank.ylear.modules.mms.action;

import com.frank.ylear.common.constant.Constants;
import com.frank.ylear.common.util.DateUtils;
import com.frank.ylear.common.util.Tools;
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
	 * 列表显示,带查询功能
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
	 * 添加或修改，跳转到该方法
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
	 * 删除
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
		
		String sendtime=this.getMms().getSendtime();
		//check  sendtime
		if (sendtime == null
				|| sendtime.trim().equals(""))
		{
			sendtime=DateUtils.getTimestamp14();
		}
		else
		{
			// 检查时间戳值格式是否正确
			if(!DateUtils.isValidTimestamp14(sendtime))
			{
				sendtime=DateUtils.getTimestamp14(sendtime);
				if(sendtime==null)
				{
					this.addFieldError("mms.sendtime",
							getText("mms.sendtime.error"));
					return;
				}
			}
			
		}
		this.getMms().setSendtime(sendtime);
		//过滤非法号码，重复号码
		String recipient=Tools.parse(this.getMms().getRecipient());
		if(recipient.length()<=0)
		{
			this.addFieldError("mms.recipient",
					getText("mms.recipient.error"));
			return;
		}
		this.getMms().setRecipient(recipient);
		mms.setStatus(0);// 0表示未发送，1表示已发送
	}

	/**
	 * 保存
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
