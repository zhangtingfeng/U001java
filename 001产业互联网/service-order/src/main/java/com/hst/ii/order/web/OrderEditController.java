package com.hst.ii.order.web;

import com.hst.core.MapBean;
import com.hst.core.ServiceException;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.SendMessage.service.SendMessage;
import com.hst.ii.debugLog;
import com.hst.ii.fee.entity.TPaymentRequest;
import com.hst.ii.flow.service.FlowConfigService;
import com.hst.ii.flow.service.FlowService;
import com.hst.ii.order.entity.*;
import com.hst.ii.order.service.OrderService;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orderedit")
public class OrderEditController {

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    @Autowired
    SendMessage sendmessageService;

    @Autowired
    OrderService OrderServiceservice;

    @Autowired
    FlowService flowService;

    @Autowired
    FlowConfigService flowConfigService;

    /**
     * 保存交易商信息
     */
    @RequestMapping("/savesellerinfo")
    public void saveTraderinfo(@RequestBody String data, Model m,@CookieValue(value="sid") String sid) throws IOException, ServiceException, ParseException, JSONException {

        try {
            //sendmessageService.send("msg","0000000001",sid);

            TOrderDealer reqmyData = JsonUtil.read(data, TOrderDealer.class);
            reqmyData.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            dao.update(reqmyData);

            // List<TOrderDealer> reqData = JsonUtil.readList(data, TOrderDealer.class);
		/*String orderId=service.createOrders(reqData);
		String  processId=flowService.start("order", orderId);
		TOrderInfo orderInfo = dao.get(TOrderInfo.class,orderId);
		orderInfo.setProcess1(processId);
		dao.update(orderInfo);
		Flow flow = flowConfigService.getFlow("order");
		Optional<Node> node = flow.getNodes().values().stream().filter(n-> TFlowNode.TYPE_START.equals(n.getType())).findFirst();
		if (node.isPresent()){
			flowService.proc(processId, node.get().getLinks().get(0).getNodeId2());

		}*/

        } catch (Exception e) {
            debugLog.send("DoOrder", "savesellerinfo报错了", e);
            //logger.log(level, message, e);
            throw e;
        }
    }

    @RequestMapping("/getOrderContract")
    public void getOrderContract(@RequestBody String data, Model m) throws IOException, ParseException, JSONException {
        //TUser user = dao.get(TUser.class, userid);
        MapBean<String, Object> params = new MapBean<>();
        params.put("order_id", data);
        List<TOrderContract> OrderContractList = (List<TOrderContract>) sql.query("orderInfo_contract.getOrder_contract", params);
        if (OrderContractList.size() == 0) {
            m.addAttribute("contract", new com.hst.ii.order.entity.TOrderContract());
        } else {
            m.addAttribute("contract", OrderContractList.get(0));
        }
    }

    /*   */
    @RequestMapping("/saveOrderContractinfo")
    public void savepayinfo(@RequestBody String data, Model m) throws IOException, ServiceException, ParseException, JSONException {

        try {
            TOrderContract reqmyData = JsonUtil.read(data, TOrderContract.class);


            reqmyData.setUpdateTime(new Timestamp(System.currentTimeMillis()));

            dao.update(reqmyData);

            m.addAttribute("OrderContract", reqmyData);
/*

            MapBean<String, Object> params = new MapBean<>();
            params.put("myTOrderContract", reqmyData);


            List<Integer> ddCountpackageinfodd= (List<Integer>) sql.query("Countpackageinfo", params);
            int intValue=ddCountpackageinfodd.get(0);
            if (intValue==0){
                sql.query("insertpayinfo", params);
            }
            else{
                sql.query("updatepayinfo", params);
            }
            // int dddd=Countpackageinfo();
*/


        } catch (Exception e) {
            debugLog.send("DoOrder", "savepackageinfo报错了", e);
            //logger.log(level, message, e);
            throw e;
        }
    }

    /**
     * 保存加工包装和交付时间

     @RequestMapping("/savepackageinfo") public void savepackageinfo(@RequestBody String data, Model m) throws IOException, ServiceException, ParseException, JSONException {

     try {
     TOrderContract reqmyData = JsonUtil.read(data, TOrderContract.class);


     reqmyData.setUpdateTime(new Timestamp(System.currentTimeMillis()));


     MapBean<String, Object> params = new MapBean<>();
     params.put("myTOrderContract", reqmyData);


     List<Integer> ddCountpackageinfodd= (List<Integer>) sql.query("Countpackageinfo", params);
     int intValue=ddCountpackageinfodd.get(0);
     if (intValue==0){
     sql.query("insertpackageinfo", params);
     }
     else{
     sql.query("updatepackageinfo", params);
     }
     // int dddd=Countpackageinfo();



     } catch (Exception e) {
     debugLog.send("DoOrder", "savepackageinfo报错了", e);
     //logger.log(level, message, e);
     throw e;
     }
     }
     */

    /**
     * 保存支付

     @RequestMapping("/savepayinfo") public void savepayinfo(@RequestBody String data, Model m) throws IOException, ServiceException, ParseException, JSONException {

     try {
     TOrderContract reqmyData = JsonUtil.read(data, TOrderContract.class);


     reqmyData.setUpdateTime(new Timestamp(System.currentTimeMillis()));


     MapBean<String, Object> params = new MapBean<>();
     params.put("myTOrderContract", reqmyData);


     List<Integer> ddCountpackageinfodd= (List<Integer>) sql.query("Countpackageinfo", params);
     int intValue=ddCountpackageinfodd.get(0);
     if (intValue==0){
     sql.query("insertpayinfo", params);
     }
     else{
     sql.query("updatepayinfo", params);
     }
     // int dddd=Countpackageinfo();



     } catch (Exception e) {
     debugLog.send("DoOrder", "savepackageinfo报错了", e);
     //logger.log(level, message, e);
     throw e;
     }
     }
     */


    /**
     * 保存违约信息

     @RequestMapping("/savebreakinfo") public void savebreakinfo(@RequestBody String data, Model m) throws IOException, ServiceException, ParseException, JSONException {

     try {
     TOrderContract reqmyData = JsonUtil.read(data, TOrderContract.class);


     reqmyData.setUpdateTime(new Timestamp(System.currentTimeMillis()));


     MapBean<String, Object> params = new MapBean<>();
     params.put("myTOrderContract", reqmyData);


     List<Integer> ddCountpackageinfodd= (List<Integer>) sql.query("Countpackageinfo", params);
     int intValue=ddCountpackageinfodd.get(0);
     if (intValue==0){
     sql.query("insertbreakinfo", params);
     }
     else{
     sql.query("updatebreakinfo", params);
     }
     // int dddd=Countpackageinfo();



     } catch (Exception e) {
     debugLog.send("DoOrder", "savepackageinfo报错了", e);
     //logger.log(level, message, e);
     throw e;
     }
     }
     */

    /**
     * 保存规格
     */
    @RequestMapping("/saveOrderSpecificationsinfo")
    public void saveOrderSpecificationsinfo(@RequestBody String data, Model m) throws IOException, ServiceException, ParseException, JSONException {

        try {
            List<TOrderSpecifications> reqmyDataList = (List<TOrderSpecifications>) JsonUtil.readList(data, TOrderSpecifications.class);


            //reqmyData.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            Map<String, Object> params = new HashMap<>();
            params.put("list", reqmyDataList);
            sql.query("updatespecifications", params);


        } catch (Exception e) {
            debugLog.send("DoOrder", "savepackageinfo报错了", e);
            //logger.log(level, message, e);
            throw e;
        }
    }

    /**
     * 保存商品编辑
     */
    @RequestMapping("/saveOrderGoodsinfo")
    public void saveOrderGoodsinfo(@RequestBody String data, Model m) throws IOException, ServiceException, ParseException, JSONException {

        try {
            List<TOrderGoods> reqmyDataList = (List<TOrderGoods>) JsonUtil.readList(data, TOrderGoods.class);
            for (int i = 0; i < reqmyDataList.size(); i++) {
                TOrderGoods ddd = reqmyDataList.get(i);
                ddd.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                dao.update(ddd);
            }
            //reqmyData.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            //reqmyData.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            ///Map<String,Object> params=new HashMap<>();
            ///params.put("list",reqmyDataList);
            //sql.query("updatespecifications", params);
            //for(int i=0;i<reqmyData.size();i++){
            //  MapBean<String, Object> params = new MapBean<>();
            //  params.put("ifcheck", reqmyData.get(i).isIFCheck());
            //  params.put("orderbyid", reqmyData.get(i).getOrderbyid());
            //  sql.query("updatespecifications", params);
            //}

            // int dddd=Countpackageinfo();


        } catch (Exception e) {
            debugLog.send("DoOrder", "saveOrderGoodsinfo报错了", e);
            //logger.log(level, message, e);
            throw e;
        }
    }

    /**
     * 保存支付付款申请单信息
     */
    @RequestMapping("/savespayreq")
    public void savespayreq(@RequestBody String data, Model m) throws IOException, ServiceException, ParseException, JSONException {

        try {
            // debugLog.send("DoOrder", "savesellerinfo报错了", "1");
            TPaymentRequest reqmyData = JsonUtil.read(data, TPaymentRequest.class);
            String strgetStatus = reqmyData.getStatus();
            //reqmyData.setPaymentDt(new Timestamp(System.currentTimeMillis()));
            //      debugLog.send("DoOrder", "savesellerinfo报错了", strgetStatus);

            reqmyData.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            reqmyData = dao.update(reqmyData);
            //      debugLog.send("DoOrder", "savesellerinfo报错了", reqmyData);

/*
            if (strgetStatus != null && strgetStatus.equals("1")) {//status: 1: 申请支付状态, 0: 保存状态
                reqmyData = OrderServiceservice.getTPaymentRequest(reqmyData.getOrderId(), reqmyData.getPayPhase());
                String flowId = flowConfigService.getOrgFlow("fee");
                String processId = flowService.start(flowId, reqmyData.getId());
                flowService.autoProc(processId);

                reqmyData.setProcessId(processId);
                dao.update(reqmyData);
            }*/
            //   debugLog.send("DoOrder", "savesellerinfo报错了2", reqmyData);
            m.addAttribute("payreq", reqmyData);




            // List<TOrderDealer> reqData = JsonUtil.readList(data, TOrderDealer.class);
		/*String orderId=service.createOrders(reqData);
		String  processId=flowService.start("order", orderId);
		TOrderInfo orderInfo = dao.get(TOrderInfo.class,orderId);
		orderInfo.setProcess1(processId);
		dao.update(orderInfo);
		Flow flow = flowConfigService.getFlow("order");
		Optional<Node> node = flow.getNodes().values().stream().filter(n-> TFlowNode.TYPE_START.equals(n.getType())).findFirst();
		if (node.isPresent()){
			flowService.proc(processId, node.get().getLinks().get(0).getNodeId2());

		}*/

        } catch (Exception e) {
            debugLog.send("DoOrder", "savesellerinfo报错了", e);
            //logger.log(level, message, e);
            throw e;
        }
    }

    /**
     * 发货收货信息
     */
    @RequestMapping("/savesRQ")
    public void savesRQ(@RequestBody String data, Model m) throws IOException, ServiceException, ParseException, JSONException {

        try {
            TOrder_rd reqmyData = JsonUtil.read(data, TOrder_rd.class);

            //reqmyData.setPaymentDt(new Timestamp(System.currentTimeMillis()));
            reqmyData.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            dao.update(reqmyData);

            // List<TOrderDealer> reqData = JsonUtil.readList(data, TOrderDealer.class);
		/*String orderId=service.createOrders(reqData);
		String  processId=flowService.start("order", orderId);
		TOrderInfo orderInfo = dao.get(TOrderInfo.class,orderId);
		orderInfo.setProcess1(processId);
		dao.update(orderInfo);
		Flow flow = flowConfigService.getFlow("order");
		Optional<Node> node = flow.getNodes().values().stream().filter(n-> TFlowNode.TYPE_START.equals(n.getType())).findFirst();
		if (node.isPresent()){
			flowService.proc(processId, node.get().getLinks().get(0).getNodeId2());

		}*/

        } catch (Exception e) {
            debugLog.send("DoOrder", "savesellerinfo报错了", e);
            //logger.log(level, message, e);
            throw e;
        }
    }

    /**
     * 保存评论
     */
    @RequestMapping("/saveevaluate")
    public void saveevaluate(@RequestBody String data, Model m) throws IOException, ServiceException, ParseException, JSONException {

        try {
            TOrder_evaluate reqmyData = JsonUtil.read(data, TOrder_evaluate.class);
            reqmyData.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            dao.update(reqmyData);

        } catch (Exception e) {
            debugLog.send("DoOrder", "saveevaluate报错了", e);
            throw e;
        }
    }


}
