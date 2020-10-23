package com.hst.ii.flow.web;

import com.hst.core.ServiceException;
import com.hst.core.annotation.WebAuth;
import com.hst.ii.flow.po.Flow;
import com.hst.ii.flow.service.FlowConfigService;
import com.hst.ii.flow.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * FlowController
 *
 * @author WangYH
 * @date 2020/8/24
 */
@Controller
@RequestMapping("/flow")
@WebAuth
public class FlowController {
    @Autowired
    FlowConfigService flowConfigService;

    @Autowired
    FlowService flowService;

    /**
     * 获取流程定义
     */
    @RequestMapping(value = "/def/{id}", method = RequestMethod.GET)
    void flow(@PathVariable String id, Model m) throws ServiceException {
        Flow flow = flowConfigService.getFlow(id);
        if (flow.isUserControl()) {
            flow.setUserNodes(flowConfigService.getFlowUserNode(flow.getType()));
        }
        m.addAttribute("flow", flow);
    }

    /**
     * 获取流程实例信息
     */
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    void idx(@PathVariable String id, Model m) throws ServiceException {
        m.addAllAttributes(flowService.getFlowProcess(id, true,true));
    }

    /**
     * 流程流转  http://localhost:92/api/order/flow/208U000007?next=o_signed1
     *
     * @param id
     * @param next
     * @param m
     * @throws ServiceException
     */
    @RequestMapping(value="/{id}",method = RequestMethod.POST)
    void submit(@PathVariable String id, @RequestBody(required = false) Map<String, String> data,
                String next, String remark, Model m) throws ServiceException {
        if (data != null){
            if (data.containsKey("next")){
                next = data.get("next");
            }
            if (data.containsKey("remark")){
                remark = data.get("remark");
            }
        }
        try {
            flowService.proc(id, next, remark);
            m.addAttribute("rs", "0");
        } catch (IllegalArgumentException ex) {
            throw new ServiceException(ServiceException.INTERNAL, ex.getMessage());
        }
    }
}
