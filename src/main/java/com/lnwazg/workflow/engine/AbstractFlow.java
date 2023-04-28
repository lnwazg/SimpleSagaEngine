package com.lnwazg.workflow.engine;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 可执行的流程
 *
 * @param <T>
 */
@Component
public abstract class AbstractFlow<T extends BaseWorkFlowContext> {
    @Resource
    private WorkFlowEngine workFlowEngine;

    public void exec(T t) {
        //执行工作流
        workFlowEngine.runWorkFlow(this, t);
    }
}
