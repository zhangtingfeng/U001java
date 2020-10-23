package com.hst.ii.user.web;

import com.hst.core.ServiceContext;
import com.hst.core.auth.User;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.company.entity.TCompanyLog;
import com.hst.ii.org.entity.TOrgInfo;
import com.hst.ii.sys.entity.TUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/9/11 17:42
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    @RequestMapping("/userInfo")
    public void userInfo(Model m) throws IOException {
        User u = ServiceContext.getInstance().getUser();
        if (u == null) {
            return;
        }
        TUser user = dao.get(TUser.class, u.getUid());
        if (user == null) {
            return;
        }
        m.addAttribute("user", JsonUtil.toString(user));

        if (StringUtils.isNotBlank(user.getOrgId())) {
            TOrgInfo orgInfo = dao.get(TOrgInfo.class, user.getOrgId());
            m.addAttribute("orgInfo", JsonUtil.toString(orgInfo));
        } else {
            List<ORQuery> querys = new ArrayList<>();
            querys.add(new ORQuery(ORQuery.Op.eq, "applyUserId", u.getUid()));
            List<TCompanyLog> tCompanyLogList = dao.list(TCompanyLog.class, querys);
            if (tCompanyLogList.size() > 0) {
                m.addAttribute("compInfo", JsonUtil.toString(tCompanyLogList.get(0)));
            }
        }

    }
}
