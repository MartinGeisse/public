/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Thread-related utility methods.
 */
public final class ThreadUtil {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(ThreadUtil.class);
	
	/**
	 * Prevent instantiation.
	 */
	private ThreadUtil() {
	}

	/**
	 * Logs the currently active threads at the specified level.
	 * @param level the log level
	 */
	public static void dumpThreads(Level level) {
		logger.log(level, "Active threads:");
		int activeThreadCount = Thread.activeCount();
		Thread[] activeThreads = new Thread[activeThreadCount];
		activeThreadCount = Thread.enumerate(activeThreads);
		for (int i=0; i<activeThreadCount; i++) {
			Thread thread = activeThreads[i];
			logger.log(level, thread.getName());
		}
	}
	
}
