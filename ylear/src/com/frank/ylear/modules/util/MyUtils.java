package com.frank.ylear.modules.util;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;


/**
 * 系统工具类
 * @author Memory
 * 
 */
public class MyUtils {
	


	/**
	 * 两个对象之间的(非null)属性copy
	 * @param from
	 * @param to 接收copy的对象
	 * @throws Exception
	 */
	public static void copyNotNullProperties(Object from, Object to)
			throws Exception {
		Class c1 = from.getClass();
		Class c2 = to.getClass();
		Field[] f1 = c1.getDeclaredFields();
		Field[] f2 = c2.getDeclaredFields();
		for (Field ff1 : f1) {
			ff1.setAccessible(true);
			for (Field ff2 : f2) {
				ff2.setAccessible(true);
				if (ff1.getName().equals(ff2.getName())) {
					if (ff1.get(from) != null) {
						ff2.set(to, ff1.get(from));
					}
					break;
				}
			}
		}
	}
	
}
