/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator.core;

/**
 * This interace can be implemented by code outside the picoblaze to
 * trigger an interrupt.
 */
public interface IPicoblazeInterruptSignal {

	/**
	 * @return true if an interrupt is pending, false if not
	 */
	public boolean isInterruptPending();
	
}
