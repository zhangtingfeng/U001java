package com.hst.ii.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @description: 挂牌销售属性信息表
 * @author: ZhaJun
 * @time: 2020/7/10 13:41
 */
@Entity
@Table(name = "sale_prop_info")
@Comment("挂牌销售属性信息表")
@HSID(length = 10, prefix = "P")
@Data
@NoArgsConstructor
public class TSalePropInfo implements Serializable {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    @Comment("ID")
    private String id;

    @Column(name = "id_sales", length = 10)
    @Comment("挂牌销售ID")
    private String salesId;

    @Column(name = "id_prop", nullable = false, length = 10)
    @Comment("挂牌销售属性ID")
    private String propId;

//    @Column(name = "prop_group", length = 20)
//    @Comment("属性分组")
//    private String propGroup;
//
//    @Column(name = "prop_class", length = 20)
//    @Comment("属性分类")
//    private String propClass;

    @Column(name = "prop_name", length = 20)
    @Comment("属性名称")
    private String propName;

    @Column(name = "prop_value", length = 50)
    @Comment("属性值")
    private String propValue;

    @Column(name = "custom_prop_value", length = 500)
    @Comment("自定义属性值")
    private String customPropValue;


}
