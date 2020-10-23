package com.hst.ii.company.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 企业基本信息(包含招行交易通要求)
 *
 * @description:
 * @author: ZhaJun
 * @time: 2020/8/20 10:20
 */
@Entity
@Table(name = "comp_info")
@Comment("企业基本信息")
@HSID(length = 10, prefix = "C")
@Data
@NoArgsConstructor
public class TCompany implements Serializable {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("ID")
    private String id;

    @Column(name = "id_org", length = 10)
    @Comment("机构ID")
    private String orgId;

    @Column(name = "company_name", length = 200)
    @Comment("企业名称")
    private String companyName;

    @Column(name = "company_type", length = 2)
    @Comment("企业性质")
    private String companyType;

    @Column(length = 30)
    @Comment("邮政编码")
    private String postcode;

    @Column(length = 30)
    @Comment("企业座机")
    private String phone;

    @Column(length = 30)
    @Comment("企业传真")
    private String fax;

    @Column(length = 30)
    @Comment("企业签字人")
    private String signer;

    @Column(length = 30)
    @Comment("联系人")
    private String contactor;

    @Column(name = "contact_phone", length = 30)
    @Comment("联系电话")
    private String contactPhone;

    @Column(name = "contact_address", length = 50)
    @Comment("联系地址")
    private String contactAddress;

    @Column(name = "biz_license", length = 200)
    @Comment("营业执照")
    private String bizLicense;

    @Column(name = "billing_info", length = 200)
    @Comment("开票资料")
    private String billingInfo;

    @Column(length = 200)
    @Comment("授权书")
    private String authorization;

    @Column(name = "yur_ref", length = 30)
    @Comment("业务参考号(交易通)")
    private String yurRef;

    @Column(name = "ctf_cnr", length = 3)
    @Comment("证件国别地区 CHN：中国")
    private String ctfCnr;

    @Column(name = "prf_typ", length = 3)
    @Comment("证件类型 C01 营业执照")
    private String prfTyp;

    @Column(name = "prf_nbr", length = 20)
    @Comment("证明号码(？统一社会信用代码)")
    private String prfNbr;

    @Column(name = "csd_cod", length = 30)
    @Comment("统一社会信用代码")
    private String csdCod;

    @Column(name = "pct_nbr", length = 40)
    @Comment("影像ID")
    private String pctNbr;

    @Column(name = "reg_adr", length = 200)
    @Comment("企业注册地址")
    private String regAdr;

    @Column(name = "dtl_adr", length = 200)
    @Comment("企业实际办公地址")
    private String dtlAdr;

    @Column(name = "ctc_tel", length = 20)
    @Comment("企业联系电话")
    private String ctcTel;

}
