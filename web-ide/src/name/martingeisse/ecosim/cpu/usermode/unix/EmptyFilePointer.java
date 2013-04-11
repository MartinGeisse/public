/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode.unix;

import name.martingeisse.ecosim.cpu.toolhost.StatResult;
import name.martingeisse.ecosim.cpu.toolhost.ToolhostProcess;
import name.martingeisse.ecosim.cpu.usermode.SparseMemory;

/**
 * This file pointer accepts but discards all written data and signals EOF
 * when reading. 
 */
public class EmptyFilePointer extends AbstractFilePointer {

	/**
	 * Constructor.
	 */
	public EmptyFilePointer() {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.IFilePointer#read(name.martingeisse.ecotools.simulator.cpu.toolhost.ToolhostProcess, name.martingeisse.ecotools.simulator.cpu.usermode.SparseMemory, int, int)
	 */
	@Override
	public int read(ToolhostProcess process, SparseMemory memory, int startAddress, int count) throws UnixSyscallException {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.IFilePointer#write(name.martingeisse.ecotools.simulator.cpu.toolhost.ToolhostProcess, name.martingeisse.ecotools.simulator.cpu.usermode.SparseMemory, int, int)
	 */
	@Override
	public int write(ToolhostProcess process, SparseMemory memory, int startAddress, int count) throws UnixSyscallException {
		return count;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.IFilePointer#seek(int, name.martingeisse.ecotools.simulator.cpu.usermode.unix.SeekOrigin)
	 */
	@Override
	public int seek(int offset, SeekOrigin origin) throws UnixSyscallException {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.IFilePointer#stat()
	 */
	@Override
	public StatResult stat() throws UnixSyscallException {
		throw new RuntimeException("not yet implemented");
	}

}
