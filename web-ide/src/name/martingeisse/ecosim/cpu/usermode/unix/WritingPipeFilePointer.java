/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode.unix;

import name.martingeisse.ecosim.cpu.toolhost.StatResult;
import name.martingeisse.ecosim.cpu.toolhost.ToolhostProcess;
import name.martingeisse.ecosim.cpu.usermode.CopyToBufferAction;
import name.martingeisse.ecosim.cpu.usermode.SparseMemory;
import name.martingeisse.ecosim.util.ByteQueue;

/**
 * The writing end of a pipe. This class synchronizes on the queue to
 * allow reading and writing from different threads.
 */
public class WritingPipeFilePointer extends AbstractFilePointer {

	/**
	 * the queue
	 */
	private final ByteQueue queue;

	/**
	 * Constructor.
	 * @param queue the queue to write to
	 */
	public WritingPipeFilePointer(ByteQueue queue) {
		this.queue = queue;
	}
	
	/**
	 * Getter method for the queue.
	 * @return the queue
	 */
	public ByteQueue getQueue() {
		return queue;
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
		byte[] buffer = new byte[count];
		CopyToBufferAction action = new CopyToBufferAction(memory, buffer, false);
		action.execute(startAddress);
		synchronized(queue) {
			queue.writeNoCopy(buffer);
		}
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

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.AbstractFilePointer#dispose()
	 */
	@Override
	protected void dispose() {
		synchronized(queue) {
			queue.notifyEof();
		}
	}
	
}
