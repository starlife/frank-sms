package com.chinamobile.cmpp2_0.protocol.util;

/**
 * 本长短信协议头采用6字节格式05 00 03 XX MM NN
 * 
 * @author Administrator
 */
public class SmsUtil
{
	public static final int SMS_LENGTH = 140;
	public static final int SMS_HEADER_LENGTH = 6;
	public static final byte[] tp_udhiHead = new byte[SMS_HEADER_LENGTH];

	private static byte allocTpUdhiHead3 = 0;

	static
	{
		tp_udhiHead[0] = 0x05;// 表示后面还有五个字节
		tp_udhiHead[1] = 0x00;// 固定为0 在GSM 03.40规范9.2.3.24.1中规定
		tp_udhiHead[2] = 0x03;// 表示后面还有三个字节
		// tp_udhiHead[3] =0x0A;
		// tp_udhiHead[4] = (byte)msgCount;
		// tp_udhiHead[5] = (byte)(i+1);
	}
	
	/**
	 * 把一条短信分割成多条短信发送，传进来的参数应该是ucs2编码的字节数组
	 * @param msgByte 
	 * @return
	 */
	public static byte[][] getLongSmsByte(byte[] msgByte)
	{

		byte[][] msgArray = null;
		/*if (msgByte.length <= 140)
		{
			msgArray = new byte[1][];
			return msgArray;
		}*/
		int smsBodyLen = SMS_LENGTH - SMS_HEADER_LENGTH;
		int msgCount = msgByte.length / smsBodyLen + 1;// 得到短信条数
		msgArray = new byte[msgCount][];
		tp_udhiHead[3] = getTpUdhiHead3();// 得掉tp_udhiHead byte4的值
		for (int i = 0; i < msgCount; i++)
		{
			tp_udhiHead[4] = (byte) msgCount;
			tp_udhiHead[5] = (byte) (i + 1);
			int offset = i * smsBodyLen;
			int length = Math.min(msgByte.length - offset, smsBodyLen);// 本条短信长度
			byte[] dest = new byte[SMS_HEADER_LENGTH + length];
			System.arraycopy(tp_udhiHead, 0, dest, 0, SMS_HEADER_LENGTH);// copy
																			// header
			System.arraycopy(msgByte, offset, dest, SMS_HEADER_LENGTH,
					dest.length);// copy body
			msgArray[i] = dest;
		}
		return msgArray;
	}

	/**
	 * 得到tp_udhi Head中的byte4字节
	 * 
	 * @return
	 */
	private static byte getTpUdhiHead3()
	{
		allocTpUdhiHead3 = (byte) ((allocTpUdhiHead3 + 1) & 0xff);
		return allocTpUdhiHead3;
	}

}
