package com.hst.ii.im.po;

import lombok.Data;

/**
 * ChatPayload
 *
 * @author WangYH
 * @date 2020/8/22
 */
@Data
public class ChatMsg {
    private String id;
    /**
     * 0: text
     * 1: goods
     * 2: order
     */
    private String type;
    private String msg;
}
