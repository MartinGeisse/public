/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.picoblaze.simulator;

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
