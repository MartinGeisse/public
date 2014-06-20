/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackerspace.ingame.player;

/**
 * Represents another player whose data gets updated from the server.
 * Updating replaces the player proxy object, so the fields in this
 * class can be final.
 * 
 * TODO the "workingSet" reference in this class is currently always null.
 */
public final class PlayerProxy extends PlayerBase {

	/**
	 * the id
	 */
	private final int id;

	/**
	 * Constructor.
	 * @param id the session ID
	 */
	public PlayerProxy(final int id) {
		super(null);
		this.id = id;
	}

	/**
	 * Getter method for the id.
	 * @return the id
	 */
	public int getId() {
		return id;
	}

}
