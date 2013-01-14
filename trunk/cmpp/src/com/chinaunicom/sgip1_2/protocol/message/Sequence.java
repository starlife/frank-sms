package com.chinaunicom.sgip1_2.protocol.message;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.chinamobile.cmpp2_0.protocol.util.ByteConvert;

public class Sequence
{
	private static SimpleDateFormat SDF = new SimpleDateFormat("MMddHHmmss");
	private static int allocSequenceId = 0;
	private String nodeid;
	private String timestamp;
	private String seq;

	public Sequence(String nodeid)
	{
		this.nodeid = nodeid;
		this.timestamp = SDF.format(new Date());
		this.seq = String.valueOf(allocSequenceID());
	}

	public Sequence(byte[] buf)
	{
		this(buf, 0);
	}

	public Sequence(byte[] buf, int loc)
	{
		// copy nodeid 适配大于0x7fffffff
		byte[] temp = new byte[5];
		System.arraycopy(buf, loc + 0, temp, 1, 4);
		this.nodeid = String.valueOf(new BigInteger(temp).longValue());
		// copy timestamp 适配MMddHHmmss 对于1到9月 加上0
		timestamp = String.valueOf(ByteConvert.byte2int(buf, loc + 4));
		if (timestamp.length() < 10)
		{
			this.timestamp = "0" + timestamp;
		}
		// copy seq 适配大于0x7fffffff
		temp = new byte[5];
		System.arraycopy(buf, loc + 8, temp, 1, 4);
		this.seq = String.valueOf(new BigInteger(temp).longValue());
	}

	/**
	 * 取得Sequence的byte形式
	 * 
	 * @return
	 */
	public byte[] getBytes()
	{
		byte[] buf = new byte[12];
		// 源节点编号 适配大于0x7fffffff
		System.arraycopy(ByteConvert
				.int2byte(new BigInteger(nodeid).intValue()), 0, buf, 0, 4);
		System.arraycopy(ByteConvert.int2byte(Integer.valueOf(timestamp)), 0,
				buf, 4, 4);
		// 流水号
		System.arraycopy(ByteConvert.int2byte(new BigInteger(seq).intValue()),
				0, buf, 8, 4);
		return buf;
	}

	/**
	 * 分配流水号
	 * 
	 * @return
	 */
	public static synchronized int allocSequenceID()
	{
		allocSequenceId = (allocSequenceId + 1) & 0x7fffffff;
		return allocSequenceId;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(nodeid).append("|").append(timestamp).append("|").append(seq);
		return sb.toString();
	}

	public String getNodeid()
	{
		return nodeid;
	}

	public void setNodeid(String nodeid)
	{
		this.nodeid = nodeid;
	}

	public String getTimestamp()
	{
		return timestamp;
	}

	public String getSeq()
	{
		return seq;
	}

	public void setSeq(String seq)
	{
		this.seq = seq;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	public static void main(String[] args)
	{
		Sequence seq = new Sequence("3053141211");
		seq.setSeq("3053141211");
		System.out.println(seq);
		Sequence seq1 = new Sequence(seq.getBytes());
		System.out.println(seq1);

	}

}
