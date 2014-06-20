/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackerspace.ingame.network;

import name.martingeisse.stackd.common.geometry.EulerAngles;
import name.martingeisse.stackd.common.geometry.Vector3d;

/**
 * Represents a message from the server indicating that the
 * player's state has been resumed.
 */
public final class PlayerResumedMessage {

	/**
	 * the position
	 */
	private final Vector3d position;

	/**
	 * the orientation
	 */
	private final EulerAngles orientation;

	/**
	 * Constructor.
	 * @param position the player's position
	 * @param orientation the player's orientation
	 */
	public PlayerResumedMessage(final Vector3d position, final EulerAngles orientation) {
		this.position = position;
		this.orientation = orientation;
	}

	/**
	 * Getter method for the position.
	 * @return the position
	 */
	public Vector3d getPosition() {
		return position;
	}
	
	/**
	 * Getter method for the orientation.
	 * @return the orientation
	 */
	public EulerAngles getOrientation() {
		return orientation;
	}
	
}
