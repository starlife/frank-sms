package com.chinamobile.cmpp2_0.protocol.message;

/**
 * 从sp端发出的包都要实现Send接口，实现该接口的类必须要有字段buf
 * 
 * @author linxinzheng
 */
public interface Send
{
	public byte[] getBytes();

}
