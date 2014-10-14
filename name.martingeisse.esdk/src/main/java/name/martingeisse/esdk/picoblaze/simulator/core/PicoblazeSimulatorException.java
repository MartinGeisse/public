/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator.core;

/**
 * General exception supertype for any exception that occurs
 * in the PicoBlaze model and indicates that modeling the
 * real PicoBlaze has failed.
 */
public class PicoblazeSimulatorException extends Exception {

	/**
	 * Constructor.
	 */
	public PicoblazeSimulatorException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public PicoblazeSimulatorException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause the exception that caused this exception
	 */
	public PicoblazeSimulatorException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param cause the exception that caused this exception
	 */
	public PicoblazeSimulatorException(String message, Throwable cause) {
		super(message, cause);
	}

}
