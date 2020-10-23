package com.hst.ii.order.po;

import com.hst.core.annotation.Comment;
import lombok.Data;

import javax.persistence.Column;
import java.util.Map;

/**
 * BaseInfo
 * 订单基本信息
 * @author WangYH
 * @date 2020/8/25
 */
@Data
public class BaseInfo {
    public static final String STATUS_CANCEL = "0";
    public static final String STATUS_INIT = "1";
    public static final String STATUS_PROC = "2";
    public static final String STATUS_END = "9";

    public static final String TYPE_BUYER = "1";
    public static final String TYPE_SELLER = "2";
    /**
     * 订单ID
     */
    private String id;

    /**
     * 买家ID
     */
    private String buyerId;

    /**
     * 卖家ID
     */
    private String sellerId;

    /**
     * 处理类型: 1: 买家, 2: 卖家
     */
    private String type;

    /**
     * 订单状态. 1: 订单生成, 2: 订单交易, 0: 订单取消, 9:订单完成
     */
    private String status;

    /**
     * 买家状态
     */
    private String state1;

    /**
     * 卖家状态
     */
    private String state2;

    /**
     * 订单生成的流程
     */
    private String flow1;

    /**
     * 订单生成的流程实例ID
     */
    private String process1;

    /**
     * 订单生成的流程节点
     */
    private String node1;

    /**
     * 订单交易的流程
     */
    private String flow2;

    /**
     * 订单交易的流程实例ID
     */
    private String process2;

    /**
     * 订单交易的流程节点
     */
    private String node2;

    /**
     * 流程当前节点的link条件是否满足
     */
    private Map<String, Boolean> links;
}
