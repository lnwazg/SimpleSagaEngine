package com.lnwazg.workflow.flow.context;

import com.lnwazg.dto.OrderDTO;
import com.lnwazg.workflow.engine.BaseWorkFlowContext;
import lombok.Data;

/**
 * 下单相关的流程的上下文
 */
@Data
public class OrderFlowContext extends BaseWorkFlowContext {
    private OrderDTO orderDTO;
}
