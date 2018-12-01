package com.wmt.framework.provider;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by weimiantong on 18/11/11.
 */
public class ProviderFactoryBean implements FactoryBean, InitializingBean {
    /**
     * 服务接口
     */
    private Class<?> serviceItf;
    /**
     * 服务实现
     */
    private Object serviceObject;
    /**
     * 服务端口
     */
    private String servicePort;
    /**
     * 服务超时时间
     */
    private long timeout;
    /**
     * 服务代理对象
     */
    private Object serviceProxyObject;
    /**
     * 服务提供者唯一标识
     */
    private String appKey;
    /**
     * 服务分组名称
     */
    private String groupName = "default";
    /**
     * 服务提供者权重,默认为1 范围1-100
     */
    private int weight = 1;
    /**
     * 服务端线程数, 默认为10个线程
     */
    private int workerThreads = 10;

    @Override
    public Object getObject() throws Exception {
        return serviceProxyObject;
    }

    @Override
    public Class<?> getObjectType() {
        return serviceItf;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
