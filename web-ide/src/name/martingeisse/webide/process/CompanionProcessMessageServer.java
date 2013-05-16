/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.process;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import name.martingeisse.webide.process.netty.CharacterStreamCodec;
import name.martingeisse.webide.process.netty.JsonCodec;
import name.martingeisse.webide.process.netty.JsonSocketFrameCodec;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.json.simple.JSONObject;

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
					new TestHandler(),
					new LoggingHandler()
				);
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
			Object untypedPayload = messageEvent.getMessage();
			@SuppressWarnings("unchecked")
			Map<String, Object> payload = (Map<String, Object>)untypedPayload;
			@SuppressWarnings("unchecked")
			Map<String, Object> resultObject = new JSONObject();
			try {
				int a = ((Number)payload.get("a")).intValue();
				int b = ((Number)payload.get("b")).intValue();
				resultObject.put("result", a + b);
			} catch (Exception e) {
				resultObject.put("result", -1);
			}
	        Channels.write(context, messageEvent.getFuture(), resultObject, messageEvent.getRemoteAddress());
		}
		
	}
	
	static class MyLoggingHandler extends LoggingHandler {
		
		/* (non-Javadoc)
		 * @see org.jboss.netty.handler.logging.LoggingHandler#log(org.jboss.netty.channel.ChannelEvent)
		 */
		@Override
		public void log(ChannelEvent e) {
			super.log(e);
			if (e instanceof MessageEvent) {
				getLogger().log(getLevel(), ((MessageEvent)e).getMessage().toString());
			}
		}
		
	}
	
}
