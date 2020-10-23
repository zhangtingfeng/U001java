package com.hst.ii.order.info;

import com.hst.core.ServiceContext;
import com.hst.core.auth.User;
import com.hst.core.dao.IORDao;
import com.hst.ii.flow.entity.TFlowProcess;
import com.hst.ii.flow.service.FlowService;
import com.hst.ii.order.entity.TOrderInfo;
import com.hst.ii.order.po.BaseInfo;
import com.hst.ii.order.service.IOrderSubInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * OrderBaseInfo
 *
 * @author WangYH
 * @date 2020/8/26
 */
@Service
@Qualifier("base")
public class OrderBaseInfo implements IOrderSubInfo {
    @Autowired
    IORDao dao;

    @Autowired
    FlowService flowService;

    @Override
    public Object get(String id) {
        BaseInfo info = null;
        TOrderInfo order = dao.get(TOrderInfo.class, id);
        if (null != order) {
            info = new BaseInfo();
            info.setId(id);
            info.setBuyerId(order.getBuyerId());
            info.setSellerId(order.getSellerId());
            info.setStatus(order.getStatus());
            info.setProcess1(order.getProcess1());
            info.setProcess2(order.getProcess2());
            info.setState1(order.getState1());
            info.setState2(order.getState2());

//                if (!BaseInfo.STATUS_CANCEL.equals(info.getStatus())){
//                    getProcess1(info);
//                    getProcess2(info);
//                }
            if (StringUtils.isNoneEmpty(order.getProcess2())) {
                getProcess2(info);
                info.setLinks(flowService.getFlowProcessLink(info.getProcess2(), info.getFlow2(), info.getNode2()));
            } else {
                getProcess1(info);
                info.setLinks(flowService.getFlowProcessLink(info.getProcess1(), info.getFlow1(), info.getNode1()));
            }

            User user = ServiceContext.getInstance().getUser();
            if (info.getSellerId().equals(user.getOrgcode())) {
                info.setType(BaseInfo.TYPE_SELLER);
            } else if (info.getBuyerId().equals(user.getOrgcode())) {
                info.setType(BaseInfo.TYPE_BUYER);
            }
        }

        return info;
    }

    void getProcess1(BaseInfo info) {
        if (StringUtils.isEmpty(info.getProcess1()))
            return;
        TFlowProcess fp = dao.get(TFlowProcess.class, info.getProcess1());
        if (null != fp) {
            info.setFlow1(fp.getFlowId());
            info.setNode1(fp.getNodeId());
        }
    }

    void getProcess2(BaseInfo info) {
        if (StringUtils.isEmpty(info.getProcess2()))
            return;
        TFlowProcess fp = dao.get(TFlowProcess.class, info.getProcess2());
        if (null != fp) {
            info.setFlow2(fp.getFlowId());
            info.setNode2(fp.getNodeId());
        }
    }
}
