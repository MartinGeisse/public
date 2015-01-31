/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration;

/**
 * This exception gets thrown if there is an error in a configuration file.
 */
public class ConfigurationException extends Exception {

	/**
	 * Constructor.
	 */
	public ConfigurationException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public ConfigurationException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public ConfigurationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
