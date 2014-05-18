/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import name.martingeisse.common.security.SecurityTokenUtil;
import name.martingeisse.miner.common.MinerCommonConstants;
import name.martingeisse.miner.common.MinerCubeTypes;
import name.martingeisse.miner.common.MinerPacketConstants;
import name.martingeisse.miner.server.game.DigUtil;
import name.martingeisse.miner.server.terrain.TerrainGenerator;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;
import name.martingeisse.stackd.common.geometry.SectionId;
import name.martingeisse.stackd.common.network.SectionDataId;
import name.martingeisse.stackd.common.network.SectionDataType;
import name.martingeisse.stackd.common.network.StackdPacket;
import name.martingeisse.stackd.server.network.StackdServer;
import name.martingeisse.stackd.server.section.SectionCubesCacheEntry;
import name.martingeisse.stackd.server.section.storage.MemorySectionStorage;
import name.martingeisse.webide.entity.Player;
import name.martingeisse.webide.entity.QPlayer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.joda.time.Instant;
import com.mysema.query.sql.SQLQuery;

/**
 * High-level server code.
 * 
 * Applications can subclass this class to implement their own functionality,
 * mainly by implementing
 * {@link #handleApplicationRequest(HttpServletRequest, HttpServletResponse)}.
 */
public class MinerServer extends StackdServer<MinerSession> {

	/**
	 * Constructor.
	 */
	public MinerServer() {
		
		// 1s startup time
		// super(new FolderBasedSectionStorageBackend(new ClusterSize(4), new File("data/persisted-procedural-world")));
		
		// 5-8s startup time
		// super(new CassandraSectionStorage(new ClusterSize(4), Databases.world, "sections"));
		
		// testing
		super(new MemorySectionStorage(MinerCommonConstants.CLUSTER_SIZE));
		setCubeTypes(MinerCubeTypes.CUBE_TYPES);
		
		Timer timer = new Timer(true);
		timer.schedule(new PlayerListUpdateSender(), 0, 200);
		timer.schedule(new PlayerNameUpdateSender(), 0, 2000);
		
		//
		setConsoleCommandHandler(new MinerConsoleCommandHandler(this));		
		
		// TODO for testing
		try {
			// initializeWorld();
			initializeWorldWithHeightField();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.StackdServer#newSession(int, org.jboss.netty.channel.Channel)
	 */
	@Override
	protected MinerSession newSession(int id, Channel channel) {
		return new MinerSession(id, channel);
	}
	
	/**
	 * Handles an "initialize world" request.
	 * 
	 * @throws Exception on errors 
	 */
	public void initializeWorld() throws Exception {
		
		TestRegionImporter importer = new TestRegionImporter(getSectionWorkingSet().getStorage());
		// importer.setTranslation(-15, -9, -72);
		// importer.importRegions(new File("resource/fis/region"));
		importer.importRegions(new File("resource/stoneless"));
		
		getSectionWorkingSet().getSectionDataCache().clearCache();
		System.out.println("world initialized");
		
	}
	
	/**
	 * Initializes the world using a Perlin noise based height field.
	 */
	public void initializeWorldWithHeightField() {
		int horizontalRadius = 10;
		int verticalRadius = 5;
		TerrainGenerator terrainGenerator = new TerrainGenerator();
		terrainGenerator.generate(getSectionWorkingSet().getStorage(), new SectionId(-horizontalRadius, -verticalRadius, -horizontalRadius), new SectionId(horizontalRadius, verticalRadius, horizontalRadius));
		getSectionWorkingSet().getSectionDataCache().clearCache();
		System.out.println("world initialized");
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
	protected void onClientConnected(MinerSession session) {
		try {
			session.sendFlashMessage("Connected to server.");
			session.sendCoinsUpdate();
		} finally {
			EntityConnectionManager.disposeConnections();
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.StackdServer#onApplicationPacketReceived(name.martingeisse.stackd.server.StackdSession, name.martingeisse.stackd.common.netty.StackdPacket)
	 */
	@Override
	protected void onApplicationPacketReceived(MinerSession session, StackdPacket packet) {
		ChannelBuffer buffer = packet.getBuffer();
		switch (packet.getType()) {
		
		case MinerPacketConstants.TYPE_C2S_UPDATE_POSITION: {
			session.setX(buffer.readDouble());
			session.setY(buffer.readDouble());
			session.setZ(buffer.readDouble());
			break;
		}
			
		case MinerPacketConstants.TYPE_C2S_RESUME_PLAYER: {
			byte[] tokenBytes = new byte[buffer.readInt()];
			buffer.readBytes(tokenBytes);
			String token = new String(tokenBytes, StandardCharsets.UTF_8);
			String tokenSubject = SecurityTokenUtil.validateToken(token, new Instant(), MinerServerSecurityConstants.SECURITY_TOKEN_SECRET);
			long playerId = Long.parseLong(tokenSubject);
			try {
				final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
				query.from(QPlayer.player);
				query.where(QPlayer.player.id.eq(playerId));
				Player player = query.singleResult(QPlayer.player);
				if (player == null) {
					throw new RuntimeException("player not found, id: " + playerId);
				}
				session.setPlayerId(player.getId());
				session.setX(player.getX().doubleValue());
				session.setY(player.getY().doubleValue());
				session.setZ(player.getZ().doubleValue());
				session.sendCoinsUpdate();
			} finally {
				EntityConnectionManager.disposeConnections();
			}
			StackdPacket responsePacket = new StackdPacket(MinerPacketConstants.TYPE_S2C_PLAYER_RESUMED, 24);
			ChannelBuffer responseBuffer = responsePacket.getBuffer();
			responseBuffer.writeDouble(session.getX());
			responseBuffer.writeDouble(session.getY());
			responseBuffer.writeDouble(session.getZ());
			broadcast(responsePacket);
			break;
		}
		
		case MinerPacketConstants.TYPE_C2S_DIG_NOTIFICATION: {
			
			// determine the cube being dug away
			int shiftBits = getSectionWorkingSet().getClusterSize().getShiftBits();
			int x  = buffer.readInt(), sectionX = (x >> shiftBits);
			int y  = buffer.readInt(), sectionY = (y >> shiftBits);
			int z  = buffer.readInt(), sectionZ = (z >> shiftBits);
			SectionId id = new SectionId(sectionX, sectionY, sectionZ);
			SectionCubesCacheEntry sectionDataCacheEntry = (SectionCubesCacheEntry)getSectionWorkingSet().getSectionDataCache().get(new SectionDataId(id, SectionDataType.DEFINITIVE));
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
			MinerServer.this.broadcast(packet);
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.StackdServer#onClientDisconnected(name.martingeisse.stackd.server.StackdSession)
	 */
	@Override
	protected void onClientDisconnected(MinerSession session) {
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
			List<MinerSession> sessionList = new ArrayList<MinerSession>();
			for (MinerSession session : getSessions()) {
				if (session.getPlayerId() != null) {
					sessionList.add(session);
				}
			}
			
			// assemble the packet
			StackdPacket packet = new StackdPacket(MinerPacketConstants.TYPE_S2C_PLAYER_LIST_UPDATE, 28 * sessionList.size());
			ChannelBuffer buffer = packet.getBuffer();
			for (MinerSession session : sessionList) {
				buffer.writeInt(session.getId());
				buffer.writeDouble(session.getX());
				buffer.writeDouble(session.getY());
				buffer.writeDouble(session.getZ());
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
			for (MinerSession session : getSessions()) {
				String name = session.getName();
				buffer.writeInt(session.getId());
				buffer.writeInt(name.length());
				for (int i=0; i<name.length(); i++) {
					buffer.writeChar(name.charAt(i));
				}
			}
			
			// wrap it in a packet and send it
			StackdPacket packet = new StackdPacket(MinerPacketConstants.TYPE_S2C_PLAYER_NAMES_UPDATE, buffer, false);
			broadcast(packet);
			
		}
		
	}

}
