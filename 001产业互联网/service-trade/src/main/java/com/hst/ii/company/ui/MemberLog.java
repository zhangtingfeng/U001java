package com.hst.ii.company.ui;

import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.ii.company.entity.TMemberLog;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@EInfo(name = "member-log", title = "公司成员申请记录", entity = TMemberLog.class, fk = "companyLogId = id")
public class MemberLog extends MetaData {

    @EField(title = "ID", list = false, form = 0)
    private String id;

    @EField(title = "机构ID", list = false, form = 0)
    private String orgId;

    @EField(title = "公司LogID", list = false, form = 0)
    private String companyLogId;

    @EField(title = "成员类型", viewer = "dict.mbrTyp", editor = "select.mbrTyp", validate = "required")
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

    @EField(title = "联系电话", validate = "required")
    private String mbrTel;

    @EField(title = "联系地址")
    private String mbrAdr;

}
