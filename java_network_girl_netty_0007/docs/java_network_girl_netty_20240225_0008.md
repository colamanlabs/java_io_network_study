  
# java_network_girl_netty_20240225_0008.md

#
#
# 5장 이벤트 모델(p 138 ~ p 154)
#

```
이벤트 루프 기반 프레임워크

자바스크립트 진영
- Node.js

자바 진영
Vert.x, 네티

단일 스레드 이벤트 루프와 다중 스레드 이벤트 루프는 각각 장단점을 가지고 있다.
```


## 5.1 이벤트 루프

```
통상적인 이벤트 기반 애플리케이션이 이벤트를 처리하는 방법은 크게 두가지다.

1)
첫번째는 이벤트 리스너와, 이벤트 처리 스레드에 기반한 방법이다.
대부분의 UI 처리 프레임워크가 사용하는 방법으로,
이벤트를 처리하는 로직을 가진 이벤트 메소드를 대상 객체의 이벤트 리스너에 등록하고,
객체에 이벤트가 발생했을 때 이벤트 처리 스레드에서 등록된 메소드를 수행한다.
이때 이벤트 메소드를 수행하는 스레드는 대부분 단일 스레드로 구현한다.

2)
두번째는 이벤트 큐에 이벤트를 등록하고, 이벤트 루프가 이벤트 큐에 접근하여 처리한느 방법이다.
첫번째 방법에 비해 프레임워크의 구현이 복잡하지만, 
프레임워크 사용자 입장에서는 더 간단하게 사용할 수 있다.
```

```
이벤트 루프가 다중 스레드 일 때, 이벤트 큐는 여러개의 스레드에서 공유되며, 
가장 먼저 이벤트 큐에 접근한 스레드가 첫번째 이벤트를 가져와서 이벤트를 수행한다.
이때 이벤트큐에 입력된 이벤트를 처리하고자 이벤트 루프 스레드를 사용한다.
```

```
[이벤트 루프]
이벤트 루프란 이벤트를 실행하기 위한 무한루프 스레드를 지칭한다.

1) 객체 에서 이벤트를 발생시키고, 이벤트 큐에 이벤트를 넣는다.

2) 이벤트 루프 스레드는 이벤트 큐에서 꺼내서 이벤트를 수행한다.


이 개념에 더해서, 

- 이벤트 루프가 지원하는 스레드 종류에 따라서
	- 단일 스레드 이벤트 루프와 다중 스레드 이벤트 루프로 나뉘고

- 이벤트 루프가 처리한 이벤트의 결과를 돌려주는 방식에 따라서,
	- 1) 콜백 패턴과
	- 2) 퓨쳐 패턴 으로 나뉜다.
	- 네티는 두가지 패턴을 모두 지원한다.
```


### 5.1.1 단일 스레드와 다중 스레드 이벤트 루프
```
단일 스레드 이벤트 루프는 이벤트를 처리하는 스레드가 하나인 상태이다.

[장점]
그러므로, 이벤트 루프의 구현이 단순하고, 예측 가능한 동작을 보장한다.
또한, 하나의 스레드가 이벤트 큐에 입력된 이벤트를 처리하므로, 이벤트가 발생한 순서대로 수행할 수 있다.

[단점]
다중코어 CPU 를 효율적으로 사용하지 못하며,
이벤트 메소드 처리 시간이 오래 걸리는 작업이 섞여 있을 때, 나중에 들어온 이벤트는 처리까지 더 오랜 시간이 걸린다.


단일 스레드 이벤트 루프의 단점을 극복하려고, 다중 스레드 이벤트 루프를 사용하기도 한다.
다중 코어를 가진 서버에서는 단일 스레드 이벤트 루프를 사용하는 애플리케이션을 효율적으로 운영하려고, 여러개의 인스턴스를 실행하기도 한다.
```

```
다중 스레드 이벤트 루프는 이벤트를 처리하는 스레드가 여러개다.
단일 스레드 이벤트 루프에 비해서 프레임워크의 구현이 복잡하다.
이벤트 루프 스레드들이 이벤트 메소드를 병렬로 수행하므로, 다중 코어 CPU 를 효율적으로 사용한다.

[단점]
단점으로는 여러 이벤트 루프 스레드가 이벤트 큐 하나에 접근하므로, 여러 스레드가 하나의 자원 하나를 공유할 때 발생하는 스레드 경합이 발생한다.
또한, 여러 스레드가 이벤트 메소드를 수행하므로, 이벤트의 발생순서와 실행순서가 일치하지 않는다.

애플리케이션 개발자는 다중 스레드 이벤트 루프의 단점인 이벤트 발생 순서와, 실행 순서 불일치로 인하여, 
이벤트가 발생한 순서대로 처리된다는 가정을 하지 않아야 한다.


다중 스레드 아키텍처를 선택했다면, 반드시 사용하는 시스템에 적정한 수치로 스레드 개수를 설정 하여야 한다.
```



## 5.2 네티의 이벤트 루프
```
네티는 단일 스레드 이벤트 루프와, 다중 스레드 이벤트 루프를 모두 사용 할 수 있다.
```

```
[이벤트 발생 순서와, 실행 순서가 일치하지 않을 경우 문제가 되는 예]
1) 읽어들일 파일을 열어서, 데이터를 버퍼로 읽어들이고, 파일 닫기 이벤트를 발생시킨다.
2) 읽어들인 데이터를 채널에 기록하고, 기록완료 이벤트를 발생시킨다.
3) 데이터를 기록한 소켓 채널을 닫는 이벤트를 발생시킨다.

위 시나리오에서, 순서가 바뀌면 애플리케이션은 기대한 대로 동작하지 않는다.
```

```
네티가 다중 스레드 이벤트 루프를 사용함에도 불구하고, 이벤트 발생순서와 실행 순서를 일치시킬 수 있는 이유는 세 가지 특징에 기반한다.

- 네티의 이벤트는 채널에서 발생한다.
- 이벤트 루프 객체는 이벤트 큐를 가지고 있다.
- 네티의 채널은 하나의 이벤트 루프에 등록된다.

네티의 각 채널은 개별 이벤트 루프 스레드에 등록된다.
그러므로, 채널에서 발생한 이벤트는 항상 동일한 이벤트 루프 스레드에서 처리하여, 
이벤트 발생순서와 처리 순서가 일치된다.

이벤트 수행순서가 일치하지 않는 근본적인 이유는 이벤트 루프들이 이벤트를 공유하기 때문에 발생하는데,
네티는 이벤트 큐를 이벤트 루프 스레드의 내부에 둠으로서 수행순서 불일치의 원인을 제거 했다.
```



## 5.3 네티의 비동기 I/O 처리

```
네티는 비동기 호출을 위한 두가지 패턴을 제공한다.

1) 첫번째는 리엑터 패턴의 구현체인 이벤트 핸들러이며

2) 두번째는 퓨쳐 패턴 이다.
```

```
퓨쳐 패턴은 미래에 완료될 작업을 등록하고, 처리 결과를 확인하는 객체를 통해서, 작업의 완료를 확인하는 패턴이다.
퓨처 패턴은 메소드를 호출하는 즉시 퓨쳐 객체를 돌려준다.
메소드의 처리결과는 나중에 Futrure 객체를 통해서 확인한다.
```

```
...
ChannelFuture f = b.bind(8888).sync();
f.channel().closeFuture().sync();
...

```

```
			### 에코서버가 8888 포트를 사용하도록 바인드하는 비동기 bind 메소드를 호출한다.
			### 부트스트랩 클래스의 bind 메소드는 포트 바인딩이 완료되기 전에 ChannelFuture 객체를 돌려준다.
            ChannelFuture bindFuture = b.bind(8888);

            System.out.println("Bind 시작.");
                        
            ### ChannelFuture 인터페이스의 sync 메소드는 주어진 ChannelFuture 객체의 작업이 왆료될 때 까지 블로킹 하는 메소드다.
			### 그러므로 bind 메소드의 처리가 완료될때, sync 메소드도 같이 완료된다.            
            bindFuture.sync();
            System.out.println("Bind 완료.");
            
            ### bindFuture 객체를 통해서, 채널을 얻어온다.
            ### 여기서 얻어진 채널은 8888 포트에 바인딩 된 서버 채널이다.            
            Channel serverChannel = bindFuture.channel();
            
            ### 바인드가 완료된 서버채널의 CloseFuture 객체를 돌려준다.
            ### 네티 내부에서는 채널이 생성될 때, CloseFuture 객체도 같이 생성되므로, 
            ### closeFuture 메소드가 돌려주는 CloseFuture 객체는 항상 동일한 객체다.            
            ChannelFuture closeFuture = serverChannel.closeFuture();
            
            ### CloseFuture 객체는 채널의 연결이 종료될 때 연결 종료 이벤트를 받는다.
            ### 채널이 생성될 때 같이 생성되는 기본 CloseFuture 객체에는 아무 동작도 설정되어 있지 않으므로,
            ### 이벤트를 받았을 때 아무 동작도 하지 않는다.
            closeFuture.sync();
        }
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
```



```
네티에서는 ChannelFuture 객체에 작업이 완료되었을 때, 수행할 채널 리스너를 설정 할 수 있다.
```

```
public class EchoServerHandler extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
    	### 수신된 데이터를 클라이언트 소켓 버퍼에 기록하고, 
    	### 버퍼의 데이터를 채널로 전송하는 비동기 메소드인 writeAndFlush 를 호출하고,
    	### ChannelFuture 객체를 돌려받는다.
        ChannelFuture channelFuture = ctx.writeAndFlush(msg);
        
        ### ChannelFuture 객체에 채널을 종료하는 리스너를 등록한다.
        ### ChannelFutrureListener.CLOSE 리스너는 네티가 제공하는 기본 리스너로서,
        ### ChannelFuture 객체가 완료 이벤트를 수신할 때 수행된다.
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
}
```


https://netty.io/4.0/api/io/netty/channel/ChannelFutureListener.html

```
static ChannelFutureListener	CLOSE
A ChannelFutureListener that closes the Channel which is associated with the specified ChannelFuture.
ChannelFuture 객체가 작업 완료 이벤트를 수신했을 때, ChannelFuture 객체에 포함된 채널을 닫는다. 
작업 성공 여부와 상관없이 수행된다.


static ChannelFutureListener	CLOSE_ON_FAILURE
A ChannelFutureListener that closes the Channel when the operation ended up with a failure or cancellation rather than a success.
ChannelFuture 객체가 완료 이벤트를 수신하고,
결과가 실패일 때, ChannelFuture 객체에 포함된 채널을 닫는다.


static ChannelFutureListener	FIRE_EXCEPTION_ON_FAILURE
A ChannelFutureListener that forwards the Throwable of the ChannelFuture into the ChannelPipeline.
ChannelFuture 객체가 완료 이벤트를 수신하고,
결과가 실패일때, 채널 예외 이벤트를 발생시킨다.
```



```
ChannelFutureListener 인터페이스를 구현한 클래스를 작성하여,
ChannelFuture 객체에 등록하면 네티가 제공하는 채널 리스너 보다 더 복잡한 작업을 처리할 수 있다.

즉, 사용자 정의 채널 리스너를 구현하여 사용한다.

클라이언트 소켓 채널에 데이터 기록이 완료되었을 때 기록 완료 메시지와,
기록된 메세지의 크기를 출력하고 소켓 채널을 닫는 이벤트 핸들러 코드를 살펴뵤자.


EchoServerHandlerWithFuture 클래스는 channelRead 이벤트에서 수신한 데이터를 클라이언트로 돌려주고,
전송한 데이터 크기를 출력한 다음 클라이언트 채널을 닫는다.
클라이언트로 전송한 데이터 크기를 기록하고, 클라이언트 채널을 닫기 위해서, 
사용자 정의 채널 리스너를 사용했다.

public class EchoServerHandlerWithFuture extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        ChannelFuture channelFuture = ctx.writeAndFlush(msg);
   		
     	### 네티가 수신한 msg 객체는 ByteBuf 객체이다.
     	### 또한 클라이언트로부터 수신한 데이터를 클라이언트로 되돌려주므로, 전송한 데이터의 크기는 msg 객체의 크기와 같다.
     	### msg 객체에 저장된 데이터의 크기를 final 지역변수에 저장한다.     	
        final int writeMessageSize = ((ByteBuf) msg).readableBytes();
        
        ### 사용자 정의 채널 리스너를 생성하여, ChannelFuture 객체에 할당한다.
        channelFuture.addListener(new ChannelFutureListener()
        {
            @Override
            ### operationComplete 메소드는 ChannelFuture 객체에서 발생하는 작업완료 이벤트로서, 
            ### 사용자 정의 채널 리스너 구현에 포함되어야 한다.
            public void operationComplete(ChannelFuture future) throws Exception
            {
            
	            ### 전송한 데이터의 크기를 출력한다.
                System.out.println("전송한 Byte : " + writeMessageSize);
                
                ### ChannelFuture 객체에 포함된 채널을 가져와서 채널 닫기 이벤트를 발생시킨다.
                future.channel().close();
            }
        });
    }
...
}
```

#### 실행결과
##### client 
```
telnet 127.0.0.1 8888

 a

호스트에 대한 연결을 잃었습니다.

C:\Users\colaman>
```

##### server
```
JavaNetworkGirlNetty0007Application/init] BEGIN
Bind 시작.
Bind 완료.
전송한 Byte : 1
```


### 5장 끝 (page 153 까지 완료)
###
###
### 6장 바이트 버퍼(p 155 ~ p 181)
###


