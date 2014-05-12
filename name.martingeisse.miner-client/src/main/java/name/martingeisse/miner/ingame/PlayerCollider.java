/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.ingame;

import name.martingeisse.stackd.common.StackdConstants;
import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.geometry.RectangularRegion;

/**
 * The collision detection model for the player. This class does not store
 * position information -- this is already in the player object.
 * 
 * The main functionality of a collider is to return the cubes the player occupies.
 */
public class PlayerCollider {

	/**
	 * Constructor.
	 */
	public PlayerCollider() {
	}

	/**
	 * Creates a region for this collider with normal precision.
	 * 
	 * @param x the player's x position
	 * @param y the player's y position
	 * @param z the player's z position
	 * @return a new {@link RectangularRegion} that describes the occupied region.
	 */
	public RectangularRegion createRegion(double x, double y, double z) {
		return createDetailRegion(x, y, z).divideAndRoundToOuter(new ClusterSize(StackdConstants.GEOMETRY_DETAIL_SHIFT));
	}

	/**
	 * Creates a region for this collider with detail precision.
	 * 
	 * @param x the player's x position
	 * @param y the player's y position
	 * @param z the player's z position
	 * @return a new {@link RectangularRegion} that describes the occupied region.
	 */
	public RectangularRegion createDetailRegion(double x, double y, double z) {

		// collider size
		int width = 2;
		int height = 2;
		int depth = 13;
		
		// scale the player's position to detail coordinates
		int detailX = (int)(x * StackdConstants.GEOMETRY_DETAIL_FACTOR);
		int detailY = (int)(y * StackdConstants.GEOMETRY_DETAIL_FACTOR);
		int detailZ = (int)(z * StackdConstants.GEOMETRY_DETAIL_FACTOR);
		
		// create the occupied region. note: +1 because "end" is exclusive
		return new RectangularRegion(detailX - width, detailY - depth, detailZ - width, detailX + width + 1, detailY + height + 1, detailZ + width + 1);
		
	}
	
}
