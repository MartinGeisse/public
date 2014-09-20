/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator.rtl;

import name.martingeisse.esdk.picoblaze.simulator.PicoblazeSimulatorBase;
import name.martingeisse.esdk.picoblaze.simulator.PicoblazeSimulatorException;
import name.martingeisse.esdk.rtl.IClockSignalConsumer;
import name.martingeisse.esdk.util.IValueSource;

/**
 * An RTL-simulated PicoBlaze instance.
 */
public final class RtlPicoblazeSimulator extends PicoblazeSimulatorBase implements IClockSignalConsumer {

	/**
	 * the pcValueSource
	 */
	private final IValueSource<Integer> pcValueSource = new IValueSource<Integer>() {
		@Override
		public Integer getValue() {
			return getState().getPc();
		}
	};
	
	/**
	 * the secondInstructionClockCycle
	 */
	private boolean secondInstructionClockCycle = false;
	
	/**
	 * the nextInstruction
	 */
	private int nextInstruction;
	
	/**
	 * Getter method for the pcValueSource.
	 * @return the pcValueSource
	 */
	public IValueSource<Integer> getPcValueSource() {
		return pcValueSource;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.rtl.IClockSignalConsumer#computeNextState()
	 */
	@Override
	public void computeNextState() {
		try {
			nextInstruction = getInstructionMemory().getInstruction(getState().getPc());
		} catch (PicoblazeSimulatorException e) {
			throw new RuntimeException("unexpected exception", e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.rtl.IClockSignalConsumer#enterNextState()
	 */
	@Override
	public void enterNextState() {
		if (!secondInstructionClockCycle) {
			// TODO cannot handle this correctly -- the instruction must be performed
			// in two separate steps
		} else {
			// TODO
		}
	}

}
