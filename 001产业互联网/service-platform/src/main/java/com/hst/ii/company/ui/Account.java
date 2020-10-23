package com.hst.ii.company.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.ii.company.entity.TAccount;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EInfo(name = "account", title = "公司账户信息", entity = TAccount.class, fk = "companyId = id")
public class Account extends MetaData {

    @EField(title = "ID", list = false, form = 0)
    private String id;

    @EField(title = "机构ID", list = false, form = 0)
    private String orgId;

    @EField(title = "公司ID", list = false, form = 0)
    private String companyId;

    @EField(title = "账户类型", viewer = "dict.rltTyp", editor = "select.rltTyp")
    private String rltTyp;

    @EField(title = "账户", validate = "required")
    private String rltAcc;

    @EField(title = "行内外标志", viewer = "dict.bnkFlg", editor = "select.bnkFlg")
    private String bnkFlg;

    @EField(title = "账户户名")
    private String rltAcm;

    @EField(title = "账户开户行行号")
    private String rltBbn;

    @EField(title = "账户行名称")
    private String rltBnk;

    @EField(title = "账户行地址")
    private String rltAdr;
}
