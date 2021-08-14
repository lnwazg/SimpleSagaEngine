package com.lnwazg.workflow.engine;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 可执行的流程
 *
 * @param <T>
 */
public abstract class AbstractFlow<T extends BaseWorkFlowContext> implements ApplicationContextAware {
    private ApplicationContext context;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public void exec(T t) {
        //获取引擎实例
        WorkFlowEngine workFlowEngine = (WorkFlowEngine) context.getBean("workFlowEngine");
        //执行工作流
        workFlowEngine.runWorkFlow(this, t);
    }
}
