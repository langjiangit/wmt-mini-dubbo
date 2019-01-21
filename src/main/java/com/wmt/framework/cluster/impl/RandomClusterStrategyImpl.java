package com.wmt.framework.cluster.impl;

import com.wmt.framework.cluster.ClusterStrategy;
import com.wmt.framework.model.ProviderService;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

/**
 * 软负载随机算法实现
 *
 * @author weimiantong
 * @date 19/1/21
 */
public class RandomClusterStrategyImpl implements ClusterStrategy {
    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        int MAX_LEN = providerServices.size();
        return providerServices.get(RandomUtils.nextInt(0, MAX_LEN - 1));
    }
}
