/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.geometry;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

/**
 * A rectangular region of the world.
 */
public final class RectangularRegion {

	/**
	 * The maximal region.
	 */
	public static final RectangularRegion MAXIMAL = new RectangularRegion(MIN_VALUE, MIN_VALUE, MIN_VALUE, MAX_VALUE, MAX_VALUE, MAX_VALUE);

	/**
	 * An empty region that has each boundary set to the "empty" direction as much as possible.
	 * Useful as the starting point to compute the union of a set of regions.
	 */
	public static final RectangularRegion EMPTY_ANTIMAXIMAL = new RectangularRegion(MAX_VALUE, MAX_VALUE, MAX_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE);

	/**
	 * the startX
	 */
	private final int startX;

	/**
	 * the startY
	 */
	private final int startY;

	/**
	 * the startZ
	 */
	private final int startZ;

	/**
	 * the endX
	 */
	private final int endX;

	/**
	 * the endY
	 */
	private final int endY;

	/**
	 * the endZ
	 */
	private final int endZ;

	/**
	 * Constructor for a single cube at the specified position.
	 * @param x the x coordinate of the position
	 * @param y the y coordinate of the position
	 * @param z the z coordinate of the position
	 */
	public RectangularRegion(int x, int y, int z) {
		this.startX = x;
		this.startY = y;
		this.startZ = z;
		this.endX = x + 1;
		this.endY = y + 1;
		this.endZ = z + 1;
	}
	
	/**
	 * Constructor for a single cube at the specified position.
	 * 
	 * @param position the position
	 */
	public RectangularRegion(final Vector3i position) {
		this(position.getX(), position.getY(), position.getZ());
	}
	
	/**
	 * Constructor.
	 * @param startX the starting x coordinate of the region
	 * @param startY the starting y coordinate of the region
	 * @param startZ the starting z coordinate of the region
	 * @param endX the ending x coordinate of the region
	 * @param endY the ending y coordinate of the region
	 * @param endZ the ending z coordinate of the region
	 */
	public RectangularRegion(int startX, int startY, int startZ, int endX, int endY, int endZ) {
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.endX = endX;
		this.endY = endY;
		this.endZ = endZ;
	}

	/**
	 * Getter method for the startX.
	 * @return the startX
	 */
	public int getStartX() {
		return startX;
	}

	/**
	 * Getter method for the startY.
	 * @return the startY
	 */
	public int getStartY() {
		return startY;
	}

	/**
	 * Getter method for the startZ.
	 * @return the startZ
	 */
	public int getStartZ() {
		return startZ;
	}

	/**
	 * Getter method for the endX.
	 * @return the endX
	 */
	public int getEndX() {
		return endX;
	}

	/**
	 * Getter method for the endY.
	 * @return the endY
	 */
	public int getEndY() {
		return endY;
	}

	/**
	 * Getter method for the endZ.
	 * @return the endZ
	 */
	public int getEndZ() {
		return endZ;
	}
	
	/**
	 * Getter method for the sizeX.
	 * @return the sizeX
	 */
	public int getSizeX() {
		return endX - startX;
	}
	
	/**
	 * Getter method for the sizeY.
	 * @return the sizeY
	 */
	public int getSizeY() {
		return endY - startY;
	}
	
	/**
	 * Getter method for the sizeZ.
	 * @return the sizeZ
	 */
	public int getSizeZ() {
		return endZ - startZ;
	}
	
	/**
	 * Extends this region by enlarging it until the dimensions of the specified anchor are reached.
	 * For each one of the six axial directions, a flag specifies whether to extend into that direction
	 * or to leave that side alone. Specifically, for each direction specified, the new
	 * coordinate value is the min (for lower-coordinate sides) or max (for higher-coordinate sides)
	 * of the coordinate value of this region and the anchor region.
	 * 
	 * Returns the extended region as a new {@link RectangularRegion} instance.
	 * 
	 * Note: Calling this method with all flags set to true is equivalent
	 * to {@link #getUnion(RectangularRegion)}.
	 * 
	 * @param anchor the anchor to extend to
	 * @param negativeX whether to extend towards the negative X direction
	 * @param positiveX whether to extend towards the positive X direction
	 * @param negativeY whether to extend towards the negative Y direction
	 * @param positiveY whether to extend towards the positive Y direction
	 * @param negativeZ whether to extend towards the negative Z direction
	 * @param positiveZ whether to extend towards the positive Z direction
	 * @return the extended region
	 */
	public RectangularRegion getExtended(RectangularRegion anchor, boolean negativeX, boolean positiveX, boolean negativeY, boolean positiveY, boolean negativeZ, boolean positiveZ) {
		int resultStartX = negativeX ? (startX < anchor.startX ? startX : anchor.startX) : startX;
		int resultStartY = negativeY ? (startY < anchor.startY ? startY : anchor.startY) : startY;
		int resultStartZ = negativeZ ? (startZ < anchor.startZ ? startZ : anchor.startZ) : startZ;
		int resultEndX = positiveX ? (endX > anchor.endX ? endX : anchor.endX) : endX;
		int resultEndY = positiveY ? (endY > anchor.endY ? endY : anchor.endY) : endY;
		int resultEndZ = positiveZ ? (endZ > anchor.endZ ? endZ : anchor.endZ) : endZ;
		return new RectangularRegion(resultStartX, resultStartY, resultStartZ, resultEndX, resultEndY, resultEndZ);
	}

	/**
	 * Sets this region to the minimal region that contains the both this
	 * region and the specified other region.
	 * @param other the region to union with
	 * @return the union
	 */
	public RectangularRegion getUnion(RectangularRegion other) {
		int resultStartX = (startX < other.startX ? startX : other.startX);
		int resultStartY = (startY < other.startY ? startY : other.startY);
		int resultStartZ = (startZ < other.startZ ? startZ : other.startZ);
		int resultEndX = (endX > other.endX ? endX : other.endX);
		int resultEndY = (endY > other.endY ? endY : other.endY);
		int resultEndZ = (endZ > other.endZ ? endZ : other.endZ);
		return new RectangularRegion(resultStartX, resultStartY, resultStartZ, resultEndX, resultEndY, resultEndZ);
	}
	
	/**
	 * Sets this region to the intersection of this region and the specified other region.
	 * @param other the region to intersect with
	 * @return the intersection
	 */
	public RectangularRegion getIntersection(RectangularRegion other) {
		int resultStartX = (startX > other.startX ? startX : other.startX);
		int resultStartY = (startY > other.startY ? startY : other.startY);
		int resultStartZ = (startZ > other.startZ ? startZ : other.startZ);
		int resultEndX = (endX < other.endX ? endX : other.endX);
		int resultEndY = (endY < other.endY ? endY : other.endY);
		int resultEndZ = (endZ < other.endZ ? endZ : other.endZ);
		return new RectangularRegion(resultStartX, resultStartY, resultStartZ, resultEndX, resultEndY, resultEndZ);
	}

	/**
	 * Checks whether this region is empty. This is the case whenever the end value
	 * isn't greater than the start value in any one direction.
	 * @return whether this region is empty
	 */
	public boolean isEmpty() {
		return (startX >= endX) || (startY >= endY) || (startZ >= endZ);
	}
	
	/**
	 * Checks whether the specified cell is within this region.
	 * @param x the x position of the cell
	 * @param y the y position of the cell
	 * @param z the z position of the cell
	 * @return true if the cell is within this region, false if not
	 */
	public boolean contains(int x, int y, int z) {
		return (x >= startX && x < endX && y >= startY && y < endY && z >= startZ && z < endZ);
	}

	/**
	 * Checks whether the specified cell is within this region.
	 * @param position the position of the cell
	 * @return true if the cell is within this region, false if not
	 */
	public boolean contains(final Vector3i position) {
		return contains(position.getX(), position.getY(), position.getZ());
	}

	/**
	 * Multiplies the coordinates of this region by the specified cluster size.
	 * 
	 * @param clusterSize the cluster size
	 * @return the resulting region
	 */
	public RectangularRegion multiply(ClusterSize clusterSize) {
		int shift = clusterSize.getShiftBits();
		int csx = (startX << shift);
		int csy = (startY << shift);
		int csz = (startZ << shift);
		int cex = (endX << shift);
		int cey = (endY << shift);
		int cez = (endZ << shift);
		return new RectangularRegion(csx, csy, csz, cex, cey, cez);
	}

	/**
	 * Divides the coordinates of this region by the specified cluster size,
	 * rounding towards a larger result in all directions.
	 * 
	 * @param clusterSize the cluster size
	 * @return the resulting region
	 */
	public RectangularRegion divideAndRoundToOuter(ClusterSize clusterSize) {
		int shift = clusterSize.getShiftBits();
		int mask = clusterSize.getMask();
		int csx = (startX >> shift);
		int csy = (startY >> shift);
		int csz = (startZ >> shift);
		int cex = ((endX + mask) >> shift);
		int cey = ((endY + mask) >> shift);
		int cez = ((endZ + mask) >> shift);
		return new RectangularRegion(csx, csy, csz, cex, cey, cez);
	}

	/**
	 * Divides the coordinates of this region by the specified cluster size,
	 * rounding towards a smaller result in all directions. This may result
	 * in an empty region.
	 * 
	 * @param clusterSize the cluster size
	 * @return the resulting region
	 */
	public RectangularRegion divideAndRoundToInner(ClusterSize clusterSize) {
		int shift = clusterSize.getShiftBits();
		int mask = clusterSize.getMask();
		int csx = ((startX + mask) >> shift);
		int csy = ((startY + mask) >> shift);
		int csz = ((startZ + mask) >> shift);
		int cex = (endX >> shift);
		int cey = (endY >> shift);
		int cez = (endZ >> shift);
		return new RectangularRegion(csx, csy, csz, cex, cey, cez);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "subregion(" + startX + ", " + startY + ", " + startZ + " -- " + endX + ", " + endY + ", " + endZ + ")";
	}
	
}
