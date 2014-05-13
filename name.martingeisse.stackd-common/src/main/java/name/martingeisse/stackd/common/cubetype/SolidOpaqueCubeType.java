/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.cubetype;

import java.util.Arrays;

import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;

/**
 * Cube type that is used for the typical "solid opaque" cube, like dirt, stone etc.
 * 
 * This type *cannot* be used for transparent cubes like glass!
 */
public class SolidOpaqueCubeType extends CubeType {

	/**
	 * the cubeFaceTextureIndices
	 */
	private final int[] cubeFaceTextureIndices;

	/**
	 * Constructor for cube types that use the same texture all over.
	 * @param cubeFaceTextureIndex the texture index to use for all six cube faces
	 */
	public SolidOpaqueCubeType(final int cubeFaceTextureIndex) {
		this(new int[] { cubeFaceTextureIndex, cubeFaceTextureIndex, cubeFaceTextureIndex, cubeFaceTextureIndex, cubeFaceTextureIndex, cubeFaceTextureIndex });
	}

	/**
	 * Constructor.
	 * @param cubeFaceTextureIndices the texture indices to use for the six faces, using
	 * the natural {@link AxisAlignedDirection} order.
	 */
	public SolidOpaqueCubeType(final int[] cubeFaceTextureIndices) {
		if (cubeFaceTextureIndices.length != 6) {
			throw new IllegalArgumentException("textureIndices must have length 6, has " + cubeFaceTextureIndices.length);
		}
		this.cubeFaceTextureIndices = Arrays.copyOf(cubeFaceTextureIndices, cubeFaceTextureIndices.length);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#obscuresNeighbor(name.martingeisse.stackd.common.geometry.AxisAlignedDirection)
	 */
	@Override
	public final boolean obscuresNeighbor(final AxisAlignedDirection direction) {
		return true;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#blocksMovementToNeighbor(name.martingeisse.stackd.common.geometry.AxisAlignedDirection)
	 */
	@Override
	public boolean blocksMovementToNeighbor(final AxisAlignedDirection direction) {
		return true;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#getCubeFaceTextureIndex(int)
	 */
	@Override
	public final int getCubeFaceTextureIndex(final int directionOrdinal) {
		return cubeFaceTextureIndices[directionOrdinal];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#collidesWithRegion(int, int, int, int, int, int)
	 */
	@Override
	public boolean collidesWithRegion(final int startX, final int startY, final int startZ, final int endX, final int endY, final int endZ) {
		return true;
	}

}
