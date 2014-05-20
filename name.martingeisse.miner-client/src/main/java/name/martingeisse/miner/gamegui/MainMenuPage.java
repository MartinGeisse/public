/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.gamegui;

import org.lwjgl.input.Mouse;

import name.martingeisse.miner.ingame.IngameHandler;
import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.element.Margin;
import name.martingeisse.stackd.client.gui.element.Spacer;
import name.martingeisse.stackd.client.gui.element.VerticalLayout;
import name.martingeisse.stackd.client.util.MouseUtil;

/**
 * The "login" menu page.
 */
public class MainMenuPage extends AbstractGameGuiPage {

	/**
	 * Constructor.
	 */
	public MainMenuPage() {
		final VerticalLayout menu = new VerticalLayout();
		menu.addElement(new GameGuiButton("Resume Game") {
			@Override
			protected void onClick() {
				IngameHandler.gameMenuHandlerWrapper.setWrappedHandler(null);
				MouseUtil.grab();
			}
		});
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(new GameGuiButton("Quit") {
			@Override
			protected void onClick() {
				GameMenuHandler.programmaticExit = true;
			}
		});
		initializePage(null, new Margin(menu, 30 * Gui.GRID, 30 * Gui.GRID));
	}

}
