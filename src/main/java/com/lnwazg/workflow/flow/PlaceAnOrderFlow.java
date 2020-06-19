package com.lnwazg.workflow.flow;

import com.lnwazg.workflow.anno.Node;
import com.lnwazg.workflow.anno.StartNode;
import com.lnwazg.workflow.anno.WorkFlow;
import com.lnwazg.workflow.engine.BusinessException;
import com.lnwazg.workflow.engine.WorkFlowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 下单工作流
 */
@WorkFlow
@Component
public class PlaceAnOrderFlow {
    private Logger logger = LoggerFactory.getLogger(PlaceAnOrderFlow.class);

    /**
     * 起始节点
     *
     * @param workFlowContext
     */
    @StartNode
    @Node
    void start(WorkFlowContext workFlowContext) throws BusinessException {
        workFlowContext.setNextNodeName("lockInventoryProcessor");
    }

    /**
     * 锁库存
     *
     * @param workFlowContext
     */
    @Node
    void lockInventoryProcessor(WorkFlowContext workFlowContext) throws BusinessException {
        logger.info("LockInventoryProcessor processing...");
        workFlowContext.setNextNodeName("decreaseInventoryProcessor");
//        workFlowContext.setNextNodeName("failProcessor");
    }

    /**
     * 减库存
     *
     * @param workFlowContext
     */
    @Node
    void decreaseInventoryProcessor(WorkFlowContext workFlowContext) throws BusinessException {
        logger.info("decreaseInventoryProcessor processing...");
        workFlowContext.setNextNodeName("createOrderProcessor");
    }

    /**
     * 创建订单
     *
     * @param workFlowContext
     */
    @Node
    void createOrderProcessor(WorkFlowContext workFlowContext) throws BusinessException {
        logger.info("createOrderProcessor processing...");
        workFlowContext.setNextNodeName("releaselockInventoryProcessor");
    }

    /**
     * 释放库存锁
     *
     * @param workFlowContext
     */
    @Node
    void releaselockInventoryProcessor(WorkFlowContext workFlowContext) throws BusinessException {
        logger.info("releaselockInventoryProcessor processing...");
    }

    /**
     * 失败处理
     *
     * @param workFlowContext
     */
    @Node
    void failProcessor(WorkFlowContext workFlowContext) throws BusinessException {
        logger.info("failProcessor...");
    }

}
