/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.collision;

/**
 * Implemented by objects that have an {@link IAxisAlignedCollider}. The
 * main purpose of this interface is to allow access to the
 * *current* collider of an object that might change colliders.
 */
public interface IAxisAlignedCollidingObject {

	/**
	 * Returns the currently used collider.
	 * @return the collider
	 */
	public IAxisAlignedCollider getCurrentCollider();
	
}
