/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.cubetype;

import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;

/**
 * A "slab" cube type that fills the lower half of a cube.
 * 
 * This type *cannot* be used for transparent cubes like glass!
 */
public class SlabCubeType extends CubeType {

	/**
	 * the sideTextureIndex
	 */
	private final int sideTextureIndex;

	/**
	 * the bottomTextureIndex
	 */
	private final int bottomTextureIndex;

	/**
	 * the topTextureIndex
	 */
	private final int topTextureIndex;

	/**
	 * Constructor.
	 * @param sideTextureIndex the texture index to use for the sides
	 * @param topTextureIndex the texture index to use for the top
	 * @param bottomTextureIndex the texture index to use for the bottom
	 */
	public SlabCubeType(final int sideTextureIndex, final int bottomTextureIndex, final int topTextureIndex) {
		this.sideTextureIndex = sideTextureIndex;
		this.bottomTextureIndex = bottomTextureIndex;
		this.topTextureIndex = topTextureIndex;
	}

	/**
	 * Constructor.
	 * @param sideTextureIndex the texture index to use for the sides
	 * @param topAndBottomTextureIndex the texture index to use for the top and bottom
	 */
	public SlabCubeType(final int sideTextureIndex, final int topAndBottomTextureIndex) {
		this.sideTextureIndex = sideTextureIndex;
		this.bottomTextureIndex = topAndBottomTextureIndex;
		this.topTextureIndex = topAndBottomTextureIndex;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#obscuresNeighbor(name.martingeisse.stackd.common.geometry.AxisAlignedDirection)
	 */
	@Override
	public final boolean obscuresNeighbor(final AxisAlignedDirection direction) {
		return (direction == AxisAlignedDirection.NEGATIVE_Y);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#blocksMovementToNeighbor(name.martingeisse.stackd.common.geometry.AxisAlignedDirection)
	 */
	@Override
	public boolean blocksMovementToNeighbor(final AxisAlignedDirection direction) {
		return (direction == AxisAlignedDirection.NEGATIVE_Y);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#getCubeFaceTextureIndex(int)
	 */
	@Override
	public final int getCubeFaceTextureIndex(final int directionOrdinal) {
		if (directionOrdinal == AxisAlignedDirection.NEGATIVE_Y.ordinal()) {
			return bottomTextureIndex;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#buildInnerPolygons(name.martingeisse.stackd.common.cubes.MeshBuilderBase, int, int, int)
	 */
	@Override
	public void buildInnerPolygons(final MeshBuilderBase meshBuilder, final int baseX, final int baseY, final int baseZ) {
		meshBuilder.addAxisAlignedBox(baseX, baseY, baseZ, baseX + 8, baseY + 4, baseZ + 8, sideTextureIndex, sideTextureIndex, 0, topTextureIndex, sideTextureIndex, sideTextureIndex);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#collidesWithRegion(int, int, int, int, int, int)
	 */
	@Override
	public boolean collidesWithRegion(final int startX, final int startY, final int startZ, final int endX, final int endY, final int endZ) {
		return (startY < 4);
	}

}
