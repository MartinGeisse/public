/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.geometry;

/**
 * TODO angles measured in degrees or radians?
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

}
