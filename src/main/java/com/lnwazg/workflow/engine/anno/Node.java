package com.lnwazg.workflow.engine.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记这是一个流程节点
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Node {
    /**
     * 该节点对应的回滚节点名称
     *
     * @return
     */
    String rollbackNode() default "";
}
