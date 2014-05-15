/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.startmenu;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.miner.Main;
import name.martingeisse.miner.account.AccountApiClient;
import name.martingeisse.miner.common.Faction;
import name.martingeisse.miner.ingame.IngameHandler;
import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.element.Spacer;
import name.martingeisse.stackd.client.gui.element.VerticalLayout;
import org.lwjgl.input.Mouse;

/**
 * The "choose your character" menu page.
 */
public class ChooseCharacterPage extends AbstractStartmenuPage {

	/**
	 * Constructor.
	 */
	public ChooseCharacterPage() {
		final VerticalLayout menu = new VerticalLayout();
		
		// fetch players
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
		
		// build the remaining menu
		menu.addElement(new StartmenuButton("Create Character") {
			@Override
			protected void onClick() {
				getGui().setRootElement(new ChooseFactionPage());
			}
		});
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(EXIT_BUTTON);
		initializeStartmenuPage(menu);
		
	}

}
