package com.colamanlabs.netty.networkgirl.s0008;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.Test;

public class ReadByteBufferTest
{
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
        assertEquals(4, firstBuffer.limit());   // flip 메소드 호출에 따라, limit 속성값이 마지막에 기록한 데이터의 위치로 변경된다.
        
        firstBuffer.get(3);     // 바이트 버퍼의 세번째 요소의 값을 조회한다.
        assertEquals(0, firstBuffer.position());
        assertEquals(4, firstBuffer.limit());
    }
}
