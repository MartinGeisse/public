/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.process.msgserv;

import java.net.SocketAddress;
import java.util.Map;

import name.martingeisse.webide.process.CompanionProcess;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.SucceededChannelFuture;

/**
 * TODO: document me
 *
 */
public class MessageHandler extends SimpleChannelUpstreamHandler {

	/**
	 * the context
	 */
	private ChannelHandlerContext context;
	
	/**
	 * the remoteAddress
	 */
	private SocketAddress remoteAddress;
	
	/**
	 * the companionProcess
	 */
	private CompanionProcess companionProcess;
	
	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext context, MessageEvent messageEvent) throws Exception {
		
		// these values don't change
		this.context = context;
		this.remoteAddress = messageEvent.getRemoteAddress();
		
		// extract the message data and type
		@SuppressWarnings("unchecked")
		Map<String, Object> envelope = (Map<String, Object>)messageEvent.getMessage();
		String type = getType(envelope);
		if (type == null) {
			return;
		}
		
		// dispatch the message
		if (type.equals("init")) {
			onInit(context, messageEvent, envelope);
		} else if (type.equals("ipc")) {
			onIpc(context, messageEvent, envelope);
		}
		
	}

	private String getType(Map<String, Object> data) {
		Object value = data.get("type");
		if (value instanceof String) {
			return (String)value;
		} else {
			return null;
		}
	}
	
	private void onInit(ChannelHandlerContext context, MessageEvent messageEvent, Map<String, Object> envelope) {
		long companionId = ((Number)envelope.get("companionId")).longValue();
		this.companionProcess = CompanionProcess.getRunningProcess(companionId);
		companionProcess.internalNotifyConnected(this);
	}

	private void onIpc(ChannelHandlerContext context, MessageEvent messageEvent, Map<String, Object> envelope) {
		if (companionProcess == null) {
			return;
		}
		companionProcess.internalNotifyIpc(envelope);
	}
	
	/**
	 * Sends a low-level IPC envelope to the companion process.
	 * @param envelope the envelope to send
	 */
	public void sendEnvelope(Map<String, Object> envelope) {
        Channels.write(context, new SucceededChannelFuture(context.getChannel()), envelope, remoteAddress);
	}
	
}
