package com.hst.ii.company.ui;

import com.hst.core.meta.annotation.EInfo;
import com.hst.ii.company.entity.TCompanyLog;
import com.hst.ii.company.proxy.ApproveViewProxy;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EInfo(name = "approve-view", title = "审核结果查询", entity = TCompanyLog.class, proxy = ApproveViewProxy.class, children = {"member-log", "account-log"})
public class ApproveView extends CompanyLog {

}
