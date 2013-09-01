/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.mobsc.application;

import name.martingeisse.common.javascript.JavascriptAssembler;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

/**
 * This class initializes various parts of the system through a single
 * static method.
 */
public class Initializer {

	/**
	 * Prevent instantiation.
	 */
	private Initializer() {
	}
	
	/**
	 * Initializes the system.
	 */
	public static void initialize() {

		// initialize time zone
		Constants.timeZone = DateTimeZone.forID("Europe/Berlin");
		Constants.internalDateFormatter = JavascriptAssembler.defaultDateFormatter = DateTimeFormat.forPattern("YYYY-MM-dd").withZone(Constants.timeZone);
		Constants.internalDateTimeFormatter = JavascriptAssembler.defaultDateTimeFormatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss").withZone(Constants.timeZone);
		Constants.loggingDateFormatter = DateTimeFormat.forPattern("dd.MM.YYYY").withZone(Constants.timeZone);
		Constants.loggingDateTimeFormatter = DateTimeFormat.forPattern("dd.MM.YYYY HH:mm:ss").withZone(Constants.timeZone);
		Constants.uiDateFormatter = Constants.loggingDateFormatter;
		Constants.uiDateTimeFormatter = Constants.loggingDateTimeFormatter;
		
	}
	
}
