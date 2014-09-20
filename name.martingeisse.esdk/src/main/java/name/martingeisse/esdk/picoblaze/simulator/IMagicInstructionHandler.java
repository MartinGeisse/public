/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.picoblaze.simulator;

/**
 * This interface can be used to implement "magic" 
 * instructions during simulations. These instructions
 * must use otherwise undefined instruction codes.
 * They can be used to implement a simplified simulation
 * by performing complex operations that could never be
 * implemented in a single instruction in real hardware.
 */
public interface IMagicInstructionHandler {

	/**
	 * Handles an instruction that was undefined for the picoblaze
	 * itself.
	 * 
	 * @param state the picoblaze state
	 * @param instructionCode the unknown instruction code
	 * @return true if the instruction was handled, false if not.
	 * Returning false here causes the simulator to throw an
	 * {@link UndefinedInstructionCodeException}.
	 */
	public boolean handleInstruction(PicoblazeState state, int instructionCode);
	
}
