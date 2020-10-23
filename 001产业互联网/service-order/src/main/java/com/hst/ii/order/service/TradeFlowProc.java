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
import org.springframework.transaction.annotation.Transactional;

/**
 * OrderFlowProc
 * 订单流程处理
 *
 * @author WangYH
 * @date 2020/8/28
 */
@Service
@Qualifier("trade")
@Transactional
public class TradeFlowProc extends BaseFlowProc {
    @Override
    public void prevProc(TFlowProcess process, Flow flow, Link link) throws ServiceException {

    }

    @Override
    public void postProc(TFlowProcess process, Flow flow, Link link) throws ServiceException {
        // 更新订单状态
        updateOrderState(process, flow, link);
    }
}
