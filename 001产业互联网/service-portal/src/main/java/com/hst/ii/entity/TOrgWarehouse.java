package com.hst.ii.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @description:机构仓库表
 * @author: ZhaJun
 * @time: 2020/9/8 11:08
 */
@Entity
@Table(name = "sys_org_warehouse", indexes = {@Index(name = "idx_code", unique = true, columnList = "code")})
@Comment("机构仓库表")
@HSID(length = 10, prefix = "W")
@Data
@NoArgsConstructor
public class TOrgWarehouse implements Serializable {
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
    @Comment("仓库编码")
    private String code;

    @Column(length = 50)
    @Comment("仓库名称")
    private String name;

    @Column(length = 10)
    @Comment("仓库地址区域")
    private String area;

    @Column(length = 100)
    @Comment("仓库地址详细")
    private String address;
}
