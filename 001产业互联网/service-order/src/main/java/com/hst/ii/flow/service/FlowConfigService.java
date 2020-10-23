package com.hst.ii.flow.service;

import com.hst.core.ServiceContext;
import com.hst.core.ServiceException;
import com.hst.core.auth.User;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ORQuery;
import com.hst.ii.flow.FlowConfig;
import com.hst.ii.flow.entity.*;
import com.hst.ii.flow.po.Flow;
import com.hst.ii.flow.po.Link;
import com.hst.ii.flow.po.Node;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * FlowConfigService
 * 订单流程配置服务
 *
 * @author WangYH
 * @date 2020/8/24
 */
@Service
public class FlowConfigService {
    Map<String, Flow> flows = new HashMap<>();

    @Autowired
    FlowConfig flowConfig;

    @Autowired
    IORDao dao;

    public String getOrgFlow(String flowType) throws ServiceException {
        TOrgFlow orgFlow = new TOrgFlow();
        orgFlow.setFlowType(flowType);
        orgFlow.setOrgId(ServiceContext.getInstance().getOrg());

        orgFlow = dao.get(TOrgFlow.class, orgFlow);
        if (null == orgFlow)
            throw new ServiceException(ServiceException.INTERNAL, String.format("Org[%s]'s [%s] flow is not defined.", orgFlow.getOrgId(), orgFlow.getFlowType()));

        return orgFlow.getFlowId();
    }

    /**
     * 获取指定流程的定义
     *
     * @param id
     * @return
     */
    public Flow getFlow(String id) throws ServiceException {
//        if (!flows.containsKey(id)) {
//            try {
//                flows.put(id, load(id));
//            }catch(IllegalArgumentException ex){
//                throw new ServiceException("1", ex.getMessage());
//            }
//        }
//        return flows.get(id);
        return load(id);
    }

    public Flow load(String id)  {
        TFlow tFlow = dao.get(TFlow.class, id);
        Assert.notNull(tFlow,
                () -> StringUtils.join("Flow[", id, "is not defined."));

        Flow flow = new Flow();
        flow.setId(id);
        flow.setName(tFlow.getName());
        flow.setType(tFlow.getType());
        flow.setUserControl(!flowConfig.getNoUserControl().contains(tFlow.getType()));

        loadNodes(flow);
        loadLinks(flow);
        loadNodeProps(flow);

        return flow;
    }

    void loadNodes(Flow flow) {
        Map<String, Node> nodes = dao.list(TFlowNode.class, new ORQuery(ORQuery.Op.eq, "flowId", flow.getId()))
                .stream()
                .map(tn -> {
                    Node n = new Node();
                    n.setFlow(flow);
                    n.setId(tn.getId());
                    n.setName(tn.getName());
                    n.setType(tn.getType());
                    n.setX(tn.getX());
                    n.setY(tn.getY());
                    return n;
                }).collect(Collectors.toMap(Node::getId, n -> n));
        flow.setNodes(nodes);
    }

    void loadLinks(Flow flow) {
        dao.list(TFlowLink.class, new ORQuery(ORQuery.Op.eq, "flowId", flow.getId()))
                .stream()
                .forEach(tfl -> {
                    loadLink(flow, tfl);
                });
    }

    void loadLink(Flow flow, TFlowLink tfl) {
        Link l = new Link();
        Node node1 = flow.getNodes().get(tfl.getNode1());
        Node node2 = flow.getNodes().get(tfl.getNode2());
        Assert.noNullElements(new Node[]{node1, node2},
                () -> StringUtils.join("Load flow[", flow.getId(), "] link[", tfl.getNode1(), " -> ", tfl.getNode2(), "] failed."));
        l.setNode1(node1);
        l.setNode2(node2);
        l.setName(tfl.getName());
        l.setType(tfl.getType());
        l.setCondition(tfl.getCondition());

        node1.getLinks().add(l);
    }

    void loadNodeProps(Flow flow) {
        dao.list(TFlowNodeProp.class, new ORQuery(ORQuery.Op.eq, "flowId", flow.getId()))
                .stream()
                .forEach(tfnp -> {
                    Node node = flow.getNodes().get(tfnp.getNodeId());
                    if (node != null) {
                        node.getProps().put(tfnp.getName(), tfnp.getVal());
                    }
                });
    }

    public List<String> getFlowUserNode(String type) throws ServiceException {
        User user = ServiceContext.getInstance().getUser();
        if (null == user || StringUtils.isEmpty(user.getOrgcode()))
            throw  new ServiceException(ServiceException.NOAUTH);
        if (flowConfig.getNoUserControl().contains(type)) {
            List<ORQuery> querys = new ArrayList<>();
            querys.add(new ORQuery(ORQuery.Op.eq, "orgId", user.getOrgcode()));
            querys.add(new ORQuery(ORQuery.Op.eq, "uid", user.getUid()));
            querys.add(new ORQuery(ORQuery.Op.eq, "type", type));
            return dao.list(TOrgFlowNodeUser.class, querys)
                    .stream()
                    .map(nu -> nu.getNodeId())
                    .collect(Collectors.toList());
        }
        return null;
    }
}
