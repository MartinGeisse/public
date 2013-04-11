/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.toolhost;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import name.martingeisse.ecosim.cpu.usermode.unix.IFilePointer;
import name.martingeisse.ecosim.cpu.usermode.unix.UnixSyscallErrorCode;
import name.martingeisse.ecosim.cpu.usermode.unix.UnixSyscallException;

/**
 * Default implementation of {@link IToolhostFileSystem}.
 */
public class ToolhostFileSystem implements IToolhostFileSystem {

	/**
	 * The known inode IDs. Whenever a file is stat()ed and is not yet listed in this map,
	 * it is assigned a new inode ID. This is needed because there is no cross-platform
	 * notion of an inode ID, yet userspace programs rely on this ID actually identifying
	 * the file (i.e., the same ID is returned for any number of calls for a single file,
	 * and calls for different files always return a different ID).
	 */
	private Map<File, Integer> knownInodeIds = new HashMap<File, Integer>();
	
	/**
	 * the inodeIdAllocator
	 */
	private int inodeIdAllocator = 50;
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.toolhost.IToolhostFileSystem#open(java.io.File, boolean, boolean, boolean, boolean, boolean)
	 */
	@Override
	public IFilePointer open(File file, boolean create, boolean truncate, boolean readable, boolean writable, boolean append) throws UnixSyscallException {
		return new RegularFilePointer(this, file, create, truncate, readable, writable, append);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.toolhost.IToolhostFileSystem#setFileTime(java.io.File, int, int)
	 */
	@Override
	public void setFileTime(File file, int lastAccessedTime, int lastModifiedTime) throws UnixSyscallException {
		file.setLastModified(1000 * (long)lastModifiedTime);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.toolhost.IToolhostFileSystem#unlink(java.io.File)
	 */
	@Override
	public void unlink(File file) throws UnixSyscallException {
		if (!file.delete()) {
			throw new UnixSyscallException(UnixSyscallErrorCode.IO_ERROR);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.toolhost.IToolhostFileSystem#stat(java.io.File)
	 */
	@Override
	public StatResult stat(File file) throws UnixSyscallException {
		try {
			
			// the file must exist to have an inode that can be stat()ed
			if (!file.exists()) {
				throw new UnixSyscallException(UnixSyscallErrorCode.IO_ERROR);
			}
			
			// determine inode id
			File canonicalFile = file.getCanonicalFile();
			Integer inodeId = knownInodeIds.get(canonicalFile);
			if (inodeId == null) {
				inodeId = inodeIdAllocator;
				inodeIdAllocator++;
				knownInodeIds.put(canonicalFile, inodeId);
			}
			
			// create result
			return StatResult.createForRegularFile(inodeId, (int)file.length(), (int)(file.lastModified() / 1000));
			
		} catch (IOException e) {
			throw new UnixSyscallException(UnixSyscallErrorCode.IO_ERROR);
		}
	}
	
}
