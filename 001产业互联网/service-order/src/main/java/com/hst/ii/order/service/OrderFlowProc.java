package com.hst.ii.order.service;

import com.hst.core.ServiceException;
import com.hst.core.dao.IORDao;
import com.hst.ii.flow.entity.TFlowNode;
import com.hst.ii.flow.entity.TFlowProcess;
import com.hst.ii.flow.po.Flow;
import com.hst.ii.flow.po.Link;
import com.hst.ii.flow.po.Node;
import com.hst.ii.flow.service.FlowService;
import com.hst.ii.flow.service.IFlowLinkProc;
import com.hst.ii.order.entity.TOrderInfo;
import com.hst.ii.order.po.BaseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OrderFlowProc
 * 订单生成流程处理
 *
 * @author WangYH
 * @date 2020/8/28
 */
@Service
@Qualifier("order")
@Transactional
public class OrderFlowProc extends BaseFlowProc {
    public static  final String TRADE_FLOW = "trade";

    @Autowired
    FlowService flowService;

    @Override
    public void prevProc(TFlowProcess process, Flow flow, Link link) throws ServiceException {

    }

    @Override
    public void postProc(TFlowProcess process, Flow flow, Link link) throws ServiceException {
        Node node = link.getNode2();
        // 如果订单生成流程进入最后一个节点，自动创建订单交易流程
        if(node != null && TFlowNode.TYPE_END.equals(node.getType())){
            startTrade(process);
        }else{
            // 更新订单状态
            updateOrderState(process, flow, link);
        }
    }

    void startTrade(TFlowProcess process) throws ServiceException {
        TOrderInfo order = dao.get(TOrderInfo.class, process.getServiceId());
        String tradeProcessId =  flowService.start(TRADE_FLOW, process.getServiceId());
//        flowService.proc(tradeProcessId,"t_pay1");
        flowService.autoProc(tradeProcessId);
        order.setStatus(BaseInfo.STATUS_PROC);
        order.setProcess2(tradeProcessId);
        dao.update(order);
    }
}
