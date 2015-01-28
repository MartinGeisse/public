/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.cubetype;

import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;

/**
 * A "stairs" cube type that fills 3/4 of a cube.
 * 
 * This type *cannot* be used for transparent cubes like glass!
 */
public class StairsCubeType extends CubeType {

	/**
	 * the upstairsDirection
	 */
	private final AxisAlignedDirection upstairsDirection;

	/**
	 * the frontTextureIndex
	 */
	private final int frontTextureIndex;

	/**
	 * the backTextureIndex
	 */
	private final int backTextureIndex;

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
	 * @param upstairsDirection the direction (along x or z axis) to go upstairs
	 * @param frontTextureIndex texture index
	 * @param backTextureIndex texture index
	 * @param sideTextureIndex texture index
	 * @param bottomTextureIndex texture index
	 * @param topTextureIndex texture index
	 */
	public StairsCubeType(final AxisAlignedDirection upstairsDirection, final int frontTextureIndex, final int backTextureIndex, final int sideTextureIndex, final int bottomTextureIndex, final int topTextureIndex) {
		this.upstairsDirection = upstairsDirection;
		this.frontTextureIndex = frontTextureIndex;
		this.backTextureIndex = backTextureIndex;
		this.sideTextureIndex = sideTextureIndex;
		this.bottomTextureIndex = bottomTextureIndex;
		this.topTextureIndex = topTextureIndex;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#obscuresNeighbor(name.martingeisse.stackd.common.geometry.AxisAlignedDirection)
	 */
	@Override
	public final boolean obscuresNeighbor(final AxisAlignedDirection direction) {
		return (direction == AxisAlignedDirection.NEGATIVE_Y || direction == upstairsDirection);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#blocksMovementToNeighbor(name.martingeisse.stackd.common.geometry.AxisAlignedDirection)
	 */
	@Override
	public boolean blocksMovementToNeighbor(final AxisAlignedDirection direction) {
		return (direction == AxisAlignedDirection.NEGATIVE_Y || direction == upstairsDirection);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#getCubeFaceTextureIndex(int)
	 */
	@Override
	public final int getCubeFaceTextureIndex(final int directionOrdinal) {
		if (directionOrdinal == AxisAlignedDirection.NEGATIVE_Y.ordinal()) {
			return bottomTextureIndex;
		} else if (directionOrdinal == upstairsDirection.ordinal()) {
			return backTextureIndex;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#buildInnerPolygons(name.martingeisse.stackd.common.cubes.MeshBuilderBase, int, int, int)
	 */
	@Override
	public void buildInnerPolygons(final MeshBuilderBase meshBuilder, final int baseX, final int baseY, final int baseZ) {
		switch (upstairsDirection) {

		case NEGATIVE_X:
			meshBuilder.addOneSidedQuad(topTextureIndex, AxisAlignedDirection.POSITIVE_Y, AxisAlignedDirection.POSITIVE_Y, baseX, baseY + 8, baseZ, 4, 0, 0, 0, 0, 8);
			meshBuilder.addOneSidedQuad(topTextureIndex, AxisAlignedDirection.POSITIVE_Y, AxisAlignedDirection.POSITIVE_Y, baseX + 4, baseY + 4, baseZ, 4, 0, 0, 0, 0, 8);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.NEGATIVE_Z, AxisAlignedDirection.NEGATIVE_Z, baseX, baseY, baseZ, 4, 0, 0, 0, 8, 0);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.NEGATIVE_Z, AxisAlignedDirection.NEGATIVE_Z, baseX + 4, baseY, baseZ, 4, 0, 0, 0, 4, 0);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.POSITIVE_Z, AxisAlignedDirection.POSITIVE_Z, baseX, baseY, baseZ + 8, 0, 8, 0, 4, 0, 0);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.POSITIVE_Z, AxisAlignedDirection.POSITIVE_Z, baseX + 4, baseY, baseZ + 8, 0, 4, 0, 4, 0, 0);
			meshBuilder.addOneSidedQuad(frontTextureIndex, AxisAlignedDirection.POSITIVE_X, AxisAlignedDirection.POSITIVE_X, baseX + 4, baseY + 4, baseZ, 0, 0, 8, 0, 4, 0);
			meshBuilder.addOneSidedQuad(frontTextureIndex, AxisAlignedDirection.POSITIVE_X, AxisAlignedDirection.POSITIVE_X, baseX + 8, baseY, baseZ, 0, 0, 8, 0, 4, 0);
			break;

		case POSITIVE_X:
			meshBuilder.addOneSidedQuad(topTextureIndex, AxisAlignedDirection.POSITIVE_Y, AxisAlignedDirection.POSITIVE_Y, baseX, baseY + 4, baseZ, 4, 0, 0, 0, 0, 8);
			meshBuilder.addOneSidedQuad(topTextureIndex, AxisAlignedDirection.POSITIVE_Y, AxisAlignedDirection.POSITIVE_Y, baseX + 4, baseY + 8, baseZ, 4, 0, 0, 0, 0, 8);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.NEGATIVE_Z, AxisAlignedDirection.NEGATIVE_Z, baseX, baseY, baseZ, 4, 0, 0, 0, 4, 0);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.NEGATIVE_Z, AxisAlignedDirection.NEGATIVE_Z, baseX + 4, baseY, baseZ, 4, 0, 0, 0, 8, 0);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.POSITIVE_Z, AxisAlignedDirection.POSITIVE_Z, baseX, baseY, baseZ + 8, 0, 4, 0, 4, 0, 0);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.POSITIVE_Z, AxisAlignedDirection.POSITIVE_Z, baseX + 4, baseY, baseZ + 8, 0, 8, 0, 4, 0, 0);
			meshBuilder.addOneSidedQuad(frontTextureIndex, AxisAlignedDirection.NEGATIVE_X, AxisAlignedDirection.NEGATIVE_X, baseX + 4, baseY + 4, baseZ, 0, 4, 0, 0, 0, 8);
			meshBuilder.addOneSidedQuad(frontTextureIndex, AxisAlignedDirection.NEGATIVE_X, AxisAlignedDirection.NEGATIVE_X, baseX, baseY, baseZ, 0, 4, 0, 0, 0, 8);
			break;

		case NEGATIVE_Z:
			meshBuilder.addOneSidedQuad(topTextureIndex, AxisAlignedDirection.POSITIVE_Y, AxisAlignedDirection.POSITIVE_Y, baseX, baseY + 8, baseZ, 8, 0, 0, 0, 0, 4);
			meshBuilder.addOneSidedQuad(topTextureIndex, AxisAlignedDirection.POSITIVE_Y, AxisAlignedDirection.POSITIVE_Y, baseX, baseY + 4, baseZ + 4, 8, 0, 0, 0, 0, 4);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.NEGATIVE_X, AxisAlignedDirection.NEGATIVE_X, baseX, baseY, baseZ, 0, 8, 0, 0, 0, 4);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.NEGATIVE_X, AxisAlignedDirection.NEGATIVE_X, baseX, baseY, baseZ + 4, 0, 4, 0, 0, 0, 4);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.POSITIVE_X, AxisAlignedDirection.POSITIVE_X, baseX + 8, baseY, baseZ, 0, 0, 4, 0, 8, 0);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.POSITIVE_X, AxisAlignedDirection.POSITIVE_X, baseX + 8, baseY, baseZ + 4, 0, 0, 4, 0, 4, 0);
			meshBuilder.addOneSidedQuad(frontTextureIndex, AxisAlignedDirection.POSITIVE_Z, AxisAlignedDirection.POSITIVE_Z, baseX, baseY + 4, baseZ + 4, 0, 4, 0, 8, 0, 0);
			meshBuilder.addOneSidedQuad(frontTextureIndex, AxisAlignedDirection.POSITIVE_Z, AxisAlignedDirection.POSITIVE_Z, baseX, baseY, baseZ + 8, 0, 4, 0, 8, 0, 0);
			break;

		case POSITIVE_Z:
			meshBuilder.addOneSidedQuad(topTextureIndex, AxisAlignedDirection.POSITIVE_Y, AxisAlignedDirection.POSITIVE_Y, baseX, baseY + 4, baseZ, 8, 0, 0, 0, 0, 4);
			meshBuilder.addOneSidedQuad(topTextureIndex, AxisAlignedDirection.POSITIVE_Y, AxisAlignedDirection.POSITIVE_Y, baseX, baseY + 8, baseZ + 4, 8, 0, 0, 0, 0, 4);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.NEGATIVE_X, AxisAlignedDirection.NEGATIVE_X, baseX, baseY, baseZ, 0, 4, 0, 0, 0, 4);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.NEGATIVE_X, AxisAlignedDirection.NEGATIVE_X, baseX, baseY, baseZ + 4, 0, 8, 0, 0, 0, 4);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.POSITIVE_X, AxisAlignedDirection.POSITIVE_X, baseX + 8, baseY, baseZ, 0, 0, 4, 0, 4, 0);
			meshBuilder.addOneSidedQuad(sideTextureIndex, AxisAlignedDirection.POSITIVE_X, AxisAlignedDirection.POSITIVE_X, baseX + 8, baseY, baseZ + 4, 0, 0, 4, 0, 8, 0);
			meshBuilder.addOneSidedQuad(frontTextureIndex, AxisAlignedDirection.NEGATIVE_Z, AxisAlignedDirection.NEGATIVE_Z, baseX, baseY + 4, baseZ + 4, 8, 0, 0, 0, 4, 0);
			meshBuilder.addOneSidedQuad(frontTextureIndex, AxisAlignedDirection.NEGATIVE_Z, AxisAlignedDirection.NEGATIVE_Z, baseX, baseY, baseZ, 8, 0, 0, 0, 4, 0);
			break;

		case NEGATIVE_Y:
		case POSITIVE_Y:
			break;

		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubetype.CubeType#collidesWithRegion(int, int, int, int, int, int)
	 */
	@Override
	public boolean collidesWithRegion(final int startX, final int startY, final int startZ, final int endX, final int endY, final int endZ) {
		if (startY < 4) {
			return true;
		}
		switch (upstairsDirection) {

		case NEGATIVE_X:
			return (startX < 4);

		case POSITIVE_X:
			return (endX > 4);

		case NEGATIVE_Z:
			return (startZ < 4);

		case POSITIVE_Z:
			return (endZ > 4);

		default:
			return false;

		}
	}

}
