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
public class MutableEulerAngles extends ReadableEulerAngles {


	/**
	 * the horizontalAngle
	 */
	public double horizontalAngle;

	/**
	 * the verticalAngle
	 */
	public double verticalAngle;

	/**
	 * the rollAngle
	 */
	public double rollAngle;

	/**
	 * Constructor.
	 * @param horizontalAngle the horizontalAngle coordinate
	 * @param verticalAngle the verticalAngle coordinate
	 * @param rollAngle the rollAngle coordinate
	 */
	public MutableEulerAngles(double horizontalAngle, double verticalAngle, double rollAngle) {
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
	 * Setter method for the horizontalAngle.
	 * @param horizontalAngle the horizontalAngle
	 */
	public void setHorizontalAngle(double horizontalAngle) {
		this.horizontalAngle = horizontalAngle;
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
	 * Setter method for the verticalAngle.
	 * @param verticalAngle the verticalAngle
	 */
	public void setVerticalAngle(double verticalAngle) {
		this.verticalAngle = verticalAngle;
	}

	/**
	 * Getter method for the rollAngle.
	 * @return the rollAngle
	 */
	@Override
	public double getRollAngle() {
		return rollAngle;
	}

	/**
	 * Setter method for the rollAngle.
	 * @param rollAngle the rollAngle
	 */
	public void setRollAngle(double rollAngle) {
		this.rollAngle = rollAngle;
	}


	/**
	 * Copies field values from the specified object.
	 * @param other the object to copy values from
	 */
	public void copyFrom(ReadableEulerAngles other) {
		horizontalAngle = other.getHorizontalAngle();
		verticalAngle = other.getVerticalAngle();
		rollAngle = other.getRollAngle();
	}

}
