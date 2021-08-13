package com.lnwazg.workflow.engine;

import lombok.Data;

/**
 * 基础的工作流上下文
 */
@Data
public class BaseWorkFlowContext {
    /**
     * 下一个要处理节点的名称
     */
    private String nextNodeName;
}
