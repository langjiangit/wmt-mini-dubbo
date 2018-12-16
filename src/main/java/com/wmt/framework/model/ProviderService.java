package com.wmt.framework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 远程服务自身的一些属性定义
 * 服务注册中心的服务提供者注册信息
 * Created by weimiantong on 18/11/11.
 */
public class ProviderService implements Serializable{
    /**
     * 服务接口
     */
    private Class<?> serviceItf;
    /**
     * 服务实现
     */
    private Object serviceObject;
    @JsonIgnore
    private transient Method serviceMethod;
    private String serverIp;
    /**
     * 服务端口
     */
    private int serverPort;
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

    public ProviderService copy() {
        ProviderService providerService = new ProviderService();
        providerService.setServiceItf(serviceItf);
        providerService.setServiceObject(serviceObject);
        providerService.setServiceMethod(serviceMethod);
        providerService.setServerIp(serverIp);
        providerService.setServerPort(serverPort);
        providerService.setTimeout(timeout);
        providerService.setWeight(weight);
        providerService.setWorkerThreads(workerThreads);
        providerService.setAppKey(appKey);
        providerService.setGroupName(groupName);
        return providerService;
    }


    public Method getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(Method serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Class<?> getServiceItf() {
        return serviceItf;
    }

    public Object getServiceObject() {
        return serviceObject;
    }

    public int getServerPort() {
        return serverPort;
    }

    public long getTimeout() {
        return timeout;
    }

    public Object getServiceProxyObject() {
        return serviceProxyObject;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getWeight() {
        return weight;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setServiceItf(Class<?> serviceItf) {
        this.serviceItf = serviceItf;
    }

    public void setServiceObject(Object serviceObject) {
        this.serviceObject = serviceObject;
    }

    public void setServerPort(int servicePort) {
        this.serverPort = servicePort;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setServiceProxyObject(Object serviceProxyObject) {
        this.serviceProxyObject = serviceProxyObject;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }
}
