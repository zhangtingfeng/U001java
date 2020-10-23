package com.hst.ii;

import com.hst.core.ServiceContext;
import com.hst.core.ServiceException;
import com.hst.core.auth.User;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ORQuery;
import com.hst.ii.flow.entity.TFlowLink;
import com.hst.ii.flow.entity.TOrgFlow;
import com.hst.ii.flow.po.Flow;
import com.hst.ii.flow.service.FlowConfigService;
import com.hst.ii.flow.service.FlowService;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * AppTest
 *
 * @author WangYH
 * @date 2020/8/22
 */
@SpringBootTest
@ActiveProfiles("vpn")
public class AppTest {
    @Autowired
    IORDao dao;

    @Autowired
    FlowService flowService;

    @Autowired
    FlowConfigService flowConfigService;

    @PostConstruct
    void init() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        ServiceContext serviceContext = ServiceContext.getInstance();
        Class clz = ServiceContext.class;
        Field f = clz.getDeclaredField("userLoaded");
        f.setAccessible(true);
        f.setBoolean(serviceContext, true);

        User user = new User();
        user.setUid("0000000001");
        user.setOrgcode("ORG208F001");
        user.setName("admin");

        f = clz.getDeclaredField("user");
        f.setAccessible(true);
        f.set(serviceContext, user);
    }

    @Test
    public void test1() throws ServiceException {
        String flowId = flowConfigService.getOrgFlow("fee");
        String processId = flowService.start(flowId, "abcd12345");
        flowService.autoProc(processId);
    }
}
