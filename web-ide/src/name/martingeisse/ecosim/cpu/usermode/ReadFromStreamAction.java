/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode;

import java.io.IOException;
import java.io.InputStream;


/**
 * This action reads from an {@link InputStream} into the memory of a
 * usermode process.
 * 
 * This class throws an IOException if unexpected end-of-file is
 * encountered.
 */
public class ReadFromStreamAction extends AbstractSparseMemoryBulkAction {

	/**
	 * the stream
	 */
	private final InputStream stream;

	/**
	 * Constructor.
	 * @param stream the stream to read from
	 * @param memory the memory to operate on
	 */
	public ReadFromStreamAction(InputStream stream, SparseMemory memory) {
		super(memory);
		this.stream = stream;
	}
	
	/**
	 * Constructor.
	 * @param stream the stream to read from
	 * @param memory the memory to operate on
	 * @param autoAllocate whether to auto-allocate pages
	 */
	public ReadFromStreamAction(InputStream stream, SparseMemory memory, boolean autoAllocate) {
		super(memory, autoAllocate);
		this.stream = stream;
	}

	/**
	 * Executes this action.
	 * @param baseAddress the first address to operate on
	 * @param count the number of bytes to operate on
	 * @throws IOException on I/O errors
	 */
	public void execute(int baseAddress, int count) throws IOException {
		try {
			executeWrapExceptions(baseAddress, count);
		} catch (WrappedException e) {
			Throwable cause = e.getCause();
			if (cause instanceof IOException) {
				throw (IOException)cause;
			} else if (cause instanceof RuntimeException) {
				throw (RuntimeException)cause;
			} else {
				throw e;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.AbstractSparseMemoryBulkAction#handleChunk(byte[], int, int, int, int)
	 */
	@Override
	protected void handleChunk(byte[] page, int pageBaseAddress, int operationBaseAddress, int chunkStart, int chunkCount) throws Exception {
		readFully(stream, page, chunkStart, chunkCount);
	}

	/**
	 * This method works similar to FileInputStream.read(), but will call
	 * read() repeatedly until (count) bytes have been read and will throw
	 * an {@link IOException} if end-of-file is reached before that.
	 */
	private void readFully(final InputStream in, final byte[] destination, final int offset, int count) throws IOException {
		while (count > 0) {
			final int n = in.read(destination, offset, count);
			if (n == -1) {
				throw new IOException("unexpected end-of-file");
			}
			count -= n;
		}
	}
	
}
