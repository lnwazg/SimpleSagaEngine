package com.lnwazg.workflow.engine;

import lombok.Data;

import java.util.UUID;

/**
 * 基础的工作流上下文
 */
@Data
public class BaseWorkFlowContext {
    /**
     * 当前的事务Id
     */
    private String txId = UUID.randomUUID().toString();
    /**
     * 下一个要处理节点的名称
     */
    private String nextNodeName;
}
