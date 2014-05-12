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
public class AccountApiHandler extends NamedResourceFolderHandler {

	/**
	 * Constructor.
	 */
	public AccountApiHandler() {
		getResources().put("login", new LoginHandler());
		getResources().put("players", new PlayerListHandler());
	}
	
}
