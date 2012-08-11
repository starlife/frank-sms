package com.vasp.mm7.dao.buffer;

import java.util.Iterator;

public interface buffer<E>
{
	public void add(Iterator<E> it);
	
	public void add(E bean);
	
	public void commit();
}
