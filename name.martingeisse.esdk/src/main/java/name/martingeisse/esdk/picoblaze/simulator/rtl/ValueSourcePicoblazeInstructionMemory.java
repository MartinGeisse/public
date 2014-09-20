/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.picoblaze.simulator.rtl;

import name.martingeisse.esdk.picoblaze.simulator.IPicoblazeInstructionMemory;
import name.martingeisse.esdk.picoblaze.simulator.PicoblazeSimulatorException;
import name.martingeisse.esdk.util.IValueSource;

/**
 * An {@link IPicoblazeInstructionMemory} implementation that simply
 * returns the value of an {@link IValueSource}. This is typically
 * used together with some mechanism that determines the value of that
 * value source based on the Picoblaze's current PC, taken from
 * {@link RtlPicoblazeSimulator#getPcValueSource()}.
 */
public final class ValueSourcePicoblazeInstructionMemory implements IPicoblazeInstructionMemory {

	/**
	 * the valueSource
	 */
	private final IValueSource<Integer> valueSource;

	/**
	 * Constructor.
	 * @param valueSource the value source
	 */
	public ValueSourcePicoblazeInstructionMemory(IValueSource<Integer> valueSource) {
		this.valueSource = valueSource;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.IPicoblazeInstructionMemory#getInstruction(int)
	 */
	@Override
	public int getInstruction(int address) throws PicoblazeSimulatorException {
		return valueSource.getValue();
	}

}
