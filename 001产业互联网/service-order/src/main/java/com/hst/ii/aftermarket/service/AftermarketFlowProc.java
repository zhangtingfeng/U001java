package com.hst.ii.aftermarket.service;

import com.hst.core.ServiceException;
import com.hst.core.dao.ORQuery;
import com.hst.ii.aftermarket.entity.TAftermarket_Detail;
import com.hst.ii.flow.entity.TFlowProcess;
import com.hst.ii.flow.po.Flow;
import com.hst.ii.flow.po.Link;
import com.hst.ii.flow.po.Node;
import com.hst.ii.order.service.BaseFlowProc;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * OrderFlowProc
 * 订单生成流程处理
 *
 * @author WangYH
 * @date 2020/8/28
 */
@Service
@Qualifier("as")
@Transactional
public class AftermarketFlowProc extends BaseFlowProc {
    @Override
    public void prevProc(TFlowProcess process, Flow flow, Link link) throws ServiceException {

    }


    public void postProc(TFlowProcess process, Flow flow, Link link) throws ServiceException {
        // 更新订单状态
        Node node = link.getNode2();
        if (null == node) return;

        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(ORQuery.Op.eq, "processid", (String) process.getId()));
        List<TAftermarket_Detail> aftermarket_DetailList = dao.list(TAftermarket_Detail.class, querys);
        if (aftermarket_DetailList.size()>0)
        {
            TAftermarket_Detail orderas =aftermarket_DetailList.get(0);
            orderas.setState1(node.getProps().get(STATE1));
            orderas.setState2(node.getProps().get(STATE2));
            dao.update(orderas);
        }
       // TAftermarket_Detail orderas = dao.get(TAftermarket_Detail.class, process.getServiceId());

    }

}
