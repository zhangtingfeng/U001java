package com.hst.ii.im.po;

import com.hst.core.auth.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ChatContext 对话的双方用户信息
 *
 * @author WangYH
 * @date 2020/8/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatContext {
    private String chatId;
    private ChatUser user;
    private ChatUser user2;
}
