package com.hst.ii.aftermarket.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import com.hst.core.dao.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @description: 售后服务图片信息表
 * @author: ZhaJun
 * @time: 2020/7/10 13:41
 */
@Entity
@Table(name = "aftermarket_img")
@Comment("售后服务图片信息表")
@HSID(length = 10, prefix = "I")
@Data
@NoArgsConstructor
public class TAftermarketImg  extends BaseEntity implements Serializable {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("ID")
    private String id;

    @Column(name="id_order", length = 10)
    @Comment("订单ID")
    private String orderId;

    @Column(name="id_aftermarket", length = 10)
    @Comment("售后服务ID")
    private String aftermarketID;

    @Column(name="id_aftermarket_detail", length = 10)
    @Comment("aftermarket_detailID  那一次发起问题上传的图片 外键使用 ")
    private String aftermarket_detailID;

    @Column(name = "img_path", length = 50)
    @Comment("售后服务图片路径")
    private String imgPath;

    @Column(length = 2)
    @Comment("图片用途 售后服务")
    private String type;

    @Column(length = 2)
    @Comment("图片顺序")
    private Integer idx;


}
