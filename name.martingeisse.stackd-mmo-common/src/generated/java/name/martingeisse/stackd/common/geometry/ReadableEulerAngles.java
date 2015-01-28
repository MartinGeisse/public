/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.geometry;


/**
 * Euler angles (actually, nautical angles), expressed as a horizontal angle (yaw),
 * vertical angle (pitch) and roll angle. All angles are expressed in degrees, not radians.
 */
public abstract class ReadableEulerAngles {



	/**
	 * Getter method for the horizontalAngle.
	 * @return the horizontalAngle
	 */
	public abstract double getHorizontalAngle();

	/**
	 * Getter method for the verticalAngle.
	 * @return the verticalAngle
	 */
	public abstract double getVerticalAngle();

	/**
	 * Getter method for the rollAngle.
	 * @return the rollAngle
	 */
	public abstract double getRollAngle();


}
