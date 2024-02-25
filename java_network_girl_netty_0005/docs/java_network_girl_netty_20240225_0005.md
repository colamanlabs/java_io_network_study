  
# java_network_girl_netty_20240225_0005.md




### 4.3.3 이벤트 이동 경로와 이벤트 메서드 실행

```

채널 파이프라인에 여러 이벤트 핸들러가 등록되어 있을 때
어떻게 이벤트 메소드가 실행되는지 확인한다.

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ChannelPipeline p = ch.pipeline();
                    
                    ### 채널 파이프라인에 두 이벤트 핸들러를 등록했다.
                    p.addLast(new EchoServerV3FirstHandler());
                    p.addLast(new EchoServerV3SecondHandler());
                }
            });
```



```
public class EchoServerV3FirstHandler extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        ByteBuf readMessage = (ByteBuf) msg;
        System.out.println("channelRead : " + readMessage.toString(Charset.defaultCharset()));
        ctx.write(msg);
    }
}
```


```
public class EchoServerV3SecondHandler extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        System.out.println("channelReadComplete 발생");
        ctx.flush();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
}
```



```
한 이벤트 핸들러에 구현되어 있던 코드를 두 이벤트 핸들러로 분리하여 채널 파이프 라인에 등록했을 뿐이므로, 
EchoServerV3 을 실행하면, EchoServerV2 와 동일하게 동작할 것이다.


[채널 파이프라인의 이벤트 흐름]

1) EchoServerV3FirstHandler
1-1) channelActive 이벤트
1-2) channelRead 이벤트 

2) EchoServerV3SecondHandler
2-1) channelReadComplete 이벤트


EchoServerV3 에서 클라이언트 채널이 생성되고, 
해당 채널의 채널 파이프라인에 channelActive 이벤트가 발생했다.
그러나, 등롣된 이벤트 핸들러에 channelActive 이벤트 메소드가 구현되어 있지 않으므로, channelActive 이벤트는 무시된다.

다음으로, channelRead 이벤트가 발생하고, 
채널 파이프라인에 등록된 EchoServerV3FirstHandler 의 chanelRead 이벤트 메소드가 수행된다.

마지막으로 channelReadComplete 이벤트가 발생하고, 
EchoServerV3SecondHandler 에 구현된 channelReadComplete 메소드가 수행된다.

즉, 여러개의 이벤트 핸들러가 등록되어 있을 때 이벤트에 해당하는 이벤트 메소드가 실행된다.
```


#### [실행결과]
```
Started JavaNetworkGirlNetty0005Application in 0.773 seconds (JVM running for 1.346)
[JavaNetworkGirlNetty0005Application/init] BEGIN
[EchoServerV3FirstHandler/channelRead] BEGIN
channelRead : A
[EchoServerV3FirstHandler/channelRead] END
[EchoServerV3SecondHandler/channelReadComplete] BEGIN
channelReadComplete 발생
[EchoServerV3SecondHandler/channelReadComplete] END
[EchoServerV3FirstHandler/channelRead] BEGIN
channelRead : B
[EchoServerV3FirstHandler/channelRead] END
[EchoServerV3SecondHandler/channelReadComplete] BEGIN
channelReadComplete 발생
[EchoServerV3SecondHandler/channelReadComplete] END
[EchoServerV3FirstHandler/channelRead] BEGIN
channelRead : C
[EchoServerV3FirstHandler/channelRead] END
[EchoServerV3SecondHandler/channelReadComplete] BEGIN
hannelReadComplete 발생
[EchoServerV3SecondHandler/channelReadComplete] END
Application shutdown requested.
HikariPool-1 - Starting...
...
```
