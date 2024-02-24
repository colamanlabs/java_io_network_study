 
# java_network_girl_netty_20240224_0002.md


# 1장 네티 맛보기

## 1.4.1 에코 서버 구현

```

19:13:03.001 [Thread-0] DEBUG org.springframework.boot.devtools.restart.classloader.RestartClassLoader - Created RestartClassLoader org.springframework.boot.devtools.restart.classloader.RestartClassLoader@13a89db

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

...
[JavaNetworkGirlNetty0002Application/init] BEGIN
[EchoServer/main] BEGIN
[EchoServer/main] f.sync() ready......
[.../initChannel] BEGIN
[.../initChannel] ch:[[id: 0x7b54bcfb, L:/127.0.0.1:8888 - R:/127.0.0.1:5540]]
[.../initChannel] END
[EchoServerHandler/channelRead] BEGIN
[EchoServerHandler/channelRead] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0x7b54bcfb, L:/127.0.0.1:8888 - R:/127.0.0.1:5540])]
[EchoServerHandler/channelRead] msg:[PooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 2048)]
[EchoServerHandler/channelRead] readMessage:[A]
[EchoServerHandler/channelRead] builder.toString()):[수신한 문자열 [A]]
[EchoServerHandler/channelReadComplete] BEGIN
[EchoServerHandler/channelReadComplete] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0x7b54bcfb, L:/127.0.0.1:8888 - R:/127.0.0.1:5540])]
[EchoServerHandler/channelReadComplete] END
[EchoServerHandler/channelRead] BEGIN
[EchoServerHandler/channelRead] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0x7b54bcfb, L:/127.0.0.1:8888 - R:/127.0.0.1:5540])]
[EchoServerHandler/channelRead] msg:[PooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 2048)]
[EchoServerHandler/channelRead] readMessage:[B]
[EchoServerHandler/channelRead] builder.toString()):[수신한 문자열 [B]]
[EchoServerHandler/channelReadComplete] BEGIN
[EchoServerHandler/channelReadComplete] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0x7b54bcfb, L:/127.0.0.1:8888 - R:/127.0.0.1:5540])]
[EchoServerHandler/channelReadComplete] END
[EchoServerHandler/channelRead] BEGIN
[EchoServerHandler/channelRead] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0x7b54bcfb, L:/127.0.0.1:8888 - R:/127.0.0.1:5540])]
[EchoServerHandler/channelRead] msg:[PooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 1024)]
[EchoServerHandler/channelRead] readMessage:[C]
[EchoServerHandler/channelRead] builder.toString()):[수신한 문자열 [C]]
[EchoServerHandler/channelReadComplete] BEGIN
[EchoServerHandler/channelReadComplete] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0x7b54bcfb, L:/127.0.0.1:8888 - R:/127.0.0.1:5540])]
[EchoServerHandler/channelReadComplete] END
[EchoServerHandler/channelRead] BEGIN
[EchoServerHandler/channelRead] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0x7b54bcfb, L:/127.0.0.1:8888 - R:/127.0.0.1:5540])]
[EchoServerHandler/channelRead] msg:[PooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 1024)]
[EchoServerHandler/channelRead] readMessage:[D]
[EchoServerHandler/channelRead] builder.toString()):[수신한 문자열 [D]]
[EchoServerHandler/channelReadComplete] BEGIN
[EchoServerHandler/channelReadComplete] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0x7b54bcfb, L:/127.0.0.1:8888 - R:/127.0.0.1:5540])]
[EchoServerHandler/channelReadComplete] END
[EchoServerHandler/channelRead] BEGIN
[EchoServerHandler/channelRead] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0x7b54bcfb, L:/127.0.0.1:8888 - R:/127.0.0.1:5540])]
[EchoServerHandler/channelRead] msg:[PooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 512)]
[EchoServerHandler/channelRead] readMessage:[E]
[EchoServerHandler/channelRead] builder.toString()):[수신한 문자열 [E]]
[EchoServerHandler/channelReadComplete] BEGIN
[EchoServerHandler/channelReadComplete] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0x7b54bcfb, L:/127.0.0.1:8888 - R:/127.0.0.1:5540])]
[EchoServerHandler/channelReadComplete] END
...
```
