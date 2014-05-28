/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.util;

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW_MATRIX;
import static org.lwjgl.opengl.GL11.GL_PROJECTION_MATRIX;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.util.glu.GLU.gluUnProject;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import name.martingeisse.stackd.common.StackdConstants;
import org.lwjgl.BufferUtils;

/**
 * This class captures the information that is needed for ray actions.
 * 
 * Usage: Invoke capture() during the render process as described in
 * the method documentation, then invoke execute() at any later time
 * (typically in the same frame, but this is not a requirement) to
 * actually execute the ray action. Use release() to release the
 * captured state(). execute() will do nothing if there is no ray
 * target captured.
 */
public class RayActionSupport {

	/**
	 * the width
	 */
	private final int width;
	
	/**
	 * the height
	 */
	private final int height;
	
	/**
	 * the depthValueBuffer
	 */
	private final FloatBuffer depthValueBuffer;

	/**
	 * the viewport
	 */
	private final IntBuffer viewport;
	
	/**
	 * the modelviewTransform
	 */
	private final FloatBuffer modelviewTransform;

	/**
	 * the projectionTransform
	 */
	private final FloatBuffer projectionTransform;
	
	/**
	 * the objectPosition
	 */
	private final FloatBuffer objectPosition;
	
	/**
	 * the impactX
	 */
	private double impactX;
	
	/**
	 * the impactY
	 */
	private double impactY;
	
	/**
	 * the impactZ
	 */
	private double impactZ;
	
	/**
	 * the captured
	 */
	private boolean captured;
	
	/**
	 * Constructor.
	 * 
	 * Note: The source position of the ray could actually be obtained from the
	 * modelview matrix, just like all other projection properties are.
	 * However, this involves *inverting* the modelview matrix, so I try
	 * to avoid it. It is basically (MV^-1)*(0, 0, 0, 1), transforming the
	 * origin in view space back to world space (the model transformation is
	 * the identity).
	 * 
	 * @param width the width of the frame buffer
	 * @param height the height of the frame buffer
	 */
	public RayActionSupport(int width, int height) {
		this.width = width;
		this.height = height;
		this.depthValueBuffer = BufferUtils.createFloatBuffer(1);
		this.viewport = BufferUtils.createIntBuffer(16);
		this.modelviewTransform = BufferUtils.createFloatBuffer(16);
		this.projectionTransform = BufferUtils.createFloatBuffer(16);
		this.objectPosition = BufferUtils.createFloatBuffer(3);
		this.captured = false;
	}

	/**
	 * This method captures the current position from the modelview transform
	 * and the ray impact distance from the depth buffer. It must be invoked
	 * after the world has been drawn, but before the HUD is drawn (potentially
	 * destroying the world's pixels) and before swapping buffers.
	 * 
	 * Performance-wise, it is advised to call this method only if a ray action
	 * is actually being used in this frame, because it might stall and flush
	 * the render pipeline.
	 */
	public void capture() {
		
		// read the distance from the depth buffer
		glReadPixels(width >> 1, height >> 1, 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, depthValueBuffer);
		float depthBufferValue = depthValueBuffer.get(0);
		
		// read the viewport and transform values
		glGetInteger(GL_VIEWPORT, viewport);
		glGetFloat(GL_MODELVIEW_MATRIX, modelviewTransform);
		glGetFloat(GL_PROJECTION_MATRIX, projectionTransform);

		// compute the impact position
		gluUnProject(width >> 1, height >> 1, depthBufferValue, modelviewTransform, projectionTransform, viewport, objectPosition);
		impactX = objectPosition.get(0) / StackdConstants.GEOMETRY_DETAIL_FACTOR;
		impactY = objectPosition.get(1) / StackdConstants.GEOMETRY_DETAIL_FACTOR;
		impactZ = objectPosition.get(2) / StackdConstants.GEOMETRY_DETAIL_FACTOR;
		captured = true;
		
	}
	
	/**
	 * Releases the captured values.
	 */
	public void release() {
		captured = false;
	}
	
	/**
	 * Executes the specified ray action. This method only works when capture() has
	 * been called during the render process. The captured impact position, together
	 * with the source position specified here as well as the "forward" flag determine
	 * which cube is affected by the ray action.
	 * 
	 * @param sourceX the x position of the ray source
	 * @param sourceY the y position of the ray source
	 * @param sourceZ the z position of the ray source
	 * @param action the action to execute
	 */
	public void execute(double sourceX, double sourceY, double sourceZ, RayAction action) {
		if (!captured) {
			return;
		}
		double dx = (impactX - sourceX);
		double dy = (impactY - sourceY);
		double dz = (impactZ - sourceZ);
		double distance = Math.sqrt(dx*dx + dy*dy + dz*dz);
		double factor = ((action.isForward() ? 0.01 : -0.01) / distance);
		dx *= factor;
		dy *= factor;
		dz *= factor;
		int baseX = (int)Math.floor(impactX - dx * 5);
		int baseY = (int)Math.floor(impactY - dy * 5);
		int baseZ = (int)Math.floor(impactZ - dz * 5);
		for (int i=0; i<10; i++) {
			int currentX = (int)Math.floor(impactX);
			int currentY = (int)Math.floor(impactY);
			int currentZ = (int)Math.floor(impactZ);
			if (currentX != baseX || currentY != baseY || currentZ != baseZ) {
				action.handleImpact(currentX, currentY, currentZ, distance);
				return;
			}
			impactX += dx;
			impactY += dy;
			impactZ += dz;
		}
	}
	
}
