/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode.unix;

/**
 * Generic UNIX szscall error codes. Specific Unix-like
 * OSes might want to define additional codes.
 */
public enum UnixSyscallErrorCode {

	/**
	 * No error occurred.
	 */
	NONE,
	
	/**
	 * A file descriptor was expected as a syscall argument, but
	 * the specified argument was not a valid file descriptor.
	 */
	INVALID_FILE_DESCRIPTOR,
	
	/**
	 * The syscall was expected to allocate a file descriptor, but
	 * the file pointer table was full.
	 */
	FILE_POINTER_TABLE_FULL,

	/**
	 * The syscall has caused a memory fault.
	 * 
	 * Example: read() with a buffer that is not fully mapped.
	 */
	MEMORY_FAULT,
	
	/**
	 * The arguments were illegal in the sense that they did
	 * not specify any actual oepration to perform.
	 * 
	 * Example: ioctl() with an illegal request code.
	 */
	ILLEGAL_ARGUMENT,
	
	/**
	 * The specified operation is not supported either by the
	 * OS or by the specific object.
	 * 
	 * Example: ioctl() with a request code not supported
	 * by the file pointer object.
	 */
	UNSUPPORTED_OPERATION,
	
	/**
	 * An I/O error occurred that isn't specified any further.
	 */
	IO_ERROR,
	
	/**
	 * A process ID was expected as a syscall argument, but
	 * the specified argument was not a valid process ID.
	 */
	INVALID_PROCESS,
	
	/**
	 * The process does not have sufficient permissions for the
	 * requested operation.
	 */
	NO_PERMISSION,
	
}
