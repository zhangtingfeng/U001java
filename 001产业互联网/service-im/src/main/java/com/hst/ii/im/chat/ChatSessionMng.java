package com.hst.ii.im.chat;

import com.hst.core.utils.JsonUtil;
import com.hst.ii.im.po.ChatContext;
import com.hst.ii.im.po.ChatPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ChatSessionMng
 *
 * @author WangYH
 * @date 2020/8/21
 */
@Service
public class ChatSessionMng {
    public static final String CHAT_CONTEXT = "chat-context";
    Logger logger = LoggerFactory.getLogger(ChatSessionMng.class);

    Map<String, WebSocketSession> sessions;

    public ChatSessionMng() {
        this.sessions = new HashMap<>();
    }

    public void register(ChatContext ctx, WebSocketSession session) throws IOException {
        logger.info("register: {}, {}", ctx.toString(), session.getRemoteAddress().toString());
        synchronized (sessions) {
            if (sessions.containsKey(ctx.getUser().getId())) {
                WebSocketSession session1 = sessions.get(ctx.getUser().getId());
                sessions.remove(ctx.getUser().getId());
                kickout(session1);
            }

            sessions.put(ctx.getUser().getId(), session);
            session.getAttributes().put(CHAT_CONTEXT, ctx);

            ChatPayload payload = new ChatPayload();
            payload.setCmd(ChatPayload.CMD_INIT);
            payload.setData(JsonUtil.toString(ctx));
            session.sendMessage(new TextMessage(JsonUtil.toString(payload)));
        }
    }

    protected void kickout(WebSocketSession session) {
        try {
            ChatPayload payload = new ChatPayload();
            payload.setCmd(ChatPayload.CMD_KICKOUT);
//            payload.setData(null);
            session.sendMessage(new TextMessage(JsonUtil.toString(payload)));
        } catch (IOException ex) {
        }
    }

    public void unregister(WebSocketSession session) {
        ChatContext ctx = getChatContext(session);
        if (null != ctx){
            logger.info("unregister: {},{}", ctx.toString(), session == null ? "": session.getRemoteAddress().toString());
            sessions.remove(ctx.getUser().getId());
        }else {
            String userid= null;
            for(Map.Entry<String, WebSocketSession> e : sessions.entrySet()){
                if (e.equals(session)){
                    userid = e.getKey();
                    break;
                }
            }
            if (null != userid)
                sessions.remove(userid);
        }
    }

    public ChatContext getChatContext(WebSocketSession session){
        return (ChatContext) session.getAttributes().get(CHAT_CONTEXT);
    }

    public WebSocketSession getSession(String userid){
        synchronized (sessions) {
            if (sessions.containsKey(userid)) {
                return sessions.get(userid);
            }
            return null;
        }
    }
}
