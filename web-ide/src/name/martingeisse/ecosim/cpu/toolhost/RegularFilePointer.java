/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.toolhost;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import name.martingeisse.ecosim.cpu.usermode.CopyFromBufferAction;
import name.martingeisse.ecosim.cpu.usermode.CopyToBufferAction;
import name.martingeisse.ecosim.cpu.usermode.SparseMemory;
import name.martingeisse.ecosim.cpu.usermode.unix.AbstractFilePointer;
import name.martingeisse.ecosim.cpu.usermode.unix.IFilePointer;
import name.martingeisse.ecosim.cpu.usermode.unix.SeekOrigin;
import name.martingeisse.ecosim.cpu.usermode.unix.UnixSyscallErrorCode;
import name.martingeisse.ecosim.cpu.usermode.unix.UnixSyscallException;

/**
 * Implementation of {@link IFilePointer} for regular files.
 */
public class RegularFilePointer extends AbstractFilePointer {

	/**
	 * the fileSystem
	 */
	private IToolhostFileSystem fileSystem;
	
	/**
	 * the file
	 */
	private File file;
	
	/**
	 * the randomAccess
	 */
	private RandomAccessFile randomAccess;
	
	/**
	 * the readable
	 */
	private boolean readable;
	
	/**
	 * the writable
	 */
	private boolean writable;
	
	/**
	 * the append
	 */
	private boolean append;
	
	/**
	 * Constructor.
	 * @param fileSystem the file system
	 * @param file the file to open
	 * @param create whether to create the file if it does not exist
	 * @param truncate whether the file should be truncated (ignored if not writable)
	 * @param readable whether the file should be readable
	 * @param writable whether the file should be writable
	 * @param append whether write operations should always append
	 * @throws UnixSyscallException on I/O errors
	 */
	public RegularFilePointer(IToolhostFileSystem fileSystem, File file, boolean create, boolean truncate, boolean readable, boolean writable, boolean append) throws UnixSyscallException {
		
		// check for errors
		if (!create && !file.exists()) {
			throw new UnixSyscallException(UnixSyscallErrorCode.IO_ERROR);
		}
		
		// convert exceptions
		try {
			
			// initialize
			this.fileSystem = fileSystem;
			this.file = file;
			this.randomAccess = new RandomAccessFile(file, writable ? "rw" : "r");
			this.readable = readable;
			this.writable = writable;
			this.append = append;
			
			// honor creation flags
			if (writable && truncate) {
				this.randomAccess.setLength(0);
			}
			
		} catch (IOException e) {
			throw new UnixSyscallException(UnixSyscallErrorCode.IO_ERROR);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.AbstractFilePointer#dispose()
	 */
	@Override
	protected void dispose() {
		try {
			randomAccess.close();
		} catch (IOException e) {
		}
		randomAccess = null;
		super.dispose();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.IFilePointer#read(name.martingeisse.ecotools.simulator.cpu.toolhost.ToolhostProcess, name.martingeisse.ecotools.simulator.cpu.usermode.SparseMemory, int, int)
	 */
	@Override
	public int read(ToolhostProcess process, SparseMemory memory, int startAddress, int count) throws UnixSyscallException {

		// permission check
		if (!readable) {
			throw new UnixSyscallException(UnixSyscallErrorCode.IO_ERROR);
		}
		
		// edge cases
		if (count < 1) {
			return 0;
		}

		// report exceptions as syscall exceptions
		try {

			// try to read
			byte[] buffer = new byte[count];
			int n = randomAccess.read(buffer);
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

		// permission check
		if (!writable) {
			throw new UnixSyscallException(UnixSyscallErrorCode.IO_ERROR);
		}

		// report exceptions as syscall exceptions
		try {
			
			// check for append mode
			if (append) {
				randomAccess.seek(randomAccess.length());
			}
			
			// edge cases
			if (count < 1) {
				return 0;
			}

			// try to write
			byte[] buffer = new byte[count];
			new CopyToBufferAction(memory, buffer, false).execute(startAddress, count);
			randomAccess.write(buffer);
			return count;
			
		} catch (IOException e) {
			throw new UnixSyscallException(UnixSyscallErrorCode.IO_ERROR);
		}
		
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.IFilePointer#seek(int, name.martingeisse.ecotools.simulator.cpu.usermode.unix.SeekOrigin)
	 */
	@Override
	public int seek(int offset, SeekOrigin origin) throws UnixSyscallException {
		try {
			switch (origin) {
			
			case START:
				randomAccess.seek(offset);
				break;
				
			case CURRENT:
				randomAccess.seek(randomAccess.getFilePointer() + offset);
				break;
				
			case END:
				randomAccess.seek(randomAccess.length() + offset);
				break;
				
			}
			return (int)randomAccess.getFilePointer();
		} catch (IOException e) {
			throw new UnixSyscallException(UnixSyscallErrorCode.IO_ERROR);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.unix.IFilePointer#stat()
	 */
	@Override
	public StatResult stat() throws UnixSyscallException {
		return fileSystem.stat(file);
	}

}
