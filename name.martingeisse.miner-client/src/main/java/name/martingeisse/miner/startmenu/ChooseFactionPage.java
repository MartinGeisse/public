/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.startmenu;

import name.martingeisse.launcher.assets.LauncherAssets;
import name.martingeisse.miner.common.Faction;
import name.martingeisse.stackd.client.frame.handlers.ExitHandler;
import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.control.Page;
import name.martingeisse.stackd.client.gui.element.FillTexture;
import name.martingeisse.stackd.client.gui.element.Margin;
import name.martingeisse.stackd.client.gui.element.Spacer;
import name.martingeisse.stackd.client.gui.element.VerticalLayout;
import name.martingeisse.stackd.client.system.StackdTexture;

/**
 * The "choose your faction" menu page.
 */
public class ChooseFactionPage extends Page {

	/**
	 * the exitHandler
	 */
	private final ExitHandler exitHandler;

	/**
	 * Constructor.
	 * @param exitHandler the exit handler
	 */
	public ChooseFactionPage(final ExitHandler exitHandler) {
		this.exitHandler = exitHandler;
		final VerticalLayout menu = new VerticalLayout();
		
		for (final Faction faction : Faction.values()) {
			menu.addElement(new StartmenuButton(faction.getDisplayName()) {
				@Override
				protected void onClick() {
					System.out.println("selected faction #" + faction.ordinal());
					// TODO 
					System.out.println("TODO actually use this faction");
				}
			});
			menu.addElement(new Spacer(2 * Gui.GRID));
		}
		
		menu.addElement(new StartmenuButton("Back") {
			@Override
			protected void onClick() {
				getGui().setRootElement(new ChooseCharacterPage(exitHandler));
			}
		});
		StackdTexture backgroundTexture = new StackdTexture(LauncherAssets.class, "dirt.png", false);
		initializePage(new FillTexture(backgroundTexture), new Margin(menu, 30 * Gui.GRID, 30 * Gui.GRID));
	}

}
