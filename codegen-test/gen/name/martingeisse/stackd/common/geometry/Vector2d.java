/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.geometry;


/**
 *
 */
public final class Vector2d extends BaseVector2d {


	/**
	 * Constructor.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Vector2d(double x, double y) {
		super(x, y);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Vector2d) {
			return baseFieldsEqual((Vector2d)other);
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
		StringBuilder builder = new StringBuilder("{Vector2d ");
		buildBaseFieldsDescription(builder);
		builder.append('}');
		return builder.toString();
	}

}
