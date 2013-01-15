/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chinaunicom.sgip1_2.protocol.message.bean;
import java.io.UnsupportedEncodingException;
import java.util.regex.*;

import com.chinaunicom.sgip1_2.protocol.message.SubmitMessage;

/**
 *
 * @author Administrator
 */
public class Submit {
	/**
	 * sp接入号 需要配置文件提供
	 */
    private String SPNumber=""; //text(21)
    
    /**
     * 计费号码 有的话需要配置文件提供
     * 手机号码前加“86”国别标志；当且仅当群发且对用户收费时为空；
     * 如果为空，则该条短消息产生的费用由UserNumber代表的用户支付；
     * 如果为全零字符串“000000000000000000000”，表示该条短消息产生的费用由SP支付。
     */
    private String ChargeNumber="000000000000000000000";//text(21)
    /**
     * 接受手机号码个数
     */
    private int UserCount=1;//integer(1)
    /**
     * 接收手机号码
     */
    private String[] UserNumber;//n*text(21)
    
    /**
     * spid
     */
    private String CorpId="";//text(5) 企业代码
    /**
     * serviceCode
     */
    private String ServiceType="";//text(10) 业务代码
    /**
     * 计费类型
     * 0	“短消息类型”为“发送”，对“计费用户号码”不计信息费，此类话单仅用于核减SP对称的信道费
     * 1	对“计费用户号码”免费
	 * 2	对“计费用户号码”按条计信息费
	 * 3	对“计费用户号码”按包月收取信息费
	 * 4	对“计费用户号码”的收费是由SP实现
     */
    private int FeeType=1;//integer(1) 计费类型
    private String FeeValue="0";//text(6) 费用 分
    public String GivenValue="0";//text(6) 赠送话费
    /**
     * 代收费标志 0：应收；1：实收
     */
    private int AgentFlag=0;//integer(1) 
    /**
     * 引起MT消息的原因
     * 0-MO点播引起的第一条MT消息；
     * 1-MO点播引起的非第一条MT消息；
     * 2-非MO点播引起的MT消息；
     * 3-系统反馈引起的MT消息。
     */
    private int MorelatetoMTFlag=2;//integer(1) 

    private int Priority=0; //integer(1)  优先级0-9从低到高，默认为0
    private String ExpireTime="";//text(16) 短消息过期时间
    private String ScheduleTime="";//text(16) 短消息定时发送时间
    /**
     * 状态报告标记
     * 0-该条消息只有最后出错时要返回状态报告
     * 1-该条消息无论最后是否成功都要返回状态报告
     * 2-该条消息不需要返回状态报告
     * 3-该条消息仅携带包月计费信息，不下发给用户，要返回状态报告
     * 其它-保留
     * 缺省设置为0
     */
    private int ReportFlag=1;//integer(1) 无聊成功失败都返回状态报告
    private int TP_pid=0;//integer(1)
    private int TP_udhi=0;//integer(1)
    /**
     * 短消息的编码格式。
     * 	0：纯ASCII字符串
     *  3：写卡操作
     *  4：二进制编码
	 *  8：UCS2编码
	 *  15: GBK编码
	 *  其它参见GSM3.38第4节：SMS Data Coding Scheme
     */
    private int MsgCoding=15;//integer(1) 短消息编码
    private int MsgType=0; //integer(1) 信息类型  0表示短消息信息
    private int MsgLength; // integer(4) 短消息的长度
    private byte[] MsgContent;// text(MsgLength)短消息内容
    private String Reserve="";// text(8) 保留

   
    public Submit(String spid, String spnumber, String serviceCode,
			String[] desttermid, byte[] msgcontent, String param) {
    	this.CorpId=spid;
        this.SPNumber = spnumber;
        this.UserNumber = desttermid;
        this.UserCount =desttermid.length;
        this.ServiceType=serviceCode;
        this.MsgContent = msgcontent;
        this.MsgLength=msgcontent.length;
        this.setParams(param);

    }

    /**
     * 根据键值取相应数据，例如，getValue("srvcode")如果，有返回该值，否则返回空
     * @param param
     * @return
     */
    private String getValue(String param, String key) {
        String pattern = "(" + key + "[ ]*=[ ]*)(.*)(\r\n)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(param);
        if (m.find()) {
            return m.group(2);
        } else {
            return null;
        }
    }

    public String getMsgContentString(){
        String msg;
        try{
            if(this.MsgCoding==15){
                msg=new String(this.MsgContent,"GB18030");
            }
            else if(this.MsgCoding==4){
                msg=new String(this.MsgContent,"UTF-16BE");
            }
            else if(this.MsgCoding==8){
                msg=new String(this.MsgContent,"UTF-16BE");
            }
            else if(this.MsgCoding==0){
                msg=new String(this.MsgContent,"iso-8859-1");
            }
            else{
                msg=new String(this.MsgContent,"UTF-16BE");
            }
        }catch(UnsupportedEncodingException ex){
            msg=new String(this.MsgContent);
        }
        return msg;
    }
    
    public byte[] getMsgContent(){
        return this.MsgContent;
    }

    public void setParams(String param) {
        String value = "";
        if ((value = getValue(param, "SPNumber")) != null) {
            SPNumber = value;
        }
        if ((value = getValue(param, "ChargeNumber")) != null) {
            ChargeNumber = value;
        }
        if ((value = getValue(param, "FeeType")) != null) {
            FeeType = Integer.parseInt(value);
        }
        if ((value = getValue(param, "FeeValue")) != null) {
            FeeValue = value;
        }
        if ((value = getValue(param, "GivenValue")) != null) {
            GivenValue = value;
        }
        if ((value = getValue(param, "AgentFlag")) != null) {
            AgentFlag =  Integer.parseInt(value);
        }
        if ((value = getValue(param, "MorelatetoMTFlag")) != null) {
            MorelatetoMTFlag =  Integer.parseInt(value);
        }
        if ((value = getValue(param, "ScheduleTime")) != null) {
            this.ScheduleTime=value;
        }
        if ((value = getValue(param, "TP_udhi")) != null) {
            try {
                TP_udhi = Integer.parseInt(value);
            } catch (Exception e) {
            }
        }
        if ((value = getValue(param, "MsgCoding")) != null) {
            try {
                MsgCoding = Integer.parseInt(value);
            } catch (Exception e) {
            }
        }
       
        if ((value = getValue(param, "ServiceType")) != null) {
            ServiceType = value;
        }
        

    }

    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append(" SPNumber-byte       : " + this.SPNumber + "\r\n");
        sb.append(" ChargeNumber-byte   : " + this.ChargeNumber + "\r\n");
        sb.append(" UserCount-char      : " + this.UserCount + "\r\n");
        sb.append(" UserNumber-byte     : " + this.UserNumber + "\r\n");
        sb.append(" CorpId-byte         : " + this.CorpId + "\r\n");
        sb.append(" ServiceType-char    : " + this.ServiceType + "\r\n");
        sb.append(" FeeType-char        : " + this.FeeType + "\r\n");
        sb.append(" FeeValue-byte       : " + this.FeeValue + "\r\n");
        sb.append(" GivenValue-byte     : " + this.GivenValue + "\r\n");
        sb.append(" AgentFlag-char      : " + this.AgentFlag + "\r\n");
        sb.append(" MorelatetoMTFlag-char: " + this.MorelatetoMTFlag + "\r\n");
        sb.append(" Priority-char       : " + this.Priority + "\r\n");
        sb.append(" ExpireTime-byte     : " + this.ExpireTime + "\r\n");
        sb.append(" ScheduleTime-byte   : " + this.ScheduleTime + "\r\n");
        sb.append(" ReportFlag-byte     : " + this.ReportFlag + "\r\n");
        sb.append(" TP_pid-char         : " + this.TP_pid + "\r\n");
        sb.append(" TP_udhi-char        : " + this.TP_udhi + "\r\n");
        sb.append(" MsgCoding-char      : " + this.MsgCoding + "\r\n");
        sb.append(" MsgType-char        : " + this.MsgType + "\r\n");
        sb.append(" MsgLength-char      : " + this.MsgLength + "\r\n");
        sb.append(" MsgContent-byte     : " + this.MsgContent + "\r\n");
        sb.append(" Reserve-byte        : " + this.Reserve + "\r\n");
        
        return sb.toString();
    }

    /*public static SubmitMessage getSingleSubmit(String to,byte[] msg,String param){
        Submit submit = new Submit("spnumber",to,msg,param);
        submit.CorpId = "spid";
        SubmitMessage sm=new SubmitMessage(submit);
        return sm;
    }*/

    public static void main(String[] args){

    }

	public String getSPNumber()
	{
		return SPNumber;
	}

	public void setSPNumber(String number)
	{
		SPNumber = number;
	}

	public String getChargeNumber()
	{
		return ChargeNumber;
	}

	public void setChargeNumber(String chargeNumber)
	{
		ChargeNumber = chargeNumber;
	}

	public int getUserCount()
	{
		return UserCount;
	}

	public void setUserCount(int userCount)
	{
		UserCount = userCount;
	}

	public String[] getUserNumber()
	{
		return UserNumber;
	}

	public void setUserNumber(String[] userNumber)
	{
		UserNumber = userNumber;
	}

	public String getCorpId()
	{
		return CorpId;
	}

	public void setCorpId(String corpId)
	{
		CorpId = corpId;
	}

	public String getServiceType()
	{
		return ServiceType;
	}

	public void setServiceType(String serviceType)
	{
		ServiceType = serviceType;
	}

	public int getFeeType()
	{
		return FeeType;
	}

	public void setFeeType(int feeType)
	{
		FeeType = feeType;
	}

	public String getFeeValue()
	{
		return FeeValue;
	}

	public void setFeeValue(String feeValue)
	{
		FeeValue = feeValue;
	}

	public String getGivenValue()
	{
		return GivenValue;
	}

	public void setGivenValue(String givenValue)
	{
		GivenValue = givenValue;
	}

	public int getAgentFlag()
	{
		return AgentFlag;
	}

	public void setAgentFlag(int agentFlag)
	{
		AgentFlag = agentFlag;
	}

	public int getMorelatetoMTFlag()
	{
		return MorelatetoMTFlag;
	}

	public void setMorelatetoMTFlag(int morelatetoMTFlag)
	{
		MorelatetoMTFlag = morelatetoMTFlag;
	}

	public int getPriority()
	{
		return Priority;
	}

	public void setPriority(int priority)
	{
		Priority = priority;
	}

	public String getExpireTime()
	{
		return ExpireTime;
	}

	public void setExpireTime(String expireTime)
	{
		ExpireTime = expireTime;
	}

	public String getScheduleTime()
	{
		return ScheduleTime;
	}

	public void setScheduleTime(String scheduleTime)
	{
		ScheduleTime = scheduleTime;
	}

	public int getReportFlag()
	{
		return ReportFlag;
	}

	public void setReportFlag(int reportFlag)
	{
		ReportFlag = reportFlag;
	}

	public int getTP_pid()
	{
		return TP_pid;
	}

	public void setTP_pid(int tp_pid)
	{
		TP_pid = tp_pid;
	}

	public int getTP_udhi()
	{
		return TP_udhi;
	}

	public void setTP_udhi(int tp_udhi)
	{
		TP_udhi = tp_udhi;
	}

	public int getMsgCoding()
	{
		return MsgCoding;
	}

	public void setMsgCoding(int msgCoding)
	{
		MsgCoding = msgCoding;
	}

	public int getMsgType()
	{
		return MsgType;
	}

	public void setMsgType(int msgType)
	{
		MsgType = msgType;
	}

	public int getMsgLength()
	{
		return MsgLength;
	}

	public void setMsgLength(int msgLength)
	{
		MsgLength = msgLength;
	}

	public String getReserve()
	{
		return Reserve;
	}

	public void setReserve(String reserve)
	{
		Reserve = reserve;
	}

	public void setMsgContent(byte[] msgContent)
	{
		MsgContent = msgContent;
	}

}
