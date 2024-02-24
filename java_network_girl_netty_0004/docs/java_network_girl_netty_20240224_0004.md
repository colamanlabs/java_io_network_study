 
# java_network_girl_netty_20240224_0004.md


# 2장 네티의 주요 특징 

## 목표
```
동기와 비동기의 정의
블록킹과 논블로킹 소켓
이벤트 기반 프로그래밍
```

# 네티 홈페이지

https://netty.io/




## 2.1 동기와 비동기

## 2.2 블로킹과 논블로킹

## 2.3 이벤트 기반 프로그래밍



# 네티 상세
#
# 3장 부트스트랩

## 3.1 부트스트랩의 정의
```
부트스트랩은 네티로 작성한 네트워크 애플리케이션의 동작 방식과 환경을 설정하는 도우미 클래스다.
```

## 3.2 부트스트랩의 구조


### 부트스트랩의 논리적 구조
```
- 전송계층(소켓모드 및 IO 종류)
- 이벤트 루프(단일 스레드, 다중 스레드)
- 채널 파이프라인 설정
- 소켓 주소와 포트
- 소켓 옵션 
```

```
-- 서버 애플리케이션을 위한 ServerBootstrap 클래스
public class ServerBootstrap extends AbstractBootstrap<ServerBootstrap,ServerChannel>
https://netty.io/4.1/api/io/netty/bootstrap/ServerBootstrap.html


-- 클라이언트 어플리케이션을 위한 Bootstrap 클래스
public class Bootstrap extends AbstractBootstrap<Bootstrap,Channel>
https://netty.io/4.1/api/io/netty/bootstrap/Bootstrap.html

각 부트스트랩 클래스는 AbstractBootstrap 클래스와 Channel 인터페이스를 상속받는다.

부트스트랩은 빌더패턴을 사용하여 구현되어 있다.

빌더 패턴은 객체 생성에 사용되는 복잡한 생성자 대신 자신의 객체 참조룰 돌려주는 메소드로 초기화 한다.
생성자로 생성하면 설정되는 멤버 변수 수 조합만큼 생성자가 필요하게 되어,
생성자 코드가 비대해지는 단점을 보완한 것이다.
이와같은 구조로 클래스를 작성하면, 객체를 초기화하고자 복잡한 생성자 구조를 고민할 필요가 없으며,
애플리케이션 개발자가 원하는대로 다양한 설정을 할 수 있다.
```


## 3.3 ServerBootstrap

### [예제 3-3] 논블록킹 입출력 모드를 지원하는 ServerBootstrap 초기화
```
-- 서버 애플리케이션을 위한 ServerBootstrap 클래스
public class ServerBootstrap extends AbstractBootstrap<ServerBootstrap,ServerChannel>
https://netty.io/4.1/api/io/netty/bootstrap/ServerBootstrap.html
```


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
        	### new ServerBootstrap() 으로 객체를 생성하고        	
            ServerBootstrap b = new ServerBootstrap();
            
            ### 빌더 패턴으로 객체를 초기화 한다.
            ### bossGroup 는 클라이언트의 연결을 수락하는 부모 쓰레드 그룹
            ### workerGroup 는 연결된 클라이언트 소켓으로 부터 데이터 입출력 및 이벤트 처리를 담당하는 자식 쓰레드 그룹이다.
            ###
            ### 위의 각 NioEventLoopGroup() 의 생성자의 인수로 사용되는 숫자는 스레드 그룹내애서 생성할 최대 스레드 수를 위미한다.
            ### 1을 설정하였으므로, 부모 스레드 그룹은 단일 스레드로 동작한다.
            ### 
            ### workerGroup 은 인수없는 생성자를 호출했다. 
            ### NioEventLoopGroup 클래스의 인수없는 생성자는 사용할 스레드수를 하드웨어가 가지고 있는 CPU 코어수의 2배를 사용한다.
            b.group(bossGroup, workerGroup)
            
            ### 서버소켓(부모 스레드)이 사용할 네트워크 입출력 모드를 설정한다. 여기서는 NioServerSocketChannel 로 했으므로, NIO 모드로 동작한다.
            .channel(NioServerSocketChannel.class)
            
            ### 자식 채넗의 초기화 방법을 설정한다. 여기서는 익명 클래스로 채널 초기화 방법을 지정했다.
			### ChannelInitializer 는 클라이언트로 부터 연결된 채널이 초기화될 때의 기본동작이 지정된 추상 클래스이다.
            .childHandler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                public void initChannel(SocketChannel ch)
                {
                    log.info(String.format("[.../initChannel] BEGIN"));
                    log.info(String.format("[.../initChannel] ch:[%s]", ch));
                    
                    ### 채널 파이프라인 객체를 생성한다.
                    ChannelPipeline p = ch.pipeline();
                    
                    ### 채널 파이프라인에 EchoServerHandler 클래스를 등록한다. 
                    ### EchoServerHandler 클ㄹ래스는 이후에 클라이언트 연결이 생성되었을때 데이터 처리를 담당한다.
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

### [예제 3-4] 블록킹 입출력 모드를 지원하는 ServerBootstrap 초기화

```
package com.github.nettybook.ch3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

public class BlockingEchoServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new OioEventLoopGroup(1);
        EventLoopGroup workerGroup = new OioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
            
            
            .channel(OioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
               @Override
               public void initChannel(SocketChannel ch) {
                   ChannelPipeline p = ch.pipeline();
                   p.addLast(new EchoServerHandler());
               }
            });

            ChannelFuture f = b.bind(8888).sync();

            f.channel().closeFuture().sync();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}



https://netty.io/4.1/api/io/netty/channel/oio/OioEventLoopGroup.html

NioEventLoopGroup 를 OioEventLoopGroup 로 변경하면 블록킹 모드 소켓으로 동작한다.

```



### [예제 3-5] Epoll 입출력 모드를 지원하는 ServerBootstrap 초기화
### 리눅스 운영체에 에서만 동작한다.

```
package com.github.nettybook.ch3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;

/**
 * 리눅스 운영체제에서만 동작함.
 */
public class EpollEchoServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new EpollEventLoopGroup(1);
        EventLoopGroup workerGroup = new EpollEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(EpollServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new EchoServerHandler());
                }
            });

            ChannelFuture f = b.bind(8888).sync();

            f.channel().closeFuture().sync();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}




https://netty.io/4.1/api/io/netty/channel/epoll/EpollEventLoopGroup.html

EventLoopGroup which uses epoll under the covers. Because of this it only works on linux.

윈도우에서 실행하면 에러난다.

```



### 3.3.1 ServerBootstrap API

page 76)

내일하자.