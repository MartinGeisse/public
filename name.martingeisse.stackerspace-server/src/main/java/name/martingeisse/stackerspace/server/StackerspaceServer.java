/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackerspace.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;
import name.martingeisse.stackd.common.geometry.SectionId;
import name.martingeisse.stackd.common.network.SectionDataId;
import name.martingeisse.stackd.common.network.SectionDataType;
import name.martingeisse.stackd.common.network.StackdPacket;
import name.martingeisse.stackd.server.network.StackdServer;
import name.martingeisse.stackd.server.section.entry.SectionCubesCacheEntry;
import name.martingeisse.stackd.server.section.storage.CassandraSectionStorage;
import name.martingeisse.stackerspace.common.StackerspaceCommonConstants;
import name.martingeisse.stackerspace.common.StackerspaceCubeTypes;
import name.martingeisse.stackerspace.common.StackerspacePacketConstants;
import name.martingeisse.stackerspace.server.game.DigUtil;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

/**
 * High-level server code.
 * 
 * Applications can subclass this class to implement their own functionality,
 * mainly by implementing
 * {@link #handleApplicationRequest(HttpServletRequest, HttpServletResponse)}.
 */
public class StackerspaceServer extends StackdServer<StackerspaceSession> {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(StackerspaceServer.class);
	
	/**
	 * Constructor.
	 */
	public StackerspaceServer() {
		super(new CassandraSectionStorage(StackerspaceCommonConstants.CLUSTER_SIZE, Databases.world, "section_data"));
		// super(new MemorySectionStorage(StackerspaceCommonConstants.CLUSTER_SIZE));
		setCubeTypes(StackerspaceCubeTypes.CUBE_TYPES);
		
		Timer timer = new Timer(true);
		timer.schedule(new PlayerListUpdateSender(), 0, 200);
		timer.schedule(new PlayerNameUpdateSender(), 0, 2000);
		
		try {
			initializeWorldWithHeightField();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.StackdServer#newSession(int, org.jboss.netty.channel.Channel)
	 */
	@Override
	protected StackerspaceSession newSession(int id, Channel channel) {
		return new StackerspaceSession(id, channel);
	}
	
	/**
	 * Initializes the world using a Perlin noise based height field.
	 */
	public void initializeWorldWithHeightField() {
		int horizontalRadius = 10;
		int verticalRadius = 5;
		TerrainGenerator terrainGenerator = new TerrainGenerator();
		terrainGenerator.generate(getSectionWorkingSet().getStorage(), new SectionId(-horizontalRadius, -verticalRadius, -horizontalRadius), new SectionId(horizontalRadius, verticalRadius, horizontalRadius));
		getSectionWorkingSet().clearCache();
		logger.info("world initialized");
	}

	/**
	 * Handles an application-specific request.
	 * 
	 * @param request the HTTP request
	 * @param response the HTTP response
	 * @throws Exception on errors 
	 */
	public void handleApplicationRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.StackdServer#onClientConnected(name.martingeisse.stackd.server.StackdSession)
	 */
	@Override
	protected void onClientConnected(StackerspaceSession session) {
		try {
			session.sendFlashMessage("Connected to server.");
		} finally {
			EntityConnectionManager.disposeConnections();
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.StackdServer#onApplicationPacketReceived(name.martingeisse.stackd.server.StackdSession, name.martingeisse.stackd.common.netty.StackdPacket)
	 */
	@Override
	protected void onApplicationPacketReceived(StackerspaceSession session, StackdPacket packet) {
		ChannelBuffer buffer = packet.getBuffer();
		switch (packet.getType()) {
		
		case StackerspacePacketConstants.TYPE_C2S_UPDATE_POSITION: {
			session.setX(buffer.readDouble());
			session.setY(buffer.readDouble());
			session.setZ(buffer.readDouble());
			session.setLeftAngle(buffer.readDouble());
			session.setUpAngle(buffer.readDouble());
			break;
		}
			
		case StackerspacePacketConstants.TYPE_C2S_RESUME_PLAYER: {
			StackdPacket responsePacket = new StackdPacket(StackerspacePacketConstants.TYPE_S2C_PLAYER_RESUMED, 40);
			ChannelBuffer responseBuffer = responsePacket.getBuffer();
			responseBuffer.writeDouble(session.getX());
			responseBuffer.writeDouble(session.getY());
			responseBuffer.writeDouble(session.getZ());
			responseBuffer.writeDouble(session.getLeftAngle());
			responseBuffer.writeDouble(session.getUpAngle());
			broadcast(responsePacket);
			break;
		}
		
		case StackerspacePacketConstants.TYPE_C2S_DIG_NOTIFICATION: {
			
			// determine the cube being dug away
			int shiftBits = getSectionWorkingSet().getClusterSize().getShiftBits();
			int x  = buffer.readInt(), sectionX = (x >> shiftBits);
			int y  = buffer.readInt(), sectionY = (y >> shiftBits);
			int z  = buffer.readInt(), sectionZ = (z >> shiftBits);
			SectionId id = new SectionId(sectionX, sectionY, sectionZ);
			SectionCubesCacheEntry sectionDataCacheEntry = (SectionCubesCacheEntry)getSectionWorkingSet().get(new SectionDataId(id, SectionDataType.DEFINITIVE));
			byte oldCubeType = sectionDataCacheEntry.getCubeAbsolute(x, y, z);
			
			// determine whether digging is successful
			boolean success;
			if (oldCubeType == 1 || oldCubeType == 5 || oldCubeType == 15) {
				success = true;
			} else {
				success = (new Random().nextInt(3) < 1);
			}
			if (!success) {
				// TODO enable god mode -- digging always succeeds
				// break;
			}
			
			// remove the cube and notify other clients
			sectionDataCacheEntry.setCubeAbsolute(x, y, z, (byte)0);

			// TODO should not be necessary with auto-save
			sectionDataCacheEntry.save();
			
			// notify listeners
			notifyClientsAboutModifiedSections(id);
			for (AxisAlignedDirection neighborDirection : getSectionWorkingSet().getClusterSize().getBorderDirections(x, y, z)) {
				notifyClientsAboutModifiedSections(id.getNeighbor(neighborDirection));
			}
			
			// trigger special logic (e.g. add a unit of ore to the player's inventory)
			try {
				DigUtil.onCubeDugAway(session, x, y, z, oldCubeType);
			} finally {
				EntityConnectionManager.disposeConnections();
			}
			
			break;
		}
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.network.StackdServer#onSectionsModified(name.martingeisse.stackd.common.geometry.SectionId[])
	 */
	@Override
	protected void onSectionsModified(SectionId[] sectionIds) {
		notifyClientsAboutModifiedSections(sectionIds);
	}
	
	/**
	 * Sends a "section modified" event packet to all clients.
	 * @param sectionIds the modified section IDs
	 */
	public void notifyClientsAboutModifiedSections(SectionId... sectionIds) {
		for (SectionId sectionId : sectionIds) {
			StackdPacket packet = new StackdPacket(StackdPacket.TYPE_SINGLE_SECTION_MODIFICATION_EVENT, 12);
			ChannelBuffer buffer = packet.getBuffer();
			buffer.writeInt(sectionId.getX());
			buffer.writeInt(sectionId.getY());
			buffer.writeInt(sectionId.getZ());
			StackerspaceServer.this.broadcast(packet);
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.StackdServer#onClientDisconnected(name.martingeisse.stackd.server.StackdSession)
	 */
	@Override
	protected void onClientDisconnected(StackerspaceSession session) {
		try {
			session.handleDisconnect();
		} finally {
			EntityConnectionManager.disposeConnections();
		}
	}
	
	/**
	 * The runnable is run regularly to send an updated player's
	 * list to all players.
	 */
	private class PlayerListUpdateSender extends TimerTask {

		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
			
			// copy the session list to be safe against concurrent modification
			// (the number of sessions must not change since we must allocate
			// a buffer of the correct size in advance)
			List<StackerspaceSession> sessionList = new ArrayList<StackerspaceSession>();
			for (StackerspaceSession session : getSessions()) {
				if (session.getPlayerId() != null) {
					sessionList.add(session);
				}
			}
			
			// assemble the packet
			StackdPacket packet = new StackdPacket(StackerspacePacketConstants.TYPE_S2C_PLAYER_LIST_UPDATE, 44 * sessionList.size());
			ChannelBuffer buffer = packet.getBuffer();
			for (StackerspaceSession session : sessionList) {
				buffer.writeInt(session.getId());
				buffer.writeDouble(session.getX());
				buffer.writeDouble(session.getY());
				buffer.writeDouble(session.getZ());
				buffer.writeDouble(session.getLeftAngle());
				buffer.writeDouble(session.getUpAngle());
			}
			
			// send the packet
			broadcast(packet);
			
		}
		
	}

	/**
	 * The runnable is run regularly to tell the clients each other's names.
	 */
	private class PlayerNameUpdateSender extends TimerTask {

		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
			
			// assemble the buffer
			ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
			buffer.writeZero(StackdPacket.HEADER_SIZE);
			for (StackerspaceSession session : getSessions()) {
				String name = session.getName();
				buffer.writeInt(session.getId());
				buffer.writeInt(name.length());
				for (int i=0; i<name.length(); i++) {
					buffer.writeChar(name.charAt(i));
				}
			}
			
			// wrap it in a packet and send it
			StackdPacket packet = new StackdPacket(StackerspacePacketConstants.TYPE_S2C_PLAYER_NAMES_UPDATE, buffer, false);
			broadcast(packet);
			
		}
		
	}

}
