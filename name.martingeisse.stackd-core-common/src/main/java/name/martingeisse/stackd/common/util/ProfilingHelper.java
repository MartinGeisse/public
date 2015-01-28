/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.util;

import org.apache.log4j.Logger;

/**
 * Simple helper class for code profiling. This uses three methods.
 * The start() method starts a timer. The check() method outputs
 * the number of milliseconds since the timer was started or
 * last checked. The checkTotal() method always outputs the
 * number of milliseconds since the timer was started, ignoring
 * previous check() or checkTotal() calls.
 */
public class ProfilingHelper {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(ProfilingHelper.class);
	
	/**
	 * the startTime
	 */
	private static long startTime;
	
	/**
	 * the checkTime
	 */
	private static long checkTime;
	
	/**
	 * 
	 */
	public static void start() {
		startTime = checkTime = System.nanoTime();
	}
	
	/**
	 * @param description a description string to output
	 */
	public static void check(String description) {
		long newCheckTime = System.nanoTime();
		print(description, newCheckTime - checkTime, 0);
		checkTime = newCheckTime;
	}

	/**
	 * @param description a description string to output
	 */
	public static void checkTotal(String description) {
		print(description, System.nanoTime() - startTime, 0);
	}
	
	/**
	 * @param description a description string to output
	 */
	public static void checkRelevant(String description) {
		long newCheckTime = System.nanoTime();
		print(description, newCheckTime - checkTime, 1);
		checkTime = newCheckTime;
	}
	
	/**
	 * @param description a description string to output
	 */
	public static void checkRelevantTotal(String description) {
		print(description, System.nanoTime() - startTime, 1);
	}
	
	/**
	 * @param description a description string to output
	 * @param threshold the minimum number of milliseconds needed to print a message
	 */
	public static void checkRelevant(String description, int threshold) {
		long newCheckTime = System.nanoTime();
		print(description, newCheckTime - checkTime, threshold);
		checkTime = newCheckTime;
	}
	
	/**
	 * @param description a description string to output
	 * @param threshold the minimum number of milliseconds needed to print a message
	 */
	public static void checkRelevantTotal(String description, int threshold) {
		print(description, System.nanoTime() - startTime, threshold);
	}

	/**
	 * 
	 */
	private static void print(String description, long deltaNanos, int threshold) {
		long deltaMillis = deltaNanos / 1000000;
		if (deltaMillis >= threshold) {
			logger.info(description + ": " + deltaMillis);
		}
	}
	
}
