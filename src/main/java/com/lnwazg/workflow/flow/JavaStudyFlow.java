package com.lnwazg.workflow.flow;

import com.lnwazg.workflow.anno.Node;
import com.lnwazg.workflow.anno.StartNode;
import com.lnwazg.workflow.anno.WorkFlow;
import com.lnwazg.workflow.engine.BusinessException;
import com.lnwazg.workflow.engine.WorkFlowContext;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@WorkFlow
@Component
public class JavaStudyFlow {
    private Logger logger = LoggerFactory.getLogger(JavaStudyFlow.class);

    @StartNode
    @Node
    void findABook(MyContext workFlowContext) throws BusinessException {
        workFlowContext.setNextNodeName("read");
    }

    @Node
    void read(MyContext workFlowContext) throws BusinessException {
        workFlowContext.setReadTimes(workFlowContext.getReadTimes() + 1);
        workFlowContext.setNextNodeName("rest");
        logger.info("times" + workFlowContext.getReadTimes());
    }

    @Node
    void rest(MyContext workFlowContext) throws BusinessException {
        if (workFlowContext.getReadTimes() == 5) {
            workFlowContext.setNextNodeName("end");
        } else {
            workFlowContext.setNextNodeName("read");
        }
    }

    @Node
    void end(MyContext workFlowContext) throws BusinessException {
    }

    /**
     * 自定义工作流上下文，可扩展
     */
    @Data
    public static class MyContext extends WorkFlowContext {
        private int readTimes;
    }
}
