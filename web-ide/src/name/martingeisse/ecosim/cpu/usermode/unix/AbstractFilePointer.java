/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode.unix;

import name.martingeisse.ecosim.cpu.toolhost.ToolhostProcess;
import name.martingeisse.ecosim.cpu.usermode.SparseMemory;

/**
 * This class implements reference counting. It also implements ioctl as a NOP
 * since this is the default behavior assumed by many file pointer types.
 */
public abstract class AbstractFilePointer implements IFilePointer {

	/**
	 * the referenceCount
	 */
	private int referenceCount;
	
	/**
	 * Constructor.
	 */
	public AbstractFilePointer() {
		referenceCount = 1;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.IFilePointer#acquireReference()
	 */
	@Override
	public IFilePointer acquireReference() {
		referenceCount++;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.IFilePointer#releaseReference()
	 */
	@Override
	public void releaseReference() {
		referenceCount--;
		if (referenceCount == 0) {
			dispose();
		}
	}
	
	/**
	 * This method is invoked when the reference count drops to zero. The default implementation
	 * does nothing.
	 */
	protected void dispose() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.IFilePointer#ioctl(name.martingeisse.ecotools.simulator.cpu.toolhost.ToolhostProcess, name.martingeisse.ecotools.simulator.cpu.usermode.SparseMemory, int, int)
	 */
	@Override
	public int ioctl(ToolhostProcess process, SparseMemory memory, int requestCode, int parameterAddress) throws UnixSyscallException {
		return 0;
	}
	
}
