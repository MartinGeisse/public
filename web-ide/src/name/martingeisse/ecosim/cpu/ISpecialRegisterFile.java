/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu;


/**
 * This interface represents the special register file of the CPU. It contains 5 registers, each 32 bits
 * wide. This interface also defines the constants that indicate the semantics of the special registers.
 */
public interface ISpecialRegisterFile {

	/**
	 * the index of the PSW register within this special register file
	 */
	public static final int INDEX_PSW = 0;

	/**
	 * the bit mask of retained PSW bits
	 */
	public static final int RETENTION_MASK_PSW = 0xffffffff;

	/**
	 * the index of the TLB index register within this special register file
	 */
	public static final int INDEX_TLB_INDEX = 1;

	/**
	 * the bit mask of retained TLB index bits
	 */
	public static final int RETENTION_MASK_TLB_INDEX = 31;

	/**
	 * the index of the TLB entry high register (virtual page start address) within this special register file
	 */
	public static final int INDEX_TLB_ENTRY_HIGH = 2;

	/**
	 * the bit mask of retained TLB entry high bits
	 */
	public static final int RETENTION_MASK_TLB_ENTRY_HIGH = 0xfffff000;

	/**
	 * the index of the TLB entry low register (physical page frame start address and control bits) within this special register file
	 */
	public static final int INDEX_TLB_ENTRY_LOW = 3;

	/**
	 * the bit mask of retained TLB entry low bits
	 */
	public static final int RETENTION_MASK_TLB_ENTRY_LOW = 0xfffff003;

	/**
	 * the index of the TLB bad address register within this special register file
	 */
	public static final int INDEX_TLB_BAD_ADDRESS = 4;

	/**
	 * the bit mask of retained TLB bad address bits
	 */
	public static final int RETENTION_MASK_TLB_BAD_ADDRESS = 0xffffffff;

	/**
	 * the number of special registers
	 */
	public static final int SIZE = 5;

	/**
	 * @return Returns the userInterface.
	 */
	public ICpuUserInterface getUserInterface();

	/**
	 * Sets the userInterface.
	 * @param userInterface the new value to set
	 */
	public void setUserInterface(ICpuUserInterface userInterface);

	/**
	 * Reads from a special-purpose register, then optionally notifies the user
	 * interface. If the user interface reacts by changing the value again,
	 * the original value is still returned.
	 * @param index the index of the register to read
	 * @param notifyUserInterface whether onReadSpecialRegister() of the user interface is invoked
	 * @return Returns the value of that register
	 */
	public int read(int index, boolean notifyUserInterface);

	/**
	 * Writes a value into a special-purpose register, then optionally notifies the user
	 * interface. Only those bits of the value are saved that are set in the corresponding
	 * retention mask.
	 * @param index the index of the register to write
	 * @param value the value to write into that register
	 * @param notifyUserInterface whether onWriteGeneralRegister() of the user interface is invoked
	 */
	public void write(int index, int value, boolean notifyUserInterface);

	/**
	 * Writes a value into a special-purpose register, then optionally notifies the user
	 * interface. This method bypasses the retention masks and simply writes the value
	 * into the register as-is.
	 * @param index the index of the register to write
	 * @param value the value to write into that register
	 * @param notifyUserInterface whether onWriteGeneralRegister() of the user interface is invoked
	 */
	public void forceWrite(int index, int value, boolean notifyUserInterface);

	/**
	 * Clones this object.
	 * @return the clone
	 */
	public ISpecialRegisterFile clone();
	
}
