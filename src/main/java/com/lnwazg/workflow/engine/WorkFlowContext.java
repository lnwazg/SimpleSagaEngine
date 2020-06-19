package com.lnwazg.workflow.engine;

/**
 * 工作流上下文
 */
public class WorkFlowContext {
    /**
     * 下一个处理节点的名称
     */
    private String nextNodeName;

    public String getNextNodeName() {
        return nextNodeName;
    }

    public void setNextNodeName(String nextNodeName) {
        this.nextNodeName = nextNodeName;
    }
}
