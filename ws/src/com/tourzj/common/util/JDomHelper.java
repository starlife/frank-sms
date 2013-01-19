package com.tourzj.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.Charset;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.tourzj.mms.manager.MmsDecode;
import com.tourzj.mms.manager.MmsManager;
import com.unicom.mm7.bean.UMms;

/***
 * JDOM ��ʽ����XML 
 * @author Administrator
 *
 */
public class JDomHelper
{
	/** 
     * DOCUMENT��ʽ���������ΪXML 
     *  
     * @param doc JDOM��Document 
     * @param filePath ����ļ�·�� 
     * @throws Exception 
     */  
    public static void doc2XML(Document doc, String filePath,String charset) throws Exception{ 
    	String indent="    ";//ÿһ��Ԫ�������ĸ�
    	boolean newlines=true;//��Ԫ�غ���
    	String encoding=charset;
        XMLOutputter outputter = new XMLOutputter(indent,newlines,encoding);//������� ,��Ԫ�غ��У�ÿһ��Ԫ�������ĸ�   
        FileOutputStream output=new FileOutputStream(filePath);
        //OutputStreamWriter writer=new OutputStreamWriter(output,Charset.forName(charset));
        //FileWriter writer = new FileWriter(filePath);//�����  
        outputter.output(doc, output);
        output.close();  
    }  
      
    /** 
     * �ַ���ת��ΪDOCUMENT 
     *  
     * @param xmlStr �ַ��� 
     * @return doc JDOM��Document 
     * @throws Exception 
     */  
    public static Document string2Doc(String xmlStr) throws Exception {  
        java.io.Reader in = new StringReader(xmlStr);  
        Document doc = (new SAXBuilder()).build(in);         
        return doc;  
    }  
  
    /** 
     * Documentת��Ϊ�ַ��� 
     *  
     * @param xmlFilePath XML�ļ�·�� 
     * @return xmlStr �ַ��� 
     * @throws Exception 
     */  
    public static String doc2String(Document doc,String charset) throws Exception {  
    	String indent="    ";//ÿһ��Ԫ�������ĸ�
    	boolean newlines=true;//��Ԫ�غ���
    	String encoding=charset;
        XMLOutputter outputter = new XMLOutputter(indent,newlines,encoding);//������� ,��Ԫ�غ��У�ÿһ��Ԫ�������ĸ�   
        ByteArrayOutputStream bo = new ByteArrayOutputStream();  
        outputter.output(doc, bo);  
        return bo.toString(encoding);
    }  
  
    /** 
     * XMLת��ΪDocument 
     *  
     * @param xmlFilePath XML�ļ�·�� 
     * @return doc Document���� 
     * @throws Exception 
     */  
    public static Document xml2Doc(String xmlFilePath) throws Exception {  
        File file = new File(xmlFilePath);  
        return (new SAXBuilder()).build(file);  
    }  
      
    public static void main(String[] args) {  
        try{  
        	String sendId ="34c53ce1-5656-4e78-b16d-93ffe84e9445";
    		UMms mms=MmsManager.getMms(sendId);
            Document doc = MmsDecode.decodeMms(mms, "E:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/ws/uploadFile");
            //System.out.println(doc);  
            //System.out.println(doc2String(doc));  
            //doc = string2Doc(doc2String(doc));  
            doc2XML(doc, "F:/test.xml","UTF-8");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
          
    }  

}
