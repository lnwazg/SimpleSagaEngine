package com.lnwazg.workflow.flow.order;

import com.lnwazg.workflow.engine.AbstractFlow;
import com.lnwazg.workflow.engine.anno.Node;
import com.lnwazg.workflow.engine.anno.StartNode;
import com.lnwazg.workflow.engine.anno.WorkFlow;
import com.lnwazg.workflow.engine.exception.BusinessException;
import com.lnwazg.workflow.flow.context.OrderFlowContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 订单支付
 */
@WorkFlow
@Component
@Slf4j
public class OrderPayFlow extends AbstractFlow<OrderFlowContext> {

    @StartNode
    @Node
    void start(OrderFlowContext orderFlowContext) throws BusinessException {

    }

}
