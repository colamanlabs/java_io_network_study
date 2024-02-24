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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * Sends one message when a connection is open and echoes back any received data to the server. Simply put, the echo client initiates the ping-pong traffic
 * between the echo client and server by sending the first message to the server.
 */
@Slf4j
public final class EchoClient
{
    public void main(String[] args) throws Exception
    {
        /*
         * 클라이언트 애플리케이션은 서버와 달리 서버에 연결된 채널 하나만 조재하기 때문에, 이벤트 루프 그룹이 하나다. 
         */
        EventLoopGroup group = new NioEventLoopGroup();
        
        try
        {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                public void initChannel(SocketChannel ch) throws Exception
                {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new EchoClientHandler());
                }
            });
            
            /*
             * 비동기 입출력 메서드인 connect 를 호출한다.
             * connect 메소드는 호출결과로 ChannelFuture 객체를 돌려주는데,
             * 이 객체를 통해 비동기 메소드의 처리결과를 확인할 수 있다.
             * 단, 요청이 실패하면 예외를 던진다.
             * 즉, connect 메소드의 처리가 완료될 때 까지 다음 라인으로 진행하지 않는다.
             */
            ChannelFuture f = b.connect("localhost", 8888);
            log.info(String.format("[EchoClient/main] f.sync() ready......"));
            f.sync();
            
            f.channel().closeFuture().sync();
        }
        finally
        {
            group.shutdownGracefully();
        }
    }
}
