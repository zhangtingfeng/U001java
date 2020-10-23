package com.hst.ii.flow.service;

import com.hst.core.ServiceContext;
import com.hst.core.ServiceException;
import com.hst.core.auth.User;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ORQuery;
import com.hst.ii.flow.dao.IFlowDao;
import com.hst.ii.flow.entity.TFlowNodePropConfig;
import com.hst.ii.flow.entity.TOrgFlow;
import com.hst.ii.flow.entity.TOrgFlowNodeUser;
import com.hst.ii.flow.po.NodePropConfig;
import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultMapEntry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * FlowMngService
 *
 * @author WangYH
 * @date 2020/9/2
 */
@Service
@Transactional
public class FlowMngService {
    @Autowired
    IORDao dao;

    @Autowired
    IFlowDao flowDao;

    public List<NodePropConfig> getFlowNodePropsConfig(String type) {
        return dao.list(TFlowNodePropConfig.class, new ORQuery(ORQuery.Op.eq, "flowType", type))
                .stream()
                .sorted((a, b)->a.getSeq() - b.getSeq())
                .map(p->new NodePropConfig(p.getId(), p.getName(), p.getEditor(), p.getExtArgs()))
                .collect(Collectors.toList());
    }

    public void saveSettings(List<Map<String, Object>> datas) throws ServiceException {
        for (Map<String, Object> d: datas)
            saveSetting(d);
    }

    public void saveSetting(Map<String, Object> data) throws ServiceException {
        User user = ServiceContext.getInstance().getUser();
        if (null == user || StringUtils.isEmpty(user.getOrgcode()))
            throw new ServiceException(ServiceException.NOAUTH);

        TOrgFlow orgFlow = new TOrgFlow();
        orgFlow.setOrgId(user.getOrgcode());
        orgFlow.setFlowType((String) data.get("type"));
        orgFlow.setFlowId((String) data.get("flow"));
        flowDao.delOrgFlow(orgFlow.getOrgId(), orgFlow.getFlowType());
        dao.save(orgFlow);

        flowDao.delOrgFlowUser(orgFlow.getOrgId(), orgFlow.getFlowType());

        List<Map<String, String>> nodes = (List<Map<String, String>>) data.get("nodes");
        for(Map<String, String> node : nodes){
            String users = node.get("users");
            for(String uid : users.split(",")){
                TOrgFlowNodeUser nodeUser = new TOrgFlowNodeUser();
                nodeUser.setOrgId(orgFlow.getOrgId());
                nodeUser.setType(orgFlow.getFlowType());
                nodeUser.setNodeId(node.get("id"));
                nodeUser.setUid(uid);
                dao.save(nodeUser);
            }
        }
    }
}
