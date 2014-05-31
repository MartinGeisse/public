/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.geometry;


/**
 *
 */
public class MutableVector2d extends ReadableVector2d {


	/**
	 * the x
	 */
	public double x;

	/**
	 * the y
	 */
	public double y;

	/**
	 * Constructor.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public MutableVector2d(double x, double y) {
		this.x = x;
		this.y = y;
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
	@Override
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
	 * Copies field values from the specified object.
	 * @param other the object to copy values from
	 */
	public void copyFrom(ReadableVector2d other) {
		x = other.getX();
		y = other.getY();
	}

}
