 
# java_network_girl_netty_20240224_0001.md

# 자바 네트워크 소녀 Netty
#
# 구매도서
#
#


```
1. 
스프링부트 플랫폼에 네티 를 사용하여 테스트를 하자.

책에서는 netty.io 에서 내려받는 것으로 기재되어 있지만,
현재는 mvnrepository 를 통해 메이븐 으로 배포하고 있다.


2.
스프링부트에사 기본으로 로거와 DB 연동 환경을 제공하여, 
확장 테스트가 용이하다.  
```


## 네티버전
```
현재(2024.02.24) 기준 가장 최신 release 버전으로 한다.

https://mvnrepository.com/artifact/io.netty/netty-all/4.1.107.Final


<!-- https://mvnrepository.com/artifact/io.netty/netty-all -->
<dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-all</artifactId>
    <version>4.1.107.Final</version>
</dependency>


```

# 도서 저자 제공 예제소스
https://github.com/krisjey/netty.book.kor




# 1장 네티 맛보기



## 1.3 Discard 서버 

```

page 28) 예제소스 검토


실행후 상태 
C:\Users\colaman>netstat -an | findstr 8888
  TCP    0.0.0.0:8888           0.0.0.0:0              LISTENING
  TCP    [::]:8888              [::]:0                 LISTENING

C:\Users\colaman>


C:\Users\colaman>telnet 127.0.0.1 8888


  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/


...
[JavaNetworkGirlNetty0001Application/init] BEGIN
[DiscardServer/main] BEGIN
[DiscardServer/main] f.sync() ready......

### 최초 커넥트 성공시 
[.../initChannel] BEGIN
[.../initChannel] ch:[[id: 0xa4aaba84, L:/127.0.0.1:8888 - R:/127.0.0.1:5307]]
[.../initChannel] END

### 실제 HELLO 입력시 1글자 입력시 마다 콜백된다.
[DiscardServerHandler/channelRead0] BEGIN
[DiscardServerHandler/channelRead0] ctx:[ChannelHandlerContext(DiscardServerHandler#0, [id: 0xa4aaba84, L:/127.0.0.1:8888 - R:/127.0.0.1:5307])]
[DiscardServerHandler/channelRead0] msg:[PooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 2048)]
[DiscardServerHandler/channelRead0] END
[DiscardServerHandler/channelRead0] BEGIN
[DiscardServerHandler/channelRead0] ctx:[ChannelHandlerContext(DiscardServerHandler#0, [id: 0xa4aaba84, L:/127.0.0.1:8888 - R:/127.0.0.1:5307])]
[DiscardServerHandler/channelRead0] msg:[PooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 2048)]
[DiscardServerHandler/channelRead0] END
[DiscardServerHandler/channelRead0] BEGIN
[DiscardServerHandler/channelRead0] ctx:[ChannelHandlerContext(DiscardServerHandler#0, [id: 0xa4aaba84, L:/127.0.0.1:8888 - R:/127.0.0.1:5307])]
[DiscardServerHandler/channelRead0] msg:[PooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 1024)]
[DiscardServerHandler/channelRead0] END
[DiscardServerHandler/channelRead0] BEGIN
[DiscardServerHandler/channelRead0] ctx:[ChannelHandlerContext(DiscardServerHandler#0, [id: 0xa4aaba84, L:/127.0.0.1:8888 - R:/127.0.0.1:5307])]
[DiscardServerHandler/channelRead0] msg:[PooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 1024)]
[DiscardServerHandler/channelRead0] END
[DiscardServerHandler/channelRead0] BEGIN
[DiscardServerHandler/channelRead0] ctx:[ChannelHandlerContext(DiscardServerHandler#0, [id: 0xa4aaba84, L:/127.0.0.1:8888 - R:/127.0.0.1:5307])]
[DiscardServerHandler/channelRead0] msg:[PooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 512)]
[DiscardServerHandler/channelRead0] END
...
```
