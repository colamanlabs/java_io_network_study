  
# java_network_girl_netty_20240225_0004.md




### 4.3.2 아웃바운드 이벤트 (page 112)

https://netty.io/4.1/api/io/netty/channel/ChannelOutboundHandler.html

```
아웃바운드 이벤트는 소캣 채널에서 발생한 이벤트 중에서, 
네티사용자가 요청한 동작에 해당하는 이벤트를 말하며,

연결요청, 데이터 전송, 소켓 닫기 등이 이에 해당한다.

네티는 아웃바운드 이벤트를 ChannelOutboundHandler 인터페이스로 제공한다.

https://netty.io/4.1/api/io/netty/channel/ChannelOutboundHandler.html


또한 모든 ChannelOutboundHandler 이벤트는 ChannelHandlerContext 객체를 인수로 받는다.

https://netty.io/4.1/api/io/netty/channel/ChannelHandlerContext.html
```  


#### ChannelHandlerContext 객체 (page 113)
```
ChannelHandlerContext 는 두가지 네티 객체에 대한 상호작용을 도와주는 인터페이스 이다.


1) 첫번째는 채널에 대한 입출력 처리다. 
ChannelHandlerContext 의 writeAndFlush 메소드로 채널에 데이터를 기록한다.
또는 ChannelHandlerContext 의 close 메소드로 채널의 연결을 종료할 수 있다.


2) 두번째는 채널 파이프라인에 대한 상호작용이다.
채널 파이프라인에 대한 상호작용으로는 
사용자에 의한 이벤트 발생과, 
채널 파이프라인에 등록된 이벤트 핸들러의 동적 변경이 있다.

```
ChannelOutboundHandler
https://netty.io/4.1/api/io/netty/channel/ChannelOutboundHandler.html

void	bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise)
Called once a bind operation is made.

void	close(ChannelHandlerContext ctx, ChannelPromise promise)
Called once a close operation is made.

void	connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
Called once a connect operation is made.

void	deregister(ChannelHandlerContext ctx, ChannelPromise promise)
Called once a deregister operation is made from the current registered EventLoop.

void	disconnect(ChannelHandlerContext ctx, ChannelPromise promise)
Called once a disconnect operation is made.

void	flush(ChannelHandlerContext ctx)
Called once a flush operation is made.

void	read(ChannelHandlerContext ctx)
Intercepts ChannelHandlerContext.read().

void	write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
Called once a write operation is made.
```

#### bind 이벤트 
```
bind 이벤트는 서버소켓 채널이 클라이언트 연결을 대기하는 IP 와 포트가 설정되었을 때 발생한다.
bind 이벤트는 서버 소켓 채널이 사용중인 SocketAddress 객체가 인수로 입력된다.
즉, 서버소켓 채널이 사용하는 IP 와 포트정보를 확인할 수 있다.
```


#### connect 이벤트
```
connect 이벤트는 소켓 채널이 서버에 연결되었을 때 발생한다.
connect 이벤트 에서는 원격지의 SocketAddress 정보와 로컬 SocketAddress 정보가 인수로 입력된다.

만약, 원격지의 연결을 생성할 때 로컬 SocketAddress 정보를 입력하지 않았다면, 
이 이벤트에서 수신한 로컬 SocketAddress 는 null 이다.
```

#### disconnect 이벤트
```
disconnect 이벤트는 클라이언트 소켓 채널의 연결이 끊어졌을 때 발생한다.
disconnect 이벤트에는 별도의 인수가 없다.
```

#### close 이벤트
```
close 이벤트는 클라이언트 소켓 채널의 연결이 닫혔을 때 발생한다. 
close 이벤트에는 별도의 인수가 없다.
```

#### write 이벤트
```
write 이벤트는 소켓 채널에 데이터가 기록되었을 때 발생한다.
write 이벤트에는 소켓 채널에 기록된 데이터 버퍼가 인수로 입력된다.
```


#### flush 이벤트
```
flush 이벤트는 소켓 채널에 대한 flush 메소드가 호출되었을 때 발생한다.
flush 이벤트에는 별도의 인수가 없다.
```



