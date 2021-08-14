package com.lnwazg.workflow.engine;

import com.lnwazg.workflow.engine.anno.Node;
import com.lnwazg.workflow.engine.anno.StartNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
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

    public void runWorkFlow(AbstractFlow workFlow, BaseWorkFlowContext workFlowContext) {
        String workFlowName = StringUtils.uncapitalize(workFlow.getClass().getSimpleName());
        Method[] declaredMethods = workFlow.getClass().getDeclaredMethods();
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
        if (startNodeMethod == null) {
            logger.error("流程【" + workFlowName + "】未设置起始节点，忽略执行！");
            return;
        }
        logger.info("流程【" + workFlowName + "】开始运行。起始节点：" + startNodeMethod.getName() + "，后续可选节点：" + flowNodeMap.keySet());
        try {
            String nextNodeName = null;
            Method nextMethod = null;
            workFlowContext.setNextNodeName(startNodeMethod.getName());
            flowNodeMap.put(startNodeMethod.getName(), startNodeMethod);
            //一直执行，直到执行完所有的流程
            while (!StringUtils.isEmpty(nextNodeName = workFlowContext.getNextNodeName())) {
                nextMethod = flowNodeMap.get(nextNodeName);
                if (nextMethod != null) {
                    workFlowContext.setNextNodeName(null);
                    logger.info("开始执行节点【" + nextMethod.getName() + "】");
                    //框架记录每个流程的执行耗时
                    //若走到某一步执行失败了，则要依次执行每一个已执行完的流程的回滚流程（若存在rollback流程）
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    nextMethod.invoke(workFlow, workFlowContext);
                    stopWatch.stop();
                    long totalTimeMillis = stopWatch.getTotalTimeMillis();
                    logger.info("结束执行节点【" + nextMethod.getName() + "】，耗时：" + totalTimeMillis + "ms");
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
