package com.hst.ii.company.service;

import com.hst.core.ServiceContext;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.dao.ORQuery;
import com.hst.ii.company.entity.*;
import com.hst.ii.org.entity.TOrgInfo;
import com.hst.ii.org.entity.TOrgRole;
import com.hst.ii.user.entity.TUser;
import com.hst.ii.user.entity.TUserRole;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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

    public void approve(TLogApproval tLogApproval) throws InvocationTargetException, IllegalAccessException {
        // 审核信息
        String userid = ServiceContext.getInstance().getUser().getUid();
        tLogApproval.setTime(new Date());
        tLogApproval.setOperator(userid);
        dao.save(tLogApproval);

        String companyLogId = tLogApproval.getCompanyLogId();
        TCompanyLog tCompanyLog = dao.get(TCompanyLog.class, companyLogId);
        // 审批通过
        if ("1".equals(tLogApproval.getResult())) {

            // 组织
            TOrgInfo tOrgInfo = new TOrgInfo();
            tOrgInfo.setName(tCompanyLog.getCompanyName());
            tOrgInfo.setFreeze("0");
            tOrgInfo.setCreateUserid(userid);
            tOrgInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
            dao.save(tOrgInfo);
            // 组织角色
            TOrgRole orgRole = new TOrgRole();
            orgRole.setOrgid(tOrgInfo.getId());
            orgRole.setRoleid("orgadmin");
            dao.save(orgRole);
            // 用户
            TUser tUser = dao.get(TUser.class, tCompanyLog.getApplyUserId());
            tUser.setOrgId(tOrgInfo.getId());
            tUser.setLocked("0");
            dao.update(tUser);
            // 用户角色
            TUserRole tUserRole = new TUserRole();
            tUserRole.setUserid(tUser.getId());
            tUserRole.setRoleid("casual");
            dao.delete(tUserRole);

            TUserRole tUserRole1 = new TUserRole();
            tUserRole1.setUserid(tUser.getId());
            tUserRole1.setRoleid("orgadmin");
            dao.save(tUserRole1);

            // 公司
            TCompany tCompany = new TCompany();
            BeanUtils.copyProperties(tCompany, tCompanyLog);
            tCompany.setOrgId(tOrgInfo.getId());
            tCompany.setId(null);
            dao.save(tCompany);
            // 成员
            List<ORQuery> querys = new ArrayList<>();
            querys.add(new ORQuery(ORQuery.Op.eq, "companyLogId", companyLogId));
            List<TMemberLog> tMemberLogList = dao.list(TMemberLog.class, querys);
            for (TMemberLog tMemberLog : tMemberLogList) {
                TMember tMember = new TMember();
                BeanUtils.copyProperties(tMember, tMemberLog);
                tMember.setCompanyId(tCompany.getId());
                tMember.setId(null);
                dao.save(tMember);
            }
            // 账户
            List<TAccountLog> tAccountLogList = dao.list(TAccountLog.class, querys);
            for (TAccountLog tAccountLog : tAccountLogList) {
                TAccount tAccount = new TAccount();
                BeanUtils.copyProperties(tAccount, tAccountLog);
                tAccount.setCompanyId(tCompany.getId());
                tAccount.setId(null);
                dao.save(tAccount);
            }

            // 申请
            tCompanyLog.setStatus("2");
            tCompanyLog.setOrgId(tOrgInfo.getId());
            tCompanyLog.setCompanyId(tCompany.getId());
            dao.update(tCompanyLog);

            // 审批不通过
        } else if ("2".equals(tLogApproval.getResult())) {
            tCompanyLog.setStatus("3");
            dao.update(tCompanyLog);
        }
    }

    public TLogApproval approveInfo(String companyLogId) {
        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(ORQuery.Op.eq, "companyLogId", companyLogId));
        List<TLogApproval> tLogApprovalList = dao.list(TLogApproval.class, querys);
        if (tLogApprovalList.size() > 0) {
            return tLogApprovalList.get(0);
        } else {
            return new TLogApproval();
        }
    }
}
