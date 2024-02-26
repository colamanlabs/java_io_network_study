import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

import org.junit.Test;

public class CreateByteBufferByNettyTest
{
    @Test
    public void createUnpooledHeapBufferTest()
    {
        // https://netty.io/4.1/api/io/netty/buffer/Unpooled.html
        // 언풀드 힙버퍼 생성
        ByteBuf buf = Unpooled.buffer(11);
        
        testBuffer(buf, false);
    }
    
    @Test
    public void createUnpooledDirectBufferTest()
    {
        // 언풀드 다이렉트 버퍼 생성
        ByteBuf buf = Unpooled.directBuffer(11);
        
        testBuffer(buf, true);
    }
    
    @Test
    public void createPooledHeapBufferTest()
    {
        // https://javadoc.io/static/io.netty/netty-all/4.1.66.Final/io/netty/buffer/PooledByteBufAllocator.html
        // 풀드 힙 버퍼 생성
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(11);
        
        testBuffer(buf, false);
    }
    
    @Test
    public void createPooledDirectBufferTest()
    {
        // 풀드 다이렉트 버퍼 생성
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(11);
        
        testBuffer(buf, true);
    }
    
    private void testBuffer(ByteBuf buf, boolean isDirect)
    {
        assertEquals(11, buf.capacity());
        
        assertEquals(isDirect, buf.isDirect());
        
        assertEquals(0, buf.readableBytes());
        assertEquals(11, buf.writableBytes());
    }
}
