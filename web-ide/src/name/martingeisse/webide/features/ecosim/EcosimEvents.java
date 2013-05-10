/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim;

import name.martingeisse.ecosim.devices.chardisplay.CharacterDisplayController;
import name.martingeisse.ecosim.devices.chardisplay.ICharacterDisplayHost;
import name.martingeisse.webide.features.ecosim.debugout.DebugOutputController;
import name.martingeisse.webide.features.ecosim.terminal.Terminal;

/**
 * Defines names for the simulation events.
 */
public final class EcosimEvents {

	/**
	 * This event is sent from the simulator to all listeners when
	 * output data was sent from the CPU to the terminal. The event
	 * source is the {@link Terminal} for the terminal;
	 * the event data is null.
	 */
	public static final String TERMINAL_OUTPUT = "ecosim.terminal.output";
	
	/**
	 * This event is sent from the simulator to all listeners when
	 * output data was sent from the CPU to the debug output log. The event
	 * source is the {@link DebugOutputController};
	 * the event data is null.
	 */
	public static final String DEBUG_OUTPUT = "ecosim.debugout.output";
	
	/**
	 * This event is sent from the simulator to all listeners when
	 * output data was sent from the CPU to the character display. The event
	 * source is the {@link ICharacterDisplayHost} (typically a
	 * {@link CharacterDisplayController}); the event data is null.
	 */
	public static final String CHARACTER_DISPLAY_OUTPUT = "ecosim.chardisplay.output";
	
	/**
	 * Prevent instantiation.
	 */
	private EcosimEvents() {
	}
	
}
