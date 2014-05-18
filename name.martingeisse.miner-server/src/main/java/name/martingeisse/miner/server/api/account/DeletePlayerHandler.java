/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.api.account;

import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.common.javascript.jsonbuilder.JsonValueBuilder;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.webide.entity.Player;
import name.martingeisse.webide.entity.QPlayer;
import name.martingeisse.webide.entity.UserAccount;
import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * This handler soft-deletes a player.
 */
public final class DeletePlayerHandler extends AbstractLoggedInHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.miner.server.api.account.AbstractLoggedInHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.common.javascript.analyze.JsonAnalyzer, name.martingeisse.common.javascript.jsonbuilder.JsonValueBuilder, name.martingeisse.webide.entity.UserAccount)
	 */
	@Override
	protected void handle(RequestCycle requestCycle, JsonAnalyzer input, JsonValueBuilder<?> output, UserAccount userAccount) throws Exception {
		Player player = AccountApiUtil.fetchPlayer(input, userAccount);
		QPlayer qp = QPlayer.player;
		SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(qp);
		update.where(qp.id.eq(player.getId())).set(qp.deleted, true).execute();
		output.object().end();
	}
	
}
