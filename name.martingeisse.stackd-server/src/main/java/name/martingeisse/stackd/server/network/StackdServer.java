/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.network;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import name.martingeisse.stackd.common.cubetype.CubeType;
import name.martingeisse.stackd.common.geometry.SectionId;
import name.martingeisse.stackd.common.network.SectionDataId;
import name.martingeisse.stackd.common.network.SectionDataType;
import name.martingeisse.stackd.common.network.StackdPacket;
import name.martingeisse.stackd.server.console.IConsoleCommandHandler;
import name.martingeisse.stackd.server.console.NullConsoleCommandHandler;
import name.martingeisse.stackd.server.section.SectionCubesCacheEntry;
import name.martingeisse.stackd.server.section.SectionWorkingSet;
import name.martingeisse.stackd.server.section.storage.AbstractSectionStorage;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.Channel;
import org.json.simple.JSONValue;

/**
 * The main server class. Typically, a single instance of this class
 * is used on the server side, and it is actually an application-specific
 * subclass of this class.
 * 
 * @param <S> the session type
 */
public abstract class StackdServer<S extends StackdSession> {

	/**
	 * the sessions
	 */
	private final ConcurrentHashMap<Integer, S> sessions;

	/**
	 * the sectionWorkingSet
	 */
	private final SectionWorkingSet sectionWorkingSet;

	/**
	 * the cubeTypes
	 */
	private CubeType[] cubeTypes;
	
	/**
	 * the consoleCommandHandler
	 */
	private IConsoleCommandHandler<S> consoleCommandHandler;

	/**
	 * Constructor.
	 * @param sectionStorage the section storage implementation
	 */
	public StackdServer(final AbstractSectionStorage sectionStorage) {
		this.sessions = new ConcurrentHashMap<Integer, S>();
		this.sectionWorkingSet = new SectionWorkingSet(this, sectionStorage);
		this.cubeTypes = new CubeType[0];
		this.consoleCommandHandler = new NullConsoleCommandHandler<S>();
	}

	/**
	 * Getter method for the sectionWorkingSet.
	 * @return the sectionWorkingSet
	 */
	public SectionWorkingSet getSectionWorkingSet() {
		return sectionWorkingSet;
	}

	/**
	 * Getter method for the cubeTypes.
	 * @return the cubeTypes
	 */
	public final CubeType[] getCubeTypes() {
		return cubeTypes;
	}

	/**
	 * Setter method for the cubeTypes.
	 * @param cubeTypes the cubeTypes to set
	 */
	public final void setCubeTypes(final CubeType[] cubeTypes) {
		this.cubeTypes = cubeTypes;
	}

	/**
	 * Getter method for the consoleCommandHandler.
	 * @return the consoleCommandHandler
	 */
	public IConsoleCommandHandler<S> getConsoleCommandHandler() {
		return consoleCommandHandler;
	}
	
	/**
	 * Setter method for the consoleCommandHandler.
	 * @param consoleCommandHandler the consoleCommandHandler to set
	 */
	public void setConsoleCommandHandler(IConsoleCommandHandler<S> consoleCommandHandler) {
		this.consoleCommandHandler = (consoleCommandHandler == null ? new NullConsoleCommandHandler<S>() : consoleCommandHandler);
	}
	
	/**
	 * Creates a new session.
	 * 
	 * Note that a race condition might cause this method to be invoked twice to
	 * create a session for the same ID. In such a case, the race condition will be
	 * detected later on and one of the sessions will be thrown away. This method must
	 * be able to handle such a case. Use {@link #onClientConnected(StackdSession)}
	 * for code that should only run once per created session.
	 * 
	 * @param id the session ID
	 * @param channel the channel to the client
	 * @return the session
	 */
	protected abstract S newSession(int id, Channel channel);

	/**
	 * Returns the session with the specified ID, or null if no such session exists.
	 * 
	 * @param id the session ID
	 * @return the session or null
	 */
	public final S getExistingSession(final int id) {
		return sessions.get(id);
	}

	/**
	 * Returns the session with the specified ID, creating it if it does not yet exist.
	 * 
	 * @param id the session ID
	 * @param channel the channel to use when creating a session
	 * @return the session
	 */
	public final S getOrCreateSession(final int id, final Channel channel) {

		// shortcut for existing sessions
		S existingSession = sessions.get(id);
		if (existingSession != null) {
			return existingSession;
		}

		// create and store a new session in a thread-safe way
		final S newSession = newSession(id, channel);
		existingSession = sessions.putIfAbsent(id, newSession);
		final S effectiveSession = (existingSession != null ? existingSession : newSession);

		return effectiveSession;
	}

	/**
	 * Creates a new session with a random unused ID and returns it.
	 * 
	 * @param channel the channel that connects to the client
	 * @return the session
	 */
	public final S createSession(final Channel channel) {
		final Random random = new Random();
		while (true) {
			final S session = newSession(random.nextInt(0x7fffffff), channel);
			if (sessions.putIfAbsent(session.getId(), session) == null) {
				return session;
			}
		}
	}

	/**
	 * Returns a collection that contains all sessions. This collection is
	 * a live view on the session map in this server and will be changed
	 * concurrently by other threads.
	 * 
	 * @return the sessions
	 */
	public final Collection<S> getSessions() {
		return sessions.values();
	}

	/**
	 * Broadcasts a packet to all clients.
	 * 
	 * This method takes the list of clients as it exists when calling this
	 * method. Calling code must make sure that the packet does not
	 * contain information that can become invalid when the list of clients
	 * has changed since the packet was built.
	 * 
	 * Header fields of the packet will be assembled by this method. This
	 * will actually happen for each channel, but since the header fields
	 * are the same for all channel (they only depend on the packet), this
	 * should not be a problem. (Implementation note: This method still
	 * must ensure that each channel gets its own buffer object with
	 * separate reader/writer index).
	 * 
	 * @param packet the packet to broadcast to all clients
	 */
	public final void broadcast(final StackdPacket packet) {
		for (final S session : sessions.values()) {
			final StackdPacket duplicatePacket = new StackdPacket(packet.getType(), packet.getBuffer().duplicate(), false);
			session.sendPacketDestructive(duplicatePacket);
		}
	}
	
	/**
	 * Internal packet dispatch that gets called in the receiving thread.
	 * TODO should not dispatch directly but through a queue.
	 */
	final void onRawPacketReceived(final S session, final StackdPacket packet) throws Exception {
		
		// analyze the packet
		ChannelBuffer buffer = packet.getBuffer();
		switch (packet.getType()) {
		
		case StackdPacket.TYPE_JSON_API: {
			byte[] binary = new byte[buffer.readableBytes()];
			buffer.readBytes(binary);
			String json = new String(binary, StandardCharsets.UTF_8);
			Object data = JSONValue.parse(json);
			onJsonPacketReceived(session, data);
			break;
		}
		
		case StackdPacket.TYPE_CUBE_MODIFICATION: {
			
			// process modifications
			SectionWorkingSet sectionWorkingSet = getSectionWorkingSet();
			int shiftBits = sectionWorkingSet.getClusterSize().getShiftBits();
			List<SectionId> affectedSectionIds = new ArrayList<SectionId>();
			while (buffer.readable()) {
				int x  = buffer.readInt(), sectionX = (x >> shiftBits);
				int y  = buffer.readInt(), sectionY = (y >> shiftBits);
				int z  = buffer.readInt(), sectionZ = (z >> shiftBits);
				byte newCubeType = (byte)buffer.readUnsignedByte();
				SectionId sectionId = new SectionId(sectionX, sectionY, sectionZ);
				SectionDataId sectionDataId = new SectionDataId(sectionId, SectionDataType.DEFINITIVE);
				SectionCubesCacheEntry sectionDataCacheEntry = (SectionCubesCacheEntry)sectionWorkingSet.getSectionDataCache().get(sectionDataId);
				sectionDataCacheEntry.setCubeAbsolute(x, y, z, newCubeType);
				affectedSectionIds.add(sectionId);
			}
			
			// notify clients to update their render models
			SectionId[] affectedSectionIdArray = affectedSectionIds.toArray(new SectionId[affectedSectionIds.size()]);
			onSectionsModified(affectedSectionIdArray);
			
			break;
		}
		
		case StackdPacket.TYPE_SINGLE_SECTION_DATA_DEFINITIVE:
		case StackdPacket.TYPE_SINGLE_SECTION_DATA_INTERACTIVE:
		case StackdPacket.TYPE_SINGLE_SECTION_DATA_VIEW_LOD_0:
		{
			SectionId sectionId = new SectionId(buffer.readInt(), buffer.readInt(), buffer.readInt());
			SectionDataType type = SectionDataType.values()[packet.getType() - StackdPacket.TYPE_SINGLE_SECTION_DATA_BASE];
			SectionDataId dataId = new SectionDataId(sectionId, type);
			System.out.println("* " + (System.currentTimeMillis() % 100000) + ": SERVER received section data request: " + dataId);
			onSectionDataRequested(session, dataId);
			break;
		}
		
		case StackdPacket.TYPE_CONSOLE: {
			List<String> segments = new ArrayList<String>();
			try (ChannelBufferInputStream s = new ChannelBufferInputStream(buffer)) {
				while (buffer.readable()) {
					segments.add(s.readUTF());
				}
			}
			if (segments.size() < 1) {
				break;
			}
			String command = segments.remove(0);
			String[] args = segments.toArray(new String[segments.size()]);
			handleConsoleCommand(session, command, args);
			break;
		}
		
		default:
			if (packet.getType() < 0xff00) {
				onApplicationPacketReceived(session, packet);
			} else {
				System.err.println("invalid protocol packet type: " + packet.getType());
			}
			break;
		
		}
		
	}

	/**
	 * This method is called when a client has been connected.
	 * 
	 * @param session the client's session
	 */
	protected void onClientConnected(final S session) {
	}

	/**
	 * This method is called when a client has sent an application-specific
	 * binary packet.
	 * 
	 * @param session the client's session
	 * @param packet the packet
	 */
	protected void onApplicationPacketReceived(final S session, final StackdPacket packet) {
	}

	/**
	 * This method is called when a client has sent an application-specific
	 * JSON packet.
	 * 
	 * @param session the client's session
	 * @param data the decoded JSON data
	 */
	protected void onJsonPacketReceived(final S session, final Object data) {
	}

	/**
	 * This method is called when one or more sections have been modified.
	 * @param sections the modified sections
	 */
	protected void onSectionsModified(final SectionId[] sections) {
	}

	/**
	 * This method is called when a client channel has been disconnected.
	 */
	final void internalOnClientDisconnected(final S session) {
		if (session == null) {
			System.out.println("client without session disconnected");
		} else {
			final int sessionId = session.getId();
			System.out.println("client disconnected: " + sessionId);
			onClientDisconnected(session);
			sessions.remove(sessionId);
		}
	}

	/**
	 * This method is called when a client has been disconnected.
	 * 
	 * Special case: Clients that have not been allocated a session
	 * yet are handled without calling this method.
	 * 
	 * @param session the client's session
	 */
	protected void onClientDisconnected(final S session) {
	}

	/**
	 * This method is called when a client requests that we send them a section data object.
	 * 
	 * @param session the requesting session
	 * @param sectionDataId the section data object ID to send the image for
	 */
	final void onSectionDataRequested(final StackdSession session, final SectionDataId sectionDataId) {
		final SectionId sectionId = sectionDataId.getSectionId();
		final SectionDataType type = sectionDataId.getType();
		final byte[] data = getSectionWorkingSet().getSectionDataCache().get(sectionDataId).getDataForClient();
		StackdPacket response = new StackdPacket(StackdPacket.TYPE_SINGLE_SECTION_DATA_BASE + type.ordinal(), data.length + 12);
		ChannelBuffer buffer = response.getBuffer();
		buffer.writeInt(sectionId.getX());
		buffer.writeInt(sectionId.getY());
		buffer.writeInt(sectionId.getZ());
		buffer.writeBytes(data);
		session.sendPacketDestructive(response);
		System.out.println("* " + (System.currentTimeMillis() % 100000) + ": SERVER sent section data: " + sectionDataId + " (" + data.length + " bytes)");
	}
	
	/**
	 * Handles a console command. Such a command is typically sent by a client. Even if
	 * not, the command must at least be associated with a client, and is handled as if
	 * that client sent it.
	 * 
	 * The default implementation delegates to the current console command handler set
	 * for this server.
	 *  
	 * @param session the client's session
	 * @param command the command
	 * @param args the arguments
	 */
	public void handleConsoleCommand(final S session, String command, String[] args) {
		consoleCommandHandler.handleCommand(session, command, args);
	}

}
