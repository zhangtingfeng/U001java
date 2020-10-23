package com.hst.ii.flow.service;

import com.googlecode.aviator.AviatorEvaluator;
import com.hst.core.ServiceContext;
import com.hst.core.ServiceException;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.dao.ORQuery.Op;
import com.hst.ii.flow.entity.*;
import com.hst.ii.flow.po.Flow;
import com.hst.ii.flow.po.Link;
import com.hst.ii.flow.po.Node;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * FlowService 流程流转处理
 *
 * @author WangYH
 * @date 2020/8/26
 */
@Service
@Transactional
public class FlowService {
    Map<String, IFlowLinkProc> flowProces = new HashMap<>();

    @Autowired
    FlowConfigService flowConfigService;

    @Autowired
    IORDao dao;

    @Autowired(required = false)
    void setFlowProces(List<IFlowLinkProc> proces) {
        flowProces = proces.stream().collect(Collectors.toMap(p -> {
            Qualifier q = p.getClass().getAnnotation(Qualifier.class);
            return q.value();
        }, p -> p));
    }

    /**
     * 检查link的条件
     *
     * @param condition
     * @param props
     */
    public boolean checkLinkCondition(String condition, Map<String, Object> props) {
        if (StringUtils.isEmpty(condition))
            return true;

        Object o = AviatorEvaluator.execute(condition, (Map<String, Object>) props);
        if (o == null) {
            return false;
        } else {
            Class clz = o.getClass();
            if (clz.equals(Boolean.class)) {
                return (Boolean) o;
            } else if (clz.equals(String.class)) {
                return "1".equals(o) || "true".equalsIgnoreCase((String) o);
            } else if (clz.equals(Long.class)) {
                return 0 != (Long) o;
            } else if (clz.equals(Double.class)) {
                return 0 != (Double) o;
            }
        }
        return false;
    }

    /**
     * 开始新流程
     *
     * @param flowId
     * @param serviceId
     * @return 流程实例ID
     */
    public String start(String flowId, String serviceId) throws ServiceException {
        // 1. 获取流程
        Flow flow = flowConfigService.getFlow(flowId);

        // 2. 获取开始节点
        Node nodeStart = flow.getNodes().values().stream().filter(n -> TFlowNode.TYPE_START.equals(n.getType()))
                .findFirst().get();

        // 更新数据库，并且返回流程实例ID
        return updateStartDb(flow, serviceId, nodeStart).getId();
    }

    TFlowProcess updateStartDb(Flow flow, String serviceId, Node nodeStart) {
        // 3. 更新到数据库
        TFlowProcess process = new TFlowProcess();
        process.setFlowId(flow.getId());
        process.setServiceId(serviceId);
        process.setOrgId(ServiceContext.getInstance().getOrg());
        process.setNodeId(nodeStart.getId());
        process.setNodeUserid(ServiceContext.getInstance().getUid());
        process.setCreateTime(new Timestamp(System.currentTimeMillis()));
        dao.save(process);

        TFlowProcessHist hist = new TFlowProcessHist();
        hist.setProcessId(process.getId());
        hist.setNodeId(nodeStart.getId());
        hist.setNodeUserid(process.getNodeUserid());
        hist.setNodeDt(new Date());
        hist.setCreateTime(new Timestamp(System.currentTimeMillis()));
        dao.save(hist);

        process.setHistId(hist.getId());
        dao.update(process);

        return process;
    }

    /**
     * 流程流转到下一个节点
     *
     * @param processId 流程ID
     * @param nextNode  下一个节点ID
     */
    public void proc(String processId, String nextNode) throws ServiceException {
        this.proc(processId, nextNode, "");
    }

    /**
     * 流程流转到下一个节点
     *
     * @param processId 流程ID
     * @param nextNode  下一个节点ID
     * @param remark    说明
     */
    public void proc(String processId, String nextNode, String remark) throws ServiceException {
        // 1. 获取流程实列信息
        TFlowProcess process = dao.get(TFlowProcess.class, processId);

        // 2. 获取流程
        Flow flow = flowConfigService.getFlow(process.getFlowId());

        // 3. 检查当前用户是否可以执行
        Node node = flow.getNodes().get(process.getNodeId());
        checkNode(flow, node);

        // 4. 查找处理的Link
        Optional<Link> link = node.getLinks().stream().filter(l -> nextNode.equals(l.getNodeId2())).findFirst();
        Assert.isTrue(link.isPresent(), () -> String.format("Flow[%s] is not define the link of [%s => %s]",
                process.getFlowId(), process.getNodeId(), nextNode));

        // 5. 检查link的条件是否满足
        checkLink(process, flow, link.get());

        // 6. 获取流程流转处理业务对象进行预处理
        IFlowLinkProc flowProc = flowProces.get(flow.getType());
        if (null != flowProc)
            flowProc.prevProc(process, flow, link.get());

        // 7. 更新数据库
        updateProcDb(process, link.get(), remark);

        // 8. 业务后处理
        if (null != flowProc)
            flowProc.postProc(process, flow, link.get());
    }

    private void checkNode(Flow flow, Node node) throws ServiceException {
        if (flow.isUserControl() && TFlowNode.TYPE_ACTIVITY.equals(node.getType())) {
            TOrgFlowNodeUser onu = new TOrgFlowNodeUser();
            onu.setOrgId(ServiceContext.getInstance().getOrg());
            onu.setType(flow.getType());
            onu.setNodeId(node.getId());
            onu.setUid(ServiceContext.getInstance().getUid());
            if (null == dao.get(TOrgFlowNodeUser.class, onu))
                throw new ServiceException(ServiceException.NOAUTH);
        }
    }

    private void checkLink(TFlowProcess process, Flow flow, Link link) throws ServiceException {
        if (StringUtils.isEmpty(link.getCondition()))
            return;

        Map<String, Object> props = getFlowProps(process.getId());
        if (!checkLinkCondition(link.getCondition(), props)) {
            throw new ServiceException("condition");
        }
    }


    private void updateProcDb(TFlowProcess process, Link link, String remark) {
        String uid = ServiceContext.getInstance().getUid();

        // 1. 更新节点当前历史
        TFlowProcessHist hist = dao.get(TFlowProcessHist.class, process.getHistId());
        hist.setNodeUserid(process.getNodeUserid());
        hist.setNodeDt(new Date());
        hist.setRemark(remark);
        hist.setNextNode(link.getNodeId2());
        hist.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        dao.update(hist);

        // 2. 增加新的处理历史
        hist = new TFlowProcessHist();
        hist.setProcessId(process.getId());
        hist.setPrevNodeDt(new Date());
        hist.setPrevNodeId(link.getNodeId1());
        hist.setPrevNodeUserid(uid);
        hist.setNodeId(link.getNodeId2());
        hist.setNodeUserid(process.getNodeUserid());
        hist.setNodeDt(new Date());
        hist.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        dao.save(hist);

        // 3. 更新流程信息
        process.setHistId(hist.getId());
        process.setPrevNodeDt(hist.getPrevNodeDt());
        process.setPrevNodeId(hist.getPrevNodeId());
        process.setPrevNodeUserid(uid);
        process.setPrevNodeRemark(remark);
        process.setNodeId(link.getNodeId2());
        process.setNodeUserid(uid);
        process.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        //process.setNodeDt(new Date());
        //hist.setNodeDt(new Date());
        dao.update(process);
    }

    /**
     * 获取流程实例的属性
     *
     * @param processId
     * @return
     */
    public Map<String, Object> getFlowProps(String processId) {
        return dao.list(TFlowProcessProp.class, new ORQuery(ORQuery.Op.eq, "id", processId)).stream()
                .collect(Collectors.toMap(p -> p.getName(), p -> p.getVal()));
    }

    /**
     * 设置流程属性
     *
     * @param processId
     * @param name
     * @param val
     */
    public void setFlowProp(String processId, String name, String val) {
        TFlowProcessProp prop = new TFlowProcessProp();
        prop.setId(processId);
        prop.setName(name);
        TFlowProcessProp prop1 = dao.get(TFlowProcessProp.class, prop);
        if (null != prop1) {
            prop1.setVal(val);
            dao.update(prop1);
        } else {
            prop.setVal(val);
            dao.save(prop);
        }
    }

    /**
     * 获取流程实例信息
     *
     * @param processId
     * @param includeLinks
     * @param includeHist
     * @return
     */
    public Map<String, Object> getFlowProcess(String processId, boolean includeLinks, boolean includeHist) {
        Map<String, Object> data = new HashMap<>();
        TFlowProcess flowProcess = dao.get(TFlowProcess.class, processId);
        if (flowProcess != null) {
            data.put("process", flowProcess);

            // node links
            if (includeLinks) {
                data.put("links", getFlowProcessLink(flowProcess.getId(), flowProcess.getFlowId(), flowProcess.getNodeId()));
            }
            if (includeHist) {
                data.put("hists", getFlowProcessHists(processId));
            }
        }
        return data;
    }

    /**
     * 获取流程当前节点对应的link的条件
     * @param processId
     * @param flowId
     * @param nodeId
     * @return
     */
    public Map<String, Boolean> getFlowProcessLink(String processId, String flowId, String nodeId) {
        Map<String, Object>[] props = new Map[1];
        List<ORQuery> querys = new ArrayList<ORQuery>();
        querys.add(new ORQuery(Op.eq, "flowId", flowId));
        querys.add(new ORQuery(Op.eq, "node1", nodeId));
        return dao.list(TFlowLink.class, querys)
                .stream()
                .collect(Collectors.toMap(TFlowLink::getNode2,
                        fl -> {
                            if (StringUtils.isEmpty(fl.getCondition())) {
                                return true;
                            } else {
                                if (props[0] == null) {
                                    props[0] = getFlowProps(processId);
                                }
                                return checkLinkCondition(fl.getCondition(), props[0]);
                            }
                        }
                ));
    }

    /**
     * 获取流程执行历史情况
     * @param processId
     * @return
     */
    public List<TFlowProcessHist> getFlowProcessHists(String processId){
        return dao.list(TFlowProcessHist.class, new ORQuery(Op.eq, "processId", processId)).stream()
            .sorted((h1, h2) -> {
                if (h1.getNodeDt() == null)
                    return -1;
                if (h2.getNodeDt() == null)
                    return -1;
                return h1.getNodeDt().compareTo(h2.getNodeDt());
            }).collect(Collectors.toList());
    }

    public String autoProc(String processId) throws ServiceException {
        // 1. 获取流程实列信息
        TFlowProcess process = dao.get(TFlowProcess.class, processId);

        // 2. 找到流程当前节点
        Flow flow = flowConfigService.getFlow(process.getFlowId());
        Optional<Node> nodeOptional = flow.getNodes().values().stream().filter(n -> n.getId().equals(process.getNodeId())).findFirst();
        Node node = nodeOptional.get();

        // 3. 获取node的唯一link
        if (node.getLinks().size() != 1)
            throw new ServiceException(ServiceException.INTERNAL, String.format("Node[%s.%s] has more than one links", flow.getId(), node.getId()));
        String next = node.getLinks().get(0).getNodeId2();

        // 4. 自动流转
        this.proc(processId, next);

        return next;
    }
}
