/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.api.account;

import java.math.BigDecimal;
import name.martingeisse.api.handler.jsonapi.JsonApiException;
import name.martingeisse.api.request.ApiRequestCycle;
import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.common.javascript.jsonbuilder.JsonObjectBuilder;
import name.martingeisse.common.javascript.jsonbuilder.JsonValueBuilder;
import name.martingeisse.miner.common.Faction;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.webide.entity.QPlayer;
import name.martingeisse.webide.entity.UserAccount;
import com.mysema.query.sql.dml.SQLInsertClause;

/**
 * This handler creates a player character for the logged-in user.
 */
public final class CreatePlayerHandler extends AbstractLoggedInHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.miner.server.api.account.AbstractLoggedInHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.common.javascript.analyze.JsonAnalyzer, name.martingeisse.common.javascript.jsonbuilder.JsonValueBuilder, name.martingeisse.webide.entity.UserAccount)
	 */
	@Override
	protected void handle(ApiRequestCycle requestCycle, JsonAnalyzer input, JsonValueBuilder<?> output, UserAccount userAccount) throws Exception {
		
		// analyze request data
		int factionIndex = input.analyzeMapElement("faction").expectInteger();
		Faction[] factions = Faction.values();
		if (factionIndex < 0 || factionIndex >= factions.length) {
			throw new JsonApiException(1, "invalid faction index: " + factionIndex);
		}
		String name = input.analyzeMapElement("name").expectString();
		if (name.isEmpty()) {
			throw new JsonApiException(1, "empty character name not allowed");
		}
		
		// create the player character
		final QPlayer qp = QPlayer.player;
		final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(qp);
		insert.set(QPlayer.player.userAccountId, userAccount.getId());
		insert.set(QPlayer.player.coins, 0L);
		insert.set(QPlayer.player.name, name);
		insert.set(QPlayer.player.factionId, (long)factionIndex);
		insert.set(QPlayer.player.x, BigDecimal.ZERO);
		insert.set(QPlayer.player.y, BigDecimal.ONE.add(BigDecimal.ONE));
		insert.set(QPlayer.player.z, BigDecimal.ZERO);
		insert.set(QPlayer.player.leftAngle, BigDecimal.ZERO);
		insert.set(QPlayer.player.upAngle, BigDecimal.ZERO);
		long playerId = insert.executeWithKey(Long.class);

		// build the response
		JsonObjectBuilder<?> objectBuilder = output.object();
		objectBuilder.property("id").number(playerId);
		objectBuilder.end();
		
	}
	
}
