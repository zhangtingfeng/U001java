package com.hst.ii.fee.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.fee.service.FeeProcessProxy;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * FeeProcess
 * 费用审批流程对象
 * @author WangYH
 * @date 2020/9/5
 */
@EInfo(name = "fee-process", title = "费用审批流程|Fee flow", proxy = FeeProcessProxy.class)
@Data
@NoArgsConstructor
public class FeeProcess extends MetaData {
    @EField(title="ID")
    private String id;

    @EField(list =false)
    private String processId;

    @EField(title="标题", query = FieldQuery.LIKE)
    private String title;

    @EField(title="付款类型")
    private String type;

    @EField(title="付款金额")
    private Double amount;

    @EField(title="付款时间")
    private Date paymentDt;

    @EField(title="收款机构")
    private String receiptOrgId;

    @EField(title="收款机构")
    private String receiptOrg;

    @EField(title="状态")
    private String status;
}
