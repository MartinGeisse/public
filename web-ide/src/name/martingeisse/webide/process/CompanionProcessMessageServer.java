/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.process;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * This server listens for JSON message stream connections
 * from companion processes.
 */
public class CompanionProcessMessageServer {

	/**
	 * Starts the message server.
	 * @param port the port to listen on
	 */
	public static void start(final int port) {

		// Configure the server.
		final Executor bossExecutor = Executors.newCachedThreadPool();
		final Executor workerExecutor = Executors.newCachedThreadPool();
		final ChannelFactory channelFactory = new NioServerSocketChannelFactory(bossExecutor, workerExecutor);
		final ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);

		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new TestHandler());
			}
		});

		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(port));

	}

	static class TestHandler extends SimpleChannelUpstreamHandler {
		
		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
		 */
		@Override
		public void messageReceived(ChannelHandlerContext context, MessageEvent messageEvent) throws Exception {
			ChannelBuffer messageBuffer = (ChannelBuffer)messageEvent.getMessage();
			byte[] bytes = new byte[messageBuffer.readableBytes()];
			*read*;
			System.err.println("*** message: " + new String(bytes, Charset.forName("utf-8")));
		}
		
	}
	
}
