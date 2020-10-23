package com.hst.ii.order.service;

import com.hst.core.MapBean;
import com.hst.core.ServiceContext;
import com.hst.core.ServiceException;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.dao.ORQuery.Op;
import com.hst.ii.aftermarket.entity.TAftermarket_Detail;
import com.hst.ii.common.entity.TSaleInfo;
import com.hst.ii.common.entity.TSalePropInfo;
import com.hst.ii.common.entity.TUser;
import com.hst.ii.common.entity.TUserCart;
import com.hst.ii.fee.entity.TPaymentRequest;
import com.hst.ii.flow.entity.TFlowNode;
import com.hst.ii.flow.po.Flow;
import com.hst.ii.flow.po.Node;
import com.hst.ii.flow.service.FlowConfigService;
import com.hst.ii.flow.service.FlowService;
import com.hst.ii.order.entity.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class OrderService {

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    @Autowired
    FlowService flowService;

    @Autowired
    FlowConfigService flowConfigService;

    public String createOrders(List<TUserCart> cartList, String addrId) throws IOException, ServiceException {

        String sellerId = cartList.get(0).getSellerId();
        TUser seller = dao.get(TUser.class, sellerId);
        TUser buyer = dao.get(TUser.class, cartList.get(0).getBuyerId());
        TOrderInfo orderInfo = new TOrderInfo();
        orderInfo.setBuyerId(buyer.getOrgId());
        orderInfo.setSellerId(seller.getOrgId());
        orderInfo.setAddrId(addrId);
        orderInfo.setStatus("1");
        orderInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        dao.save(orderInfo);

        TOrderDealer buyDealer = getTOrderDealerByUserId(cartList.get(0).getBuyerId());
        buyDealer.setOrderId(orderInfo.getId());
        buyDealer.setType("1");
        dao.save(buyDealer);

        TOrderDealer sellDealer = getTOrderDealerByUserId(cartList.get(0).getSellerId());
        sellDealer.setOrderId(orderInfo.getId());
        sellDealer.setType("2");
        dao.save(sellDealer);

        //添加订单信息
        for (TUserCart cart : cartList) {
            // 同一卖家的不同商品
            if (sellerId.equals(cart.getSellerId())) {
                TSaleInfo sale = dao.get(TSaleInfo.class, cart.getGoodsId());

                TOrderGoods orderGoods = new TOrderGoods();
                orderGoods.setOrderId(orderInfo.getId());
                orderGoods.setGoodsId(cart.getGoodsId());
                orderGoods.setGoodsPrice(sale.getUnitPrice());
                orderGoods.setNum(cart.getNum());
                orderGoods.setAmountPrice(sale.getUnitPrice().multiply(BigDecimal.valueOf(cart.getNum())));
                orderGoods.setCreateTime(new Timestamp(System.currentTimeMillis()));
                dao.save(orderGoods);

                TUserCart userCart = dao.get(TUserCart.class, cart.getId());
                dao.delete(userCart);
            }
        }

        //启动订单流程
        startFlow(orderInfo);

        return orderInfo.getId();
    }
////创建流程售后服务的
    public TOrderInfo createfatermarket(String orderId) throws IOException, ServiceException {
        TOrderInfo orderInfo=null;
        try {
            //Map<String, Object> reqData = JsonUtil.read(data, Map.class);




            orderInfo= dao.get(TOrderInfo.class, orderId);
            String processId3 = "";
            if (orderInfo.getProcess3() == null || orderInfo.getProcess3().equals("")) {
                processId3 = flowService.start("as", orderId);

                TAftermarket_Detail tTAftermarket_Detail = new TAftermarket_Detail();
                tTAftermarket_Detail.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                tTAftermarket_Detail.setCreateTime(new Timestamp(System.currentTimeMillis()));
                tTAftermarket_Detail.setOrderId(orderInfo.getId());
                tTAftermarket_Detail.setProcessid(processId3);
                tTAftermarket_Detail.setCreateUserid(ServiceContext.getInstance().getUser().getUid());
                tTAftermarket_Detail.setOrgId(ServiceContext.getInstance().getUser().getOrgcode());
                tTAftermarket_Detail.setBuyerId(orderInfo.getBuyerId());
                tTAftermarket_Detail.setSellerId(orderInfo.getSellerId());
                dao.save(tTAftermarket_Detail);

                flowService.autoProc(processId3);
                orderInfo.setProcess3(processId3);
                dao.update(orderInfo);




            }

        } catch (Exception e) {
            //debugLog.send("seller", "getOrderDetail报错了", e);
            //logger.log(level, message, e);
            //throw e;
        }

        return orderInfo;
    }


    public void startFlow(TOrderInfo orderInfo) throws ServiceException {
        String orderId = orderInfo.getId();
        // 流程处理
        String processId = flowService.start("order", orderId);

        orderInfo.setProcess1(processId);
        dao.update(orderInfo);

        Flow flow = flowConfigService.getFlow("order");
        Optional<Node> node = flow.getNodes().values().stream().filter(n -> TFlowNode.TYPE_START.equals(n.getType())).findFirst();
        if (node.isPresent()) {
            flowService.proc(processId, node.get().getLinks().get(0).getNodeId2());

        }
    }

    public TOrderDealer getTOrderDealerByUserId(String userid) throws ServiceException {
        TUser user = dao.get(TUser.class, userid);
        MapBean<String, Object> params = new MapBean<>();
        params.put("orgid", user.getOrgId());
        List<TOrderDealer> dealerList = (List<TOrderDealer>) sql.query("orderInfo.getOrderDealer", params);
        if (dealerList.size() == 0) {
            throw new ServiceException("1");
        }
        try {
            return (TOrderDealer) BeanUtils.cloneBean(dealerList.get(0));
        } catch (Exception ex) {
            throw new ServiceException(ServiceException.INTERNAL);
        }
    }

    private String mapValue(Map<String, Object> map, String key) {
        Object o = map.get(key);
        return null == o ? null : o.toString();
    }

    private Integer toInteger(String st) {
        return StringUtils.isBlank(st) ? 1 : Integer.valueOf(st);
    }


    /**
     * 获取卖家ID列表
     *
     * @param orderList
     * @return
     */
    public List<String> getSellerIdList(List<TOrderInfo> orderList) {
        List<String> sellerIdList = new ArrayList<>();
        for (TOrderInfo order : orderList) {
            if (!sellerIdList.contains(order.getSellerId())) {
                sellerIdList.add(order.getSellerId());
            }
        }

        return sellerIdList;
    }

    public List<Map<String, String>> getSellerRecAddressList(List<String> sellerIdList) {
        Map<String, Object> params = new HashMap<>();
        params.put("sellerIds", sellerIdList);
        return (List<Map<String, String>>) sql.query("orderInfo.getSellerUsers", params);
    }

    public List<Map<String, String>> getSellerInfoList(List<String> sellerIdList) {
        Map<String, Object> params = new HashMap<>();
        params.put("sellerIds", sellerIdList);
        return (List<Map<String, String>>) sql.query("orderInfo.getSellerUsers", params);
    }

    public List<Map<String, String>> getSellerInfo(String sellerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("sellerId", sellerId);
        return (List<Map<String, String>>) sql.query("buyer.getSellerUser", params);
    }

    /**
     * 获取买家ID列表
     *
     * @param orderList
     * @return
     */
    public List<String> getBuyerIdList(List<TOrderInfo> orderList) {
        List<String> buyerIdList = new ArrayList<>();
        for (TOrderInfo order : orderList) {
            if (!buyerIdList.contains(order.getBuyerId())) {
                buyerIdList.add(order.getBuyerId());
            }
        }

        return buyerIdList;
    }

    public List<Map<String, String>> getBuyerInfoList(List<String> buyerIdList) {
        Map<String, Object> params = new HashMap<>();
        params.put("buyerIds", buyerIdList);
        return (List<Map<String, String>>) sql.query("orderInfo.getBuyerUsers", params);
    }

    public List<TOrderDealer> getDealersInfo(String orderId) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.eq, "orderId", orderId));
        return dao.list(TOrderDealer.class, querys);
    }

    public TOrderContract getcontractInfo(String orderId) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.eq, "orderId", orderId));
        List<TOrderContract> myList = dao.list(TOrderContract.class, querys);
        TOrderContract myTOrderContract = new TOrderContract();
        if (myList.size() > 0) {
            myTOrderContract = myList.get(0);
        } else {
            myTOrderContract.setPayDay(new Date());
            myTOrderContract.setDelivery_Start_dt(new Date());
            myTOrderContract.setDelivery_End_dt(new Date());




            ////收货地址start
            TOrderInfo orderInfo = dao.get(TOrderInfo.class, orderId);
            String strgetAddrId=           orderInfo.getAddrId();
            TOrgReceiveAddress orderInfoAddress = dao.get(TOrgReceiveAddress.class, strgetAddrId);
            myTOrderContract.setDelivery_address(orderInfoAddress.getAddress());
///end


            myTOrderContract.setOrderId(orderId);
        }
        return myTOrderContract;

		/*TOrderContract returnTOrderContract=new TOrderContract();
		MapBean<String, Object> params = new MapBean<>();
		params.put("order_id", orderId);
		List<TOrderContract> OrderContractList = (List<TOrderContract>) sql.query("orderInfo_contract.getOrder_contract", params);
		if (OrderContractList.size() == 0) {
			returnTOrderContract= new com.hst.ii.order.entity.TOrderContract();
		} else {
			returnTOrderContract= OrderContractList.get(0);
		}

		return returnTOrderContract;*/
    }

    /**
     * 获取订单ID列表
     *
     * @param orderList
     * @return
     */
    public List<String> getOrderIdList(List<TOrderInfo> orderList) {
        List<String> orderIdList = new ArrayList<>();
        for (TOrderInfo order : orderList) {
            if (!orderIdList.contains(order.getId())) {
                orderIdList.add(order.getId());
            }
        }

        return orderIdList;
    }

    public List<TOrderGoods> getOrderGoodsList(List<String> orderIdList) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.in, "orderId", orderIdList));
        return dao.list(TOrderGoods.class, querys);
    }

    public List<TOrderGoods> getOrderGoodsList(String orderId) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.eq, "orderId", orderId));
        return dao.list(TOrderGoods.class, querys);
    }

    public List<TPaymentRequest> getTPaymentRequest(String orderId) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.eq, "orderId", orderId));
        List<TPaymentRequest> myreturn = dao.list(TPaymentRequest.class, querys);
        if (myreturn.size() == 0) {
         //   TPaymentRequest ddd = new TPaymentRequest();
         //   String dateStr = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
         //   ddd.setId(dateStr);
         //   myreturn.add(ddd);
        }
        return myreturn;
    }

    public TPaymentRequest getTPaymentRequest(String orderId,String strArgs) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.eq, "orderId", orderId));
       if (strArgs!=null){
           querys.add(new ORQuery(Op.eq, "payPhase", strArgs));
       }
        List<TPaymentRequest> myreturn = dao.list(TPaymentRequest.class, querys);
       if (myreturn.size()>0){
          return myreturn.get(0);
         // return  myreturn[0];
       }
       else{
           return null;
       }
    }

    public List<TOrder_rd> getTOrder_rd(String orderId) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.eq, "orderId", orderId));
        List<TOrder_rd> myreturn = dao.list(TOrder_rd.class, querys);
        if (myreturn.size() == 0) {
            TOrder_rd ddd = new TOrder_rd();
            String dateStr = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            ddd.setId(dateStr);
            myreturn.add(ddd);
        }
        return myreturn;
    }


    public TOrder_evaluate getTevaluate(String orderId) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.eq, "orderId", orderId));
        List<TOrder_evaluate> myreturn = dao.list(TOrder_evaluate.class, querys);
        if (myreturn.size() == 0) {
            return null;
        }
        else{
            return myreturn.get(0);
        }
    }

    public TOrgReceiveAddress getTADDOrd_id(String orderaddId) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.eq, "id", orderaddId));
        List<TOrgReceiveAddress> myreturn = dao.list(TOrgReceiveAddress.class, querys);
        return myreturn.get(0);
    }

    public List<TOrderSpecifications> getSpecificationsList(String orderId) {
        MapBean<String, Object> params = new MapBean<>();
        params.put("orderId", orderId);

        List<TOrderSpecifications> myreturnList = (List<TOrderSpecifications>) sql.query("orderInfo_contract.getspecifications", params);
        if (myreturnList.size() > 0) {

        } else {
            MapBean<String, Object> paramsinsert = new MapBean<>();
            paramsinsert.put("orderid", orderId);
            for (int i = 0; i < 46; i++) {
                sql.query("insertspecifications", paramsinsert);
            }
            myreturnList = (List<TOrderSpecifications>) sql.query("orderInfo_contract.getspecifications", params);


        }
        return myreturnList;
    }


    /**
     * 获取商品ID列表
     *
     * @param oGoodsList
     * @return
     */
    public List<String> getGoodsIdList(List<TOrderGoods> oGoodsList) {
        List<String> goodsIdList = new ArrayList<>();
        for (TOrderGoods oGoods : oGoodsList) {
            if (!goodsIdList.contains(oGoods.getGoodsId())) {
                goodsIdList.add(oGoods.getGoodsId());
            }
        }

        return goodsIdList;
    }

    /**
     * 获取商品列表
     *
     * @param goodsIdList
     * @return
     */
    public List<TSaleInfo> getGoodsList(List<String> goodsIdList) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.in, "id", goodsIdList));
        return dao.list(TSaleInfo.class, querys);
    }

    public Map<String, List<Map<String, String>>> getSaleImages(List<String> goodsIdList) {
        Map<String, List<Map<String, String>>> dataMap = new HashMap<>();
        for (String goodsId : goodsIdList) {
            // 过期时间
            MapBean<String, Object> params = new MapBean<String, Object>();
            params.put("id_sales", goodsId);
            List<Map<String, String>> salePropertyInfoList = (List<Map<String, String>>) sql.query("orderInfo.getImages", params);
            dataMap.put(goodsId, salePropertyInfoList);
        }
        return dataMap;
    }

    /**
     * 获取商品属性列表
     *
     * @param goodsIdList
     * @return
     */
    public List<TSalePropInfo> getPropsList(List<String> goodsIdList) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(Op.in, "salesId", goodsIdList));
        return dao.list(TSalePropInfo.class, querys);
    }

    public List<Map<String, String>> getOrderSummary() {
        return null;
    }

}
