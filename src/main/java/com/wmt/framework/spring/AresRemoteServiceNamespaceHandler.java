package com.wmt.framework.spring;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 服务发布自定义标签
 * Created by weimiantong on 18/12/16.
 */
public class AresRemoteServiceNamespaceHandler extends NamespaceHandlerSupport{

    @Override
    public void init() {
        registerBeanDefinitionParser("service", new ProviderFactoryBeanDefinitionParser());
    }
}
