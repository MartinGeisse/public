/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu.usermode;

import name.martingeisse.ecosim.cpu.ICpuUserInterface;
import name.martingeisse.ecosim.cpu.ISpecialRegisterFile;
import name.martingeisse.ecosim.cpu.ProcessorStatusWord;

/**
 * Pure-usermode implementation of {@link ISpecialRegisterFile}.
 */
public final class UsermodeSpecialRegisterFile implements ISpecialRegisterFile {

	/**
	 * Constructor
	 */
	public UsermodeSpecialRegisterFile() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.ISpecialRegisterFile#getUserInterface()
	 */
	@Override
	public ICpuUserInterface getUserInterface() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.ISpecialRegisterFile#setUserInterface(name.martingeisse.ecotools.simulator.cpu.ICpuUserInterface)
	 */
	@Override
	public void setUserInterface(ICpuUserInterface userInterface) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.ISpecialRegisterFile#read(int, boolean)
	 */
	@Override
	public int read(int index, boolean notifyUserInterface) {
		if (index == INDEX_PSW) {
			// user mode with interrupts disabled
			return ProcessorStatusWord.USER_MODE_BIT;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.ISpecialRegisterFile#write(int, int, boolean)
	 */
	@Override
	public void write(int index, int value, boolean notifyUserInterface) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.ISpecialRegisterFile#forceWrite(int, int, boolean)
	 */
	@Override
	public void forceWrite(int index, int value, boolean notifyUserInterface) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.ISpecialRegisterFile#clone()
	 */
	@Override
	public ISpecialRegisterFile clone() {
		return this;
	}
}
