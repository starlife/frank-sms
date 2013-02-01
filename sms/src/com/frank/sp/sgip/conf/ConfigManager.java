package com.frank.sp.sgip.conf;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;

/**
 * 读取xml类型的配置文件
 * 
 * @author linxinzheng
 */
public class ConfigManager
{
	private static final Log log = LogFactory.getLog(ConfigManager.class);
	public Map<String, String> map = new HashMap<String, String>();

	public ConfigManager()
	{
	}

	public void load(String configFileName)
	{

		readXMLFile(configFileName);

	}

	/**
	 * @param inFile
	 */
	private void readXMLFile(String inFile)
	{
		Document doc;
		map.clear();
		try
		{
			DOMBuilder domb = new DOMBuilder();
			doc = domb.build(new FileInputStream(inFile));
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
			log.error("解析配置文件时遇到错误", ex);
		}

	}
}
