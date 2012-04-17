package com.chinamobile.cmpp2_0.protocol.message;

/**
 * sp接受到的数据包必须要实现Recv接口，需要实现一个构造函数 <? extends AMessage>(BasePackage)
 * 
 * @author linxinzheng
 */
public interface Recv
{
	public String toString();
}
