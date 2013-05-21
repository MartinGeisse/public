/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.process.msgserv;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.json.simple.JSONValue;

/**
 * JSON-decodes incoming messages and JSON-encodes
 * outgoing messages. This handler expects strings
 * to be exchanged with its downstream neighbor,
 * with each string containing a single JSON-encoded
 * value.
 */
public class JsonCodec extends SimpleChannelHandler {
	
	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(final ChannelHandlerContext context, final MessageEvent event) throws Exception {
		final Object message = event.getMessage();
		if (message instanceof String) {
			Object decodedValue = JSONValue.parse((String)message);
			Channels.fireMessageReceived(context, decodedValue, event.getRemoteAddress());				
		} else {
			context.sendUpstream(event);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#writeRequested(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void writeRequested(final ChannelHandlerContext context, final MessageEvent e) throws Exception {
		String encodedValue = JSONValue.toJSONString(e.getMessage());
        Channels.write(context, e.getFuture(), encodedValue, e.getRemoteAddress());
	}

}
