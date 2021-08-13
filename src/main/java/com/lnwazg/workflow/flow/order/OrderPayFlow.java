package com.lnwazg.workflow.flow.order;

import com.lnwazg.workflow.engine.anno.Node;
import com.lnwazg.workflow.engine.anno.StartNode;
import com.lnwazg.workflow.engine.anno.WorkFlow;
import com.lnwazg.workflow.engine.exception.BusinessException;
import com.lnwazg.workflow.flow.context.OrderFlowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 订单支付
 */
@WorkFlow
@Component
public class OrderPayFlow {
    private Logger logger = LoggerFactory.getLogger(OrderPayFlow.class);

    @StartNode
    @Node
    void start(OrderFlowContext orderFlowContext) throws BusinessException {

    }

}
