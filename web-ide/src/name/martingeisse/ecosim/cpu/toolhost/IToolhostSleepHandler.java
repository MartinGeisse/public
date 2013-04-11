/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.toolhost;

/**
 * Implementations take action instead of the CPU while a process
 * is sleeping. Note that sleep handlers are called only once
 * for a number of CPU ticks to improve performance.
 */
public interface IToolhostSleepHandler {

	/**
	 * Handles the specified process being asleep.
	 * @param process the process
	 */
	public void handle(ToolhostProcess process);
	
}
