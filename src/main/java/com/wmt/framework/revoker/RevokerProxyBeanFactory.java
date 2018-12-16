package com.wmt.framework.revoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;

/**
 * 消费端bean代理工厂
 * Created by weimiantong on 18/12/2.
 */
public class RevokerProxyBeanFactory implements InvocationHandler{

    private ExecutorService fixedThreadPool = null;

    //服务接口
    private Class<?> targetInterface;
    //超时时间
    private int consumeTimeout;
    //调用者线程数
    private static int threadWorkerNumber = 10;
    //负载均衡策略
    private String clusterStrategy;

    public RevokerProxyBeanFactory(Class<?> targetInterface, int consumeTimeout, String clusterStrategy) {
        this.targetInterface = targetInterface;
        this.consumeTimeout = consumeTimeout;
        this.clusterStrategy = clusterStrategy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{targetInterface}, this);
    }

    private static volatile RevokerProxyBeanFactory singleton;

    public static RevokerProxyBeanFactory singleton(Class<?> targetInterface, int consumeTimeout, String clusterStrategy) {
        if (singleton == null) {
            synchronized (RevokerProxyBeanFactory.class) {
                if (singleton == null) {
                    singleton = new RevokerProxyBeanFactory(targetInterface, consumeTimeout, clusterStrategy);
                }
            }
        }
        return singleton;
    }
}
