/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.picoblaze.simulator.cycle.test;

/**
 * An expected milestone to reach during the simulation of a
 * program. Depending on the concrete subclass, this class
 * performs assertions or side effects.
 */
public interface SimulationMilestone {

	/**
	 * Notifies this milestone that it is now visible
	 * as the next milestone to reach. Depending on the
	 * subclass, this may either mean that the milestone
	 * has been reached (and should be removed to make the
	 * next milestone visible), or that it waits for a
	 * specific event such as a bus transaction.
	 * 
	 * @return true if the milestone has been reached
	 * and should be removed; false if the milestone waits
	 * for an event.
	 */
	public boolean notifyVisibleAsNextMilestone();

	/**
	 * Notifies this milestone that a bus transaction has happened.
	 * @param write whether the operation is a write
	 * @param address the address of the operation
	 * @param data the data written, or 0 for reads
	 * @return 0 for writes, or the data read for reads
	 */
	public int handleBusTransaction(boolean write, int address, int data);
	
}
