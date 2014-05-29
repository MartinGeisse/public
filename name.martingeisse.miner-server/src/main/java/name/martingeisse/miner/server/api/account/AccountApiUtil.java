/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.api.account;

import name.martingeisse.api.handler.jsonapi.JsonApiException;
import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.common.security.SecurityTokenUtil;
import name.martingeisse.miner.server.MinerServerSecurityConstants;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.webide.entity.Player;
import name.martingeisse.webide.entity.QPlayer;
import name.martingeisse.webide.entity.UserAccount;
import org.joda.time.Instant;
import com.mysema.query.sql.SQLQuery;

/**
 * Common helper methods for the account API.
 */
final class AccountApiUtil {

	/**
	 * Builds a player access token for the player with the specified ID.
	 * 
	 * @param playerId the player's ID
	 * @return the token
	 */
	public static String createPlayerAccessToken(long playerId) {
		return createPlayerAccessToken(Long.toString(playerId));
	}
	
	/**
	 * Builds a player access token for the player with the specified ID (already converted
	 * to a string).
	 * 
	 * @param playerIdText the player's ID, converted to a string
	 * @return the token
	 */
	public static String createPlayerAccessToken(String playerIdText) {
		Instant expiryTime = new Instant().plus(MinerServerSecurityConstants.PLAYER_ACCESS_TOKEN_MAX_AGE_MILLISECONDS);
		return SecurityTokenUtil.createToken(playerIdText, expiryTime, MinerServerSecurityConstants.SECURITY_TOKEN_SECRET);
	}
	
	/**
	 * Fetches one of the user's players. The player ID is specified in the field "playerId"
	 * in the reuqest data.
	 * 
	 * This method throws a {@link JsonApiException} if the player could not be fetched or
	 * if the input data has no "playerId" field.
	 * 
	 * @param input the input request data
	 * @param userAccount the user's account
	 * @return the player
	 */
	public static Player fetchPlayer(JsonAnalyzer input, UserAccount userAccount) {
		final long playerId = input.analyzeMapElement("playerId").expectLong();
		final QPlayer qp = QPlayer.player;
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		final Player player = query.from(qp).where(qp.id.eq(playerId), qp.userAccountId.eq(userAccount.getId()), qp.deleted.isFalse()).singleResult(qp);
		if (player == null) {
			throw new JsonApiException(1, "player not found");
		}
		return player;
	}
	
	
}
