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
	private List<E> list = new ArrayList<E>(); //要返回的某一页的记录列表
	private int totalCount;         //总记录数
    //private int totalPage;        //总页数
    private int currentPage=1;    //当前页
    private int pageSize=10;        //每页记录数
    
    /*private boolean isFirstPage;    //是否为第一页
    private boolean isLastPage;        //是否为最后一页
    private boolean hasPreviousPage;    //是否有前一页
    private boolean hasNextPage;        //是否有下一页*/

	//添加排序功能
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
	 * 计算总页数
	 * @return
	 */
	public int getTotalPage() {
		int totalPage = (this.getTotalCount() - 1) / this.getPageSize() + 1;
		totalPage = (totalPage<1)?1:totalPage;
		return totalPage;
	}
	
    /**
	 * 记录开始
	 * @return
	 */
	public int getOffset()
	{
		int ret = (this.getCurrentPage()-1) * this.getPageSize();
		ret = (ret < 1)?0:ret;
		return ret;
	}

	 /** *//**
     * 以下判断页的信息,只需getter方法(is方法)即可
     * @return
     */
    
    /*public boolean isFirstPage() {
        return currentPage == 1;    // 如是当前页是第1页
    }
    public boolean isLastPage() {
        return currentPage == totalPage;    //如果当前页是最后一页
    }
    public boolean isHasPreviousPage() {
        return currentPage != 1;        //只要当前页不是第1页
    }
    public boolean isHasNextPage() {
        return currentPage != totalPage;    //只要当前页不是最后1页
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
