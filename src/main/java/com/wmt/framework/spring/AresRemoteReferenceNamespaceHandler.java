package com.wmt.framework.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 服务引入自定义标签
 *
 * Created by weimiantong on 18/12/2.
 */
public class AresRemoteReferenceNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("reference", new RevokerFactoryBeanDefinitionParser());
    }
}
