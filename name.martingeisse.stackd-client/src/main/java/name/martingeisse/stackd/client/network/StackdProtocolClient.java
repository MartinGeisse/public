/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import name.martingeisse.stackd.client.console.Console;
import name.martingeisse.stackd.client.frame.handlers.FlashMessageHandler;
import name.martingeisse.stackd.common.network.StackdPacket;
import name.martingeisse.stackd.common.network.StackdPacketCodec;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.json.simple.JSONValue;

/**
 * This class handles the connection to the server. Applications are
 * free to create subclasses that add application-specific message
 * types.
 */
public class StackdProtocolClient {

	/**
	 * the connectFuture
	 */
	private final ChannelFuture connectFuture;
	
	/**
	 * the syncObject
	 */
	private final Object syncObject = new Object();
	
	/**
	 * the sessionId
	 */
	private int sessionId = -1;
	
	/**
	 * the flashMessageHandler
	 */
	private FlashMessageHandler flashMessageHandler;
	
	/**
	 * the sectionGridLoader
	 */
	private SectionGridLoader sectionGridLoader;
	
	/**
	 * the console
	 */
	private Console console;

	/**
	 * Constructor.
	 * @param host the host to connect to
	 * @param port the port to connect to
	 */
	public StackdProtocolClient(String host, int port) {
		final ChannelFactory factory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		final ClientBootstrap bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() {
				return Channels.pipeline(StackdPacketCodec.createFrameCodec(), new StackdPacketCodec(), new ApplicationHandler());
			}
		});
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		System.out.println("* " + (System.currentTimeMillis() % 100000) + ": connecting to server");
		connectFuture = bootstrap.connect(new InetSocketAddress(host, port));
	}

	/**
	 * @return true if ready, false if still connecting
	 */
	public final boolean isReady() {
		synchronized (syncObject) {
			return (sessionId != -1);
		}
	}
	
	/**
	 * Waits until this client is ready.
	 * @throws InterruptedException if interrupted while waiting
	 */
	public final void waitUntilReady() throws InterruptedException {
		synchronized (syncObject) {
			if (sessionId == -1) {
				syncObject.wait();
			}
		}
	}
	
	/**
	 * Getter method for the sessionId.
	 * @return the sessionId
	 */
	public final int getSessionId() {
		return sessionId;
	}
	
	/**
	 * Getter method for the flashMessageHandler.
	 * @return the flashMessageHandler
	 */
	public FlashMessageHandler getFlashMessageHandler() {
		return flashMessageHandler;
	}
	
	/**
	 * Setter method for the flashMessageHandler.
	 * @param flashMessageHandler the flashMessageHandler to set
	 */
	public void setFlashMessageHandler(FlashMessageHandler flashMessageHandler) {
		this.flashMessageHandler = flashMessageHandler;
	}
	
	/**
	 * Getter method for the sectionGridLoader.
	 * @return the sectionGridLoader
	 */
	public SectionGridLoader getSectionGridLoader() {
		return sectionGridLoader;
	}
	
	/**
	 * Setter method for the sectionGridLoader.
	 * @param sectionGridLoader the sectionGridLoader to set
	 */
	public void setSectionGridLoader(SectionGridLoader sectionGridLoader) {
		this.sectionGridLoader = sectionGridLoader;
	}
	
	/**
	 * Getter method for the console.
	 * @return the console
	 */
	public Console getConsole() {
		return console;
	}
	
	/**
	 * Setter method for the console.
	 * @param console the console to set
	 */
	public void setConsole(Console console) {
		this.console = console;
	}
	
	/**
	 * Sends a packet to the server.
	 * 
	 * The packet object should be considered invalid afterwards
	 * (hence "destructive") since this method will assemble header
	 * fields in the packet and alter its reader/writer index,
	 * possibly asynchronous to the calling thread.
	 * 
	 * TODO: call this method sendDestructive().
	 * 
	 * @param packet the packet to send
	 */
	public final void send(StackdPacket packet) {
		connectFuture.getChannel().write(packet);
	}
	
	/**
	 * Disconnects from the server.
	 */
	public final void disconnect() {
		connectFuture.getChannel().disconnect();
	}

	/**
	 * This method gets invoked after receiving the "hello" packet from the server.
	 * The default implementation does nothing.
	 * 
	 * NOTE: This method gets invoked by the Netty thread, asynchronous
	 * to the main game thread!
	 */
	protected void onReady() {
	}

	/**
	 * This method gets invoked when receiving an application packet from the server.
	 * The default implementation does nothing.
	 * 
	 * NOTE: This method gets invoked by the Netty thread, asynchronous
	 * to the main game thread!
	 * 
	 * @param packet the received packet
	 */
	protected void onApplicationPacketReceived(StackdPacket packet) {
	}

	/**
	 * This method gets invoked when receiving a JSON-API packet from the server.
	 * This class has already decoded the JSON payload.
	 * The default implementation does nothing.
	 * 
	 * NOTE: This method gets invoked by the Netty thread, asynchronous
	 * to the main game thread!
	 * 
	 * @param data the decoded JSON data
	 */
	protected void onJsonPacketReceived(Object data) {
	}
	
	/**
	 * This method gets invoked when receiving a flash message packet from the server.
	 * The default implementation adds the message to the flash message handler
	 * that was previously set via {@link #setFlashMessageHandler(FlashMessageHandler)}.
	 * 
	 * @param message the message
	 */
	protected void onFlashMessageReceived(String message) {
		if (flashMessageHandler != null) {
			// TODO: this happens in another thread, posibly causing a ConcurrentModificationException
			// -> use a concurrent queue for *all* messages including flash messages!
			flashMessageHandler.addMessage(message);
		}
	}

	/**
	 * Invoked when the networking code throws an exception.
	 * 
	 * NOTE: This method gets invoked by the Netty thread, asynchronous
	 * to the main game thread!
	 * 
	 * @param e the exception
	 */
	protected void onException(Throwable e) {
		throw new RuntimeException(e);
	}

	/**
	 * This method handles special protocol packets.
	 */
	private final void onProtocolPacketReceived(StackdPacket packet) {
		ChannelBuffer buffer = packet.getBuffer();
		if (packet.getType() == StackdPacket.TYPE_HELLO) {
			System.out.println("* " + (System.currentTimeMillis() % 100000) + ": hello packet received");
			synchronized (syncObject) {
				sessionId = buffer.readInt();
				if (sessionId < 0) {
					throw new RuntimeException("server sent invalid session ID: " + sessionId);
				}
				syncObject.notifyAll();
			}
			onReady();
			System.out.println("* " + (System.currentTimeMillis() % 100000) + ": protocol client ready");
		} else if (packet.getType() == StackdPacket.TYPE_JSON_API) {
			byte[] binary = new byte[buffer.readableBytes()];
			buffer.readBytes(binary);
			String json = new String(binary, StandardCharsets.UTF_8);
			Object data = JSONValue.parse(json);
			onJsonPacketReceived(data);
		} else if (packet.getType() == StackdPacket.TYPE_FLASH_MESSAGE) {
			byte[] binary = new byte[buffer.readableBytes()];
			buffer.readBytes(binary);
			String message = new String(binary, StandardCharsets.UTF_8);
			onFlashMessageReceived(message);
		} else if (packet.getType() == StackdPacket.TYPE_SINGLE_SECTION_DATA_INTERACTIVE) {
			if (sectionGridLoader != null) {
				sectionGridLoader.handleInteractiveSectionImagePacket(packet);
			} else {
				System.err.println("received interactive section image but no sectionGridLoader is set in the StackdProtoclClient!");
			}
		} else if (packet.getType() == StackdPacket.TYPE_SINGLE_SECTION_MODIFICATION_EVENT) {
			if (sectionGridLoader != null) {
				sectionGridLoader.handleModificationEventPacket(packet);
			} else {
				System.err.println("received section modification event but no sectionGridLoader is set in the StackdProtoclClient!");
			}
		} else if (packet.getType() == StackdPacket.TYPE_CONSOLE) {
			if (console != null) {
				try (ChannelBufferInputStream in = new ChannelBufferInputStream(buffer)) {
					while (buffer.readable()) {
						console.println(in.readUTF());
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			} else {
				System.err.println("received console output packet but there's no console set for the StackdProtocolClient!");
			}
		}
	}
	
	/**
	 * Sends a server-side console command to the server.
	 * @param command the command
	 * @param args the arguments
	 */
	public void sendConsoleCommand(String command, String[] args) {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeZero(StackdPacket.HEADER_SIZE);
		try (ChannelBufferOutputStream out = new ChannelBufferOutputStream(buffer)) {
			out.writeUTF(command);
			for (String arg : args) {
				out.writeUTF(arg);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		send(new StackdPacket(StackdPacket.TYPE_CONSOLE, buffer, false));
	}
	
	/**
	 * The netty handler class.
	 */
	final class ApplicationHandler extends SimpleChannelHandler {
		
		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
		 */
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
			StackdPacket packet = (StackdPacket)e.getMessage();
			if (packet.getType() < 0xff00) {
				if (sessionId != -1) {
					onApplicationPacketReceived(packet);
				}
			} else {
				onProtocolPacketReceived(packet);
			}
		}
		
		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#exceptionCaught(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ExceptionEvent)
		 */
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
			onException(e.getCause());
		}
		
	}
}
