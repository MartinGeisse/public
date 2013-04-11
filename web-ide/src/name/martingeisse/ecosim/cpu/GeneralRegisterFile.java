/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu;

/**
 * Standard implementation of {@link IGeneralRegisterFile}.
 */
public final class GeneralRegisterFile implements IGeneralRegisterFile {

	/**
	 * the values
	 */
	private int values[];

	/**
	 * the userInterface
	 */
	private ICpuUserInterface userInterface;

	/**
	 * Constructor
	 */
	public GeneralRegisterFile() {
		this.values = new int[32];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IGeneralRegisterFile#getUserInterface()
	 */
	@Override
	public ICpuUserInterface getUserInterface() {
		return userInterface;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IGeneralRegisterFile#setUserInterface(name.martingeisse.ecotools.simulator.cpu.ICpuUserInterface)
	 */
	@Override
	public void setUserInterface(ICpuUserInterface userInterface) {
		this.userInterface = userInterface;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IGeneralRegisterFile#read(int, boolean)
	 */
	@Override
	public int read(int index, boolean notifyUserInterface) {
		int value = values[index];
		if (notifyUserInterface && userInterface != null) {
			userInterface.onReadGeneralRegister(index);
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IGeneralRegisterFile#write(int, int, boolean)
	 */
	@Override
	public void write(int index, int value, boolean notifyUserInterface) {
		if (index != 0) {
			values[index] = value;
		}
		if (notifyUserInterface && userInterface != null) {
			userInterface.onWriteGeneralRegister(index);
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IGeneralRegisterFile#clone()
	 */
	@Override
	public IGeneralRegisterFile clone() {
		GeneralRegisterFile clone = new GeneralRegisterFile();
		System.arraycopy(values, 0, clone.values, 0, values.length);
		return clone;
	}
	
}
