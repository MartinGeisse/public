/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.ingame;

import name.martingeisse.stackd.client.frame.AbstractIntervalFrameHandler;

/**
 * This frame handler sends the player's position to the server.
 */
public class SendPositionToServerHandler extends AbstractIntervalFrameHandler {

	/**
	 * the player
	 */
	private final Player player;
	
	/**
	 * Constructor.
	 * @param player the player whose position to send
	 */
	public SendPositionToServerHandler(Player player) {
		super(200);
		this.player = player;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.AbstractIntervalFrameHandler#onIntervalTimerExpired()
	 */
	@Override
	protected void onIntervalTimerExpired() {
		IngameHandler.protocolClient.sendPositionUpdate(player.getX(), player.getY(), player.getZ(), player.getLeftAngle(), player.getUpAngle());
	}
	
}
