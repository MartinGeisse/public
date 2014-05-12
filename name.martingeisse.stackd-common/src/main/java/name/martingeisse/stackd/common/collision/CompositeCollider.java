/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.collision;

import java.util.ArrayList;
import java.util.Collection;
import name.martingeisse.stackd.common.geometry.RectangularRegion;


/**
 * This class is used for collider hierarchies. It stores a collection of
 * objects, each of which has a collider. This collider reports a collision if
 * at least one of the contained colliders does.
 * 
 * This class does not store the colliders directly, but rather a collection
 * of {@link IAxisAlignedCollidingObject}s. This allows the contained objects
 * to switch colliders without changing the collection stored in this class.
 * 
 * The collection stored in this class can be shared with other objects if needed.
 */
public final class CompositeCollider implements IAxisAlignedCollider {

	/**
	 * the collidingObjects
	 */
	private final Collection<? extends IAxisAlignedCollidingObject> collidingObjects;
	
	/**
	 * Constructor. This constructor creates an internal list of colliders.
	 */
	public CompositeCollider() {
		this(new ArrayList<IAxisAlignedCollidingObject>());
	}
	
	/**
	 * Constructor. This constructor shares a collection of colliders with other
	 * objects.
	 * 
	 * @param collidingObjects the collection of colliding objects
	 */
	public CompositeCollider(Collection<? extends IAxisAlignedCollidingObject> collidingObjects) {
		this.collidingObjects = collidingObjects;
	}
	
	/**
	 * Getter method for the collidingObjects.
	 * @return the collidingObjects
	 */
	public Collection<? extends IAxisAlignedCollidingObject> getCollidingObjects() {
		return collidingObjects;
	}

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
		for (IAxisAlignedCollidingObject collidingObject : collidingObjects) {
			if (collidingObject.getCurrentCollider().collides(region)) {
				return true;
			}
		}
		return false;
	}
	
}
