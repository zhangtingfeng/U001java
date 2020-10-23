package com.hst.ii.im.service;

import com.hst.core.ServiceContext;
import com.hst.core.ServiceException;
import com.hst.core.auth.User;
import com.hst.core.dao.IORDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.im.chat.ChatSessionMng;
import com.hst.ii.im.dao.ChatMsgRepository;
import com.hst.ii.im.dao.UserMapper;
import com.hst.ii.im.entity.TChat;
import com.hst.ii.im.entity.TChatMsg;
import com.hst.ii.im.po.ChatContext;
import com.hst.ii.im.po.ChatPayload;
import com.hst.ii.im.po.ChatUser;
import com.hst.ii.im.web.ChatController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * AuthService
 *
 * @author WangYH
 * @date 2020/8/21
 */
@Service
public class ChatService {
    public static final long EXPIRED = 30;
    Logger log = LoggerFactory.getLogger(ChatController.class);
    @Autowired
    IORDao dao;

    @Autowired
    ChatMsgRepository chatMsgRepository;

    @Autowired
    UserMapper userMapper;


    @Autowired
    RedisTemplate<String, String> redis;

    Map<String, IMsgHandler> handlers;

    @Autowired
    void setMsgHandlers(List<IMsgHandler> msgHandlers) {
        handlers = new HashMap<>();
        msgHandlers.stream().forEach(mh -> {
            handlers.put(mh.getCmd(), mh);
        });
    }

    private String getChatId(String uid1, String uid2) {
        String tmp = (uid1.compareTo(uid2) > 0) ? StringUtils.join(uid1, ",", uid2) : StringUtils.join(uid2, ",", uid1);
        return tmp;
    }

    public String auth(String userid) throws ServiceException, IOException {
        User user = ServiceContext.getInstance().getUser();
        if (null == user)
            throw new ServiceException("-1");

        ChatUser user1 = new ChatUser();
        user1.setId(user.getUid());
        user1.setName(user.getName());
        user1.setOrgId(user.getOrgcode());
        ChatUser user2 = userMapper.getUserByUserid(userid);
        if (user2 == null) {
            throw new ServiceException("-2");
        }

        String chatId = getChatId(user1.getId(), user2.getId());
        TChat chat = dao.get(TChat.class, chatId);
        if (null == chat){
            chat = new TChat();
            chat.setId(chatId);
            if (user1.getId().compareTo(user2.getId()) > 0){
                chat.setUserid1(user1.getId());
                chat.setUserid2(user2.getId());
            }else{
                chat.setUserid1(user2.getId());
                chat.setUserid2(user1.getId());
            }
            Date dt = new Date();
            chat.setDt(dt);
            chat.setDt1(dt);
            chat.setDt2(dt);
            dao.save(chat);
        }

        String chatSessionId = UUID.randomUUID().toString();

        String json = JsonUtil.toString(new ChatContext(chatId, user1, user2));
        String key = getRedisKey(chatSessionId);

        redis.opsForValue().set(key, json);
        redis.expire(key, EXPIRED, TimeUnit.SECONDS);

        return chatSessionId;
    }

    String getRedisKey(String chatSessionId) {
        return StringUtils.join("chat-", chatSessionId);
    }

    public ChatContext getChatContext(String chatSessionId) {
        String key = getRedisKey(chatSessionId);
        if (redis.hasKey(key)) {
            String json = redis.opsForValue().get(key);
            redis.delete(key);
            try {
                return JsonUtil.read(json, ChatContext.class);
            } catch (IOException e) {
                return null;
            }
        }

        return null;
    }

    public void procChatPayload(WebSocketSession session, ChatPayload payload) throws Exception {
        IMsgHandler msgHandler = handlers.get(payload.getCmd());
        msgHandler.proc(session, payload);
    }

    public List<TChatMsg> getChatMsgs(String chatId, String maxId) throws ServiceException {
        String uid = ServiceContext.getInstance().getUid();
        String[] tmp = chatId.split(",");
        if (uid.equals(tmp[0]) || uid.equals(tmp[1])) {
            if (StringUtils.isNotEmpty(maxId)){
                return chatMsgRepository.findTop10ByChatIdAndIdBeforeOrderByIdDesc(chatId, maxId);
            }else{
                return chatMsgRepository.findTop10ByChatIdOrderByIdDesc(chatId);
            }
        }
        throw new ServiceException(ServiceException.NOAUTH);
    }

    public void  saveim_msgs(String StrToUid,String strServiceMsg){
        User user = ServiceContext.getInstance().getUser();
        String strUid=user.getUid();
        log.error("strUid="+strUid);
        TChatMsg chatMsg = new TChatMsg();
        chatMsg.setDt(new Date());
        chatMsg.setChatId(getChatId(StrToUid,strUid));
        chatMsg.setUserid(strUid);
        chatMsg.setUserid2(StrToUid);
        chatMsg.setType("0");
        chatMsg.setMsg(strServiceMsg);
        dao.update(chatMsg);
    }

}
