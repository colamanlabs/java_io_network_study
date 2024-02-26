   
# java_network_girl_netty_20240226_0001.md


## 6.2 네티 바이트 버퍼 ( page 167 )


```
네티 바이트 버퍼는 자바 바이트 버퍼에 비하여 더 빠른 성능을 제공한다.
또한, 네티 바이트 버퍼 풀은 빈번한 바이트 버퍼 할당과 해제에 대한 부담을 줄여주어,
가비지 컬렉션에 대한 부담을 줄여준다.

네티 바이트 버퍼는 저장되는 데이터 형에 대한 별도의 바이트 버퍼를 제공하지 않는 대신
각 데이터형에 대한 읽기 쓰기 메소드를 제공한다.

읽기 쓰기 메소드는 readFloat, writeFloat 과 같이 행동을 의미하는 read/write 접두사와, 
데이터형을 의미하는 접미사를 사용한다.

또한, 읽기, 쓰기 메소드가 실행될 때 각각 읽기 인덱스와 쓰기 인덱스를 증가시킨다.
즉, 읽기 인덱스와 쓰기 인덱스가 분리되어 있기 때문에,
별도의 메소드 호출 없이 읽기와 쓰기를 수행할 수 있다.

네티의 바이트 버퍼는 위와 같은 이유로 읽기 작업이 완료된 후에 쓰기작업을 위해서 flip 메소드 호출이 필요없다.
그러므로, 하나의 바이트 버퍼에 대하여 쓰기 작업과 읽기 작업을 병행할 수 있다.
```


### 6.2.1 네티 바이트 버퍼 생성

```
    @Test
    public void createUnpooledHeapBufferTest()
    {
        // https://netty.io/4.1/api/io/netty/buffer/Unpooled.html
        // 언풀드 힙버퍼 생성
        ByteBuf buf = Unpooled.buffer(11);
    }
    
    @Test
    public void createUnpooledDirectBufferTest()
    {
        // 언풀드 다이렉트 버퍼 생성
        ByteBuf buf = Unpooled.directBuffer(11);
    }
    
    @Test
    public void createPooledHeapBufferTest()
    {
        // https://javadoc.io/static/io.netty/netty-all/4.1.66.Final/io/netty/buffer/PooledByteBufAllocator.html
        // 풀드 힙 버퍼 생성
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(11);
    }
    
    @Test
    public void createPooledDirectBufferTest()
    {
        // 풀드 다이렉트 버퍼 생성
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(11);
    }
```

### 6.2.2 버퍼 사용

#### 바이트 버퍼 읽기 쓰기
```
네티 바이트 버퍼는 자바 바이트 버퍼와 달리 읽기 쓰기 전환에 flip 메소드를 호출하지 않는다.

```

#### 가변 크기 버퍼
```
네티의 바이트 버퍼는 capacity 메소드를 사용하여, 바이트 버퍼의 크기를 동적으로 조절할 수 있다.
단, 저장된 데이터보다 작은 크기로 조절하면 나머지 데이터는 잘려진다.

...
buf.capacity(6);
buf.capacity(13);
...

```


#### 바이트 버퍼 풀링
```
#### 바이트 버퍼 풀링 장점
- 버퍼를 빈번히 할당하고 해제할때 일어나는 가비지 컬렉션 횟수의 감소다.

```


#### 부호없는 값 읽기
```
    public void unsignedBufferToJavaBuffer()
    {
        ByteBuf buf = Unpooled.buffer(11);        
        buf.writeShort(-1);        
        assertEquals(65535, buf.getUnsignedShort(0));
    }
```



#### 엔디안 변환
```
    public void pooledHeapBufferTest()
    {
        ByteBuf buf = Unpooled.buffer(11);
        assertEquals(ByteOrder.BIG_ENDIAN, buf.order());		 
        
        buf.markReaderIndex();	// 
        buf.writeShort(1);
        assertEquals(1, buf.readShort());
        
        buf.resetReaderIndex();
        
        ByteBuf lettleEndianBuf = buf.order(ByteOrder.LITTLE_ENDIAN);
        assertEquals(256, lettleEndianBuf.readShort());
    }
```


### 바이트 버퍼 상호변환 

```
ByteBuf buf = Unpooled.buffer(11);
ByteBUffer nioByteBuffer = buf.nioBuffer();
...
```