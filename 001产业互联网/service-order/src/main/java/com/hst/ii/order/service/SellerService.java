package com.hst.ii.order.service;

import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.ii.order.entity.TOrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/8/26 11:22
 */
@Service
@Transactional
public class SellerService {

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    public List<TOrderInfo> getOrderList(String id, Date startTime, Date EndTime) {

        List<ORQuery> querys=new ArrayList<>();
        querys.add(new ORQuery(ORQuery.Op.eq,"sellerId",id));
        querys.add(new ORQuery(ORQuery.Op.order,"id","asc"));
        if (startTime!=null ){
            querys.add(new ORQuery(ORQuery.Op.gt,"createTime",startTime));
        }
        if (EndTime!=null){
            querys.add(new ORQuery(ORQuery.Op.lt,"createTime",EndTime));
        }
        return dao.list(TOrderInfo.class,querys);
    }
}
