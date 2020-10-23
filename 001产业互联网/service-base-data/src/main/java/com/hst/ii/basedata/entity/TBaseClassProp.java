package com.hst.ii.basedata.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import com.hst.core.annotation.ResponseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "base_class_prop")
@HSID(length = 10, prefix = "CP", resetByDay = true)
@Comment("商品分类属性关系表")
@ResponseModel
@Data
@NoArgsConstructor
public class TBaseClassProp {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("id")
    private String id;

    @Column(name = "class_id", length = 10)
    @Comment("商品分类id")
    private String classId;

    @Column(name = "prop_id", length = 10)
    @Comment("属性id")
    private String propId;

    @Column(name = "select_values", length = 2000)
    @Comment("属性待选值")
    private String selectValues;

    @Column(name = "visible_level", length = 10)
    @Comment("可见级别")
    private String visibleLevel;

}

