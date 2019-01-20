package com.wmt.framework.cluster.impl;

import com.wmt.framework.cluster.ClusterStrategy;
import com.wmt.framework.helper.IPHelper;
import com.wmt.framework.model.ProviderService;

import java.util.List;

/**
 *  软负载哈希算法实现
 *
 * @author weimiantong
 * @date 19/1/20
 */
public class HashClusterStrategyImpl implements ClusterStrategy{
    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        //获取调用方ip
        String localIP = IPHelper.localIp();
        //获取源地址对应的hashcode
        int hashCode = localIP.hashCode();
        //获取服务列表大小
        int size = providerServices.size();

        return providerServices.get(hashCode % size);
    }
}
