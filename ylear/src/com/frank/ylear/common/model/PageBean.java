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
	private List<E> list = new ArrayList<E>(); //Ҫ���ص�ĳһҳ�ļ�¼�б�
	private int totalCount;         //�ܼ�¼��
    //private int totalPage;        //��ҳ��
    private int currentPage=1;    //��ǰҳ
    private int pageSize=10;        //ÿҳ��¼��
    
    /*private boolean isFirstPage;    //�Ƿ�Ϊ��һҳ
    private boolean isLastPage;        //�Ƿ�Ϊ���һҳ
    private boolean hasPreviousPage;    //�Ƿ���ǰһҳ
    private boolean hasNextPage;        //�Ƿ�����һҳ*/

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
	 * ������ҳ��
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
     * �����ж�ҳ����Ϣ,ֻ��getter����(is����)����
     * @return
     */
    
    /*public boolean isFirstPage() {
        return currentPage == 1;    // ���ǵ�ǰҳ�ǵ�1ҳ
    }
    public boolean isLastPage() {
        return currentPage == totalPage;    //�����ǰҳ�����һҳ
    }
    public boolean isHasPreviousPage() {
        return currentPage != 1;        //ֻҪ��ǰҳ���ǵ�1ҳ
    }
    public boolean isHasNextPage() {
        return currentPage != totalPage;    //ֻҪ��ǰҳ�������1ҳ
    }*/

	
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
