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
import java.util.LinkedHashMap;
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
        Map<String, Method> flowNodeMap = new LinkedHashMap<>();
        for (Method m : declaredMethods) {
            m.setAccessible(true);
            if (m.isAnnotationPresent(StartNode.class)) {
                startNodeMethod = m;
            } else if (m.isAnnotationPresent(Node.class)) {
                flowNodeMap.put(m.getName(), m);
            }
        }
        logger.info("流程【" + workFlowName + "】开始运行。起始节点：" + startNodeMethod.getName() + "，后续可选节点：" + flowNodeMap.keySet());
        try {
            //若未指定第一个节点的名称，则默认从首节点开始执行
            if (StringUtils.isEmpty(workFlowContext.getNextNodeName())) {
                //默认执行首节点
                workFlowContext.setNextNodeName(startNodeMethod.getName());
                logger.info("开始执行节点【" + startNodeMethod.getName() + "】");
                //先清空下节点数据
                workFlowContext.setNextNodeName(null);
                //然后执行（执行完毕后，若该节点认为还需要执行下一个流程，则其内部会进行指定）
                startNodeMethod.invoke(workFlow, workFlowContext);
            }

            String nextNodeName = null;
            Method nextMethod = null;
            //一直执行，直到执行完所有的流程
            while (!StringUtils.isEmpty(nextNodeName = workFlowContext.getNextNodeName())) {
                nextMethod = flowNodeMap.get(nextNodeName);
                if (nextMethod != null) {
                    workFlowContext.setNextNodeName(null);
                    logger.info("开始执行节点【" + nextMethod.getName() + "】");
                    nextMethod.invoke(workFlow, workFlowContext);
                } else {
                    //指定了下个节点名称，但实际节点不存在，则直接结束整个流程。
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(workFlowName + "执行工作流出现异常", e);
        } finally {
            logger.info("流程【" + workFlowName + "】运行完毕。");
        }
    }
}
