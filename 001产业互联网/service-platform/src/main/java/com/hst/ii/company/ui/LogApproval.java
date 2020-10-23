package com.hst.ii.company.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.ii.company.entity.TLogApproval;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@EInfo(name = "log-approval", title = "申请审核", entity = TLogApproval.class)
public class LogApproval extends MetaData {

    @EField(title = "ID", list = false, form = 0)
    private String id;

    @EField(title = "公司LogID", list = false, form = 0)
    private String companyLogId;

    @EField(title = "审批时间")
    private Date time;

    @EField(title = "审批人员")
    private String operator;

    @EField(title = "审批结果")
    private String result;

    @EField(title = "审批意见")
    private String opinions;

}
