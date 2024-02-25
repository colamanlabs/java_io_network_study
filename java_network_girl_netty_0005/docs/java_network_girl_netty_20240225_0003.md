  
# java_network_girl_netty_20240225_0003.md




# 4장 채널 파이프 라인과 코덱 
## (97 ~ 135) 


## 1) 채널 파이프라인
``` 
채널 파이프라인은 채널에서 발생한 이벤트가 이동하는 통로다.
```


## 2) 코덱
```
이 통로를 통해서 이동하는  이벤트를 처리하는 클래스를 이벤트 핸들러 라고 하며,
이벤트 핸들러를 상속받아서 구현한 구현체들을 코덱 이라고 한다.

대표적인 코엑으로 HTTP 코덱이 있다.

```

## 4.1 이벤트 실행


## 4.2 채널 파이프라인


### 4.2.1 채널 파이프라인의 구조


```
1) 채널 에서 이벤트를 채널 파이프 라인에 전달한다.

2) 채널 파이프라인에 있는 것을 이벤트 핸들러가 꺼내서 처리한다. 

```


### 4.2.2 채널 파이프라인의 동작 

```
...
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
            
            ### childHandler 메소드를 통해 연결된 클라이언트 소켓이 사용할 채널 파이프라인을 설정한다.
            ### 이때 ChannelInitializer 인터페이스를 구현한 익명 클래스를 작성하여, 
            ### childHandler 의 인자로 입력한다.
            .childHandler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                
                
                ### initChannel 메소드는 클라이언트 소캣 채널이 생성될 때 자동으로 호출되는데, 
                ### 이때 채널 파이프라인의 설정을 수행한다.
                public void initChannel(SocketChannel ch)
                {
					
					### initChannel 메소드 인자로 입력된 소캣채널(즉, 연결된 클라이언트 소캣 채널)에 설정된 채널 파이프라인을 가져오는데,
					### 네티 내부에서는 클라리언트 소켓 채널을 생성할 때 
					### 빈 채널 파이프라인 객체를 생성하여 할당한다. 
                    ChannelPipeline p = ch.pipeline();
                    
                    ### 이밴트 핸들러인 EchoServerHandler 를 채널 파이프라인에 등록하려면 
                    ### 채널 파이프라인의 add 메소드를 사용한다.
                    p.addLast(new EchoServerHandler());
                    
                }
            });
...
```



## 4.3 이벤트 핸들러 

```
네티는 비동기 호출을 지원하는 두가지 패턴을 제공한다.
첫번째는 퓨쳐 패턴이며,
두번째는 리랙터 패턴의 구현체인 이벤트 핸들러다.

소켓 채널의 이벤트를 인터페이스로 정의하고, 
이 인터페이스를 상속받은 이벤트 핸들러를 작성하여, 
채널 파이프라인에 등록한다.

채널 파이프라인으로 입력되는 이벤트를 이벤트 루프가 가로채어 이벤트에 해당하는 메소드를 수행하는 구조다.
```


### 4.3.1 채널 인바운드 이벤트 
```
네티는 소켓 채널에서 발생하는 이벤트를
인바운드 이벤트와 아웃바운드 이벤트로 추상화 한다.

인바운드 이벤트는 소켓 채널에서 발생한 이벤트 중에서 연결 상대방이 어떤 동작을 취했을 때 발생한다.

예를 들면, 채널 활성화, 데이터 수신등의 이벤트가 이에 해당한다.
```

```
1) 클라이언트 -> 서버로 데이터 전송

2) 
2-1) 소켓 채널이 받고
2-2) 데이터 수신 이벤트 발생
2-3) 채널 파이프라인에 전달 


3) 채널 파이프라인은 인바운드 이벤트 핸들러에게 전달 
```

```
io.netty.channel
Interface ChannelInboundHandler

https://netty.io/4.1/api/io/netty/channel/ChannelInboundHandler.html

void	channelActive(ChannelHandlerContext ctx)
The Channel of the ChannelHandlerContext is now active

void	channelInactive(ChannelHandlerContext ctx)
The Channel of the ChannelHandlerContext was registered is now inactive and reached its end of lifetime.

void	channelRead(ChannelHandlerContext ctx, Object msg)
Invoked when the current Channel has read a message from the peer.

void	channelReadComplete(ChannelHandlerContext ctx)
Invoked when the last message read by the current read operation has been consumed by channelRead(ChannelHandlerContext, Object).

void	channelRegistered(ChannelHandlerContext ctx)
The Channel of the ChannelHandlerContext was registered with its EventLoop

void	channelUnregistered(ChannelHandlerContext ctx)
The Channel of the ChannelHandlerContext was unregistered from its EventLoop

void	channelWritabilityChanged(ChannelHandlerContext ctx)
Gets called once the writable state of a Channel changed.

void	exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
Gets called if a Throwable was thrown.

void	userEventTriggered(ChannelHandlerContext ctx, Object evt)
Gets called if an user event was triggered.
```


#### 인바운드 이벤트 발생순서
```
네티서버에서는

1) 이벤트 루프에 채널 등록(channelRegistered)

2) 채널 활성화(channelActive)

3) 데이터 수신(channelRead)

4) 데이터 수신 완료(channelReadComplete)

5) 채널 비활성화(channelInactive)

6) 이벤트 루프에서 채널 제거(channelUnregistered)
```


#### channelRegistered 이벤트
```
channelRegistered 이벤트는 채널이 이벤트 루프에 등록되었을 때 발생한다.
이벤트 루프는 네티가 이벤트를 실행하는 스레드로서 부트스트랩에 설정한 이벤트 루프다.

channelRegistered 이벤트는 클라이언트와 서버에서 발생위치가 조금 다르다.


#### 서버(2) channelRegistered 이벤트 발생 조건
- 처음 서버 소캣 채널을 생성할 때
- 새로운 클라이언트가 서버에 접속하여, 클라이언트 소켓 채널이 생성될 때   


#### 클라이언트(1) channelRegistered 이벤트 발생 조건
- 클라이언트 에서는 서버 접속을 위한 connect 메소드를 수행할 때 channelRegistered 이벤트가 발생


channelRegistered 이벤트는 서버와 클라이언트 상관없이 새로운 채널이 생성되는 시점에 발생한다. 

``` 



#### channelActive 이벤트
```
channelActive 이벤트는 channelRegistered 이벤트 이후에 발생한다.
이는 채널이 생성되고 이벤트 루프에 등록된 이후에 네티 API 를 사용하여 채널 입출력을 수행할 상태가 되었음을 알려주는 이벤트다.

이 이벤트를 사용하기 적합한 작업의 예

-- 서버 
1) 서버 애플리케이션에 연결된 클라이언트의 연결 개수를 셀 때
2) 서버 애플리케이션에 연결된 클라이언트에게 최초 연결에 대한 메세지 전송할 때

-- 클라이언트
1) 클라이언트 애플리케이션이 연결된 서버에 최초 메시지를 전달할 때
2) 클라이언트 애플리케이션에서 서버에 연결된 상태에 대한 작업이 필요할 때 


channelActive 이벤트는 서버 또는 클라이언트가 상대방에 연결한 직후에 한 번 수행할 작업을 처리하는데 적합하다.
```



#### channelRead 이벤트
```
channelRead 이벤트는 네티로 작성된 애플리케이션에서 빈도 높게 생성되는 이벤트로서, 데이터가 수신되었음을 알려준다.

수신된 데이터는 네티의 ByteBuf 객체에 저장되어 있으며, 
이벤트의 두번째 인자인 msg 를 통해서 접근할 수 있다.
```


```
public class EchoServerV1Handler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    
    	### 네티 내부에서는 모든 데이터가 ByteBuf 로 관리된다.
        ByteBuf readMessage = (ByteBuf) msg;
        System.out.println("channelRead : " + readMessage.toString(Charset.defaultCharset()));
        
        ### 데이터를 수신하자마자, 상대방에게 데이터를 전송한다.(writeAndFlush)
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
```
  
  
  
#### channelReadComplete 이벤트
```
channelReadComplete 이벤트는 데이터 수신이 완료되었음을 알려준다.

channelRead 이벤트는 
- 채널에 데이터가 있을 때 발생하고,

channelReadComplete 이벤트는
- 채널에 데이터를 다 읽어서 더 이상 데이터가 없을 때 channelReadComplete 이벤트가 발생한다.  
```

```
public class EchoServerV2Handler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf readMessage = (ByteBuf) msg;
        System.out.println("channelRead : " + readMessage.toString(Charset.defaultCharset()));
        ctx.write(msg);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("channelReadComplete 발생");
        
        ### flush 메소드는 네티의 채널 버퍼에 저장된 데이터를 상대방으로 즉시 전송한다.
        ctx.flush();
    }
    
	### EchoServerV1Handler 는 데이터를 수신하자마자, 상대방에게 데이터를 전송한다.(writeAndFlush)
	### EchoServerV2Handler 는 소켓 채널에서 더이상 읽어들일 데이터가 없을 때 channelReadComplete 이벤트에서 데이터를 전송한다.

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
```

  
#### channelInactive 이벤트
```
channelInactive 이벤트는 channelActive 이벤트와는 반대로 채널이 비활성화 되었을 때 발생한다.
channelInactive 이벤트가 발생한 이후에는 채널에 대한 입출력 작업을 수행할 수 없다.
```


#### channelUnregistered 이벤트
```
channelUnregistered 이벤트는 channelRegistered 이벤트의 반대로 채널이 이벤트 루프에서 제거되었을 때 발생한다.
이 이벤트를 수신한 이후에는 채널에서 발생한 이벤트를 처리 할 수 없다.
```
  
  