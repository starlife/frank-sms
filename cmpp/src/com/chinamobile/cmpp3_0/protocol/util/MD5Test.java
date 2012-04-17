package com.chinamobile.cmpp3_0.protocol.util;

import junit.framework.Assert;
import junit.framework.TestCase;

public class MD5Test extends TestCase {

	public void testCompile(){
		String input="abc";
		String actual=MD5.compile(input);
		String expected = "900150983cd24fb0d6963f7d28e17f72";
		Assert.assertEquals(actual,expected);
	}


}
