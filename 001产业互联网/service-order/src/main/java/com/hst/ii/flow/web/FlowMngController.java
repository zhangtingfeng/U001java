package com.hst.ii.flow.web;

import com.hst.core.ServiceContext;
import com.hst.core.ServiceException;
import com.hst.core.annotation.WebAuth;
import com.hst.core.auth.User;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ORQuery;
import com.hst.ii.common.entity.TUser;
import com.hst.ii.flow.entity.TFlowProp;
import com.hst.ii.flow.entity.TFlowType;
import com.hst.ii.flow.entity.TOrgFlow;
import com.hst.ii.flow.entity.TOrgFlowNodeUser;
import com.hst.ii.flow.service.FlowConfigService;
import com.hst.ii.flow.service.FlowMngService;
import com.hst.ii.flow.service.FlowService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * FlowMngController
 *
 * @author WangYH
 * @date 2020/8/24
 */
@Controller
@RequestMapping("/flow-mng")
@WebAuth
public class FlowMngController {
    @Autowired
    FlowMngService flowMngService;

    @Autowired
    FlowConfigService flowConfigService;

    @Autowired
    IORDao dao;

    /**
     * 获取流程类型定义
     */
    @RequestMapping(value = "/types", method = RequestMethod.GET)
    void flowTypes(Model m) throws ServiceException {
        m.addAttribute("flowType", dao.list(TFlowType.class, new ORQuery(ORQuery.Op.order, "seq", "asc")));
    }


    /**
     * 获取流程类型定义
     */
    @RequestMapping(value = "/type/{type}", method = RequestMethod.GET)
    void flowType(@PathVariable String type, Model m) throws ServiceException {
        m.addAttribute("nodeProps", flowMngService.getFlowNodePropsConfig(type));
    }

    /**
     * 获取流程定义
     */
    @RequestMapping(value = "/flow/{id}", method = RequestMethod.GET)
    void flow(@PathVariable String id, Model m) throws ServiceException {
        m.addAttribute("flow", flowConfigService.load(id));
//        m.addAttribute("props", dao.list(TFlowProp.class, new ORQuery(ORQuery.Op.eq, "flowId", id)));
    }

    @RequestMapping(value = "/flow-users", method = RequestMethod.GET)
    void users(Model m) throws ServiceException {
        User user = ServiceContext.getInstance().getUser();
        if (StringUtils.isNotEmpty(user.getOrgcode())) {
            List<ORQuery> querys = new ArrayList<>();
            querys.add(new ORQuery(ORQuery.Op.eq, "orgId", user.getOrgcode()));
            m.addAttribute("flow-users",
                    dao.list(TUser.class, querys)
                            .stream()
                            .map(u -> {
                                Map<String, String> um = new HashMap<>();
                                um.put("id", u.getId());
                                um.put("text", u.getName());
                                return um;
                            }).collect(Collectors.toList()));
        } else
            throw new ServiceException(ServiceException.NOAUTH);
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    void getSettings(Model m) throws ServiceException {
        User user = ServiceContext.getInstance().getUser();
        if (StringUtils.isNotEmpty(user.getOrgcode())) {
            List<ORQuery> querys = new ArrayList<>();
            querys.add(new ORQuery(ORQuery.Op.eq, "orgId", user.getOrgcode()));
//            querys.add(new ORQuery(ORQuery.Op.eq, "isLocked", "0"));
            m.addAttribute("users",
                    dao.list(TUser.class, querys)
                            .stream()
                            .map(u -> {
                                Map<String, String> um = new HashMap<>();
                                um.put("id", u.getId());
                                um.put("text", StringUtils.join(u.getUserid(), ":", u.getName()));
                                return um;
                            }).collect(Collectors.toList()));
            m.addAttribute("flows",
                    dao.list(TOrgFlow.class, querys));
            m.addAttribute("nodes",
                    dao.list(TOrgFlowNodeUser.class, querys));
        } else
            throw new ServiceException(ServiceException.NOAUTH);
    }

    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    void settings(@RequestBody List<Map<String, Object>> settings, Model m) throws ServiceException {
        flowMngService.saveSettings(settings);
        m.addAttribute("rs", 1);
    }
}
