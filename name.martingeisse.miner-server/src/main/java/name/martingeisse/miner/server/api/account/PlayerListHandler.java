/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.api.account;

import java.util.List;

import name.martingeisse.api.request.ApiRequestCycle;
import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.common.javascript.jsonbuilder.JsonListBuilder;
import name.martingeisse.common.javascript.jsonbuilder.JsonObjectBuilder;
import name.martingeisse.common.javascript.jsonbuilder.JsonValueBuilder;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.webide.entity.Player;
import name.martingeisse.webide.entity.QPlayer;
import name.martingeisse.webide.entity.UserAccount;

import com.mysema.query.sql.SQLQuery;

/**
 * This handler returns the list of players for the user.
 */
public final class PlayerListHandler extends AbstractLoggedInHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.miner.server.api.account.AbstractLoggedInHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.common.javascript.analyze.JsonAnalyzer, name.martingeisse.common.javascript.jsonbuilder.JsonValueBuilder, name.martingeisse.webide.entity.UserAccount)
	 */
	@Override
	protected void handle(ApiRequestCycle requestCycle, JsonAnalyzer input, JsonValueBuilder<?> output, UserAccount userAccount) throws Exception {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		final QPlayer qp = QPlayer.player;
		final List<Player> players = query.from(qp).where(qp.userAccountId.eq(userAccount.getId()), qp.deleted.isFalse()).list(qp);
		JsonObjectBuilder<?> objectBuilder = output.object();
		JsonListBuilder<?> listBuilder = objectBuilder.property("players").list();
		for (Player player : players) {
			JsonObjectBuilder<?> playerBuilder = listBuilder.element().object();
			playerBuilder.property("id").number(player.getId());
			playerBuilder.property("name").string(player.getName());
			playerBuilder.property("faction").number(player.getFactionId());
			playerBuilder.end();
		}
		listBuilder.end();
		objectBuilder.end();
	}
	
}
