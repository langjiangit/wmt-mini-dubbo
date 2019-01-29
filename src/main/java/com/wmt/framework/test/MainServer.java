package com.wmt.framework.test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author weimiantong
 * @version $Id$
 */
public class MainServer {


    public static void main(String[] args) throws Exception {

        //发布服务
//        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("test-rpc-server.xml");
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("ares-serverBAK.xml");
        System.out.println(" 服务发布完成");
    }
}
