package com.hst.ii.company.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.ii.company.entity.TMember;
import com.hst.ii.company.entity.TMemberLog;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@EInfo(name = "member", title = "公司成员信息", entity = TMember.class, fk = "companyId = id")
public class Member extends MetaData {

    @EField(title = "ID", list = false, form = 0)
    private String id;

    @EField(title = "机构ID", list = false, form = 0)
    private String orgId;

    @EField(title = "公司ID", list = false, form = 0)
    private String companyId;

    @EField(title = "成员类型", viewer = "dict.mbrTyp", editor = "select.mbrTyp")
    private String mbrTyp;

    @EField(title = "成员姓名", validate = "required")
    private String mbrNam;

//    @EField(title = "国籍")
//    private String mbrNtn;

    @EField(title = "证件类型", viewer = "dict.docTyp", editor = "select.docTyp")
    private String docTyp;

    @EField(title = "证件号码")
    private String docNbr;

//    @EField(title = "证件有效期", editor = "date")
//    private Date docVld;

    @EField(title = "联系电话")
    private String mbrTel;

    @EField(title = "联系地址")
    private String mbrAdr;

}
