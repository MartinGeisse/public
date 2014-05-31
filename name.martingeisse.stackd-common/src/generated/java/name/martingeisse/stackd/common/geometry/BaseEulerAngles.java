/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.geometry;

/**
 * TODO angles measured in degrees or radians?
 */
public abstract class BaseEulerAngles extends ReadableEulerAngles {


	/**
	 * the horizontalAngle
	 */
	public final double horizontalAngle;

	/**
	 * the verticalAngle
	 */
	public final double verticalAngle;

	/**
	 * the rollAngle
	 */
	public final double rollAngle;

	/**
	 * Constructor.
	 * @param horizontalAngle the horizontalAngle coordinate
	 * @param verticalAngle the verticalAngle coordinate
	 * @param rollAngle the rollAngle coordinate
	 */
	public BaseEulerAngles(double horizontalAngle, double verticalAngle, double rollAngle) {
		this.horizontalAngle = horizontalAngle;
		this.verticalAngle = verticalAngle;
		this.rollAngle = rollAngle;
	}

	/**
	 * Getter method for the horizontalAngle.
	 * @return the horizontalAngle
	 */
	@Override
	public double getHorizontalAngle() {
		return horizontalAngle;
	}

	/**
	 * Getter method for the verticalAngle.
	 * @return the verticalAngle
	 */
	@Override
	public double getVerticalAngle() {
		return verticalAngle;
	}

	/**
	 * Getter method for the rollAngle.
	 * @return the rollAngle
	 */
	@Override
	public double getRollAngle() {
		return rollAngle;
	}

}
