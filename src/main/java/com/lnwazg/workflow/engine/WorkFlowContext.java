package com.lnwazg.workflow.engine;

import lombok.Data;

/**
 * 工作流上下文
 */
@Data
public class WorkFlowContext {
    /**
     * 下一个处理节点的名称
     */
    private String nextNodeName;
}
