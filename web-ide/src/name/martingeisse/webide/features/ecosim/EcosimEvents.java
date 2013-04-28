/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim;

import name.martingeisse.webide.features.ecosim.model.Terminal;

/**
 * Defines names for the simulation events.
 */
public final class EcosimEvents {

	/**
	 * This event is sent from the simulator to all listeners when
	 * output data was sent from the CPU to the terminal. The event
	 * source is the {@link Terminal} for the terminal;
	 * the event data is the current output buffer {@link String}.
	 */
	public static final String TERMINAL_OUTPUT = "ecosim.terminal.output";
	
	/**
	 * Prevent instantiation.
	 */
	private EcosimEvents() {
	}
	
}
