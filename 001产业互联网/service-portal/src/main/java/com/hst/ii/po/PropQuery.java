package com.hst.ii.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/8/19 14:56
 */
@Data
@NoArgsConstructor
public class PropQuery {

    private String id;

    private List<String> valueList;
}
