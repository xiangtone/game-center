package com.mas;
import java.util.Date;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.mas.data.BaseResponse;
import com.mas.data.Data;
import com.mas.data.MachineRequest;
import com.mas.util.AddressUtils;
import com.mas.util.DateUtil;
import com.mas.ws.pojo.ClientMachineWithBLOBs;
import com.mas.ws.service.ClientMachineService;

@Service
@Path(value="/machine")
@Produces("application/json")
public class MachineResource extends BaseResoure{
	
	@Autowired
	ClientMachineService clientMachineService;
	
	@POST
	@Path(value = "run")
	public BaseResponse activate(MachineRequest req)
	{
		BaseResponse rep = new BaseResponse();
		rep.setTrxrc(50001);
		Data data = req.getData();
		if (data == null)
		{
			rep.getState().setCode(501);
			rep.getState().setMsg("parameter error,data is null");
			return rep;
		}
		ClientMachineWithBLOBs clientMachine = req.getClientMachine();
		clientMachine.setAppPackageName(data.getAppPackageName());
		clientMachine.setAppVersionName(data.getAppVersionName());
		clientMachine.setAppVersionCode(data.getAppVersionCode());
		try
		{
			clientMachine.setResolution(clientMachine.getScreenWidth()+"x"+clientMachine.getScreenHeight());
			String ip = AddressUtils.getClientIp(this.getRequest());
			clientMachine.setIP(ip);
			String[] address = AddressUtils.getAddresses(ip);
			if(null!=address){
				clientMachine.setCountry(address[0]);
				clientMachine.setProvince(address[1]);
				clientMachine.setCity(address[2]);
			}
			clientMachine.setActiveNum(1);
			clientMachine.setActiveCreateTime(DateUtil.formatDate(clientMachine.getCreateTime(),DateUtil.DATE_FORMAT_SECOND));
			clientMachine.setActiveUpdateTime(DateUtil.formatDate(new Date(),DateUtil.DATE_FORMAT_SECOND));
			clientMachineService.insertSelective(clientMachine);
		} 
		catch (DuplicateKeyException e) {
			clientMachineService.updateByImeiSelective(clientMachine);
		}
		catch (Exception e)
		{
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
}