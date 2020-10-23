package com.hst.ii.company.ui;

import com.hst.core.meta.annotation.EInfo;
import com.hst.ii.company.entity.TCompanyLog;
import com.hst.ii.company.proxy.CompanyApproveProxy;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EInfo(name = "company-approve", title = "交易商审核", entity = TCompanyLog.class, proxy = CompanyApproveProxy.class, children = {"member-log", "account-log"})
public class CompanyApprove extends CompanyLog {

}
