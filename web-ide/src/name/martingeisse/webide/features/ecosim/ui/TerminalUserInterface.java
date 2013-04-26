/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim.ui;

import java.io.Serializable;

import name.martingeisse.ecosim.devices.terminal.ITerminalUserInterface;
import name.martingeisse.webide.features.ecosim.EcosimEvents;
import name.martingeisse.webide.ipc.IIpcEventOutbox;
import name.martingeisse.webide.ipc.IpcEvent;

/**
 * Dummy implementation for {@link ITerminalUserInterface}.
 */
public class TerminalUserInterface implements ITerminalUserInterface, Serializable {

	/**
	 * the outputBuilder
	 */
	private final StringBuilder outputBuilder = new StringBuilder();

	/**
	 * the eventOutbox
	 */
	private final IIpcEventOutbox eventOutbox;

	/**
	 * Constructor.
	 * @param eventOutbox the event outbox
	 */
	public TerminalUserInterface(final IIpcEventOutbox eventOutbox) {
		this.eventOutbox = eventOutbox;
	}

	/**
	 * Sends a character to the output buffer and informs listeners.
	 * @param c the character
	 */
	public void sendCharacter(char c) {
		outputBuilder.append(c);
		eventOutbox.sendEvent(new IpcEvent(EcosimEvents.TERMINAL_OUTPUT, this, getOutput()));
	}
	
	/**
	 * Clears all previous output.
	 */
	public void clearOutput() {
		outputBuilder.setLength(0);
	}

	/**
	 * Returns the buffered output.
	 * @return the output
	 */
	public String getOutput() {
		return outputBuilder.toString();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.terminal.ITerminalUserInterface#sendByte(byte)
	 */
	@Override
	public void sendByte(final byte b) {
		sendCharacter((char)b);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecosim.devices.terminal.ITerminalUserInterface#sendCorruptedByte()
	 */
	@Override
	public void sendCorruptedByte() {
		sendCharacter('?');
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

}
