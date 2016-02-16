package com.mas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Indomog.pay.Paywall;
import com.Indomog.pay.conf.IndomogConfig;
import com.Indomog.pay.data.IndomogResponse;
import com.Indomog.pay.data.MogValue;
import com.Indomog.pay.data.MogValueExchange;
import com.Indomog.pay.utils.DateUtil;
import com.Indomog.pay.utils.Util;
import com.alibaba.fastjson.JSON;
import com.mas.data.BaseResponse;
import com.mas.data.CallbackData;
import com.mas.data.CallbackRequest;
import com.mas.data.DataPay;
import com.mas.data.Page;
import com.mas.data.PayMode;
import com.mas.data.PayRequest;
import com.mas.data.PayResponse;
import com.mas.data.PayWay;
import com.mas.data.RechargeRequest;
import com.mas.data.RechargeResponse;
import com.mas.market.pojo.TCp;
import com.mas.market.pojo.TPayIndomog;
import com.mas.market.service.TCpService;
import com.mas.market.service.TPayIndomogService;
import com.mas.util.AddressUtils;
import com.mas.util.HttpUtils;
import com.mas.ws.pojo.MasUser;
import com.mas.ws.pojo.PayRechargeFailing;
import com.mas.ws.pojo.PayRechargeSuccess;
import com.mas.ws.pojo.PayUserPurchase;
import com.mas.ws.service.MasUserService;
import com.mas.ws.service.PayRechargeFailingService;
import com.mas.ws.service.PayRechargeSuccessService;
import com.mas.ws.service.PayUserPurchaseService;
/**
 * @author hhk
 */
@Service
@Path(value="/pay")
@Produces("application/json")
public class PayResource extends BaseResoure
{
	@Autowired
	private MasUserService masUserService;
	@Autowired
	private PayUserPurchaseService payUserPurchaseService;
	@Autowired
	private TPayIndomogService tPayIndomogService;
	@Autowired
	private PayRechargeFailingService payRechargeFailingService;
	@Autowired
	private PayRechargeSuccessService PayRechargeSuccessService;
	@Autowired
	private TCpService tCpService;
	
	@POST
	@Path("/dopay")
	public BaseResponse dopay(PayRequest req){
		PayResponse rep = new PayResponse();
		rep.setTrxrc(40001);
		try {
			MasUser masUser = req.getMasUser();
			if(null != masUser && masUser.getUserId()!=0){
				MasUser user = masUserService.selectByPrimaryKey(masUser.getUserId());
				if(null!=user && masUser.getUserName().equals(user.getUserName()) && masUser.getUserPwd().equals(user.getUserPwd())){
					DataPay data = req.getData();
					this.purchase(user,data,rep);
				}else{
					rep.getState().setCode(201);
					rep.getState().setMsg("account password is incorrect,please relogin.");
				}
			}else{
				rep.getState().setCode(201);
				rep.getState().setMsg("account password is incorrect,please relogin.");
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		return rep;
	}
	
	@POST
	@Path("/paymode")
	public BaseResponse paymode(PayRequest req){
		PayResponse rep = new PayResponse();
		rep.setTrxrc(40002);
		try {
			MasUser masUser = req.getMasUser();
			if(null != masUser && masUser.getUserId()!=0){
				MasUser user = masUserService.selectByPrimaryKey(masUser.getUserId());
				if(null!=user && masUser.getUserName().equals(user.getUserName()) && masUser.getUserPwd().equals(user.getUserPwd())){
					DataPay data = req.getData();
					PayMode payMode = req.getPayMode();
					if(payMode.getPayWay()==PayWay.Indomog.getId()){
						com.mas.market.pojo.Criteria criteria = new com.mas.market.pojo.Criteria();
						criteria.put("channelId", data.getChannelId());
						criteria.put("cpId", data.getCpId());
						criteria.put("appId", data.getAppId());
						criteria.setOrderByClause(" aValuePresent desc ");
						List<TPayIndomog> list = tPayIndomogService.selectByExchange(criteria);
						
						List<MogValueExchange> exchangeList = new ArrayList<MogValueExchange>();
						for(MogValue mogValue:MogValue.values()){
							MogValueExchange exchange = new MogValueExchange();
							exchange.setMogValue(mogValue.getValue());
							exchange.setaValue(mogValue.getValue()/IndomogConfig.exchange);
							for(TPayIndomog mog:list){
								if(mog.getMogValue().intValue() == mogValue.getValue().intValue()){
									exchange.setaValuePresent(mog.getaValuePresent());break;
								}
							}
							exchangeList.add(exchange);
						}
						rep.setExchange(exchangeList);
					}else{
						rep.getState().setCode(501);
						rep.getState().setMsg("payMode Options error , there is no payway:"+payMode.getPayWay());
					}
				}else{
					rep.getState().setCode(201);
					rep.getState().setMsg("account password is incorrect,please relogin");
				}
			}else{
				rep.getState().setCode(201);
				rep.getState().setMsg("account password is incorrect,please relogin");
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		return rep;
	}
	
	@POST
	@Path("/payrecharge")
	public PayResponse payrecharge(PayRequest req){
		PayResponse rep = new PayResponse();
		rep.setTrxrc(40003);
		try {
			MasUser masUser = req.getMasUser();
			if(null != masUser && masUser.getUserId()!=0){
				MasUser user = masUserService.selectByPrimaryKey(masUser.getUserId());
				if(null!=user && masUser.getUserName().equals(user.getUserName()) && masUser.getUserPwd().equals(user.getUserPwd())){
					DataPay data = req.getData();
					PayMode payMode = req.getPayMode();
					if(payMode.getPayWay()==PayWay.Indomog.getId()){
						String orderId = ""+data.getChannelId()+System.currentTimeMillis();
						data.setOrderId(orderId);
						String cardNo = payMode.getCardNo();
						Integer carValue = payMode.getCarValue();
						IndomogResponse indomogResponse = Paywall.Inquiry(cardNo,carValue,orderId);
						com.Indomog.pay.pojo.Data indomgoData = indomogResponse.getResponse().getData();
						String code = indomgoData.getRspCode();
						
						PayRechargeSuccess payIndomogRecharge = new PayRechargeSuccess();
						payIndomogRecharge.setCreateTime(new Date());
						payIndomogRecharge.setCardNo(cardNo);
						payIndomogRecharge.setRechargeType("Indomog");
						payIndomogRecharge.setOrderId(orderId);
						payIndomogRecharge.setAppId(data.getAppId());
						payIndomogRecharge.setApkKey(data.getApkKey());
						payIndomogRecharge.setChannelId(data.getChannelId());
						payIndomogRecharge.setCpId(data.getCpId());
						payIndomogRecharge.setClientId(data.getClientId());
						payIndomogRecharge.setServerId(data.getServerId());
						payIndomogRecharge.setOrderId(data.getOrderId());
						payIndomogRecharge.setUserId(masUser.getUserId());
						payIndomogRecharge.setUserName(masUser.getUserName());
						payIndomogRecharge.setUserPwd(masUser.getUserPwd());
						String sessionId = getRequest().getSession().getId();
						payIndomogRecharge.setSessionId(sessionId);
						String ip = AddressUtils.getClientIp(getRequest());
						payIndomogRecharge.setIP(ip);
						
						payIndomogRecharge.setNow(indomgoData.getNow());
						payIndomogRecharge.setRMID(indomgoData.getRMID());
						payIndomogRecharge.setQID(indomgoData.getQID());
						payIndomogRecharge.setRspCode(indomgoData.getRspCode());
						payIndomogRecharge.setRspDesc(indomgoData.getRspDesc());
						payIndomogRecharge.setTrxID(indomgoData.getTrxID());
						payIndomogRecharge.setTrxStatus(indomgoData.getTrxStatus());
						payIndomogRecharge.setBID(indomgoData.getBID());
						payIndomogRecharge.setTrxRC(indomgoData.getTrxRC());
						payIndomogRecharge.setTrxTime(indomgoData.getTrxTime());
						payIndomogRecharge.setTrxValue(indomgoData.getTrxValue());
						payIndomogRecharge.setSignature(indomogResponse.getSignature());
						payIndomogRecharge.setCertificate(indomogResponse.getCertificate());
						if("000".equals(code)){
							com.Indomog.pay.pojo.PayMode imdomgoPayMode = indomogResponse.getResponse().getPayMode();
							if(null!=imdomgoPayMode){
								payIndomogRecharge.setBMod(imdomgoPayMode.getBMod());
								payIndomogRecharge.setProdID(imdomgoPayMode.getProdID());
								payIndomogRecharge.setSN(imdomgoPayMode.getSN());
								payIndomogRecharge.setVal(imdomgoPayMode.getVal());
								carValue = Integer.parseInt(imdomgoPayMode.getVal());
							}
							payIndomogRecharge.setRechargeValue(carValue);
							
							Integer exchangeAValue = carValue/IndomogConfig.exchange;
							payIndomogRecharge.setaValue(exchangeAValue);
							user.setaValue(user.getaValue()+exchangeAValue);
							user.setaValueAll(user.getaValueAll()+exchangeAValue);
							com.mas.market.pojo.Criteria criteria = new com.mas.market.pojo.Criteria();
							criteria.put("channelId", data.getChannelId());
							criteria.put("cpId", data.getCpId());
							criteria.put("appId", data.getAppId());
							criteria.setOrderByClause(" aValuePresent desc ");
							List<TPayIndomog> list = tPayIndomogService.selectByExchange(criteria);
							for(TPayIndomog mog:list){
								if(mog.getMogValue().intValue() == carValue.intValue()){
									exchangeAValue += mog.getaValuePresent();
									user.setaValuePresent(user.getaValuePresent()+mog.getaValuePresent());
									user.setaValuePresentAll(user.getaValuePresentAll()+mog.getaValuePresent());
									payIndomogRecharge.setaValuePresent(mog.getaValuePresent());
								}
							}
							Date date = new Date();
							user.setUpdateTime(date);
							user.setRechargeNum(user.getRechargeNum()+1);
							String rechargeTimes = user.getRechargeTimes();
							String month = DateUtil.formatDate(date,DateUtil.DATE_FORMAT_3);
							if(StringUtils.isEmpty(rechargeTimes)){
								user.setRechargeTimes(month);
							}else{
								String[] recharges =  rechargeTimes.split("&");
								boolean b = Arrays.asList(recharges).contains(month);
								if(!b){
									user.setRechargeTimes(user.getRechargeTimes()+"&"+month);
								}
							}
							if(req.isPurchase()){
								this.purchase(user, data, rep);
								DataPay dataPay = rep.getData();
								dataPay.setOrderId(orderId);
								dataPay.setCarValue(carValue);
								dataPay.setExchangeAValue(payIndomogRecharge.getaValue());
								dataPay.setExchangeAValuePresent(payIndomogRecharge.getaValuePresent());
								rep.getState().setCode(200);
								rep.getState().setMsg("recharge:"+carValue+" to exchange aValue:"+exchangeAValue+" success. purchase "+rep.getState().getMsg());
							}else{
								DataPay dataPay = new DataPay();
								dataPay.setOrderId(orderId);
								dataPay.setaValue(user.getaValue());
								dataPay.setaValuePresent(user.getaValuePresent());
								dataPay.setCarValue(carValue);
								dataPay.setExchangeAValue(payIndomogRecharge.getaValue());
								dataPay.setExchangeAValuePresent(payIndomogRecharge.getaValuePresent());
								rep.setData(dataPay);
								rep.getState().setCode(200);
								rep.getState().setMsg("recharge:"+carValue+" to exchange aValue:"+exchangeAValue+" success.");
							}
							
							payIndomogRecharge.setExchangeAValue(exchangeAValue);
							PayRechargeSuccessService.insertSelective(payIndomogRecharge);
							masUserService.updateByPrimaryKeySelective(user);
						}else{
							payIndomogRecharge.setRechargeValue(carValue);
							PayRechargeFailing pirf = new PayRechargeFailing();
							BeanUtils.copyProperties(pirf, payIndomogRecharge);
							pirf.setaValue(user.getaValue());
							pirf.setaValuePresent(user.getaValuePresent());
							payRechargeFailingService.insertSelective(pirf);
							if("003".equals(code)){
								rep.getState().setCode(203);
								rep.getState().setMsg("Please choose the right value.");
							}else{
								rep.getState().setCode(502);
								rep.getState().setMsg(Util.getIndomogCode(code));
							}
						}
					}else{
						rep.getState().setCode(501);
						rep.getState().setMsg("payMode Options error , there is no payway:"+payMode.getPayWay());
					}
				}else{
					rep.getState().setCode(201);
					rep.getState().setMsg("account password is incorrect,please relogin");
				}
			}else{
				rep.getState().setCode(201);
				rep.getState().setMsg("account password is incorrect,please relogin");
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		return rep;
	}
	private void purchase(final MasUser user,final DataPay data,PayResponse rep){
		Integer aValue = user.getaValue();
		Integer aValuePresent = user.getaValuePresent();
		Integer orderValue = data.getOrderValue();
		DataPay dataPay = new DataPay();
		dataPay.setOrderIdCp(data.getOrderIdCp());
		String orderId = ""+data.getAppId()+System.currentTimeMillis();
		dataPay.setOrderId(orderId);
		data.setOrderId(orderId);
		if(orderValue<=(aValue+aValuePresent)){
			if(orderValue<=aValuePresent){
				user.setaValuePresent(aValuePresent-orderValue);
			}else{
				user.setaValuePresent(0);
				user.setaValue(aValue+aValuePresent-orderValue);
			}
			PayUserPurchase payUserPurchase = new PayUserPurchase();
			payUserPurchase.setAppId(data.getAppId());
			payUserPurchase.setApkKey(data.getApkKey());
			payUserPurchase.setChannelId(data.getChannelId());
			payUserPurchase.setCpId(data.getCpId());
			payUserPurchase.setClientId(data.getClientId());
			payUserPurchase.setServerId(data.getServerId());
			payUserPurchase.setOrderId(orderId);
			payUserPurchase.setOrderIdCp(data.getOrderIdCp());
			payUserPurchase.setOrderValue(data.getOrderValue());
			payUserPurchase.setaValue(user.getaValue());
			payUserPurchase.setaValuePresent(user.getaValuePresent());
			payUserPurchase.setUserId(user.getUserId());
			payUserPurchase.setUserName(user.getUserName());
			payUserPurchase.setUserPwd(user.getUserPwd());
			String sessionId = getRequest().getSession().getId();
			payUserPurchase.setSessionId(sessionId);
			String ip = AddressUtils.getClientIp(getRequest());
			payUserPurchase.setIP(ip);
			String remark = "success. orderValue:"+orderValue+",all aValue:"+(aValue+aValuePresent)+" surplus aValue:"+user.getaValue()+",present aValue:"+user.getaValuePresent();
			payUserPurchase.setRemark(remark);
			masUserService.updateByPrimaryKeySelective(user);
			payUserPurchaseService.insertSelective(payUserPurchase);
			dataPay.setOrderStatus(200);
			dataPay.setOrderValue(orderValue);
			dataPay.setaValue(user.getaValue());
			dataPay.setaValuePresent(user.getaValuePresent());
			rep.setData(dataPay);
			rep.getState().setMsg(remark);
			new Thread(){ public void run() { callbackUrl(data,user);};}.start();
		}else{
			dataPay.setOrderStatus(202);
			dataPay.setOrderValue(orderValue);
			dataPay.setaValue(aValue);
			dataPay.setaValuePresent(aValuePresent);
			rep.setData(dataPay);
			rep.getState().setCode(202);
			rep.getState().setMsg("failure. the aValue:"+(aValue+aValuePresent)+" is not enough:"+orderValue+", please choose pay way to recharge");
		}
	}
	private void callbackUrl(DataPay data,MasUser user){
		TCp cp = tCpService.selectByPrimaryKey(data.getCpId());
		if(null!=cp){
			String callbackUrl = cp.getCallbackUrl();
			if(!StringUtils.isEmpty(callbackUrl)){
				CallbackRequest callBackRequest = new CallbackRequest();
				CallbackData callbackData = new CallbackData();
				callbackData.setAppId(data.getAppId());
				callbackData.setServerId(data.getServerId());
				callbackData.setOrderId(data.getOrderId());
				callbackData.setCpOrderId(data.getOrderIdCp());
				callbackData.setOrderValue(data.getOrderValue());
				callbackData.setUserId(user.getUserId());
				callbackData.setTimeNow(Util.getTime());
				callBackRequest.setSign(callbackData.setSign(data.getCpId(),data.getApkKey()));
				callBackRequest.setData(callbackData);
				String js = JSON.toJSONString(callBackRequest);
				String callback = HttpUtils.doPostJson(callbackUrl,js);
				if(!StringUtils.isEmpty(callbackUrl) && "success".equalsIgnoreCase(callback.replaceAll("\n","").replaceAll("\r",""))){
					com.mas.ws.pojo.Criteria criteria = new com.mas.ws.pojo.Criteria();
					criteria.put("orderId", data.getOrderId());
					PayUserPurchase payUserPurchase = new PayUserPurchase();
					payUserPurchase.setCallback(true);
					payUserPurchaseService.updateByExampleSelective(payUserPurchase,criteria.getCondition());
				}
			}
		}
	}
//---------------平台相关充值接口--------------------------------------------------------------------------------------------------------
	@POST
	@Path("/recharge")
	public BaseResponse recharge(PayRequest req){
		req.setPurchase(false);
		PayResponse rep = this.payrecharge(req);
		rep.setTrxrc(40004);
		return rep;
	}
	
	@POST
	@Path("/rechargelist")
	public RechargeResponse rechargelist(RechargeRequest req){
		RechargeResponse rep = new RechargeResponse();
		rep.setTrxrc(40005);
		try {
			MasUser masUser = req.getMasUser();
			if(null != masUser && masUser.getUserId()!=0){
				MasUser user = masUserService.selectByPrimaryKey(masUser.getUserId());
				if(null!=user && masUser.getUserName().equals(user.getUserName()) && masUser.getUserPwd().equals(user.getUserPwd())){
					com.mas.ws.pojo.Criteria cr = new  com.mas.ws.pojo.Criteria();
					cr.put("userId", masUser.getUserId());
					cr.setOrderByClause(" createTime desc ");
					Page page = req.getPage();
					int startIndex=getStartIndex(page.getPs(),page.getPn());
					cr.setMysqlOffset(startIndex);
					cr.setMysqlLength(page.getPs());
					List<PayRechargeSuccess> list = PayRechargeSuccessService.selectByExample(cr);
					if(list == null || list.size() == 0){
						rep.setIsLast(true);rep.setRechargeNum(0);
					}else{
						List<DataPay> rechargelist = new ArrayList<DataPay>();
						for(PayRechargeSuccess log:list){
							DataPay dataPay = new DataPay();
							dataPay.setOrderId(log.getOrderId());
							dataPay.setaValue(log.getaValue());
							dataPay.setaValuePresent(log.getaValuePresent());
							dataPay.setRechargeTime(DateUtil.formatDate(log.getCreateTime(),DateUtil.DATE_FORMAT_1));
							dataPay.setRechargeType(log.getRechargeType());
							rechargelist.add(dataPay);
						}
						if(list.size() < page.getPs()){
							rep.setIsLast(true);
						}
						rep.setRechargeNum(list.size());
						rep.setRechargelist(rechargelist);
					}
				}else{
					rep.getState().setCode(201);
					rep.getState().setMsg("account password is incorrect,please relogin");
				}
			}else{
				rep.getState().setCode(201);
				rep.getState().setMsg("account password is incorrect,please relogin");
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		return rep;
	}
}
