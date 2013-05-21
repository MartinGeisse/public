/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.process.msgserv;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.logging.LoggingHandler;

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
				return Channels.pipeline(
					new LoggingHandler(),
					new CharacterStreamCodec(),
					new LoggingHandler(),
					new JsonSocketFrameCodec(),
					new LoggingHandler(),
					new JsonCodec(),
					new LoggingHandler(),
					new MessageHandler(),
					new LoggingHandler()
				);
			}
		});

		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(port));

	}

}
