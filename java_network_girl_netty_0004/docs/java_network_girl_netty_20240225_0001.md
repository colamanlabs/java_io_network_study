 
# java_network_girl_netty_20240225_0001.md



## 참고예제
```
package com.colamanlabs.netty.networkgirl.s0002;

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

```


### 3.3.1 ServerBootstrap API

#### https://netty.io/4.1/api/index.html

```
page 76)
https://netty.io/4.1/api/index.html
```

#### AbstractBootstrap
```
https://netty.io/4.1/api/io/netty/bootstrap/AbstractBootstrap.html
```


#### ServerBootstrap
##### group - 이벤트 루프 설정
```
https://netty.io/4.1/api/io/netty/bootstrap/ServerBootstrap.html

public ServerBootstrap group(EventLoopGroup parentGroup,
                             EventLoopGroup childGroup)
Set the EventLoopGroup for the parent (acceptor) and the child (client). These EventLoopGroup's are used to handle all the events and IO for ServerChannel and Channel's.
```


##### channel - 소켓 입출력 모드 설정
```
...
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
...
            
channel
public B channel(Class<? extends C> channelClass)
The Class which is used to create Channel instances from. You either use this or channelFactory(io.netty.channel.ChannelFactory) if your Channel implementation has no no-args constructor.
```

#### 참고소스
```
package com.colamanlabs.netty.networkgirl.s0004;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;

public class EchoServerV2
{
    public static void main(String[] args) throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.DEBUG)).childHandler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                public void initChannel(SocketChannel ch)
                {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new EchoServerHandler());
                }
            });
            
            ChannelFuture f = b.bind(8888).sync();
            
            f.channel().closeFuture().sync();
        }
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

```            

##### channelFactory - 소켓 입출력 모드 설정
```
https://netty.io/4.1/api/io/netty/bootstrap/ChannelFactory.html

https://netty.io/4.1/api/io/netty/channel/udt/nio/NioUdtProvider.html


ChannelFactory 인터페이스를 상속받은 클래스를 설정할 수 있으며,
channel 메서드와 동일 기능을 수행한다.
네티가 제공하는 ChannelFactory 인터페이스 구현체로는 NioUdtProvider 가 있다.
```


##### handler - 서버 소켓 채널의 이벤트 핸들러 설정
```
AbstractBootstrap.handler(ChannelHandler handler)

https://netty.io/4.1/api/io/netty/bootstrap/AbstractBootstrap.html

handler
public B handler(ChannelHandler handler)
the ChannelHandler to use for serving the requests.
```

```

LoggingHandler 는 ChannelDuplexHandler 를 상속받는다.
https://www.javadoc.io/doc/io.netty/netty-transport/4.1.46.Final/io/netty/channel/ChannelDuplexHandler.html

ChannelDuplexHandler 는 ChannelInboundHandler, ChannelOutboundHandler 를 모두 구현하고 있다.
```

```
12:50:24.402 [main] DEBUG io.netty.util.internal.logging.InternalLoggerFactory - Using SLF4J as the default logging framework
12:50:24.406 [main] DEBUG io.netty.channel.MultithreadEventLoopGroup - -Dio.netty.eventLoopThreads: 48
12:50:24.416 [main] DEBUG io.netty.util.concurrent.GlobalEventExecutor - -Dio.netty.globalEventExecutor.quietPeriodSeconds: 1
12:50:24.427 [main] DEBUG io.netty.util.internal.InternalThreadLocalMap - -Dio.netty.threadLocalMap.stringBuilder.initialSize: 1024
12:50:24.427 [main] DEBUG io.netty.util.internal.InternalThreadLocalMap - -Dio.netty.threadLocalMap.stringBuilder.maxSize: 4096
12:50:24.449 [main] DEBUG io.netty.util.internal.PlatformDependent0 - -Dio.netty.noUnsafe: false
12:50:24.449 [main] DEBUG io.netty.util.internal.PlatformDependent0 - Java version: 17
12:50:24.450 [main] DEBUG io.netty.util.internal.PlatformDependent0 - sun.misc.Unsafe.theUnsafe: available
12:50:24.451 [main] DEBUG io.netty.util.internal.PlatformDependent0 - sun.misc.Unsafe.copyMemory: available
12:50:24.451 [main] DEBUG io.netty.util.internal.PlatformDependent0 - sun.misc.Unsafe.storeFence: available
12:50:24.451 [main] DEBUG io.netty.util.internal.PlatformDependent0 - java.nio.Buffer.address: available
12:50:24.452 [main] DEBUG io.netty.util.internal.PlatformDependent0 - direct buffer constructor: unavailable: Reflective setAccessible(true) disabled
12:50:24.452 [main] DEBUG io.netty.util.internal.PlatformDependent0 - java.nio.Bits.unaligned: available, true
12:50:24.453 [main] DEBUG io.netty.util.internal.PlatformDependent0 - jdk.internal.misc.Unsafe.allocateUninitializedArray(int): unavailable: class io.netty.util.internal.PlatformDependent0$7 cannot access class jdk.internal.misc.Unsafe (in module java.base) because module java.base does not export jdk.internal.misc to unnamed module @48ae9b55
12:50:24.454 [main] DEBUG io.netty.util.internal.PlatformDependent0 - java.nio.DirectByteBuffer.<init>(long, {int,long}): unavailable
12:50:24.454 [main] DEBUG io.netty.util.internal.PlatformDependent - sun.misc.Unsafe: available
12:50:24.454 [main] DEBUG io.netty.util.internal.PlatformDependent - -Dio.netty.tmpdir: C:\Users\colaman\AppData\Local\Temp (java.io.tmpdir)
12:50:24.454 [main] DEBUG io.netty.util.internal.PlatformDependent - -Dio.netty.bitMode: 64 (sun.arch.data.model)
12:50:24.455 [main] DEBUG io.netty.util.internal.PlatformDependent - Platform: Windows
12:50:24.456 [main] DEBUG io.netty.util.internal.PlatformDependent - -Dio.netty.maxDirectMemory: -1 bytes
12:50:24.456 [main] DEBUG io.netty.util.internal.PlatformDependent - -Dio.netty.uninitializedArrayAllocationThreshold: -1
12:50:24.457 [main] DEBUG io.netty.util.internal.CleanerJava9 - java.nio.ByteBuffer.cleaner(): available
12:50:24.457 [main] DEBUG io.netty.util.internal.PlatformDependent - -Dio.netty.noPreferDirect: false
12:50:24.457 [main] DEBUG io.netty.channel.nio.NioEventLoop - -Dio.netty.noKeySetOptimization: false
12:50:24.458 [main] DEBUG io.netty.channel.nio.NioEventLoop - -Dio.netty.selectorAutoRebuildThreshold: 512
12:50:24.469 [main] DEBUG io.netty.util.internal.PlatformDependent - org.jctools-core.MpscChunkedArrayQueue: available
12:50:24.602 [main] DEBUG io.netty.channel.DefaultChannelId - -Dio.netty.processId: 20720 (auto-detected)
12:50:24.605 [main] DEBUG io.netty.util.NetUtil - -Djava.net.preferIPv4Stack: false
12:50:24.605 [main] DEBUG io.netty.util.NetUtil - -Djava.net.preferIPv6Addresses: false
12:50:24.616 [main] DEBUG io.netty.util.NetUtilInitializations - Loopback interface: lo (Software Loopback Interface 1, 127.0.0.1)
12:50:24.617 [main] DEBUG io.netty.util.NetUtil - Failed to get SOMAXCONN from sysctl and file \proc\sys\net\core\somaxconn. Default: 200
12:50:24.629 [main] DEBUG io.netty.channel.DefaultChannelId - -Dio.netty.machineId: 00:e0:63:ff:fe:31:26:52 (auto-detected)
12:50:24.639 [main] DEBUG io.netty.util.ResourceLeakDetector - -Dio.netty.leakDetection.level: simple
12:50:24.639 [main] DEBUG io.netty.util.ResourceLeakDetector - -Dio.netty.leakDetection.targetRecords: 4
12:50:24.662 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.numHeapArenas: 48
12:50:24.662 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.numDirectArenas: 48
12:50:24.662 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.pageSize: 8192
12:50:24.662 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.maxOrder: 9
12:50:24.662 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.chunkSize: 4194304
12:50:24.662 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.smallCacheSize: 256
12:50:24.662 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.normalCacheSize: 64
12:50:24.662 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.maxCachedBufferCapacity: 32768
12:50:24.662 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.cacheTrimInterval: 8192
12:50:24.662 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.cacheTrimIntervalMillis: 0
12:50:24.662 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.useCacheForAllThreads: false
12:50:24.662 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.maxCachedByteBuffersPerChunk: 1023
12:50:24.676 [main] DEBUG io.netty.buffer.ByteBufUtil - -Dio.netty.allocator.type: pooled
12:50:24.676 [main] DEBUG io.netty.buffer.ByteBufUtil - -Dio.netty.threadLocalDirectBufferSize: 0
12:50:24.676 [main] DEBUG io.netty.buffer.ByteBufUtil - -Dio.netty.maxThreadLocalCharBufferSize: 16384
12:50:24.678 [main] DEBUG io.netty.bootstrap.ChannelInitializerExtensions - -Dio.netty.bootstrap.extensions: null
12:50:24.691 [nioEventLoopGroup-2-1] DEBUG com.colamanlabs.netty.networkgirl.s0004.LoggingHandler - [id: 0x39e144ef] REGISTERED
12:50:24.692 [nioEventLoopGroup-2-1] DEBUG com.colamanlabs.netty.networkgirl.s0004.LoggingHandler - [id: 0x39e144ef] BIND: 0.0.0.0/0.0.0.0:8888
12:50:24.695 [nioEventLoopGroup-2-1] DEBUG com.colamanlabs.netty.networkgirl.s0004.LoggingHandler - [id: 0x39e144ef, L:/[0:0:0:0:0:0:0:0]:8888] ACTIVE
12:50:31.839 [nioEventLoopGroup-2-1] DEBUG com.colamanlabs.netty.networkgirl.s0004.LoggingHandler - [id: 0x39e144ef, L:/[0:0:0:0:0:0:0:0]:8888] RECEIVED: [id: 0xd5025144, L:/127.0.0.1:8888 - R:/127.0.0.1:4568]
12:50:33.797 [nioEventLoopGroup-3-1] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.maxCapacityPerThread: 4096
12:50:33.797 [nioEventLoopGroup-3-1] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.ratio: 8
12:50:33.797 [nioEventLoopGroup-3-1] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.chunkSize: 32
12:50:33.797 [nioEventLoopGroup-3-1] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.blocking: false
12:50:33.797 [nioEventLoopGroup-3-1] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.batchFastThreadLocalOnly: true
12:50:33.801 [nioEventLoopGroup-3-1] DEBUG io.netty.buffer.AbstractByteBuf - -Dio.netty.buffer.checkAccessible: true
12:50:33.801 [nioEventLoopGroup-3-1] DEBUG io.netty.buffer.AbstractByteBuf - -Dio.netty.buffer.checkBounds: true
12:50:33.802 [nioEventLoopGroup-3-1] DEBUG io.netty.util.ResourceLeakDetectorFactory - Loaded default ResourceLeakDetector: io.netty.util.ResourceLeakDetector@42d07f13
12:50:33.805 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelRead] BEGIN
12:50:33.805 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelRead] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0xd5025144, L:/127.0.0.1:8888 - R:/127.0.0.1:4568])]
12:50:33.805 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelRead] msg:[PooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 2048)]
12:50:33.806 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelRead] readMessage:[a]
12:50:33.806 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelRead] builder.toString()):[수신한 문자열 [a]]
12:50:33.806 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelReadComplete] BEGIN
12:50:33.806 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelReadComplete] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0xd5025144, L:/127.0.0.1:8888 - R:/127.0.0.1:4568])]
...
12:50:35.554 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelReadComplete] END
```



##### childHandler - 소켓 채널의 데이터 가공 핸들러 설정





```


public class EchoServerV3
{
...
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                public void initChannel(SocketChannel ch)
                {
                    ChannelPipeline p = ch.pipeline();
                    
                    ### LoggingHandler 를 클라이언트 소켓 채널의 파이프라인에 등록했다.
                    p.addLast(new LoggingHandler(LogLevel.DEBUG));
                    p.addLast(new EchoServerHandler());
                }
            });
...
}
```



```
13:06:54.077 [main] DEBUG io.netty.buffer.ByteBufUtil - -Dio.netty.allocator.type: pooled
13:06:54.078 [main] DEBUG io.netty.buffer.ByteBufUtil - -Dio.netty.threadLocalDirectBufferSize: 0
13:06:54.078 [main] DEBUG io.netty.buffer.ByteBufUtil - -Dio.netty.maxThreadLocalCharBufferSize: 16384
13:06:54.079 [main] DEBUG io.netty.bootstrap.ChannelInitializerExtensions - -Dio.netty.bootstrap.extensions: null
13:06:56.876 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerV3 - [EchoServerV3/.../initChannel] ch:[[id: 0xbe14e32d, L:/127.0.0.1:8888 - R:/127.0.0.1:4772]]
```

```
### 서버에 접속해서, 이벤트 루프에 등록되었다. 
13:06:56.882 [nioEventLoopGroup-3-1] DEBUG com.colamanlabs.netty.networkgirl.s0004.LoggingHandler - [id: 0xbe14e32d, L:/127.0.0.1:8888 - R:/127.0.0.1:4772] REGISTERED

### 소켓이 활성화 되었다.
13:06:56.882 [nioEventLoopGroup-3-1] DEBUG com.colamanlabs.netty.networkgirl.s0004.LoggingHandler - [id: 0xbe14e32d, L:/127.0.0.1:8888 - R:/127.0.0.1:4772] ACTIVE

13:06:57.742 [nioEventLoopGroup-3-1] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.maxCapacityPerThread: 4096
13:06:57.743 [nioEventLoopGroup-3-1] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.ratio: 8
13:06:57.743 [nioEventLoopGroup-3-1] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.chunkSize: 32
13:06:57.743 [nioEventLoopGroup-3-1] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.blocking: false
13:06:57.743 [nioEventLoopGroup-3-1] DEBUG io.netty.util.Recycler - -Dio.netty.recycler.batchFastThreadLocalOnly: true
13:06:57.747 [nioEventLoopGroup-3-1] DEBUG io.netty.buffer.AbstractByteBuf - -Dio.netty.buffer.checkAccessible: true
13:06:57.747 [nioEventLoopGroup-3-1] DEBUG io.netty.buffer.AbstractByteBuf - -Dio.netty.buffer.checkBounds: true
13:06:57.748 [nioEventLoopGroup-3-1] DEBUG io.netty.util.ResourceLeakDetectorFactory - Loaded default ResourceLeakDetector: io.netty.util.ResourceLeakDetector@3712b52a

### 수신 이벤트 록그
13:06:57.755 [nioEventLoopGroup-3-1] DEBUG com.colamanlabs.netty.networkgirl.s0004.LoggingHandler - [id: 0xbe14e32d, L:/127.0.0.1:8888 - R:/127.0.0.1:4772] RECEIVED: 1B
         +-------------------------------------------------+
         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
+--------+-------------------------------------------------+----------------+
|00000000| 41                                              |A               |
+--------+-------------------------------------------------+----------------+
13:06:57.755 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelRead] BEGIN
13:06:57.755 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelRead] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0xbe14e32d, L:/127.0.0.1:8888 - R:/127.0.0.1:4772])]
13:06:57.755 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelRead] msg:[PooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 2048)]
13:06:57.755 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelRead] readMessage:[A]
13:06:57.755 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelRead] builder.toString()):[수신한 문자열 [A]]

### write 메소드를 호출한 이벤트 
13:06:57.755 [nioEventLoopGroup-3-1] DEBUG com.colamanlabs.netty.networkgirl.s0004.LoggingHandler - [id: 0xbe14e32d, L:/127.0.0.1:8888 - R:/127.0.0.1:4772] WRITE: 1B
         +-------------------------------------------------+
         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
+--------+-------------------------------------------------+----------------+
|00000000| 41                                              |A               |
+--------+-------------------------------------------------+----------------+
13:06:57.756 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelReadComplete] BEGIN
13:06:57.756 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelReadComplete] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0xbe14e32d, L:/127.0.0.1:8888 - R:/127.0.0.1:4772])]


13:06:57.756 [nioEventLoopGroup-3-1] DEBUG com.colamanlabs.netty.networkgirl.s0004.LoggingHandler - [id: 0xbe14e32d, L:/127.0.0.1:8888 - R:/127.0.0.1:4772] FLUSH

### 채널 버퍼에 기록된 데이터를 telnet 클라이언트로 전송하기 위한 flush 이벤트를 나타낸다.
13:06:57.756 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelReadComplete] END
13:06:58.995 [nioEventLoopGroup-3-1] DEBUG com.colamanlabs.netty.networkgirl.s0004.LoggingHandler - [id: 0xbe14e32d, L:/127.0.0.1:8888 - R:/127.0.0.1:4772] RECEIVED: 1B
         +-------------------------------------------------+
         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
+--------+-------------------------------------------------+----------------+
|00000000| 42                                              |B               |
+--------+-------------------------------------------------+----------------+
13:06:58.996 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelRead] BEGIN
13:06:58.996 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelRead] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0xbe14e32d, L:/127.0.0.1:8888 - R:/127.0.0.1:4772])]
13:06:58.996 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelRead] msg:[PooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 2048)]
13:06:58.996 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelRead] readMessage:[B]
13:06:58.996 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelRead] builder.toString()):[수신한 문자열 [B]]
13:06:58.996 [nioEventLoopGroup-3-1] DEBUG com.colamanlabs.netty.networkgirl.s0004.LoggingHandler - [id: 0xbe14e32d, L:/127.0.0.1:8888 - R:/127.0.0.1:4772] WRITE: 1B
         +-------------------------------------------------+
         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
+--------+-------------------------------------------------+----------------+
|00000000| 42                                              |B               |
+--------+-------------------------------------------------+----------------+
13:06:58.996 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelReadComplete] BEGIN
13:06:58.996 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelReadComplete] ctx:[ChannelHandlerContext(EchoServerHandler#0, [id: 0xbe14e32d, L:/127.0.0.1:8888 - R:/127.0.0.1:4772])]
13:06:58.996 [nioEventLoopGroup-3-1] DEBUG com.colamanlabs.netty.networkgirl.s0004.LoggingHandler - [id: 0xbe14e32d, L:/127.0.0.1:8888 - R:/127.0.0.1:4772] FLUSH
13:06:58.996 [nioEventLoopGroup-3-1] INFO com.colamanlabs.netty.networkgirl.s0004.EchoServerHandler - [EchoServerHandler/channelReadComplete] END
13:06:59.540 [nioEventLoopGroup-3-1] DEBUG com.colamanlabs.netty.networkgirl.s0004.LoggingHandler - [id: 0xbe14e32d, L:/127.0.0.1:8888 - R:/127.0.0.1:4772] RECEIVED: 1B
...
```

### 이벤트 흐름
```
1. [연결요청] 클라이언트에서 연결 요청을 하는 경우 
- REGISTERED 이벤트
- ACTIVE 이벤트 

2. [데이터 송신] 클라이언트 -> 서버 
- 서버의 데이터 수신 
- RECEIVED 이벤트 

3. 서버 -> 클라이언트
- channel.write("hello.")
	- WRITE 이벤트
- channel.flush()
	- FLUSH 이벤트
```


### option - 서버 소켓 채널의 옵션 설정
```
page 87)
SO_SNDBUF 상대방으로 송신할 커널 송신 버퍼의 크기
SO_RCVBUF 상대방으로부터 수신할 커널 수신의 버퍼의 크기
...
```



#### TCP_NODELAY
```
- Nagle 알고리즘의 비활성화 여부 설정
- 기본값은 False 이고, 즉, 네이글 알고리즘을 사용하겠다는 의미이다.

네이글 알고리즘은 "가능하면 데이터를 나누어 보내지 말고 한꺼번에 보내라" 라는 원칙을 기반으로 만들어진 알고리즘이다.
데이터를 여러번에 나누어 전송하면 각 패킷에 불필요한 50바이트의 TCP/IP 헤더 정보의 오버헤드가 있어, 이를 방지하고자 데이터를 모아서 보내라는 의도다.
이런 동작방식 덕분에 네트워크 상태가 좋지 않고, 대역폭이 좁을 때 네트워크를 효율적으로 사용할 수 있다.

네이글 알고리즘의 두가지 특징
1) 작은 크기의 데이터를 전송하면 커널의 송신 버퍼에서 적당한 크기로 보낸다.
2) 이전에 보낸 패킷의 ACK 를 받아야 다음 패킷을 전송한다.

네이글 알고리즘은 데이터를 송신하고, ACK 를 수신하기 전까지 다음 데이터를 전송하지 않는다.
그러므로 빠른 응답시간이 필요한 네트워크 애플리케이션에서는 좋지 않은 결과를 가져온다.
온라인 게임과 같이 빠른 응답을 요구하는 애플리케이션 에서 문제가 된다.
그럴 때는 TCP_NODELAY 옵션을 활성화 하여, 네이글 알고리즘을 사용하지 않도록 설정한다.
```


#### SO_REUSEADDR
````
해당 포트가 TIME_WAIT 이더라도 사용(bind) 할 수 있다.
```


```
...
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_BACKLOG, 1)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     p.addLast(new EchoServerHandler());
                 }
             });
...```


#### SO_BACKLOG
````
.option(ChannelOption.SO_BACKLOG, 1)

백로그 큐 크기 설정
```




### childOption - 소켓 채널의 소켓 옵션 설정


#### SO_LINGER
```
클라이언트 소켓 채널의 대표적인 옵션으로 SO_LINGER 가 있다.
소켓에 대해 close 메소드를 호출한 이후의 제어권은 애플리케이션 에서 운영체제로 넘어가게 된다.
이때 커널 버퍼에 아직 전송되지 않은 데이터가 남아있으면 어떻게 처리할지 지정하는 옵션이다.

이 옵션은 "옶션의 사용여부" 와 "타임아웃"을 설정 할 수 있고,
기본값은 "사용하지 않음" 이다.

이 옵션을 키면 close 메소드가 호출되었을 때 커널버퍼의 데이터를 상대방으로 모두 전송하고, 상대방의 ACK 패킷을 기다린다.

포트상태가 TIME_WAIT 으로 전환되는 것을 방지하기 위해,
SO_LINGER 옵션을 활성화 하고, 타임아웃값을 0으로 설정하는 편법도 있다.

cf)
특히 서버 애플리케이션이 동작중ㅇ인 운영체제에서 TIME_WAIT 이 많이 생기는 것을 방지하기 위해 사용하거나,
Proxy 서버가 동작중인 운영체제에서 TIME_WAIT 이 발생하는 것을 방지하기 위해 사용함.
```


```
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             
             ### 연결된 클라이언트 채널에 대해 SO_LINGER 옵션을 설정했다. 
             ### 이때 두번재 인자로 0 을 주었으므로, 커널 버퍼에 남은 데이터를 상대방 소켓 채널로 모두 전송하고, 즉시 연결을 끊는다.
             ### 이 방법은 TIME_WAIT 이 발생하지 않는 장점을 가지고 있지만,
             ### 반대로 마지막으로 전송한 데이터가 클라이언트로 모두 전송되었는지 확인할 방법이 없다.
             ###
             ### 또 한가지 유의해야할 부분은 블로킹 모드의 소켓을 사용할 때의 SO_LINGER 옵션 설정이다.
             ### 블로킹 모드 소켓을 사용하면서, SO_LINGER 옵션을 활성화 한뒤, 타임아웃값을 1초로 지정했다고 가정하자.
             ### 이때 클라이언트로 부터 ACK 패킷이 도착하지 않으면, 지정된 타임아웃 시간동안 블로킹 된다.
             .childOption(ChannelOption.SO_LINGER, 0)
             .childHandler(new ChannelInitializer<SocketChannel>() {
```





