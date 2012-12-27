package com.unicom.mm7.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

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
    public static void doc2XML(Document doc, String filePath) throws Exception{ 
    	String indent="    ";//ÿһ��Ԫ�������ĸ�
    	boolean newlines=true;//��Ԫ�غ���
    	String encoding="UTF-8";
        XMLOutputter outputter = new XMLOutputter(indent,newlines,encoding);//������� ,��Ԫ�غ��У�ÿһ��Ԫ�������ĸ�   
        FileWriter writer = new FileWriter(filePath);//�����  
        outputter.output(doc, writer);  
        writer.close();  
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
    public static String doc2String(Document doc) throws Exception {  
    	String indent="    ";//ÿһ��Ԫ�������ĸ�
    	boolean newlines=true;//��Ԫ�غ���
    	String encoding="UTF-8";
        XMLOutputter outputter = new XMLOutputter(indent,newlines,encoding);//������� ,��Ԫ�غ��У�ÿһ��Ԫ�������ĸ�   
        ByteArrayOutputStream bo = new ByteArrayOutputStream();  
        outputter.output(doc, bo);  
        return bo.toString();  
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
            Document doc = xml2Doc("test.xml");  
            System.out.println(doc);  
            System.out.println(doc2String(doc));  
            doc = string2Doc(doc2String(doc));  
            doc2XML(doc, "1.xml");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
          
    }  

}
