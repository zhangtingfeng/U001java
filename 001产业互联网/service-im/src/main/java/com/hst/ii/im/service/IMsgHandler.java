package com.hst.ii.im.service;

import com.hst.ii.im.po.ChatPayload;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * IMsgHandler
 *
 * @author WangYH
 * @date 2020/8/22
 */
public interface IMsgHandler {
    String getCmd();

    void proc(WebSocketSession session, ChatPayload payload) throws IOException, Exception;
}
