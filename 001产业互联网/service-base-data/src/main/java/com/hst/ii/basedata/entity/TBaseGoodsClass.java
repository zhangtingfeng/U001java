package com.hst.ii.basedata.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import com.hst.core.annotation.ResponseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "base_goods_class")
@HSID(length = 10, prefix = "C", resetByDay = true)
@Comment("商品分类表")
@ResponseModel
@Data
@NoArgsConstructor
public class TBaseGoodsClass {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("id")
    private String id;

    @Column(length = 10)
    @Comment("父id")
    private String pid;

    @Column(length = 200)
    @Comment("商品分类名称")
    private String name;

}

