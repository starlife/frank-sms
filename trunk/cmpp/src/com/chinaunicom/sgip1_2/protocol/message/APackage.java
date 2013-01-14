package com.chinaunicom.sgip1_2.protocol.message;

/**
 * 消息基类 对于任何消息都需要有一个head字段(包含他的消息长度，消息类型 流水号) ；还有buf字段
 * 保存消息的二进制数；重载toString()方法，便于记录打印日志； 对于任何的发送消息 都需要有个getBytes()函数发送数据 有发送时间
 * （submit有发送次数） 对于任何的接收消息 都需要有Message(BasePackage)构造函数 有接收时间
 * 
 * @author linxinzheng
 */
public abstract class APackage
{
	// Header head;//保存消息头
	// byte[] buf;//保存消息的二进制数据

	private long timestamp;// 包的发送或者接收时间

	private int times;// 包的发送次数

	public abstract Header getHead();

	public abstract byte[] getBytes();

	/**
	 * 设置包的发送时间或接收时间
	 */
	public void setTimeStamp()
	{
		this.timestamp = System.currentTimeMillis();
	}

	/**
	 * 设置包的发送时间或接收时间
	 */
	public void setTimeStamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	/**
	 * 包的发送时间或接收时间
	 * 
	 * @return
	 */
	public long getTimeStamp()
	{
		return this.timestamp;
	}

	/**
	 * @return
	 */
	public int getTryTimes()
	{
		return this.times;
	}

	/**
	 * 发送次数加1
	 */
	public void addTimes()
	{
		this.times++;
	}
}
