/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode.unix;

import name.martingeisse.ecosim.cpu.toolhost.IToolhostSleepHandler;
import name.martingeisse.ecosim.cpu.toolhost.StatResult;
import name.martingeisse.ecosim.cpu.toolhost.ToolhostProcess;
import name.martingeisse.ecosim.cpu.usermode.CopyFromBufferAction;
import name.martingeisse.ecosim.cpu.usermode.SparseMemory;
import name.martingeisse.ecosim.util.ByteQueue;

/**
 * The reading end of a pipe. This class synchronizes on the queue to
 * allow reading and writing from different threads.
 */
public class ReadingPipeFilePointer extends AbstractFilePointer {

	/**
	 * the queue
	 */
	private final ByteQueue queue;

	/**
	 * Constructor.
	 * @param queue the queue to read from
	 */
	public ReadingPipeFilePointer(ByteQueue queue) {
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
	public int read(final ToolhostProcess process, final SparseMemory memory, final int startAddress, final int count) throws UnixSyscallException {

		// try to read without blocking
		final byte[] buffer = new byte[count];
		int n = readHelper(buffer, memory, startAddress, count);
		if (n > 0) {
			return n;
		}
		
		// no data available -- might be EOF
		boolean eof;
		synchronized(queue) {
			eof = queue.isEof();
		}
		if (eof) {
			return 0;
		}
		
		// block until some data is available
		process.setSleeping(new IToolhostSleepHandler() {
			@Override
			public void handle(ToolhostProcess process) {
				int n = readHelper(buffer, memory, startAddress, count);
				if (n > 0) {
					process.getCpu().getGeneralRegisters().write(2, n, false);
					process.setRunning();
				}
			}
		});
		return 0;
		
	}
	
	// common handling for blocked and unblocked reading
	private int readHelper(final byte[] buffer, final SparseMemory memory, final int startAddress, final int count) {
		int n;
		synchronized(queue) {
			n = queue.read(buffer, 0, count);
		}
		if (n > 0) {
			CopyFromBufferAction action = new CopyFromBufferAction(memory, buffer, false);
			action.execute(startAddress, n);
		}
		return n;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.IFilePointer#write(name.martingeisse.ecotools.simulator.cpu.toolhost.ToolhostProcess, name.martingeisse.ecotools.simulator.cpu.usermode.SparseMemory, int, int)
	 */
	@Override
	public int write(ToolhostProcess process, SparseMemory memory, int startAddress, int count) throws UnixSyscallException {
		return 0;
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
			queue.notifyReaderStopped();
		}
	}

}
