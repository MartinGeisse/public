/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.util;


/**
 * Base class for an action that shoots a ray from the viewer and operates
 * on the block hit by that ray. The computation is based on the OpenGL
 * depth buffer and as such depends on the drawing code.
 * 
 * Ray actions are executed by a {@link RayActionSupport} that performs the
 * math and captures the necessary depth value.
 * 
 * Cube selection can be "forward" (operating on the cube that blocked the
 * ray, typical for digging) or "backward" (operating on the last empty
 * cube before the ray was blocked, typical for cube placement).
 */
public abstract class RayAction {

	/**
	 * the forward
	 */
	private final boolean forward;

	/**
	 * Constructor.
	 * @param forward whether the cube is selected in a "forward" or
	 * "backward" way.
	 */
	public RayAction(boolean forward) {
		this.forward = forward;
	}
	
	/**
	 * Getter method for the forward.
	 * @return the forward
	 */
	boolean isForward() {
		return forward;
	}

	/**
	 * Handles the ray impact.
	 * @param x the x position of the cube that was hit by the ray
	 * @param y the y position of the cube that was hit by the ray
	 * @param z the z position of the cube that was hit by the ray
	 * @param distance the distance the ray traveled before hitting its target
	 */
	public abstract void handleImpact(int x, int y, int z, double distance);

}
