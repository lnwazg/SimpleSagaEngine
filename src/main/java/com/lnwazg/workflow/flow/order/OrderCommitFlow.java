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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
@Slf4j
public class OrderCommitFlow extends AbstractFlow<OrderFlowContext> {
    @Resource
    private OrderService orderService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private AccountService accountService;
    @Resource
    private PromotionService promotionService;

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
        log.info("begin to lockInventory...");
        log.info("end to lockInventory");
        orderFlowContext.setNextNodeName("lockCoupon");
        //        throw new RuntimeException("lockInventory failed test!");
    }

    @RollbackNode
    void lockInventoryRollback(OrderFlowContext orderFlowContext) throws BusinessException {
        log.info("begin to lockInventoryRollback...");
        log.info("end to lockInventoryRollback");
    }

    @Node(rollbackNode = "lockCouponRollback")
    void lockCoupon(OrderFlowContext orderFlowContext) {
        log.info("begin to lockCoupon...");
        log.info("end to lockCoupon");
        orderFlowContext.setNextNodeName("changeOrderStatus");
//        throw new RuntimeException("lockCoupon failed test!");
    }

    @RollbackNode
    void lockCouponRollback(OrderFlowContext orderFlowContext) {
        log.info("begin to lockCouponRollback...");
        log.info("end to lockCouponRollback");
    }

    @Node
    void changeOrderStatus(OrderFlowContext orderFlowContext) {
        orderFlowContext.getOrderDTO().setOrderStatus(20);
        orderFlowContext.setNextNodeName("sendEvent");
    }

    @Node
    void sendEvent(OrderFlowContext orderFlowContext) {
        log.info("当前订单信息：" + orderFlowContext);
        log.info("begin to send orderCommitted event ...");
        log.info("end to send orderCommitted event!");
    }
}
