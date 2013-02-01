package com.unicom.mm7.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/*******************************************************************************
 * JDOM 方式操作XML
 * 
 * @author Administrator
 */
public class JDomHelper
{
	/**
	 * DOCUMENT格式化输出保存为XML
	 * 
	 * @param doc
	 *            JDOM的Document
	 * @param filePath
	 *            输出文件路径
	 * @throws Exception
	 */
	public static void doc2XML(Document doc, String filePath) throws Exception
	{
		String indent = "    ";// 每一层元素缩排四格
		boolean newlines = true;// 在元素后换行
		String encoding = "UTF-8";
		XMLOutputter outputter = new XMLOutputter(indent, newlines, encoding);// 定义输出
																				// ,在元素后换行，每一层元素缩排四格
		FileWriter writer = new FileWriter(filePath);// 输出流
		outputter.output(doc, writer);
		writer.close();
	}

	/**
	 * 字符串转换为DOCUMENT
	 * 
	 * @param xmlStr
	 *            字符串
	 * @return doc JDOM的Document
	 * @throws Exception
	 */
	public static Document string2Doc(String xmlStr) throws Exception
	{
		java.io.Reader in = new StringReader(xmlStr);
		Document doc = (new SAXBuilder()).build(in);
		return doc;
	}

	/**
	 * Document转换为字符串
	 * 
	 * @param xmlFilePath
	 *            XML文件路径
	 * @return xmlStr 字符串
	 * @throws Exception
	 */
	public static String doc2String(Document doc) throws Exception
	{
		String indent = "    ";// 每一层元素缩排四格
		boolean newlines = true;// 在元素后换行
		String encoding = "UTF-8";
		XMLOutputter outputter = new XMLOutputter(indent, newlines, encoding);// 定义输出
																				// ,在元素后换行，每一层元素缩排四格
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		outputter.output(doc, bo);
		return bo.toString();
	}

	/**
	 * XML转换为Document
	 * 
	 * @param xmlFilePath
	 *            XML文件路径
	 * @return doc Document对象
	 * @throws Exception
	 */
	public static Document xml2Doc(String xmlFilePath) throws Exception
	{
		File file = new File(xmlFilePath);
		return (new SAXBuilder()).build(file);
	}

	public static void main(String[] args)
	{
		try
		{
			Document doc = xml2Doc("test.xml");
			System.out.println(doc);
			System.out.println(doc2String(doc));
			doc = string2Doc(doc2String(doc));
			doc2XML(doc, "1.xml");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
