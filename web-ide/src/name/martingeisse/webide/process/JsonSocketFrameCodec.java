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
 * This codec converts between a boundary-less character
 * stream and json-socket frames. It requires
 * {@link CharacterStreamCodec} on its downstreasm side
 * because it can only handle characters, not bytes.
 */
public class JsonSocketFrameCodec extends SimpleChannelHandler {

	/**
	 * the inputBuilder
	 */
	private StringBuilder inputBuilder = new StringBuilder();
	
	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(final ChannelHandlerContext context, final MessageEvent event) throws Exception {
		synchronized(inputBuilder) {
			
			// extract the buffer from the message event and append it to the builder
			final Object message = event.getMessage();
			if (!(message instanceof CharSequence)) {
				context.sendUpstream(event);
				return;
			}
			inputBuilder.append((CharSequence)message);
			
			// extract as many frames as possible
			while (true) {
				int hashIndex = inputBuilder.indexOf("#");
				if (hashIndex == -1) {
					// we haven't event received the length of the next frame
					break;
				}
				String lengthText = inputBuilder.substring(0, hashIndex);
				int length;
				try {
					length = Integer.parseInt(lengthText);
				} catch (NumberFormatException e) {
					// TODO: we should be able to recover by skipping garbled characters,
					// then picking up the next frame. However, that requires that we
					// recognize the frame header (length + '#').
					throw new RuntimeException(e);
				}
				if (inputBuilder.length() < hashIndex + 1 + length) {
					// we haven't received the whole frame yet
					return;
				}
				String frameContents = inputBuilder.substring(hashIndex + 1, hashIndex + 1 + length);
				CUT FRAME
				Channels.fireMessageReceived(context, frameContents, event.getRemoteAddress());				
			}
			
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#writeRequested(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void writeRequested(final ChannelHandlerContext context, final MessageEvent e) throws Exception {
		
		TODO
		
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
