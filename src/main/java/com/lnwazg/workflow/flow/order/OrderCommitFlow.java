package com.lnwazg.workflow.flow.order;

import com.lnwazg.dto.OrderDTO;
import com.lnwazg.service.OrderService;
import com.lnwazg.workflow.engine.anno.Node;
import com.lnwazg.workflow.engine.anno.StartNode;
import com.lnwazg.workflow.engine.anno.WorkFlow;
import com.lnwazg.workflow.engine.exception.BusinessException;
import com.lnwazg.workflow.flow.context.OrderFlowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 订单提交
 */
@WorkFlow
@Component
public class OrderCommitFlow {
    private Logger logger = LoggerFactory.getLogger(OrderCommitFlow.class);

    private final OrderService orderService;

    public OrderCommitFlow(OrderService orderService) {
        this.orderService = orderService;
    }

    //下单流程：
    //查询商品信息
    //查询用户信息
    //算费
    //创建分布式Id
    //保存订单信息
    //锁库存
    //扣积分
    //增加活动次数
    //扣减会员权益
    //锁定优惠券

    //修改订单状态
    //发送履约消息

    //框架记录每个流程的执行耗时
    //若走到某一步执行失败了，则要依次执行每一个已执行完的流程的回滚流程（若存在rollback流程）

    //订单取消流程

    @StartNode
    @Node
    void createOrder(OrderFlowContext orderFlowContext) throws BusinessException {
        OrderDTO orderDTO = orderService.createOrder();
        orderFlowContext.setOrderDTO(orderDTO);
        orderFlowContext.setNextNodeName("lockInventory");
    }

    @Node
    void lockInventory(OrderFlowContext orderFlowContext) throws BusinessException {
        orderFlowContext.setNextNodeName("decreaseInventoryProcessor");
    }


}
