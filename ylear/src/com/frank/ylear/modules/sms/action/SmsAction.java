package com.frank.ylear.modules.sms.action;

import java.util.ArrayList;
import java.util.List;

import com.frank.ylear.common.constant.Constants;
import com.frank.ylear.common.util.DateUtils;
import com.frank.ylear.common.util.Tools;
import com.frank.ylear.modules.base.action.BaseAction;
import com.frank.ylear.modules.sms.entity.USms;
import com.frank.ylear.modules.sms.service.SmsService;
import com.frank.ylear.modules.unitInfo.entity.TPosition;



public class SmsAction extends BaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SmsService smsService;
	/* 查询bean */
	private USms queryBean = null;
	private USms sms = null;

	private String id = null;


	
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void validateSave()
	{
		if (this.hasErrors())
		{
			return;
		}
		String sendtime=this.getSms().getSendtime();
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
					this.addFieldError("sms.sendtime",
							getText("sms.sendtime.error"));
					return;
				}
			}
			
		}
		this.getSms().setSendtime(sendtime);
		
		//过滤非法号码，重复号码
		String recipient=Tools.parse(this.getSms().getRecipient());
		if(recipient.length()<=0)
		{
			this.addFieldError("sms.recipient",
					getText("sms.recipient.error"));
			return;
		}
		this.getSms().setRecipient(recipient);
		this.getSms().setStatus(0);// 0表示未发送，1表示已发送
	}

	/**
	 * 列表显示,带查询功能
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception
	{
		smsService.getSmsList(this.getQueryBean(), this.getPage());
		return SUCCESS;
	}

	/**
	 * 添加或修改，跳转到该方法
	 */
	public String input() throws Exception
	{
		if (this.getId() != null)
		{
			USms fetched = smsService.getSms(Long.parseLong(id));
			if (fetched != null)
			{
				this.setSms(fetched);
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
			smsService.delSms(Long.parseLong(id));
		}
		return SUCCESS;
	}

	/**
	 * 保存
	 * 
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception
	{
		if (this.getSms() != null)
		{
			smsService.saveSms(this.getSms());
		}
		return Constants.SUCCESS;
	}

	/**
	 * 群发号码选择后跳回
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	/*
	 * public ActionForward toNext(ActionMapping mapping, ActionForm form,
	 * HttpServletRequest request, HttpServletResponse response) throws
	 * Exception { SmsForm myForm = (SmsForm)form; //取得号码字段的值 String
	 * queryStr=request.getParameter("query"); StringBuffer query=new
	 * StringBuffer(); String[] items=queryStr.split("[，,]"); //组装查询条件
	 * //通讯录符合(手机号码=13777802386,姓名=林新正)的号码 //通讯录中所有手机号码 if(items.length==0) {
	 * query.append("通讯录中所有号码"); }else if(items.length==1) {
	 * query.append("通讯录符合(手机号码="+items[0]+")的号码"); }else if(items.length==2) {
	 * query.append("通讯录符合("); if(!items[0].trim().equals("")) {
	 * query.append("手机号码="+items[0]+","); }
	 * query.append("姓名="+items[1]+")的号码"); }else if(items.length==3) {
	 * query.append("通讯录符合("); if(!items[0].trim().equals("")) {
	 * query.append("手机号码="+items[0]+","); } if(!items[1].trim().equals("")) {
	 * query.append("姓名="+items[1]+","); } query.append("部门="+items[2]+")的号码"); }
	 * myForm.getItem().setRecipient(query.toString()); return
	 * mapping.findForward("next"); }
	 */


	
	public SmsService getSmsService()
	{
		return smsService;
	}

	public void setSmsService(SmsService smsService)
	{
		this.smsService = smsService;
	}

	public USms getQueryBean()
	{
		return queryBean;
	}

	public void setQueryBean(USms queryBean)
	{
		this.queryBean = queryBean;
	}

	public USms getSms()
	{
		return sms;
	}

	public void setSms(USms sms)
	{
		this.sms = sms;
	}

}
