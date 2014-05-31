/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.geometry;

/**
 *
 */
public abstract class BaseVector3d extends ReadableVector3d {


	/**
	 * the x
	 */
	public final double x;

	/**
	 * the y
	 */
	public final double y;

	/**
	 * the z
	 */
	public final double z;

	/**
	 * Constructor.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public BaseVector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Getter method for the x.
	 * @return the x
	 */
	@Override
	public double getX() {
		return x;
	}

	/**
	 * Getter method for the y.
	 * @return the y
	 */
	@Override
	public double getY() {
		return y;
	}

	/**
	 * Getter method for the z.
	 * @return the z
	 */
	@Override
	public double getZ() {
		return z;
	}

}
