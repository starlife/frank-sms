/**
 * 
 */
package com.frank.ylear.modules.unitInfo.service.impl;

import java.io.Serializable;
import java.util.List;


import com.frank.ylear.modules.base.service.BaseService;
import com.frank.ylear.modules.unitInfo.entity.TPosition;
import com.frank.ylear.modules.unitInfo.service.PositionService;

/**
 * 通讯录模块
 * 
 * @author Memory
 * 
 */
@SuppressWarnings("unchecked")
public class PositionServiceImpl extends BaseService implements PositionService {

	/**
	 * 根据ID删除方位
	 * @param id
	 * @return
	 */
	public void delPositionById(Integer id){
		baseDao.del(TPosition.class, (Serializable)id);
	}

	public List<TPosition> findAddressPosition() {
		return baseDao.list(" from TPosition t ");
	}

	public List<TPosition> findPositionByPosName(String posName,
			Integer parentId) {
		StringBuffer hql = new StringBuffer(" from TPosition p where unitname = '")
		.append(posName).append("' ");

		//全局搜索
		if(parentId != null){
		hql.append(" and TPositionParent.unitid = ").append(parentId);
		}
			
		return baseDao.list(hql.toString());
	}

	public List<TPosition> findSonPosition(Integer parentId) {
		StringBuffer hql = new StringBuffer(" from TPosition p where TPositionParent.unitid =").append(parentId);
		return baseDao.list(hql.toString());
	}

	public TPosition getPositionById(Integer id) {
		return (TPosition)baseDao.get(TPosition.class, (Serializable)id);
	}

	public Integer savePosition(TPosition position) {
		//得到父级对象
		TPosition parentPos = (TPosition)baseDao.get(TPosition.class, (Serializable)(position.getTPositionParent().getUnitid()));
		
		//生成code
		//当前同等级最大编码
		String maxPcode = getMaxPosCodeByPid(parentPos);
		
		//下一个编码
		Integer nextCode = Integer.parseInt(maxPcode)+1;
		//下一个编码字符串
		String nextCodeStr = nextCode.toString();
		
		//最高级
		if(parentPos == null){
			//不够两位的加0
			if(nextCodeStr.length() < 2){
				nextCodeStr = "0" + nextCodeStr;
			}
		}else{
			//不够两位的加0
			if(nextCodeStr.length() < 2){
				nextCodeStr = "0" + nextCodeStr;
			}
			nextCodeStr = parentPos.getUnitcode()+nextCodeStr;
		}
		position.setUnitcode(nextCodeStr);
		return (Integer)baseDao.add(position);
	}

	public void updatePosition(TPosition position, boolean flag) {
		//是否修改父单位，如果修改父单位则需要重新编码
		if(flag){
			//得到父级对象
			TPosition parentPos =  (TPosition)baseDao.get(TPosition.class, (Serializable)(position.getTPositionParent().getUnitid()));
			
			//生成code
			//当前同等级最大编码
			String maxPcode = getMaxPosCodeByPid(parentPos);
			
			//下一个编码
			Integer nextCode = Integer.parseInt(maxPcode)+1;
			//下一个编码字符串
			String nextCodeStr = nextCode.toString();
			
			//最高级
			if(parentPos == null){
				//不够两位的加0
				if(nextCodeStr.length() < 2){
					nextCodeStr = "0" + nextCodeStr;
				}
			}else{
				//不够两位的加0
				if(nextCodeStr.length() < 2){
					nextCodeStr = "0" + nextCodeStr;
				}
				nextCodeStr = parentPos.getUnitcode()+nextCodeStr;
			}
			
			//设置方位编码
			position.setUnitcode(nextCodeStr);
		}
		
		baseDao.update(position);
	}
	
	/**
	 * 得到平级方位最大编码
	 * @param 上级方位对象
	 * @return
	 */
	private String getMaxPosCodeByPid(TPosition parentPos) {
		StringBuffer hql = new StringBuffer();
		if(parentPos != null){
			hql.append("select MAX(SUBSTRING(unitcode,(LENGTH(unitcode)-LENGTH('").append(parentPos.getUnitcode()).append("')+1),2)) from TPosition t where t.TPositionParent.unitid = ");
			hql.append(parentPos.getUnitid());
		}else{
			hql.append("select MAX(unitcode) from TPosition t where t.TPositionParent.unitid = 0 ");
		}
		
		//查询得到最大编码
		String maxcode;
		List results = baseDao.list(hql.toString());
		
		if (results != null && results.size() > 0) {

			if (results.get(0) == null || "".equals(results.get(0))) {
				maxcode = "0";
			}else{
				maxcode = (String)results.get(0);
			}
		}else{
			
			maxcode = "0";
		}
		
		return maxcode;
	}

}
