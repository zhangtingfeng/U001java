package com.hst.ii.company.ui;

import com.hst.core.annotation.Comment;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.company.entity.TCompanyLog;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EInfo(name = "company-log", title = "公司申请记录", entity = TCompanyLog.class, children = {"account-log", "member-log"})
public class CompanyLog extends MetaData {

    @EField(title = "ID", list = false, form = 0)
    private String id;

    @EField(title = "机构ID", list = false, form = 0)
    private String orgId;

    @EField(title = "公司ID", list = false, form = 0)
    private String companyId;

    @EField(title = "申请用户ID", list = false, form = 0)
    private String applyUserId;

    @EField(title = "企业名称", group = "企业信息", query = FieldQuery.LIKE, validate = "required", form = 2)
    private String companyName;

    @EField(title = "企业性质", viewer = "dict.companyType", editor = "select.companyType", form = 2)
    private String companyType;

    @EField(title = "邮政编码", form = 2)
    private String postcode;

    @EField(title = "企业座机", form = 2)
    private String phone;

    @EField(title = "企业传真", form = 2)
    private String fax;

//    @EField(title = "营业执照", editor = "upload.bizLicense")
//    private String bizLicense;

    @EField(title = "企业注册地址", form = 2)
    private String regAdr;

    @EField(title = "企业实际办公地址", validate = "required", form = 2)
    private String dtlAdr;

//    @EField(title = "开票资料", editor = "upload.billingInfo")
//    private String billingInfo;
//
//    @EField(title = "授权书", editor = "upload.authorization")
//    private String authorization;

    @EField(title = "企业签字人", group = "合同信息", form = 2)
    private String signer;

    @EField(title = "联系人", form = 2)
    private String contactor;

    @EField(title = "联系电话", form = 2)
    private String contactPhone;

    @EField(title = "联系地址", form = 2)
    private String contactAddress;

//    @EField(title = "业务参考号", group = "招行交易通信息")
//    private String yurRef;
//
//    @EField(title = "证件国别地区")
//    private String ctfCnr;
//
//    @EField(title = "证件类型")
//    private String prfTyp;
//
//    @EField(title = "证明号码")
//    private String prfNbr;
//
//    @EField(title = "统一社会信用代码")
//    private String csdCod;
//
//    @EField(title = "影像ID")
//    private String pctNbr;

//    @EField(title = "企业注册地址")
//    private String regAdr;
//
//    @EField(title = "企业实际办公地址")
//    private String dtlAdr;

//    @EField(title = "企业联系电话")
//    private String ctcTel;

    @EField(title = "状态", viewer = "dict.compLogStatus", editor = "select.compLogStatus", form = 2)
    private String status;

}
