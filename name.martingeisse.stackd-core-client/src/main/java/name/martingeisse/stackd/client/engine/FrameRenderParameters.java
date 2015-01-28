/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.engine;

/**
 * This class captures the per-frame parameters that affect rendering.
 * 
 * This class keeps the viewer's position, but for now that position does
 * not affect the modelview transformation -- that transformation must be
 * set outside the call to {@link WorldWorkingSet#draw(FrameRenderParameters)}.
 * The position is only used for culling and special effects.
 */
public final class FrameRenderParameters {

	/**
	 * the x
	 */
	private final int x;

	/**
	 * the y
	 */
	private final int y;

	/**
	 * the z
	 */
	private final int z;

	/**
	 * Constructor.
	 * @param x the viewer's x coordinate
	 * @param y the viewer's y coordinate
	 * @param z the viewer's z coordinate
	 */
	public FrameRenderParameters(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Getter method for the x.
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter method for the y.
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Getter method for the z.
	 * @return the z
	 */
	public int getZ() {
		return z;
	}

}
