package com.fly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Hello world!
 */
public class App {
    
    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    
    
    public static void main(String[] args) {
        
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        LOG.info("start app...");
    }
}
