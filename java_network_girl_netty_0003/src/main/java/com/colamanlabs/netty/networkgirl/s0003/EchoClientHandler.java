/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.colamanlabs.netty.networkgirl.s0003;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler implementation for the echo client. It initiates the ping-pong traffic between the echo client and server by sending the first message to the server.
 */
@Slf4j
public class EchoClientHandler extends ChannelInboundHandlerAdapter
{
    
    
    /*
     * channelActive 이벤트는 ChannelInboundHandler 에 정의된 이벤트로서 소켓채널이 최초 활성화 되었을 때 실행된다.
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {        
        log.info(String.format("[EchoClientHandler/channelActive] ctx:[%s]", ctx));
        String sendMessage = "Hello netty";
        
        ByteBuf messageBuffer = Unpooled.buffer();
        messageBuffer.writeBytes(sendMessage.getBytes());
        
        StringBuilder builder = new StringBuilder();
        builder.append("전송한 문자열 [");
        builder.append(sendMessage);
        builder.append("]");
        
//        System.out.println(builder.toString());
        log.info(String.format("[EchoClientHandler/channelActive] builder.toString():[%s]", builder.toString()));
        
        /*
         * writeAndFlush 메소드는 내부적으로 데이터 기록과 전송의 두가지 메소드를 호출한다.
         * 첫번째는 채널에 데이터를 기록하는 write 메소드 이며,
         * 두번째는 채널에 기록된 데이터를 서버로 전송하는 flush 메소드다.
         */        
        ctx.writeAndFlush(messageBuffer);
    }
    
    /*
     * channelRead 
     * 서버로부터 수신된 데이터가 있을 때 호출되는 네티 이벤트 메소드다 
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        log.info(String.format("[EchoClientHandler/channelRead] ctx:[%s]", ctx));
        log.info(String.format("[EchoClientHandler/channelRead] msg:[%s]", msg));
        log.info(String.format("[EchoClientHandler/channelRead] Charset.defaultCharset():[%s]", Charset.defaultCharset()));
        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());
        
        StringBuilder builder = new StringBuilder();
        builder.append("수신한 문자열 [");
        builder.append(readMessage);
        builder.append("]");
        
//        System.out.println(builder.toString());
        log.info(String.format("[EchoClientHandler/channelRead] builder.toString():[%s]", builder.toString()));
    }
    
    
    /*
     * channelReadComplete
     * 수신된 데이터를 모두 읽었을 때 호출되는 이벤트 메소드다.
     * channelRead 메소드의 수행이 완료되고 나서 자동으로 호출된다.
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        log.info(String.format("[EchoClientHandler/channelReadComplete] ctx:[%s]", ctx));
        /*
         * 수신된 데이터를 모두 읽은 후 서버와 연결된 채널을 닫는다. 
         * 이후 데이터 송수신 채널은 닫히게 되고,
         * 클라이언트 프로그램은 종료된다.
         */
        ctx.close();        
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        log.info(String.format("[EchoClientHandler/exceptionCaught] ctx:[%s]", ctx));
        log.info(String.format("[EchoClientHandler/exceptionCaught] cause:[%s]", cause));
        cause.printStackTrace();
        ctx.close();
    }
}
