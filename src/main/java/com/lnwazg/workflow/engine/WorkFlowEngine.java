package com.lnwazg.workflow.engine;

import com.lnwazg.workflow.engine.anno.Node;
import com.lnwazg.workflow.engine.anno.RollbackNode;
import com.lnwazg.workflow.engine.anno.StartNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
        Map<String, Method> flowNameNodeMap = new LinkedHashMap<>();
        Map<String, Method> flowNameRollbackNodeMap = new LinkedHashMap<>();
        Map<String, String> node2rollbackMap = new HashMap<>();
        for (Method m : declaredMethods) {
            m.setAccessible(true);
            if (m.isAnnotationPresent(StartNode.class)) {
                startNodeMethod = m;
            } else if (m.isAnnotationPresent(Node.class)) {
                flowNameNodeMap.put(m.getName(), m);
                Node node = m.getAnnotation(Node.class);
                String rollbackNode = node.rollbackNode();
                if (!StringUtils.isEmpty(rollbackNode)) {
                    node2rollbackMap.put(m.getName(), rollbackNode);
                    flowNameRollbackNodeMap.put(rollbackNode,
                            Arrays.asList(declaredMethods)
                                    .stream()
                                    .filter(
                                            x -> {
                                                x.setAccessible(true);
                                                //该方法必须被标记为回滚节点
                                                return x.isAnnotationPresent(RollbackNode.class) && rollbackNode.equals(x.getName());
                                            }
                                    ).findFirst().get()
                    );
                }
            }
        }
        if (startNodeMethod == null) {
            logger.error("流程【" + workFlowName + "】未设置起始节点，忽略执行！");
            return;
        }
        logger.info("流程【" + workFlowName + "】开始运行。起始节点：" + startNodeMethod.getName() + "，后续可选节点：" + flowNameNodeMap.keySet());

        String nextNodeName = null;
        Method nextMethod = null;
        //将起始节点加入
        workFlowContext.setNextNodeName(startNodeMethod.getName());
        flowNameNodeMap.put(startNodeMethod.getName(), startNodeMethod);
        List<String> rollbackNames = new ArrayList<>();
        try {
            //一直执行，直到执行完所有的流程
            while (!StringUtils.isEmpty(nextNodeName = workFlowContext.getNextNodeName())) {
                nextMethod = flowNameNodeMap.get(nextNodeName);
                if (nextMethod != null) {
                    workFlowContext.setNextNodeName(null);
                    //记录相应的回滚node
                    String rollbackNodeName = node2rollbackMap.get(nextNodeName);
                    if (!StringUtils.isEmpty(rollbackNodeName)) {
                        rollbackNames.add(rollbackNodeName);
                    }
                    logger.info("开始执行节点【" + nextMethod.getName() + "】");
                    //框架记录每个流程的执行耗时
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
            logger.error("流程【" + workFlowName + "】执行节点【" + nextMethod.getName() + "】 出现异常！", e);
            //若走到某一步执行失败了，则要依次执行每一个已执行完的流程的回滚流程（若存在rollback流程）
            logger.info("即将执行的回滚节点列表：" + rollbackNames.toString());
            if (!CollectionUtils.isEmpty(rollbackNames)) {
                //开始回滚
                rollbackNames.stream().forEach(x -> {
                    logger.info("begin to rollback: " + x);
                    Method method = flowNameRollbackNodeMap.get(x);
                    try {
                        method.invoke(workFlow, workFlowContext);
                    } catch (IllegalAccessException illegalAccessException) {
                        illegalAccessException.printStackTrace();
                    } catch (InvocationTargetException invocationTargetException) {
                        invocationTargetException.printStackTrace();
                    }
                });
            }
        } finally {
            logger.info("流程【" + workFlowName + "】运行完毕。");
        }
    }
}
