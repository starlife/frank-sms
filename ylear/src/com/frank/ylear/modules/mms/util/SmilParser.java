package com.frank.ylear.modules.mms.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.frank.ylear.common.constant.Constants;
	
/**
 * 根据Smil文件的内容解析各个元素
 * @author Administrator
 *
 */
public class SmilParser
{
	
	private String filecontent="";
	
	private int framecount=0;
	
	private List<Frame> frames=new ArrayList<Frame>();
	
	public class Frame
	{
		public int framenumber;
		public int dur;
		public String imagesrc;
		public String audiosrc;
		public String txtsrc;
		
		public String toString()
		{
			StringBuffer sb=new StringBuffer();
			sb.append("(framenumber="+framenumber);
			sb.append(",dur="+dur);
			sb.append(",imagesrc="+imagesrc);
			sb.append(",audiosrc="+audiosrc);
			sb.append(",txtsrc="+txtsrc+")");
			return sb.toString();
		}
	}
	
	public SmilParser()
	{
		
	}
	
	public SmilParser(String smil)
	{
		this.filecontent=smil;
	}
	
	public String getFilecontent()
	{
		return filecontent;
	}

	public void setFilecontent(String filecontent)
	{
		this.filecontent = filecontent;
	}
	
	public void setFilecontent(File f) throws IOException
	{
		FileInputStream input=new FileInputStream(f);
		BufferedReader reader=null;
		StringBuffer sb=new StringBuffer();
		try
		{
			reader=new BufferedReader(new InputStreamReader(input));
			String str;
			while((str=reader.readLine())!=null)
			{
				sb.append(str+Constants.NEWLINE);
			}
		}catch(IOException ex)
		{
			throw ex;
		}finally
		{
			if(reader!=null)
			{
				reader.close();
			}
		}
		this.setFilecontent(sb.toString());
	}

	public int getFramecount()
	{
		return framecount;
	}

	public List<Frame> getFrames()
	{
		return frames;
	}

	
	
	public void parse()
	{
		Document doc=null;
		try
		{
			doc = DocumentHelper.parseText(this.filecontent); // 
			Element root=doc.getRootElement();//获取根节点
			
			Element body=root.element("body");
			Iterator itpars=body.elementIterator("par");
			while(itpars.hasNext())
			{
				framecount++;
				Frame fr=new Frame();
				fr.framenumber=framecount;
				Element par=(Element)itpars.next();
				Attribute dur=par.attribute("dur");
				fr.dur=parseDuring(dur.getStringValue());
				Element img=par.element("img");
				if(img!=null)
				{
					fr.imagesrc=img.attribute("src").getStringValue();
				}	
				Element audio=par.element("audio");
				if(audio!=null)
				{
					fr.audiosrc=audio.attribute("src").getStringValue();
				}
				Element txt=par.element("text");
				if(txt!=null)
				{
					fr.txtsrc=txt.attribute("src").getStringValue();
				}
				frames.add(fr);
			}
		}catch(Exception ex)
		{
			System.out.println("文档不符合标准，解析失败");
		}
	}
	
	public static int parseDuring(String during)
	{
		int dur=0;
		try
		{
			int index=during.length();
			for(int i=0;i<during.length();i++)
			{
				char c=during.charAt(i);
				if(!(c>='0'&&c<='9'))
				{
					index=i;
					break;
				}
			}
			if(index!=during.length())
			{
				during=during.substring(0,index);
			}
			dur=Integer.parseInt(during);
			
		}catch(Exception ex)
		{
			
		}
		return dur;
	}
	
	public static void main(String[] args) throws IOException
	{
		/*ParseSmil parse=new ParseSmil();
		parse.setFilecontent(new File("D:/test1.smil"));
		parse.parse();
		System.out.println(parse.frames);*/
		System.out.println(parseDuring("1300s"));
		
	}
	
	
	
	

}
