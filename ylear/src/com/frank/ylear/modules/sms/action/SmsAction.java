package com.frank.ylear.modules.sms.action;

import java.text.ParseException;
import java.util.Date;

import com.frank.ylear.common.constant.Constants;
import com.frank.ylear.modules.base.action.BaseAction;
import com.frank.ylear.modules.sms.entity.USms;
import com.frank.ylear.modules.sms.service.SmsService;

public class SmsAction extends BaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SmsService smsService;
	/* ��ѯbean */
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
		if (this.getSms().getSendtimeStr() == null
				|| this.getSms().getSendtimeStr().trim().equals(""))
		{
			this.getSms().setSendtime(new Date());
		}
		else
		{

			try
			{
				Date d=Constants.SDF.parse(this.getSms().getSendtimeStr());
				this.getSms().setSendtime(d);

			}
			catch (ParseException ex)
			{
				this.addFieldError("sms.sendtime",
						getText("sms.sendtime.error"));
				return;
			}
		}
		this.getSms().setStatus(0);// 0��ʾδ���ͣ�1��ʾ�ѷ���
	}

	/**
	 * �б���ʾ,����ѯ����
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
	 * ��ӻ��޸ģ���ת���÷���
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
	 * ɾ��
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
	 * ����
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
	 * Ⱥ������ѡ�������
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
	 * Exception { SmsForm myForm = (SmsForm)form; //ȡ�ú����ֶε�ֵ String
	 * queryStr=request.getParameter("query"); StringBuffer query=new
	 * StringBuffer(); String[] items=queryStr.split("[��,]"); //��װ��ѯ����
	 * //ͨѶ¼����(�ֻ�����=13777802386,����=������)�ĺ��� //ͨѶ¼�������ֻ����� if(items.length==0) {
	 * query.append("ͨѶ¼�����к���"); }else if(items.length==1) {
	 * query.append("ͨѶ¼����(�ֻ�����="+items[0]+")�ĺ���"); }else if(items.length==2) {
	 * query.append("ͨѶ¼����("); if(!items[0].trim().equals("")) {
	 * query.append("�ֻ�����="+items[0]+","); }
	 * query.append("����="+items[1]+")�ĺ���"); }else if(items.length==3) {
	 * query.append("ͨѶ¼����("); if(!items[0].trim().equals("")) {
	 * query.append("�ֻ�����="+items[0]+","); } if(!items[1].trim().equals("")) {
	 * query.append("����="+items[1]+","); } query.append("����="+items[2]+")�ĺ���"); }
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
