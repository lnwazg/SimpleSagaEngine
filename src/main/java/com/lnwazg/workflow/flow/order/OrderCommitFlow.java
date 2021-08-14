package com.lnwazg.workflow.flow.order;

import com.lnwazg.dto.GoodsInfoDTO;
import com.lnwazg.dto.OrderDTO;
import com.lnwazg.dto.SaleInfoDTO;
import com.lnwazg.service.AccountService;
import com.lnwazg.service.GoodsService;
import com.lnwazg.service.OrderService;
import com.lnwazg.service.PromotionService;
import com.lnwazg.workflow.engine.AbstractFlow;
import com.lnwazg.workflow.engine.anno.Node;
import com.lnwazg.workflow.engine.anno.RollbackNode;
import com.lnwazg.workflow.engine.anno.StartNode;
import com.lnwazg.workflow.engine.anno.WorkFlow;
import com.lnwazg.workflow.engine.exception.BusinessException;
import com.lnwazg.workflow.flow.context.OrderFlowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

//完整的下单流程：
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

/**
 * 订单提交
 */
@WorkFlow
@Component
public class OrderCommitFlow extends AbstractFlow<OrderFlowContext> {
    private Logger logger = LoggerFactory.getLogger(OrderCommitFlow.class);

    private final OrderService orderService;
    private final GoodsService goodsService;
    private final AccountService accountService;
    private final PromotionService promotionService;

    public OrderCommitFlow(OrderService orderService, GoodsService goodsService, AccountService accountService, PromotionService promotionService) {
        this.orderService = orderService;
        this.goodsService = goodsService;
        this.accountService = accountService;
        this.promotionService = promotionService;
    }

    @StartNode
    void queryGoodsInfo(OrderFlowContext orderFlowContext) {
        List<GoodsInfoDTO> list = goodsService.queryGoodsInfo(orderFlowContext.getOrderCommitReqDTO().getSkuIdList());
        orderFlowContext.getOrderCommitReqDTO().setGoodsInfoDTOS(list);
        orderFlowContext.setNextNodeName("querySaleInfo");
    }

    @Node
    void querySaleInfo(OrderFlowContext orderFlowContext) {
        SaleInfoDTO saleInfoDTO = accountService.querySaleInfo(orderFlowContext.getOrderCommitReqDTO().getSaleAccountId());
        orderFlowContext.getOrderCommitReqDTO().setSaleInfoDTO(saleInfoDTO);
        orderFlowContext.setNextNodeName("calcPrice");
    }

    @Node
    void calcPrice(OrderFlowContext orderFlowContext) {
        promotionService.calcPrice(orderFlowContext.getOrderCommitReqDTO());
        orderFlowContext.setNextNodeName("generateDistributedId");
    }

    @Node
    void generateDistributedId(OrderFlowContext orderFlowContext) {
        Long id = 1000000001L;
        orderFlowContext.getOrderCommitReqDTO().setOrderId(id);
        orderFlowContext.setNextNodeName("createOrder");
    }

    @Node
    void createOrder(OrderFlowContext orderFlowContext) throws BusinessException {
        OrderDTO orderDTO = orderService.createOrder(orderFlowContext.getOrderCommitReqDTO());
        orderFlowContext.setOrderDTO(orderDTO);
        orderFlowContext.setNextNodeName("lockInventory");
    }

    @Node(rollbackNode = "lockInventoryRollback")
    void lockInventory(OrderFlowContext orderFlowContext) throws BusinessException {
        logger.info("begin to lockInventory...");
        logger.info("end to lockInventory");
        orderFlowContext.setNextNodeName("lockCoupon");
        //        throw new RuntimeException("lockInventory failed test!");
    }

    @RollbackNode
    void lockInventoryRollback(OrderFlowContext orderFlowContext) throws BusinessException {
        logger.info("begin to lockInventoryRollback...");
        logger.info("end to lockInventoryRollback");
    }

    @Node(rollbackNode = "lockCouponRollback")
    void lockCoupon(OrderFlowContext orderFlowContext) {
        logger.info("begin to lockCoupon...");
        logger.info("end to lockCoupon");
        orderFlowContext.setNextNodeName("changeOrderStatus");
//        throw new RuntimeException("lockCoupon failed test!");
    }

    @RollbackNode
    void lockCouponRollback(OrderFlowContext orderFlowContext) {
        logger.info("begin to lockCouponRollback...");
        logger.info("end to lockCouponRollback");
    }

    @Node
    void changeOrderStatus(OrderFlowContext orderFlowContext) {
        orderFlowContext.getOrderDTO().setOrderStatus(20);
        orderFlowContext.setNextNodeName("sendEvent");
    }

    @Node
    void sendEvent(OrderFlowContext orderFlowContext) {
        logger.info("当前订单信息：" + orderFlowContext);
        logger.info("begin to send orderCommitted event ...");
        logger.info("end to send orderCommitted event!");
    }
}
