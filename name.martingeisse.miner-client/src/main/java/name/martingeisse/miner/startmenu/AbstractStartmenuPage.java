/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.startmenu;

import name.martingeisse.launcher.assets.LauncherAssets;
import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.control.Page;
import name.martingeisse.stackd.client.gui.element.FillTexture;
import name.martingeisse.stackd.client.gui.element.Margin;
import name.martingeisse.stackd.client.system.StackdTexture;

/**
 * The base class for start menu pages.
 */
public class AbstractStartmenuPage extends Page {

	/**
	 * the EXIT_BUTTON
	 */
	protected static final StartmenuButton EXIT_BUTTON = new StartmenuButton("Quit") {
		@Override
		protected void onClick() {
			StartmenuHandler.programmaticExit = true;
		}
	};
	
	/**
	 * Constructor.
	 */
	public AbstractStartmenuPage() {
	}

	/**
	 * 
	 */
	protected final void initializeStartmenuPage(GuiElement mainElement) {
		StackdTexture backgroundTexture = new StackdTexture(LauncherAssets.class, "dirt.png", false);
		initializePage(new FillTexture(backgroundTexture), new Margin(mainElement, 30 * Gui.GRID, 30 * Gui.GRID));
	}
	
}
