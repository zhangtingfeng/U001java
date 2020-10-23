package com.hst.ii.im.entity;

import com.hst.core.annotation.Comment;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * TChat
 *
 * @author WangYH
 * @date 2020/8/22
 */
@Entity
@Table(name="im_chat", indexes = {@Index(columnList = "uid1, uid2", unique = true)})
@Data
@Comment("聊天室")
public class TChat {
    @Id
    @Column(length = 21)
    @Comment("聊天室ID")
    private String id;

    @Column(name="uid1", length = 10)
    @Comment("用户1")
    private String userid1;

    @Column(name="uid2", length = 10)
    @Comment("用户2")
    private String userid2;

    @Column
    @Comment("用户1最后访问时间")
    private Date dt1;

    @Column
    @Comment("用户1最后访问时间")
    private Date dt2;

    @Column
    @Comment("最后一条消息的时间")
    private Date dt;
}
