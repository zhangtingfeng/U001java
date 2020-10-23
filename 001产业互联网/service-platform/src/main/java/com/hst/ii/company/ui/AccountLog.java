package com.hst.ii.company.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.ii.company.entity.TAccountLog;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EInfo(name = "account-log", title = "公司账户申请记录", entity = TAccountLog.class, fk = "companyLogId = id")
public class AccountLog extends MetaData {

    @EField(title = "ID", list = false, form = 0)
    private String id;

    @EField(title = "机构ID", list = false, form = 0)
    private String orgId;

    @EField(title = "公司LogID", list = false, form = 0)
    private String companyLogId;

    @EField(title = "账户类型", viewer = "dict.rltTyp", editor = "select.rltTyp", form = 1)
    private String rltTyp;

    @EField(title = "账户", validate = "required", form = 1)
    private String rltAcc;

    @EField(title = "行内外标志", viewer = "dict.bnkFlg", editor = "select.bnkFlg", form = 1)
    private String bnkFlg;

    @EField(title = "账户户名", form = 1)
    private String rltAcm;

    @EField(title = "账户开户行行号", form = 1)
    private String rltBbn;

    @EField(title = "账户行名称", form = 1)
    private String rltBnk;

    @EField(title = "账户行地址", form = 1)
    private String rltAdr;
}
