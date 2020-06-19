package com.lnwazg.workflow.engine;

import com.lnwazg.workflow.anno.Node;
import com.lnwazg.workflow.anno.StartNode;
import com.lnwazg.workflow.anno.WorkFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 工作流引擎
 */
@Component
public class WorkFlowEngine {
    private Logger logger = LoggerFactory.getLogger(WorkFlowEngine.class);
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 运行一个工作流
     *
     * @param workFlowName
     * @param workFlowContext
     */
    public void runWorkFlow(String workFlowName, WorkFlowContext workFlowContext) {
        //获取引擎实例
        Object workFlow = applicationContext.getBean(workFlowName);
        Class<?> workFlowClazz = workFlow.getClass();
        if (!workFlowClazz.isAnnotationPresent(WorkFlow.class)) {
            logger.error(workFlowName + "不是一个工作流，忽略执行！");
            return;
        }
        Method[] declaredMethods = workFlowClazz.getDeclaredMethods();
        //起始节点
        Method startNodeMethod = null;
        //节点表
        Map<String, Method> flowNodeMap = new HashMap<>();
        for (Method m : declaredMethods) {
            m.setAccessible(true);
            if (m.isAnnotationPresent(StartNode.class)) {
                startNodeMethod = m;
            } else if (m.isAnnotationPresent(Node.class)) {
                flowNodeMap.put(m.getName(), m);
            }
        }
        logger.info("起始节点：" + startNodeMethod.getName() + "，后续可选节点：" + flowNodeMap.keySet());
        try {
            startNodeMethod.invoke(workFlow, workFlowContext);
            String nextNodeName = null;
            Method nextMethod = null;
            //一直执行完所有的流程
            while (!StringUtils.isEmpty(nextNodeName = workFlowContext.getNextNodeName())) {
                nextMethod = flowNodeMap.get(nextNodeName);
                if (nextMethod != null) {
                    workFlowContext.setNextNodeName(null);
                    nextMethod.invoke(workFlow, workFlowContext);
                }
            }
        } catch (Exception e) {
            logger.error(workFlowName + "执行工作流出现异常", e);
        }
    }
}
