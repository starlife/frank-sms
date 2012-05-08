package com.frank.ylear.common.taglib;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.frank.ylear.common.model.PageBean;

public class PaginationTag extends TagSupport
{
	private String form = null;

	public int doEndTag()
	{
		try
		{
			PageBean page = (PageBean) pageContext.getRequest().getAttribute(
					"page");
			if (page == null)
			{
				page = (PageBean) pageContext.getSession().getAttribute("page");
			}
			if (page != null)
			{
				StringBuffer sb = new StringBuffer();
				sb
						.append(
								"<div style=\"text-align:right;padding:6px 6px 0 0;\">\r\n")
						.append("��" + page.getTotalCount() + "����¼&nbsp;\r\n")
						.append(
								"ÿҳ��ʾ<input name=\"page.pageSize\" value=\""
										+ page.getPageSize()
										+ "\" size=\"3\" />��&nbsp;\r\n")
						.append(
								"��<input name=\"page.currentPage\" value=\""
										+ page.getCurrentPage()
										+ "\" size=\"3\" />ҳ")
						.append(" / ��" + page.getTotalPage() + "ҳ \r\n")
						.append(
								"<a href=\"javascript:page_first();\">��һҳ</a> \r\n")
						.append(
								"<a href=\"javascript:page_pre();\">��һҳ</a>\r\n")
						.append(
								"<a href=\"javascript:page_next();\">��һҳ</a> \r\n")
						.append(
								"<a href=\"javascript:page_last();\">���һҳ</a>\r\n")
						.append(
								"<input type=\"button\" style=\"padding: .2em .3em 0.1em .3em;\" onclick=\"javascript:page_go();\" value=\"ת��\" />\r\n")
						.append(
								"<input type=\"hidden\" name=\"page.orderBy\"  value=\""
										+ page.getOrderBy() + "\" />\r\n")
						.append(
								"<input type=\"hidden\" name=\"page.sort\"  value=\""
										+ page.getSort() + "\" />\r\n").append(
								"<script>\r\n").append(
								"	var pageTotal = " + page.getTotalPage()
										+ ";\r\n").append(
								"	var recTotal = " + page.getTotalCount() + ";\r\n")
						.append("</script>\r\n").append("</div>\r\n");
				sb.append("<script>\r\n");
				sb
						.append("function page_go()\r\n")
						.append("{\r\n")
						.append("	page_validate();		\r\n")
						.append("	document.forms[0].submit();\r\n")
						.append("}\r\n")
						.append("function page_first()\r\n")
						.append("{\r\n")
						.append(
								"	document.forms[0].elements[\"page.currentPage\"].value = 1;\r\n")
						.append("	document.forms[0].submit();\r\n")
						.append("}\r\n")
						.append("function page_pre()\r\n")
						.append("{\r\n")
						.append(
								"	var pageNo = document.forms[0].elements[\"page.currentPage\"].value;\r\n")
						.append(
								"	document.forms[0].elements[\"page.currentPage\"].value = parseInt(pageNo) - 1;\r\n")
						.append("	page_validate();\r\n")
						.append("	document.forms[0].submit();\r\n")
						.append("}\r\n")
						.append("function page_next()\r\n")
						.append("{\r\n")
						.append(
								"	var pageNo = document.forms[0].elements[\"page.currentPage\"].value;\r\n")
						.append(
								"	document.forms[0].elements[\"page.currentPage\"].value = parseInt(pageNo) + 1;\r\n")
						.append("	page_validate();\r\n")
						.append("	document.forms[0].submit();\r\n")
						.append("}\r\n")
						.append("function page_last()\r\n")
						.append("{\r\n")
						.append(
								"	document.forms[0].elements[\"page.currentPage\"].value = pageTotal;\r\n")
						.append("	document.forms[0].submit();\r\n")
						.append("}\r\n")
						.append("function page_validate()\r\n")
						.append("{\r\n")
						.append(
								"	var pageNo = document.forms[0].elements[\"page.currentPage\"].value;\r\n")
						.append("	if (pageNo<1)pageNo=1;\r\n")
						.append("	if (pageNo>pageTotal)pageNo=pageTotal;\r\n")
						.append(
								"	document.forms[0].elements[\"page.currentPage\"].value = pageNo;\r\n")
						.append(
								"	var pageSize = document.forms[0].elements[\"page.pageSize\"].value;\r\n")
						.append("	if (pageSize<1)pageSize=1;\r\n")
						.append(
								"	document.forms[0].elements[\"page.pageSize\"].value = pageSize;\r\n")
						.append("}\r\n")
						.append("function order_by(field){\r\n")
						.append(
								"	document.forms[0].elements[\"page.orderBy\"].value = field;\r\n")
						.append("	page_first();\r\n").append("}\r\n");
				sb.append("</script>\r\n");
				JspWriter out = pageContext.getOut();
				out.println(sb.toString());

			}
		}
		catch (Exception e)
		{
		}
		return EVAL_PAGE;
	}

}
