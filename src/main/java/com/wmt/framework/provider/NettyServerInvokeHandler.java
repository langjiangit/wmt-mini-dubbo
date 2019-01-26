package com.wmt.framework.provider;

import avro.shaded.com.google.common.base.Predicate;
import avro.shaded.com.google.common.collect.Collections2;
import avro.shaded.com.google.common.collect.Maps;
import com.alibaba.fastjson.JSON;
import com.wmt.framework.model.AresRequest;
import com.wmt.framework.model.AresResponse;
import com.wmt.framework.model.ProviderService;
import com.wmt.framework.zookeeper.IRegisterCenter4Provider;
import com.wmt.framework.zookeeper.RegisterCenter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 处理服务端的逻辑  根据解码得到的Java请求对象确定服务提供者的接口以及方法,然后反射发起调用
 * Created by weimiantong on 18/12/1.
 */
public class NettyServerInvokeHandler extends SimpleChannelInboundHandler<AresRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerInvokeHandler.class);

    /**
     * 服务端限流
     */
    private static final Map<String, Semaphore> serviceKeySemaphoreMap = Maps.newConcurrentMap();

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //发生异常,关闭链路
        ctx.close();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AresRequest request) throws Exception {
        if (ctx.channel().isWritable()) {
            //从服务调用对象里获取服务提供者信息
            ProviderService metaDataModel = request.getProviderService();
            long consumeTimeOut = request.getInvokeTimeout();
            final String methodName = request.getInvokedMethodName();

            //根据方法名称定位到具体某一个服务提供者
            String serviceKey = metaDataModel.getServiceItf().getName();
            //获取配置的服务端工作线程数
            int workerThread = metaDataModel.getWorkerThreads();
            //获取限流工具类
            Semaphore semaphore = serviceKeySemaphoreMap.get(serviceKey);
            if (semaphore == null) {
                synchronized (serviceKeySemaphoreMap) {
                    semaphore = serviceKeySemaphoreMap.get(serviceKey);
                    if (semaphore == null) {
                        semaphore = new Semaphore(workerThread);
                        serviceKeySemaphoreMap.put(serviceKey, semaphore);
                    }
                }
            }

            //获取注册中心服务
            IRegisterCenter4Provider registerCenter4Provider = RegisterCenter.singleton();
            List<ProviderService> localProviderCaches = registerCenter4Provider.getProviderServiceMap().get(serviceKey);

            Object result = null;
            boolean acquire = false;

            try {
                ProviderService localProviderCache = Collections2.filter(localProviderCaches, new Predicate<ProviderService>() {
                    @Override
                    public boolean apply(ProviderService providerService) {
                        return StringUtils.equals(providerService.getServiceMethod().getName(), methodName);
                    }
                }).iterator().next();
                Object serviceObject = localProviderCache.getServiceObject();

                //利用反射发起服务调用
                Method method = localProviderCache.getServiceMethod();
                //利用semaphore实现限流
                acquire = semaphore.tryAcquire(consumeTimeOut, TimeUnit.MILLISECONDS);
                if (acquire) {
                    result = method.invoke(serviceObject, request.getArgs());
                }
            } catch (Exception e) {
                System.out.println(JSON.toJSONString(localProviderCaches) + "  " + methodName+" "+e.getMessage());
                result = e;
            } finally {
                if (acquire) {
                    semaphore.release();
                }
            }

            //根据服务调用结果组装调用返回对象
            AresResponse response = new AresResponse();
            response.setInvokeTimeout(consumeTimeOut);
            response.setUniqueKey(request.getUniqueKey());
            response.setResult(result);
            ctx.writeAndFlush(response);
        } else {
            logger.error("------------channel closed!---------------");
        }
    }
}
