/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.ingame;

/**
 * Represents another player whose data gets updated from the server.
 * Updating replaces the player proxy object, so the fields in this
 * class can be final.
 */
public class PlayerProxy {

	/**
	 * the id
	 */
	private final int id;

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
	 * @param id the session ID
	 * @param x the x position of the player
	 * @param y the y position of the player
	 * @param z the z position of the player
	 */
	public PlayerProxy(final int id, final double x, final double y, final double z) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Getter method for the id.
	 * @return the id
	 */
	public int getId() {
		return id;
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
