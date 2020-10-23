package com.hst.ii.sales.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @description: 挂牌销售图片信息表
 * @author: ZhaJun
 * @time: 2020/7/10 13:41
 */
@Entity
@Table(name = "sale_img")
@Comment("挂牌销售图片信息表")
@HSID(length = 10, prefix = "I")
@Data
@NoArgsConstructor
public class TSaleImg implements Serializable {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("ID")
    private String id;

    @Column(name = "id_sales", length = 10)
    @Comment("挂牌销售ID")
    private String salesId;

    @Column(name = "img_path", length = 50)
    @Comment("商品图片路径")
    private String imgPath;

    @Column(length = 2)
    @Comment("图片用途 1 轮播图 2 详情图片")
    private String type;

    @Column(length = 2)
    @Comment("图片顺序")
    private Integer idx;


}
