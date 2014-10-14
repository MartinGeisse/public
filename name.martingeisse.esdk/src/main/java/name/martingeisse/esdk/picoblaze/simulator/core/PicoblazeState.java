/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator.core;

import name.martingeisse.esdk.picoblaze.simulator.magic.IMagicInstructionHandler;
import name.martingeisse.esdk.picoblaze.simulator.port.IPicoblazePortHandler;

/**
 * An abstraction level-neutral representation of the internal state of a PicoBlaze
 * instance. This state model can be used both for simulating a PB program within
 * an instruction-level simulator and simulating the PB in a hardware system at
 * register transfer level.
 * 
 * Note that the instruction store is *NOT* part of this model.
 */
public final class PicoblazeState {

	/**
	 * the registers
	 */
	private final byte[] registers;

	/**
	 * the zero
	 */
	private boolean zero;

	/**
	 * the carry
	 */
	private boolean carry;

	/**
	 * the ram
	 */
	private final byte[] ram;

	/**
	 * the stack
	 */
	private final int[] returnStack;

	/**
	 * the returnStackPointer
	 */
	private int returnStackPointer;

	/**
	 * the interruptEnable
	 */
	private boolean interruptEnable;

	/**
	 * the preservedZero
	 */
	private boolean preservedZero;

	/**
	 * the preservedCarry
	 */
	private boolean preservedCarry;

	/**
	 * the portHandler
	 */
	private IPicoblazePortHandler portHandler;
	
	/**
	 * the magicInstructionHandler
	 */
	private IMagicInstructionHandler magicInstructionHandler;

	/**
	 * Constructor.
	 */
	public PicoblazeState() {
		this.registers = new byte[16];
		this.ram = new byte[64];
		this.returnStack = new int[32];
	}

	/**
	 * Resets the PB state. Specifically, this sets the PC to 0 and clears the
	 * INTERRUPT ENABLE, CARRY and ZERO flags. Other state is not affected.
	 */
	public void reset() {
		setPc(0);
		setInterruptEnable(false);
		setZero(false);
		setCarry(false);
	}

	/**
	 * Getter method for the pc.
	 * @return the pc
	 */
	public int getPc() {
		return (returnStack[returnStackPointer] & 1023);
	}

	/**
	 * Setter method for the pc.
	 * @param pc the pc to set
	 */
	public void setPc(final int pc) {
		this.returnStack[returnStackPointer] = (pc & 1023);
	}

	/**
	 * Getter method for the zero.
	 * @return the zero
	 */
	public boolean isZero() {
		return zero;
	}

	/**
	 * Setter method for the zero.
	 * @param zero the zero to set
	 */
	public void setZero(final boolean zero) {
		this.zero = zero;
	}

	/**
	 * Getter method for the carry.
	 * @return the carry
	 */
	public boolean isCarry() {
		return carry;
	}

	/**
	 * Setter method for the carry.
	 * @param carry the carry to set
	 */
	public void setCarry(final boolean carry) {
		this.carry = carry;
	}

	/**
	 * Getter method for the returnStackPointer.
	 * @return the returnStackPointer
	 */
	public int getReturnStackPointer() {
		return returnStackPointer;
	}

	/**
	 * Setter method for the returnStackPointer.
	 * @param returnStackPointer the returnStackPointer to set
	 */
	public void setReturnStackPointer(final int returnStackPointer) {
		this.returnStackPointer = (returnStackPointer & 31);
	}

	/**
	 * Getter method for the registers.
	 * @return the registers
	 */
	public byte[] getRegisters() {
		return registers;
	}

	/**
	 * Getter method for the ram.
	 * @return the ram
	 */
	public byte[] getRam() {
		return ram;
	}

	/**
	 * Getter method for the returnStack.
	 * @return the returnStack
	 */
	public int[] getReturnStack() {
		return returnStack;
	}

	/**
	 * Getter method for the interruptEnable.
	 * @return the interruptEnable
	 */
	public boolean isInterruptEnable() {
		return interruptEnable;
	}

	/**
	 * Setter method for the interruptEnable.
	 * @param interruptEnable the interruptEnable to set
	 */
	public void setInterruptEnable(final boolean interruptEnable) {
		this.interruptEnable = interruptEnable;
	}

	/**
	 * Getter method for the preservedZero.
	 * @return the preservedZero
	 */
	public boolean isPreservedZero() {
		return preservedZero;
	}

	/**
	 * Setter method for the preservedZero.
	 * @param preservedZero the preservedZero to set
	 */
	public void setPreservedZero(final boolean preservedZero) {
		this.preservedZero = preservedZero;
	}

	/**
	 * Getter method for the preservedCarry.
	 * @return the preservedCarry
	 */
	public boolean isPreservedCarry() {
		return preservedCarry;
	}

	/**
	 * Setter method for the preservedCarry.
	 * @param preservedCarry the preservedCarry to set
	 */
	public void setPreservedCarry(final boolean preservedCarry) {
		this.preservedCarry = preservedCarry;
	}

	/**
	 * Getter method for the portHandler.
	 * @return the portHandler
	 */
	public IPicoblazePortHandler getPortHandler() {
		return portHandler;
	}

	/**
	 * Setter method for the portHandler.
	 * @param portHandler the portHandler to set
	 */
	public void setPortHandler(final IPicoblazePortHandler portHandler) {
		this.portHandler = portHandler;
	}

	/**
	 * Getter method for the magicInstructionHandler.
	 * @return the magicInstructionHandler
	 */
	public IMagicInstructionHandler getMagicInstructionHandler() {
		return magicInstructionHandler;
	}
	
	/**
	 * Setter method for the magicInstructionHandler.
	 * @param magicInstructionHandler the magicInstructionHandler to set
	 */
	public void setMagicInstructionHandler(IMagicInstructionHandler magicInstructionHandler) {
		this.magicInstructionHandler = magicInstructionHandler;
	}
	
	/**
	 * Returns the value of the specified register
	 * @param registerNumber the register number (only the lowest four bits are considered)
	 * @return the value of the register, in the range 0-255
	 */
	public int getRegisterValue(final int registerNumber) {
		return (registers[registerNumber & 15] & 0xff);
	}

	/**
	 * Sets the value of the specified register
	 * @param registerNumber the register number (only the lowest four bits are considered)
	 * @param value the value to set (only the lowest eight bits are considered)
	 */
	public void setRegisterValue(final int registerNumber, final int value) {
		registers[registerNumber & 15] = (byte)value;
	}

	/**
	 * Returns the value of the specified RAM cell
	 * @param address the RAM address (only the lowest six bits are considered)
	 * @return the value of the RAM cell, in the range 0-255
	 */
	public int getRamValue(final int address) {
		return (ram[address & 63] & 0xff);
	}

	/**
	 * Sets the value of the RAM cell.
	 * @param address the RAM address (only the lowest six bits are considered)
	 * @param value the value to set (only the lowest eight bits are considered)
	 */
	public void setRamValue(final int address, final int value) {
		ram[address & 63] = (byte)value;
	}

	/**
	 * Inputs a value from the specified port.
	 * @param port the port to input from (only the lowest eight bits are considered)
	 * @return the value taken from the port, in the range 0-255
	 * @throws PicoblazeSimulatorException when this model fails
	 */
	public int input(final int port) throws PicoblazeSimulatorException {
		if (portHandler == null) {
			throw new PicoblazeSimulatorException("no port handler");
		}
		return (portHandler.handleInput(port & 0xff) & 0xff);
	}

	/**
	 * Outputs a value to the specified port.
	 * @param port the port to output to (only the lowest eight bits are considered)
	 * @param value the value to output (only the lowest eight bits are considered)
	 * @throws PicoblazeSimulatorException when this model fails
	 */
	public void output(final int port, final int value) throws PicoblazeSimulatorException {
		if (portHandler == null) {
			throw new PicoblazeSimulatorException("no port handler");
		}
		portHandler.handleOutput(port & 0xff, value & 0xff);
	}

	/**
	 * Returns the byte value (0-255) of either a register or the specified value.
	 * @param x the register number or immediate value
	 * @param immediate whether the value is immediate
	 * @return the byte value
	 */
	int getRegisterOrImmediate(final int x, final boolean immediate) {
		return (immediate ? (x & 0xff) : getRegisterValue(x));
	}
	
	/**
	 * Increments the PC register by 1.
	 */
	void incrementPc() {
		setPc(getPc() + 1);
	}

	/**
	 * Performs an ADD operation
	 * @param leftOperand the left operand register number
	 * @param rightOperand the right operand register number or immediate value
	 * @param rightOperandIsImmediate whether the right operand is specified as an immediate value (true)
	 * or as a register number (false)
	 */
	public void performAdd(final int leftOperand, final int rightOperand, final boolean rightOperandIsImmediate) {
		final int result = (getRegisterValue(leftOperand) + getRegisterOrImmediate(rightOperand, rightOperandIsImmediate));
		setRegisterValue(leftOperand, result);
		setZero((result & 0xff) == 0);
		setCarry(result > 255);
		incrementPc();
	}

	/**
	 * Performs an ADDCY operation
	 * @param leftOperand the left operand register number
	 * @param rightOperand the right operand register number or immediate value
	 * @param rightOperandIsImmediate whether the right operand is specified as an immediate value (true)
	 * or as a register number (false)
	 */
	public void performAddWithCarry(final int leftOperand, final int rightOperand, final boolean rightOperandIsImmediate) {
		final int result = (getRegisterValue(leftOperand) + getRegisterOrImmediate(rightOperand, rightOperandIsImmediate) + (carry ? 1 : 0));
		setRegisterValue(leftOperand, result);
		setZero((result & 0xff) == 0);
		setCarry(result > 255);
		incrementPc();
	}

	/**
	 * Performs a SUB operation
	 * @param leftOperand the left operand register number
	 * @param rightOperand the right operand register number or immediate value
	 * @param rightOperandIsImmediate whether the right operand is specified as an immediate value (true)
	 * or as a register number (false)
	 */
	public void performSub(final int leftOperand, final int rightOperand, final boolean rightOperandIsImmediate) {
		final int result = (getRegisterValue(leftOperand) - getRegisterOrImmediate(rightOperand, rightOperandIsImmediate));
		setRegisterValue(leftOperand, result);
		setZero((result & 0xff) == 0);
		setCarry(result < 0);
		incrementPc();
	}

	/**
	 * Performs a SUBCY operation
	 * @param leftOperand the left operand register number
	 * @param rightOperand the right operand register number or immediate value
	 * @param rightOperandIsImmediate whether the right operand is specified as an immediate value (true)
	 * or as a register number (false)
	 */
	public void performSubWithCarry(final int leftOperand, final int rightOperand, final boolean rightOperandIsImmediate) {
		final int result = (getRegisterValue(leftOperand) - getRegisterOrImmediate(rightOperand, rightOperandIsImmediate) - (carry ? 1 : 0));
		setRegisterValue(leftOperand, result);
		setZero((result & 0xff) == 0);
		setCarry(result < 0);
		incrementPc();
	}

	/**
	 * Performs an AND operation
	 * @param leftOperand the left operand register number
	 * @param rightOperand the right operand register number or immediate value
	 * @param rightOperandIsImmediate whether the right operand is specified as an immediate value (true)
	 * or as a register number (false)
	 */
	public void performAnd(final int leftOperand, final int rightOperand, final boolean rightOperandIsImmediate) {
		final int result = (getRegisterValue(leftOperand) & getRegisterOrImmediate(rightOperand, rightOperandIsImmediate));
		setRegisterValue(leftOperand, result);
		setZero((result & 0xff) == 0);
		setCarry(false);
		incrementPc();
	}

	/**
	 * Performs an OR operation
	 * @param leftOperand the left operand register number
	 * @param rightOperand the right operand register number or immediate value
	 * @param rightOperandIsImmediate whether the right operand is specified as an immediate value (true)
	 * or as a register number (false)
	 */
	public void performOr(final int leftOperand, final int rightOperand, final boolean rightOperandIsImmediate) {
		final int result = (getRegisterValue(leftOperand) | getRegisterOrImmediate(rightOperand, rightOperandIsImmediate));
		setRegisterValue(leftOperand, result);
		setZero((result & 0xff) == 0);
		setCarry(false);
		incrementPc();
	}

	/**
	 * Performs an XOR operation
	 * @param leftOperand the left operand register number
	 * @param rightOperand the right operand register number or immediate value
	 * @param rightOperandIsImmediate whether the right operand is specified as an immediate value (true)
	 * or as a register number (false)
	 */
	public void performXor(final int leftOperand, final int rightOperand, final boolean rightOperandIsImmediate) {
		final int result = (getRegisterValue(leftOperand) ^ getRegisterOrImmediate(rightOperand, rightOperandIsImmediate));
		setRegisterValue(leftOperand, result);
		setZero((result & 0xff) == 0);
		setCarry(false);
		incrementPc();
	}

	/**
	 * Returns the XOR of the lowest eight bits of the specified value as
	 * a boolean value
	 * @param value the value containing the bits
	 * @return true if the XOR result is 1, false if it is 0
	 */
	private boolean computeByteXor(int value) {
		value ^= (value >> 4);
		value ^= (value >> 2);
		value ^= (value >> 1);
		return ((value & 1) != 0);
	}

	/**
	 * Performs a TEST operation
	 * @param leftOperand the left operand register number
	 * @param rightOperand the right operand register number or immediate value
	 * @param rightOperandIsImmediate whether the right operand is specified as an immediate value (true)
	 * or as a register number (false)
	 */
	public void performTest(final int leftOperand, final int rightOperand, final boolean rightOperandIsImmediate) {
		final int temp = (getRegisterValue(leftOperand) & getRegisterOrImmediate(rightOperand, rightOperandIsImmediate));
		setZero((temp & 0xff) == 0);
		setCarry(computeByteXor(temp));
		incrementPc();
	}

	/**
	 * Performs a COMPARE operation
	 * @param leftOperand the left operand register number
	 * @param rightOperand the right operand register number or immediate value
	 * @param rightOperandIsImmediate whether the right operand is specified as an immediate value (true)
	 * or as a register number (false)
	 */
	public void performCompare(final int leftOperand, final int rightOperand, final boolean rightOperandIsImmediate) {
		final int temp = (getRegisterValue(leftOperand) - getRegisterOrImmediate(rightOperand, rightOperandIsImmediate));
		setZero(temp == 0);
		setCarry(temp < 0);
		incrementPc();
	}

	/**
	 * @param operand the operand register number
	 * @param shiftInValue true to shift in a 1 bit, false to shift in a 0 bit
	 */
	private void performShiftLeft(final int operand, final boolean shiftInValue) {
		int x = getRegisterValue(operand);
		setCarry((x & 128) != 0);
		x = ((x << 1) | (shiftInValue ? 1 : 0));
		setRegisterValue(operand, x);
		setZero((x & 0xff) == 0);
		incrementPc();
	}

	/**
	 * Performs a SL0 (shift left and fill with 0) operation
	 * @param operand the operand register number
	 */
	public void performShiftLeftZero(final int operand) {
		performShiftLeft(operand, false);
	}

	/**
	 * Performs a SL1 (shift left and fill with 1) operation
	 * @param operand the operand register number
	 */
	public void performShiftLeftOne(final int operand) {
		performShiftLeft(operand, true);
	}

	/**
	 * Performs a SLX (shift left and replicate bit 0) operation
	 * @param operand the operand register number
	 */
	public void performShiftLeftReplicate(final int operand) {
		performShiftLeft(operand, (getRegisterValue(operand) & 1) != 0);
	}

	/**
	 * Performs a SLA (shift left through all bits including CARRY) operation
	 * @param operand the operand register number
	 */
	public void performShiftLeftAll(final int operand) {
		performShiftLeft(operand, carry);
	}

	/**
	 * Performs a RL (rotate left) operation
	 * @param operand the operand register number
	 */
	public void performRotateLeft(final int operand) {
		performShiftLeft(operand, (getRegisterValue(operand) & 128) != 0);
	}

	/**
	 * @param operand the operand register number
	 * @param shiftInValue true to shift in a 1 bit, false to shift in a 0 bit
	 */
	private void performShiftRight(final int operand, final boolean shiftInValue) {
		int x = getRegisterValue(operand);
		setCarry((x & 1) != 0);
		x = ((x >> 1) | (shiftInValue ? 128 : 0));
		setRegisterValue(operand, x);
		setZero((x & 0xff) == 0);
		incrementPc();
	}

	/**
	 * Performs a SL0 (shift right and fill with 0) operation
	 * @param operand the operand register number
	 */
	public void performShiftRightZero(final int operand) {
		performShiftRight(operand, false);
	}

	/**
	 * Performs a SL1 (shift right and fill with 1) operation
	 * @param operand the operand register number
	 */
	public void performShiftRightOne(final int operand) {
		performShiftRight(operand, true);
	}

	/**
	 * Performs a SLX (shift right and replicate bit 0) operation
	 * @param operand the operand register number
	 */
	public void performShiftRightReplicate(final int operand) {
		performShiftRight(operand, (getRegisterValue(operand) & 128) != 0);
	}

	/**
	 * Performs a SLA (shift right through all bits including CARRY) operation
	 * @param operand the operand register number
	 */
	public void performShiftRightAll(final int operand) {
		performShiftRight(operand, carry);
	}

	/**
	 * Performs a RL (rotate right) operation
	 * @param operand the operand register number
	 */
	public void performRotateRight(final int operand) {
		performShiftRight(operand, (getRegisterValue(operand) & 1) != 0);
	}

	/**
	 * Performs a LOAD operation
	 * @param leftOperand the left operand register number
	 * @param rightOperand the right operand register number or immediate value
	 * @param rightOperandIsImmediate whether the right operand is specified as an immediate value (true)
	 * or as a register number (false)
	 */
	public void performLoad(final int leftOperand, final int rightOperand, final boolean rightOperandIsImmediate) {
		setRegisterValue(leftOperand, getRegisterOrImmediate(rightOperand, rightOperandIsImmediate));
		incrementPc();
	}

	/**
	 * Performs a FETCH operation
	 * @param leftOperand the left operand register number
	 * @param rightOperand the right operand register number or immediate value
	 * @param rightOperandIsImmediate whether the right operand is specified as an immediate value (true)
	 * or as a register number (false)
	 */
	public void performFetch(final int leftOperand, final int rightOperand, final boolean rightOperandIsImmediate) {
		setRegisterValue(leftOperand, getRamValue(getRegisterOrImmediate(rightOperand, rightOperandIsImmediate)));
		incrementPc();
	}

	/**
	 * Performs a STORE operation
	 * @param leftOperand the left operand register number
	 * @param rightOperand the right operand register number or immediate value
	 * @param rightOperandIsImmediate whether the right operand is specified as an immediate value (true)
	 * or as a register number (false)
	 */
	public void performStore(final int leftOperand, final int rightOperand, final boolean rightOperandIsImmediate) {
		setRamValue(getRegisterOrImmediate(rightOperand, rightOperandIsImmediate), getRegisterValue(leftOperand));
		incrementPc();
	}

	/**
	 * Performs a INPUT operation
	 * @param leftOperand the left operand register number
	 * @param rightOperand the right operand register number or immediate value
	 * @param rightOperandIsImmediate whether the right operand is specified as an immediate value (true)
	 * or as a register number (false)
	 * @throws PicoblazeSimulatorException when this model fails
	 */
	public void performInput(final int leftOperand, final int rightOperand, final boolean rightOperandIsImmediate) throws PicoblazeSimulatorException {
		setRegisterValue(leftOperand, input(getRegisterOrImmediate(rightOperand, rightOperandIsImmediate)));
		incrementPc();
	}

	/**
	 * Performs a OUTPUT operation
	 * @param leftOperand the left operand register number
	 * @param rightOperand the right operand register number or immediate value
	 * @param rightOperandIsImmediate whether the right operand is specified as an immediate value (true)
	 * or as a register number (false)
	 * @throws PicoblazeSimulatorException when this model fails
	 */
	public void performOutput(final int leftOperand, final int rightOperand, final boolean rightOperandIsImmediate) throws PicoblazeSimulatorException {
		output(getRegisterOrImmediate(rightOperand, rightOperandIsImmediate), getRegisterValue(leftOperand));
		incrementPc();
	}

	/**
	 * Performs a JUMP, JUMP Z, JUMP NZ, JUMP C or JUMP NC instruction, depending on the specified condition.
	 * @param condition the condition that determines whether to jump
	 * @param targetAddress the target address to jump to if the condition is met
	 */
	public void performJump(final PicoblazeJumpCondition condition, final int targetAddress) {
		if (condition.test(zero, carry)) {
			setPc(targetAddress);
		} else {
			incrementPc();
		}
	}

	/**
	 * Performs a CALL, CALL Z, CALL NZ, CALL C or CALL NC instruction, depending on the specified condition.
	 * @param condition the condition that determines whether to call
	 * @param targetAddress the target address to call to if the condition is met
	 */
	public void performCall(final PicoblazeJumpCondition condition, final int targetAddress) {
		if (condition.test(zero, carry)) {
			setReturnStackPointer(getReturnStackPointer() + 1);
			setPc(targetAddress);
		} else {
			incrementPc();
		}
	}

	/**
	 * Performs a RETURN, RETURN Z, RETURN NZ, RETURN C or RETURN NC instruction, depending on the specified condition.
	 * @param condition the condition that determines whether to return
	 */
	public void performReturn(final PicoblazeJumpCondition condition) {
		if (condition.test(zero, carry)) {
			setReturnStackPointer(getReturnStackPointer() - 1);
			incrementPc();
		} else {
			incrementPc();
		}
	}
	
	/**
	 * This method does not correspond to an instruction, but to an asserted interrupt signal.
	 */
	public void performInterrupt() {
		setReturnStackPointer(getReturnStackPointer() + 1);
		setPc(-1);
		setPreservedZero(isZero());
		setPreservedCarry(isCarry());
		setInterruptEnable(false);
	}
	
	/**
	 * Performs a RETURNI ENABLE or RETURNI DISABLE instruction, depending on the specified enable flag.
	 * @param enable whether to enable or disable interrupts after returning
	 */
	public void performReturnInterrupt(boolean enable) {
		setReturnStackPointer(getReturnStackPointer() - 1);
		setZero(isPreservedZero());
		setCarry(isPreservedCarry());
		setInterruptEnable(enable);
	}

	/**
	 * Performs a DISABLE INTERRUPT or ENABLE INTERRUPT instruction, depending on the specified
	 * enable flag.
	 * @param enable whether to enable or disable interrupts
	 */
	public void performSetInterruptEnable(boolean enable) {
		setInterruptEnable(enable);
		incrementPc();
	}
	
	/**
	 * Performs a shift or rotate instruction. This method is used for
	 * all types of shift and rotate, left and right.
	 * @param encodedInstruction the encoded instruction
	 * @param operand the (left) operand register number. This could actually be
	 * obtained from the encoded instruction, but isn't for performance reasons.
	 */
	private void performShiftOrRotateInstruction(int encodedInstruction, int operand) throws PicoblazeSimulatorException {
		int subOpcode = encodedInstruction & 15;
		switch (subOpcode) {
			
		case 0:
			performShiftLeftAll(operand);
			break;
			
		case 2:
			performRotateLeft(operand);
			break;
			
		case 4:
			performShiftLeftReplicate(operand);
			break;
			
		case 6:
			performShiftLeftZero(operand);
			break;
			
		case 7:
			performShiftLeftOne(operand);
			break;
			
		case 8:
			performShiftRightAll(operand);
			break;
			
		case 10:
			performShiftRightReplicate(operand);
			break;
			
		case 12:
			performRotateRight(operand);
			break;
			
		case 14:
			performShiftRightZero(operand);
			break;
			
		case 15:
			performShiftRightOne(operand);
			break;

		// undefined shift sub-opcodes: 1, 3, 5, 9, 11, 13
		default:
			undefinedInstruction(encodedInstruction, "Unknown shift instruction sub-opcode: " + subOpcode);
			
		}
	}
	
	/**
	 * Performs the specified instruction. The instruction is assumed to be an encoded value
	 * as taken from the instruction memory. Only the lowest 18 bits are respected. The
	 * highest-order bits of those 18 bits are assumed to contain the opcode, and so on.
	 * @param encodedInstruction the encoded instruction, as taken from the instruction memory
	 * @throws PicoblazeSimulatorException when this model fails
	 */
	public void performInstruction(int encodedInstruction) throws PicoblazeSimulatorException {
		
		int primaryOpcode = ((encodedInstruction >> 13) & 31);
		boolean rightOperandIsImmediate = (((encodedInstruction >> 12) & 1) == 0);
		PicoblazeJumpCondition jumpCondition = PicoblazeJumpCondition.fromEncodedInstruction(encodedInstruction);
		int leftOperand = ((encodedInstruction >> 8) & 15);
		int rightOperand = (encodedInstruction & 255);
		int targetAddress = (encodedInstruction & 1023);
		
		if (!rightOperandIsImmediate) {
			rightOperand = (rightOperand >> 4);
		}
		
		switch (primaryOpcode) {
		
		// LOAD
		case 0:
			performLoad(leftOperand, rightOperand, rightOperandIsImmediate);
			break;
			
		// INPUT
		case 2:
			performInput(leftOperand, rightOperand, rightOperandIsImmediate);
			break;
			
		// FETCH
		case 3:
			performFetch(leftOperand, rightOperand, rightOperandIsImmediate);
			break;
			
		// AND
		case 5:
			performAnd(leftOperand, rightOperand, rightOperandIsImmediate);
			break;
			
		// OR
		case 6:
			performOr(leftOperand, rightOperand, rightOperandIsImmediate);
			break;
			
		// XOR
		case 7:
			performXor(leftOperand, rightOperand, rightOperandIsImmediate);
			break;
			
		// TEST
		case 9:
			performTest(leftOperand, rightOperand, rightOperandIsImmediate);
			break;
			
		// COMPARE
		case 10:
			performCompare(leftOperand, rightOperand, rightOperandIsImmediate);
			break;
			
		// ADD
		case 12:
			performAdd(leftOperand, rightOperand, rightOperandIsImmediate);
			break;
			
		// ADDCY
		case 13:
			performAddWithCarry(leftOperand, rightOperand, rightOperandIsImmediate);
			break;
			
		// SUB
		case 14:
			performSub(leftOperand, rightOperand, rightOperandIsImmediate);
			break;
			
		// SUBCY
		case 15:
			performSubWithCarry(leftOperand, rightOperand, rightOperandIsImmediate);
			break;
			
		// all shift and rotate instructions
		case 16:
			performShiftOrRotateInstruction(encodedInstruction, leftOperand);
			break;
			
		// RETURN*
		case 21:
			performReturn(jumpCondition);
			break;
			
		// OUTPUT
		case 22:
			performOutput(leftOperand, rightOperand, rightOperandIsImmediate);
			break;
			
		// STORE
		case 23:
			performStore(leftOperand, rightOperand, rightOperandIsImmediate);
			break;
			
		// CALL*
		case 24:
			performCall(jumpCondition, targetAddress);
			break;
			
		// JUMP*
		case 26:
			performJump(jumpCondition, targetAddress);
			break;
			
		// RETURNI*
		case 28:
			performReturnInterrupt((encodedInstruction & 1) != 0);
			break;
			
		// ENABLE INTERRUPT, DISABLE INTERRUPT
		case 30:
			performSetInterruptEnable((encodedInstruction & 1) != 0);
			break;

		// undefined primary opcodes: 1, 4, 8, 11, 17-20, 25, 27, 29, 31
		default:
			undefinedInstruction(encodedInstruction, "unknown primary opcode (highest five bits): " + primaryOpcode);
			break;
			
		}
	}

	/**
	 * 
	 */
	private void undefinedInstruction(int encodedInstruction, String message) throws UndefinedInstructionCodeException {
		if (magicInstructionHandler != null) {
			if (magicInstructionHandler.handleInstruction(this, encodedInstruction)) {
				incrementPc();
				return;
			}
		}
		throw new UndefinedInstructionCodeException(message);
	}
	
}
