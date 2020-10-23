package com.hst.ii.im;

import com.hst.ii.im.chat.ChatSessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * ImConfiguration
 *
 * @author WangYH
 * @date 2020/8/20
 */
@Configuration
@EnableWebSocket
public class ImConfiguror implements WebSocketConfigurer {
    @Autowired
    ChatSessionHandler chatSessionHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatSessionHandler, "/chat")
                .setAllowedOrigins("*");
//                .withSockJS();
    }
}
