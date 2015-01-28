/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.collision;

import name.martingeisse.stackd.common.geometry.RectangularRegion;

/**
 * This collider never reports a collision.
 */
public class EmptyCollider implements IAxisAlignedCollider {

	/**
	 * shared instance of this class
	 */
	public static final EmptyCollider instance = new EmptyCollider();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.collision.IAxisAlignedCollidingObject#getCurrentCollider()
	 */
	@Override
	public IAxisAlignedCollider getCurrentCollider() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.collision.IAxisAlignedCollider#collides(name.martingeisse.stackd.common.geometry.RectangularRegion)
	 */
	@Override
	public boolean collides(RectangularRegion region) {
		return false;
	}
	
}
