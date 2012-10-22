/**
 * 
 */
package com.frank.ylear.modules.unitInfo.service;

import java.util.HashMap;
import java.util.List;

import com.frank.ylear.modules.unitInfo.entity.TPosition;


/**
 * @author Memory
 * 
 */
public interface PositionService {
	
	/**
	 * 获取联系人单位
	 * @return
	 */
	public List<TPosition> findAddressPosition();
	
	/**
	 * 根据ID查找单位
	 * @param id
	 * @return
	 */
	public TPosition getPositionById(Integer id);
	
	/**
	 * 保存单位
	 * @param position
	 * @return
	 */
	public Integer savePosition(TPosition position);
	
	/**
	 * 根据单位名来查找
	 * @param posName
	 * @return
	 */
	public List<TPosition> findPositionByPosName(String posName,Integer parentId);
	
	/**
	 * 修改单位
	 * @param position
	 * @param flag 是否修改父单位
	 */
	public void updatePosition(TPosition position, boolean flag);
	
	/**
	 * 获得子类列表
	 * @param parentId
	 * @return
	 */
	public List<TPosition> findSonPosition(Integer parentId);
	
	/**
	 * 根据ID删除单位
	 * @param id
	 * @return
	 */
	public void delPositionById(Integer id);
}
