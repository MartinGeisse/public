/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.configuration.storage;

/**
 * Indicates failure of the configuration storage.
 */
public class StorageException extends Exception {

	/**
	 * Constructor.
	 */
	public StorageException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public StorageException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public StorageException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}

}
