package com.wmt.framework.spring;

import com.wmt.framework.revoker.RevokerFactoryBean;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * 解析远程服务引入的自定义标签类,解析标签中配置的xml属性,将属性值写入远程服务引入的Bean中,完成服务引入对象的构建
 * Created by weimiantong on 18/12/2.
 */
public class RevokerFactoryBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private static final Logger logger = LoggerFactory.getLogger(RevokerFactoryBeanDefinitionParser.class);

    @Override
    protected Class getBeanClass(Element element) {
        return RevokerFactoryBean.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        try {
            String timeOut = element.getAttribute("timeout");
            String targetInterface = element.getAttribute("interface");
            String clusterStrategy = element.getAttribute("clusterStrategy");
            String remoteAppKey = element.getAttribute("remoteAppKey");
            String groupName = element.getAttribute("groupName");

            bean.addPropertyValue("timeout", Integer.parseInt(timeOut));
            bean.addPropertyValue("targetInterface", Class.forName(targetInterface));
            bean.addPropertyValue("remoteAppKey", remoteAppKey);

            if (StringUtils.isNotBlank(clusterStrategy)) {
                bean.addPropertyValue("clusterStrategy", clusterStrategy);
            }

            if (StringUtils.isNotBlank(groupName)) {
                bean.addPropertyValue("groupName", groupName);
            }
        } catch (Exception e) {
            logger.error("RevokerFactoryBeanDefinitionParser error.", e);
            throw new RuntimeException(e);
        }
    }

}
