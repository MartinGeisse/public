/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.ingame.player;

import name.martingeisse.stackd.client.engine.WorldWorkingSet;
import name.martingeisse.stackd.common.StackdConstants;
import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.geometry.MutableEulerAngles;
import name.martingeisse.stackd.common.geometry.MutableVector3d;
import name.martingeisse.stackd.common.geometry.RectangularRegion;
import name.martingeisse.stackd.common.geometry.SectionId;
import org.apache.log4j.Logger;

/**
 * Base class for players and player proxies. 
 */
public abstract class PlayerBase {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(PlayerBase.class);
	
	/**
	 * the world
	 */
	private final WorldWorkingSet world;

	/**
	 * the position
	 */
	private final MutableVector3d position;
	
	/**
	 * the orientation
	 */
	private final MutableEulerAngles orientation;
	
	/**
	 * Constructor.
	 * @param worldWorkingSet the world working set
	 */
	public PlayerBase(final WorldWorkingSet worldWorkingSet) {
		this.world = worldWorkingSet;
		this.position = new MutableVector3d(0, 0, 0);
		this.orientation = new MutableEulerAngles(0, 0, 0);
	}

	/**
	 * Getter method for the world.
	 * @return the world
	 */
	public final WorldWorkingSet getWorld() {
		return world;
	}

	/**
	 * Getter method for the position.
	 * @return the position
	 */
	public final MutableVector3d getPosition() {
		return position;
	}
	
	/**
	 * Getter method for the orientation.
	 * @return the orientation
	 */
	public final MutableEulerAngles getOrientation() {
		return orientation;
	}
	
	/**
	 * @return the section ID of the section the player is currently in
	 */
	public final SectionId getSectionId() {
		return new SectionId(position, world.getClusterSize());
	}
	
	/**
	 * Moves the player by the specified amounts forward and to the right.
	 * @param forwardAmount the amount to move forward
	 * @param rightAmount the amount to move to the right
	 * @param maxStairsHeight the maximum height for stairs to climb
	 */
	public final void moveHorizontal(final double forwardAmount, final double rightAmount, final double maxStairsHeight) {
		
		// coordinate system note: (leftAngle == 0) means "forward", i.e. towards -Z, with +X pointing right
		final double radians = orientation.getHorizontalAngle() * 2 * Math.PI / 360.0;
		final double sin = Math.sin(radians), cos = Math.cos(radians);
		final double dx = -sin * forwardAmount + cos * rightAmount;
		final double dz = -cos * forwardAmount - sin * rightAmount;
		
		// try to move along x and z independently (for wall sliding). If any one of them failed, try
		// stair climbing.
		boolean xok = checkAndMoveTo(position.getX() + dx, position.getY(), position.getZ());
		boolean zok = checkAndMoveTo(position.getX(), position.getY(), position.getZ() + dz);
		if (!xok || !zok) {
			moveUp(maxStairsHeight);
			if (!xok) {
				checkAndMoveTo(position.getX() + dx, position.getY(), position.getZ());
			}
			if (!zok) {
				checkAndMoveTo(position.getX(), position.getY(), position.getZ() + dz);
			}
			moveUp(-maxStairsHeight / 4);
			moveUp(-maxStairsHeight / 4);
			moveUp(-maxStairsHeight / 4);
			moveUp(-maxStairsHeight / 4);
		}
		
	}

	/**
	 * Moves the player by the specified amount upwards (negative values indicate downwards).
	 * @param amount the amount to move
	 * @return whether movement succeeded
	 */
	public final boolean moveUp(final double amount) {
		return checkAndMoveTo(position.getX(), position.getY() + amount, position.getZ());
	}

	/**
	 * @param nx the new x position
	 * @param ny the new y position
	 * @param nz the new z position
	 * @return whether movement succeeded
	 */
	public final boolean checkAndMoveTo(final double nx, final double ny, final double nz) {
		if (!isBlockedAt(nx, ny, nz)) {
			position.setX(nx);
			position.setY(ny);
			position.setZ(nz);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks whether the player would be blocked if he were at the specified position.
	 * @param nx the new x position
	 * @param ny the new y position
	 * @param nz the new z position
	 * @return whether the player is blocked at that position
	 */
	public final boolean isBlockedAt(final double nx, final double ny, final double nz) {
		return world.getCompositeCollider().collides(createDetailCollisionRegion(nx, ny, nz));
	}
	
	/**
	 * Checks whether the player is blocked at his current position
	 * @return true if blocked, false if not
	 */
	public final boolean isBlocked() {
		return isBlockedAt(position.getX(), position.getY(), position.getZ());
	}

	/**
	 * 
	 */
	public void dump() {
		logger.info("position: " + position);
		logger.info("orientation: " + orientation);
	}

	/**
	 * Creates a collision region for this player with normal precision.
	 * 
	 * @return a new {@link RectangularRegion} that describes the occupied region.
	 */
	public final RectangularRegion createCollisionRegion() {
		return createDetailCollisionRegion().divideAndRoundToOuter(new ClusterSize(StackdConstants.GEOMETRY_DETAIL_SHIFT));
	}

	/**
	 * Creates a collision region for this player with detail precision.
	 * 
	 * @return a new {@link RectangularRegion} that describes the occupied region.
	 */
	public final RectangularRegion createDetailCollisionRegion() {
		return createDetailCollisionRegion(position.getX(), position.getY(), position.getZ());
	}
	
	/**
	 * 
	 */
	private static final RectangularRegion createDetailCollisionRegion(double x, double y, double z) {
		
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
