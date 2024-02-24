package com.colamanlabs.netty.networkgirl.s0001;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles a server-side channel.
 */


/*
 * https://netty.io/4.0/api/io/netty/channel/SimpleChannelInboundHandler.html
 */
@Slf4j
public class DiscardServerHandler extends SimpleChannelInboundHandler<Object>
{
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        // 아무것도 하지 않음.
        log.info(String.format("[DiscardServerHandler/channelRead0] BEGIN"));
        log.info(String.format("[DiscardServerHandler/channelRead0] ctx:[%s]", ctx));
        log.info(String.format("[DiscardServerHandler/channelRead0] msg:[%s]", msg));
        
        log.info(String.format("[DiscardServerHandler/channelRead0] END"));
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        log.info(String.format("[DiscardServerHandler/exceptionCaught] BEGIN"));
        log.info(String.format("[DiscardServerHandler/channelRead0] ctx:[%s]", ctx));
        log.info(String.format("[DiscardServerHandler/channelRead0] cause:[%s]", cause));
        cause.printStackTrace();
        ctx.close();
        log.info(String.format("[DiscardServerHandler/exceptionCaught] END"));
    }
}
