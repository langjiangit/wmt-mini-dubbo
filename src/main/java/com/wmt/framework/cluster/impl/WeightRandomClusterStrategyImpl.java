package com.wmt.framework.cluster.impl;

import avro.shaded.com.google.common.collect.Lists;
import com.wmt.framework.cluster.ClusterStrategy;
import com.wmt.framework.model.ProviderService;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

/**
 *
 * @author weimiantong
 * @date 19/1/21
 */
public class WeightRandomClusterStrategyImpl implements ClusterStrategy{
    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        //存放加权后的服务提供者列表
        List<ProviderService> providerList = Lists.newArrayList();
        for (ProviderService provider : providerServices) {
            int weight = provider.getWeight();
            for (int i = 0; i < weight; i++) {
                providerList.add(provider.copy());
            }
        }

        int MAX_LEN = providerList.size();
        int index = RandomUtils.nextInt(0, MAX_LEN - 1);
        return providerList.get(index);
    }
}
