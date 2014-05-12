/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.ingame;

import name.martingeisse.stackd.client.engine.WorldWorkingSet;
import name.martingeisse.stackd.common.geometry.SectionId;

/**
 * Note: Angles are measured in degrees, not radians! 
 */
public class Player {

	/**
	 * the world
	 */
	private WorldWorkingSet world;

	/**
	 * the x
	 */
	private double x;

	/**
	 * the y
	 */
	private double y;

	/**
	 * the z
	 */
	private double z;

	/**
	 * the verticalSpeed
	 */
	private double verticalSpeed;

	/**
	 * the leftAngle
	 */
	private double leftAngle;

	/**
	 * the upAngle
	 */
	private double upAngle;

	/**
	 * the observerMode
	 */
	private boolean observerMode;

	/**
	 * the wantsToJump
	 */
	private boolean wantsToJump;
	
	/**
	 * the onGround
	 */
	private boolean onGround;
	
	/**
	 * the justLanded
	 */
	private boolean justLanded;

	/**
	 * Constructor.
	 */
	public Player() {
		observerMode = false;
		onGround = false;
	}

	/**
	 * Getter method for the world.
	 * @return the world
	 */
	public WorldWorkingSet getWorld() {
		return world;
	}

	/**
	 * Setter method for the world.
	 * @param world the world to set
	 */
	public void setWorld(final WorldWorkingSet world) {
		this.world = world;
	}

	/**
	 * Getter method for the x.
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Setter method for the x.
	 * @param x the x to set
	 */
	public void setX(final double x) {
		this.x = x;
	}

	/**
	 * Getter method for the y.
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Setter method for the y.
	 * @param y the y to set
	 */
	public void setY(final double y) {
		this.y = y;
	}

	/**
	 * Getter method for the z.
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Setter method for the z.
	 * @param z the z to set
	 */
	public void setZ(final double z) {
		this.z = z;
	}

	/**
	 * Getter method for the leftAngle.
	 * @return the leftAngle
	 */
	public double getLeftAngle() {
		return leftAngle;
	}

	/**
	 * Setter method for the leftAngle.
	 * @param leftAngle the leftAngle to set
	 */
	public void setLeftAngle(final double leftAngle) {
		this.leftAngle = leftAngle;
	}

	/**
	 * Getter method for the upAngle.
	 * @return the upAngle
	 */
	public double getUpAngle() {
		return upAngle;
	}

	/**
	 * Setter method for the upAngle.
	 * @param upAngle the upAngle to set
	 */
	public void setUpAngle(final double upAngle) {
		this.upAngle = upAngle;
	}

	/**
	 * Getter method for the observerMode.
	 * @return the observerMode
	 */
	public boolean isObserverMode() {
		return observerMode;
	}

	/**
	 * Setter method for the observerMode.
	 * @param observerMode the observerMode to set
	 */
	public void setObserverMode(final boolean observerMode) {
		this.observerMode = observerMode;
	}

	/**
	 * Getter method for the wantsToJump.
	 * @return the wantsToJump
	 */
	public boolean isWantsToJump() {
		return wantsToJump;
	}

	/**
	 * Setter method for the wantsToJump.
	 * @param wantsToJump the wantsToJump to set
	 */
	public void setWantsToJump(final boolean wantsToJump) {
		this.wantsToJump = wantsToJump;
	}

	/**
	 * Getter method for the onGround.
	 * @return the onGround
	 */
	public boolean isOnGround() {
		return onGround;
	}
	
	/**
	 * Getter method for the justLanded.
	 * @return the justLanded
	 */
	public boolean isJustLanded() {
		return justLanded;
	}
	
	/**
	 * @return the section ID of the section the player is currently in
	 */
	public SectionId getSectionId() {
		int shift = world.getClusterSize().getShiftBits();
		final int playerSectionX = ((int)(Math.floor(x)) >> shift);
		final int playerSectionY = ((int)(Math.floor(y)) >> shift);
		final int playerSectionZ = ((int)(Math.floor(z)) >> shift);
		return new SectionId(playerSectionX, playerSectionY, playerSectionZ);
	}
	
	/**
	 * Returns the distance to the specified point.
	 * @param x the x coordinate of the point
	 * @param y the y coordinate of the point
	 * @param z the z coordinate of the point
	 * @return the distance
	 */
	public double getDistanceTo(final double x, final double y, final double z) {
		final double dx = (x - this.x);
		final double dy = (y - this.y);
		final double dz = (z - this.z);
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	/**
	 * Moves the player by the specified amounts forward and to the right.
	 * @param forwardAmount the amount to move forward
	 * @param rightAmount the amount to move to the right
	 * @param maxStairsHeight the maximum height for stairs to climb
	 */
	public void moveHorizontal(final double forwardAmount, final double rightAmount, final double maxStairsHeight) {
		
		// coordinate system note: (leftAngle == 0) means "forward", i.e. towards -Z, with +X pointing right
		final double radians = leftAngle * 2 * Math.PI / 360.0;
		final double sin = Math.sin(radians), cos = Math.cos(radians);
		final double dx = -sin * forwardAmount + cos * rightAmount;
		final double dz = -cos * forwardAmount - sin * rightAmount;
		
		// try to move along x and z independently (for wall sliding). If any one of them failed, try
		// stair climbing.
		boolean xok = checkAndMove(x + dx, y, z);
		boolean zok = checkAndMove(x, y, z + dz);
		if (!xok || !zok) {
			moveUp(maxStairsHeight);
			if (!xok) {
				checkAndMove(x + dx, y, z);
			}
			if (!zok) {
				checkAndMove(x, y, z + dz);
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
	public boolean moveUp(final double amount) {
		return checkAndMove(x, y + amount, z);
	}

	/**
	 * @param nx the new x position
	 * @param ny the new y position
	 * @param nz the new z position
	 * @return whether movement succeeded
	 */
	public boolean checkAndMove(final double nx, final double ny, final double nz) {
		if (!isBlockedAt(nx, ny, nz)) {
			this.x = nx;
			this.y = ny;
			this.z = nz;
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
	public boolean isBlockedAt(final double nx, final double ny, final double nz) {
		return world.getCompositeCollider().collides(new PlayerCollider().createDetailRegion(nx, ny, nz));
	}
	
	/**
	 * Checks whether the player is blocked at his current position
	 * @return true if blocked, false if not
	 */
	public boolean isBlocked() {
		return isBlockedAt(x, y, z);
	}

	/**
	 * 
	 */
	public void dump() {
		double x = ((int)(this.x * 100)) / 100.0; 
		double y = ((int)(this.y * 100)) / 100.0; 
		double z = ((int)(this.z * 100)) / 100.0; 
		System.out.println("position: " + x + ", " + y + ", " + z);
		System.out.println("leftAngle: " + leftAngle);
		System.out.println("upAngle: " + upAngle);
	}

	/**
	 * Handles a game step.
	 * @param frameDurationMultiplier the frame duration multiplier that adapts movement to varying frame rates
	 */
	public void step(double frameDurationMultiplier) {
		
		// observer mode
		if (observerMode) {
			verticalSpeed = 0;
			onGround = false;
			return;
		}

		// apply gravity
		verticalSpeed -= 10.0 * frameDurationMultiplier;
		
		// jumping and landing
		onGround = false;
		justLanded = false;
		if (!moveUp(verticalSpeed * frameDurationMultiplier)) {
			
			// check if just landed (to play a landing sound)
			if (verticalSpeed < -1.0) {
				justLanded = true;
			}
			
			// stay on gound or jump off
			verticalSpeed = 0;
			if (verticalSpeed <= 0 && wantsToJump) {
				verticalSpeed = 8.0;
			} else {
				onGround = true;
			}
			
		}
		
	}

}
