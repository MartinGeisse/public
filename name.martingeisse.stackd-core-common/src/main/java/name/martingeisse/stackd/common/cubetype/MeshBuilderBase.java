/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.cubetype;

import java.util.ArrayList;
import java.util.List;
import name.martingeisse.stackd.common.StackdConstants;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;
import name.martingeisse.stackd.common.geometry.RectangularRegion;

/**
 * This is the base class for the client-side mesh builder. It allows to define cube types
 * (which use this class) in common code that is shared on client and server, without
 * referring to client-only OpenGL code. The actual mesh builder class on the client does
 * the actual OpenGL handling.
 * 
 * The methods in this class use detail coordinates, with {@link StackdConstants#GEOMETRY_DETAIL_FACTOR}
 * units per cube.
 */
public class MeshBuilderBase {

	/**
	 * the boundingBox
	 */
	private RectangularRegion boundingBox;

	/**
	 * the triangles
	 */
	private List<Triangle> triangles;

	/**
	 * Constructor.
	 */
	public MeshBuilderBase() {
		this.boundingBox = null;
		this.triangles = new ArrayList<>();
	}

	/**
	 * Setter method for the boundingBox.
	 * @param boundingBox the boundingBox to set
	 */
	public final void setBoundingBox(final RectangularRegion boundingBox) {
		this.boundingBox = boundingBox;
	}
	
	/**
	 * Getter method for the boundingBox.
	 * @return the boundingBox
	 */
	protected final RectangularRegion getBoundingBox() {
		return boundingBox;
	}
	
	/**
	 * Getter method for the triangles.
	 * @return the triangles
	 */
	protected final List<Triangle> getTriangles() {
		return triangles;
	}

	/**
	 * Adds a one-sided triangle.
	 * 
	 * This method uses detail coordinates, with {@link StackdConstants#GEOMETRY_DETAIL_FACTOR} units per cube.
	 * 
	 * @param textureIndex the texture index
	 * @param textureCoordinateGenerationDirection the direction used to set up texture coordinate generation
	 * @param backfaceCullingDirection the direction used for backface culling, or null to disable BFC
	 * @param x1 the x coordinate of the first vertex
	 * @param y1 the y coordinate of the first vertex
	 * @param z1 the z coordinate of the first vertex
	 * @param x2 the x coordinate of the second vertex
	 * @param y2 the y coordinate of the second vertex
	 * @param z2 the z coordinate of the second vertex
	 * @param x3 the x coordinate of the third vertex
	 * @param y3 the y coordinate of the third vertex
	 * @param z3 the z coordinate of the third vertex
	 */
	public final void addTriangle(final int textureIndex, final AxisAlignedDirection textureCoordinateGenerationDirection, final AxisAlignedDirection backfaceCullingDirection, final int x1, final int y1, final int z1, final int x2, final int y2, final int z2, final int x3, final int y3, final int z3) {
		if (textureIndex != 0) {
			final Triangle triangle = new Triangle();
			triangle.textureIndex = textureIndex;
			triangle.textureCoordinateGenerationDirection = textureCoordinateGenerationDirection;
			triangle.backfaceCullingDirection = backfaceCullingDirection;
			triangle.x1 = x1;
			triangle.y1 = y1;
			triangle.z1 = z1;
			triangle.x2 = x2;
			triangle.y2 = y2;
			triangle.z2 = z2;
			triangle.x3 = x3;
			triangle.y3 = y3;
			triangle.z3 = z3;
			triangles.add(triangle);
		}
	}

	/**
	 * Adds a two-sided triangle.
	 * 
	 * This method uses detail coordinates, with {@link StackdConstants#GEOMETRY_DETAIL_FACTOR} units per cube.
	 * 
	 * @param textureIndex the texture index
	 * @param textureCoordinateGenerationDirection the direction used to set up texture coordinate generation
	 * @param x1 the x coordinate of the first vertex
	 * @param y1 the y coordinate of the first vertex
	 * @param z1 the z coordinate of the first vertex
	 * @param x2 the x coordinate of the second vertex
	 * @param y2 the y coordinate of the second vertex
	 * @param z2 the z coordinate of the second vertex
	 * @param x3 the x coordinate of the third vertex
	 * @param y3 the y coordinate of the third vertex
	 * @param z3 the z coordinate of the third vertex
	 */
	public final void addTwoSidedTriangle(final int textureIndex, final AxisAlignedDirection textureCoordinateGenerationDirection, final int x1, final int y1, final int z1, final int x2, final int y2, final int z2, final int x3, final int y3, final int z3) {
		addTriangle(textureIndex, textureCoordinateGenerationDirection, null, x1, y1, z1, x2, y2, z2, x3, y3, z3);
		addTriangle(textureIndex, textureCoordinateGenerationDirection, null, x1, y1, z1, x3, y3, z3, x2, y2, z2);
	}

	/**
	 * Adds a one-sided parallelogram quad.
	 * 
	 * If you are looking straight at the quad, i.e. the quad's facing direction is pointing towards you,
	 * then the "first" direction should be to the right and the "second" direction should be upwards.
	 * 
	 * This method uses detail coordinates, with {@link StackdConstants#GEOMETRY_DETAIL_FACTOR} units per cube.
	 * 
	 * @param textureIndex the texture index
	 * @param textureCoordinateGenerationDirection the direction used to set up texture coordinate generation
	 * @param backfaceCullingDirection the direction used for backface culling, or null to disable BFC
	 * @param x the base x coordinate
	 * @param y the base y coordinate
	 * @param z the base z coordinate
	 * @param dx1 the delta-x coordinate in the first direction
	 * @param dy1 the delta-y coordinate in the first direction
	 * @param dz1 the delta-z coordinate in the first direction
	 * @param dx2 the delta-x coordinate in the second direction
	 * @param dy2 the delta-y coordinate in the second direction
	 * @param dz2 the delta-z coordinate in the second direction
	 */
	public final void addOneSidedQuad(final int textureIndex, final AxisAlignedDirection textureCoordinateGenerationDirection, final AxisAlignedDirection backfaceCullingDirection, int x, int y, int z, int dx1, int dy1, int dz1, int dx2, int dy2, int dz2) {
		addTriangle(textureIndex, textureCoordinateGenerationDirection, backfaceCullingDirection, x + dx1, y + dy1, z + dz1, x, y, z, x + dx2, y + dy2, z + dz2);
		addTriangle(textureIndex, textureCoordinateGenerationDirection, backfaceCullingDirection, x + dx1, y + dy1, z + dz1, x + dx2, y + dy2, z + dz2, x + dx1 + dx2, y + dy1 + dy2, z + dz1 + dz2);
	}
	
	/**
	 * Adds a two-sided parallelogram quad.
	 * 
	 * This method uses detail coordinates, with {@link StackdConstants#GEOMETRY_DETAIL_FACTOR} units per cube.
	 * 
	 * @param textureIndex the texture index
	 * @param textureCoordinateGenerationDirection the direction used to set up texture coordinate generation
	 * @param x the base x coordinate
	 * @param y the base y coordinate
	 * @param z the base z coordinate
	 * @param dx1 the delta-x coordinate in the first direction
	 * @param dy1 the delta-y coordinate in the first direction
	 * @param dz1 the delta-z coordinate in the first direction
	 * @param dx2 the delta-x coordinate in the second direction
	 * @param dy2 the delta-y coordinate in the second direction
	 * @param dz2 the delta-z coordinate in the second direction
	 */
	public final void addTwoSidedQuad(final int textureIndex, final AxisAlignedDirection textureCoordinateGenerationDirection, int x, int y, int z, int dx1, int dy1, int dz1, int dx2, int dy2, int dz2) {
		addTwoSidedTriangle(textureIndex, textureCoordinateGenerationDirection, x, y, z, x + dx1, y + dy1, z + dz1, x + dx2, y + dy2, z + dz2);
		addTwoSidedTriangle(textureIndex, textureCoordinateGenerationDirection, x + dx1, y + dy1, z + dz1, x + dx2, y + dy2, z + dz2, x + dx1 + dx2, y + dy1 + dy2, z + dz1 + dz2);
	}
	
	/**
	 * Adds some or all of the polygons that make up an axis-aligned box with the specified min/max
	 * coordinates and texture indices. Any texture index that is 0 causes the correspding box face
	 * to be omitted. The order of the texture indices is the natural {@link AxisAlignedDirection}
	 * order.
	 * 
	 * This method uses detail coordinates, with {@link StackdConstants#GEOMETRY_DETAIL_FACTOR} units per cube.
	 * 
	 * @param x1 the min x coordinate
	 * @param y1 the min y coordinate
	 * @param z1 the min z coordinate
	 * @param x2 the max x coordinate
	 * @param y2 the max y coordinate
	 * @param z2 the max z coordinate
	 * @param textureIndex0 the texture index for face 0, or 0 to omit that face
	 * @param textureIndex1 the texture index for face 1, or 0 to omit that face
	 * @param textureIndex2 the texture index for face 2, or 0 to omit that face
	 * @param textureIndex3 the texture index for face 3, or 0 to omit that face
	 * @param textureIndex4 the texture index for face 4, or 0 to omit that face
	 * @param textureIndex5 the texture index for face 5, or 0 to omit that face
	 */
	public final void addAxisAlignedBox(int x1, int y1, int z1, int x2, int y2, int z2, int textureIndex0, int textureIndex1, int textureIndex2, int textureIndex3, int textureIndex4, int textureIndex5) {
		int dx = x2 - x1;
		int dy = y2 - y1;
		int dz = z2 - z1;
		addOneSidedQuad(textureIndex0, AxisAlignedDirection.NEGATIVE_X, AxisAlignedDirection.NEGATIVE_X, x1, y1, z1, 0, dy, 0, 0, 0, dz);
		addOneSidedQuad(textureIndex1, AxisAlignedDirection.POSITIVE_X, AxisAlignedDirection.POSITIVE_X, x2, y1, z1, 0, 0, dz, 0, dy, 0);
		addOneSidedQuad(textureIndex2, AxisAlignedDirection.NEGATIVE_Y, AxisAlignedDirection.NEGATIVE_Y, x1, y1, z1, 0, 0, dz, dx, 0, 0);
		addOneSidedQuad(textureIndex3, AxisAlignedDirection.POSITIVE_Y, AxisAlignedDirection.POSITIVE_Y, x1, y2, z1, dx, 0, 0, 0, 0, dz);
		addOneSidedQuad(textureIndex4, AxisAlignedDirection.NEGATIVE_Z, AxisAlignedDirection.NEGATIVE_Z, x1, y1, z1, dx, 0, 0, 0, dy, 0);
		addOneSidedQuad(textureIndex5, AxisAlignedDirection.POSITIVE_Z, AxisAlignedDirection.POSITIVE_Z, x1, y1, z2, 0, dy, 0, dx, 0, 0);
	}

	/**
	 * Adds a pair of square polygons inside a cube cell, oriented as a horizontal X, to represent
	 * things like grass in a simple way. The X fills a rectangular region with the specified
	 * min/max coordinates.
	 * 
	 * This method uses detail coordinates, with {@link StackdConstants#GEOMETRY_DETAIL_FACTOR} units per cube.
	 * 
	 * @param x1 the min x coordinate
	 * @param y1 the min y coordinate
	 * @param z1 the min z coordinate
	 * @param x2 the max x coordinate
	 * @param y2 the max y coordinate
	 * @param z2 the max z coordinate
	 * @param textureIndex the texture index
	 */
	public final void addDiagonalCross(int x1, int y1, int z1, int x2, int y2, int z2, int textureIndex) {
		int dx = x2 - x1;
		int dy = y2 - y1;
		int dz = z2 - z1;
		addTwoSidedQuad(textureIndex, AxisAlignedDirection.NEGATIVE_X, x1, y1, z1, dx, 0, dz, 0, dy, 0);
		addTwoSidedQuad(textureIndex, AxisAlignedDirection.NEGATIVE_X, x2, y1, z1, -dx, 0, dz, 0, dy, 0);
	}

	/**
	 *
	 */
	protected static class Triangle {
		public int textureIndex;
		public AxisAlignedDirection textureCoordinateGenerationDirection;
		public AxisAlignedDirection backfaceCullingDirection;
		public int x1, y1, z1;
		public int x2, y2, z2;
		public int x3, y3, z3;
	}

}
