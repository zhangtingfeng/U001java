package com.hst.ii.im.po;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User
 *
 * @author WangYH
 * @date 2020/8/21
 */
@Data
@NoArgsConstructor
public class ChatUser {
    private String id;
    private String name;
    private String orgId;
    private String orgName;
}
