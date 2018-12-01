package com.wmt.framework.provider;

import com.wmt.framework.helper.PropertyConfigeHelper;
import com.wmt.framework.model.AresRequest;
import com.wmt.framework.serialization.NettyDecoderHandler;
import com.wmt.framework.serialization.NettyEncoderHandler;
import com.wmt.framework.serialization.common.SerializeType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 *
 * @author weimiantong
 * @date 18/11/11
 */
public class NettyServer {
    private static NettyServer nettyServer = new NettyServer();
    private Channel channel;
    /**
     * 服务端boss线程组
     */
    private EventLoopGroup bossGroup;
    /**
     * 服务端worker线程组
     */
    private EventLoopGroup workerGroup;
    /**
     * 序列化类型配置信息
     */
    private SerializeType serializeType = PropertyConfigeHelper.getSerializeType();

    /**
     * 启动Netty服务
     *
     * @param port
     */
    public void start(final int port) {
        synchronized (NettyServer.class) {
            if (bossGroup != null || workerGroup != null) {
                return;
            }
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //注册解码器NettyDecoderHandler
                            ch.pipeline().addLast(new NettyDecoderHandler(AresRequest.class,  serializeType));

                            //注册编码器NettyEncoderHandler
                            ch.pipeline().addLast(new NettyEncoderHandler(serializeType));
                        }
                    });
        }
    }
}
