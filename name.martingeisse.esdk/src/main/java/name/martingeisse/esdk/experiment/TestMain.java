/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.experiment;

import name.martingeisse.esdk.picoblaze.simulator.AssociatedPicoblazeSourceCode;
import name.martingeisse.esdk.picoblaze.simulator.IMagicInstructionHandler;
import name.martingeisse.esdk.picoblaze.simulator.InstructionCyclePicoblazeSimulator;
import name.martingeisse.esdk.picoblaze.simulator.PicoblazeState;

/**
 * TODO: document me
 *
 */
public class TestMain {

	public static void main(String[] args) throws Exception {
		AssociatedPicoblazeSourceCode program = new AssociatedPicoblazeSourceCode(TestMain.class);
		InstructionCyclePicoblazeSimulator simulator = new InstructionCyclePicoblazeSimulator();
		simulator.setInstructionMemory(program);
		simulator.setMagicInstructionHandler(new IMagicInstructionHandler() {
			@Override
			public boolean handleInstruction(PicoblazeState state, int instructionCode) {
				System.out.println("* " + state.getRegisterValue(instructionCode));
				return true;
			}
		});
		simulator.performInfiniteInstructionCycles();
	}
	
}