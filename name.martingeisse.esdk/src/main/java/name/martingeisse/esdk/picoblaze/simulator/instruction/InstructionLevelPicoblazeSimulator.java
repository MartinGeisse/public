/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator.instruction;

import java.io.File;
import java.io.IOException;

import name.martingeisse.esdk.picoblaze.simulator.PicoblazeSimulatorException;
import name.martingeisse.esdk.picoblaze.simulator.PicoblazeState;
import name.martingeisse.esdk.picoblaze.simulator.port.IPicoblazePortHandler;

/**
 * An instruction-level simulated PicoBlaze instance.
 */
public final class InstructionLevelPicoblazeSimulator {

	/**
	 * the instructionMemory
	 */
	private IPicoblazeInstructionMemory instructionMemory;

	/**
	 * the interruptSignal
	 */
	private IPicoblazeInterruptSignal interruptSignal;

	/**
	 * the state
	 */
	private PicoblazeState state;

	/**
	 * Constructor.
	 */
	public InstructionLevelPicoblazeSimulator() {
		this.state = new PicoblazeState();
	}

	/**
	 * Getter method for the instructionMemory.
	 * @return the instructionMemory
	 */
	public IPicoblazeInstructionMemory getInstructionMemory() {
		return instructionMemory;
	}

	/**
	 * Setter method for the instructionMemory.
	 * @param instructionMemory the instructionMemory to set
	 */
	public void setInstructionMemory(final IPicoblazeInstructionMemory instructionMemory) {
		this.instructionMemory = instructionMemory;
	}
	
	/**
	 * Loads the specified PSMBIN file and creates and uses an instruction memory
	 * from the instructions contained in the file.
	 * @param file the file to load
	 * @throws IOException on I/O errors
	 */
	public void setInstructionMemoryFromPsmBinFile(File file) throws IOException {
		setInstructionMemory(PicoblazeInstructionMemory.createFromPsmBinFile(file));
	}

	/**
	 * Getter method for the interruptSignal.
	 * @return the interruptSignal
	 */
	public IPicoblazeInterruptSignal getInterruptSignal() {
		return interruptSignal;
	}

	/**
	 * Setter method for the interruptSignal.
	 * @param interruptSignal the interruptSignal to set
	 */
	public void setInterruptSignal(final IPicoblazeInterruptSignal interruptSignal) {
		this.interruptSignal = interruptSignal;
	}

	/**
	 * Getter method for the state.
	 * @return the state
	 */
	public PicoblazeState getState() {
		return state;
	}

	/**
	 * Setter method for the state.
	 * @param state the state to set
	 */
	public void setState(final PicoblazeState state) {
		if (state == null) {
			throw new IllegalArgumentException("cannot set the state to null");
		}
		this.state = state;
	}

	/**
	 * Convenience getter method for the portHandler of the state.
	 * @return the portHandler
	 */
	public IPicoblazePortHandler getPortHandler() {
		return state.getPortHandler();
	}

	/**
	 * Convenience setter method for the portHandler of the state.
	 * @param portHandler the portHandler to set
	 */
	public void setPortHandler(IPicoblazePortHandler portHandler) {
		state.setPortHandler(portHandler);
	}

	/**
	 * Performs a single instruction cycle. This either executes one instruction or
	 * performs an interrupt entry, depending on the interrupt signal. If an instruction
	 * is executed, a program memory is needed and used. Depending on the instruction,
	 * a port handler is also needed and used.
	 * @throws PicoblazeSimulatorException when this model fails
	 */
	public void performInstructionCycle() throws PicoblazeSimulatorException {
		if (interruptSignal != null && interruptSignal.isActive()) {
			state.performInterrupt();
		} else if (instructionMemory == null) {
			throw new PicoblazeSimulatorException("no instruction memory");
		} else {
			state.performInstruction(instructionMemory.getInstruction(state.getPc()));
		}
	}
	
	/**
	 * Performs the specified number of instruction cycles.
	 * @param count the number of instruction cycles to perform
	 * @throws PicoblazeSimulatorException when this model fails
	 */
	public void performMultipleInstructionCycles(int count) throws PicoblazeSimulatorException {
		for (int i=0; i<count; i++) {
			performInstructionCycle();
		}
	}
	
}
