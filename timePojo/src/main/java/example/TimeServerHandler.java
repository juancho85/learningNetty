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
        final ChannelFuture f = ctx.writeAndFlush(new UnixTime());
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
