/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu.usermode;

import name.martingeisse.ecosim.cpu.CpuException;
import name.martingeisse.ecosim.cpu.ICpuUserInterface;
import name.martingeisse.ecosim.cpu.IMemoryManagementUnit;
import name.martingeisse.ecosim.cpu.MemoryVisualizationException;

/**
 * EOS usermode implementation of {@link IMemoryManagementUnit}.
 */
public final class IdentityMemoryManagementUnit implements IMemoryManagementUnit {

	/**
	 * Constructor
	 */
	public IdentityMemoryManagementUnit() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#getUserInterface()
	 */
	@Override
	public ICpuUserInterface getUserInterface() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#setUserInterface(name.martingeisse.ecotools.simulator.cpu.ICpuUserInterface)
	 */
	@Override
	public void setUserInterface(ICpuUserInterface userInterface) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#getTlbEntryHigh(int)
	 */
	@Override
	public int getTlbEntryHigh(int index) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#getTlbEntryLow(int)
	 */
	@Override
	public int getTlbEntryLow(int index) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#setTlbEntry(int, int, int, boolean)
	 */
	@Override
	public void setTlbEntry(int index, int highValue, int lowValue, boolean notifyUserInterface) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#getRandomCounter()
	 */
	@Override
	public int getRandomCounter() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#setRandomCounter(int)
	 */
	@Override
	public void setRandomCounter(int randomCounter) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#updateRandomCounter()
	 */
	@Override
	public void updateRandomCounter() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#findTlbEntry(int)
	 */
	@Override
	public int findTlbEntry(int virtualAddress) {
		return INVALID_TLB_INDEX;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#mapAddressForCpu(int, boolean)
	 */
	@Override
	public int mapAddressForCpu(int virtualAddress, boolean write) throws CpuException {
		return virtualAddress;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#mapAddressForVisualization(int)
	 */
	@Override
	public int mapAddressForVisualization(int virtualAddress) throws MemoryVisualizationException {
		return virtualAddress;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#executeTbsInstruction()
	 */
	@Override
	public void executeTbsInstruction() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#executeTbwrInstruction()
	 */
	@Override
	public void executeTbwrInstruction() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#executeTbriInstruction()
	 */
	@Override
	public void executeTbriInstruction() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#executeTbwiInstruction()
	 */
	@Override
	public void executeTbwiInstruction() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#clone()
	 */
	@Override
	public IMemoryManagementUnit clone() {
		return this;
	}
	
}
