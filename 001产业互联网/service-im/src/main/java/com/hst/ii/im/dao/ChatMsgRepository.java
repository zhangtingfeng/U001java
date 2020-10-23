package com.hst.ii.im.dao;

import com.hst.ii.im.entity.TChatMsg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ChatMsgRepository
 *
 * @author WangYH
 * @date 2020/8/22
 */
@Repository
public interface ChatMsgRepository extends PagingAndSortingRepository<TChatMsg, String> {
    List<TChatMsg> findTop10ByChatIdOrderByIdDesc(String chatId);
    List<TChatMsg> findTop10ByChatIdAndIdBeforeOrderByIdDesc(String chatId, String id);
}
