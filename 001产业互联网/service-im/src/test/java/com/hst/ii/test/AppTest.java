package com.hst.ii.test;

import com.hst.ii.im.HstApplication;
import com.hst.ii.im.dao.ChatMsgRepository;
import com.hst.ii.im.entity.TChatMsg;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * AppTest
 *
 * @author WangYH
 * @date 2020/8/22
 */
@SpringBootTest(classes = HstApplication.class)
@ActiveProfiles("vpn")
public class AppTest {
    @Autowired
    ChatMsgRepository chatMsgRepository;

    @Test
    public  void test1(){
        List<TChatMsg> datas = chatMsgRepository.findTop10ByChatIdAndIdBeforeOrderByIdDesc("U208F00001,0000000001", "208N000019");
        System.out.println(datas.size());
    }
}
