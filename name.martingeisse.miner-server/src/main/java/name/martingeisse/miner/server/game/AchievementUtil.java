/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.game;

import name.martingeisse.miner.server.MinerSession;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.webide.entity.Player;
import name.martingeisse.webide.entity.QPlayer;
import name.martingeisse.webide.entity.QPlayerAwardedAchievement;

import com.mysema.query.QueryException;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * Utility methods to deal with player's achievements.
 */
public class AchievementUtil {

	/**
	 * Awards an achievement to a player, but takes into account that the
	 * player might have been awarded the same achievement previously. Returns
	 * true if successfully awarded, false if the player already had been
	 * awarded the same achievement before.
	 * 
	 * This method is intended to be called when the prerequisites for the
	 * achievement have been asserted. If it returns false, the calling
	 * code should not take any further action. If it returns true, the
	 * achievement has been saved into the database (except if running in
	 * a database transaction; then it is of course only saved on commit).
	 * In that case, the calling function should next credit a reward to
	 * the player.
	 * 
	 * @param session the player's session
	 * @param achievementCode the internal unique code for the achievement
	 * @return true if successfully awarded, false if the player had already
	 * been awarded this achievement (or if no player was loaded)
	 */
	public static boolean awardAchievment(MinerSession session, String achievementCode) {
		if (session.getPlayerId() == null) {
			return false;
		}
		QPlayerAwardedAchievement qpaa = QPlayerAwardedAchievement.playerAwardedAchievement;
		SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(qpaa);
		insert.set(qpaa.playerId, session.getPlayerId());
		insert.set(qpaa.achievementCode, achievementCode);
		try {
			return (insert.execute() > 0);
		} catch (QueryException e) {
			return false;
		}
	}
	
	/**
	 * Like {@link #awardAchievment(MinerSession, String)}, but also sends a message to
	 * the client if the achievment was successfully awarded and assigns some bonus
	 * coins to the player.
	 * 
	 * @param session the player's session
	 * @param achievementCode the internal unique code for the achievement
	 * @param description a short description of the achievement, used in the message
	 * @param reward player's reward in coins
	 * @return true if successfully awarded, false if the player had already
	 * been awarded this achievement
	 */
	public static boolean awardAchievment(MinerSession session, String achievementCode, String description, int reward) {
		boolean success = awardAchievment(session, achievementCode);
		if (success) {
			session.sendFlashMessage("Achievement Unlocked: " + description + "(reward: " + reward + " coins)");
			Player player;
			{
				final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
				query.from(QPlayer.player).where(QPlayer.player.id.eq(session.getPlayerId()));
				player = query.singleResult(QPlayer.player);
			}
			if (player == null) {
				session.sendFlashMessage("no player loaded");
			} else {
				long newCoins = player.getCoins() + reward;
				SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(QPlayer.player);
				update.where(QPlayer.player.id.eq(session.getPlayerId()));
				update.set(QPlayer.player.coins, newCoins);
				update.execute();
				session.sendCoinsUpdate(newCoins);
			}
		}
		return success;
	}

	/**
	 * Prevent instantiation.
	 */
	private AchievementUtil() {
	}
	
}
