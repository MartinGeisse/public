/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.edit;

import name.martingeisse.stackd.common.geometry.RectangularRegion;

/**
 * This is the main class that deals with editing world data. To actually
 * modify the data, you must obtain an instance of this class from the
 * world, working set, section or whatever.
 */
public final class EditAccess {

	/**
	 * the host
	 */
	private final IEditAccessHost host;
	
	/**
	 * Constructor.
	 * @param host the data host to edit
	 */
	public EditAccess(IEditAccessHost host) {
		this.host = host;
	}
	
	/**
	 * Checks whether the specified position is inside the edit region boundaries.
	 * @param x the x coordinate of the position
	 * @param y the y coordinate of the position
	 * @param z the z coordinate of the position
	 * @return true if inside, false if outside
	 */
	public boolean containsPosition(int x, int y, int z) {
		return host.containsPosition(x, y, z);
	}
	
	/**
	 * Obtains a {@link RectangularRegion} for the whole edit region.
	 * @return a region for the whole edit region
	 */
	public RectangularRegion getRegion() {
		return host.getRegion();
	}
	
	/**
	 * Returns the cube value for the specified position.
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @return the cube value
	 */
	public byte getCube(int x, int y, int z) {
		return host.getCube(x, y, z);
	}
	
	/**
	 * Gets the cube value for the specified position. This method first ensures
	 * that the specified position is inside the region boundaries, so it does not
	 * cause an exception if that is not the case (instead, it returns 0).
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @return the cube type
	 */
	public final byte getCubeSafe(int x, int y, int z) {
		if (host.containsPosition(x, y, z)) {
			return host.getCube(x, y, z);
		} else {
			return 0;
		}
	}
	
	/**
	 * Sets the cube value for the specified position.
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @param value the cube value to set
	 */
	public void setCube(int x, int y, int z, byte value) {
		host.setCube(x, y, z, value);
	}

	/**
	 * Sets the cube value for the specified position. This method first ensures
	 * that the specified position is inside the region boundaries, so it does not
	 * cause an exception if that is not the case (instead, it does nothing).
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @param value the cube value to set
	 */
	public final void setCubeSafe(int x, int y, int z, byte value) {
		if (host.containsPosition(x, y, z)) {
			host.setCube(x, y, z, value);
		}
	}
	
}
