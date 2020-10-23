package com.hst.ii.im.handlers;

import com.hst.core.dao.IORDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.im.chat.ChatSessionMng;
import com.hst.ii.im.entity.TChatMsg;
import com.hst.ii.im.po.ChatContext;
import com.hst.ii.im.po.ChatMsg;
import com.hst.ii.im.po.ChatPayload;
import com.hst.ii.im.service.IMsgHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Date;

/**
 * ChatHandler
 *
 * @author WangYH
 * @date 2020/8/22
 */
@Service
public class ChatHandler implements IMsgHandler {
    @Autowired
    ChatSessionMng sessionMng;

    @Autowired
    IORDao dao;

    @Override
    public String getCmd() {
        return ChatPayload.CMD_CHAT;
    }

    @Override
    public void proc(WebSocketSession session, ChatPayload payload) throws Exception {
        ChatMsg msg = JsonUtil.read(payload.getData(), ChatMsg.class);
        ChatContext ctx = sessionMng.getChatContext(session);
        TChatMsg chatMsg = save2Db(ctx, msg);
        msg.setId(chatMsg.getId());
        String json = JsonUtil.toString(chatMsg);
        notify(ctx, json);
        reply(session, json);
    }

    TChatMsg save2Db(ChatContext ctx, ChatMsg msg){
        TChatMsg chatMsg = new TChatMsg();
        chatMsg.setDt(new Date());
        chatMsg.setChatId(ctx.getChatId());
        chatMsg.setUserid(ctx.getUser().getId());
        chatMsg.setUserid2(ctx.getUser2().getId());
        chatMsg.setType(msg.getType());
        chatMsg.setMsg(msg.getMsg());
        dao.save(chatMsg);
        return chatMsg;
    }

    void notify(ChatContext ctx, String json) throws IOException {
        WebSocketSession session = sessionMng.getSession(ctx.getUser2().getId());
        if (session == null) return;

        ChatPayload payload = new ChatPayload();
        payload.setCmd(ChatPayload.CMD_CHAT2);
        payload.setData(json);
        session.sendMessage(new TextMessage(JsonUtil.toString(payload)));
    }

    void reply(WebSocketSession session, String json) throws IOException {
        ChatPayload payload = new ChatPayload();
        payload.setCmd(ChatPayload.CMD_RESULT);
        payload.setData(json);
        session.sendMessage(new TextMessage(JsonUtil.toString(payload)));
    }
}
