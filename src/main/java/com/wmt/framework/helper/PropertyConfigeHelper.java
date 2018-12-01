package com.wmt.framework.helper;

import com.wmt.framework.serialization.common.SerializeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件中的值
 * Created by weimiantong on 18/11/11.
 */
public class PropertyConfigeHelper {
    private static final Logger logger = LoggerFactory.getLogger(PropertyConfigeHelper.class);
    private static final String PROPERTY_CLASSPATH = "/mini_dubbo.properties";
    private static final Properties properties = new Properties();

    /**
     * ZK服务地址
     */
    private static String zkService = "";
    /**
     * ZK session超时时间
     */
    private static int zkSessionTimeout;
    /**
     * ZK connection超时时间
     */
    private static int zkConnectionTimeout;
    /**
     * 序列化算法类型
     */
    private static SerializeType serializeType;
    /**
     * 每个服务端提供者的Netty的连接数
     */
    private static int channelConnectSize;

    /**
     * 初始化,读取配置文件值并加载到内存
     */
    static {
        InputStream is = null;
        try {
            is = PropertyConfigeHelper.class.getResourceAsStream(PROPERTY_CLASSPATH);
            if (null  == is) {
                throw new IllegalStateException("mini_dubbo.properties can not found in the classpath.");
            }
            properties.load(is);

            zkService = properties.getProperty("zk_service");
            zkSessionTimeout = Integer.parseInt(properties.getProperty("zk_sessionTimeout", "500"));
            zkConnectionTimeout = Integer.parseInt(properties.getProperty("zk_connectionTimeout", "500"));
            channelConnectSize = Integer.parseInt(properties.getProperty("channel_connect_size", "10"));
            String seriType = properties.getProperty("serialize_type");
            serializeType = SerializeType.queryByType(seriType);
            if (serializeType == null) {
                throw new RuntimeException("serializeType is null");
            }
        } catch (Throwable t) {
            logger.warn("load ares_remoting's properties file failed.", t);
            throw new RuntimeException("serializeType is null");
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int getChannelConnectSize() {
        return channelConnectSize;
    }

    public static String getZkService() {
        return zkService;
    }

    public static int getZkSessionTimeout() {
        return zkSessionTimeout;
    }

    public static int getZkConnectionTimeout() {
        return zkConnectionTimeout;
    }

    public static SerializeType getSerializeType() {
        return serializeType;
    }
}
