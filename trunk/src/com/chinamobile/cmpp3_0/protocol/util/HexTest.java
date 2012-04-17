package com.chinamobile.cmpp3_0.protocol.util;

import junit.framework.Assert;
import junit.framework.TestCase;

public class HexTest extends TestCase {

	public void testRstr() {
		byte[] actual=Hex.rstr("61");
		byte[] expected={(byte)97};
		Assert.assertEquals(expected[0],actual[0]);

	}

	public void testRhex() {
		byte[] b={(byte)97,(byte)8,(byte)98};
		String actual=Hex.rhex(b);
		String expected="610862";
		Assert.assertEquals(expected,actual);
	}

}
