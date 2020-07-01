# SimpleWorkFlowAnno
注解版的简易工作流引擎。

#### Show Me Your Code：
```java
/**
 * 下单工作流
 */
@WorkFlow
@Component
public class PlaceAnOrderFlow {
    private Logger logger = LoggerFactory.getLogger(PlaceAnOrderFlow.class);

    /**
     * 起始节点
     *
     * @param workFlowContext
     */
    @StartNode
    @Node
    void start(WorkFlowContext workFlowContext) throws BusinessException {
        workFlowContext.setNextNodeName("lockInventoryProcessor");
    }

    /**
     * 锁库存
     *
     * @param workFlowContext
     */
    @Node
    void lockInventoryProcessor(WorkFlowContext workFlowContext) throws BusinessException {
        logger.info("LockInventoryProcessor processing...");
        workFlowContext.setNextNodeName("decreaseInventoryProcessor");
        //workFlowContext.setNextNodeName("failProcessor");
    }

    /**
     * 减库存
     *
     * @param workFlowContext
     */
    @Node
    void decreaseInventoryProcessor(WorkFlowContext workFlowContext) throws BusinessException {
        logger.info("decreaseInventoryProcessor processing...");
        workFlowContext.setNextNodeName("createOrderProcessor");
    }

    /**
     * 创建订单
     *
     * @param workFlowContext
     */
    @Node
    void createOrderProcessor(WorkFlowContext workFlowContext) throws BusinessException {
        logger.info("createOrderProcessor processing...");
        workFlowContext.setNextNodeName("releaselockInventoryProcessor");
    }

    /**
     * 释放库存锁
     *
     * @param workFlowContext
     */
    @Node
    void releaselockInventoryProcessor(WorkFlowContext workFlowContext) throws BusinessException {
        logger.info("releaselockInventoryProcessor processing...");
    }

    /**
     * 失败处理
     *
     * @param workFlowContext
     */
    @Node
    void failProcessor(WorkFlowContext workFlowContext) throws BusinessException {
        logger.info("failProcessor...");
    }

}

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        //获取引擎实例
        WorkFlowEngine workFlowEngine = (WorkFlowEngine) context.getBean("workFlowEngine");
        //执行引擎
        workFlowEngine.runWorkFlow("placeAnOrderFlow", new WorkFlowContext());
    }
}
```

#### 代码运行效果:
```
2020-07-01 11:16:10.821  INFO 2504 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【placeAnOrderFlow】开始运行。起始节点：start，后续可选节点：[createOrderProcessor, lockInventoryProcessor, decreaseInventoryProcessor, releaselockInventoryProcessor, failProcessor]
2020-07-01 11:16:10.821  INFO 2504 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【start】
2020-07-01 11:16:10.821  INFO 2504 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【lockInventoryProcessor】
2020-07-01 11:16:10.821  INFO 2504 --- [           main] c.lnwazg.workflow.flow.PlaceAnOrderFlow  : LockInventoryProcessor processing...
2020-07-01 11:16:10.821  INFO 2504 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【decreaseInventoryProcessor】
2020-07-01 11:16:10.821  INFO 2504 --- [           main] c.lnwazg.workflow.flow.PlaceAnOrderFlow  : decreaseInventoryProcessor processing...
2020-07-01 11:16:10.821  INFO 2504 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【createOrderProcessor】
2020-07-01 11:16:10.821  INFO 2504 --- [           main] c.lnwazg.workflow.flow.PlaceAnOrderFlow  : createOrderProcessor processing...
2020-07-01 11:16:10.821  INFO 2504 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 开始执行节点【releaselockInventoryProcessor】
2020-07-01 11:16:10.821  INFO 2504 --- [           main] c.lnwazg.workflow.flow.PlaceAnOrderFlow  : releaselockInventoryProcessor processing...
2020-07-01 11:16:10.821  INFO 2504 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 流程【placeAnOrderFlow】运行完毕。
```

#### 框架功能：
- 支持流程内部任意节点（方法名）之间的跳转。
- 若某个节点的代码逻辑未指定下个节点，则该节点为最后一个节点。
- 可手动指定流程的起始节点名称。若不指定，则默认以@StartNode标注的方法作为起始节点。
- （可选）流程意外中断后继续运行的方案：  
  在数据库表增加一个flowNode字段，初始状态该字段为空。    
  执行流程前，将flowNode字段设置到WorkFlowContext的nextNodeName。   
  每执行一个节点，在节点起始代码位置将节点名称更新到flowNode字段。  
  若流程意外中断，则数据库的flowNode字段内容停留在上一个未执行完毕的节点位置，那么重新开始流程即可继续执行。  
  流程重复执行的正确性需要每个流程的代码逻辑做幂等性保障。
  
