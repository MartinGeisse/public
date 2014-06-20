/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackerspace.ingame.network;

import name.martingeisse.stackd.client.frame.AbstractIntervalFrameHandler;
import name.martingeisse.stackerspace.ingame.IngameHandler;
import name.martingeisse.stackerspace.ingame.player.Player;

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
		IngameHandler.protocolClient.sendPositionUpdate(player.getPosition(), player.getOrientation());
	}
	
}
