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
public final class EulerAngles extends BaseEulerAngles {


	/**
	 * @param horizontalAngle the horizontalAngle coordinate
	 * @param verticalAngle the verticalAngle coordinate
	 * @param rollAngle the rollAngle coordinate
	 */
	public EulerAngles(double horizontalAngle, double verticalAngle, double rollAngle) {
		super(horizontalAngle, verticalAngle, rollAngle);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof EulerAngles) {
			return baseFieldsEqual((EulerAngles)other);
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return baseFieldsHashCode();
	}

	/* (non-Javadoc)
	 * @@see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{EulerAngles ");
		buildBaseFieldsDescription(builder);
		builder.append('}');
		return builder.toString();
	}

}
