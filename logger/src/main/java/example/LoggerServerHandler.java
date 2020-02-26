package example;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * https://netty.io/wiki/user-guide-for-4.x.html#looking-into-the-received-data
 *
 * DiscardServerHandler extends ChannelInboundHandlerAdapter, which is an implementation of ChannelInboundHandler.
 * ChannelInboundHandler provides various event handler methods that you can override.
 * For now, it is just enough to extend ChannelInboundHandlerAdapter
 * rather than to implement the handler interface by yourself.
 */
public class LoggerServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    //We override the channelRead() event handler method here. This method is called with the received message,
    // whenever new data is received from a client. In this example, the type of the received message is ByteBuf.
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // We can contact the server via telnet: "telnet localhost 8009" and see what we send
        ByteBuf in = (ByteBuf) msg;
        try {
            System.out.println(in.toString(CharsetUtil.UTF_8));
        } finally {
            ReferenceCountUtil.release(msg); // (2)
        }
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
