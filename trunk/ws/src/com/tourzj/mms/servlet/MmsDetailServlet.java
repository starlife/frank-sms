package com.tourzj.mms.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tourzj.common.constant.Constants;
import com.tourzj.common.util.FileUtil;

public class MmsDetailServlet extends HttpServlet {

	private static final Log log = LogFactory.getLog(MmsDetailServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String sendid = req.getParameter("sendid");
		String str = "没有该条数据";
		if (sendid != null) {
			File f = new File(getXMLFilePath() + File.separator + sendid);
			if (f.exists()) {
				String basePath = getBasePath(req);
				// str=process(f,basePath);
				str = FileUtil.getData(f, Constants.CHARSET);
				str = process(str, basePath);
			}else
			{
				log.debug("文件"+f.getAbsoluteFile()+"不存在");
			}
		}else
		{
			log.debug("得到请求 senid为 null");
		}
		resp.setContentType("text/xml");
		PrintWriter out = resp.getWriter();
		out.print(str);
		out.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}

	public String getBasePath(HttpServletRequest request) {
		StringBuffer baseURL = new StringBuffer(128);
		baseURL.append(request.getScheme());
		baseURL.append("://");
		baseURL.append(request.getServerName());
		if (request.getServerPort() != 80) {
			baseURL.append(":");
			baseURL.append(request.getServerPort());
		}
		baseURL.append(request.getContextPath());
		return baseURL.toString();
	}

	/*public String process(File f, String basePath) {
		String str = null;
		try {
			Document doc = JDomHelper.xml2Doc(f.getAbsolutePath());
			List<Element> list = doc.getRootElement().getChildren(
					"MobileNewsInfo");
			for (int i = 0; i < list.size(); i++) {
				Element e = list.get(i);
				Element img = e.getChild("ImgFile");
				if (img != null) {
					img.setText(basePath + "/" + img.getText());
				}
			}
			str = JDomHelper.doc2String(doc, Constants.CHARSET);
		} catch (Exception ex) {
			log.error(null, ex);
		}
		return str;
	}*/

	/**
	 * 加上图片的路径
	 * 
	 * @param str
	 * @return
	 */
	public String process(String str, String basePath) {
		Pattern p = Pattern.compile("(<ImgFile>)([^<]+)(</ImgFile>)");
		Matcher m = p.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, m.group(1) + basePath + "/" + m.group(2)
					+ m.group(3));
		}
		m.appendTail(sb);
		// System.out.println(sb.toString());
		return sb.toString();
	}

	private String getXMLFilePath() {
		File dir = new File(getServletContext().getRealPath(
				File.separator + Constants.XML_FILE_DIR));
		return dir.getAbsolutePath();

	}

}
