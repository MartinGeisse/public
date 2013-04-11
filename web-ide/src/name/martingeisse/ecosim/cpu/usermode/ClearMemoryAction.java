/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode;

import java.util.Arrays;


/**
 * This action clears a range of memory with zero-bytes.
 */
public class ClearMemoryAction extends AbstractSparseMemoryBulkAction {

	/**
	 * Constructor.
	 * @param memory the memory to operate on
	 */
	public ClearMemoryAction(SparseMemory memory) {
		super(memory);
	}
	
	/**
	 * Constructor.
	 * @param memory the memory to operate on
	 * @param autoAllocate whether to auto-allocate pages
	 */
	public ClearMemoryAction(SparseMemory memory, boolean autoAllocate) {
		super(memory, autoAllocate);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.AbstractSparseMemoryBulkAction#handleChunk(byte[], int, int, int, int)
	 */
	@Override
	protected void handleChunk(byte[] page, int pageBaseAddress, int operationBaseAddress, int chunkStart, int chunkCount) throws Exception {
		Arrays.fill(page, chunkStart, chunkStart + chunkCount, (byte)0);
	}
	
}
