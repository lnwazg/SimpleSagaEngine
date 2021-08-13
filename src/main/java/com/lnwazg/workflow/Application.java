package com.lnwazg.workflow;

import com.lnwazg.workflow.engine.WorkFlowEngine;
import com.lnwazg.workflow.flow.context.OrderFlowContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.lnwazg")
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        //获取引擎实例
        WorkFlowEngine workFlowEngine = (WorkFlowEngine) context.getBean("workFlowEngine");

        //构造下单流程的上下文
        OrderFlowContext orderFlowContext = new OrderFlowContext();
        //执行订单提交流程
        workFlowEngine.runWorkFlow("orderCommitFlow", orderFlowContext);
        //执行订单支持流程
        workFlowEngine.runWorkFlow("orderPayFlow", orderFlowContext);
        //执行订单履约流程
        workFlowEngine.runWorkFlow("orderFulfillingFlow", orderFlowContext);
    }
}
