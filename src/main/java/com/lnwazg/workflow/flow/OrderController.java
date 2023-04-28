package com.lnwazg.workflow.flow;

import com.lnwazg.dto.request.OrderCommitReqDTO;
import com.lnwazg.workflow.flow.context.OrderFlowContext;
import com.lnwazg.workflow.flow.order.OrderCancelFlow;
import com.lnwazg.workflow.flow.order.OrderCommitFlow;
import com.lnwazg.workflow.flow.order.OrderFulfillingFlow;
import com.lnwazg.workflow.flow.order.OrderPayFlow;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

@Component
public class OrderController {
    @Resource
    private OrderCommitFlow orderCommitFlow;
    @Resource
    private OrderCancelFlow orderCancelFlow;
    @Resource
    private OrderPayFlow orderPayFlow;
    @Resource
    private OrderFulfillingFlow orderFulfillingFlow;

    public void runOrder() {
        //构造下单流程的上下文
        OrderFlowContext orderFlowContext = new OrderFlowContext();
        OrderCommitReqDTO orderCommitReqDTO = OrderCommitReqDTO.builder().saleAccountId(1L).skuIdList(Arrays.asList(1L, 2L)).skuQuantityList(Arrays.asList(2, 1)).build();
        orderFlowContext.setOrderCommitReqDTO(orderCommitReqDTO);
        //执行订单提交流程
        orderCommitFlow.exec(orderFlowContext);
        //执行订单支付流程
        orderPayFlow.exec(orderFlowContext);
        //执行订单履约流程
        orderFulfillingFlow.exec(orderFlowContext);
        //执行订单取消流程（会失败）
        orderCancelFlow.exec(orderFlowContext);
    }
}
