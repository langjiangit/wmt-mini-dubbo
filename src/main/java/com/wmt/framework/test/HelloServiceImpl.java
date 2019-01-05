package com.wmt.framework.test;

import org.springframework.stereotype.Service;

/**
 * @author liyebing created on 16/10/5.
 * @version $Id$
 */
@Service("helloService")
public class HelloServiceImpl implements HelloService {


    @Override
    public String sayHello(String somebody) {
        return "hello " + somebody + "!";
    }


}
