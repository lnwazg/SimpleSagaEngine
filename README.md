# SimpleSagaEngine
极简saga事务引擎，借鉴了saga分布式事务模式状态机引擎的设计思路。

#### Show Me Your Code：
```java
/**
 * 订单提交
 */
@WorkFlow
@Component
public class OrderCommitFlow extends AbstractFlow<OrderFlowContext> {
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
  
  @StartNode
  void createOrder(OrderFlowContext orderFlowContext) throws BusinessException {
    OrderDTO orderDTO = orderService.createOrder();
    orderFlowContext.setOrderDTO(orderDTO);
    orderFlowContext.setNextNodeName("lockInventory");
  }

  @Node(rollbackNode = "lockInventoryRollback")
  void lockInventory(OrderFlowContext orderFlowContext) throws BusinessException {
    logger.info("begin to lockInventory...");
    logger.info("end to lockInventory");
    throw new RuntimeException("xxxx");
  }

  @RollbackNode
  void lockInventoryRollback(OrderFlowContext orderFlowContext) throws BusinessException {
    logger.info("begin to lockInventoryRollback...");
    logger.info("end to lockInventoryRollback");
  }
}

```

#### 代码运行效果:
```
2021-08-14 13:38:50.525  INFO 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【orderCommitFlow】开始运行。起始节点：createOrder，后续可选节点：[lockInventory]
2021-08-14 13:38:50.525  INFO 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【createOrder】
2021-08-14 13:38:50.529  INFO 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 结束执行节点【createOrder】，耗时：3ms
2021-08-14 13:38:50.529  INFO 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【lockInventory】
2021-08-14 13:38:50.529  INFO 2464 --- [           main] c.l.workflow.flow.order.OrderCommitFlow  : begin to lockInventory...
2021-08-14 13:38:50.529  INFO 2464 --- [           main] c.l.workflow.flow.order.OrderCommitFlow  : end to lockInventory
2021-08-14 13:38:50.532 ERROR 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【orderCommitFlow】执行节点【lockInventory】 出现异常！

java.lang.reflect.InvocationTargetException: null
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_60]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_60]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_60]
	at java.lang.reflect.Method.invoke(Method.java:497) ~[na:1.8.0_60]
	at com.lnwazg.workflow.engine.WorkFlowEngine.runWorkFlow(WorkFlowEngine.java:84) ~[classes/:na]
	at com.lnwazg.workflow.engine.AbstractFlow.exec(AbstractFlow.java:22) [classes/:na]
	at com.lnwazg.workflow.flow.OrderController.runOrder(OrderController.java:28) [classes/:na]
	at com.lnwazg.workflow.Application.main(Application.java:16) [classes/:na]
Caused by: java.lang.RuntimeException: xxxx
	at com.lnwazg.workflow.flow.order.OrderCommitFlow.lockInventory(OrderCommitFlow.java:55) ~[classes/:na]
	... 8 common frames omitted

2021-08-14 13:38:50.532  INFO 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 即将执行的回滚节点列表：[lockInventoryRollback]
2021-08-14 13:38:50.532  INFO 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : begin to rollback: lockInventoryRollback
2021-08-14 13:38:50.532  INFO 2464 --- [           main] c.l.workflow.flow.order.OrderCommitFlow  : begin to lockInventoryRollback...
2021-08-14 13:38:50.532  INFO 2464 --- [           main] c.l.workflow.flow.order.OrderCommitFlow  : end to lockInventoryRollback
2021-08-14 13:38:50.532  INFO 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【orderCommitFlow】运行完毕。
2021-08-14 13:38:50.532  INFO 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【orderPayFlow】开始运行。起始节点：start，后续可选节点：[]
2021-08-14 13:38:50.532  INFO 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【start】
2021-08-14 13:38:50.532  INFO 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 结束执行节点【start】，耗时：0ms
2021-08-14 13:38:50.532  INFO 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【orderPayFlow】运行完毕。
2021-08-14 13:38:50.532  INFO 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【orderFulfillingFlow】开始运行。起始节点：start，后续可选节点：[]
2021-08-14 13:38:50.532  INFO 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【start】
2021-08-14 13:38:50.533  INFO 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 结束执行节点【start】，耗时：0ms
2021-08-14 13:38:50.533  INFO 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【orderFulfillingFlow】运行完毕。
2021-08-14 13:38:50.533 ERROR 2464 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【orderCancelFlow】未设置起始节点，忽略执行！
```

#### 框架基础：
- 以OrderFlowContext为例，作为下单流程的状态机。
- 以@StartNode标注的方法作为起始节点。
- 以@Node标注的方法作为执行节点（可使用rollbackNode属性指定回滚节点）。
- 以@RollbackNode标注的方法作为回滚节点。

#### 框架特色：
- 免状态机设计器：流程无须事先固化，与繁琐的流程设计工具彻底说拜拜。
- 编排灵活：各执行节点自由灵活控制状态机决定下一个执行节点，也可直接终止。
- 自动补偿：若某个节点执行出现异常，则框架自动按倒序依次执行每个已执行的节点的回滚方法（若配置）。
- 轻量：无须搭建事务控制服务器（例如Seata），正向\逆向流程的幂等控制&回滚能力由各业务节点自行控制。

#### 执行一个完整的下单流程:
```
2021-08-14 21:04:41.915  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【orderCommitFlow】开始运行。起始节点：queryGoodsInfo，后续可选节点：[querySaleInfo, createOrder, calcPrice, lockCoupon, sendEvent, lockInventory, generateDistributedId, changeOrderStatus]
2021-08-14 21:04:41.915  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【queryGoodsInfo】
2021-08-14 21:04:41.916  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 结束执行节点【queryGoodsInfo】，耗时：1ms
2021-08-14 21:04:41.916  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【querySaleInfo】
2021-08-14 21:04:41.917  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 结束执行节点【querySaleInfo】，耗时：0ms
2021-08-14 21:04:41.917  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【calcPrice】
2021-08-14 21:04:41.917  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 结束执行节点【calcPrice】，耗时：0ms
2021-08-14 21:04:41.917  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【generateDistributedId】
2021-08-14 21:04:41.917  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 结束执行节点【generateDistributedId】，耗时：0ms
2021-08-14 21:04:41.917  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【createOrder】
2021-08-14 21:04:41.920  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 结束执行节点【createOrder】，耗时：3ms
2021-08-14 21:04:41.921  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【lockInventory】
2021-08-14 21:04:41.921  INFO 12116 --- [           main] c.l.workflow.flow.order.OrderCommitFlow  : begin to lockInventory...
2021-08-14 21:04:41.921  INFO 12116 --- [           main] c.l.workflow.flow.order.OrderCommitFlow  : end to lockInventory
2021-08-14 21:04:41.921  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 结束执行节点【lockInventory】，耗时：0ms
2021-08-14 21:04:41.921  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【lockCoupon】
2021-08-14 21:04:41.921  INFO 12116 --- [           main] c.l.workflow.flow.order.OrderCommitFlow  : begin to lockCoupon...
2021-08-14 21:04:41.921  INFO 12116 --- [           main] c.l.workflow.flow.order.OrderCommitFlow  : end to lockCoupon
2021-08-14 21:04:41.921  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 结束执行节点【lockCoupon】，耗时：0ms
2021-08-14 21:04:41.921  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【changeOrderStatus】
2021-08-14 21:04:41.921  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 结束执行节点【changeOrderStatus】，耗时：0ms
2021-08-14 21:04:41.921  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【sendEvent】
2021-08-14 21:04:41.996  INFO 12116 --- [           main] c.l.workflow.flow.order.OrderCommitFlow  : 当前订单信息：{"orderCommitReqDTO":{"goodsInfoDTOS":[{"skuId":1,"skuName":"Apple/苹果 Mac mini Apple M1","skuPrice":6545},{"skuId":2,"skuName":"苹果 MacBook Air 13.3英寸M1笔记本电脑教育优惠","skuPrice":6660}],"orderId":1000000001,"payAmount":19750,"saleAccountId":1,"saleInfoDTO":{"accountId":1,"accountName":"Apple旗舰店客服专用账号"},"skuIdList":[1,2],"skuQuantityList":[2,1]},"orderDTO":{"createdTime":"2021-08-14T21:04:41.920","lastModifiedTime":"2021-08-14T21:04:41.920","orderId":1000000001,"orderStatus":20,"payAmount":19750,"saleAccountId":1}}
2021-08-14 21:04:41.996  INFO 12116 --- [           main] c.l.workflow.flow.order.OrderCommitFlow  : begin to send orderCommitted event ...
2021-08-14 21:04:41.996  INFO 12116 --- [           main] c.l.workflow.flow.order.OrderCommitFlow  : end to send orderCommitted event!
2021-08-14 21:04:41.996  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 结束执行节点【sendEvent】，耗时：75ms
2021-08-14 21:04:41.996  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【orderCommitFlow】运行完毕，总计耗时：81ms
2021-08-14 21:04:41.996  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【orderPayFlow】开始运行。起始节点：start，后续可选节点：[]
2021-08-14 21:04:41.996  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【start】
2021-08-14 21:04:41.997  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 结束执行节点【start】，耗时：0ms
2021-08-14 21:04:41.997  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【orderPayFlow】运行完毕，总计耗时：0ms
2021-08-14 21:04:41.997  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【orderFulfillingFlow】开始运行。起始节点：start，后续可选节点：[]
2021-08-14 21:04:41.997  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【start】
2021-08-14 21:04:41.997  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 结束执行节点【start】，耗时：0ms
2021-08-14 21:04:41.997  INFO 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【orderFulfillingFlow】运行完毕，总计耗时：0ms
2021-08-14 21:04:41.997 ERROR 12116 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【orderCancelFlow】未设置起始节点，忽略执行！
```