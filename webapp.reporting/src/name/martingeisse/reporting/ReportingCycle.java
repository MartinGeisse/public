/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting;

/**
 * This class wraps an invocation ("cycle") of the reporting system and is
 * used by the client to configure and run the reporting system.
 */
public class ReportingCycle {

	/**
	 * the systemConfiguration
	 */
	private ReportingSystemConfiguration systemConfiguration;
	
	/**
	 * Constructor.
	 */
	public ReportingCycle() {
		this.systemConfiguration = new ReportingSystemConfiguration();
	}
	
	/**
	 * Getter method for the systemConfiguration.
	 * @return the systemConfiguration
	 */
	public ReportingSystemConfiguration getSystemConfiguration() {
		return systemConfiguration;
	}
	
}
