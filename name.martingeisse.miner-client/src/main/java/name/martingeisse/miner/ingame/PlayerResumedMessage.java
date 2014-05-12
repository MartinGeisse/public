/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.ingame;

/**
 * Represents a message from the server indicating that the
 * player's state has been resumed.
 */
public final class PlayerResumedMessage {

	/**
	 * the x
	 */
	private final double x;

	/**
	 * the y
	 */
	private final double y;

	/**
	 * the z
	 */
	private final double z;

	/**
	 * Constructor.
	 * @param x the x position of the player
	 * @param y the y position of the player
	 * @param z the z position of the player
	 */
	public PlayerResumedMessage(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Getter method for the x.
	 * @return the x
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Getter method for the y.
	 * @return the y
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Getter method for the z.
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

}
