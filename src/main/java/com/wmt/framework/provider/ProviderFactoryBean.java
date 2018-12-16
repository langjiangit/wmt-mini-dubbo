package com.wmt.framework.provider;

import avro.shaded.com.google.common.collect.Lists;
import com.wmt.framework.helper.IPHelper;
import com.wmt.framework.model.ProviderService;
import com.wmt.framework.zookeeper.IRegisterCenter4Provider;
import com.wmt.framework.zookeeper.RegisterCenter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Method;
import java.util.List;

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
    private String serverPort;
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
        //启动Netty服务端
        NettyServer.singleton().start(Integer.parseInt(serverPort));

        //注册到zk,元数据注册中心
        List<ProviderService> providerServiceList = buildProviderServiceInfos();
        IRegisterCenter4Provider iRegisterCenter4Provider = RegisterCenter.singleton();
        iRegisterCenter4Provider.registerProvider(providerServiceList);
    }

    private List<ProviderService> buildProviderServiceInfos() {
        List<ProviderService> providerList = Lists.newArrayList();
        Method[] methods = serviceObject.getClass().getDeclaredMethods();
        for (Method method : methods) {
            ProviderService providerService = new ProviderService();
            providerService.setServiceItf(serviceItf);
            providerService.setServiceObject(serviceObject);
            providerService.setServerIp(IPHelper.localIp());
            providerService.setServerPort(Integer.parseInt(serverPort));
            providerService.setTimeout(timeout);
            providerService.setServiceMethod(method);
            providerService.setWeight(weight);
            providerService.setWorkerThreads(workerThreads);
            providerService.setAppKey(appKey);
            providerService.setGroupName(groupName);
            providerList.add(providerService);
        }
        return providerList;
    }
}
