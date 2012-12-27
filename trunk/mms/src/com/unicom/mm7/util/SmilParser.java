package com.unicom.mm7.util;

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

	
/**
 * 根据Smil文件的内容解析各个元素
 * @author Administrator
 *
 */
public class SmilParser
{
	
	private String smil="";
	
	//private int framecount=0;
	
	private List<Frame> frames=new ArrayList<Frame>();
	
	public class Frame
	{
		private int framenumber;
		private int dur;
		private String imagesrc;
		private String audiosrc;
		private String txtsrc;
		
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

		public int getFramenumber()
		{
			return framenumber;
		}

		public void setFramenumber(int framenumber)
		{
			this.framenumber = framenumber;
		}

		public int getDur()
		{
			return dur;
		}

		public void setDur(int dur)
		{
			this.dur = dur;
		}

		public String getImagesrc()
		{
			return imagesrc;
		}

		public void setImagesrc(String imagesrc)
		{
			this.imagesrc = imagesrc;
		}

		public String getAudiosrc()
		{
			return audiosrc;
		}

		public void setAudiosrc(String audiosrc)
		{
			this.audiosrc = audiosrc;
		}

		public String getTxtsrc()
		{
			return txtsrc;
		}

		public void setTxtsrc(String txtsrc)
		{
			this.txtsrc = txtsrc;
		}
	}
	
	public SmilParser()
	{
		
	}
	
	public SmilParser(String smil)
	{
		this.smil=smil;
	}
	
	public String getSmil()
	{
		return smil;
	}

	public void setSmil(String smil)
	{
		this.smil = smil;
	}
	
	public void setSmil(File f) throws IOException
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
		this.setSmil(sb.toString());
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
			int framecount=0;
			doc = DocumentHelper.parseText(this.smil); // 
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
		String smil ="<smil><head><layout><root-layout width=\"320px\" height=\"480px\"/><region id=\"Text\" left=\"0\" top=\"320\" width=\"320px\" height=\"160px\" fit=\"meet\"/></layout></head><body><par dur=\"5000ms\"><text src=\"text_0.txt\" region=\"Text\"/></par></body></smil>";
		/*ParseSmil parse=new ParseSmil();
		parse.setFilecontent(new File("D:/test1.smil"));
		parse.parse();
		System.out.println(parse.frames);*/
		SmilParser parser = new SmilParser(smil);
		parser.parse();
		
		
		
	}
	
	
	
	

}
