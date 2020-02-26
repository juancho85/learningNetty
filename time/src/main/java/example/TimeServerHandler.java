package example;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * https://netty.io/wiki/user-guide-for-4.x.html#writing-a-time-server
 *
 * The protocol to implement in this section is the TIME protocol. It is different from the previous examples
 * in that it sends a message, which contains a 32-bit integer, without receiving any requests and closes the
 * connection once the message is sent. In this example, you will learn how to construct and send a message,
 * and to close the connection on completion.
 *
 * To test if our time server works as expected, you can use the UNIX rdate command:
 * rdate -o <port> -p <host>
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    // Because we are going to ignore any received data but to send a message as soon as a connection is established,
    // we cannot use the channelRead() method this time. Instead, we should override the channelActive() method
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        // To send a new message, we need to allocate a new buffer which will contain the message. We are going to write
        // a 32-bit integer, and therefore we need a ByteBuf whose capacity is at least 4 bytes.
        // Get the current ByteBufAllocator via ChannelHandlerContext.alloc() and allocate a new buffer.
        final ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        // we write the constructed message.
        // ByteBuf does not have such a flip method (as java.nio.ByteBuffer) because it has two pointers,
        // one for read operations and the other for write operations. The writer index increases when you write
        // something to a ByteBuf while the reader index does not change. The reader index and the writer index
        // represents where the message starts and ends respectively.

        // Another point to note is that the ChannelHandlerContext.write() (and writeAndFlush()) method returns a
        // ChannelFuture. A ChannelFuture represents an I/O operation which has not yet occurred.
        // It means, any requested operation might not have been performed yet because all operations are asynchronous
        // in Netty

        // You need to call the close() method after the ChannelFuture is complete, which was returned by the write()
        // method, and it notifies its listeners when the write operation has been done.
        final ChannelFuture f = ctx.writeAndFlush(time);
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                assert f == future;
                ctx.close();
            }
        });

        // How do we get notified when a write request is finished then? This is as simple as adding a
        // ChannelFutureListener to the returned ChannelFuture. Here, we created a new anonymous ChannelFutureListener
        // which closes the Channel when the operation is done.
        // Alternatively, you could simplify the code using a pre-defined listener:
        // f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
