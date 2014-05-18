/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.startmenu;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.miner.account.AccountApiClient;
import name.martingeisse.miner.common.Faction;
import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.element.Spacer;
import name.martingeisse.stackd.client.gui.element.VerticalLayout;

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
		for (final JsonAnalyzer playerJson : json.analyzeList()) {
			final long playerId = playerJson.analyzeMapElement("id").expectLong();
			final String playerName = playerJson.analyzeMapElement("name").expectString();
			final int playerFactionIndex = playerJson.analyzeMapElement("faction").expectInteger();
			final String playerFactionName = Faction.values()[playerFactionIndex].getDisplayName();
			menu.addElement(new StartmenuButton(playerName + " (" + playerFactionName + ")") {
				@Override
				protected void onClick() {
					getGui().setRootElement(new PlayerDetailsPage(playerId));
				}
			});
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
