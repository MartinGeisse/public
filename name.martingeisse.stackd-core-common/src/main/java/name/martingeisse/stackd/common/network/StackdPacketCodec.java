/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.network;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

/**
 * Netty handler that decodes and encodes between {@link StackdPacket}
 * and correctly sized raw buffers. Other payloads are ignored.
 * 
 * This handler expects that the size of raw buffers is exactly one
 * packet. Downstream handlers must ensure this.
 */
public class StackdPacketCodec extends SimpleChannelHandler {

	/**
	 * Creates a Netty handler that splits and merges raw buffers
	 * according to packet frame boundaries. Such a handler is needed
	 * downstream of the {@link StackdPacketCodec}.
	 * 
	 * @return the handler
	 */
	public static ChannelHandler createFrameCodec() {
		return new LengthFieldBasedFrameDecoder(StackdPacket.MAX_PACKET_SIZE, 0, 2, 2, 0, true);
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		Object payload = e.getMessage();
		if (payload instanceof ChannelBuffer) {
			ChannelBuffer buffer = (ChannelBuffer)payload;
			buffer.setIndex(2, buffer.capacity());
			int type = buffer.readUnsignedShort();
			StackdPacket packet = new StackdPacket(type, buffer, false);
			Channels.fireMessageReceived(ctx, packet);
		} else {
			super.messageReceived(ctx, e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#writeRequested(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		Object payload = e.getMessage();
		if (payload instanceof StackdPacket) {
			StackdPacket packet = (StackdPacket)payload;
			packet.encodeHeader();
			Channels.write(ctx, e.getFuture(), packet.getBuffer());
		} else {
			super.writeRequested(ctx, e);
		}
	}
	
}
