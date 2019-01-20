package com.wmt.framework.cluster.engine;

import avro.shaded.com.google.common.collect.Maps;
import com.wmt.framework.cluster.ClusterStrategy;
import com.wmt.framework.cluster.impl.ClusterStrategyEnum;

import java.util.Map;

/**
 *  负载均衡引擎
 * @author weimiantong
 * @date 19/1/20
 */
public class ClusterEngine {
    private static final Map<ClusterStrategyEnum, ClusterStrategy> clusterStrategyMap = Maps.newConcurrentMap();
    
}
