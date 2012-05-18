package com.frank.ylear.modules.mms.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Smil
{
	private final Log log = LogFactory.getLog(Smil.class);
	// smil�ļ����·��
	private String filepath = "";
	// smil�ļ�����
	private String filename = "";
	// smil�ļ�����
	private String content = "";

	// �����ļ����·��
	public void setSmilPath(String path)
	{
		this.filepath = path;
		// ���Ŀ¼�����ڣ��򴴽�
		File rootDir = new File(this.filepath);
		if (!rootDir.isDirectory())
		{
			rootDir.mkdirs();
		}
	}

	public String getSmilPath()
	{
		return this.filepath;
	}

	// ����smil�ļ�����
	public void setSmilName(String name)
	{
		this.filename = name;
	}

	public String getSmilName()
	{
		return this.filename;
	}

	// ����smil�ļ�
	public void resetSmil()
	{
		this.content = "";
	}

	// Ϊsmil�ļ�����ͷ����Ϣ
	public void smilAddHead()
	{
		this.content = "<smil xmlns=\"http://www.w3.org/2001/SMIL20/CR/Language\">";
		this.content += "\n  <head>";
		this.content += "\n    <layout>";
		this.content += "\n      <root-layout width=\"320px\" height=\"480px\"/>";
		// this.filecontent+="\n <root-layout height=\"100%\" width=\"100%\"/>";
		// this.filecontent+="\n <region id=\"Text\" height=\"100%\"
		// width=\"100%\" left=\"0\" top=\"0\" fit=\"scroll\"/>";
		// this.filecontent+="\n <region id=\"Image\" height=\"100%\"
		// width=\"100%\" left=\"0\" top=\"0\"/>";
		this.content += "\n    </layout>";
		this.content += "\n  </head>";
		this.content += "\n  <body>";
	}

	// ���ò��ſ�ʼ��Ǽ����Ų���ʱ��
	public void setSmilParStart(int time)
	{
		this.content += "\n    <par dur=\"" + time + "s\">";
	}

	// ���ò��Ž�β���
	public void setSmilParEnd()
	{
		this.content += "\n    </par>";
	}

	// ���ò���Ƭ�Ͽ�ʼ���
	public void setSmilSeqStart()
	{
		this.content += "\n      <seq>";
	}

	// ���ò���Ƭ�Ͻ������
	public void setSmilSeqEnd()
	{
		this.content += "\n      </seq>";
	}

	// ���ò�������
	public void smilAddAudio(String audio)
	{
		this.content += "\n      <audio src=\"" + audio + "\"/>";
	}

	// ���ļ������������
	public void smilAddText(String text)
	{
		this.content += "\n      <text src=\"" + text + "\"/>";
	}

	// //���ļ����ͼƬ
	public void smilAddImg(String img)
	{
		this.content += "\n      <img src=\"" + img + "\"/>";
	}

	// ���ļ����β����Ϣ
	public void smilAddFoot()
	{
		this.content += "\n  </body>";
		this.content += "\n</smil>";
	}

	// ���ɲ���smil�ļ�
	public void generateSmil()
	{
		FileWriter fw;
		try
		{
			fw = new FileWriter(this.filepath + this.filename + ".smil");
			fw.write(this.content, 0, this.content.length());
			fw.flush();
			fw.close();
		}
		catch (IOException e)
		{
			log.error(null, e);
		}
	}

	// ��ȡ�����ļ�����
	public String getSmil()
	{
		return this.content;
	}

	public static void main(String[] args)
	{
		/*
		 * String path="D:/"; String name="test1"; //�������� Smil sml=new Smil();
		 * //�����ļ����·�� sml.setSmilPath(path); //�����ļ��� sml.setSmilName(name);
		 * //�����ļ����� sml.resetSmil();//�����ļ����� sml.smilAddHead();//������ļ�ͷ����Ϣ��
		 * sml.setSmilParStart(60);//���ſ�ʼ��� sml.smilAddImg("1.gif");
		 * sml.smilAddText("1.txt"); sml.smilAddAudio("1.wav");//���������ļ�
		 * sml.setSmilParEnd();//���Ž������ sml.setSmilParStart(30);//���ſ�ʼ���
		 * sml.smilAddImg("2.gif"); sml.smilAddText("2.txt");
		 * sml.smilAddAudio("2.wav");//���������ļ� sml.setSmilParEnd();//���Ž������
		 * sml.smilAddFoot();//�����ļ�β����Ϣ //���ɲ����ļ� sml.generateSmil(); //��ȡ�����ļ�
		 * String str=sml.getFileContent(); System.out.println(str);
		 */
	}

}
