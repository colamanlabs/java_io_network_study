package com.colamanlabs.netty.networkgirl.s0004;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles a server-side channel.
 */

/*
 * https://netty.io/4.0/api/io/netty/channel/ChannelInboundHandlerAdapter.html
 * 
 * DiscardServer 와의 차이점
 * 1. DiscardServer 는 SimpleChannelInboundHandler 를 상속받았고, EchoServerHandler 는 ChannelInboundHandlerAdapter 를 상속받았다.
 * 
 * 2. SimpleChannelInboundHandler 는 ChannelInboundHandlerAdapter 를 상속받았다.
 * 
 */
@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        log.info(String.format("[EchoServerHandler/channelRead] BEGIN"));
        log.info(String.format("[EchoServerHandler/channelRead] ctx:[%s]", ctx));
        log.info(String.format("[EchoServerHandler/channelRead] msg:[%s]", msg));
        
        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());
        log.info(String.format("[EchoServerHandler/channelRead] readMessage:[%s]", readMessage));
        
        StringBuilder builder = new StringBuilder();
        builder.append("수신한 문자열 [");
        builder.append(readMessage);
        builder.append("]");
        // System.out.println(builder.toString());
        
        log.info(String.format("[EchoServerHandler/channelRead] builder.toString()):[%s]", builder.toString()));
        ctx.write(msg);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        log.info(String.format("[EchoServerHandler/channelReadComplete] BEGIN"));
        log.info(String.format("[EchoServerHandler/channelReadComplete] ctx:[%s]", ctx));
        ctx.flush();
        log.info(String.format("[EchoServerHandler/channelReadComplete] END"));
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        log.info(String.format("[EchoServerHandler/exceptionCaught] BEGIN"));
        log.info(String.format("[EchoServerHandler/exceptionCaught] ctx:[%s]", ctx));
        log.info(String.format("[EchoServerHandler/exceptionCaught] cause:[%s]", cause));
        
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
        log.info(String.format("[EchoServerHandler/exceptionCaught] END"));
    }
}
