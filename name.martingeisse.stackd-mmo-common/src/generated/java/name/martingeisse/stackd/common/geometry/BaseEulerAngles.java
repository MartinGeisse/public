/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.geometry;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Euler angles (actually, nautical angles), expressed as a horizontal angle (yaw),
 * vertical angle (pitch) and roll angle. All angles are expressed in degrees, not radians.
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


	/**
	 * Helper method to implement {@link #equals(Object)}. Checks if all data fields in the base data class are equal.
	 * @param other the other object to compare to
	 * @return true if the fields are equal, false if not
	 */
	protected final boolean baseFieldsEqual(BaseEulerAngles other) {
		return (horizontalAngle == other.horizontalAngle && verticalAngle == other.verticalAngle && rollAngle == other.rollAngle);
	}

	/**
	 * Helper method to implement {@link #hashCode()}. Produces a hash code from the fields in the base data class.
	 * @return the hash code
	 */
	protected final int baseFieldsHashCode() {
		return new HashCodeBuilder().append(horizontalAngle).append(verticalAngle).append(rollAngle).toHashCode();
	}

	/**
	 * Helper method to implement {@link #toString()}. Writes a description string from the fields in the base data class.
	 * to the specified StringBuilder.
	 * @param builder the string builder
	 */
	protected final void buildBaseFieldsDescription(StringBuilder builder) {
		builder.append("horizontalAngle = ").append(horizontalAngle);
		builder.append(", verticalAngle = ").append(verticalAngle);
		builder.append(", rollAngle = ").append(rollAngle); 
	}

}
