  
# java_network_girl_netty_20240225_0009.md

#
#
# 6장 바이트 버퍼( p 156 ~ p 181 )
#
#
### 네티가 자체 바이트 버퍼를 제공하는 이유를 살펴보고, 
### 자바의 바이트 버퍼와 비교하여 어떠한 장점이 있는지 알아본다.
###
###

## 6.1 자바 NIO 바이트 버퍼


### 샘플 소스 JUnit 테스트 주의사항 
```
JUnit 테스트시
1. 소스 메인창 우클릭 
2. Run As -> Run Configuration
3. 
- 테스트 대상 클래스 지정
- Runner 지정 "Junit 4" 로 한다.
```

```
public class CreateByteBufferTest
{
    
    /*
     * 테스트할 때 Runner 가 JUnit 4 인지 확인한다. JUnit 4 로 지정되어 있지 않으면, 테스트 결과가 안나온다.
     * 
     * 우클릭 > Run As > Run configurations
     * 
     */
    @Test
    public void createTest()
    {
        CharBuffer heapBuffer = CharBuffer.allocate(11);
        assertEquals(11, heapBuffer.capacity());
        assertEquals(false, heapBuffer.isDirect());
        
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(11);
        assertEquals(11, directBuffer.capacity());
        assertEquals(true, directBuffer.isDirect());
        
        int[] array =
        { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0 };
        IntBuffer intHeapBuffer = IntBuffer.wrap(array);
        assertEquals(11, intHeapBuffer.capacity());
        assertEquals(false, intHeapBuffer.isDirect());
    }
}
```

```
자바 NIO 바이트 버퍼는 바이트 데이터를 저장하고 읽는 저장소이다.
바이트 버퍼 클래스는 내부의 배열 상태를 관리한느 세 가지 속성을 가지고 있다.

1) capacity
버퍼에 저장할 수 있는 데이터의 최대 크기로 한번 정하면 변경이 불가능 하다.
이 값은 버퍼를 생성할 때 생성자의 인수로 입력한 값이다.

2) position
읽기 또는 쓰기가 작업중인 위치를 나타낸다.
버퍼 객체가 생성될 때 0 으로 초기화 되고, 
데이터를 입력하는 put 메소드나,
데이터를 읽는 get 메소드가 호출되면,
자동으로 증가하여 limit 와 capacity 값보다 작거나 같다.

3) limit
읽고 쓸 수 있는 버퍼 공간의 최대치를 나타낸다.
limit 메소드로 값을 조절할 수 있다.
이 값을 capacity 값보다 크게 설정할 수 없다.
```

### 6.1.1 자바 바이트 버퍼 생성

#### 바이트 버퍼를 생성하는 메소드는 모두 세가지가 존재한다.
```
allocate 
JVM 의 힙 영역에 바이트 버퍼를 생성한다.
메소드의 인수는 생성할 바이트 버퍼의 크기이며, 앞절에서 설명한 capacity 속성값이다. 
또한 생성되는 바이트 버퍼의 값은 모두 0 으로 초기화 된다.
일반적으로 힙 버퍼라고 부른다.


allocateDirect
JVM의 힙 영역이 아닌, 운영체제의 커널 영역에 바이트 버퍼를 생성한다.
allocateDirect 메소드는 ByteBuffer 추상 클래스만 사용할 수 있다.
즉, 다이렉트 버퍼는 ByteBuffer 로만 생성할 수 있다.
메소드의 인수는 생성할 바이트 버퍼의 크기이며, 앞절에서 설명한 capacity 속성값이다.
또한 생성되는 바이트 버퍼의 값은 모두 0 으로 초기화 된다.
일반적으로 다이렉트 버퍼라고 부른다.
다이렉트 버퍼는 힙버퍼에 비해 생성 시간은 길지만 더 빠른 읽기 쓰기 성능을 제공한다.


wrap
입력된 바이트 배열을 사용하여, 바이트 버퍼를 생성한다.
입력에 사용된 바이트 배열이 변경되면, wrap 메소드를 사용해서 생성한 바이트 버퍼의 내용도 변경된다.
```


### 6.1.2 버퍼 사용

```
public class ByteBufferTest2
{
    public static void main(String[] args)
    {
        ByteBuffer firstBuffer = ByteBuffer.allocate(11);
        System.out.println("초기 상태 : " + firstBuffer);
        
        byte[] source = "Hello world!".getBytes();
        
        for (byte item : source)
        {
            firstBuffer.put(item);
            System.out.println("현재 상태 : " + firstBuffer);
        }
    }
}

초기 상태 : java.nio.HeapByteBuffer[pos=0 lim=11 cap=11]
현재 상태 : java.nio.HeapByteBuffer[pos=1 lim=11 cap=11]
현재 상태 : java.nio.HeapByteBuffer[pos=2 lim=11 cap=11]
현재 상태 : java.nio.HeapByteBuffer[pos=3 lim=11 cap=11]
현재 상태 : java.nio.HeapByteBuffer[pos=4 lim=11 cap=11]
현재 상태 : java.nio.HeapByteBuffer[pos=5 lim=11 cap=11]
현재 상태 : java.nio.HeapByteBuffer[pos=6 lim=11 cap=11]
현재 상태 : java.nio.HeapByteBuffer[pos=7 lim=11 cap=11]
현재 상태 : java.nio.HeapByteBuffer[pos=8 lim=11 cap=11]
현재 상태 : java.nio.HeapByteBuffer[pos=9 lim=11 cap=11]
현재 상태 : java.nio.HeapByteBuffer[pos=10 lim=11 cap=11]
현재 상태 : java.nio.HeapByteBuffer[pos=11 lim=11 cap=11]
Exception in thread "main" java.nio.BufferOverflowException
	at java.base/java.nio.Buffer.nextPutIndex(Buffer.java:722)
	at java.base/java.nio.HeapByteBuffer.put(HeapByteBuffer.java:209)
	at com.colamanlabs.netty.networkgirl.s0008.ByteBufferTest2.main(ByteBufferTest2.java:16)

capacity 보다 position 값이 커졌기 때문에, BufferOverflowException 이 발생한다.
```

```

package com.colamanlabs.netty.networkgirl.s0008;

import java.nio.ByteBuffer;

import org.junit.Test;

public class ByteBufferTest3 {
    @Test
    public void test() {
        ByteBuffer firstBuffer = ByteBuffer.allocate(11);
        System.out.println("초기 상태 : " + firstBuffer);

        firstBuffer.put((byte) 1);		// 실행후 포지션 1
        System.out.println(firstBuffer.get());	// 실행후 포지션 2
        System.out.println(firstBuffer);
    }
}

초기 상태 : java.nio.HeapByteBuffer[pos=0 lim=11 cap=11]
0
java.nio.HeapByteBuffer[pos=2 lim=11 cap=11]

put(), get() 모두 position 을 증가시킨다.
```



```
...
    @Test
    public void test()
    {
        ByteBuffer firstBuffer = ByteBuffer.allocate(11);
        System.out.println("초기 상태 : " + firstBuffer);
        
        firstBuffer.put((byte) 1);
        firstBuffer.put((byte) 2);
        assertEquals(2, firstBuffer.position());
        
        firstBuffer.rewind();	// 실행후 포지션 0 으로 돌아간다.
        assertEquals(0, firstBuffer.position());
        
        assertEquals(1, firstBuffer.get());	// 실행후 포지션 1 증가한다.
        assertEquals(1, firstBuffer.position());
        
        System.out.println(firstBuffer);
        
    }
    
    
초기 상태 : java.nio.HeapByteBuffer[pos=0 lim=11 cap=11]
java.nio.HeapByteBuffer[pos=1 lim=11 cap=11]
  

ByteByffer.rewind() 메소드를 호출하면 position 속성을 0 으로 변경해서,
바이트 버퍼에 저장된 첫번째 값을 조회(get())할 수 있게 되고,
다시 포지션이 1 증가한다.
```

```
지금 까지는 limit 과 capacity 속성이 동일하다.

자바 바이트 버퍼는 이전에 수행한 put 또는 get 메소드가 호출된 이후의 postion 정보를 저장하기 위하여 flip 메소드를 제공한다.
flip 메소드는 자바 바이트 버퍼를 사용할 때 개발자에게 많은 혼란을 주므로 유의하여 사용해야 한다.

    @Test
    public void writeTest()
    {
        ByteBuffer firstBuffer = ByteBuffer.allocateDirect(11);
        assertEquals(0, firstBuffer.position());
        assertEquals(11, firstBuffer.limit());		// 11 이다.
        assertEquals(11, firstBuffer.capacity());
        
        firstBuffer.put((byte) 1);
        firstBuffer.put((byte) 2);
        firstBuffer.put((byte) 3);
        firstBuffer.put((byte) 4);
        assertEquals(4, firstBuffer.position());
        assertEquals(11, firstBuffer.limit());
        
        firstBuffer.flip();
        assertEquals(0, firstBuffer.position());	// position 은 0 이지만
        assertEquals(4, firstBuffer.limit());		// limit 속성이 4가 되었다.
    }
    
데이터를 기록한 이후에 flip 메소드를 호출하면, 
limit 속성값이 마지막에 기록한 데이터의 위치로 변경된다.   
```


```
    @Test
    public void readTest()
    {
        byte[] tempArray =
        { 1, 2, 3, 4, 5, 0, 0, 0, 0, 0, 0 };
        ByteBuffer firstBuffer = ByteBuffer.wrap(tempArray);
        assertEquals(0, firstBuffer.position());
        assertEquals(11, firstBuffer.limit());
        
        assertEquals(1, firstBuffer.get());
        assertEquals(2, firstBuffer.get());
        assertEquals(3, firstBuffer.get());
        assertEquals(4, firstBuffer.get());
        assertEquals(4, firstBuffer.position());
        assertEquals(11, firstBuffer.limit());
        
        firstBuffer.flip();     // 실행후 position 이 0 으로 변경된다/
        assertEquals(0, firstBuffer.position());   
        assertEquals(4, firstBuffer.limit());   
        
        firstBuffer.get(3);     // 바이트 버퍼의 세번째 요소의 값을 조회한다.
        assertEquals(0, firstBuffer.position());
        assertEquals(4, firstBuffer.limit());
    }

flip 메소드는 이전에 작업한 마지막 위치를 limit 속성으로 변경한다.
즉 flip 메소드는 쓰기작업 완료 이후에 데이터의 처음부터 읽을 수 있도록 현재 포인터의 위치를 변경하여,
읽기에서 쓰기, 또는 쓰기에서 읽기로 전환할 수 있게 된다.

그러므로, 하나의 바이트 버퍼에 대하여 읽기 작업 또는 쓰기 작업의 완료를 의미하는 flip 메소드를 호출하지 않으면 반대 작업을 수행할 수 없다.

자바의 바이트 버퍼를 사용할 때는 읽기와 쓰기를 분리하여 생각해야 하며,
특히 다중 스레드 환경에서 바이트 버퍼를 공유하지 않아야 한다.

네티는 이와 같은 자바 바이트 버퍼의 문제점을 해결하기 위해서,
읽기를 위한 인덱스와, 쓰기를 위한 인덱스를 별도로 제공한다.
```

## 6.2 네티 바이트 버퍼

