package com.lnwazg.workflow;

import com.lnwazg.workflow.engine.WorkFlowContext;
import com.lnwazg.workflow.engine.WorkFlowEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

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
