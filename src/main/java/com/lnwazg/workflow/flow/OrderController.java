package com.lnwazg.workflow.flow;

import com.lnwazg.workflow.flow.context.OrderFlowContext;
import com.lnwazg.workflow.flow.order.OrderCancelFlow;
import com.lnwazg.workflow.flow.order.OrderCommitFlow;
import com.lnwazg.workflow.flow.order.OrderFulfillingFlow;
import com.lnwazg.workflow.flow.order.OrderPayFlow;
import org.springframework.stereotype.Component;

@Component
public class OrderController {
    private final OrderCommitFlow orderCommitFlow;
    private final OrderCancelFlow orderCancelFlow;
    private final OrderPayFlow orderPayFlow;
    private final OrderFulfillingFlow orderFulfillingFlow;

    public OrderController(OrderCommitFlow orderCommitFlow, OrderCancelFlow orderCancelFlow, OrderPayFlow orderPayFlow, OrderFulfillingFlow orderFulfillingFlow) {
        this.orderCommitFlow = orderCommitFlow;
        this.orderCancelFlow = orderCancelFlow;
        this.orderPayFlow = orderPayFlow;
        this.orderFulfillingFlow = orderFulfillingFlow;
    }

    public void runOrder() {
        //构造下单流程的上下文
        OrderFlowContext orderFlowContext = new OrderFlowContext();
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
