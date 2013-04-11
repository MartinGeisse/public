/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode;



/**
 * This action copies data from memory into a byte array.
 */
public class CopyToBufferAction extends AbstractSparseMemoryBulkAction {

	/**
	 * the buffer
	 */
	private byte[] buffer;
	
	/**
	 * Constructor.
	 * @param memory the memory to operate on
	 * @param buffer the buffer to copy from
	 */
	public CopyToBufferAction(SparseMemory memory, byte[] buffer) {
		super(memory);
		this.buffer = buffer;
	}
	
	/**
	 * Constructor.
	 * @param memory the memory to operate on
	 * @param buffer the buffer to copy from
	 * @param autoAllocate whether to auto-allocate pages
	 */
	public CopyToBufferAction(SparseMemory memory, byte[] buffer, boolean autoAllocate) {
		super(memory, autoAllocate);
		this.buffer = buffer;
	}

	/**
	 * Executes this action at the specified base address.
	 * Copies as many bytes as the size of the buffer.
	 * @param baseAddress the base address to copy to
	 */
	public final void execute(int baseAddress) {
		executeWrapExceptions(baseAddress, buffer.length);
	}

	/**
	 * Executes this action at the specified base address.
	 * @param baseAddress the base address to copy to
	 * @param length the number of bytes to copy
	 */
	public final void execute(int baseAddress, int length) {
		executeWrapExceptions(baseAddress, length);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.AbstractSparseMemoryBulkAction#handleChunk(byte[], int, int, int, int)
	 */
	@Override
	protected void handleChunk(byte[] page, int pageBaseAddress, int operationBaseAddress, int chunkStart, int chunkCount) throws Exception {
		int bufferOffset = pageBaseAddress - operationBaseAddress + chunkStart;
		System.arraycopy(page, chunkStart, buffer, bufferOffset, chunkCount);
	}
	
}
