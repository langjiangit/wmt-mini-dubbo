package com.wmt.framework.cluster;

import com.wmt.framework.model.ProviderService;

import java.util.List;

/**
 *
 * @author weimiantong
 * @date 19/1/20
 */
public interface ClusterStrategy {
    /**
     * 负载策略算法选择
     *
     * @param providerServices
     * @return
     */
     ProviderService select(List<ProviderService> providerServices);
}
