package com.hst.ii.order.web;

import com.hst.core.ServiceContext;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.order.entity.TOrderInfo;
import com.hst.ii.order.service.BuyerService;
import com.hst.ii.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
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
@RequestMapping("/buyer")
public class BuyerController {

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    @Autowired
    OrderService service;

    @Autowired
    BuyerService buyerService;

    @RequestMapping("/getOrderList")
    public void getOrderList(@RequestBody String data, Model m,@CookieValue(value="sid") String sid) throws IOException, ParseException {
        Map<String,Object> reqData= JsonUtil.read(data,Map.class);
        String buyerId= ServiceContext.getInstance().getUser().getOrgcode();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String srrstarttime=(String)reqData.get("starttime");
        Date  startTime=srrstarttime!=null?formatter.parse(srrstarttime):null;
        String srrEndTime=(String)reqData.get("endtime");
        Date  EndTime=srrstarttime!=null?formatter.parse(srrEndTime):null;

        startTime= com.hst.ii.commonDate.getStartOfDay(startTime);
        EndTime= com.hst.ii.commonDate.getEndOfDay(EndTime);


        List<TOrderInfo> orderList=buyerService.getOrderList(buyerId,startTime,EndTime);
        if(orderList.size()==0){
            m.addAttribute("orders",new ArrayList<>());
            m.addAttribute("sellers", new ArrayList<>());
            return;
        }
        List<String> sellerIdList=service.getSellerIdList(orderList);

        m.addAttribute("orders",orderList) ;
        m.addAttribute("sellers",service.getSellerInfoList(sellerIdList)) ;
    }
/*
    @RequestMapping("/getOrderDetail")
    public void getOrderDetail(@RequestBody String data, Model m) throws IOException, ParseException, JSONException {
        Map<String,Object> reqData= JsonUtil.read(data,Map.class);

        TOrderInfo orderInfo=dao.get(TOrderInfo.class, (String) reqData.get("orderId"));

        List<TOrderGoods> oGoodsList=service.getOrderGoodsList(orderInfo.getId());
        List<String> goodsIdList=service.getGoodsIdList(oGoodsList);

        m.addAttribute("order",JsonUtil.toString(orderInfo)) ;
        m.addAttribute("dealers",service.getDealersInfo(orderInfo.getId())) ;
        m.addAttribute("orderGoods",oGoodsList) ;
        m.addAttribute("goods",service.getGoodsList(goodsIdList)) ;
        m.addAttribute("props",service.getPropsList(goodsIdList)) ;


        debugLog.send("buyer", "order", JsonUtil.toString(orderInfo));
        debugLog.send("buyer", "dealers", service.getDealersInfo(orderInfo.getId()));
        debugLog.send("buyer", "orderGoods", oGoodsList);
        debugLog.send("buyer", "goods", service.getGoodsList(goodsIdList));
        debugLog.send("buyer", "contract", service.getcontractInfo(orderInfo.getId()));
        debugLog.send("buyer", "props", service.getPropsList(goodsIdList));
    }

*/
}
