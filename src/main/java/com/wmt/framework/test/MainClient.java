package com.wmt.framework.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 *
 * @author weimiantong
 * @version $Id$
 */
public class MainClient {

    private static final Logger logger = LoggerFactory.getLogger(MainClient.class);

    public static void main(String[] args) throws Exception {

        //引入远程服务
//        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("hessian-rpc-client.xml");
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("test-client.xml");

        //获取远程服务
        final HelloService helloService = (HelloService) context.getBean("remoteHelloService");
     // final HelloService helloService = (HelloService) context.getBean("userServiceHessianProxy");


        long count = 1000000000000000000L;

        //调用服务并打印结果
        for (int i = 0; i < count; i++) {
            try {
                String result = helloService.sayHello("weimiantong,i=" + i);
                System.out.println(result);
            } catch (Exception e) {
                logger.warn("--------", e);
            }
        }

        //关闭jvm
        System.exit(0);
    }
}
