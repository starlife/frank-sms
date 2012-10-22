package com.frank.ylear.common.model;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Administrator
 *
 * @param <E>
 */
public class PageBean<E> {
	private List<E> list = new ArrayList<E>(); //�ļ�¼�б�
	private int totalCount;         //�ܼ�¼��
    //private int totalPage;        //��
    private int currentPage=1;    //��ǰ
    private int pageSize=10;        //��¼��
    
  

	//���������
	private String orderBy = "";
	private String sort = "asc";
	
	public List<E> getList() {
		return list;
	}
	public void setList(List<E> list) {
		this.list = list;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int recTotal) {
		this.totalCount = recTotal;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public int getPageSize() {
		return (0>=pageSize)?15:pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * ��
	 * @return
	 */
	public int getTotalPage() {
		int totalPage = (this.getTotalCount() - 1) / this.getPageSize() + 1;
		totalPage = (totalPage<1)?1:totalPage;
		return totalPage;
	}
	
    /**
	 * ��¼��ʼ
	 * @return
	 */
	public int getOffset()
	{
		int ret = (this.getCurrentPage()-1) * this.getPageSize();
		ret = (ret < 1)?0:ret;
		return ret;
	}

	 /** *//**
     * �����ж�����
     * @return
     */
    
   

	
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
}
