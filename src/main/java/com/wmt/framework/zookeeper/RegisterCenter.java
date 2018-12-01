package com.wmt.framework.zookeeper;

import avro.shaded.com.google.common.base.Function;
import avro.shaded.com.google.common.collect.Lists;
import avro.shaded.com.google.common.collect.Maps;
import com.alibaba.fastjson.JSON;
import com.wmt.framework.helper.IPHelper;
import com.wmt.framework.helper.PropertyConfigeHelper;
import com.wmt.framework.model.InvokerService;
import com.wmt.framework.model.ProviderService;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 注册中心实现
 * Created by weimiantong on 18/12/1.
 */
public class RegisterCenter implements IRegisterCenter4Invoker, IRegisterCenter4Provider{
    /**
     * 饿汉式单例
     */
    private static RegisterCenter registerCenter = new RegisterCenter();

    /**
     * 服务提供者列表,Key:服务提供者接口  value:服务提供者服务方法列表
     */
    private static final Map<String, List<ProviderService>> providerServiceMap = Maps.newConcurrentMap();

    /**
     * 服务端ZK服务元信息,选择服务(第一次直接从ZK拉取,后续由ZK的监听机制主动更新)
     */
    private static final Map<String, List<ProviderService>> serviceMetaDataMap4Consume = Maps.newConcurrentMap();

    private static String ZK_SERVICE = PropertyConfigeHelper.getZkService();
    private static int ZK_SESSION_TIME_OUT = PropertyConfigeHelper.getZkConnectionTimeout();
    private static int ZK_CONNECTION_TIME_OUT = PropertyConfigeHelper.getZkConnectionTimeout();
    private static String ROOT_PATH = "/config_register";
    public static String PROVIDER_TYPE = "provider";
    public static String INVOKER_TYPE = "consumer";
    private static volatile ZkClient zkClient = null;

    private RegisterCenter() {
    }

    public static RegisterCenter singleton() {
        return registerCenter;
    }
    @Override
    public void registerProvider(List<ProviderService> serviceMetaData) {
        if (CollectionUtils.isEmpty(serviceMetaData)) {
            return;
        }
        //连接zk,注册服务
        synchronized (RegisterCenter.class) {
            for (ProviderService provider : serviceMetaData) {
                String serviceItfKey = provider.getServiceItf().getName();
                List<ProviderService> providers = providerServiceMap.get(serviceItfKey);
                if (providers == null) {
                    providers = Lists.newArrayList();
                }
                providers.add(provider);
                providerServiceMap.put(serviceItfKey, providers);
            }

            //创建zk连接客户端
            if (zkClient == null) {
                zkClient = new ZkClient(ZK_SERVICE, ZK_SESSION_TIME_OUT, ZK_CONNECTION_TIME_OUT, new SerializableSerializer());
            }
            //创建 ZK命名空间/当前部署应用APP命名空间/
            String APP_KEY = serviceMetaData.get(0).getAppKey();
            String ZK_PATH = ROOT_PATH + "/" + APP_KEY;
            boolean exist = zkClient.exists(ROOT_PATH);
            if (!exist) {
                zkClient.createPersistent(ROOT_PATH, true);
            }

            for(Map.Entry<String, List<ProviderService>> entry : providerServiceMap.entrySet()) {
                //服务分组
                String groupName = entry.getValue().get(0).getGroupName();
                //创建服务提供者节点
                String serviceNode = entry.getKey();
                String servicePath = ZK_PATH + "/" + groupName + "/" + serviceNode + "/" + PROVIDER_TYPE;
                exist = zkClient.exists(servicePath);
                if (!exist) {
                    zkClient.createPersistent(servicePath, true);
                }

                //创建当前服务器节点
                int serverPort = entry.getValue().get(0).getServicePort();//服务端口
                int weight = entry.getValue().get(0).getWeight();//服务权重
                int workerThreads =  entry.getValue().get(0).getWorkerThreads();//服务工作线程

                String localIp = IPHelper.localIp();
                String currentServiceIpNode = servicePath + "/" + localIp + "|" + serverPort + "|" + weight + "|" + workerThreads + "|" + groupName;
                exist = zkClient.exists(currentServiceIpNode);
                if(!exist) {
                    //注意,这里创建的是临时节点
                    zkClient.createEphemeral(currentServiceIpNode);
                }

                //监听注册服务的变化,同时更新数据到本地缓存
                zkClient.subscribeChildChanges(servicePath, new IZkChildListener() {
                    @Override
                    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                        if (currentChilds == null) {
                            currentChilds = Lists.newArrayList();
                        }

                        //存活的服务IP列表
                        List<String> activityServiceIpList = Lists.newArrayList(Lists.transform(currentChilds, new Function<String, String>() {
                            @Override
                            public String apply(String input) {
                                return StringUtils.split(input, "|")[0];
                            }
                        }));
                        refreshActivityService(activityServiceIpList);
                    }
                });
            }
        }

    }

    //利用ZK自动刷新当前存活的服务提供者列表数据
    private void refreshActivityService(List<String> serviceIpList) {
        if (serviceIpList == null) {
            serviceIpList = Lists.newArrayList();
        }

        Map<String, List<ProviderService>> currentServiceMetaDataMap = Maps.newHashMap();
        for (Map.Entry<String, List<ProviderService>> entry : providerServiceMap.entrySet()) {
            String key = entry.getKey();
            List<ProviderService> providerServices = entry.getValue();

            List<ProviderService> serviceMetaDataModelList = currentServiceMetaDataMap.get(key);
            if (serviceMetaDataModelList == null) {
                serviceMetaDataModelList = Lists.newArrayList();
            }
            for (ProviderService serviceMetaData : providerServices) {
                if (serviceIpList.contains(serviceMetaData.getServerIp())) {
                    serviceMetaDataModelList.add(serviceMetaData);
                }
            }
            currentServiceMetaDataMap.put(key, serviceMetaDataModelList);
        }
        providerServiceMap.clear();
        System.out.println("currentServiceMetaDataMap,"+ JSON.toJSONString(currentServiceMetaDataMap));
        providerServiceMap.putAll(currentServiceMetaDataMap);
    }

    @Override
    public void initProviderMap(String remoteAppKey, String groupName) {

    }

    @Override
    public Map<String, List<ProviderService>> getServiceMetaDataMap4Consume() {
        return null;
    }

    @Override
    public void registerInvoker(InvokerService invoker) {

    }


    @Override
    public Map<String, List<ProviderService>> getProviderServiceMap() {
        return null;
    }
}
