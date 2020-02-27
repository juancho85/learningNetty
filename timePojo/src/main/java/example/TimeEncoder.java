package example;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;

//public class TimeEncoder extends ChannelOutboundHandlerAdapter {
//    @Override
//    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
//        UnixTime m = (UnixTime) msg;
//        ByteBuf encoded = ctx.alloc().buffer(4);
//        encoded.writeInt((int)m.value());
//        // First, we pass the original ChannelPromise as-is so that Netty marks it as success or failure when
//        // the encoded data is actually written out to the wire.
//        // Second, we did not call ctx.flush(). There is a separate handler method void flush(ChannelHandlerContext ctx)
//        // which is purposed to override the flush() operation.
//        ctx.write(encoded, promise); // (1)
//    }
//}

// To simplify even further, you can make use of MessageToByteEncoder:
public class TimeEncoder extends MessageToByteEncoder<UnixTime> {
    @Override
    protected void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf out) {
        out.writeInt((int)msg.value());
    }
}