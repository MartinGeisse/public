/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackerspace.common;

/**
 * Constants for Stackerspace application-specific packets.
 */
public final class StackerspacePacketConstants {

	/**
	 * the TYPE_C2S_UPDATE_POSITION
	 */
	public static final int TYPE_C2S_UPDATE_POSITION = 0x0001;
	
	/**
	 * the TYPE_C2S_RESUME_PLAYER
	 */
	public static final int TYPE_C2S_RESUME_PLAYER = 0x0005;
	
	/**
	 * the TYPE_C2S_DIG_NOTIFICATION
	 */
	public static final int TYPE_C2S_DIG_NOTIFICATION = 0x0006;
	
	/**
	 * the TYPE_S2C_PLAYER_LIST_UPDATE
	 */
	public static final int TYPE_S2C_PLAYER_LIST_UPDATE = 0x0000;
	
	/**
	 * the TYPE_S2C_PLAYER_NAMES_UPDATE
	 */
	public static final int TYPE_S2C_PLAYER_NAMES_UPDATE = 0x0002;

	/**
	 * the TYPE_S2C_PLAYER_RESUMED
	 */
	public static final int TYPE_S2C_PLAYER_RESUMED = 0x0005;

	/**
	 * the TYPE_S2C_UPDATE_COINS
	 */
	public static final int TYPE_S2C_UPDATE_COINS = 0x0006;

	/**
	 * Prevent instantiation.
	 */
	private StackerspacePacketConstants() {
	}
	
}
