package com.hst.ii.company.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 企业账户信息(包含招行交易通要求)
 *
 * @description:
 * @author: ZhaJun
 * @time: 2020/8/20 10:20
 */
@Entity
@Table(name = "comp_account_log")
@Comment("企业账户信息")
@HSID(length = 10, prefix = "A")
@Data
@NoArgsConstructor
public class TAccountLog implements Serializable {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("ID")
    private String id;

    @Column(name = "id_org", length = 10)
    @Comment("机构ID")
    private String orgId;

    @Column(name = "id_company_log", length = 10)
    @Comment("公司LogID")
    private String companyLogId;

    @Column(name = "rlt_typ", length = 1)
    @Comment("绑定账户类型")
    private String rltTyp;

    @Column(name = "rlt_acc", length = 35)
    @Comment("绑定账户")
    private String rltAcc;

    @Column(name = "bnk_flg", length = 1)
    @Comment("行内外标志 Y：招行,N：他行")
    private String bnkFlg;

    @Column(name = "rlt_acm", length = 200)
    @Comment("绑定账户户名 他行必输 招行：如果输入了校验")
    private String rltAcm;

    @Column(name = "rlt_bbn", length = 20)
    @Comment("绑定账户开户行行号")
    private String rltBbn;

    @Column(name = "rlt_bnk", length = 62)
    @Comment("绑定账户行名称 他行必输 招行：不校验")
    private String rltBnk;

    @Column(name = "rlt_adr", length = 62)
    @Comment("绑定账户行地址 他行必输 招行：不校验")
    private String rltAdr;

}
