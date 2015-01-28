/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.network;

import name.martingeisse.stackd.common.network.StackdPacketCodec;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

/**
 * Creates a handler pipeline for newly connected clients.
 *
 * @param <S> the session type
 */
public class StackdNettyPipelineFactory<S extends StackdSession> implements ChannelPipelineFactory {

	/**
	 * the server
	 */
	private final StackdServer<S> server;
	
	/**
	 * Constructor.
	 * @param server the application server
	 */
	public StackdNettyPipelineFactory(StackdServer<S> server) {
		this.server = server;
	}


	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() {
		ChannelHandler frameCodec = StackdPacketCodec.createFrameCodec();
		ChannelHandler packetCodec = new StackdPacketCodec();
		ChannelHandler applicationHandler = new StackdApplicationHandler<S>(server);
		return Channels.pipeline(frameCodec, packetCodec, applicationHandler);
	}
	
}
