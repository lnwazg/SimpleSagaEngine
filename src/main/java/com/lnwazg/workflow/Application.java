package com.lnwazg.workflow;

import com.lnwazg.workflow.flow.OrderController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 程序主入口
 */
@ComponentScan("com.lnwazg")
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        OrderController orderController = context.getBean(OrderController.class);
        orderController.runOrder();
    }
}
