/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.ingame.gui;

import name.martingeisse.stackd.client.frame.handlers.HandlerList;

/**
 * The handler for the in-game menu that runs in parallel to the game.
 */
public class IngameMenuHandler extends HandlerList {

	/**
	 * Loads the in-game menu GUI into Nifty.
	 */
	public static void load() {
		// NiftyHandler.getInstance().fromXml(IngameMenuHandler.class);
	}
	
	/**
	 * Constructor.
	 */
	public IngameMenuHandler() {
		// add(NiftyHandler.getInstance());
	}
	
}
