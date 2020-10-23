package com.hst.ii.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @description:机构收货地址表
 * @author: ZhaJun
 * @time: 2020/9/8 11:08
 */
@Entity
@Table(name = "sys_org_rcv_addr")
@Comment("机构收货地址表")
@HSID(length = 10, prefix = "R")
@Data
@NoArgsConstructor
public class TOrgReceiveAddress extends BaseEntity implements Serializable {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("ID")
    private String id;

    @Comment("机构id")
    @Column(name = "id_org", length = 10)
    private String orgId;

    @Column(length = 20)
    @Comment("收件人")
    private String name;

    @Column(length = 20)
    @Comment("联系方式")
    private String tel;

    @Column(length = 10)
    @Comment("收货地址区域")
    private String area;

    @Column(length = 100)
    @Comment("收货地址详细")
    private String address;

    @Column(length = 2)
    @Comment("类型 1 默认地址 2 常规")
    private String type;
}
