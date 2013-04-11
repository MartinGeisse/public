/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.util;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A FIFO byte queue.
 * 
 * The queue can be closed both by the reader and the writer. Closing by the writer
 * is indicated by a flag that can be obtained by the reader. Note that the reader
 * will want to obtain the remaining data too before considering the queue finished.
 * Closing by the reader is handled by dropping all internal data structures, since
 * this means the data written is going nowhere.
 */
public class ByteQueue {

	/**
	 * the queue
	 */
	private Queue<byte[]> queue;
	
	/**
	 * the currentBuffer
	 */
	private byte[] currentBuffer;
	
	/**
	 * the readPointer
	 */
	private int readPointer;
	
	/**
	 * the eof
	 */
	private boolean eof;

	/**
	 * Constructor.
	 */
	public ByteQueue() {
		this.queue = new LinkedList<byte[]>();
		this.currentBuffer = null;
		this.readPointer = 0;
		this.eof = false;
	}
	
	/**
	 * Getter method for the eof.
	 * @return the eof
	 */
	public boolean isEof() {
		return eof;
	}
	
	/**
	 * Notifies this queue that the writer has reached the end
	 * of the data.
	 */
	public void notifyEof() {
		eof = true;
	}
	
	/**
	 * Notifies this queue that the reader has stopped reading.
	 * This will cause all data to be discarded and new data
	 * to be ignored.
	 */
	public void notifyReaderStopped() {
		this.queue = null;
		this.currentBuffer = null;
	}
	
	/**
	 * Checks if the reader has notified this queue that it has stopped.
	 * @return true if the reader has stopped, false if not
	 */
	public boolean isReaderStopped() {
		return (queue == null);
	}

	/**
	 * Reads up to (count) bytes, or as many as available, and stores
	 * them in the specified destination array, starting at index
	 * (start). This method may read less than (count) bytes if not
	 * enough data is available.
	 * 
	 * @param destination the array to store data in
	 * @param start the first index to read to
	 * @param count the number of bytes to read
	 * @return the number of bytes actually read
	 */
	public int read(byte[] destination, int start, int count) {
		
		// if the reader has notified us that it stopped, it must not read
		if (queue == null) {
			throw new IllegalStateException("Reading from a queue although reading has stopped");
		}
		
		// loop over data buffers
		int read = 0;
		while (true) {
			
			// make sure we have a current buffer
			if (currentBuffer == null) {
				currentBuffer = queue.poll();
				readPointer = 0;
				if (currentBuffer == null) {
					return read;
				}
			}
			
			// check if the current buffer can satisfy the request
			if (readPointer + count <= currentBuffer.length) {
				System.arraycopy(currentBuffer, readPointer, destination, start, count);
				readPointer += count;
				read += count;
				return read;
			}
			
			// read the current buffer, then loop
			int n = currentBuffer.length - readPointer;
			System.arraycopy(currentBuffer, readPointer, destination, start, n);
			read += n;
			start += n;
			count -= n;
			currentBuffer = null;
			readPointer = 0;
			
		}
	}
	
	/**
	 * Writes the specified data to the queue. The array is not copied
	 * and should not be changed after calling this method.
	 * @param data the data to write
	 */
	public void writeNoCopy(byte[] data) {
		checkWriteEof();
		if (queue != null) {
			queue.offer(data);
		}
	}
	
	/**
	 * Writes the specified data to the queue, copying the array first such
	 * that later modification has no effect on the queue contents.
	 * @param data the data to write
	 */
	public void writeCopy(byte[] data) {
		checkWriteEof();
		if (queue != null) {
			writeNoCopy(data.clone());
		}
	}

	/**
	 * This method is called when data is written and ensures that EOF
	 * has not been signalled.
	 */
	private void checkWriteEof() {
		if (eof) {
			throw new IllegalStateException("Writing data although EOF has been signalled");
		}
	}
	
}
