package com.hst.ii.aftermarket.web;

import com.hst.core.ServiceContext;
import com.hst.core.ServiceException;
import com.hst.core.annotation.WebAuth;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.aftermarket.entity.TAftermarketImg;
import com.hst.ii.aftermarket.entity.TAftermarketInfo;
import com.hst.ii.aftermarket.entity.TAftermarket_Detail;
import com.hst.ii.aftermarket.service.AftermarketService;
import com.hst.ii.debugLog;
import com.hst.ii.flow.entity.TFlowProcess;
import com.hst.ii.flow.service.FlowConfigService;
import com.hst.ii.flow.service.FlowService;
import com.hst.ii.order.entity.TOrderInfo;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.sql.Timestamp;
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
@RequestMapping("/aftermarketinfo")
@WebAuth
public class AftermarketInfoController {
    @Autowired
    AftermarketService aftermarketInfoService;

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    @Autowired
    FlowService flowService;

    @Autowired
    FlowConfigService flowConfigService;

    @RequestMapping("/process/{id}")
    public void process(@PathVariable String id, Model m) {
        TFlowProcess process = (TFlowProcess) flowService.getFlowProcess(id, false, false).get("process");
        String orderId = process.getServiceId();
        TOrderInfo order = dao.get(TOrderInfo.class, orderId);
        String org = ServiceContext.getInstance().getOrg();
        String userType = StringUtils.equals(org, order.getBuyerId()) ? "1" :
                (StringUtils.equals(org, order.getSellerId()) ? "2" : "");

        m.addAttribute("process", process);
        m.addAttribute("userType", userType);
    }


    @RequestMapping("/getServicesList")
    public void getServiceList(@RequestBody String data, Model m) throws IOException, ParseException {
        Map<String, Object> reqData = JsonUtil.read(data, Map.class);

        String strbuyerOrSeller = (String) reqData.get("buyerOrSeller");

        String buyerOrSellerId = ServiceContext.getInstance().getUser().getOrgcode();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String srrstarttime = (String) reqData.get("starttime");

        Date startTime = srrstarttime != null ? formatter.parse(srrstarttime) : null;
        String srrEndTime = (String) reqData.get("endtime");
        Date EndTime = srrEndTime != null ? formatter.parse(srrEndTime) : null;

        startTime = com.hst.ii.commonDate.getStartOfDay(startTime);
        EndTime = com.hst.ii.commonDate.getEndOfDay(EndTime);


        //  EndTime.getDate()
        List<TAftermarket_Detail> aftermarketList = aftermarketInfoService.getAftermarketList(buyerOrSellerId, strbuyerOrSeller, startTime, EndTime);
        if (aftermarketList.size() == 0) {
            m.addAttribute("orders", new ArrayList<>());
            m.addAttribute("buyers", new ArrayList<>());
            return;
        }

        m.addAttribute("services", aftermarketList);
        if (strbuyerOrSeller.equals("buyer")) {
            List<String> sellerIdList = aftermarketInfoService.getSellerIdList(aftermarketList);

            m.addAttribute("sellers", aftermarketInfoService.getSellerInfoList(sellerIdList));


        } else if (strbuyerOrSeller.equals("seller")) {
            List<String> buyerIdList = aftermarketInfoService.getbuyerIdList(aftermarketList);


            m.addAttribute("buyers", aftermarketInfoService.getSellerInfoList(buyerIdList));

        }

        // List<String> buyerIdList = aftermarketInfoService.getBuyerIdList(orderList);

        //  m.addAttribute("orders", orderList);
        //  m.addAttribute("buyers", aftermarketInfoService.getBuyerInfoList(buyerIdList));
    }

    @RequestMapping("/getServiceDetail")
    public void getServiceDetail(@RequestBody String data, Model m) throws IOException, ParseException, JSONException {
        try {
            Map<String, Object> reqData = JsonUtil.read(data, Map.class);

            List<ORQuery> querys = new ArrayList<>();
            querys.add(new ORQuery(ORQuery.Op.eq, "processid", (String) reqData.get("processid")));
            List<TAftermarket_Detail> aftermarket_DetailList = dao.list(TAftermarket_Detail.class, querys);
            if (aftermarket_DetailList.size() == 0) {
                m.addAttribute("TAftermarket_Detail", new TAftermarketInfo());
            } else {
                m.addAttribute("TAftermarket_Detail", aftermarket_DetailList.get(0));

                List<ORQuery> querysimg = new ArrayList<>();
                querysimg.add(new ORQuery(ORQuery.Op.eq, "aftermarket_detailID", aftermarket_DetailList.get(0).getId()));
                querysimg.add(new ORQuery(ORQuery.Op.order, "idx", "asc"));
                List<TAftermarketImg> aftermarket_imgList = dao.list(TAftermarketImg.class, querysimg);
                m.addAttribute("TAftermarketImgList", aftermarket_imgList);
            }


        } catch (Exception e) {
            debugLog.send("aftermarketinfo", "getServiceDetail报错了", e);
            //logger.log(level, message, e);
            throw e;
        }
    }


    /**
     * 保存售后服务信息
     */
    @RequestMapping("/saveaftermarketdetail")
    public void saveaftermarketdetail(@RequestBody String data, Model m) throws IOException, ServiceException, ParseException, JSONException {

        try {
            Map<String, Object> reqData = JsonUtil.read(data, Map.class);

            String strprocessid = (String) reqData.get("processid");

            List<ORQuery> querysimgOrder = new ArrayList<>();
            querysimgOrder.add(new ORQuery(ORQuery.Op.eq, "process3", strprocessid));
            List<TOrderInfo> afterTOrderInfogList = dao.list(TOrderInfo.class, querysimgOrder);
            TOrderInfo orderInfo = afterTOrderInfogList.get(0);
//            String strOrderID= .getId();
            //          TOrderInfo orderInfo = dao.get(TOrderInfo.class, strOrderID);


            String strTAftermarket_Detailid = (String) reqData.get("id");
            /*
            if (strTAftermarket_Detailid == "" || strTAftermarket_Detailid == null) {

                tTAftermarket_Detail.setBuyeraftermarkettype((String) reqData.get("buyeraftermarkettype"));
                tTAftermarket_Detail.setBuyeraftermarketdesc((String) reqData.get("buyeraftermarketdesc"));

                tTAftermarket_Detail.setSelleraftermarkettype((String) reqData.get("selleraftermarkettype"));

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String srrsellerservice_day = (String) reqData.get("sellerservice_day");
                Date sellerservice_day = srrsellerservice_day != null ? formatter.parse(srrsellerservice_day) : null;
                tTAftermarket_Detail.setSellerserviceDay(sellerservice_day);

                tTAftermarket_Detail.setSelleraftermarketdesc((String) reqData.get("selleraftermarketdesc"));


                tTAftermarket_Detail.setBuyerId(orderInfo.getBuyerId());
                tTAftermarket_Detail.setSellerId(orderInfo.getSellerId());


                dao.save(tTAftermarket_Detail);
                strTAftermarket_Detailid = tTAftermarket_Detail.getId();

                m.addAttribute("TAftermarket_Detail", tTAftermarket_Detail);


/*
                ////由于没有创建的流程  所以第一次保存默认给一个流程
                Flow flow = flowConfigService.getFlow("as");
                Optional<Node> node = flow.getNodes().values().stream().filter(n -> TFlowNode.TYPE_START.equals(n.getType())).findFirst();
                if (node.isPresent()) {
                    String strgetNodeId1=node.get().getLinks().get(0).getNodeId2();
                    flowService.proc(strprocessid, strgetNodeId1);
 调试售后的语句

 update order_info set process3=null;
delete  from   aftermarket_detail;
delete  from   aftermarket_img;

delete  from   flow_process WHERE id_flow='as'

 update order_info set process3=null;
delete  from   aftermarket_detail;
delete  from   flow_process WHERE id_flow='as'
 }
*

            } else {
*/
            TAftermarket_Detail reqmyData = JsonUtil.read(data, TAftermarket_Detail.class);
            reqmyData.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            dao.update(reqmyData);
            m.addAttribute("TAftermarket_Detail", reqmyData);
            //  }

            // MapBean<String, Object> params = new MapBean<>();
            // params.put("id_aftermarket_detail", strTAftermarket_Detailid);
            // sql.delete("aftermarketInfo.delteimg", params);


            // 售后服务的请求图片
            List<String> images1 = (List<String>) reqData.get("images1");
            if (images1 != null) {
                int i = 1;
                for (String img : images1) {
                    TAftermarketImg saleImg = new TAftermarketImg();
                    saleImg.setOrderId(orderInfo.getId());
                    saleImg.setAftermarket_detailID(strTAftermarket_Detailid);
                    saleImg.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    saleImg.setOrderId(orderInfo.getId());
                    saleImg.setCreateUserid(ServiceContext.getInstance().getUser().getUid());
                    saleImg.setImgPath(img);
                    saleImg.setType("1");///1 是售后服务的
                    saleImg.setIdx(i);///图片顺序
                    dao.save(saleImg);
                    i++;
                }
            }


        } catch (
                Exception e) {
            debugLog.send("aftermarketinfo", "saveaftermarketdetail报错了", e);
            throw e;
        }
    }

}
