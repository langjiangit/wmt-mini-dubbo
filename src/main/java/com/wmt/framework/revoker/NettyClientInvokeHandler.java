package com.wmt.framework.revoker;

import com.wmt.framework.model.AresResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 *  客户端业务逻辑处理器,获取Netty异步调用的返回结果, 并将该结果保存至结果类RevokerResponseHolder
 * Created by weimiantong on 18/12/16.
 */
public class NettyClientInvokeHandler extends SimpleChannelInboundHandler<AresResponse>{

    public NettyClientInvokeHandler() {}

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AresResponse response) throws Exception {
        //将Netty异步返回的结果存入阻塞队列,以便调用端同步获取
        RevokerResponseHolder.putResultValue(response);
    }
}
