/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.geometry;

/**
 *
 */
public class MutableVector3i extends ReadableVector3i {


	/**
	 * the x
	 */
	public int x;

	/**
	 * the y
	 */
	public int y;

	/**
	 * the z
	 */
	public int z;

	/**
	 * Constructor.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public MutableVector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Getter method for the x.
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Setter method for the x.
	 * @param x the x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Getter method for the y.
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Setter method for the y.
	 * @param y the y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Getter method for the z.
	 * @return the z
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Setter method for the z.
	 * @param z the z
	 */
	public void setZ(int z) {
		this.z = z;
	}

}
