package com.hst.ii.order.service;

import com.hst.core.ServiceException;
import com.hst.core.dao.IORDao;
import com.hst.ii.flow.entity.TFlowProcess;
import com.hst.ii.flow.po.Flow;
import com.hst.ii.flow.po.Link;
import com.hst.ii.flow.po.Node;
import com.hst.ii.flow.service.IFlowLinkProc;
import com.hst.ii.order.entity.TOrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * OrderFlowProc
 * 订单流程处理
 *
 * @author WangYH
 * @date 2020/8/28
 */
public abstract class BaseFlowProc implements IFlowLinkProc {
    /**
     * 买家状态
     */
    public static final String STATE1 = "state1";
    /**
     * 卖家状态
     */
    public static final String STATE2 = "state2";

    @Autowired
    protected IORDao dao;

    /**
     * 更新买家/卖家订单状态
     * @param process
     * @param flow
     * @param link
     */
    public void updateOrderState(TFlowProcess process, Flow flow, Link link) {
        Node node = link.getNode2();
        if (null == node) return;

        TOrderInfo order = dao.get(TOrderInfo.class, process.getServiceId());
        order.setState1(node.getProps().get(STATE1));
        order.setState2(node.getProps().get(STATE2));
        dao.update(order);
    }
}
