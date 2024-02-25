package com.colamanlabs.netty.networkgirl.s0008;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

import org.junit.Test;

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
