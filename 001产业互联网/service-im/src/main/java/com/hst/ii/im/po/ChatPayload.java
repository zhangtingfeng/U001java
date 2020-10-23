package com.hst.ii.im.po;

import lombok.Data;

/**
 * ChatPayload 和前端的通讯消息报
 *
 * @author WangYH
 * @date 2020/8/22
 */
@Data
public class ChatPayload {
    /**
     * 发送对话反馈
     */
    public static  final String CMD_INIT = "init";

    /**
     * 发送对话
     */
    public static  final String CMD_CHAT = "chat";

    /**
     * 发送对话反馈
     */
    public static  final String CMD_RESULT = "rs";

    /**
     * 接收到对话
     */
    public static  final String CMD_CHAT2 = "chat2";

    /**
     * 处理错误
     */
    public static  final String CMD_ERROR = "err";

    /**
     * 踢出
     */
    public static  final String CMD_KICKOUT = "kickout";

    private String cmd;
    private String data;
}
