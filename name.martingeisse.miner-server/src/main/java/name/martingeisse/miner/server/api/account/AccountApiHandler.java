/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.api.account;

import name.martingeisse.api.handler.misc.NamedResourceFolderHandler;

/**
 * The main application handler.
 */
public final class AccountApiHandler extends NamedResourceFolderHandler {

	/**
	 * Constructor.
	 */
	public AccountApiHandler() {
		getResources().put("login", new LoginHandler());
		getResources().put("getPlayers", new PlayerListHandler());
		getResources().put("createPlayer", new CreatePlayerHandler());
		getResources().put("getPlayerDetails", new PlayerDetailsHandler());
		getResources().put("accessPlayer", new AccessPlayerHandler());
		getResources().put("deletePlayer", new DeletePlayerHandler());
	}
	
}
