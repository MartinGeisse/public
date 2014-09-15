/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package test;

import java.io.File;

import name.martingeisse.esdk.picoblaze.simulator.IPicoblazePortHandler;
import name.martingeisse.esdk.picoblaze.simulator.instruction.InstructionLevelPicoblazeSimulator;
import name.martingeisse.esdk.picoblaze.simulator.instruction.PicoblazeInstructionMemory;

/**
 *
 */
public class PicoblazeMain {

	/**
	 * @param args ...
	 * @throws Exception ...
	 */
	public static void main(String[] args) throws Exception {
		InstructionLevelPicoblazeSimulator sim = new InstructionLevelPicoblazeSimulator();
		sim.setInstructionMemory(PicoblazeInstructionMemory.createFromPsmBinFile(new File("pico-test/test1.psmbin")));
		sim.setPortHandler(new MyPortHandler());
		sim.performMultipleInstructionCycles(100);
	}
	
	private static class MyPortHandler implements IPicoblazePortHandler {

		/* (non-Javadoc)
		 * @see name.martingeisse.esdk.picoblaze.simulator.IPicoblazePortHandler#handleInput(int)
		 */
		@Override
		public int handleInput(int address) {
			return 0;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.esdk.picoblaze.simulator.IPicoblazePortHandler#handleOutput(int, int)
		 */
		@Override
		public void handleOutput(int address, int value) {
			System.out.println("* " + address + " -> " + value);
		}
		
	}
	
}
