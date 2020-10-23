package com.hst.ii.order.web;

import com.hst.core.ServiceContext;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.debugLog;
import com.hst.ii.order.entity.TOrderInfo;
import com.hst.ii.order.service.OrderService;
import com.hst.ii.order.service.SellerService;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/8/26 10:53
 */
@Controller
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    @Autowired
    OrderService service;

    @Autowired
    SellerService sellerService;

    @RequestMapping("/getOrderList")
    public void getOrderList(@RequestBody String data, Model m) throws IOException, ParseException, JSONException {
        Map<String, Object> reqData = JsonUtil.read(data, Map.class);

        try {
            //创建SimpleDateFormat对象实例并定义好转换格式
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            //Date startTime =(Date)(reqData.get("startTime"));
            String srrstarttime=(String)reqData.get("starttime");
            Date  startTime=srrstarttime!=null?formatter.parse(srrstarttime):null;
            String srrEndTime=(String)reqData.get("endtime");
            Date  EndTime=srrstarttime!=null?formatter.parse(srrEndTime):null;

            startTime= com.hst.ii.commonDate.getStartOfDay(startTime);
            EndTime= com.hst.ii.commonDate.getEndOfDay(EndTime);
            //Date startTime =(String) reqData.get("starttime")!= "" ? formatter.parse((String)reqData.get("starttime")) : null;
            //Date EndTime =(String)  reqData.get("endtime")!= "" ? formatter.parse((String)reqData.get("endtime")) : null;

            String sellerId = ServiceContext.getInstance().getUser().getOrgcode();
            List<TOrderInfo> orderList = sellerService.getOrderList(sellerId, startTime, EndTime);
            if (orderList.size() == 0) {
                m.addAttribute("orders",new ArrayList<>());
                m.addAttribute("buyers", new ArrayList<>());
               return;
            }
            List<String> buyerIdList = service.getBuyerIdList(orderList);

            m.addAttribute("orders", orderList);
            m.addAttribute("buyers", service.getBuyerInfoList(buyerIdList));
        } catch (Exception e) {
            debugLog.send("seller", "seller getOrderList", e);
            //logger.log(level, message, e);
            throw e;
        }
    }
/*
    @RequestMapping("/getOrderDetail")
    public void getOrderDetail(@RequestBody String data, Model m) throws IOException, ParseException, JSONException {
        try {
            Map<String, Object> reqData = JsonUtil.read(data, Map.class);

            TOrderInfo orderInfo = dao.get(TOrderInfo.class, (String) reqData.get("orderId"));

            List<TOrderGoods> oGoodsList = service.getOrderGoodsList(orderInfo.getId());
            List<String> goodsIdList = service.getGoodsIdList(oGoodsList);
            List<TOrderSpecifications> specificationsList = service.getSpecificationsList(orderInfo.getId());


            m.addAttribute("order", JsonUtil.toString(orderInfo));
            m.addAttribute("dealers", service.getDealersInfo(orderInfo.getId()));
            m.addAttribute("orderGoods", oGoodsList);
            m.addAttribute("goods", service.getGoodsList(goodsIdList));
            m.addAttribute("contract", service.getcontractInfo(orderInfo.getId()));
            m.addAttribute("specificationsList", specificationsList);

            //m.addAttribute("images", service.getSaleImages(goodsIdList));
            m.addAttribute("props", service.getPropsList(goodsIdList));
            m.addAttribute("gettpayment_requestList", service.getTPaymentRequest(orderInfo.getId()));
            m.addAttribute("order_rdList", service.getTOrder_rd(orderInfo.getId()));


            debugLog.send("seller", "order", JsonUtil.toString(orderInfo));
            debugLog.send("seller", "dealers", service.getDealersInfo(orderInfo.getId()));
            debugLog.send("seller", "orderGoods", oGoodsList);
            debugLog.send("seller", "goods", service.getGoodsList(goodsIdList));
            debugLog.send("seller", "contract", service.getcontractInfo(orderInfo.getId()));
            debugLog.send("seller", "props", service.getPropsList(goodsIdList));


        } catch (Exception e) {
            debugLog.send("seller", "getOrderDetail报错了", e);
            //logger.log(level, message, e);
            throw e;
        }
    }
*/
/*
    @RequestMapping("/gettpayment_request")
    public void gettpayment_request(@RequestBody String data, Model m) throws IOException, ParseException, JSONException {
        try {
            Map<String, Object> reqData = JsonUtil.read(data, Map.class);

            //TOrderInfo orderInfo = dao.get(TOrderInfo.class, (String) reqData.get("orderId"));

            List<TPaymentRequest> oPaymentRequestList = service.getTPaymentRequest((String) reqData.get("orderId"), (String) reqData.get("payPhase"));


            m.addAttribute("PaymentRequestList", oPaymentRequestList);


        } catch (Exception e) {
            debugLog.send("seller", "gettpayment_request报错了", e);
            //logger.log(level, message, e);
            throw e;
        }
    }

    @RequestMapping("/getOrderDetailDealer")
    public void getOrderDetailDealer(@RequestBody String data, Model m) throws IOException, ParseException, JSONException {
        try {
            Map<String, Object> reqData = JsonUtil.read(data, Map.class);

            TOrderInfo orderInfo = dao.get(TOrderInfo.class, (String) reqData.get("orderId"));

            List<TOrderGoods> oGoodsList = service.getOrderGoodsList(orderInfo.getId());
            List<String> goodsIdList = service.getGoodsIdList(oGoodsList);
            List<TOrderSpecifications> specificationsList = service.getSpecificationsList(orderInfo.getId());


            //m.addAttribute("order", JsonUtil.toString(orderInfo));
            m.addAttribute("dealers", service.getDealersInfo(orderInfo.getId()));
            //m.addAttribute("orderGoods", oGoodsList);
            //m.addAttribute("goods", service.getGoodsList(goodsIdList));
            //m.addAttribute("contract", service.getcontractInfo(orderInfo.getId()));
            //m.addAttribute("specificationsList", specificationsList);

            //m.addAttribute("images", service.getSaleImages(goodsIdList));
            //m.addAttribute("props", service.getPropsList(goodsIdList));


            //debugLog.send("seller", "order", JsonUtil.toString(orderInfo));
            //debugLog.send("seller", "dealers", service.getDealersInfo(orderInfo.getId()));
            //debugLog.send("seller", "orderGoods", oGoodsList);
            //debugLog.send("seller", "goods", service.getGoodsList(goodsIdList));
            //debugLog.send("seller", "contract", service.getcontractInfo(orderInfo.getId()));
            //debugLog.send("seller", "props", service.getPropsList(goodsIdList));


        } catch (Exception e) {
            debugLog.send("seller", "getOrderDetail报错了", e);
            //logger.log(level, message, e);
            throw e;
        }
    }
*/

}
