/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator;

import java.io.File;
import java.io.IOException;

import name.martingeisse.esdk.picoblaze.PsmBinUtil;

import org.apache.commons.io.FileUtils;

/**
 * Default implementation of {@link IPicoblazeInstructionMemory} that
 * uses a 1024-element int array.
 */
public class PicoblazeInstructionMemory implements IPicoblazeInstructionMemory {

	/**
	 * the instructions
	 */
	private int[] instructions;

	/**
	 * Constructor.
	 */
	public PicoblazeInstructionMemory() {
	}

	/**
	 * Constructor.
	 * @param instructions the instructions
	 */
	public PicoblazeInstructionMemory(final int[] instructions) {
		this.instructions = instructions;
	}

	/**
	 * Getter method for the instructions.
	 * @return the instructions
	 */
	public final int[] getInstructions() {
		return instructions;
	}

	/**
	 * Setter method for the instructions.
	 * @param instructions the instructions to set
	 */
	public final void setInstructions(final int[] instructions) {
		if (instructions.length != 1024) {
			throw new IllegalArgumentException("invalid instruction array length (1024 expected): " + instructions);
		}
		this.instructions = instructions;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.instruction.IPicoblazeInstructionMemory#getInstruction(int)
	 */
	@Override
	public final int getInstruction(int address) throws PicoblazeSimulatorException {
		if (address < 0 || address >= 1024) {
			throw new PicoblazeSimulatorException("invalid instruction address: " + address);
		}
		if (instructions == null) {
			throw new PicoblazeSimulatorException("instructions array is null");
		}
		return instructions[address];
	}

	/**
	 * Creates a {@link PicoblazeInstructionMemory} instance from a .psmbin file.
	 * @param file the file to load
	 * @return the memory instance
	 * @throws IOException on I/O errors
	 */
	public static PicoblazeInstructionMemory createFromPsmBinFile(File file) throws IOException {
		byte[] encodedInstructions = FileUtils.readFileToByteArray(file);
		int[] instructions = PsmBinUtil.decodePsmBin(encodedInstructions);
		return new PicoblazeInstructionMemory(instructions);
	}
	
}
