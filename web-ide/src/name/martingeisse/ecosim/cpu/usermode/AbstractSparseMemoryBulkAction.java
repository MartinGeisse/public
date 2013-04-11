/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode;


/**
 * This class implements basic handling for bulk actions on a {@link SparseMemory}.
 * This is necessary because the memory is split across several byte
 * arrays. This class splits the bulk action into steps that operate
 * on a single byte array each.
 * 
 * This class also supports an "autoAllocate" flag that is used to
 * invoke getPage() or getOrCreatePage() to get a page. The default
 * is to not auto-allocate.
 * 
 * Chunks are handled in ascending-address order, and each page
 * gets at most one chunk.
 */
public abstract class AbstractSparseMemoryBulkAction {

	/**
	 * the memory
	 */
	private final SparseMemory memory;
	
	/**
	 * the autoAllocate
	 */
	private boolean autoAllocate; 

	/**
	 * Constructor.
	 * @param memory the memory to operate on
	 */
	public AbstractSparseMemoryBulkAction(SparseMemory memory) {
		this(memory, false);
	}
	
	/**
	 * Constructor.
	 * @param memory the memory to operate on
	 * @param autoAllocate whether to auto-allocate pages
	 */
	public AbstractSparseMemoryBulkAction(SparseMemory memory, boolean autoAllocate) {
		this.memory = memory;
		this.autoAllocate = autoAllocate;
	}

	/**
	 * Getter method for the memory.
	 * @return the memory
	 */
	public SparseMemory getMemory() {
		return memory;
	}
	
	/**
	 * Getter method for the autoAllocate.
	 * @return the autoAllocate
	 */
	public boolean isAutoAllocate() {
		return autoAllocate;
	}
	
	/**
	 * Setter method for the autoAllocate.
	 * @param autoAllocate the autoAllocate to set
	 */
	public void setAutoAllocate(boolean autoAllocate) {
		this.autoAllocate = autoAllocate;
	}
	
	/**
	 * Gets the byte array for a page. Auto-allocates the page if the
	 * autoAllocate flag is set and the page does not yet exist.
	 * @param address the address to get the page for
	 * @return the page, or null if not yet present and
	 * autoAllocate is not set.
	 */
	public byte[] getPage(int address) {
		return autoAllocate ? memory.getOrCreatePage(address) : memory.getPage(address);
	}

	/**
	 * Executes this action. Note that subclasses might have other
	 * execute() methods that determine the baseAddress and/or count
	 * automatically.
	 * 
	 * Any exceptions thrown in handleChunk() will be wrapped in
	 * a {@link WrappedException}.
	 * 
	 * @param baseAddress the first address to operate on
	 * @param count the number of bytes to operate on
	 * @throws WrappedException if handleChunk() throws an exception
	 */
	public final void executeWrapExceptions(int baseAddress, int count) throws WrappedException {
		
		try {
			
			// preparation
			int operationBaseAddress = baseAddress;
			int pageOffset = (baseAddress & 4095);
			
			// check if we stay within a single page
			if (pageOffset + count <= 4096) {
				handleChunk(memory.getOrCreatePage(baseAddress), baseAddress - pageOffset, operationBaseAddress, pageOffset, count);
				return;
			}
			
			// handle the remainder of the first page (note: the pageOffset is ignored afterwards)
			int n = (4096 - pageOffset);
			handleChunk(memory.getOrCreatePage(baseAddress), baseAddress - pageOffset, operationBaseAddress, pageOffset, n);
			count -= n;
			baseAddress += n;
			
			// handle full pages
			while (count > 4096) {
				handleChunk(memory.getOrCreatePage(baseAddress), baseAddress, operationBaseAddress, 0, 4096);
				baseAddress += 4096;
				count -= 4096;
			}
			
			// handle the last page fraction
			handleChunk(memory.getOrCreatePage(baseAddress), baseAddress, operationBaseAddress, 0, count);
			
		} catch (Exception e) {
			throw new WrappedException(e);
		}
		
	}
	
	/**
	 * Executes this action on a chunk of data that lies within a single page.
	 * 
	 * @param page the page that contains this chunk
	 * @param pageBaseAddress the base address of the page
	 * @param operationBaseAddress the base address of the whole operation
	 * @param chunkStart the start offset of the chunk within the page
	 * @param chunkCount the number of bytes in the chunk
	 * @throws Exception on any error
	 */
	protected abstract void handleChunk(byte[] page, int pageBaseAddress, int operationBaseAddress, int chunkStart, int chunkCount) throws Exception;
	
	/**
	 * This exception type wraps exceptions that are thrown by handleChunk. This is
	 * needed because we can't have handleChunk throw a generic exception type.
	 */
	public class WrappedException extends RuntimeException {

		/**
		 * Constructor.
		 * @param cause the exception that caused this exception
		 */
		public WrappedException(Throwable cause) {
			super(cause);
		}

	}
}
