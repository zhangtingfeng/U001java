/**
 * 
 */
package com.hst.ii.msg.ui;

import com.hst.core.annotation.Comment;
import com.hst.core.meta.MetaData;
import com.hst.core.meta.annotation.EField;
import com.hst.core.meta.annotation.EInfo;
import com.hst.core.meta.annotation.ESort;
import com.hst.core.meta.annotation.FieldQuery;
import com.hst.ii.msg.entity.TMsg;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * @author wangyh
 *
 */
@EInfo(name = "msg", entity = TMsg.class, title = "消息管理|Msgs")
@Data
@NoArgsConstructor
public class Msg extends MetaData {

    @EField(title = "id", list = false, form = 0)
    @ESort
    private String id;

    @EField(title = "消息发送业务节点", viewer = "dict.msgSendBusinessNode", editor = "select.msgSendBusinessNode", validate = "required", query = FieldQuery.EQ)
    private String msgSendBusinessNode;


    @EField(title = "消息发送对象", viewer = "dict.msgSendOBJ", editor = "select.msgSendOBJ",validate = "required", query = FieldQuery.EQ)
    private String msgSendOBJ;

    @EField(title = "消息发送方式", list = false,form = 0)
    private String msgSendWay;

    @Comment("消息内容")
    @EField(title = "消息内容|Send Content",group = "发送消息模板内容",list = false,form = 0)
    private String sendcontent;

  /*
    ,

*/
}
