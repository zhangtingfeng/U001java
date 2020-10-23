package com.hst.ii.sys.entity;

import com.hst.core.annotation.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Adrian
 */
@Entity
@Table(name = "sys_user_role")
@Comment("用户角色关系表")
@Data
@NoArgsConstructor
public class TUserRole implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Comment("用户id")
    @Id
    @Column(length = 10, name = "id_user")
    private String userid;

    @Comment("角色id")
    @Id
    @Column(length = 10, name = "id_role")
    private String roleid;

}
