package com.wmt.framework.provider;

import avro.shaded.com.google.common.collect.Maps;
import com.wmt.framework.model.AresRequest;
import com.wmt.framework.model.ProviderService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * 处理服务端的逻辑
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
            //获取限流工具类
            int workerThread = metaDataModel.getWorkerThreads();
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


        } else {
            logger.error("------------channel closed!---------------");
        }
    }
}
