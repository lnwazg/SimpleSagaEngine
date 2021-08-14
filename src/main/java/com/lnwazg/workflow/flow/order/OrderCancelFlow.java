package com.lnwazg.workflow.flow.order;

import com.lnwazg.workflow.engine.AbstractFlow;
import com.lnwazg.workflow.engine.anno.WorkFlow;
import com.lnwazg.workflow.flow.context.OrderFlowContext;
import org.springframework.stereotype.Component;

@WorkFlow
@Component
public class OrderCancelFlow extends AbstractFlow<OrderFlowContext> {
}
