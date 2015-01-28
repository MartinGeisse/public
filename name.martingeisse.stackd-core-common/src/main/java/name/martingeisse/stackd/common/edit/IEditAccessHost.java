/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.edit;

import name.martingeisse.stackd.common.geometry.RectangularRegion;

/**
 * This interface is implemented by the working set and by sections to
 * support the {@link EditAccess} class.
 */
public interface IEditAccessHost {

	/**
	 * Checks whether the specified position is inside the edit boundaries.
	 * 
	 * @param x the x coordinate of the position
	 * @param y the y coordinate of the position
	 * @param z the z coordinate of the position
	 * @return true if inside, false if outside
	 */
	public boolean containsPosition(int x, int y, int z);

	/**
	 * Obtains a {@link RectangularRegion} for the whole edit region.
	 * 
	 * @return a region for the whole edit region
	 */
	public RectangularRegion getRegion();

	/**
	 * Returns the cube value for the specified position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @return the cube value
	 */
	public byte getCube(int x, int y, int z);
	
	/**
	 * Sets the cube value for the specified position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @param value the cube value to set
	 */
	public void setCube(int x, int y, int z, byte value);
	
}
