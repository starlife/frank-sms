package com.chinaunicom.sgip1_2.protocol.message;

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
		this.nodeid = String.valueOf(ByteConvert.byte2int(buf, loc + 0));
		setTimestamp(String.valueOf(ByteConvert.byte2int(buf, loc + 4)));
		this.seq = String.valueOf(ByteConvert.byte2int(buf, loc + 8));
	}

	private void setTimestamp(String timestamp)
	{
		StringBuffer sb = new StringBuffer();
		if (timestamp.length() < 10)
		{
			sb.append("0");
		}
		sb.append(timestamp);
		this.timestamp = sb.toString();
	}

	/**
	 * 分配流水号
	 * 
	 * @return
	 */
	public static synchronized int allocSequenceID()
	{
		allocSequenceId = (allocSequenceId + 1) & 0xffffffff;
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
}
