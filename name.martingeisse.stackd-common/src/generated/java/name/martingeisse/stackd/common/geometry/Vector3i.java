/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.geometry;


/**
 *
 */
public final class Vector3i extends BaseVector3i {


	/**
	 * Constructor.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public Vector3i(int x, int y, int z) {
		super(x, y, z);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Vector3i) {
			return baseFieldsEqual((Vector3i)other);
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
		StringBuilder builder = new StringBuilder("{Vector3i ");
		buildBaseFieldsDescription(builder);
		builder.append('}');
		return builder.toString();
	}

}
