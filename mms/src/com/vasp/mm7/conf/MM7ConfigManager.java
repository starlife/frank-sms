/**
 * File Name:MM7ConfigManager.java 中国移动
 */

package com.vasp.mm7.conf;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;

public class MM7ConfigManager
{
	private static final Log log = LogFactory.getLog(MM7ConfigManager.class);
	public Map<String,String> map = new HashMap<String,String>();

	public MM7ConfigManager()
	{
	}

	/** read XML file through the parameter and get value */
	public void load(String configFileName)
	{
		
		readXMLFile(configFileName);
		
	}

	/** read XML File and parser it */
	private void readXMLFile(String inFile)
	{
		Document doc;
		map.clear();
		try
		{
			DOMBuilder domb = new DOMBuilder();
			doc = domb.build(new FileInputStream(inFile));
			// SAXBuilder saxb = new SAXBuilder();
			// doc = saxb.build(new FileInputStream(inFile));
			Element root = doc.getRootElement();
			List children = root.getChildren();
			int size = root.getChildren().size();
			for (int i = 0; i < size; i++)
			{
				Element element = (Element) root.getChildren().get(i);			
				map.put(element.getName(), element.getTextTrim());
				
			}
		}
		catch (Exception ex)
		{
			log.error("解析配置文件时遇到错误",ex);
			System.exit(1);
		}
		
		
	}
}
