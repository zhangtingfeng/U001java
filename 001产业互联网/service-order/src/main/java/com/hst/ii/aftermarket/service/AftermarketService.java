package com.hst.ii.aftermarket.service;

import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.ii.aftermarket.entity.TAftermarket_Detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class AftermarketService {

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    public List<TAftermarket_Detail> getAftermarketList(String id, String buyerOrSeller, Date startTime, Date EndTime) {

        List<ORQuery> querys=new ArrayList<>();

        if (buyerOrSeller.equals("buyer")){
            querys.add(new ORQuery(ORQuery.Op.eq,"buyerId",id));
        } else  if (buyerOrSeller.equals("seller")){
            querys.add(new ORQuery(ORQuery.Op.eq,"sellerId",id));
        }

        querys.add(new ORQuery(ORQuery.Op.order,"id","asc"));///排序
        if (startTime!=null ){
            querys.add(new ORQuery(ORQuery.Op.gt,"createTime",startTime));
        }
        if (EndTime!=null){
            querys.add(new ORQuery(ORQuery.Op.lt,"createTime",EndTime));
        }
        return dao.list(TAftermarket_Detail.class,querys);
    }

    /**
     * 获取卖家ID列表
     *
     * @param aftermarketList
     * @return
     */
    public List<String> getSellerIdList(List<TAftermarket_Detail> aftermarketList) {
        List<String> sellerIdList = new ArrayList<>();
        for (TAftermarket_Detail aftermarket : aftermarketList) {
            if (!sellerIdList.contains(aftermarket.getSellerId())) {
                sellerIdList.add(aftermarket.getSellerId());
            }
        }

        return sellerIdList;
    }


    public List<Map<String, String>> getSellerInfoList(List<String> sellerIdList) {
        Map<String, Object> params = new HashMap<>();
        params.put("sellerIds", sellerIdList);
        return (List<Map<String, String>>) sql.query("aftermarketInfo.getSellerUsers", params);
    }


    /**
     * 获取卖家ID列表
     *
     * @param aftermarketList
     * @return
     */
    public List<String> getbuyerIdList(List<TAftermarket_Detail> aftermarketList) {
        List<String> buyerIdList = new ArrayList<>();
        for (TAftermarket_Detail aftermarket : aftermarketList) {
            if (!buyerIdList.contains(aftermarket.getBuyerId())) {
                buyerIdList.add(aftermarket.getBuyerId());
            }
        }

        return buyerIdList;
    }


    public List<Map<String, String>> getBuyerInfoList(List<String> buyerIdList) {
        Map<String, Object> params = new HashMap<>();
        params.put("sellerIds", buyerIdList);
        return (List<Map<String, String>>) sql.query("aftermarketInfo.getBuyerUsers", params);
    }
}
