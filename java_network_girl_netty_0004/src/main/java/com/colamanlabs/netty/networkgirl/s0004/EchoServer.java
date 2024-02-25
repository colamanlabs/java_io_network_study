package com.colamanlabs.netty.networkgirl.s0004;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoServer
{
    public void main(String[] args) throws Exception
    {
        log.info(String.format("[EchoServer/main] BEGIN"));
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                public void initChannel(SocketChannel ch)
                {
                    log.info(String.format("[.../initChannel] BEGIN"));
                    log.info(String.format("[.../initChannel] ch:[%s]", ch));
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new EchoServerHandler());
                    log.info(String.format("[.../initChannel] END"));
                }
            });
            
            ChannelFuture f = b.bind(8888);
            
            log.info(String.format("[EchoServer/main] f.sync() ready......"));
            f.sync();
            
            f.channel().closeFuture().sync();
        }
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
