/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.startmenu;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.launcher.assets.LauncherAssets;
import name.martingeisse.miner.Main;
import name.martingeisse.miner.account.AccountApiClient;
import name.martingeisse.miner.common.Faction;
import name.martingeisse.miner.ingame.IngameHandler;
import name.martingeisse.stackd.client.frame.handlers.ExitHandler;
import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.control.Page;
import name.martingeisse.stackd.client.gui.element.FillTexture;
import name.martingeisse.stackd.client.gui.element.Margin;
import name.martingeisse.stackd.client.gui.element.Spacer;
import name.martingeisse.stackd.client.gui.element.VerticalLayout;
import name.martingeisse.stackd.client.system.StackdTexture;
import org.lwjgl.input.Mouse;

/**
 * The "choose your character" menu page.
 */
public class ChooseCharacterPage extends Page {

	/**
	 * the exitHandler
	 */
	private final ExitHandler exitHandler;

	/**
	 * Constructor.
	 * @param exitHandler the exit handler
	 */
	public ChooseCharacterPage(final ExitHandler exitHandler) {
		this.exitHandler = exitHandler;
		final VerticalLayout menu = new VerticalLayout();
		
		JsonAnalyzer json = AccountApiClient.getInstance().fetchPlayers();
		json = json.analyzeMapElement("players");
		int playerIndex = 0;
		for (JsonAnalyzer playerJson : json.analyzeList()) {
			String playerName = playerJson.analyzeMapElement("name").expectString();
			int playerFactionIndex = playerJson.analyzeMapElement("faction").expectInteger();
			String playerFactionName = Faction.values()[playerFactionIndex].getDisplayName();
			final int thisPlayerIndex = playerIndex;
			menu.addElement(new StartmenuButton(playerName + " (" + playerFactionName + ")") {
				@Override
				protected void onClick() {
					System.out.println("selected player #" + thisPlayerIndex);
					// TODO 
					System.out.println("TODO actually load this player");
					try {
						Main.frameLoop.getRootHandler().setWrappedHandler(new IngameHandler());
						Mouse.setGrabbed(true);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
			playerIndex++;
			menu.addElement(new Spacer(2 * Gui.GRID));
		}
		
		menu.addElement(new StartmenuButton("Create Character") {
			@Override
			protected void onClick() {
				getGui().setRootElement(new ChooseFactionPage(exitHandler));
			}
		});
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(new StartmenuButton("Quit") {
			@Override
			protected void onClick() {
				exitHandler.setProgrammaticExit(true);
			}
		});
		StackdTexture backgroundTexture = new StackdTexture(LauncherAssets.class, "dirt.png", false);
		initializePage(new FillTexture(backgroundTexture), new Margin(menu, 30 * Gui.GRID, 30 * Gui.GRID));
	}

}
