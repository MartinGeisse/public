/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode.unix;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import name.martingeisse.ecosim.cpu.toolhost.StatResult;
import name.martingeisse.ecosim.cpu.toolhost.ToolhostProcess;
import name.martingeisse.ecosim.cpu.usermode.CopyFromBufferAction;
import name.martingeisse.ecosim.cpu.usermode.CopyToBufferAction;
import name.martingeisse.ecosim.cpu.usermode.SparseMemory;

/**
 * This class bridges between usermode {@link IFilePointer} objects
 * and Java I/O streams.
 * 
 * Each instance encapsulates up to two streams used for input and
 * output, respectively. If no input stream is set, reading from
 * the file pointer returns EOF. If no output stream is set, 
 * writing to the file pointer succeeds but has no effect.
 * 
 * The ioctl operation is implemented as a NOP by default but
 * can be overridden by subclasses.
 * 
 * Seeking is not supported by this implementation and
 * just returns 0.
 */
public class StreamFilePointer extends AbstractFilePointer {

	/**
	 * the input
	 */
	private final InputStream input;
	
	/**
	 * the output
	 */
	private final OutputStream output;
	
	/**
	 * Constructor.
	 */
	public StreamFilePointer() {
		this(null, null);
	}
	
	/**
	 * Constructor.
	 * @param input the input stream
	 */
	public StreamFilePointer(InputStream input) {
		this(input, null);
	}

	/**
	 * Constructor.
	 * @param output the output stream
	 */
	public StreamFilePointer(OutputStream output) {
		this(null, output);
	}
	
	/**
	 * Constructor.
	 * @param input the input stream
	 * @param output the output stream
	 */
	public StreamFilePointer(InputStream input, OutputStream output) {
		this.input = input;
		this.output = output;
	}

	/**
	 * Getter method for the input.
	 * @return the input
	 */
	public InputStream getInput() {
		return input;
	}
	
	/**
	 * Getter method for the output.
	 * @return the output
	 */
	public OutputStream getOutput() {
		return output;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.IFilePointer#read(name.martingeisse.ecotools.simulator.cpu.toolhost.ToolhostProcess, name.martingeisse.ecotools.simulator.cpu.usermode.SparseMemory, int, int)
	 */
	@Override
	public int read(ToolhostProcess process, SparseMemory memory, int startAddress, int count) throws UnixSyscallException {
		
		// edge cases
		if (count < 1 || input == null) {
			return 0;
		}

		// report exceptions as syscall exceptions
		try {

			// try to read
			byte[] buffer = new byte[count];
			int n = input.read(buffer);
			if (n < 1) {
				return 0;
			}
			
			// read was successful
			new CopyFromBufferAction(memory, buffer, false).execute(startAddress, n);
			return n;
			
		} catch (IOException e) {
			throw new UnixSyscallException(UnixSyscallErrorCode.IO_ERROR);
		}
		
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.IFilePointer#write(name.martingeisse.ecotools.simulator.cpu.toolhost.ToolhostProcess, name.martingeisse.ecotools.simulator.cpu.usermode.SparseMemory, int, int)
	 */
	@Override
	public int write(ToolhostProcess process, SparseMemory memory, int startAddress, int count) throws UnixSyscallException {

		// edge cases
		if (count < 1 || output == null) {
			return 0;
		}

		// report exceptions as syscall exceptions
		try {

			// try to write
			byte[] buffer = new byte[count];
			new CopyToBufferAction(memory, buffer, false).execute(startAddress, count);
			output.write(buffer);
			return count;
			
		} catch (IOException e) {
			throw new UnixSyscallException(UnixSyscallErrorCode.IO_ERROR);
		}
		
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.IFilePointer#seek(int, name.martingeisse.ecotools.simulator.cpu.usermode.unix.SeekOrigin)
	 */
	@Override
	public int seek(int offset, SeekOrigin origin) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.IFilePointer#stat()
	 */
	@Override
	public StatResult stat() throws UnixSyscallException {
		// TODO: fstat()ing this kind of special file is not yet supported
		throw new UnixSyscallException(UnixSyscallErrorCode.IO_ERROR);
	}

}
