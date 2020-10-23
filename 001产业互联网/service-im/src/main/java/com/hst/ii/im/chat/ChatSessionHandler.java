package com.hst.ii.im.chat;

import com.hst.core.utils.JsonUtil;
import com.hst.ii.im.po.ChatContext;
import com.hst.ii.im.po.ChatPayload;
import com.hst.ii.im.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * ChatHandler
 *
 * @author WangYH
 * @date 2020/8/20
 */
@Service
public class ChatSessionHandler extends TextWebSocketHandler {
    Logger log = LoggerFactory.getLogger(ChatSessionHandler.class);

    @Autowired
    ChatSessionMng sessionMng;

    @Autowired
    ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String chatSessionId = session.getUri().getQuery();
        ChatContext ctx = chatService.getChatContext(chatSessionId);
        if (null == ctx) {
            log.info("Invalid chatSessionId:{}", chatSessionId);
            session.close();
        }else
            sessionMng.register(ctx, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatPayload payload = JsonUtil.read(message.getPayload(), ChatPayload.class);
        chatService.procChatPayload(session, payload);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionMng.unregister(session);
    }
}
