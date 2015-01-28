/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.network;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import name.martingeisse.stackd.common.network.StackdPacket;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

/**
 * Stores the data for one user session (currently associated with the connection,
 * but intended to service connection dropping and re-connecting).
 * 
 * Application code may subclass this class to add application-specific
 * per-session data.
 */
public class StackdSession {

	/**
	 * the id
	 */
	private final int id;

	/**
	 * the channel
	 */
	private final Channel channel;
	
	/**
	 * Constructor.
	 * @param id the session ID
	 * @param channel the channel that connects to the client
	 */
	public StackdSession(final int id, final Channel channel) {
		this.id = id;
		this.channel = channel;
	}

	/**
	 * Getter method for the id.
	 * @return the id
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * Getter method for the channel.
	 * @return the channel
	 */
	public final Channel getChannel() {
		return channel;
	}

	/**
	 * Sends a network packet to the client that owns this session.
	 * The packet object should be considered invalid afterwards
	 * (hence "destructive") since this method will assemble header
	 * fields in the packet and alter its reader/writer index,
	 * possibly asynchronous to the calling thread.
	 * 
	 * @param packet the packet to send
	 */
	public final void sendPacketDestructive(StackdPacket packet) {
		channel.write(packet);
	}
	
	/**
	 * Sends a flash message to the client that owns this session.
	 * @param message the message
	 */
	public final void sendFlashMessage(String message) {
		byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
		StackdPacket packet = new StackdPacket(StackdPacket.TYPE_FLASH_MESSAGE, messageBytes.length);
		packet.getBuffer().writeBytes(messageBytes);
		sendPacketDestructive(packet);
	}
	
	/**
	 * Sends console output lines to the client.
	 * @param lines the lines to send
	 */
	public final void sendConsoleOutput(Collection<String> lines) {
		if (!lines.isEmpty()) {
			sendConsoleOutput(lines.toArray(new String[lines.size()]));
		}
	}

	/**
	 * Sends console output lines to the client.
	 * @param lines the lines to send
	 */
	public final void sendConsoleOutput(String... lines) {
		if (lines.length == 0) {
			return;
		}
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeZero(StackdPacket.HEADER_SIZE);
		try (ChannelBufferOutputStream out = new ChannelBufferOutputStream(buffer)) {
			for (String line : lines) {
				out.writeUTF(line);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		sendPacketDestructive(new StackdPacket(StackdPacket.TYPE_CONSOLE, buffer, false));
	}
	
}
