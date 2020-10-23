package com.hst.ii.org.entity;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.ResponseModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "sys_org_role")
@Comment("机构角色关系表")
@ResponseModel
@Data
@NoArgsConstructor
public class TOrgRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Comment("角色id")
    @Column(name = "id_role", length = 10)
    private String roleId;

    @Id
    @Comment("机构id")
    @Column(name = "id_org", length = 10)
    private String orgId;

}
