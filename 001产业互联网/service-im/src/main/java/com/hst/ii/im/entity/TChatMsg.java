package com.hst.ii.im.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * TChatMsg
 *
 * @author WangYH
 * @date 2020/8/22
 */
@Entity
@Table(name="im_msgs", indexes = {
        @Index(columnList = "chat_id")
})
@Data
public class TChatMsg {
    @Id
    @Column(length = 10)
    @GeneratedValue(generator = "hsid")
    @GenericGenerator(name = "hsid", strategy = "com.hst.core.hbm.HSIDGenerator")
    private String id;

    @Column(name="chat_id", length = 30)
    private String chatId;

    @Column(length = 10)
    private String userid;

    @Column(length = 10)
    private String userid2;

    @Column
    private Date dt;

    @Column
    private String type;

    @Column
    private String msg;
}
