  
# java_network_girl_netty_20240225_0006.md



#### 채널 파이프라인에 동일한 이벤트 메서드를 구현한 이벤트 핸들러가 여럿일 때의 동작 
```
채널 파이프라인에 동일한 이벤트 메서드를 구현한 이벤트 핸들러가 여럿일 때 어떻게 동작하는지 살펴보자.
```

```
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                public void initChannel(SocketChannel ch)
                {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new EchoServerV4FirstHandler());
                    p.addLast(new EchoServerV4SecondHandler());
                }
            });
```


```
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
        // ctx.fireChannelRead(msg);
        log.info(String.format("[EchoServerV4FirstHandler/channelRead] END"));
    }
}
```


```
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

```

#### 실행결과
```
18:48:42.709 [Thread-0] DEBUG org.springframework.boot.devtools.restart.classloader.RestartClassLoader - Created RestartClassLoader org.springframework.boot.devtools.restart.classloader.RestartClassLoader@209c2dd9

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
[32m :: Spring Boot :: [39m             [2m (v2.7.18)[0;39m

Starting JavaNetworkGirlNetty0006Application using Java 17 on DEFAULT3 with PID 23240 (C:\WORKS\WORKS_STS4_GITHUB\java_network_study\java_network_girl_netty_0006\target\classes started by colaman in C:\WORKS\WORKS_STS4_GITHUB\java_network_study\java_network_girl_netty_0006)
No active profile set, falling back to 1 default profile: "default"
Devtools property defaults active! Set 'spring.devtools.add-properties' to 'false' to disable
LiveReload server is running on port 35729
Started JavaNetworkGirlNetty0006Application in 0.772 seconds (JVM running for 1.321)
[JavaNetworkGirlNetty0005Application/init] BEGIN
[EchoServerV4FirstHandler/channelRead] BEGIN
[EchoServerV4FirstHandler/channelRead] [A]
[EchoServerV4FirstHandler/channelRead] END
[EchoServerV4SecondHandler/channelReadComplete] BEGIN
[EchoServerV4SecondHandler/channelReadComplete] channelReadComplete 발생
[EchoServerV4SecondHandler/channelReadComplete] END
[EchoServerV4FirstHandler/channelRead] BEGIN
[EchoServerV4FirstHandler/channelRead] [B]
[EchoServerV4FirstHandler/channelRead] END
[EchoServerV4SecondHandler/channelReadComplete] BEGIN
[EchoServerV4SecondHandler/channelReadComplete] channelReadComplete 발생
[EchoServerV4SecondHandler/channelReadComplete] END
[EchoServerV4FirstHandler/channelRead] BEGIN
[EchoServerV4FirstHandler/channelRead] [C]
[EchoServerV4FirstHandler/channelRead] END
[EchoServerV4SecondHandler/channelReadComplete] BEGIN
[EchoServerV4SecondHandler/channelReadComplete] channelReadComplete 발생
[EchoServerV4SecondHandler/channelReadComplete] END
Application shutdown requested.
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
Invocation of destroy method failed on bean with name 'inMemoryDatabaseShutdownExecutor': org.h2.jdbc.JdbcSQLNonTransientConnectionException: Database is already closed (to disable automatic closing at VM shutdown, add ";DB_CLOSE_ON_EXIT=FALSE" to the db URL) [90121-214]
HikariPool-1 - Shutdown initiated...
HikariPool-1 - Shutdown completed.
```


#### 분석
```
첫번째 이벤트 핸들러만 실행되고, 
두번째 이벤트 핸들러는 실행되지 않았다.

이벤트에 해당하는 이벤트 메소드가 수행되면서, 이벤트가 사라졌기 때문이다.
죽, 하나의 이벤트는 하나의 이벤트 메서드만 수행한다.
```


```
만약, 두번째 이벤트 핸들러의 channelRead 메서드도 수행하고 싶으면, 
첫번째 이벤트 핸들러의 코드를 수정해야 한다.

```


```
[AS-IS]
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
        // ctx.fireChannelRead(msg);
        log.info(String.format("[EchoServerV4FirstHandler/channelRead] END"));
    }
}


[TO-BE]
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
        ctx.fireChannelRead(msg);
        log.info(String.format("[EchoServerV4FirstHandler/channelRead] END"));
    }
}

다음 이벤트 핸들러로 이벤트를 넘겨주는 방법은 ChannelHandlerContext 인터페이스를 사용하여,
채널 파이프라인에 이벤트를 발생시키는 것이다.

ChannelHandlerContext 인터페이스의 ctx.fireChannelRead(msg); 메서드를 호출하면
네티는 채널 파이프라인에 channelRead 이벤트를 발생시킨다.

즉, 이벤트 메서드 이름에 fire 라는 접두어가 붙은 메소드가 이벤트를 발생시키는 메소드 이다.
```

#### 실행결과
```
18:54:09.779 [Thread-0] DEBUG org.springframework.boot.devtools.restart.classloader.RestartClassLoader - Created RestartClassLoader org.springframework.boot.devtools.restart.classloader.RestartClassLoader@13a89db

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
[32m :: Spring Boot :: [39m             [2m (v2.7.18)[0;39m

Starting JavaNetworkGirlNetty0006Application using Java 17 on DEFAULT3 with PID 4708 (C:\WORKS\WORKS_STS4_GITHUB\java_network_study\java_network_girl_netty_0006\target\classes started by colaman in C:\WORKS\WORKS_STS4_GITHUB\java_network_study\java_network_girl_netty_0006)
No active profile set, falling back to 1 default profile: "default"
Devtools property defaults active! Set 'spring.devtools.add-properties' to 'false' to disable
LiveReload server is running on port 35729
Started JavaNetworkGirlNetty0006Application in 0.775 seconds (JVM running for 1.337)
[JavaNetworkGirlNetty0005Application/init] BEGIN
[EchoServerV4FirstHandler/channelRead] BEGIN
[EchoServerV4FirstHandler/channelRead] [a]
[EchoServerV4SecondHandler/channelRead] BEGIN
[EchoServerV4SecondHandler/channelRead] [a]
[EchoServerV4SecondHandler/channelRead] END
[EchoServerV4FirstHandler/channelRead] END
[EchoServerV4SecondHandler/channelReadComplete] BEGIN
[EchoServerV4SecondHandler/channelReadComplete] channelReadComplete 발생
[EchoServerV4SecondHandler/channelReadComplete] END
[EchoServerV4FirstHandler/channelRead] BEGIN
[EchoServerV4FirstHandler/channelRead] [b]
[EchoServerV4SecondHandler/channelRead] BEGIN
[EchoServerV4SecondHandler/channelRead] [b]
[EchoServerV4SecondHandler/channelRead] END
[EchoServerV4FirstHandler/channelRead] END
[EchoServerV4SecondHandler/channelReadComplete] BEGIN
[EchoServerV4SecondHandler/channelReadComplete] channelReadComplete 발생
[EchoServerV4SecondHandler/channelReadComplete] END
[EchoServerV4FirstHandler/channelRead] BEGIN
[EchoServerV4FirstHandler/channelRead] [c]
[EchoServerV4SecondHandler/channelRead] BEGIN
[EchoServerV4SecondHandler/channelRead] [c]
[EchoServerV4SecondHandler/channelRead] END
[EchoServerV4FirstHandler/channelRead] END
[EchoServerV4SecondHandler/channelReadComplete] BEGIN
[EchoServerV4SecondHandler/channelReadComplete] channelReadComplete 발생
[EchoServerV4SecondHandler/channelReadComplete] END
Application shutdown requested.
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
Invocation of destroy method failed on bean with name 'inMemoryDatabaseShutdownExecutor': org.h2.jdbc.JdbcSQLNonTransientConnectionException: Database is already closed (to disable automatic closing at VM shutdown, add ";DB_CLOSE_ON_EXIT=FALSE" to the db URL) [90121-214]
HikariPool-1 - Shutdown initiated...
HikariPool-1 - Shutdown completed.
``` 
  

### page 120 까지 완료
