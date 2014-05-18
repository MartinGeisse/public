/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.startmenu;

import name.martingeisse.stackd.client.gui.control.Button;
import name.martingeisse.stackd.client.gui.element.FillColor;
import name.martingeisse.stackd.client.gui.util.Color;

/**
 * A button styled for the start menu.
 */
public abstract class StartmenuButton extends Button {

	/**
	 * Constructor.
	 * @param label the button label
	 */
	public StartmenuButton(String label) {
		this(label, false);
	}
	
	/**
	 * Constructor.
	 * @param label the button label
	 * @param big whether the button is big or normal.sized
	 */
	public StartmenuButton(String label, boolean big) {
		super(false, big);
		getTextLine().setText(label);
		setBackgroundElement(new FillColor(Color.BLUE));
		addPulseEffect(new Color(255, 255, 255, 64));
	}
	
}
