/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.ingame;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import name.martingeisse.miner.common.MinerCommonConstants;
import name.martingeisse.miner.common.MinerPacketConstants;
import name.martingeisse.miner.startmenu.AccountApiClient;
import name.martingeisse.stackd.client.network.StackdProtocolClient;
import name.martingeisse.stackd.common.network.StackdPacket;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * Application-specific protocol client.
 */
public class MinerProtocolClient extends StackdProtocolClient {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(MinerProtocolClient.class);
	
	/**
	 * the updatedPlayerProxies
	 */
	private List<PlayerProxy> updatedPlayerProxies;
	
	/**
	 * the updatedPlayerNames
	 */
	private Map<Integer, String> updatedPlayerNames;
	
	/**
	 * the playerResumedMessage
	 */
	private PlayerResumedMessage playerResumedMessage;
	
	/**
	 * the coins
	 */
	private volatile long coins = 0;

	/**
	 * Constructor.
	 */
	public MinerProtocolClient() {
		super(IngameHandler.serverName, MinerCommonConstants.NETWORK_PORT);
	}
	
	/**
	 * Sends an update message for the player's position to the server.
	 * @param x the player's x position
	 * @param y the player's y position
	 * @param z the player's z position
	 */
	public void sendPositionUpdate(double x, double y, double z) {
		StackdPacket packet = new StackdPacket(MinerPacketConstants.TYPE_C2S_UPDATE_POSITION, 24);
		ChannelBuffer buffer = packet.getBuffer();
		buffer.writeDouble(x);
		buffer.writeDouble(y);
		buffer.writeDouble(z);
		send(packet);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.network.StackdProtocolClient#onReady()
	 */
	@Override
	protected void onReady() {
		
		// send the "resume player" packet
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeZero(StackdPacket.HEADER_SIZE);
		byte[] tokenBytes = AccountApiClient.getInstance().getPlayerAccessToken().getBytes(StandardCharsets.UTF_8);
		buffer.writeInt(tokenBytes.length);
		buffer.writeBytes(tokenBytes);
		send(new StackdPacket(MinerPacketConstants.TYPE_C2S_RESUME_PLAYER, buffer, false));
		
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.network.StackdProtocolClient#onApplicationPacketReceived(name.martingeisse.stackd.common.netty.StackdPacket)
	 */
	@Override
	protected void onApplicationPacketReceived(StackdPacket packet) {
		ChannelBuffer buffer = packet.getBuffer();
		switch (packet.getType()) {
		
		case MinerPacketConstants.TYPE_S2C_PLAYER_LIST_UPDATE: {
			List<PlayerProxy> playerProxiesFromMessage = new ArrayList<PlayerProxy>();
			while (buffer.readableBytes() >= 28) {
				int id = buffer.readInt();
				double x = buffer.readDouble();
				double y = buffer.readDouble();
				double z = buffer.readDouble();
				playerProxiesFromMessage.add(new PlayerProxy(id, x, y, z));
			}
			synchronized(this) {
				this.updatedPlayerProxies = playerProxiesFromMessage;
			}
			break;
		}
			
		case MinerPacketConstants.TYPE_S2C_PLAYER_NAMES_UPDATE: {
			Map<Integer, String> updatedPlayerNames = new HashMap<Integer, String>();
			while (buffer.readableBytes() > 0) {
				int id = buffer.readInt();
				int length = buffer.readInt();
				byte[] data = new byte[2 * length];
				buffer.readBytes(data);
				try {
					updatedPlayerNames.put(id, new String(data, "UTF-16"));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			synchronized(this) {
				this.updatedPlayerNames = updatedPlayerNames;
			}
			break;
		}
		
		case MinerPacketConstants.TYPE_S2C_PLAYER_RESUMED: {
			synchronized(this) {
				this.playerResumedMessage = new PlayerResumedMessage(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
			}
			break;
		}

		case MinerPacketConstants.TYPE_S2C_UPDATE_COINS: {
			this.coins = buffer.readLong();
			logger.info("update coins: " + coins);
			break;
		}
		
		}
	}
	
	/**
	 * If there is an updated list of player proxies, returns that
	 * list and deletes it from this object.
	 * 
	 * @return the updated player proxies, or null if no update
	 * is available
	 */
	public synchronized List<PlayerProxy> fetchUpdatedPlayerProxies() {
		List<PlayerProxy> result = updatedPlayerProxies;
		updatedPlayerProxies = null;
		return result;
	}	
	
	/**
	 * If there is an updated map of player names, returns that
	 * map and deletes it from this object.
	 * 
	 * @return the updated player names, or null if no update
	 * is available
	 */
	public synchronized Map<Integer, String> fetchUpdatedPlayerNames() {
		Map<Integer, String> result = updatedPlayerNames;
		updatedPlayerNames = null;
		return result;
	}
	
	/**
	 * If there is a {@link PlayerResumedMessage}, returns that
	 * message and deletes it from this object.
	 * 
	 * @return the message, or null if no message is available
	 */
	public synchronized PlayerResumedMessage fetchPlayerResumedMessage() {
		PlayerResumedMessage result = playerResumedMessage;
		playerResumedMessage = null;
		return result;
	}

	/**
	 * Sends a notification about the fact that this player has dug away a cube.
	 * @param x the x position of the cube
	 * @param y the y position of the cube
	 * @param z the z position of the cube
	 */
	public void sendDigNotification(int x, int y, int z) {
		StackdPacket packet = new StackdPacket(MinerPacketConstants.TYPE_C2S_DIG_NOTIFICATION, 13);
		ChannelBuffer buffer = packet.getBuffer();
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		send(packet);
	}
	
	/**
	 * Getter method for the coins.
	 * @return the coins
	 */
	public long getCoins() {
		return coins;
	}
	
}
