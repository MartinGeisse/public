/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.geometry;

/**
 *
 */
public abstract class BaseVector3i extends ReadableVector3i {


	/**
	 * the x
	 */
	public final int x;

	/**
	 * the y
	 */
	public final int y;

	/**
	 * the z
	 */
	public final int z;

	/**
	 * Constructor.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public BaseVector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Getter method for the x.
	 * @return the x
	 */
	@Override
	public int getX() {
		return x;
	}

	/**
	 * Getter method for the y.
	 * @return the y
	 */
	@Override
	public int getY() {
		return y;
	}

	/**
	 * Getter method for the z.
	 * @return the z
	 */
	@Override
	public int getZ() {
		return z;
	}

}
