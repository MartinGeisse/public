/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.util;

import static org.lwjgl.opengl.GL11.glVertex3d;

import org.lwjgl.opengl.GL11;

/**
 * Helper methods to draw objects using OpenGL.
 * 
 * All methods here are based on triangles. Those that claim to craw
 * quads do so using triangles, so be sure to also call {@link GL11#glBegin(int)}
 * with {@link GL11#GL_TRIANGLES}.
 */
public final class GlUtil {

	/**
	 * Prevent instantiation.
	 */
	private GlUtil() {
	}

	/**
	 * Sends the vertices for a one-sided parallelogram quad.
	 * 
	 * If you are looking straight at the quad, i.e. the quad's facing direction is pointing towards you,
	 * then the "first" direction should be to the right and the "second" direction should be upwards.
	 * 
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
	public static void sendQuadVertices(double x, double y, double z, double dx1, double dy1, double dz1, double dx2, double dy2, double dz2) {
		glVertex3d(x + dx1, y + dy1, z + dz1);
		glVertex3d(x, y, z);
		glVertex3d(x + dx2, y + dy2, z + dz2);
		glVertex3d(x + dx1, y + dy1, z + dz1);
		glVertex3d(x + dx2, y + dy2, z + dz2);
		glVertex3d(x + dx1 + dx2, y + dy1 + dy2, z + dz1 + dz2);
	}

	/**
	 * Adds the polygons that make up an axis-aligned box with the specified min/max
	 * coordinates.
	 * 
	 * @param x1 the min x coordinate
	 * @param y1 the min y coordinate
	 * @param z1 the min z coordinate
	 * @param x2 the max x coordinate
	 * @param y2 the max y coordinate
	 * @param z2 the max z coordinate
	 */
	public static void sendAxisAlignedBoxVertices(double x1, double y1, double z1, double x2, double y2, double z2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		double dz = z2 - z1;
		sendQuadVertices(x1, y1, z1, 0, dy, 0, 0, 0, dz);
		sendQuadVertices(x2, y1, z1, 0, 0, dz, 0, dy, 0);
		sendQuadVertices(x1, y1, z1, 0, 0, dz, dx, 0, 0);
		sendQuadVertices(x1, y2, z1, dx, 0, 0, 0, 0, dz);
		sendQuadVertices(x1, y1, z1, dx, 0, 0, 0, dy, 0);
		sendQuadVertices(x1, y1, z2, 0, dy, 0, dx, 0, 0);
	}

}
