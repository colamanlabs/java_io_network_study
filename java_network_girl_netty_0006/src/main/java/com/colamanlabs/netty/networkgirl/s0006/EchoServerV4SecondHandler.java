package com.colamanlabs.netty.networkgirl.s0006;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler implementation for the echo server.
 */
@Slf4j
public class EchoServerV4SecondHandler extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        log.info(String.format("[EchoServerV4SecondHandler/channelRead] BEGIN"));
        ByteBuf readMessage = (ByteBuf) msg;
        // System.out.println("SecondHandler channelRead : " + readMessage.toString(Charset.defaultCharset()));
        log.info(String.format("[EchoServerV4SecondHandler/channelRead] [%s]", readMessage.toString(Charset.defaultCharset())));
        log.info(String.format("[EchoServerV4SecondHandler/channelRead] END"));
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        log.info(String.format("[EchoServerV4SecondHandler/channelReadComplete] BEGIN"));
        log.info(String.format("[EchoServerV4SecondHandler/channelReadComplete] channelReadComplete 발생"));
        // System.out.println("channelReadComplete 발생");
        ctx.flush();
        log.info(String.format("[EchoServerV4SecondHandler/channelReadComplete] END"));
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
}
