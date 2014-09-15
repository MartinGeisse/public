/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.hdl.core;

/**
 * This interface is implemented by clocked components. Such components
 * work in cycles of two alternating steps. In the first step,
 * each clocked component computes its next state depending on values
 * produced by value sources, which may be provided by other clocked components.
 * These next states are stored in appropriate temporary state variables. In the
 * second step, each clocked component makes the computed next step its current
 * step, thus moving to that next state.
 * 
 * Speaking in terms of a clock-synchronous hardware design, the first
 * step models the time interval between two clock edges, while the second
 * step models the state change at a clock edge.
 * 
 * The general rule is that implementations may sample the values of input
 * value sources only in the first step, and change the values of their
 * outputs (i.e. the values returned by value sources implemented by the
 * clock signal consumer itself) only in the second step. This ensures
 * that the behavior of a clock-synchronous design -- all registers
 * loading a new value at once -- is modeled correctly.
 */
public interface IClockSignalConsumer {

	/**
	 * Computes the next state and remembers it in appropriate
	 * temporary state variables.
	 */
	public void computeNextState();
	
	/**
	 * Enters the next state computed by computeNextState().
	 */
	public void enterNextState();
	
}
