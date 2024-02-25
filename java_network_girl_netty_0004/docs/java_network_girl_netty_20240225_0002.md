 
# java_network_girl_netty_20240225_0002.md



# page 93)

## 3.3.2 Bootstrap API

https://netty.io/4.1/api/io/netty/bootstrap/Bootstrap.html
```
클라이언트 애플리케이션을 설정하는 Bootstrap API 확인.
```



```
        /*
         * 클라이언트 애플리케이션은 서버와 달리 서버에 연결된 채널 하나만 조재하기 때문에, 이벤트 루프 그룹이 하나다. 
         */
        EventLoopGroup group = new NioEventLoopGroup();
        
        try
        {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                public void initChannel(SocketChannel ch) throws Exception
                {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new EchoClientHandler());
                }
            });
```

### group - 이벤트 루프 설정
```
Bootstrap 의 group 메소드는 ServerBootstrap 과 달리 단 하나의 이벤트 루프만 설정할 수 있다.
클라이언트 애플리케이션은 서버에 연결한 소켓 채널 하나만 가지고 있기 때문에,
채널의 이벤트를 처리할 이벤트 루프도 하나다.
```


### channel - 소켓 입출력 모드 설정
```
클라이언트 소켓의 입출력 모드를 설정한다.
```


### channelFactory - 소켓 입출력 모드 설정 
```
ServerBootstrap 의 channelFactory 메소드와 동일 동작을 수행한다.
```


### handler - 클라이언트 소켓 채널의 이벤트 핸들러 설정
```
클라이언트 소켓 채널에서 발생하는 이벤트를 수신하여 처리한다.
```



### option - 소켓 채널의 소켓 옵션 설정 
```
Bootstrap 의 option 메소드는 서버와 연결된 클라이언트 소켓 채널의 옵션을 설정한다.
```



# 3장(page 96) 끝
#
#
#
#
#

# 4장 채널 파이프 라인과 코덱
내일 하자.


