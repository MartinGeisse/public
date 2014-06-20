/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackerspace.server.game;

import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.stackerspace.server.StackerspaceSession;
import name.martingeisse.webide.entity.Player;
import name.martingeisse.webide.entity.QPlayer;
import org.apache.log4j.Logger;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * Utility methods to deal with digging.
 */
public final class DigUtil {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(DigUtil.class);
	
	/**
	 * This method gets invoked when a player has dug away a cube. It handles
	 * any game logic besides the basic fact that clients must update that cube
	 * and saving the updated cube server-side. For example, this method rewards
	 * inventory items for digging ores.
	 * 
	 * @param session the player's session
	 * @param x the x position of the cube
	 * @param y the y position of the cube
	 * @param z the z position of the cube
	 * @param cubeType the cube type dug away
	 */
	public static void onCubeDugAway(StackerspaceSession session, int x, int y, int z, byte cubeType) {
		logger.info("dug cube: " + cubeType);
		
		// check for ores
		switch (cubeType) {
		
		case 16:
			oreFound(session, "coal", 2);
			break;
			
		case 10:
			oreFound(session, "gold", 5);
			break;
			
		case 11:
			oreFound(session, "diamond", 20);
			break;
			
		case 12:
			oreFound(session, "emerald", 10);
			break;
			
		case 13:
			oreFound(session, "ruby", 10);
			break;
			
		case 14:
			oreFound(session, "sapphire", 10);
			break;
		
		}
		
	}
	
	private static void oreFound(StackerspaceSession session, String name, int value) {
		Player player;
		{
			final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
			query.from(QPlayer.player).where(QPlayer.player.id.eq(session.getPlayerId()));
			player = query.singleResult(QPlayer.player);
			if (player == null) {
				session.sendFlashMessage("no player loaded");
				return;
			}
		}
		long newCoins = player.getCoins() + value;
		{
			SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(QPlayer.player);
			update.where(QPlayer.player.id.eq(session.getPlayerId()));
			update.set(QPlayer.player.coins, newCoins);
			update.execute();
			
		}
		session.sendFlashMessage("You found some " + name + " (worth " + value + " coins).");
		
	}
	
	/**
	 * Prevent instantiation.
	 */
	private DigUtil() {
	}
	
}
