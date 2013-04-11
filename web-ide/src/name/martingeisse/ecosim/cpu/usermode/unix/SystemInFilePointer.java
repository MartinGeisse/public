/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode.unix;

import java.io.IOException;

import name.martingeisse.ecosim.util.ByteQueue;

/**
 * A nonblocking {@link IFilePointer} implementation for System.in.
 * 
 * The write, ioctl and seek operations of this class do nothing.
 */
public class SystemInFilePointer extends ReadingPipeFilePointer {

	/**
	 * Constructor.
	 */
	public SystemInFilePointer() {
		super(new ByteQueue());
		final ByteQueue queue = getQueue();

		// create a thread that fills the queue
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final byte[] buffer = new byte[4096];
					while (true) {
						
						// check if the reader stopped
						boolean readerStopped;
						synchronized(queue) {
							readerStopped = queue.isReaderStopped();
						}
						if (readerStopped) {
							break;
						}
						
						// transfer a block of data
						final int n = System.in.read(buffer);
						if (n < 0) {
							break;
						}
						byte[] copy = new byte[n];
						System.arraycopy(buffer, 0, copy, 0, n);
						synchronized (queue) {
							queue.writeNoCopy(copy);
						}
						
					}
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
				synchronized (queue) {
					queue.notifyEof();
				}
			}
		}).start();
	}

}
