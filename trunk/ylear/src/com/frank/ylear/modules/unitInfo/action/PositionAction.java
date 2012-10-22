package com.frank.ylear.modules.unitInfo.action;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.frank.ylear.modules.base.action.BaseAction;
import com.frank.ylear.modules.phoneaddress.entity.UPhoneaddress;
import com.frank.ylear.modules.phoneaddress.service.AddressService;
import com.frank.ylear.modules.unitInfo.entity.TPosition;
import com.frank.ylear.modules.unitInfo.service.PositionService;
import com.frank.ylear.modules.util.GlobalMessage;
import com.frank.ylear.modules.util.MyUtils;


/**
 * 单位模块
 * @author Francis.Hu
 * @createDate 2012-10-15
 */
public class PositionAction extends BaseAction {
	private static final long serialVersionUID = 2997527953261368140L;

	private PositionService positionService;
	private AddressService addressService;
	
	private List<TPosition> positionList;
	private TPosition position;
	
	
	/**
	 * 方位维护页面
	 */
	public String positionPage(){
		positionList = positionService.findAddressPosition();
		
		return SUCCESS;
	}
	
	/**
	 * 进入添加方位页面
	 * @return
	 */
	public String toAddPosition(){
		super.getSession2().setAttribute("flag", "add");
		//判断是否选中父节点
		if(position != null && position.getUnitid() != null){
			//查询父节点
			TPosition parent = positionService.getPositionById(position.getUnitid());
			position.setTPositionParent(parent);
		}else{
			position = new TPosition();
			position.setTPositionParent(new TPosition(0,"单位目录"));
		}
		return SUCCESS;
	}
	
	/**
	 * 新增入库
	 * @return
	 */
	private String createPosition(TPosition position){
		if(position != null && !StringUtils.isEmpty(position.getUnitname())){
			try {
				//验证同级方位名是否已存在
				List<TPosition> posList = positionService.findPositionByPosName(position.getUnitname(),position.getTPositionParent().getUnitid());
				if(posList != null && posList.size() > 0){
					addActionError("同等级下方位名称已被占用！");
					return INPUT;
				}
		
				//入库
				positionService.savePosition(position);
				
				//清除操作标识
				super.getSession2().removeAttribute("flag");
			
				return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				addActionError("服务器繁忙，请稍后再试！");
				return INPUT;
			}
		}
		return INPUT;
	}
	
	/**
	 * 进入修改方位页面
	 * @return
	 */
	public String toUpdatePosition(){
		super.getSession2().setAttribute("flag", "update");
		//判断是否选中父节点
		if(position != null && position.getUnitid() != null){
			position = positionService.getPositionById(position.getUnitid());
			
			//第一级单位父单位处理
			TPosition parentPosition = position.getTPositionParent();
			if(parentPosition == null || ( parentPosition != null && parentPosition.getUnitid() == 0)){
				position.setTPositionParent(new TPosition(0,"单位目录"));
			}
			
			//为修改操作保存记录
			super.getSession2().setAttribute("backToUpdatePosition", position);
		}
		return SUCCESS;
	}
	
	/**
	 * 修改入库
	 * @return
	 */
	private String updatePosition(TPosition position){
		//获取session中保留的原始数据
		TPosition sessionPos = (TPosition)super.getSession("backToUpdatePosition");
		if(position != null){
			try {
				//得到修改后的父单位
				Integer parentNewID = position.getTPositionParent().getUnitid();
				
				//验证同级下方位名是否已存在
				List<TPosition> posList = positionService.findPositionByPosName(position.getUnitname(),parentNewID);
				if(posList != null && posList.size() > 0){
					TPosition compare = posList.get(0);
					if(compare.getUnitid().intValue() != sessionPos.getUnitid().intValue()){
						addActionError("同等级下方位名已被占用！");
						return INPUT;
					}else{
						//合并原始数据防止hibernate报错
						sessionPos = compare;
					}
				}
				
				//判断是否修改了父部门
				boolean flg = false;
				if(sessionPos.getTPositionParent().getUnitid().intValue() != parentNewID.intValue()){
					flg = true;
				}
				
				//将新数据更新到sessionPOs中
				MyUtils.copyNotNullProperties(position, sessionPos);
				
				
				positionService.updatePosition(sessionPos,flg);
				
				//清除操作标识
				super.getSession2().removeAttribute("flag");
				return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				addActionError("服务器繁忙，请稍后再试！");
				return INPUT;
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * 保存入库
	 * @return
	 */
	public String savePosition(){
		//获取操作标识
		String flag = super.getSession("flag").toString();
		if("add".equals(flag)){
			return createPosition(position);
			
		}else if("update".equals(flag)){
			
			return updatePosition(position);
		}
		return INPUT;
	}
	
	/**
	 * 删除单位信息
	 */
	public void delPosition(){
		//初始化删除结果标识
		Integer result = GlobalMessage.FAIL_CODE;
		
		if(position != null && position.getUnitid() != null){
			//验证其下还有没有子单位
			List<TPosition> posList = positionService.findSonPosition(position.getUnitid());
			if(posList != null && posList.size() > 0){
				result = GlobalMessage.WARN_CODE;
			}else{
				List<UPhoneaddress> validList = addressService.findAddressListByProperty("TPosition.unitid",position.getUnitid());
				if(validList != null && validList.size() > 0){
					//该方位下还有联系人，不能删除
					result = GlobalMessage.WARN_CODE;	
				}else{
					positionService.delPositionById(position.getUnitid());
					result = GlobalMessage.SUCC_CODE;	
				}
			}
			
		}
		
		try {
			super.sendAjaxMsg(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PositionService getPositionService() {
		return positionService;
	}

	public void setPositionService(PositionService positionService) {
		this.positionService = positionService;
	}

	public List<TPosition> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<TPosition> positionList) {
		this.positionList = positionList;
	}

	public TPosition getPosition() {
		return position;
	}

	public void setPosition(TPosition position) {
		this.position = position;
	}


	public AddressService getAddressService() {
		return addressService;
	}

	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

}
