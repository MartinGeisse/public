/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.hackathon.application;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;

/**
 * System-wide constants. Some of these are only valid after initialization
 * by the {@link Initializer}.
 */
public class Constants {

	/**
	 * Prevent instantiation.
	 */
	private Constants() {
	}
	
	/**
	 * The time zone used throughout the system.
	 */
	public static DateTimeZone timeZone;
	
	/**
	 * {@link DateTimeFormatter} for the internal date format (e.g. used in the database).
	 */
	public static DateTimeFormatter internalDateFormatter;
	
	/**
	 * {@link DateTimeFormatter} for the internal date-time format (e.g. used in the database).
	 */
	public static DateTimeFormatter internalDateTimeFormatter;

	/**
	 * {@link DateTimeFormatter} for the date format used for logging
	 */
	public static DateTimeFormatter loggingDateFormatter;
	
	/**
	 * {@link DateTimeFormatter} for the date-time format used for logging
	 */
	public static DateTimeFormatter loggingDateTimeFormatter;

	/**
	 * {@link DateTimeFormatter} for the date format used for the user interface
	 */
	public static DateTimeFormatter uiDateFormatter;
	
	/**
	 * {@link DateTimeFormatter} for the date-time format used for the user interface
	 */
	public static DateTimeFormatter uiDateTimeFormatter;

}
