/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.process;

import static org.jboss.netty.buffer.ChannelBuffers.copiedBuffer;

import java.io.StringWriter;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import org.apache.commons.io.output.WriterOutputStream;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * Encodes/decodes a byte stream to a character stream without
 * requiring frames. Upstream messages are strings that contain
 * whatever character is available at that time (i.e. the string
 * boundaries have no significance).
 */
public class CharacterStreamCodec extends SimpleChannelHandler {

	/**
	 * the stringWriter
	 */
	private final StringWriter stringWriter = new StringWriter();

	/**
	 * the writerOutputStream
	 */
	private final WriterOutputStream writerOutputStream = new WriterOutputStream(stringWriter, Charset.forName("utf-8"));

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(final ChannelHandlerContext context, final MessageEvent e) throws Exception {
		String decoded;
		synchronized(stringWriter) {
			
			// extract the buffer from the message event
			final Object message = e.getMessage();
			if (!(message instanceof ChannelBuffer)) {
				context.sendUpstream(e);
				return;
			}
			final ChannelBuffer buffer = (ChannelBuffer)message;
			if (!buffer.readable()) {
				return;
			}
			
			// read all bytes from the buffer to the decoder stream
			final byte[] bytes = new byte[buffer.readableBytes()];
			buffer.readBytes(bytes);
			writerOutputStream.write(bytes);
			decoded = stringWriter.toString();
			stringWriter.getBuffer().setLength(0);
			
		}
		
		// push all successfully decoded characters upstream
		Channels.fireMessageReceived(context, decoded, e.getRemoteAddress());
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#writeRequested(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void writeRequested(final ChannelHandlerContext context, final MessageEvent e) throws Exception {
		
		// extract the string from the message event
		final Object message = e.getMessage();
		if (!(message instanceof String)) {
			context.sendDownstream(e);
			return;
		}
		String string = (String)message;
		
		// encode the string into a buffer
		ByteOrder byteOrder = context.getChannel().getConfig().getBufferFactory().getDefaultOrder();
        ChannelBuffer buffer = copiedBuffer(byteOrder, string, Charset.forName("utf-8"));
        
        // send the buffer downstream
        Channels.write(context, e.getFuture(), buffer, e.getRemoteAddress());
        
	}

}
