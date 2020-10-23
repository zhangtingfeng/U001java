package com.hst.ii.flow.service;

import com.hst.core.dao.IORDao;
import com.hst.core.meta.IMetaUpdate;
import com.hst.core.meta.MetaConvert;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.MetaRepository;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.flow.dao.IFlowDao;
import com.hst.ii.flow.entity.*;
import com.hst.ii.flow.ui.Flow;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * FlowMetaUpdate
 *
 * @author WangYH
 * @date 2020/9/3
 */
@Service
@Transactional
@Qualifier("flow")
public class FlowMetaUpdate implements IMetaUpdate {
    static String NAME = "flow";
    static String NAME2 = "flowData";

    @Autowired
    MetaRepository repo;

    @Autowired
    IORDao dao;

    @Autowired
    IFlowDao flowDao;

    @Override
    public Map<String, List<?>> update(String name, String data) throws Exception {
        Map map = JsonUtil.read(data, Map.class);
        List<Flow> flows = updateFlow((List<Map<String, Object>>) map.get(NAME));

        if (map.containsKey(NAME2)) {
            Flow flow = flows.get(0);
            updateFlowData(flow, (Map<String, ?>) map.get(NAME2));
        }
        HashMap<String, List<?>> rs = new HashMap<>();
        rs.put(NAME, flows);
        return rs;
    }

    List<Flow> updateFlow(List<Map<String, Object>> flows) {
        List<Flow> datas = flows.stream().map(f -> {
            Flow d = new Flow();
            d.setId((String) f.get("id"));
            d.setName((String) f.get("name"));
            d.setType((String) f.get("type"));
            d.setVer((String) f.get("ver"));
            if (f.containsKey("$op"))
                d.setOp((Integer) f.get("$op"));
            return d;
        }).collect(Collectors.toList());

        MetaConvert convert = repo.getMetaConvert(NAME);
        datas.stream().forEach(f -> {
            TFlow tf = (TFlow) convert.toEntity(f);
            switch (f.getOp()) {
                case MetaData.OP_DEL:
                    dao.delete(tf);
                    break;
                case MetaData.OP_UPD:
                    dao.update(tf);
                    break;
                case MetaData.OP_NEW:
                    dao.save(tf);
                    break;
            }
            f.setId(tf.getId());
        });

        return datas;
    }

    void updateFlowData(Flow flow, Map<String, ?> flowData) {
        updateNodes(flow, (List<Map>) flowData.get("nodes"));
        updateLinks(flow, (List<Map>) flowData.get("links"));
        updateProps(flow, (List<Map>) flowData.get("props"));
    }

    void updateNodes(Flow flow, List<Map> datas) {
        flowDao.delNodes(flow.getId());
        flowDao.delNodeProps(flow.getId());

        datas.stream().forEach(m -> {
            TFlowNode node = new TFlowNode();
            node.setFlowId(flow.getId());
            node.setId((String)m.get("id"));
            node.setName((String) m.get("name"));
            node.setType((String) m.get("type"));
            node.setX((Integer) m.get("x"));
            node.setY((Integer) m.get("y"));
            dao.save(node);

            Map<String, String> props = (Map<String, String>) m.get("props");
            if (null != props) {
                for (Map.Entry<String, String> entry : props.entrySet()) {
                    TFlowNodeProp nodeProp = new TFlowNodeProp();
                    nodeProp.setFlowId(node.getFlowId());
                    nodeProp.setNodeId(node.getId());
                    nodeProp.setName(entry.getKey());
                    nodeProp.setVal(entry.getValue());
                    dao.save(nodeProp);
                }
            }
        });
    }

    void updateLinks(Flow flow, List<Map> datas) {
        flowDao.delLinks(flow.getId());

        if (null != datas) {
            datas.stream().forEach(m -> {
                TFlowLink link = new TFlowLink();
                link.setFlowId(flow.getId());
                link.setName((String) m.get("name"));
                link.setNode1((String) m.get("nodeId1"));
                link.setNode2((String) m.get("nodeId2"));
                link.setType((String) m.get("type"));
                link.setCondition((String) m.get("condition"));
                dao.save(link);
            });
        }
    }

    void updateProps(Flow flow, List<Map> datas) {
        flowDao.delProps(flow.getId());

        if (datas != null) {
            datas.stream().forEach(m -> {
                TFlowProp node = new TFlowProp();
                node.setFlowId(flow.getId());
                node.setId((String) m.get("id"));
                node.setName((String) m.get("name"));
                dao.save(node);
            });
        }
    }
}
