package com.hst.ii.company.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 企业成员信息(包含招行交易通要求)
 *
 * @description:
 * @author: ZhaJun
 * @time: 2020/8/20 10:20
 */
@Entity
@Table(name = "comp_member")
@Comment("企业成员信息")
@HSID(length = 10, prefix = "M")
@Data
@NoArgsConstructor
public class TMember implements Serializable {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("ID")
    private String id;

    @Column(name = "id_company", length = 10)
    @Comment("公司ID")
    private String companyId;

    @Column(name = "id_org", length = 10)
    @Comment("机构ID")
    private String orgId;

    @Column(name = "mbr_typ", length = 35)
    @Comment("成员类型 要求 1:法人，2:经办人信息必输 （1:法人 2:经办人 3:支付联系人 4:受益所有人 5:股东 6:高管 7:财务联系人）")
    private String mbrTyp;

    @Column(name = "mbr_nam", length = 200)
    @Comment("成员姓名")
    private String mbrNam;

    @Column(name = "mbr_ntn", length = 3)
    @Comment("国籍")
    private String mbrNtn;

    @Column(name = "doc_typ", length = 3)
    @Comment("证件类型 P01:居民身份证,P03:临时居民身份证,P20:港澳内地通行证,P21:台湾大陆通行证,P31:护照,P02:学生证,P04:军人证,P08:武警身份证,P16:居民户口簿,P22:监护人证件,P23:居住证,P24:暂住证,P99:个人其他证件")
    private String docTyp;

    @Column(name = "doc_nbr", length = 20)
    @Comment("证件号码")
    private String docNbr;

    @Column(name = "doc_vld")
    @Comment("证件有效期（截止日）")
    private Date docVld;

    @Column(name = "mbr_tel", length = 20)
    @Comment("联系电话")
    private String mbrTel;

    @Column(name = "mbr_adr", length = 200)
    @Comment("联系地址")
    private String mbrAdr;

}
