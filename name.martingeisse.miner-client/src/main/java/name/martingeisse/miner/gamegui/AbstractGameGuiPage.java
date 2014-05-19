/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.gamegui;

import name.martingeisse.miner.util.UserVisibleMessageException;
import name.martingeisse.stackd.client.gui.GuiEvent;
import name.martingeisse.stackd.client.gui.control.MessageBox;
import name.martingeisse.stackd.client.gui.control.Page;

import org.lwjgl.input.Keyboard;

/**
 * The base class for start menu pages.
 */
public class AbstractGameGuiPage extends Page {

	/**
	 * Constructor.
	 */
	public AbstractGameGuiPage() {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.control.Page#onException(java.lang.Throwable)
	 */
	@Override
	protected void onException(Throwable t) {
		if (t instanceof UserVisibleMessageException) {
			new MessageBox(t.getMessage()).show(this);
		} else {
			super.onException(t);
			new MessageBox("An error has occurred.").show(this);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.control.Page#handlePageEvent(name.martingeisse.stackd.client.gui.GuiEvent)
	 */
	@Override
	protected void handlePageEvent(GuiEvent event) {
		super.handlePageEvent(event);
		if (event == GuiEvent.KEY_PRESSED && Keyboard.getEventCharacter() == '\r') {
			onEnterPressed();
		}
	}

	/**
	 * Called when the user presses the "enter" key.
	 */
	protected void onEnterPressed() {
	}
	
}
