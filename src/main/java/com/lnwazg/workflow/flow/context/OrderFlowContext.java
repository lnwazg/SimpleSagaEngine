package com.lnwazg.workflow.flow.context;

import com.lnwazg.dto.OrderDTO;
import com.lnwazg.dto.request.OrderCommitReqDTO;
import com.lnwazg.workflow.engine.BaseWorkFlowContext;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 下单相关的流程的上下文
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class OrderFlowContext extends BaseWorkFlowContext {
    private OrderCommitReqDTO orderCommitReqDTO;
    private OrderDTO orderDTO;
}
