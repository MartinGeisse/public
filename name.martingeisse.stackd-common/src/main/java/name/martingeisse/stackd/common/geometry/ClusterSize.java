/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.geometry;

/**
 * Specifies the size of a cluster. Clusters must have a
 * power-of-two size (represented by this class), the same
 * size along each axis, and a position aligned to that size.
 */
public final class ClusterSize implements Comparable<ClusterSize> {

	/**
	 * the shiftBits
	 */
	private final int shiftBits;

	/**
	 * Constructor.
	 * @param shiftBits the log2 of the cluster size along each axis
	 */
	public ClusterSize(final int shiftBits) {
		if (shiftBits < 0) {
			throw new IllegalArgumentException("cluster shift bits cannot be negative: " + shiftBits);
		}
		this.shiftBits = shiftBits;
	}

	/**
	 * Getter method for the shiftBits.
	 * @return the shiftBits
	 */
	public int getShiftBits() {
		return shiftBits;
	}

	/**
	 * Getter method for the size.
	 * @return the size
	 */
	public int getSize() {
		return (1 << shiftBits);
	}
	
	/**
	 * Getter method for the squared size.
	 * @return the squared size
	 */
	public int getSquaredSize() {
		return ((1 << shiftBits) << shiftBits);
	}

	/**
	 * Getter method for the mask.
	 * @return the mask
	 */
	public int getMask() {
		return (1 << shiftBits) - 1;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (o instanceof ClusterSize) {
			final ClusterSize other = (ClusterSize)o;
			return shiftBits == other.shiftBits;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return shiftBits;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final ClusterSize other) {
		return shiftBits - other.shiftBits;
	}

	/**
	 * Returns the maximum of this size and the specified other size.
	 * @param other the other size
	 * @return the maximum
	 */
	public ClusterSize max(final ClusterSize other) {
		return compareTo(other) > 0 ? this : other;
	}

	/**
	 * Returns the minimum of this size and the specified other size.
	 * @param other the other size
	 * @return the minimum
	 */
	public ClusterSize min(final ClusterSize other) {
		return compareTo(other) < 0 ? this : other;
	}

	/**
	 * Returns the product of this size and the specified other size.
	 * @param other the other size
	 * @return the product
	 */
	public ClusterSize multiply(final ClusterSize other) {
		return new ClusterSize(shiftBits + other.shiftBits);
	}

	/**
	 * Returns the quotient of this size and the specified other size.
	 * It is an error if the result is fractional, i.e. if the other
	 * size is greater than this size.
	 * 
	 * @param other the other size
	 * @return the quotient
	 */
	public ClusterSize divide(final ClusterSize other) {
		return new ClusterSize(shiftBits - other.shiftBits);
	}

	/**
	 * Returns the number of cells in a cluster of this size. The cluster
	 * has an edge length of (size), so it has (size^3) cells.
	 * @return the number of cells
	 */
	public int getCellCount() {
		return (1 << (3 * shiftBits));
	}

	/**
	 * Returns the largest cluster size that is contained by the specified
	 * number of units.
	 * 
	 * @param units the number of units
	 * @return the inner cluster size
	 */
	public static ClusterSize getInner(int units) {
		if (units < 1) {
			throw new IllegalAccessError("cannot generate ClusterSize from less than one unit: " + units);
		}
		int shift = 0;
		while (units > 1) {
			units >>= 1;
			shift++;
		}
		return new ClusterSize(shift);
	}

	/**
	 * Returns the smallest cluster size that contains the specified
	 * size (number of units).
	 * 
	 * @param size the number of units
	 * @return the outer cluster size
	 */
	public static ClusterSize getOuter(final int size) {
		return getInner((size << 1) - 1);
	}

	/**
	 * Checks if the specified cell position is at the border of a cluster of this
	 * cluster size. Returns an array containing the directions towards neighbor
	 * clusters. The returned array will contain zero directions for inner cells,
	 * one direction for "face" cells, two directions for "edge" cells and three
	 * directions for "corner" cells.
	 * 
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 * @param z the z coordinate of the cell
	 * @return the directions toward neighbor clusters
	 */
	public AxisAlignedDirection[] getBorderDirections(final int x, final int y, final int z) {

		// special case
		if (shiftBits == 0) {
			return new AxisAlignedDirection[] {
				AxisAlignedDirection.NEGATIVE_X, AxisAlignedDirection.POSITIVE_X, AxisAlignedDirection.NEGATIVE_Y, AxisAlignedDirection.POSITIVE_Y, AxisAlignedDirection.NEGATIVE_Z,
				AxisAlignedDirection.POSITIVE_Z,
			};
		}

		// normal case
		final int mask = getMask();
		final AxisAlignedDirection xDirection = ((x & mask) == 0 ? AxisAlignedDirection.NEGATIVE_X : ((x + 1) & mask) == 0 ? AxisAlignedDirection.POSITIVE_X : null);
		final AxisAlignedDirection yDirection = ((y & mask) == 0 ? AxisAlignedDirection.NEGATIVE_Y : ((y + 1) & mask) == 0 ? AxisAlignedDirection.POSITIVE_Y : null);
		final AxisAlignedDirection zDirection = ((z & mask) == 0 ? AxisAlignedDirection.NEGATIVE_Z : ((z + 1) & mask) == 0 ? AxisAlignedDirection.POSITIVE_Z : null);
		final int count = (xDirection == null ? 0 : 1) + (yDirection == null ? 0 : 1) + (zDirection == null ? 0 : 1);
		final AxisAlignedDirection[] result = new AxisAlignedDirection[count];
		int i = 0;
		if (xDirection != null) {
			result[i] = xDirection;
			i++;
		}
		if (yDirection != null) {
			result[i] = yDirection;
			i++;
		}
		if (zDirection != null) {
			result[i] = zDirection;
			i++;
		}
		return result;

	}

	/**
	 * See {@link #getBorderDirections(int, int, int)}.
	 * 
	 * @param v the vector that contains the (x, y, z) coordinates.
	 * @return the directions toward neighbor clusters
	 */
	public AxisAlignedDirection[] getBorderDirections(final Vector3i v) {
		return getBorderDirections(v.getX(), v.getY(), v.getZ());
	}

}
