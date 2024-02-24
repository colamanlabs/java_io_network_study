 
# java_network_girl_netty_20240224_0003.md


# 1장 네티 맛보기

## 1.4.2 클라이언트 구현

```
### 서버 
C:\WORKS\WORKS_STS4_GITHUB\java_network_study\java_network_girl_netty_0002\target>java -jar ./java_network_girl_netty_0002-0.0.1-SNAPSHOT.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::               (v2.7.18)

2024-02-24 19:33:57.311  INFO 23696 --- [           main] .n.s.JavaNetworkGirlNetty0002Application : Starting JavaNetworkGirlNetty0002Application v0.0.1-SNAPSHOT using Java 17 on DEFAULT3 with PID 23696 (C:\WORKS\WORKS_STS4_GITHUB\java_network_study\java_network_girl_netty_0002\target\java_network_girl_netty_0002-0.0.1-SNAPSHOT.jar started by colaman in C:\WORKS\WORKS_STS4_GITHUB\java_network_study\java_network_girl_netty_0002\target)
2024-02-24 19:33:57.312  INFO 23696 --- [           main] .n.s.JavaNetworkGirlNetty0002Application : No active profile set, falling back to 1 default profile: "default"
2024-02-24 19:33:58.185  INFO 23696 --- [           main] .n.s.JavaNetworkGirlNetty0002Application : Started JavaNetworkGirlNetty0002Application in 1.436 seconds (JVM running for 1.864)
2024-02-24 19:33:58.189  INFO 23696 --- [           main] .n.s.JavaNetworkGirlNetty0002Application : [JavaNetworkGirlNetty0002Application/init] BEGIN
2024-02-24 19:33:58.191  INFO 23696 --- [           main] c.c.netty.networkgirl.s0002.EchoServer   : [EchoServer/main] BEGIN
2024-02-24 19:33:58.503  INFO 23696 --- [           main] c.c.netty.networkgirl.s0002.EchoServer   : [EchoServer/main] f.sync() ready......
2024-02-24 19:34:12.020  INFO 23696 --- [ntLoopGroup-3-1] c.c.netty.networkgirl.s0002.EchoServer   : [.../initChannel] BEGIN
2024-02-24 19:34:12.023  INFO 23696 --- [ntLoopGroup-3-1] c.c.netty.networkgirl.s0002.EchoServer   : [.../initChannel] ch:[[id: 0x867dc313, L:/127.0.0.1:8888 - R:/127.0.0.1:5632]]
2024-02-24 19:34:12.024  INFO 23696 --- [ntLoopGroup-3-1] c.c.netty.networkgirl.s0002.EchoServer   : [.../initChannel] END
2024-02-24 19:34:12.042  INFO 23696 --- [ntLoopGroup-3-1] c.c.n.n.s0002.EchoServerHandler          : [EchoServerHandler/channelRead] BEGIN
2024-02-24 19:34:12.042  INFO 23696 --- [ntLoopGroup-3-1] c.c.n.n.s0002.EchoServerHandler          : [EchoServerHandler/channelRead] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0x867dc313, L:/127.0.0.1:8888 - R:/127.0.0.1:5632])]
2024-02-24 19:34:12.042  INFO 23696 --- [ntLoopGroup-3-1] c.c.n.n.s0002.EchoServerHandler          : [EchoServerHandler/channelRead] msg:[PooledUnsafeDirectByteBuf(ridx: 0, widx: 11, cap: 2048)]
2024-02-24 19:34:12.043  INFO 23696 --- [ntLoopGroup-3-1] c.c.n.n.s0002.EchoServerHandler          : [EchoServerHandler/channelRead] readMessage:[Hello netty]
2024-02-24 19:34:12.044  INFO 23696 --- [ntLoopGroup-3-1] c.c.n.n.s0002.EchoServerHandler          : [EchoServerHandler/channelRead] builder.toString()):[수신한 문자열 [Hello netty]]
2024-02-24 19:34:12.044  INFO 23696 --- [ntLoopGroup-3-1] c.c.n.n.s0002.EchoServerHandler          : [EchoServerHandler/channelReadComplete] BEGIN
2024-02-24 19:34:12.044  INFO 23696 --- [ntLoopGroup-3-1] c.c.n.n.s0002.EchoServerHandler          : [EchoServerHandler/channelReadComplete] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0x867dc313, L:/127.0.0.1:8888 - R:/127.0.0.1:5632])]
2024-02-24 19:34:12.045  INFO 23696 --- [ntLoopGroup-3-1] c.c.n.n.s0002.EchoServerHandler          : [EchoServerHandler/channelReadComplete] END
2024-02-24 19:34:12.046  INFO 23696 --- [ntLoopGroup-3-1] c.c.n.n.s0002.EchoServerHandler          : [EchoServerHandler/channelReadComplete] BEGIN
2024-02-24 19:34:12.047  INFO 23696 --- [ntLoopGroup-3-1] c.c.n.n.s0002.EchoServerHandler          : [EchoServerHandler/channelReadComplete] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0x867dc313, L:/127.0.0.1:8888 - R:/127.0.0.1:5632])]
2024-02-24 19:34:12.047  INFO 23696 --- [ntLoopGroup-3-1] c.c.n.n.s0002.EchoServerHandler          : [EchoServerHandler/channelReadComplete] END

C:\WORKS\WORKS_STS4_GITHUB\java_network_study\java_network_girl_netty_0002\target>




### 클라이언트

[JavaNetworkGirlNetty0003Application/init] BEGIN
[EchoClient/main] f.sync() ready......
[EchoClientHandler/channelActive] ctx:[ChannelHandlerContext(EchoClientHandler#0, [id: 0xa9445572, L:/127.0.0.1:5632 - R:localhost/127.0.0.1:8888])]
[EchoClientHandler/channelActive] builder.toString():[전송한 문자열 [Hello netty]]
[EchoClientHandler/channelRead] ctx:[ChannelHandlerContext(EchoClientHandler#0, [id: 0xa9445572, L:/127.0.0.1:5632 - R:localhost/127.0.0.1:8888])]
[EchoClientHandler/channelRead] msg:[PooledUnsafeDirectByteBuf(ridx: 0, widx: 11, cap: 2048)]
[EchoClientHandler/channelRead] Charset.defaultCharset():[UTF-8]
[EchoClientHandler/channelRead] builder.toString():[수신한 문자열 [Hello netty]]
[EchoClientHandler/channelReadComplete] ctx:[ChannelHandlerContext(EchoClientHandler#0, [id: 0xa9445572, L:/127.0.0.1:5632 - R:localhost/127.0.0.1:8888])]
[JavaNetworkGirlNetty0003Application/init] END
```



# 1장 끝

