package com.wmt.framework.zookeeper;

import com.wmt.framework.model.ProviderService;

import java.util.List;
import java.util.Map;

/**
 *  服务端注册中心接口
 * Created by weimiantong on 18/12/1.
 */
public interface IRegisterCenter4Provider {
    /**
     * 服务端将服务提供者信息注册到zk对应的节点下
     */
    void registerProvider(final List<ProviderService> serviceMetaData);

    /**
     * 服务端获取服务提供者信息
     *
     * 注:返回对象,Key:服务提供者接口  value:服务提供者服务方法列表
     *
     * @return
     */
    Map<String, List<ProviderService>> getProviderServiceMap();
}
