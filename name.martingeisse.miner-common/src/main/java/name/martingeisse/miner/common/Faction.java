/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.common;

/**
 * Represents the different factions in the game.
 */
public enum Faction {

	/**
	 * the THE_EMPIRE
	 */
	THE_EMPIRE("The Empire"),

	/**
	 * the THE_ORDER
	 */
	THE_ORDER("The Order"),

	/**
	 * the THE_BARBARIAN_CLANS
	 */
	THE_BARBARIAN_CLANS("The Barbarian Clans"),

	/**
	 * the THE_NOMADS
	 */
	THE_NOMADS("The Nomads");

	/**
	 * the displayName
	 */
	private final String displayName;

	/**
	 * Constructor.
	 * @param displayName the displayed name
	 */
	private Faction(final String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Getter method for the displayName.
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	
}
