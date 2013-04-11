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
 * Instances of this type are stored in the file pointer table
 * of a process, which is in turn indexed by file descriptors.
 * 
 * Conceptually, a file pointer is a pair of a file and a
 * current offset within that file. Other implementations,
 * e.g. for pipes and terminals, are possible.
 * 
 * A file pointer can be referenced by multiple clients. All
 * clients therefore share the current offset. The file pointer
 * keeps a reference count to trigger specific actions as soon
 * as no client is left, e.g. close the file. Multiple clients
 * for a single file pointer result from operations such
 * as fork() and dup().
 */
public interface IFilePointer {

	/**
	 * Increases the reference count of this file pointer by 1.
	 * @return this
	 */
	public IFilePointer acquireReference();
	
	/**
	 * Decreases the reference count of this file pointer by 1,
	 * releasing the file pointer itself if this sets the
	 * reference count to zero.
	 */
	public void releaseReference();
	
	/**
	 * Reads from this file pointer.
	 * @param process the calling process (used to implement blocking operations)
	 * @param memory the memory to read to
	 * @param startAddress the first address in the memory to read to
	 * @param count the number of bytes to read
	 * @return the number of bytes actually read, or 0 to indicate
	 * end-of-file (note: Java usually signals EOF by returning -1).
	 * @throws UnixSyscallException if reading fails
	 */
	public int read(ToolhostProcess process, SparseMemory memory, int startAddress, int count) throws UnixSyscallException;
	
	/**
	 * Writes from this file pointer.
	 * @param process the calling process (used to implement blocking operations)
	 * @param memory the memory to write to
	 * @param startAddress the first address in the memory to write to
	 * @param count the number of bytes to write
	 * @return the number of bytes actually written
	 * @throws UnixSyscallException if writing fails
	 */
	public int write(ToolhostProcess process, SparseMemory memory, int startAddress, int count) throws UnixSyscallException;
	
	/**
	 * Performs an implementation-specific I/O control operation.
	 * @param process the calling process (used to implement blocking operations)
	 * @param memory the memory (might be needed depending on the actual operation)
	 * @param requestCode the operation request code
	 * @param parameterAddress the address of request parameters (if any)
	 * @return a nonnegative output value (zero if no output is defined)
	 * @throws UnixSyscallException if the operation fails
	 */
	public int ioctl(ToolhostProcess process, SparseMemory memory, int requestCode, int parameterAddress) throws UnixSyscallException;

	/**
	 * Changes the current position of this file pointer and returns the new position.
	 * @param offset the offset of the new position relative to the origin
	 * @param origin the origin used to determine the new position
	 * @return the new position
	 * @throws UnixSyscallException if seeking fails
	 */
	public int seek(int offset, SeekOrigin origin) throws UnixSyscallException;
	
	/**
	 * Returns meta-data for the file used by this file pointer.
	 * @return the meta-data
	 * @throws UnixSyscallException if retrieving meta-data fails
	 */
	public StatResult stat() throws UnixSyscallException;
	
}
