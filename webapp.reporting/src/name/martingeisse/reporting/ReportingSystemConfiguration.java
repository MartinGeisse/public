/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting;

import java.io.File;

/**
 * This class represents system-wide configuration that is not directly
 * specified by clients, such as paths.
 */
public class ReportingSystemConfiguration {

	/**
	 * the definitionsFolder
	 */
	private File definitionsFolder;
	
	/**
	 * the documentsFolder
	 */
	private File documentsFolder;
	
	/**
	 * Constructor.
	 */
	public ReportingSystemConfiguration() {
		File dataFolder = new File("data");
		this.definitionsFolder = new File(dataFolder, "definitions");
		this.documentsFolder = new File(dataFolder, "documents");
	}
	
	/**
	 * Getter method for the definitionsFolder.
	 * @return the definitionsFolder
	 */
	public File getDefinitionsFolder() {
		return definitionsFolder;
	}
	
	/**
	 * Getter method for the documentsFolder.
	 * @return the documentsFolder
	 */
	public File getDocumentsFolder() {
		return documentsFolder;
	}
	
}
