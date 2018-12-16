package com.wmt.framework.spring;

import com.wmt.framework.provider.ProviderFactoryBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * 解析远程服务发布的自定义标签类,解析标签中配置的xml属性,将属性值写入远程服务引入的Bean中,完成服务发布对象的构建
 * Created by weimiantong on 18/12/16.
 */
public class ProviderFactoryBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private static final Logger logger = LoggerFactory.getLogger(ProviderFactoryBeanDefinitionParser.class);

    @Override
    protected Class getBeanClass(Element element) {
        return ProviderFactoryBean.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {

        try {
            String serviceItf = element.getAttribute("interface");
            String timeOut = element.getAttribute("timeout");
            String serverPort = element.getAttribute("serverPort");
            String ref = element.getAttribute("ref");
            String weight = element.getAttribute("weight");
            String workerThreads = element.getAttribute("workerThreads");
            String appKey = element.getAttribute("appKey");
            String groupName = element.getAttribute("groupName");

            bean.addPropertyValue("serverPort", Integer.parseInt(serverPort));
            bean.addPropertyValue("timeout", Integer.parseInt(timeOut));
            bean.addPropertyValue("serviceItf", Class.forName(serviceItf));
            bean.addPropertyReference("serviceObject", ref);
            bean.addPropertyValue("appKey", appKey);

            if (NumberUtils.isNumber(weight)) {
                bean.addPropertyValue("weight", Integer.parseInt(weight));
            }
            if (NumberUtils.isNumber(workerThreads)) {
                bean.addPropertyValue("workerThreads", Integer.parseInt(workerThreads));
            }
            if (StringUtils.isNotBlank(groupName)) {
                bean.addPropertyValue("groupName", groupName);
            }

        } catch (Exception e) {
            logger.error("ProviderFactoryBeanDefinitionParser error.", e);
            throw new RuntimeException(e);
        }
    }

}
