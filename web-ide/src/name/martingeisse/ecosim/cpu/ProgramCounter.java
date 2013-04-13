/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu;



/**
 * This class implements the PC register.
 */
public final class ProgramCounter {

	/**
	 * the value
	 */
	private int value;

	/**
	 * the userInterface
	 */
	private ICpuUserInterface userInterface;

	/**
	 * Constructor
	 */
	public ProgramCounter() {
		this.value = 0xe0000000;
		this.userInterface = null;
	}

	/**
	 * @return Returns the value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * @param value the new value to set
	 * @param notifyUserInterface whether the onWritePc() method of the user interface shall be called
	 */
	public void setValue(int value, boolean notifyUserInterface) {
		this.value = value;
		if (userInterface != null && notifyUserInterface) {
			userInterface.onWritePc();
		}
	}

	/**
	 * @return Returns the userInterface.
	 */
	public ICpuUserInterface getUserInterface() {
		return userInterface;
	}

	/**
	 * Sets the userInterface.
	 * @param userInterface the new value to set
	 */
	public void setUserInterface(ICpuUserInterface userInterface) {
		this.userInterface = userInterface;
	}

	/**
	 * Copies this object.
	 * @return the clone
	 */
	@Override
	public ProgramCounter clone() {
		ProgramCounter clone = new ProgramCounter();
		clone.setValue(getValue(), false);
		return clone;
	}

}
