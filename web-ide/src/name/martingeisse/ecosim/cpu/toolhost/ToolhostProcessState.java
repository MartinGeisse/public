/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.toolhost;

/**
 * The different process states.
 */
public enum ToolhostProcessState {

	/**
	 * The process is running  or waiting to be scheduled. These two states are subsumed
	 * because the toolhost runs each process on a separate virtual CPU, so formally each
	 * process is always running, even though the CPU itself might be waiting to be
	 * scheduled.
	 */
	RUNNING,
	
	/**
	 * The process is sleeping, waiting to be woken up by some event.
	 */
	SLEEPING,
	
	/**
	 * The process has been terminated and exists only to store its own exit status.
	 */
	ZOMBIE,
	
}
