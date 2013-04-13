/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu;


/**
 * This interface represents the general register file of the CPU. It contains 32 registers, each 32 bits
 * wide. Register #0 is the "zero register" which always has the value 0. Writing to this
 * register has no effect.
 */
public interface IGeneralRegisterFile {

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
	 * Reads from a general-purpose register, then optionally notifies the user
	 * interface. If the user interface reacts by changing the value again,
	 * the original value is still returned.
	 * @param index the index of the register to read
	 * @param notifyUserInterface whether onReadGeneralRegister() of the user interface is invoked
	 * @return Returns the value of that register
	 */
	public int read(int index, boolean notifyUserInterface);

	/**
	 * Writes to a general-purpose register, then optionally notifies the user
	 * interface. Notification does occur if the index is 0, although that
	 * register is read-only.
	 * @param index the index of the register to write
	 * @param value the value to write into that register
	 * @param notifyUserInterface whether onWriteGeneralRegister() of the user interface is invoked
	 */
	public void write(int index, int value, boolean notifyUserInterface);

	/**
	 * Clones this object.
	 * @return the clone
	 */
	public IGeneralRegisterFile clone();
	
}
