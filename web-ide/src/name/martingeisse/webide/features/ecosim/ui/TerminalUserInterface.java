/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim.ui;

import java.io.Serializable;

import name.martingeisse.ecosim.devices.terminal.ITerminalUserInterface;

/**
 * Dummy implementation for {@link ITerminalUserInterface}.
 */
public class TerminalUserInterface implements ITerminalUserInterface, Serializable {

	/**
	 * the outputBuilder
	 */
	private StringBuilder outputBuilder = new StringBuilder();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.terminal.ITerminalUserInterface#sendByte(byte)
	 */
	@Override
	public void sendByte(final byte b) {
		outputBuilder.append((char)b);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.terminal.ITerminalUserInterface#sendCorruptedByte()
	 */
	@Override
	public void sendCorruptedByte() {
		outputBuilder.append('?');
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.terminal.ITerminalUserInterface#hasInput()
	 */
	@Override
	public boolean hasInput() {
		return false;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.terminal.ITerminalUserInterface#receiveByte()
	 */
	@Override
	public byte receiveByte() throws IllegalStateException {
		return 0;
	}

	/**
	 * Returns the buffered output.
	 * @return the output
	 */
	public String getOutput() {
		return outputBuilder.toString();
	}
	
}
