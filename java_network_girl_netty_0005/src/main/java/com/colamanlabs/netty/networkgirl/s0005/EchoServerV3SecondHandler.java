package com.colamanlabs.netty.networkgirl.s0005;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler implementation for the echo server.
 */
@Slf4j
public class EchoServerV3SecondHandler extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        log.info(String.format("[EchoServerV3SecondHandler/channelReadComplete] BEGIN"));
        System.out.println("channelReadComplete 발생");
        ctx.flush();
        log.info(String.format("[EchoServerV3SecondHandler/channelReadComplete] END"));
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
}
