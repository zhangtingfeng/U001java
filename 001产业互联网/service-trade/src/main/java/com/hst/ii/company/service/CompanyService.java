package com.hst.ii.company.service;

import com.hst.core.ServiceContext;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.ii.company.entity.TAccountLog;
import com.hst.ii.company.entity.TCompanyLog;
import com.hst.ii.company.entity.TMemberLog;
import com.hst.ii.sys.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/9/1 14:37
 */
@Service
@Transactional(rollbackOn = Throwable.class)
public class CompanyService {

    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;

    @Autowired
    ProxyService proxyService;

    public void apply(TCompanyLog tCompanyLog, List<TMemberLog> tMemberLogList, List<TAccountLog> tAccountLogList) {
        tCompanyLog.setApplyUserId(ServiceContext.getInstance().getUser().getUid());
        tCompanyLog.setStatus("1");// 申请中
        dao.save(tCompanyLog);

        String companyLogId = tCompanyLog.getId();
        for (TMemberLog tMemberLog : tMemberLogList) {
            tMemberLog.setCompanyLogId(companyLogId);
            dao.save(tMemberLog);
        }

        for (TAccountLog tAccountLog : tAccountLogList) {
            tAccountLog.setCompanyLogId(companyLogId);
            dao.save(tAccountLog);
        }

//        String userid = ServiceContext.getInstance().getUser().getUid();
//        TUser user = dao.get(TUser.class, userid);
//        user.setLocked("1");
//        dao.update(user);//申请完成锁定用户
    }
}
