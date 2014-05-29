/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.geometry;

/**
 *
 */
public class MutableVector3d extends ReadableVector3d {


	/**
	 * the x
	 */
	public double x;

	/**
	 * the y
	 */
	public double y;

	/**
	 * the z
	 */
	public double z;

	/**
	 * Constructor.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public MutableVector3d(double x, double y, double z) {
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
	 * Setter method for the x.
	 * @param x the x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Getter method for the y.
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Setter method for the y.
	 * @param y the y
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Getter method for the z.
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Setter method for the z.
	 * @param z the z
	 */
	public void setZ(double z) {
		this.z = z;
	}

}
