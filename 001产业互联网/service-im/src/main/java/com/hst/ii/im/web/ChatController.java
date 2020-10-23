package com.hst.ii.im.web;

import com.hst.core.ServiceContext;
import com.hst.core.ServiceException;
import com.hst.core.dao.IORDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.im.dao.UserMapper;
import com.hst.ii.im.entity.TChatMsg;
import com.hst.ii.im.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * AuthController 即时通讯启动验证
 *
 * @author WangYH
 * @date 2020/8/21
 */
@Controller
public class ChatController {
    Logger log = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    ChatService chatService;

    @Autowired
    UserMapper userMapper;



    @RequestMapping("/auth/{userid}")
    void auth(@PathVariable String userid, Model m,@RequestBody String data) throws ServiceException {
        try {


            Map<String, Object> reqData = JsonUtil.read(data, Map.class);
            String strServiceMsg = (String) reqData.get("ServiceMsg");
            if (strServiceMsg!="" && strServiceMsg!=null)
            {
                chatService.saveim_msgs(userid,strServiceMsg);
            }

            String chatId = chatService.auth(userid);
            m.addAttribute("id", chatId);

        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            throw new ServiceException(ServiceException.INTERNAL);
        }
    }

    @RequestMapping("/chats")
    void chats(Model m) throws ServiceException {
        String strUID=ServiceContext.getInstance().getUid();
        List<?> lists = userMapper.getChats(strUID);
        m.addAttribute("chats", lists);
    }

//得到交谈记录
    @RequestMapping("/msgs/{chatId}")
    void msgs(@PathVariable String chatId, String id, Model m) throws ServiceException {
        m.addAttribute("data", chatService.getChatMsgs(chatId, id));
    }



}
