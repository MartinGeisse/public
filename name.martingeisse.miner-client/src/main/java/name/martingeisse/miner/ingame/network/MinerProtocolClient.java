/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.ingame.network;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import name.martingeisse.miner.common.MinerCommonConstants;
import name.martingeisse.miner.common.MinerPacketConstants;
import name.martingeisse.miner.ingame.IngameHandler;
import name.martingeisse.miner.ingame.player.PlayerProxy;
import name.martingeisse.miner.startmenu.AccountApiClient;
import name.martingeisse.stackd.client.network.StackdProtocolClient;
import name.martingeisse.stackd.common.geometry.EulerAngles;
import name.martingeisse.stackd.common.geometry.ReadableEulerAngles;
import name.martingeisse.stackd.common.geometry.ReadableVector3d;
import name.martingeisse.stackd.common.geometry.Vector3d;
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
	 * @param position the player's position
	 * @param orientation the player's orientation
	 */
	public void sendPositionUpdate(ReadableVector3d position, ReadableEulerAngles orientation) {
		StackdPacket packet = new StackdPacket(MinerPacketConstants.TYPE_C2S_UPDATE_POSITION, 40);
		ChannelBuffer buffer = packet.getBuffer();
		buffer.writeDouble(position.getX());
		buffer.writeDouble(position.getY());
		buffer.writeDouble(position.getZ());
		buffer.writeDouble(orientation.getHorizontalAngle());
		buffer.writeDouble(orientation.getVerticalAngle());
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
				double horizontalAngle = buffer.readDouble();
				double verticalAngle = buffer.readDouble();
				PlayerProxy proxy = new PlayerProxy(id);
				proxy.getPosition().setX(x);
				proxy.getPosition().setY(y);
				proxy.getPosition().setZ(z);
				proxy.getOrientation().setHorizontalAngle(horizontalAngle);
				proxy.getOrientation().setVerticalAngle(verticalAngle);
				playerProxiesFromMessage.add(proxy);
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
				double x = buffer.readDouble();
				double y = buffer.readDouble();
				double z = buffer.readDouble();
				double horizontalAngle = buffer.readDouble();
				double verticalAngle = buffer.readDouble();
				double rollAngle = 0;
				this.playerResumedMessage = new PlayerResumedMessage(new Vector3d(x, y, z), new EulerAngles(horizontalAngle, verticalAngle, rollAngle));
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
