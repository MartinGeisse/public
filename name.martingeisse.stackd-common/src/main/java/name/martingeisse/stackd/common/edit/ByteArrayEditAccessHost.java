/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.edit;

import name.martingeisse.stackd.common.geometry.RectangularRegion;

/**
 * {@link IEditAccessHost} implementation that stores the cubes in a byte array.
 * 
 * This class keeps a region that is used to clip cubes outside that region,
 * using the input coordinates to {@link #setCube(int, int, int, byte)}.
 * It is also reported by {@link #getRegion()}. {@link #getCube(int, int, int)}
 * returns 0 for cubes outside that region.
 * 
 * For cubes inside the region, this class subtracts origin coordinates from
 * the coordinate arguments. It then multiplies the coordinates by stride
 * values, and adds them together with a global index offset to obtain the
 * byte array index.
 */
public final class ByteArrayEditAccessHost implements IEditAccessHost {

	/**
	 * the region
	 */
	private final RectangularRegion region;

	/**
	 * the originX
	 */
	private final int originX;

	/**
	 * the originY
	 */
	private final int originY;

	/**
	 * the originZ
	 */
	private final int originZ;

	/**
	 * the strideX
	 */
	private final int strideX;

	/**
	 * the strideY
	 */
	private final int strideY;

	/**
	 * the strideZ
	 */
	private final int strideZ;

	/**
	 * the indexOffset
	 */
	private final int indexOffset;
	
	/**
	 * the array
	 */
	private final byte[] array;

	/**
	 * Constructor.
	 * @param region the region covered by this edit access host
	 * @param originX the x coordinate of the origin used for byte array index computation
	 * @param originY the y coordinate of the origin used for byte array index computation
	 * @param originZ the z coordinate of the origin used for byte array index computation
	 * @param strideX the array index stride for the x coordinate
	 * @param strideY the array index stride for the y coordinate
	 * @param strideZ the array index stride for the z coordinate
	 * @param indexOffset the starting index in the byte array
	 * @param array the byte array that stores cube data
	 */
	public ByteArrayEditAccessHost(final RectangularRegion region, final int originX, final int originY, final int originZ, final int strideX, final int strideY, final int strideZ, final int indexOffset, final byte[] array) {
		this.region = region;
		this.originX = originX;
		this.originY = originY;
		this.originZ = originZ;
		this.strideX = strideX;
		this.strideY = strideY;
		this.strideZ = strideZ;
		this.indexOffset = indexOffset;
		this.array = array;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.edit.IEditAccessHost#containsPosition(int, int, int)
	 */
	@Override
	public boolean containsPosition(final int x, final int y, final int z) {
		return getRegion().contains(x, y, z);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.edit.IEditAccessHost#getRegion()
	 */
	@Override
	public RectangularRegion getRegion() {
		return region;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.edit.IEditAccessHost#getCube(int, int, int)
	 */
	@Override
	public byte getCube(int x, int y, int z) {
		if (!containsPosition(x, y, z)) {
			return 0;
		}
		x -= originX;
		y -= originY;
		z -= originZ;
		int index = indexOffset + x * strideX + y * strideY + z * strideZ;
		return array[index];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.edit.IEditAccessHost#setCube(int, int, int, byte)
	 */
	@Override
	public void setCube(int x, int y, int z, byte value) {
		if (!containsPosition(x, y, z)) {
			return;
		}
		x -= originX;
		y -= originY;
		z -= originZ;
		int index = indexOffset + x * strideX + y * strideY + z * strideZ;
		array[index] = value;
	}

}
