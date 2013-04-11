/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.disk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import name.martingeisse.ecosim.bus.AbstractPeripheralDevice;
import name.martingeisse.ecosim.bus.BusTimeoutException;
import name.martingeisse.ecosim.bus.IInterruptLine;
import name.martingeisse.ecosim.util.AbstractValueTransportDelay;
import name.martingeisse.ecosim.util.BufferTools;

/**
 * Disk simulation which is backed by a file on the host file system.
 * The disk must be created from an existing file. The size of that
 * file must be divisible by the sector size and determines the
 * disk size.
 * 
 * TODO: The disk does not currently simulate physical errors, only
 * bad parameters. This disk does not correctly handle modifications
 * to the registers while an operation is in progress.
 */
public class Disk extends AbstractPeripheralDevice {

	/**
	 * the sector size in bytes
	 */
	public static final int SECTOR_SIZE = 512;
	
	/**
	 * the buffer size in sectors
	 */
	public static final int BUFFER_SIZE = 8;

	/**
	 * the delay for disk operations in ticks (currently a constant, independent of the sector count)
	 */
	public static final int DISK_DELAY = 100;

	/**
	 * the file that backs the simulated disk
	 */
	private File file;

	/**
	 * the RandomAccessFile object used to access the backing file
	 */
	private RandomAccessFile randomAccessFile;

	/**
	 * the simulated disk controller buffer
	 */
	private byte[] buffer;

	/**
	 * the interruptEnable field of the control register
	 */
	private boolean interruptEnable;

	/**
	 * the write field of the control register
	 */
	private boolean write;

	/**
	 * the error field of the control register
	 */
	private boolean error;

	/**
	 * the done field of the control register (command done)
	 */
	private boolean done;

	/**
	 * the ready field of the control register (disk initialized)
	 */
	private boolean ready;

	/**
	 * the sector count register
	 */
	private int sectorCount;

	/**
	 * the sector register
	 */
	private int sector;

	/**
	 * the capacity register
	 */
	private int capacity;

	/**
	 * the interrupt line
	 */
	private IInterruptLine interruptLine;

	/**
	 * the delay timer
	 */
	private MyDelayTimer delayTimer;

	/**
	 * Constructor
	 * @param file the file that backs this disk
	 * @throws IOException on host I/O errors
	 */
	public Disk(File file) throws IOException {

		// make sure the file exists
		if (!file.exists()) {
			String path;
			try {
				path = file.getCanonicalPath();
			} catch (IOException e) {
				path = file.getPath();
			}
			throw new FileNotFoundException("Disk image file not found: " + path);
		}
		
		this.file = file;
		this.randomAccessFile = new RandomAccessFile(file, "rwd");
		this.buffer = new byte[BUFFER_SIZE * SECTOR_SIZE];
		this.interruptEnable = false;
		this.write = false;
		this.error = false;
		this.done = false;
		this.ready = false;
		this.sectorCount = 0;
		this.sector = 0;
		this.capacity = 0;
		this.interruptLine = null;
		this.delayTimer = new MyDelayTimer();
		delayTimer.send(null);
	}

	/**
	 * 
	 */
	private void updateInterrupt() {
		interruptLine.setActive(done & interruptEnable);
	}

	/**
	 * @return Returns the interruptEnable.
	 */
	public boolean isInterruptEnable() {
		return interruptEnable;
	}

	/**
	 * Sets the interruptEnable.
	 * @param interruptEnable the new value to set
	 */
	public void setInterruptEnable(boolean interruptEnable) {
		this.interruptEnable = interruptEnable;
		updateInterrupt();
	}

	/**
	 * @return Returns the write.
	 */
	public boolean isWrite() {
		return write;
	}

	/**
	 * Sets the write.
	 * @param write the new value to set
	 */
	public void setWrite(boolean write) {
		this.write = write;
	}

	/**
	 * @return Returns the error.
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * Sets the error.
	 * @param error the new value to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * @return Returns the done.
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * Sets the done.
	 * @param done the new value to set
	 */
	public void setDone(boolean done) {
		this.done = done;
		updateInterrupt();
	}

	/**
	 * @return Returns the sectorCount.
	 */
	public int getSectorCount() {
		return sectorCount;
	}

	/**
	 * Sets the sectorCount.
	 * @param sectorCount the new value to set
	 */
	public void setSectorCount(int sectorCount) {
		this.sectorCount = sectorCount;
	}

	/**
	 * @return Returns the sector.
	 */
	public int getSector() {
		return sector;
	}

	/**
	 * Sets the sector.
	 * @param sector the new value to set
	 */
	public void setSector(int sector) {
		this.sector = sector;
	}

	/**
	 * @return Returns the file.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @return Returns the randomAccessFile.
	 */
	public RandomAccessFile getRandomAccessFile() {
		return randomAccessFile;
	}

	/**
	 * @return Returns the buffer.
	 */
	public byte[] getBuffer() {
		return buffer;
	}

	/**
	 * @return Returns the ready.
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * @return Returns the capacity.
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * @return Returns the value of the control register as if read by the CPU
	 */
	public int getControlRegisterValue() {
		int result = 0;
		result |= (delayTimer.isActive() ? 1 : 0);
		result |= (interruptEnable ? 2 : 0);
		result |= (write ? 4 : 0);
		result |= (error ? 8 : 0);
		result |= (done ? 16 : 0);
		result |= (ready ? 32 : 0);
		return result;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#readWord(int)
	 */
	@Override
	protected int readWord(int localAddress) throws BusTimeoutException {
		if ((localAddress & 0x80000) == 0) {
			if ((localAddress & 8) == 0) {
				if ((localAddress & 4) == 0) {
					return getControlRegisterValue();
				} else {
					return sectorCount;
				}
			} else {
				if ((localAddress & 4) == 0) {
					return sector;
				} else {
					return capacity;
				}
			}
		} else {
			int bufferAddress = localAddress % buffer.length;
			return BufferTools.readBigEndian32(buffer, bufferAddress);
		}
	}

	/**
	 * Sets the value of the control register as if written by the CPU.
	 * This method may start a disk operation if bit 0 is set.
	 * @param value the value to set.
	 */
	public void setControlRegisterValue(int value) {
		
		/** these values are always taken from the bus **/
		boolean start = (value & 1) != 0;
		interruptEnable = (value & 2) != 0;
		write = (value & 4) != 0;
		
		/** when starting, some values are overwritten, otherwise they too are taken from the bus **/
		if (start) {
			if (!ready) {
				error = true;
			} else if (delayTimer.isActive()) {
				error = true;
				done = true;
				delayTimer.cancel();
			} else {
				error = false;
				done = false;
				delayTimer.send(null);
			}
		} else {
			error = (value & 8) != 0;
			done = (value & 16) != 0;
		}
		updateInterrupt();
		
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#writeWord(int, int)
	 */
	@Override
	protected void writeWord(int localAddress, int value) throws BusTimeoutException {
		if ((localAddress & 0x80000) == 0) {
			if ((localAddress & 8) == 0) {
				if ((localAddress & 4) == 0) {
					setControlRegisterValue(value);
				} else {
					sectorCount = value;
				}
			} else {
				if ((localAddress & 4) == 0) {
					sector = value;
				} else {
					throw new BusTimeoutException("trying to write to disk capacity register");
				}
			}
		} else {
			int bufferAddress = localAddress % buffer.length;
			BufferTools.writeBigEndian32(buffer, bufferAddress, value);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#connectInterruptLines(name.martingeisse.ecotools.simulator.bus.IInterruptLine[])
	 */
	@Override
	public void connectInterruptLines(IInterruptLine[] interruptLines) {
		this.interruptLine = interruptLines[0];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#getInterruptLineCount()
	 */
	@Override
	public int getInterruptLineCount() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#getLocalAddressBitCount()
	 */
	@Override
	public int getLocalAddressBitCount() {
		return 20;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#dispose()
	 */
	@Override
	public void dispose() {
		try {
			randomAccessFile.close();
		} catch (IOException e) {
			throw new RuntimeException("problem while closing the simulated disk", e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.timer.ITickable#tick()
	 */
	@Override
	public void tick() {
		delayTimer.tick();
	}
	
	/**
	 * 
	 */
	private class MyDelayTimer extends AbstractValueTransportDelay<Void> {

		/**
		 * Constructor
		 */
		public MyDelayTimer() {
			super(DISK_DELAY);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.AbstractValueTransportDelay#onArrive(java.lang.Object)
		 */
		@Override
		protected void onArrive(Void value) {

			/** handle disk initialization **/
			if (!ready) {
				ready = true;
				try {
					long longCapacity = randomAccessFile.length();
					capacity = (int)longCapacity;
					if (capacity != longCapacity) {
						capacity = 0;
					}
				} catch (IOException e) {
					capacity = 0;
				}
				return;
			}

			/** any host I/O error is translated to an error of the simulated disk **/
			int fileStartPosition = sector * SECTOR_SIZE;
			int fileByteCount = sectorCount * SECTOR_SIZE;
			long fileLength = -12345;
			try {

				/** determine file length **/
				fileLength = randomAccessFile.length();
				
				/** sanity checks **/
				if (sectorCount > BUFFER_SIZE || fileStartPosition < 0 || fileByteCount < 0 || fileStartPosition + fileByteCount > fileLength) {
					throw new IOException();
				}
				randomAccessFile.seek(fileStartPosition);

				/** handle the disk operation **/
				done = true;
				error = false;
				if (write) {
					randomAccessFile.write(buffer, 0, fileByteCount);
				} else {
					randomAccessFile.readFully(buffer, 0, fileByteCount);
				}
				updateInterrupt();

			} catch (IOException e) {
				
				/** I/O error **/
				done = true;
				error = true;
				updateInterrupt();
				
			} catch (IndexOutOfBoundsException e) {
				
				/** this should not happen, since start / length parameters are checked above **/
				throw new RuntimeException("disk access bounds violation; file: " + file.getAbsolutePath() +
						", sector: " + sector + ", sectorCount: " + sectorCount +
						", fileStartPosition: " + fileStartPosition + ", fileByteCount: " + fileByteCount +
						", file length: " + fileLength);
				
			}

		}

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "disk";
	}
}
