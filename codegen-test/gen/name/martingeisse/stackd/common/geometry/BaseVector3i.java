/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.geometry;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 */
public abstract class BaseVector3i extends ReadableVector3i {


	/**
	 * the x
	 */
	public final int x;

	/**
	 * the y
	 */
	public final int y;

	/**
	 * the z
	 */
	public final int z;

	/**
	 * Constructor.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public BaseVector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Getter method for the x.
	 * @return the x
	 */
	@Override
	public int getX() {
		return x;
	}

	/**
	 * Getter method for the y.
	 * @return the y
	 */
	@Override
	public int getY() {
		return y;
	}

	/**
	 * Getter method for the z.
	 * @return the z
	 */
	@Override
	public int getZ() {
		return z;
	}


	/**
	 * Helper method to implement {@link #equals(Object)}. Checks if all data fields in the base data class are equal.
	 * @param other the other object to compare to
	 * @return true if the fields are equal, false if not
	 */
	protected final boolean baseFieldsEqual(BaseVector3i other) {
		return (x == other.x && y == other.y && z == other.z);
	}

	/**
	 * Helper method to implement {@link #hashCode()}. Produces a hash code from the fields in the base data class.
	 * @return the hash code
	 */
	protected final int baseFieldsHashCode() {
		return new HashCodeBuilder().append(x).append(y).append(z).toHashCode();
	}

	/**
	 * Helper method to implement {@link #toString()}. Writes a description string from the fields in the base data class.
	 * to the specified StringBuilder.
	 * @param builder the string builder
	 */
	protected final void buildBaseFieldsDescription(StringBuilder builder) {
		builder.append("x = ").append(x);
		builder.append(", y = ").append(y);
		builder.append(", z = ").append(z);
	}

}
