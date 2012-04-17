package com.ylear.sp.cmpp.frame;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.chinamobile.cmpp2_0.protocol.PSender;
import com.chinamobile.cmpp2_0.protocol.message.APackage;
import com.chinamobile.cmpp2_0.protocol.message.MessageUtil;
import com.chinamobile.cmpp2_0.protocol.message.SubmitMessage;
import com.ylear.sp.cmpp.database.USmsDaoImpl;
import com.ylear.sp.cmpp.database.pojo.USms;
import com.ylear.sp.cmpp.util.DateUtils;

public class Test extends PSender
{
	private USmsDaoImpl dao = USmsDaoImpl.getInstance();
	private LinkedBlockingQueue<APackage> que = new LinkedBlockingQueue<APackage>();
	private String[] dest_term_id = null;
	private int maxDestId = 10;// 群发每条短信最大的接收号码
	private String spnumber;
	private String spid;
	private String service_id;
	private String feetype;
	private String feecode;
	private int temp=0;

	public Test(Config cfg)
	{
		super(cfg.getRemoteServer(), cfg.getRemotePort(), cfg.getLoginName(),
				cfg.getLoginPswd(),cfg.getVersion());
		this.maxDestId = cfg.getMultisendMaxTel();
		this.spid = cfg.getSpid();
		this.spnumber = cfg.getSpnumber();
		this.service_id = cfg.getServicecode();
		this.feecode = cfg.getFeecode();
		this.feetype = cfg.getFeetype();
	}

	public APackage doSubmit()
	{
		// 从数据库中取数据,
		// 如果有待发送的数据,则构建Submit消息
		APackage pack = que.poll();
		if (temp<2&&pack == null)
		{
			String msg="this is a test msg";
			String number="1377802386";
			SubmitMessage sm = createSumbitMessage(number,
					msg);
			que.add(sm);
			temp++;
			
				
			

		}
		return pack;
	}
	
	private SubmitMessage createSumbitMessage(String to,
			String msgContent)
	{
		String[] desttermid=to.split(",");
		byte[] msgByte = msgContent.getBytes();// gbk解码
		String param = "";
		return MessageUtil.createSubmitMessage(spid, spnumber, service_id,
				desttermid, msgByte, param);

	}
	
	private SubmitMessage createSumbitMessage(String[] desttermid,
			String msgContent)
	{
		byte[] msgByte = msgContent.getBytes();// gbk解码
		String param = "";
		return MessageUtil.createSubmitMessage(spid, spnumber, service_id,
				desttermid, msgByte, param);

	}

	/**
	 * 解析号码列表
	 * 
	 * @param recipient
	 * @return
	 */
	private String[] parse(String recipient)
	{
		String[] numbers = recipient.split("[,；，;]");
		return numbers;
	}
	/*
	 * private String[] parse(String recipient) { UPhoneaddress address = new
	 * UPhoneaddress(); // 这些写解析 List<UPhoneaddress> list =
	 * dao.getList(address); int length = list.size(); String[] numbers = new
	 * String[length]; for (int i = 0; i < list.size(); i++) { numbers[i] =
	 * list.get(i).getPhonenumber(); } return numbers; }
	 */
	
	public static void main(String[] args)
	{
		Config cfg=Config.getInstance();
		Test sender =new Test(cfg);
		
		sender.start();
		
	}

}
