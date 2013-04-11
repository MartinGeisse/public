/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.toolhost;

import java.io.File;

import name.martingeisse.ecosim.cpu.usermode.unix.IFilePointer;
import name.martingeisse.ecosim.cpu.usermode.unix.UnixSyscallException;

/**
 * This interface represents the toolhost's file system and
 * provides methods such as opening a file.
 */
public interface IToolhostFileSystem {

	/**
	 * Opens the specified file.
	 * @param file the file to open
	 * @param create whether to create the file if it doesn't exist
	 * @param truncate whether to truncate the file when opening (only if writable)
	 * @param readable whether the file pointer should be readable
	 * @param writeable whether the file pointer should be writable
	 * @param append whether the file pointer should be appending
	 * @return the file pointer
	 * @throws UnixSyscallException on errors
	 */
	public IFilePointer open(File file, boolean create, boolean truncate, boolean readable, boolean writeable, boolean append) throws UnixSyscallException;

	/**
	 * Sets the last-accessed and last-modified times of the specified file.
	 * @param file the file
	 * @param lastAccessedTime the last-accessed time
	 * @param lastModifiedTime the last-modified time
	 * @throws UnixSyscallException on errors
	 */
	public void setFileTime(File file, int lastAccessedTime, int lastModifiedTime) throws UnixSyscallException;

	/**
	 * Unlinks the specified file from the file system. The file must not be a directory.
	 * @param file the file to unlink
	 * @throws UnixSyscallException on errors
	 */
	public void unlink(File file) throws UnixSyscallException;
	
	/**
	 * Queries meta-data for a file.
	 * @param file the file
	 * @return the meta-data
	 * @throws UnixSyscallException on errors
	 */
	public StatResult stat(File file) throws UnixSyscallException;
	
}
