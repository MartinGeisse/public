/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.ingame.player;

import name.martingeisse.stackd.client.engine.WorldWorkingSet;
import org.apache.log4j.Logger;

/**
 * Represents the current player. 
 */
public final class Player extends PlayerBase {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(Player.class);
	
	/**
	 * the verticalSpeed
	 */
	private double verticalSpeed;

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
	 * @param worldWorkingSet the world working set
	 */
	public Player(final WorldWorkingSet worldWorkingSet) {
		super(worldWorkingSet);
		this.observerMode = false;
		this.onGround = false;
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
