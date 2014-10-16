/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.picoblaze.simulator.cycle.test;

import static org.junit.Assert.*;

/**
 * This milestone invokes a user-supplied callback, to execute
 * code from the test case during the simulation.
 */
public abstract class CallbackMilestone implements SimulationMilestone {

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.cycle.test.SimulationMilestone#notifyVisibleAsNextMilestone()
	 */
	@Override
	public final boolean notifyVisibleAsNextMilestone() {
		run();
		return true;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.cycle.test.SimulationMilestone#handleBusTransaction(boolean, int, int)
	 */
	@Override
	public final int handleBusTransaction(boolean write, int address, int data) {
		// this milestone should have been removed before bus transactions
		// can occur at all
		fail("handleBusTransaction() called for a CallbackMilestone -- this indicates a bug in the test harness");
		return 0;
	}

	/**
	 * Runs this callback.
	 */
	protected abstract void run();

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(callback)";
	}
	
}
