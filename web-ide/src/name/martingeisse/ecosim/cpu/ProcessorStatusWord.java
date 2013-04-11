/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu;

/**
 * This class contains static helper methods to handle PSW values.
 */
public class ProcessorStatusWord {

	/**
	 * the bit mask for the residue bits. These bits retain the value
	 * written into them but are otherwise ignored by the CPU.
	 */
	public static final int RESIDUE_MASK = 0xF0000000;

	/**
	 * the bit mask for the vector bit
	 */
	public static final int VECTOR_BIT = 0x08000000;

	/**
	 * the bit mask for the user mode bit
	 */
	public static final int USER_MODE_BIT = 0x04000000;

	/**
	 * the bit mask for the previous user mode bit
	 */
	public static final int PREVIOUS_USER_MODE_BIT = 0x02000000;

	/**
	 * the bit mask for the old user mode bit
	 */
	public static final int OLD_USER_MODE_BIT = 0x01000000;

	/**
	 * the bit mask for the interrupt enable bit
	 */
	public static final int INTERRUPT_ENABLE_BIT = 0x00800000;

	/**
	 * the bit mask for the previous interrupt enable bit
	 */
	public static final int PREVIOUS_INTERRUPT_ENABLE_BIT = 0x00400000;

	/**
	 * the bit mask for the old interrupt enable bit
	 */
	public static final int OLD_INTERRUPT_ENABLE_BIT = 0x00200000;

	/**
	 * the bit mask for the priority bits
	 */
	public static final int PRIORITY_MASK = 0x001F0000;

	/**
	 * the number of bits to shift to obtain the priority bits
	 */
	public static final int PRIORITY_SHIFT = 16;

	/**
	 * the bit mask for the interrupt mask bits
	 */
	public static final int INTERRUPT_MASK_MASK = 0x0000FFFF;

	/**
	 * the number of bits to shift to obtain the interrupt mask bits
	 */
	public static final int INTERRUPT_MASK_SHIFT = 0;
	
	/**
	 * the initial value of the PSW after a CPU reset
	 */
	public static final int RESET_VALUE = 0;

	/**
	 * Prevent instantiation.
	 */
	private ProcessorStatusWord() {		
	}
	
	/**
	 * Obtains the value of the vector bit of the specified psw value.
	 * @param value the PSW value
	 * @return Returns true if the vector bit is set, indicating that the handlers
	 * can be found at virtual address 0xC0000000. Returns false if the vector bit is not
	 * set, indicating that the handlers can be found at virtual address 0xE0000000.
	 */
	public static boolean getVector(int value) {
		return (value & VECTOR_BIT) != 0;
	}

	/**
	 * Obtains the value of the user mode bit of the specified psw value.
	 * @param value the PSW value
	 * @return Returns true if the user mode bit is set, and false otherwise.
	 */
	public static boolean getUserMode(int value) {
		return (value & USER_MODE_BIT) != 0;
	}

	/**
	 * @param value the PSW value to transform
	 * @param priority the interrupt or exception priority to store in the new PSW
	 * @return Returns the transformed value of the PSW after an exception has occured.
	 */
	public static int transformOnException(int value, int priority) {
		if (priority < 0 || priority > 31) {
			throw new IllegalArgumentException("invalid exception priority: " + priority);
		}
		
		int result = value & (RESIDUE_MASK | VECTOR_BIT | INTERRUPT_MASK_MASK);
		result |= ((value & USER_MODE_BIT) != 0) ? PREVIOUS_USER_MODE_BIT : 0;
		result |= ((value & PREVIOUS_USER_MODE_BIT) != 0) ? OLD_USER_MODE_BIT : 0;
		result |= ((value & INTERRUPT_ENABLE_BIT) != 0) ? PREVIOUS_INTERRUPT_ENABLE_BIT : 0;
		result |= ((value & PREVIOUS_INTERRUPT_ENABLE_BIT) != 0) ? OLD_INTERRUPT_ENABLE_BIT : 0;
		result |= priority << PRIORITY_SHIFT;
		return result;
	}

	/**
	 * @param value the PSW value to transform
	 * @return Returns the transformed value of the PSW after a return-from-exception has been executed.
	 */
	public static int transformOnReturnFromException(int value) {
		int result = value & (RESIDUE_MASK | VECTOR_BIT | OLD_USER_MODE_BIT | OLD_INTERRUPT_ENABLE_BIT | PRIORITY_MASK | INTERRUPT_MASK_MASK);
		result |= ((value & PREVIOUS_USER_MODE_BIT) != 0) ? USER_MODE_BIT : 0;
		result |= ((value & OLD_USER_MODE_BIT) != 0) ? PREVIOUS_USER_MODE_BIT : 0;
		result |= ((value & PREVIOUS_INTERRUPT_ENABLE_BIT) != 0) ? INTERRUPT_ENABLE_BIT : 0;
		result |= ((value & OLD_INTERRUPT_ENABLE_BIT) != 0) ? PREVIOUS_INTERRUPT_ENABLE_BIT : 0;
		return result;
	}
	
	/**
	 * Checks if the specified PSW value allows an interrupt to occur. This is the case if the
	 * general interrupt-enable flag and the interrupt mask bit for this interrupt
	 * are both set.
	 * @param value the PSW value
	 * @param index the interrupt index (0-15) of the interrupt to check
	 * @return Returns true if the specified PSW value allows this interrupt to occur, false otherwise.
	 */
	public static boolean allowsInterrupt(int value, int index) {
		if (index < 0 || index > 15) {
			throw new IllegalArgumentException("invalid interrupt index: " + index);
		}
		return (value & INTERRUPT_ENABLE_BIT) != 0 && (value & (1 << index)) != 0;
	}


}
