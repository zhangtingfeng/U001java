package com.hst.ii.order.web;

import com.hst.core.ServiceContext;
import com.hst.core.annotation.WebAuth;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.debugLog;
import com.hst.ii.order.entity.TOrderGoods;
import com.hst.ii.order.entity.TOrderInfo;
import com.hst.ii.order.entity.TOrderSpecifications;
import com.hst.ii.order.service.OrderInfoService;
import com.hst.ii.order.service.OrderService;
import com.hst.ii.order.service.SellerService;
import com.hst.ii.order.service.WordToolService;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OrderController
 *
 * @author WangYH
 * @date 2020/8/24
 */
@Controller
@RequestMapping("/info")
@WebAuth
public class OrderInfoController {
    @Autowired
    OrderInfoService orderInfoService;


    ////http://127.0.0.1:92/api/order/info/getOrderProcees3?orderId=S209Q00001


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    void idx(@PathVariable String id, String sub, Model m) {
        String[] subs = StringUtils.isEmpty(sub) ? new String[]{} : sub.split(",");
        m.addAllAttributes(orderInfoService.info(id, subs));
    }

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    @Autowired
    OrderService service;

    @Autowired
    SellerService sellerService;

    @Autowired
    WordToolService wordToolService;


    @RequestMapping("/getOrderList")
    public void getOrderList(@RequestBody String data, Model m,@CookieValue(value="sid") String sid) throws IOException, ParseException {
       // String StrGetsid = ServiceContext.getInstance().toString();


        Map<String, Object> reqData = JsonUtil.read(data, Map.class);
        String sellerId = ServiceContext.getInstance().getUser().getOrgcode();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String srrstarttime = (String) reqData.get("starttime");
        Date startTime = srrstarttime != null ? formatter.parse(srrstarttime) : null;
        String srrEndTime = (String) reqData.get("endtime");
        Date EndTime = srrstarttime != null ? formatter.parse(srrEndTime) : null;
        startTime = com.hst.ii.commonDate.getStartOfDay(startTime);
        EndTime = com.hst.ii.commonDate.getEndOfDay(EndTime);

        List<TOrderInfo> orderList = sellerService.getOrderList(sellerId, startTime, EndTime);
        if (orderList.size() == 0) {
            m.addAttribute("orders", new ArrayList<>());
            m.addAttribute("buyers", new ArrayList<>());
            return;
        }
        List<String> buyerIdList = service.getBuyerIdList(orderList);

        m.addAttribute("orders", orderList);
        m.addAttribute("buyers", service.getBuyerInfoList(buyerIdList));
    }

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


            m.addAttribute("contractSignInfo", wordToolService.getSignValue(orderInfo.getId()));

            m.addAttribute("specificationsList", specificationsList);

            //m.addAttribute("images", service.getSaleImages(goodsIdList));
            m.addAttribute("props", service.getPropsList(goodsIdList));
            m.addAttribute("gettpayment_requestList", service.getTPaymentRequest(orderInfo.getId()));
            m.addAttribute("order_rdList", service.getTOrder_rd(orderInfo.getId()));
            m.addAttribute("order_ADDR", service.getTADDOrd_id(orderInfo.getAddrId()));
            m.addAttribute("Order_evaluate", service.getTevaluate(orderInfo.getId()));


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

}
