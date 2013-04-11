/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode;


/**
 * This class reads a zero-terminated ASCII string from memory.
 */
public class ReadStringAction extends AbstractSparseMemoryBulkAction {

	/**
	 * the builder
	 */
	private StringBuilder builder = new StringBuilder();

	/**
	 * Constructor.
	 * @param memory the memory to operate on
	 */
	public ReadStringAction(SparseMemory memory) {
		super(memory);
		
	}
	
	/**
	 * Constructor.
	 * @param memory the memory to operate on
	 * @param autoAllocate whether to auto-allocate pages
	 */
	public ReadStringAction(SparseMemory memory, boolean autoAllocate) {
		super(memory, autoAllocate);
	}

	/**
	 * Executes this action.
	 * @param startAddress the address to read the string from
	 */
	public void execute(int startAddress) {
		try {
			executeWrapExceptions(startAddress, Integer.MAX_VALUE);
		} catch (WrappedException e) {
			if (e.getCause() instanceof FinishedException) {
				return;
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
		for (int i=0; i<chunkCount; i++) {
			byte b = page[chunkStart + i];
			if (b == 0) {
				throw new FinishedException();
			}
			char c = (char)(b & 127); // ASCII decoding
			builder.append(c);
		}
	}
	
	/**
	 * This method must be used in between if this action is used more than once.
	 */
	public void reset() {
		builder.setLength(0);
	}
	
	/**
	 * @return the string read from memory
	 */
	public String getResult() {
		return builder.toString();
	}
	
	/**
	 * This exception type interrupts reading a string when the end of the
	 * string has been encountered.
	 */
	public static class FinishedException extends Exception {

		/**
		 * Constructor.
		 */
		public FinishedException() {
			super();
		}

		/**
		 * Constructor.
		 * @param message the exception message
		 */
		public FinishedException(String message) {
			super(message);
		}

		/**
		 * Constructor.
		 * @param cause the exception that caused this exception
		 */
		public FinishedException(Throwable cause) {
			super(cause);
		}

		/**
		 * Constructor.
		 * @param message the exception message
		 * @param cause the exception that caused this exception
		 */
		public FinishedException(String message, Throwable cause) {
			super(message, cause);
		}

	}
}
