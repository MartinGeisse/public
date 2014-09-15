/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.hdl.core;

import java.util.HashSet;
import java.util.Set;

/**
 * This class keeps a set of clock signal consumers and is
 * able to send a simulated clock signal to them.
 * @see IClockSignalConsumer for details on how clock signals are modeled
 */
public class ClockNetwork {

	/**
	 * the consumers
	 */
	private Set<IClockSignalConsumer> consumers;
	
	/**
	 * Constructor
	 */
	public ClockNetwork() {
		this.consumers = new HashSet<IClockSignalConsumer>();
	}
	
	/**
	 * Adds a clock signal consumer to this clock source.
	 * @param consumer the consumer to add
	 */
	public void addConsumer(IClockSignalConsumer consumer) {
		consumers.add(consumer);
	}
	
	/**
	 * Removes a clock signal consumer from this clock source.
	 * @param consumer the consumer to remove
	 */
	public void removeConsumer(IClockSignalConsumer consumer) {
		consumers.remove(consumer);
	}
	
	/**
	 * Invokes computeNextState() on all registered consumers.
	 */
	public void computeNextState() {
		for (IClockSignalConsumer consumer : consumers) {
			consumer.computeNextState();
		}
	}
	
	/**
	 * Invokes enterNextState() on all registered consumers.
	 */
	public void enterNextState() {
		for (IClockSignalConsumer consumer : consumers) {
			consumer.enterNextState();
		}
	}
	
	/**
	 * Performs a whole clock cycle by invoking computeNextState(), then
	 * enterNextState() on all registered consumers.
	 */
	public void performClockCycle() {
		computeNextState();
		enterNextState();
	}
	
}
