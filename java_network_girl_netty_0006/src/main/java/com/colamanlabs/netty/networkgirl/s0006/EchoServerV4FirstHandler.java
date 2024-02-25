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
public class EchoServerV4FirstHandler extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        log.info(String.format("[EchoServerV4FirstHandler/channelRead] BEGIN"));
        ByteBuf readMessage = (ByteBuf) msg;
        // System.out.println("FirstHandler channelRead : " + readMessage.toString(Charset.defaultCharset()));
        log.info(String.format("[EchoServerV4FirstHandler/channelRead] [%s]", readMessage.toString(Charset.defaultCharset())));
        ctx.write(msg);
        
        // 이미 channelRead 이벤트 처리로 channelRead 이벤트는 사라졌다.
        // channelRead 이벤트를 다시 발생시킨다.
        // 이를 통해 EchoServerV4SecondHandler.channelRead 도 실행된다.
        ctx.fireChannelRead(msg);
        log.info(String.format("[EchoServerV4FirstHandler/channelRead] END"));
    }
}
