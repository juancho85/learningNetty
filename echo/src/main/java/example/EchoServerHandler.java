package example;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * DiscardServerHandler extends ChannelInboundHandlerAdapter, which is an implementation of ChannelInboundHandler.
 * ChannelInboundHandler provides various event handler methods that you can override.
 * For now, it is just enough to extend ChannelInboundHandlerAdapter
 * rather than to implement the handler interface by yourself.
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    // We override the channelRead() event handler method here. This method is called with the received message,
    // whenever new data is received from a client. In this example, the type of the received message is ByteBuf.
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // A ChannelHandlerContext object provides various operations that enable you to trigger various I/O
        // events and operations. Here, we invoke write(Object) to write the received message in verbatim.
        // Please note that we did not release the received message unlike we did in the DISCARD example.
        // It is because Netty releases it for you when it is written out to the wire.
        ctx.write(msg);
        // ctx.write(Object) does not make the message written out to the wire. It is buffered internally and then
        // flushed out to the wire by ctx.flush(). Alternatively, you could call ctx.writeAndFlush(msg) for brevity.
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // The exceptionCaught() event handler method is called with a Throwable when an exception was raised
        // by Netty due to an I/O error or by a handler implementation due to the exception thrown while
        // processing events. In most cases, the caught exception should be logged and its associated channel
        // should be closed here, although the implementation of this method can be different depending on what
        // you want to do to deal with an exceptional situation. For example, you might want to send a response
        // message with an error code before closing the connection.
        cause.printStackTrace();
        ctx.close();
    }
}
