/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Each instance of this class corresponds to a temporary folder for
 * arbitrary purposes. The folder is created by the constructor and
 * removed with dispose(). No automatic removal is done, so clients
 * should use a try/finally block to ensure disposal, unless the folder
 * is intentionally kept (e.g. for diagnostic purposes).
 * 
 * The temporary folder is created as a subfolder of /var/projects/temp/app,
 * where the "app" suffix is application-specific to separate temp folders from
 * different applications. This class must be statically initialized with that
 * folder name once before usage. 
 * 
 * This class expects /var/projects/temp to exist. The application-specific
 * subfolder is re-created cleanly on initialization.
 */
public class TemporaryFolder {

	/**
	 * the applicationFolder
	 */
	private static File applicationFolder = null;

	/**
	 * the count
	 */
	private static long count = 0;

	/**
	 * the instanceId
	 */
	private long instanceId;

	/**
	 * the instanceFolder
	 */
	private File instanceFolder;

	/**
	 * Initializes the temporary folder system for the specified application.
	 * The application identifier is used as the name of the subfolder inside
	 * /var/projects/temp.
	 * @param applicationIdentifier the application-specific identifier / folder name
	 */
	public static synchronized void initialize(String applicationIdentifier) {
		applicationFolder = new File("/var/projects/temp/" + applicationIdentifier);
		try {
			FileUtils.deleteDirectory(applicationFolder);
		} catch (IOException e) {
			throw new RuntimeException("cannot delete previous temporary application folder: " + applicationFolder.getAbsolutePath());
		}
		if (!applicationFolder.mkdir()) {
			throw new RuntimeException("cannot create temporary application folder: " + applicationFolder.getAbsolutePath());
		}
	}

	/**
	 * Allocates a sequential ID number for an instance folder.
	 * @return the id number
	 */
	private static synchronized long allocateId() {
		final long id = count;
		count++;
		return id;
	}

	/**
	 * Constructor.
	 */
	public TemporaryFolder() {
		this.instanceId = allocateId();
		this.instanceFolder = new File(applicationFolder, "x" + instanceId);
		if (!instanceFolder.mkdir()) {
			throw new RuntimeException("cannot create temporary folder: " + instanceFolder.getAbsolutePath());
		}
	}

	/**
	 * Getter method for the instanceId.
	 * @return the instanceId
	 */
	public long getInstanceId() {
		return instanceId;
	}

	/**
	 * Getter method for the instanceFolder.
	 * @return the instanceFolder
	 */
	public File getInstanceFolder() {
		return instanceFolder;
	}
	
	/**
	 * Disposes of this folder. The id property is still valid after disposal,
	 * but the temporary folder itself is gone.
	 */
	public void dispose() {
		try {
			FileUtils.deleteDirectory(instanceFolder);
		} catch (IOException e) {
			throw new RuntimeException("cannot delete temporary folder: " + instanceFolder.getAbsolutePath());
		}
		instanceFolder = null;
	}

}
