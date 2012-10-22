package com.frank.ylear.modules.phoneaddress.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.frank.ylear.common.constant.Constants;
import com.frank.ylear.modules.base.action.BaseAction;
import com.frank.ylear.modules.phoneaddress.entity.UPhoneaddress;
import com.frank.ylear.modules.phoneaddress.service.AddressService;
import com.frank.ylear.modules.unitInfo.entity.TPosition;
import com.frank.ylear.modules.unitInfo.service.PositionService;

public class AddressAction extends BaseAction
{
	
	private static final long serialVersionUID = 1L;
	private AddressService addressService;
	/*查询bean*/
	private UPhoneaddress queryBean = null;
	/*添加和更新使用的bean*/
	private UPhoneaddress phoneAddress = null;
	
	private String id=null;
	
	private List<TPosition> positionList;
	private List<UPhoneaddress> linkManList;
	private PositionService positionService;
	public String getId()
	{
		return id;
	}


	public void setId(String id)
	{
		this.id = id;
	}
	
	/**
	 * 列表显示,带查询功能
	 * @return
	 * @throws Exception
	 */
	public String list()
			throws Exception
	{
		addressService.getPhoneAddressList(this.getQueryBean(),this.getPage());
		return SUCCESS;
	}
	
	/**
	 * 定制查询功能，供发短线，彩信使用
	 * @return
	 * @throws Exception
	 */
	public String listCustom()throws Exception{
//		addressService.getPhoneAddressList(this.getQueryBean(),this.getPage());
//		return SUCCESS;
		positionList = addressService.findAddressPosition();//得到所以单位
		
		//加载方位下的联系人
		if(positionList != null){
			linkManList = new ArrayList<UPhoneaddress>();
			
			for(TPosition position : positionList){
				//得到编码
				String posCode = position.getUnitcode();
				//第三级方位
				if(posCode.split("0").length >= 2){
					List<UPhoneaddress> adrList = addressService.findAddressListByPosition(position.getUnitid());
					if(adrList != null && adrList.size() > 0){
						linkManList.addAll(adrList);
					}
				}
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * 定制查询功能，供发短线，彩信使用
	 * @return
	 * @throws Exception
	 */
	public String queryPhone()
		throws Exception
	{
		String phones=addressService.getPhoneNumber(this.getQueryBean());
		PrintWriter out=this.getServletResponse().getWriter();
		out.print("<script>parent.addQueryPhone_callback('"+phones+"')</script>");
		out.flush();
		out.close();
		return null;
	}
	
	/**
	 * 添加或修改，跳转到该方法
	 */
	public String input()
			throws Exception
	{
		if(this.getId()!=null)
		{
			UPhoneaddress fetched=addressService.getPhoneAddress(Long.parseLong(id));
			if(fetched!=null)
			{
				this.setPhoneAddress(fetched);
			}
		}
		return "input";
		
	}

	/**
	 * 删除
	 */
	public String del()
			throws Exception
	{
		if(this.getId()!=null)
		{
			addressService.delPhoneAddress(Long.parseLong(id));
		}
		return SUCCESS;
	}
	
	public void validateSave()
	{
		if(this.hasErrors())
		{
			return;
		}
		if(this.getPhoneAddress().getId()==null)
		{
			//添加
			if(addressService.checkPhoneNumberExist(this.getPhoneAddress()))
			{
				this.addFieldError("phoneAddress.phonenumber",getText("phoneAddress.phonenumber.exist"));
			}
		}else
		{
			UPhoneaddress fetched=addressService.getPhoneAddress(this.getPhoneAddress().getId());
			if(!fetched.getPhonenumber().equals(this.getPhoneAddress().getPhonenumber()))
			{
				//表示修改过号码，需要判断新号码是否已经在数据库中
				if(addressService.checkPhoneNumberExist(this.getPhoneAddress()))
				{
					this.addFieldError("phoneAddress.phonenumber",getText("phoneAddress.phonenumber.exist"));
				}
			}
		}
	}
	public List<TPosition> getPositionList() {
		return positionList;
	}


	public void setPositionList(List<TPosition> positionList) {
		this.positionList = positionList;
	}


	public List<UPhoneaddress> getLinkManList() {
		return linkManList;
	}


	public void setLinkManList(List<UPhoneaddress> linkManList) {
		this.linkManList = linkManList;
	}


	/**
	 * 保存
	 * @return
	 * @throws Exception
	 */
	public String save()
			throws Exception
	{
		if(this.getPhoneAddress()!=null)
		{
			addressService.savePhoneAddress(this.getPhoneAddress());
		}

		return Constants.SUCCESS;
	}
	
	
	
	
	public AddressService getAddressService()
	{
		return addressService;
	}

	public void setAddressService(AddressService addressService)
	{
		this.addressService = addressService;
	}


	public UPhoneaddress getQueryBean()
	{
		return queryBean;
	}


	public void setQueryBean(UPhoneaddress queryBean)
	{
		this.queryBean = queryBean;
	}


	public UPhoneaddress getPhoneAddress()
	{
		return phoneAddress;
	}


	public void setPhoneAddress(UPhoneaddress phoneAddress)
	{
		this.phoneAddress = phoneAddress;
	}


	

}
