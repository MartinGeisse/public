/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.collision;

import name.martingeisse.stackd.common.geometry.RectangularRegion;

/**
 * A collider that represents any object that has axis-aligned features,
 * with detail coordinate precision.
 * 
 * The typical use for a collider is to represent world cubes, including
 * detailed features of all cube types.
 * 
 * The {@link #getCurrentCollider()} method of this object must return
 * this collider itself.
 */
public interface IAxisAlignedCollider extends IAxisAlignedCollidingObject {

	/**
	 * Checks for collisions, i.e. returns true if any part of this collider
	 * is in the specified region.
	 * 
	 * This method uses detail coordinates.
	 * 
	 * @param detailCoordinateRegion the region to check, expressed in detail coordinates
	 * @return true if there is any collision, false if not
	 */
	public boolean collides(RectangularRegion detailCoordinateRegion);
	
}
