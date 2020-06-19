# SimpleWorkFlowAnno
注解版的简易工作流引擎。

#### Show Me Your Code
```
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
//        workFlowContext.setNextNodeName("failProcessor");
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

#### 运行效果
```
2020-06-19 23:19:12.659  INFO 11520 --- [           main] com.lnwazg.workflow.Application          : Starting Application on PC-20160723SEYR with PID 11520 (D:\Documents\GitHub\SimpleWorkFlowAnno\out\production\classes started by Administrator in D:\Documents\GitHub\SimpleWorkFlowAnno)
2020-06-19 23:19:12.661  INFO 11520 --- [           main] com.lnwazg.workflow.Application          : No active profile set, falling back to default profiles: default
2020-06-19 23:19:13.150  INFO 11520 --- [           main] com.lnwazg.workflow.Application          : Started Application in 0.811 seconds (JVM running for 1.152)
2020-06-19 23:19:13.153  INFO 11520 --- [           main] c.lnwazg.workflow.engine.WorkFlowEngine  : 起始节点：start，后续可选节点：[releaselockInventoryProcessor, decreaseInventoryProcessor, createOrderProcessor, failProcessor, lockInventoryProcessor]
2020-06-19 23:19:13.153  INFO 11520 --- [           main] c.lnwazg.workflow.flow.PlaceAnOrderFlow  : LockInventoryProcessor processing...
2020-06-19 23:19:13.153  INFO 11520 --- [           main] c.lnwazg.workflow.flow.PlaceAnOrderFlow  : decreaseInventoryProcessor processing...
2020-06-19 23:19:13.153  INFO 11520 --- [           main] c.lnwazg.workflow.flow.PlaceAnOrderFlow  : createOrderProcessor processing...
2020-06-19 23:19:13.153  INFO 11520 --- [           main] c.lnwazg.workflow.flow.PlaceAnOrderFlow  : releaselockInventoryProcessor processing...
```