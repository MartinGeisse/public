/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.cubetype;

import name.martingeisse.stackd.common.StackdConstants;
import name.martingeisse.stackd.common.cubes.MeshBuilderBase;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;
import name.martingeisse.stackd.common.geometry.RectangularRegion;

/**
 * Captures information about all cubes using the same cube type
 * index in the data array of a section.
 */
public abstract class CubeType {
	
	/**
	 * Checks whether a cube of this type totally obscures a neighbor's face
	 * when the neighbor lies in the specified direction from this cube. This
	 * means that the face's direction is the opposite of the specified
	 * direction.
	 *  
	 * @param direction the direction towards the neighbor
	 * @return true if this cube obscures its neighbor, false if not
	 */
	public abstract boolean obscuresNeighbor(AxisAlignedDirection direction);

	/**
	 * Checks whether this cube blocks any movement to the neighbor in the
	 * specified direction during collision detection.
	 * 
	 * @param direction the direction towards the neighbor
	 * @return true if this cube blocks movement, false if not
	 */
	public abstract boolean blocksMovementToNeighbor(AxisAlignedDirection direction);

	/**
	 * Returns the cube face texture index for the face with the specified direction,
	 * or 0 if that face is not visible.
	 * 
	 * @param directionOrdinal the ordinal number of the direction
	 * @return the texture index or 0
	 */
	public abstract int getCubeFaceTextureIndex(int directionOrdinal);
	
	/**
	 * Returns the cube face texture index for the face with the specified direction.
	 * or 0 if that face is not visible.
	 * 
	 * @param direction the direction
	 * @return the texture index or 0
	 */
	public final int getCubeFaceTextureIndex(AxisAlignedDirection direction) {
		return getCubeFaceTextureIndex(direction.ordinal());
	}
	
	/**
	 * Builds polygons in addition to the cube faces. Many cube types just leave this
	 * method empty because they're just cubes, but others (like stairs) add the
	 * "inner" polygons here. Cube faces are *not* added in this method but handled
	 * specially because the engine tries to merge cube faces to large rectangles
	 * across cubes, but this does not happen for inner polygons.
	 * 
	 * This method uses detail coordinates, with {@link StackdConstants#GEOMETRY_DETAIL_FACTOR}
	 * units per cube. The inner polygons are contained in a cube using this cube type, located
	 * in the range (baseX, baseY, baseZ) to (baseX + df, baseY + df, baseZ + df), with df
	 * being {@link StackdConstants#GEOMETRY_DETAIL_FACTOR}.
	 * 
	 * @param meshBuilder the mesh builder used to actually add polygons
	 * @param baseX the base x coordinate of the containing cube
	 * @param baseY the base y coordinate of the containing cube
	 * @param baseZ the base z coordinate of the containing cube
	 */
	public void buildInnerPolygons(MeshBuilderBase meshBuilder, int baseX, int baseY, int baseZ) {
	}
	
	/**
	 * Checks whether a cube of this cube type that lies in the region (0, 0, 0) to
	 * (df, df, df) with df = {@link StackdConstants#GEOMETRY_DETAIL_FACTOR} collides
	 * with the specified region, which is also expressed in detail coordinates.
	 * 
	 * The arguments should be assumed to behave just like a {@link RectangularRegion}.
	 * They are passed directly to avoid memory thrashing (otherwise collision detection
	 * would have to create a {@link RectangularRegion} for each cube).
	 * 
	 * This method may assume that the region does intersect with the cube's region,
	 * i.e. (0, 0, 0) to (df, df, df). That is, the return value of this method is
	 * undefined for other regions. This means for example that a fully solid cube
	 * may just return true, without looking at the region at all.
	 * 
	 * @param startX the starting x coordinate of the region, expressed in detail coordinates
	 * @param startY the starting y coordinate of the region, expressed in detail coordinates
	 * @param startZ the starting z coordinate of the region, expressed in detail coordinates
	 * @param endX the ending x coordinate of the region, expressed in detail coordinates
	 * @param endY the ending y coordinate of the region, expressed in detail coordinates
	 * @param endZ the ending z coordinate of the region, expressed in detail coordinates
	 * @return true if there is any intersection, false if not
	 */
	public abstract boolean collidesWithRegion(int startX, int startY, int startZ, int endX, int endY, int endZ);

}
