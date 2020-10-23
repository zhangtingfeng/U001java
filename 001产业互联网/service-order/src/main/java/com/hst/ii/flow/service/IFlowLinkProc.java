package com.hst.ii.flow.service;

import com.hst.core.ServiceException;
import com.hst.ii.flow.entity.TFlowProcess;
import com.hst.ii.flow.po.Flow;
import com.hst.ii.flow.po.Link;

/**
 * IFlowLinkProc
 * 流程流转时的数据处理
 * @author WangYH
 * @date 2020/8/26
 */
public interface IFlowLinkProc {
    /**
     * 流转前处理
     * @param process
     * @param flow
     * @param link
     * @throws ServiceException
     */
    void prevProc(TFlowProcess process, Flow flow, Link link) throws ServiceException;

    /***
     * 流转后处理
     * @param process
     * @param link
     * @throws ServiceException
     */
    void postProc(TFlowProcess process, Flow flow, Link link) throws ServiceException;
}
