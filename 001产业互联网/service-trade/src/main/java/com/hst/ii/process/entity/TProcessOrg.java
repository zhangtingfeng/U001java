package com.hst.ii.process.entity;

import com.hst.core.annotation.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @description:指定机构对应流程
 * @author: ZhaJun
 * @time: 2020/8/21 17:44
 */
@Entity
@Table(name = "process_org")
@Comment("流程机构表")
@Data
@NoArgsConstructor
public class TProcessOrg  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id_process",length = 30)
    @Comment("流程ID")
    private String processId;

    @Id
    @Column(name="id_org",length = 30)
    @Comment("流程ID")
    private String orgId;

}
