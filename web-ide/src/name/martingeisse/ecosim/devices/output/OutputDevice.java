/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import name.martingeisse.ecosim.bus.AbstractPeripheralDevice;
import name.martingeisse.ecosim.bus.BusTimeoutException;
import name.martingeisse.ecosim.bus.IInterruptLine;

/**
 * A simple device that writes the lowest 8 bits of each
 * word-sized bus write to a file on the simulator host system
 * or any other output stream.
 */
public class OutputDevice extends AbstractPeripheralDevice {

	/**
	 * the outputStream
	 */
	private OutputStream outputStream;
	
	/**
	 * the closeOnExit
	 */
	private boolean closeOnExit;
	
	/**
	 * Constructor
	 * @param outputStream the output stream to write to
	 * @param closeOnExit whether this device should close the output
	 * stream upon simulator exit.
	 */
	public OutputDevice(OutputStream outputStream, boolean closeOnExit) {
		this.outputStream = outputStream;
		this.closeOnExit = closeOnExit;
	}
	
	/**
	 * Constructor
	 * @param file the file to write to. Any existing file will be overwritten.
	 */
	public OutputDevice(File file) {
		this(open(file), true);
	}
	
	/**
	 * Opens a file and converts any IO exception to a runtime exception.
	 * @param file the file to open
	 * @return Returns a {@link FileOutputStream} for the specified file.
	 */
	private static FileOutputStream open(File file) {
		try {
			return new FileOutputStream(file);
		} catch (IOException e) {
			throw new RuntimeException("problem while opening the file for the OutputDevice", e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#readWord(int)
	 */
	@Override
	protected int readWord(int localAddress) throws BusTimeoutException {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#writeWord(int, int)
	 */
	@Override
	protected void writeWord(int localAddress, int value) throws BusTimeoutException {
		try {
			outputStream.write(value);
		} catch (IOException e) {
			throw new RuntimeException("problem while writing a byte to the OutputDevice", e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#connectInterruptLines(name.martingeisse.ecotools.simulator.bus.IInterruptLine[])
	 */
	@Override
	public void connectInterruptLines(IInterruptLine[] interruptLines) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#getInterruptLineCount()
	 */
	@Override
	public int getInterruptLineCount() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#getLocalAddressBitCount()
	 */
	@Override
	public int getLocalAddressBitCount() {
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#dispose()
	 */
	@Override
	public void dispose() {
		if (closeOnExit) {
			try {
				outputStream.close();
			} catch (IOException e) {
				throw new RuntimeException("problem while closing the OutputDevice", e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.timer.ITickable#tick()
	 */
	@Override
	public void tick() {
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "output device";
	}

}
